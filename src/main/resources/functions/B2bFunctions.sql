-- Use this table for viewing the entire B2bRequestDetails
DROP TABLE IF EXISTS "B2bRequestDetails";
DROP TABLE IF EXISTS "B2bRequestStatus";


CREATE TABLE IF NOT EXISTS "B2bRequestDetails"
(
    "B2bRequestId"      BIGSERIAL PRIMARY KEY,
    "CreatedOn"         TIMESTAMP,
    "B2bUserDetailId"   INT,
    "B2bLocationId"     INT,
    "ItemId"            INT,
    "ItemCount"         BIGINT,
    "UnitPriceForAItem" NUMERIC(10, 2),
    "RequestStatus"     INT,
    "CancelReason"      CHARACTER VARYING
);

CREATE TABLE IF NOT EXISTS "B2bRequestStatus"
(
    "B2bStatusId"          BIGSERIAL PRIMARY KEY,
    "B2bRequestStatusName" CHARACTER VARYING
);

CREATE TABLE IF NOT EXISTS "RequestProgress"
(
    "RequestProgressId"  BIGSERIAL,
    "B2bRequestId"       INT,
    "RequestedItemCount" BIGINT,
    "RemainingItemCount" BIGINT
);

CREATE OR REPLACE FUNCTION insert_request_progress(
    IN pB2bRequestId INT,
    IN pRequestedItemCount BIGINT,
    IN pRemainingItemCount BIGINT,
    OUT rRequestProgressId BIGINT
)
    LANGUAGE plpgsql
AS
'
    BEGIN
        INSERT INTO "RequestProgress" (
            "B2bRequestId",
            "RequestedItemCount",
            "RemainingItemCount"
        ) VALUES (
                     pB2bRequestId,
                     pRequestedItemCount,
                     pRemainingItemCount
                 )
        RETURNING "RequestProgressId" INTO rRequestProgressId;
    END;
';

create or replace function bulk_request_form_b2b(puserdetailid integer, plocationid integer, pitemid integer, pitemcount bigint, punitpriceforaitem numeric)
    returns TABLE("rSellerDetails" integer, "rB2bRequestId" integer, "rRequestStatus" integer, "rRequestingItemCount" bigint)
    language plpgsql
as
'

    DECLARE

        lUnitPriceForAItem                 Numeric(10, 2);
        lTotalItemCountForSpecificLocation BIGINT;
        lTotalCountForEntireLocation       BIGINT;
        lCreatedDateTime                   timestamp := current_timestamp;
        lB2bRequestID                      int;
        lUserDetailId                      INT;
        lProcessingCount                   BIGINT;
    BEGIN

        SELECT "MaxSellingUnitPriceForB2B"
        into lUnitPriceForAItem
        FROM "ItemsDetails"
        WHERE "ItemId" = pItemId;


        INSERT INTO "B2bRequestDetails"("CreatedOn", "B2bUserDetailId", "B2bLocationId", "ItemId", "ItemCount",
                                        "UnitPriceForAItem", "RequestStatus", "CancelReason")
        VALUES (lCreatedDateTime, pUserDetailId, pLocationId, pItemId, pItemCount, pUnitPriceForAItem, 7, null);

        SELECT "B2bRequestId"
        into lB2bRequestID
        FROM "B2bRequestDetails"
        where "CreatedOn" = lCreatedDateTime;



        SELECT SUM("ItemCount")
        into lProcessingCount
        FROM "B2bRequestDetails"
        WHERE "RequestStatus" in (1, 3)
          AND "ItemId" = pItemId;

        IF lProcessingCount IS NULL THEN
            lProcessingCount := 0;
        END IF;

        -- Check for item price if less then status number 3 for that get item value
        IF lUnitPriceForAItem >= pUnitPriceForAItem THEN
            UPDATE "B2bRequestDetails"
            SET "RequestStatus" = 2
            WHERE "B2bRequestId" = lB2bRequestID;
            RETURN QUERY SELECT NULL::INT,
                                lB2bRequestID,
                                2, NULL::BIGINT;
            RETURN;
        END IF;

        -- Check for availability in same location
        SELECT Sum("ItemCount"), "InsertedBy"
        into lTotalItemCountForSpecificLocation,lUserDetailId
        FROM "PurchaseDetails"
        WHERE "ItemId" = pItemId
          and "ExpiryDate"::DATE > "CreatedDate"::DATE
          AND "InventoryLocationId" = pLocationId
        GROUP BY "InsertedBy";

        IF SUM(lTotalItemCountForSpecificLocation) >= (pItemCount + lProcessingCount) THEN
            UPDATE "B2bRequestDetails"
            SET "RequestStatus" = 1
            WHERE "B2bRequestId" = lB2bRequestID;
            PERFORM  insert_request_progress(lB2bRequestID, pItemCount, pItemCount);
            return query SELECT lUserDetailId,
                                lB2bRequestID,
                                1,
                                pItemCount;
            RETURN;
        ELSE
            -- Check for availability around all location
            SELECT Sum("ItemCount"),
                   "InsertedBy"
            into lTotalCountForEntireLocation, lUserDetailId
            FROM "PurchaseDetails"
            WHERE "ItemId" = pItemId
              and "ExpiryDate"::DATE > "CreatedDate"::DATE
            GROUP BY "InsertedBy";
            IF SUM(lTotalCountForEntireLocation)  >= (pItemCount + lProcessingCount) THEN
                UPDATE "B2bRequestDetails"
                SET "RequestStatus" = 3
                WHERE "B2bRequestId" = lB2bRequestID;
                PERFORM  insert_request_progress(lB2bRequestID, pItemCount, pItemCount);
                RETURN QUERY SELECT lUserDetailId,
                                    lB2bRequestID,
                                    3,
                                    pItemCount;
                RETURN;
            ELSE
                UPDATE "B2bRequestDetails"
                SET "RequestStatus" = 7
                WHERE "B2bRequestId" = lB2bRequestID;
                PERFORM insert_request_progress(lB2bRequestID, pItemCount, pItemCount);
                RETURN QUERY SELECT NULL::INT,
                                    lB2bRequestID,
                                    7,
                                    pItemCount;
                RETURN;
            END IF;
        END IF;
    END;
';

CREATE OR REPLACE FUNCTION insert_b2b_request(
    IN pSellerDetails INT,
    IN pB2bRequestId INT,
    IN pRequestingItemCount BIGINT,
    OUT rRequestProgressUpdated VARCHAR
)
    LANGUAGE plpgsql
AS
'
    DECLARE
        lRequestProgressId   INT;
        lRequestStatus       INT;
        lRemainingCount      BIGINT;
        lLocationId          INT;
        lItemID              INT;
        lItemCount           BIGINT;
        lB2bUserId           INT;
        lUnitItemPrice       NUMERIC(10, 2);
        lTotalProcessedCount BIGINT := 0; -- To accumulate total item count inserted
    BEGIN

        SELECT COALESCE("RemainingItemCount", 0),
               "RequestProgressId"
        INTO lRemainingCount, lRequestProgressId
        FROM "RequestProgress"
        WHERE "B2bRequestId" = pB2bRequestId;

        SELECT "ItemId",
               "B2bUserDetailId",
               "UnitPriceForAItem",
               "RequestStatus"
        INTO lItemID, lB2bUserId, lUnitItemPrice, lRequestStatus
        FROM "B2bRequestDetails"
        WHERE "B2bRequestId" = pB2bRequestId;

        IF lRequestStatus IN (3, 5) AND lRemainingCount <> 0 THEN

            FOR lLocationId, lItemCount IN
                SELECT "InventoryLocationId",
                       "ItemCount"
                FROM "PurchaseDetails"
                WHERE "InsertedBy" = pSellerDetails
                LOOP
                    -- Call insert_sales_details for each inventory entry
                    SELECT "rDataUpdated"
                    INTO rRequestProgressUpdated
                    FROM insert_sales_details(
                            lItemID,
                            lItemCount,
                            lLocationId,
                            lItemCount::NUMERIC * lUnitItemPrice,
                            lB2bUserId,
                            true
                         );

                    -- Accumulate processed count
                    lTotalProcessedCount := lTotalProcessedCount + lItemCount;
                END LOOP;

            -- Update remaining item count after the loop
            UPDATE "RequestProgress"
            SET "RemainingItemCount" = "RequestedItemCount" - lTotalProcessedCount
            WHERE "RequestProgressId" = lRequestProgressId;

            SELECT COALESCE("RemainingItemCount", 0)
            INTO lRemainingCount
            FROM "RequestProgress"
            WHERE "B2bRequestId" = pB2bRequestId
            ORDER BY "RequestProgressId" DESC
            LIMIT 1;

            rRequestProgressUpdated := ''More Items To Fetch'';
            IF lRemainingCount <= 0 THEN
                UPDATE "B2bRequestDetails"
                SET "RequestStatus" = 7
                WHERE "B2bRequestId" = pB2bRequestId;
                rRequestProgressUpdated := ''Item is distributed'';
            END IF;

        ELSE
            rRequestProgressUpdated := ''No update'';
        END IF;
    END;

';





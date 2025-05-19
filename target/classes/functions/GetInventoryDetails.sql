-- GET User details by email id
CREATE OR REPLACE FUNCTION get_user_details_by_email_id(
    pEmailID CHARACTER VARYING
)
    RETURNS TABLE
            (
                lFullName     CHARACTER VARYING,
                lMobileNumber CHARACTER VARYING,
                lUserRole     CHARACTER VARYING,
                lUserRoleId   INT,
                lUserLocation CHARACTER VARYING
            )
    LANGUAGE plpgsql
AS
'
    BEGIN
        RETURN QUERY
            SELECT (ud."FirstName" || '' '' || ud."LastName")::CHARACTER VARYING AS lFullName,
                   ud."MobileNumber",
                   ur."UserRoleName",
                   ud."UserRoleId"::INT,
                   ln."LocationName"
            FROM "UserDetails" ud
                     LEFT JOIN "UserRole" ur ON ur."UserRoleId" = ud."UserRoleId"
                     LEFT JOIN "LocationDetails" ln ON ln."LocationId" = ud."UserLocationId"
            WHERE ud."E-mail" like pEmailID || ''%'';

        IF NOT FOUND THEN
            RAISE EXCEPTION ''User Not Found in the database'';
        END IF;
    END;
';


-- Get purchase details by date range
CREATE OR REPLACE FUNCTION get_purchase_details_by_date_range(
    pStartDate CHARACTER VARYING,
    pEndDate CHARACTER VARYING
)
    RETURNS TABLE
            (
                rCreatedOn             CHARACTER VARYING,
                rItemName              CHARACTER VARYING,
                rExpiryDate            CHARACTER VARYING,
                rItemCount             INT,
                rInventoryLocationName CHARACTER VARYING,
                rCreatedBy             CHARACTER VARYING,
                rSoldBy                CHARACTER VARYING
            )
    LANGUAGE plpgsql
AS
'
    BEGIN
        SET DATESTYLE = ''dmy'';
        RETURN QUERY
            SELECT PD."CreatedDate"::DATE::CHARACTER VARYING,
                   ID."ItemName",
                   (PD."CreatedDate" + ID."DefaultExpiryDuration"::interval)::DATE::CHARACTER VARYING,
                   PD."ItemCount",
                   LD."LocationName",
                   CBD."E-mail",
                   SBD."E-mail"
            FROM "PurchaseDetails" PD
                     LEFT JOIN public."ItemsDetails" ID on ID."ItemId" = PD."ItemId"
                     LEFT JOIN public."LocationDetails" LD on LD."LocationId" = PD."InventoryLocationId"
                     LEFT JOIN public."UserDetails" SBD on SBD."UserDetailId" = PD."SellerId"
                     LEFT JOIN public."UserDetails" CBD on CBD."UserDetailId" = PD."InsertedBy"
            WHERE PD."CreatedDate"::DATE between pStartDate::DATE and pEndDate::DATE;

        IF NOT FOUND THEN
            RAISE EXCEPTION ''No Data In Selected Date Range'';
        END IF;

    EXCEPTION
        WHEN others THEN
            RAISE ''Inserted data format is incorrect'';

    END;
';

-- Find the item details
CREATE OR REPLACE FUNCTION get_item_details()
    RETURNS TABLE
            (
                rItemName        CHARACTER VARYING,
                rItemID          INT,
                rItemExpiryDuration VARCHAR
            )
    LANGUAGE plpgsql
AS
'
    BEGIN

        RETURN QUERY
            SELECT "ItemName",
                   "ItemId",
                   "DefaultExpiryDuration"
            FROM "ItemsDetails";

    END;
';


-- Find The Location ID
CREATE OR REPLACE FUNCTION get_location_details()
    RETURNS TABLE
            (
                rLocationName CHARACTER VARYING,
                rLocationId   INT
            )
    LANGUAGE plpgsql
AS
'
    BEGIN
        RETURN QUERY
            SELECT "LocationName", "LocationId"
            FROM "LocationDetails";
    END;
';

-- Find Products going to be expired
CREATE OR REPLACE FUNCTION get_going_to_expiry_product_and_location()
    RETURNS TABLE
            (
                rPurchaseId   VARCHAR,
                rLocationName CHARACTER VARYING,
                rLocationId   INT,
                rItemName     CHARACTER VARYING,
                rItemID       INT,
                rItemCount    INT,
                rExpiryDate   character varying
            )
    LANGUAGE plpgsql
AS
'
    BEGIN
        RETURN QUERY
            SELECT PD."PurchaseId",
                   LD."LocationName",
                   LD."LocationId",
                   ID."ItemName",
                   ID."ItemId",
                   PD."ItemCount"::INT,
                   PD."ExpiryDate"::DATE::CHARACTER VARYING
            FROM "PurchaseDetails" PD
                     LEFT JOIN public."ItemsDetails" ID on ID."ItemId" = PD."ItemId"
                     LEFT JOIN public."LocationDetails" LD on LD."LocationId" = PD."InventoryLocationId"
            WHERE PD."ExpiryDate"::DATE BETWEEN CURRENT_TIMESTAMP::DATE - INTERVAL ''2 Day'' AND CURRENT_TIMESTAMP::DATE + INTERVAL ''2 Day''
              AND PD."ItemCount"::int > 0;
        IF NOT FOUND THEN
            RAISE EXCEPTION ''No item is going to be expired '';
        END IF;
    END;
';

-- Check whether the item is available or not in the inventory
CREATE OR REPLACE FUNCTION get_no_of_available_item(
    pItemId INT,
    pLocationId INT
)
    RETURNS TABLE
            (
                rItemAvailability CHARACTER VARYING
            )
    LANGUAGE plpgsql
AS
'
    DECLARE
        rRsponse CHARACTER VARYING;
    BEGIN
        SELECT CASE
                   WHEN SUM(PD."ItemCount")::INT = 0 OR SUM(PD."ItemCount") IS NULL
                       THEN (''No item is available in the inventory'')::CHARACTER VARYING
                   ELSE SUM(PD."ItemCount")::VARCHAR
                   END
        INTO rRsponse
        FROM "PurchaseDetails" PD
        WHERE PD."ItemId" = pItemId
          AND PD."InventoryLocationId" = pLocationId;
        RETURN QUERY SELECT rRsponse;

    END;
';
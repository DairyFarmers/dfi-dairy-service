-- Purchase Details Tables
-- 1. Create table
--Create table for Location


CREATE TABLE IF NOT EXISTS "LocationDetails"
(
    "LocationId"   SERIAL PRIMARY KEY,
    "LocationName" CHARACTER VARYING NOT NULL DEFAULT ''
);

--Create table for items details
CREATE TABLE IF NOT EXISTS "ItemsDetails"
(
    "ItemId"                SERIAL PRIMARY KEY,
    "ItemName"              CHARACTER VARYING,
    "DefaultExpiryDuration" CHARACTER VARYING,
    "MaxPurchasePrice"      NUMERIC(10, 2),
    "MaxSellingPrice"       NUMERIC(10, 2),
    "MaxSellingUnitPriceForB2B" NUMERIC(10,2)
);

--Create User Role  Table
CREATE TABLE IF NOT EXISTS "UserRole"
(
    "UserRoleId"   SERIAL PRIMARY KEY,
    "UserRoleName" CHARACTER VARYING
);

--Create User Details Table
CREATE TABLE IF NOT EXISTS "UserDetails"
(
    "UserDetailId"   SERIAL PRIMARY KEY,
    "FirstName"      CHARACTER VARYING,
    "LastName"       CHARACTER VARYING,
    "UserRoleId"     INT               NOT NULL,
    "E-mail"         CHARACTER VARYING NOT NULL UNIQUE,
    "MobileNumber"   CHARACTER VARYING NOT NULL,
    "UserLocationId" INT               NOT NULL,
    "Password"       CHARACTER VARYING NOT NULL,
    FOREIGN KEY ("UserRoleId") REFERENCES "UserRole" ("UserRoleId"),
    FOREIGN KEY ("UserLocationId") REFERENCES "LocationDetails" ("LocationId")
);


-- Create table for purchase details
CREATE TABLE IF NOT EXISTS "PurchaseDetails"
(
    "PurchaseId"          VARCHAR(30) PRIMARY KEY NOT NULL,
    "CreatedDate"         TIMESTAMP,
    "InsertedBy"          INT                     NOT NULL,
    "PurchasePrice"       DECIMAL(10, 2),
    "ItemId"              INT                     NOT NULL,
    "ExpiryDate"          TIMESTAMP,
    "SellerId"            INT                     NOT NULL,
    "ItemCount"           INT                     NOT NULL,
    "InventoryLocationId" INT                     NOT NULL,
    FOREIGN KEY ("ItemId") REFERENCES "ItemsDetails" ("ItemId"),
    FOREIGN KEY ("SellerId") REFERENCES "UserDetails" ("UserDetailId"),
    FOREIGN KEY ("InsertedBy") REFERENCES "UserDetails" ("UserDetailId"),
    FOREIGN KEY ("InventoryLocationId") REFERENCES "LocationDetails" ("LocationId")
);


-- Create Sales Details
CREATE TABLE IF NOT EXISTS "SalesDetails"
(
    "SalesDetailId"       VARCHAR(30) PRIMARY KEY NOT NULL,
    "CreatedDate"         TIMESTAMP,
    "SalesPrice"          DECIMAL(10, 2),
    "ItemId"              INT,
    "ItemCount"           INT                     NOT NULL,
    "InventoryLocationId" INT                     NOT NULL,
    "BuyerID"             INT,
    "IsB2B"               BOOLEAN,
    FOREIGN KEY ("ItemId") REFERENCES "ItemsDetails" ("ItemId"),
    FOREIGN KEY ("InventoryLocationId") REFERENCES "LocationDetails" ("LocationId")
);

-- Use  this table to store the entire b2b purchase details
CREATE TABLE IF NOT EXISTS "B2bPurchaseDetails"
(
    "B2bPurchaseDetailId" BIGSERIAL PRIMARY KEY,
    "PurchasedOn"         TIMESTAMP,
    "EmailId"             CHARACTER VARYING,
    "SoldUnitCount"       BIGINT,
    "SalesDetailId"       VARCHAR,
    "PurchasePrice"       BIGINT,
    "ItemName"            CHARACTER VARYING,
    "LocationName"        CHARACTER VARYING,
    FOREIGN KEY ("SalesDetailId") REFERENCES "SalesDetails" ("SalesDetailId"),
    FOREIGN KEY ("EmailId") REFERENCES "UserDetails" ("E-mail")

);


-- Creating a table that replicates farmers sales
CREATE TABLE IF NOT EXISTS "FarmerSalesDetails"
(
    "FarmerSalesDetailId" BIGSERIAL PRIMARY KEY,
    "SoldOn"              TIMESTAMP,
    "EmailId"             CHARACTER VARYING,
    "SoldUnitCount"       BIGINT,
    "PurchaseDetailId"    CHARACTER VARYING,
    "Revenue"             NUMERIC(10, 2),
    "ItemName"            CHARACTER VARYING,
    "LocationName"        CHARACTER VARYING,
    FOREIGN KEY ("PurchaseDetailId") REFERENCES "PurchaseDetails" ("PurchaseId"),
    FOREIGN KEY ("EmailId") REFERENCES "UserDetails" ("E-mail")
);
-- 2. Create a sequence to having a auto increment number
CREATE SEQUENCE IF NOT EXISTS purchase_id_seq;
CREATE SEQUENCE IF NOT EXISTS sales_id_seq;

-- 3. Create  functions to generate the custom ID
CREATE OR REPLACE FUNCTION generate_purchase_id()
    RETURNS TRIGGER
    LANGUAGE plpgsql
AS
'
    DECLARE
        num TEXT;
    BEGIN
        num := LPAD(nextval(''purchase_id_seq'')::TEXT, 6, ''0'');
        NEW."PurchaseId" := ''Purchase'' || TO_CHAR(current_date, ''YYYYMM'') || num;
        RETURN NEW;
    END;
';

CREATE OR REPLACE FUNCTION generate_sales_id()
    RETURNS TRIGGER
    LANGUAGE plpgsql
AS
'
    DECLARE
        num TEXT;
    BEGIN
        num := LPAD(nextval(''sales_id_seq'')::TEXT, 6, ''0'');
        NEW."SalesDetailId" := ''Sales'' || TO_CHAR(current_date, ''YYYYMM'') || num;
        RETURN NEW;
    END;
';

-- 4. Create a trigger to auto-fill the custom_id on insert
DROP TRIGGER IF EXISTS trg_generate_purchase_id ON "PurchaseDetails";
DROP TRIGGER IF EXISTS trg_generate_sales_id ON "SalesDetails";

CREATE TRIGGER trg_generate_purchase_id
    BEFORE INSERT
    ON "PurchaseDetails"
    FOR EACH ROW
EXECUTE FUNCTION generate_purchase_id();

CREATE TRIGGER trg_generate_sales_id
    BEFORE INSERT
    ON "SalesDetails"
    FOR EACH ROW
EXECUTE FUNCTION generate_sales_id();

-- 5. Create function

-- To get the expiry date
CREATE OR REPLACE FUNCTION get_expiry_date(pItemId INT)
    RETURNS TABLE
            (
                "rExpiryDate" TIMESTAMP
            )
    LANGUAGE plpgsql
AS
'
    DECLARE
        lExpiryDate TIMESTAMP;
        lDuration   CHARACTER VARYING;
    BEGIN

        SELECT "DefaultExpiryDuration"
        INTO lDuration
        FROM "ItemsDetails"
        WHERE "ItemId" = pItemId;

        IF lDuration IS NULL THEN
            lExpiryDate := current_timestamp + INTERVAL ''1 day'';
            RETURN QUERY SELECT lExpiryDate;
        ELSE
            lExpiryDate := current_timestamp + lDuration::INTERVAL;
            RETURN QUERY SELECT lExpiryDate;
        END IF;
    END;
';

-- To insert data to the PurchaseDetails
CREATE OR REPLACE FUNCTION insert_purchase_details(
    pInsertedBy INT,
    pPurchasePrice DECIMAL(10, 2),
    pItemId INT,
    pSellerId INT,
    pItemCount INT,
    plocationId INT
)
    RETURNS TABLE
            (
                "rDataUpdated" CHARACTER VARYING
            )
    LANGUAGE plpgsql
AS
'
    DECLARE
        lExpiryDate       CHARACTER VARYING;
        lCurrentDateTime  TIMESTAMP := current_timestamp::timestamp;
        lPurchaseDetailId CHARACTER VARYING;
        lSalesPrice       NUMERIC(10, 2);
        lUnitSold         BIGINT;
        lItemName         CHARACTER VARYING;
        lSellerRole       INT;
        lSellerEmail      CHARACTER VARYING;
        lLocationName     CHARACTER VARYING;
    BEGIN
        SELECT "DefaultExpiryDuration"
        into lExpiryDate
        FROM "ItemsDetails"
        WHERE "ItemId" = pitemid;

        IF pInsertedBy IS NULL OR pPurchasePrice IS NULL OR pItemId IS NULL OR pSellerId IS NULL THEN
            RAISE EXCEPTION ''Invalid input: Some required fields are null'';
        END IF;

        IF pInsertedBy = 0 OR pPurchasePrice = 0 OR pItemId = 0 OR pSellerId IS NULL THEN
            RAISE EXCEPTION ''Invalid input: Some required fields are null'';
        END IF;

        INSERT INTO "PurchaseDetails"("CreatedDate", "InsertedBy", "PurchasePrice", "ItemId", "ExpiryDate", "SellerId",
                                      "ItemCount", "InventoryLocationId")
        VALUES (lCurrentDateTime, pInsertedBy, pPurchasePrice, pItemId, lCurrentDateTime + lExpiryDate::INTERVAL,
                pSellerId,
                pItemCount, plocationId);

        SELECT "PurchaseId",
               "PurchasePrice",
               "ItemCount",
               ID."ItemName",
               LD."LocationName",
               UD."E-mail",
               UD."UserRoleId"
        into lPurchaseDetailId, lSalesPrice, lUnitSold, lItemName, lLocationName, lSellerEmail, lSellerRole
        FROM "PurchaseDetails"
                 LEFT JOIN "ItemsDetails" ID on "PurchaseDetails"."ItemId" = ID."ItemId"
                 LEFT JOIN "LocationDetails" LD on LD."LocationId" = "PurchaseDetails"."InventoryLocationId"
                 LEFT JOIN "UserDetails" UD on UD."UserDetailId" = "PurchaseDetails"."SellerId"
        WHERE "CreatedDate"::timestamp = lCurrentDateTime;

        IF lSellerRole = 4 THEN
            INSERT INTO "FarmerSalesDetails"("SoldOn", "EmailId", "SoldUnitCount", "PurchaseDetailId", "Revenue",
                                             "ItemName",
                                             "LocationName")
            VALUES (lCurrentDateTime, lSellerEmail, lUnitSold, lPurchaseDetailId, lSalesPrice, lItemName,
                    lLocationName);
        END IF;

        "rDataUpdated" := ''Purchase Details Successfully Inserted'';
        RETURN QUERY SELECT "rDataUpdated";

    EXCEPTION
        WHEN OTHERS THEN
            "rDataUpdated" := ''Issue In Inserting The Purchase Details'';
            RETURN QUERY SELECT "rDataUpdated";
    END;
'
;

--To insert the User Role
CREATE OR REPLACE FUNCTION insert_user_role(
    pUserRoleName CHARACTER VARYING
)
    RETURNS TABLE
            (
                "rDataUpdated" CHARACTER VARYING
            )
    LANGUAGE plpgsql
AS
'
    DECLARE
        lExists BOOLEAN;
    BEGIN
        IF pUserRoleName IS NULL THEN
            RAISE EXCEPTION ''Invalid input: UserRoleName is null'';
        END IF;

        SELECT EXISTS (SELECT 1
                       FROM "UserRole"
                       WHERE "UserRoleName" = pUserRoleName)
        INTO lExists;

        IF lExists THEN
            RAISE EXCEPTION ''User role already exists'';
        END IF;

        INSERT
        INTO "UserRole"("UserRoleName")
        VALUES (pUserRoleName);

        "rDataUpdated" := ''User Role Successfully Inserted'';
        RETURN QUERY SELECT "rDataUpdated";

    EXCEPTION
        WHEN OTHERS THEN
            "rDataUpdated" := ''Issue In Inserting The User Role'';
            RETURN QUERY SELECT "rDataUpdated";
    END;
'
;

-- Insert Item Details
CREATE OR REPLACE FUNCTION insert_item_details(
    pItemName CHARACTER VARYING,
    pDefaultExpiryDuration CHARACTER VARYING,
    pMaxPurchasePrice NUMERIC(10, 2),
    pMaxSellingPrice NUMERIC(10, 2),
    pMaxSellingUnitPriceForB2B NUMERIC(10, 2)
)
    RETURNS TABLE
            (
                "rDataUpdated" CHARACTER VARYING
            )
    LANGUAGE plpgsql
AS
'
    DECLARE
        lExists BOOLEAN;
    BEGIN
        IF pItemName IS NULL OR pDefaultExpiryDuration IS NULL THEN
            RAISE EXCEPTION ''Invalid input: ItemName or ExpiryDuration is null'';
        END IF;

        SELECT EXISTS (SELECT 1
                       FROM "ItemsDetails"
                       WHERE "ItemName" = pItemName)
        INTO lExists;

        IF lExists THEN
            RAISE EXCEPTION ''Item already exists in the database'';
        END IF;

        INSERT INTO "ItemsDetails"("ItemName", "DefaultExpiryDuration", "MaxPurchasePrice", "MaxSellingPrice", "MaxSellingUnitPriceForB2B")
        VALUES (pItemName, pDefaultExpiryDuration, pMaxPurchasePrice, pMaxSellingPrice, pMaxSellingUnitPriceForB2B);

        "rDataUpdated" := ''Item Details Successfully Inserted'';
        RETURN QUERY SELECT "rDataUpdated";

    EXCEPTION
        WHEN OTHERS THEN
            "rDataUpdated" := ''Issue In Inserting The Item Details'';
            RETURN QUERY SELECT "rDataUpdated";
    END;
';



--Insert UserDetails
CREATE OR REPLACE FUNCTION insert_user_details(
    pFirstName CHARACTER VARYING,
    pLastName CHARACTER VARYING,
    pUserRoleId INT,
    pEmail CHARACTER VARYING,
    pMobileNumber CHARACTER VARYING,
    pLocation INT,
    pPassword CHARACTER VARYING
)
    RETURNS TABLE
            (
                "rDataUpdated" CHARACTER VARYING
            )
    LANGUAGE plpgsql
AS
'
    DECLARE
        lExists BOOLEAN;
    BEGIN
        IF pFirstName IS NULL OR pUserRoleId IS NULL OR pEmail IS NULL THEN
            RAISE EXCEPTION ''Invalid input: Required fields are missing'';
        END IF;

        SELECT EXISTS (SELECT 1
                       FROM "UserDetails"
                       WHERE "E-mail" = pEmail)
        INTO lExists;

        IF lExists THEN
            RAISE EXCEPTION ''User with the given email already exists'';
        END IF;

        INSERT INTO "UserDetails"("FirstName", "LastName", "UserRoleId", "E-mail", "MobileNumber", "UserLocationId",
                                  "Password")
        VALUES (pFirstName, pLastName, pUserRoleId, pEmail, pMobileNumber, pLocation,
                ENCODE(DIGEST(pPassword, ''sha256''), ''hex''));

        "rDataUpdated" := ''User Details Successfully Inserted'';
        RETURN QUERY SELECT "rDataUpdated";

    EXCEPTION
        WHEN OTHERS THEN
            "rDataUpdated" := ''Issue In Inserting The User Details'';
            RETURN QUERY SELECT "rDataUpdated";
    END;
';

--Insert Location
CREATE OR REPLACE FUNCTION insert_location_details(
    pLocationName CHARACTER VARYING
)
    RETURNS TABLE
            (
                "rDataUpdated" CHARACTER VARYING
            )
    LANGUAGE plpgsql
AS
'
    DECLARE
        lExists BOOLEAN;
    BEGIN
        IF pLocationName IS NULL OR trim(pLocationName) = '''' THEN
            RAISE EXCEPTION ''Invalid input: LocationName is null or empty'';
        END IF;

        SELECT EXISTS (SELECT 1
                       FROM "LocationDetails"
                       WHERE "LocationName" = pLocationName)
        INTO lExists;

        IF lExists THEN
            RAISE EXCEPTION ''Location already exists in the database'';
        END IF;

        INSERT INTO "LocationDetails"("LocationName")
        VALUES (pLocationName);

        "rDataUpdated" := ''Location Details Successfully Inserted'';
        RETURN QUERY SELECT "rDataUpdated";

    EXCEPTION
        WHEN OTHERS THEN
            "rDataUpdated" := ''Issue In Inserting The Location Details'';
            RETURN QUERY SELECT "rDataUpdated";
    END;
';

CREATE EXTENSION IF NOT EXISTS pgcrypto;

--Update User Details
CREATE OR REPLACE FUNCTION update_user_details(
    pEmail CHARACTER VARYING DEFAULT NULL,
    pMobileNumber CHARACTER VARYING DEFAULT NULL,
    pFirstName CHARACTER VARYING DEFAULT NULL,
    pLastName CHARACTER VARYING DEFAULT NULL,
    pUserRoleId INT DEFAULT NULL,
    pLocationId INT DEFAULT NULL,
    pPassword CHARACTER VARYING DEFAULT NULL
)
    RETURNS TABLE
            (
                "rDataUpdated" CHARACTER VARYING
            )
    LANGUAGE plpgsql
AS
'
    DECLARE
        lUserId INT;
    BEGIN
        -- Validate at least one identifier
        IF (pEmail IS NULL OR TRIM(pEmail) = '''') AND (pMobileNumber IS NULL OR TRIM(pMobileNumber) = '''') THEN
            RAISE EXCEPTION ''At least one of Email or MobileNumber must be provided'';
        END IF;

        -- Identify user
        SELECT "UserDetailId"
        INTO lUserId
        FROM "UserDetails"
        WHERE ("E-mail" = pEmail OR "MobileNumber" = pMobileNumber)
        LIMIT 1;

        IF lUserId IS NULL THEN
            RAISE EXCEPTION ''User not found with given Email or Mobile Number'';
        END IF;

        IF pPassword = '''' THEN
            pPassword := NULL;
        END IF;

        -- Perform update only for non-null fields
        UPDATE "UserDetails"
        SET "FirstName"      = COALESCE(pFirstName, "FirstName"),
            "LastName"       = COALESCE(pLastName, "LastName"),
            "UserRoleId"     = COALESCE(pUserRoleId, "UserRoleId"),
            "E-mail"         = COALESCE(pemail, "E-mail"),
            "MobileNumber"   = COALESCE(pmobilenumber, "MobileNumber"),
            "UserLocationId" =COALESCE(pLocationId, "UserLocationId"),
            "Password"       =COALESCE(ENCODE(DIGEST(pPassword, ''sha256''), ''hex''), "Password")
        WHERE "UserDetailId" = lUserId;

        "rDataUpdated" := ''User Details Successfully Updated'';
        RETURN QUERY SELECT "rDataUpdated";

    EXCEPTION
        WHEN OTHERS THEN
            "rDataUpdated" := ''Issue In Updating The User Details'';
            RETURN QUERY SELECT "rDataUpdated";
    END;
';

-- Insert sales details, IF it's b2b then replicate in B2b purchase details table
CREATE OR REPLACE FUNCTION insert_sales_details(
    pItemId INT,
    pItemCount BIGINT,
    pInventoryLocationId INT,
    pSalesPrice DECIMAL(10, 2),
    pBuyerId integer,
    pIsb2b boolean
)
    RETURNS TABLE
            (
                "rDataUpdated" CHARACTER VARYING
            )
    LANGUAGE plpgsql
AS
'
    DECLARE
        lRemainingToDeduct BIGINT    := pItemCount::BIGINT;
        lPurchaseRow       RECORD;
        lTotalItemCount    BIGINT;
        resultMsg          CHARACTER VARYING;
        lCurrentDateTime   TIMESTAMP := current_timestamp;
        lEmailId           CHARACTER VARYING;
        lItemName          CHARACTER VARYING;
        lLocationName      CHARACTER VARYING;
        lSalesDetailId     VARCHAR(30);
    BEGIN
        -- Get total available item count for the product in the specified location
        SELECT SUM("ItemCount")
        INTO lTotalItemCount
        FROM "PurchaseDetails"
        WHERE "InventoryLocationId"::INT = pInventoryLocationId
          AND "ItemId"::INT = pItemId;


        IF lTotalItemCount IS NULL THEN
            RAISE EXCEPTION ''Invalid input: Some required fields are null'';
        END IF;

        IF lTotalItemCount >= pItemCount::BIGINT THEN
            -- Deduct items from PurchaseDetails (FIFO)
            FOR lPurchaseRow IN
                SELECT "PurchaseId",
                       "ItemCount"
                FROM "PurchaseDetails"
                WHERE "InventoryLocationId" = pInventoryLocationId
                  AND "ItemId" = pItemId
                  AND "ItemCount" > 0
                ORDER BY "CreatedDate"
                LOOP
                    RAISE NOTICE ''Before update: PurchaseId=% Count=%'', lPurchaseRow."PurchaseId", lPurchaseRow."ItemCount";
                    EXIT WHEN lRemainingToDeduct = 0;

                    IF lPurchaseRow."ItemCount" > lRemainingToDeduct THEN
                        UPDATE "PurchaseDetails"
                        SET "ItemCount" = "ItemCount" - lRemainingToDeduct
                        WHERE "PurchaseId" = lPurchaseRow."PurchaseId";
                        lRemainingToDeduct := 0;
                    ELSE
                        UPDATE "PurchaseDetails"
                        SET "ItemCount" = 0
                        WHERE "PurchaseId" = lPurchaseRow."PurchaseId";
                        lRemainingToDeduct := lRemainingToDeduct - lPurchaseRow."ItemCount";
                    END IF;
                END LOOP;

            -- Insert into SalesDetails
            INSERT INTO "SalesDetails" ("CreatedDate", "SalesPrice", "ItemId", "ItemCount", "InventoryLocationId",
                                        "BuyerID", "IsB2B")
            VALUES (lCurrentDateTime, psalesprice, pitemid, pitemcount::BIGINT, pinventorylocationid, pBuyerId,
                    pIsB2b);

            IF pIsb2b IS TRUE THEN
                SELECT ID."ItemName",
                       LD."LocationName",
                       UD."E-mail",
                       "SalesDetailId"
                into lItemName, lLocationName, lEmailId, lSalesDetailId
                FROM "SalesDetails" SD
                         LEFT JOIN "ItemsDetails" ID ON SD."ItemId" = ID."ItemId"
                         LEFT JOIN "UserDetails" UD on UD."UserDetailId" = SD."BuyerID"
                         LEFT JOIN "LocationDetails" LD on LD."LocationId" = SD."InventoryLocationId"
                WHERE "CreatedDate" = lCurrentDateTime;

                INSERT INTO "B2bPurchaseDetails"("PurchasedOn",
                                                 "EmailId",
                                                 "SoldUnitCount",
                                                 "SalesDetailId",
                                                 "PurchasePrice",
                                                 "ItemName",
                                                 "LocationName")

                VALUES (lCurrentDateTime,
                        lEmailId,
                        pItemCount,
                        lSalesDetailId,
                        pSalesPrice,
                        lItemName,
                        lLocationName);
            END IF;

            resultMsg := ''Sales Data Updated''::CHARACTER VARYING;
        ELSE
            resultMsg := ''Selected Item is not in the inventory''::CHARACTER VARYING;
        END IF;

        RETURN QUERY SELECT resultMsg;

    EXCEPTION
        WHEN OTHERS THEN
            RETURN QUERY SELECT ''Invalid Input''::CHARACTER VARYING;
    END;
';


CREATE OR REPLACE FUNCTION get_user_auth_by_email(p_email VARCHAR)
    RETURNS TABLE
            (
                email    VARCHAR,
                password VARCHAR,
                role     int
            )
    LANGUAGE plpgsql
AS
'
    BEGIN
        RETURN QUERY
            SELECT u."E-mail",
                   u."Password",
                   u."UserRoleId"
            FROM "UserDetails" u
            WHERE u."E-mail" = p_email;
    END;
';


CREATE OR REPLACE FUNCTION change_password(
    pUserEmail CHARACTER VARYING DEFAULT NOT NULL,
    pOldPassword CHARACTER VARYING DEFAULT NOT NULL,
    pNewPassword CHARACTER VARYING DEFAULT NOT NULL
)
    RETURNS TABLE
            (
                "rPasswordHaveChanged" CHARACTER VARYING
            )
    LANGUAGE plpgsql
AS
'
    DECLARE
        lEmail    CHARACTER VARYING := null;
        lPassword CHARACTER VARYING := null;
    BEGIN

        SELECT password,
               email
        into lPassword, lEmail
        FROM get_user_auth_by_email(pUserEmail);

        IF lEmail != puseremail OR lEmail IS NULL THEN
            RAISE EXCEPTION ''Invalid Email-ID'';
        END IF;

        IF lPassword = ENCODE(DIGEST(pNewPassword, ''sha256''), ''hex'') THEN
            RAISE EXCEPTION ''Old password and New password are same.'';
        END IF;

        IF lPassword != ENCODE(DIGEST(pOldPassword, ''sha256''), ''hex'') THEN
            RAISE EXCEPTION ''Current password is Miss matching.'';
        END IF;

        UPDATE "UserDetails"
        SET "Password"=ENCODE(DIGEST(pNewPassword, ''sha256''), ''hex'')
        WHERE "E-mail" = pUserEmail;

        "rPasswordHaveChanged" := ''Password has been updated'';

        RETURN QUERY SELECT "rPasswordHaveChanged";

    END;
';
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
    "DefaultExpiryDuration" CHARACTER VARYING
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
    "E-mail"         CHARACTER VARYING NOT NULL,
    "MobileNumber"   CHARACTER VARYING NOT NULL,
    "UserLocationId" INT               NOT NULL,
    "Password" CHARACTER VARYING NOT NULL,
    FOREIGN KEY ("UserRoleId") REFERENCES "UserRole" ("UserRoleId"),
    FOREIGN KEY ("UserLocationId") REFERENCES "LocationDetails" ("LocationId")
);



-- Create table for purchase details
CREATE TABLE IF NOT EXISTS "PurchaseDetails"
(
    "PurchaseId"          VARCHAR(30) PRIMARY KEY NOT NULL,
    "CreatedDate"         TIMESTAMP WITH TIME ZONE,
    "InsertedBy"          INT                     NOT NULL,
    "PurchasePrice"       DECIMAL(10, 2),
    "ItemId"              INT                     NOT NULL,
    "ExpiryDate"          TIMESTAMP WITH TIME ZONE,
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
    "CreatedDate"         TIMESTAMP WITH TIME ZONE,
    "SalesPrice"          DECIMAL(10, 2),
    "ItemId"              INT,
    "ItemCount"           INT                     NOT NULL,
    "InventoryLocationId" INT                     NOT NULL,
    "SellerId" INT,
    "IsB2B" BOOLEAN,
    FOREIGN KEY ("ItemId") REFERENCES "ItemsDetails" ("ItemId"),
    FOREIGN KEY ("InventoryLocationId") REFERENCES "LocationDetails" ("LocationId")
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
                "rExpiryDate" TIMESTAMP WITH TIME ZONE
            )
    LANGUAGE plpgsql
AS
'
    DECLARE
        lExpiryDate TIMESTAMP WITH TIME ZONE;
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
        lExpiryDate TIMESTAMP;
    BEGIN
        SELECT "rExpiryDate"
        into lExpiryDate
        FROM get_expiry_date(pItemId);

        IF pInsertedBy IS NULL OR pPurchasePrice IS NULL OR pItemId IS NULL OR pSellerId IS NULL THEN
            RAISE EXCEPTION ''Invalid input: Some required fields are null'';
        END IF;

        IF pInsertedBy = 0 OR pPurchasePrice = 0 OR pItemId = 0 OR pSellerId IS NULL THEN
            RAISE EXCEPTION ''Invalid input: Some required fields are null'';
        END IF;

        INSERT INTO "PurchaseDetails"("CreatedDate", "InsertedBy", "PurchasePrice", "ItemId", "ExpiryDate", "SellerId",
                                      "ItemCount", "InventoryLocationId")
        VALUES (current_timestamp::timestamp, pInsertedBy, pPurchasePrice, pItemId, lExpiryDate::timestamp, pSellerId,
                pItemCount, plocationId);

        "rDataUpdated" := ''Purchase Details Successfully Inserted'';
        RETURN QUERY SELECT "rDataUpdated";

    EXCEPTION
        WHEN OTHERS THEN
            "rDataUpdated" := ''Issue In Inserting The Purchase Details'';
            RETURN QUERY SELECT "rDataUpdated";
    END;'
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

        INSERT INTO "UserRole"("UserRoleName")
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
    pDefaultExpiryDuration CHARACTER VARYING
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

        INSERT INTO "ItemsDetails"("ItemName", "DefaultExpiryDuration")
        VALUES (pItemName, pDefaultExpiryDuration);

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

        INSERT INTO "UserDetails"("FirstName", "LastName", "UserRoleId", "E-mail", "MobileNumber", "UserLocationId", "Password")
        VALUES (pFirstName, pLastName, pUserRoleId, pEmail, pMobileNumber, pLocation, ENCODE(DIGEST(pPassword, ''sha256''), ''hex''));

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
    pLocationId INT DEFAULT NULL
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

        -- Perform update only for non-null fields
        UPDATE "UserDetails"
        SET "FirstName"      = COALESCE(pFirstName, "FirstName"),
            "LastName"       = COALESCE(pLastName, "LastName"),
            "UserRoleId"     = COALESCE(pUserRoleId, "UserRoleId"),
            "E-mail"         = COALESCE(pemail, "E-mail"),
            "MobileNumber"   = COALESCE(pmobilenumber, "MobileNumber"),
            "UserLocationId" =COALESCE(pLocationId, "UserLocationId")
        WHERE "UserDetailId" = lUserId;

        "rDataUpdated" := ''User Details Successfully Updated'';
        RETURN QUERY SELECT "rDataUpdated";

    EXCEPTION
        WHEN OTHERS THEN
            "rDataUpdated" := ''Issue In Updating The User Details'';
            RETURN QUERY SELECT "rDataUpdated";
    END;
';

-- Create Sales Details
CREATE OR REPLACE FUNCTION insert_sales_details(
    pItemId INT,
    pItemCount INT,
    pInventoryLocationId INT,
    pSalesPrice DECIMAL(10, 2),
    pSellerId integer,
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
        lRemainingToDeduct BIGINT := pItemCount::BIGINT;
        lPurchaseRow       RECORD;
        lTotalItemCount    BIGINT;
        resultMsg          CHARACTER VARYING;
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
            INSERT INTO "SalesDetails" ("CreatedDate", "SalesPrice", "ItemId", "ItemCount", "InventoryLocationId","SellerId", "IsB2B")
            VALUES (CURRENT_TIMESTAMP, psalesprice, pitemid, pitemcount::BIGINT, pinventorylocationid, pSellerID, pIsB2b);

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
    RETURNS TABLE (
                      email VARCHAR,
                      password VARCHAR,
                      role int
                  ) LANGUAGE plpgsql
    AS
    '
BEGIN
    RETURN QUERY
        SELECT u."E-mail", u."Password", u."UserRoleId"
        FROM "UserDetails" u
        WHERE u."E-mail" = p_email;
END;
';


CREATE OR REPLACE FUNCTION change_password(
    pUserEmail CHARACTER VARYING DEFAULT NOT NULL,
    pOldPassword CHARACTER VARYING DEFAULT NOT NULL,
    pNewPassword CHARACTER VARYING DEFAULT NOT NULL
)   RETURNS TABLE("rPasswordHaveChanged" CHARACTER VARYING)
    LANGUAGE plpgsql
AS
'
DECLARE
    lEmail CHARACTER VARYING;
    lPassword CHARACTER VARYING:=null;
BEGIN
    SELECT password into lPassword FROM get_user_auth_by_email(pUserEmail);
    IF lPassword = ENCODE(DIGEST(pNewPassword, ''sha256''), ''hex'') THEN
        RAISE EXCEPTION ''Old password and New password are same.'';
    END IF;

    IF lPassword != ENCODE(DIGEST(pOldPassword, ''sha256''), ''hex'')  THEN
        RAISE EXCEPTION ''Current password is Miss matching.'';
    END IF;

    UPDATE "UserDetails"
    SET
        "Password"=ENCODE(DIGEST(pNewPassword, ''sha256''), ''hex'')
    WHERE "E-mail" = pUserEmail;

    "rPasswordHaveChanged" := ''Password has been updated'';

    RETURN QUERY SELECT "rPasswordHaveChanged";

END;
';
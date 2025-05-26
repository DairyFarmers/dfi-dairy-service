-- GET User details by email id
CREATE OR REPLACE FUNCTION get_user_details_by_email_id(
    pEmailID CHARACTER VARYING
)
    RETURNS TABLE
            (
                lUserId      INT,
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
            SELECT ud."UserDetailId",
                   (ud."FirstName" || '' '' || ud."LastName")::CHARACTER VARYING AS lFullName,
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

    END;
';

-- Find the item details
CREATE OR REPLACE FUNCTION get_item_details()
    RETURNS TABLE
            (
                rItemName           CHARACTER VARYING,
                rItemID             INT,
                rItemExpiryDuration VARCHAR,
                rItemMaxPurchasePrice NUMERIC(10, 2),
                rItemMaxSellingPrice NUMERIC(10,2)
            )
    LANGUAGE plpgsql
AS
'
    BEGIN

        RETURN QUERY
            SELECT "ItemName",
                   "ItemId",
                   "DefaultExpiryDuration",
                   "MaxPurchasePrice",
                    "MaxSellingPrice"
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


-- Get all purchase Details by date range and Location
CREATE OR REPLACE FUNCTION get_purchase_details_by_date_range_and_location(
    pStartDate CHARACTER VARYING,
    pEndDate CHARACTER VARYING,
    pLocationId INT
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
            WHERE
PD."CreatedDate"::DATE between pStartDate::DATE and pEndDate::DATE
AND PD."InventoryLocationId" = pLocationId;

        IF NOT FOUND THEN
            RAISE EXCEPTION ''No Data In Selected Date Range'';
        END IF;

    END;
';

--Get all sales details by date range
CREATE OR REPLACE FUNCTION get_revenue_details_by_date_range(
    pStartDate character varying,
    pEndDate character varying
)
    RETURNS TABLE (
                      lCreatedOn CHARACTER VARYING,
                      lItemName CHARACTER VARYING,
                      lInventoryItemCount BIGINT,
                      lSoldItemCount BIGINT,
                      lLocationName CHARACTER VARYING,
                      lRevenue NUMERIC(10, 2),
                      lProfit NUMERIC(10, 2)
                  )
    LANGUAGE plpgsql
AS
'
    BEGIN
        RETURN QUERY
            WITH purchase_data AS (
                SELECT
                    PD."CreatedDate"::DATE AS created_date,
                    ID."ItemName",
                    LD."LocationName",
                    SUM(PD."ItemCount") AS inventory_item_count,
                    SUM(PD."PurchasePrice") AS total_purchase_price
                FROM "PurchaseDetails" PD
                         JOIN "ItemsDetails" ID ON ID."ItemId" = PD."ItemId"
                         JOIN "LocationDetails" LD ON LD."LocationId" = PD."InventoryLocationId"
                WHERE PD."CreatedDate"::DATE BETWEEN pStartDate::DATE AND pEndDate::DATE
                GROUP BY PD."CreatedDate"::DATE, ID."ItemName", LD."LocationName"
            ),
                 sales_data AS (
                     SELECT
                         SD."CreatedDate"::DATE AS created_date,
                         ID."ItemName",
                         LD."LocationName",
                         SUM(SD."ItemCount") AS sold_item_count,
                         SUM(SD."SalesPrice") AS total_sales_price
                     FROM "SalesDetails" SD
                              JOIN "ItemsDetails" ID ON ID."ItemId" = SD."ItemId"
                              JOIN "LocationDetails" LD ON LD."LocationId" = SD."InventoryLocationId"
                     WHERE SD."CreatedDate"::DATE BETWEEN pStartDate::DATE AND pEndDate::DATE
                     GROUP BY SD."CreatedDate"::DATE, ID."ItemName", LD."LocationName"
                 )
            SELECT
                COALESCE(sd.created_date, pd.created_date)::CHARACTER VARYING AS rCreatedOn,
                COALESCE(sd."ItemName", pd."ItemName") AS lItemName,
                COALESCE(pd.inventory_item_count, 0) AS lInventoryItemCount,
                COALESCE(sd.sold_item_count, 0) AS lSoldItemCount,
                COALESCE(sd."LocationName", pd."LocationName") AS lLocationName,
                COALESCE(sd.total_sales_price, 0) AS lRevenue,
                COALESCE(sd.total_sales_price, 0) - COALESCE(pd.total_purchase_price, 0) AS lProfit
            FROM sales_data sd
                     FULL OUTER JOIN purchase_data pd
                                     ON sd.created_date = pd.created_date
                                         AND sd."ItemName" = pd."ItemName"
                                         AND sd."LocationName" = pd."LocationName";

        IF NOT FOUND THEN
            RAISE EXCEPTION ''No data found in the selected date range.'';
        END IF;
    END;
';

-- Get all sales details by date range and location id
CREATE OR REPLACE FUNCTION get_revenue_details_by_date_range_and_location(
    pStartDate character varying,
    pEndDate character varying,
    pLocationId BIGINT
)
    RETURNS TABLE (
                      lCreatedOn CHARACTER VARYING,
                      lItemName CHARACTER VARYING,
                      lInventoryItemCount BIGINT,
                      lSoldItemCount BIGINT,
                      lRevenue NUMERIC(10, 2),
                      lProfit NUMERIC(10, 2)
                  )
    LANGUAGE plpgsql
AS
'
    BEGIN
        RETURN QUERY
            WITH purchase_data AS (
                SELECT
                    PD."CreatedDate"::DATE AS created_date,
                    ID."ItemName",
                    SUM(PD."ItemCount") AS inventory_item_count,
                    SUM(PD."PurchasePrice") AS total_purchase_price
                FROM "PurchaseDetails" PD
                         JOIN "ItemsDetails" ID ON ID."ItemId" = PD."ItemId"
                         JOIN "LocationDetails" LD ON LD."LocationId" = PD."InventoryLocationId"
                WHERE PD."CreatedDate"::DATE BETWEEN pStartDate::DATE AND pEndDate::DATE
                  AND PD."InventoryLocationId" = pLocationId
                GROUP BY PD."CreatedDate"::DATE, ID."ItemName"
            ),
                 sales_data AS (
                     SELECT
                         SD."CreatedDate"::DATE AS created_date,
                         ID."ItemName",
                         SUM(SD."ItemCount") AS sold_item_count,
                         SUM(SD."SalesPrice") AS total_sales_price
                     FROM "SalesDetails" SD
                              JOIN "ItemsDetails" ID ON ID."ItemId" = SD."ItemId"
                              JOIN "LocationDetails" LD ON LD."LocationId" = SD."InventoryLocationId"
                     WHERE SD."CreatedDate"::DATE BETWEEN pStartDate::DATE AND pEndDate::DATE
                       AND SD."InventoryLocationId" = pLocationId
                     GROUP BY SD."CreatedDate"::DATE, ID."ItemName"
                 )
            SELECT
                COALESCE(sd.created_date, pd.created_date)::CHARACTER VARYING AS rCreatedOn,
                COALESCE(sd."ItemName", pd."ItemName") AS lItemName,
                COALESCE(pd.inventory_item_count, 0) AS lInventoryItemCount,
                COALESCE(sd.sold_item_count, 0) AS lSoldItemCount,
                COALESCE(sd.total_sales_price, 0) AS lRevenue,
                COALESCE(sd.total_sales_price, 0) - COALESCE(pd.total_purchase_price, 0) AS lProfit
            FROM sales_data sd
                     FULL OUTER JOIN purchase_data pd
                                     ON sd.created_date = pd.created_date
                                         AND sd."ItemName" = pd."ItemName";

        IF NOT FOUND THEN
            RAISE EXCEPTION ''No data found in the selected date range.'';
        END IF;
    END;
';
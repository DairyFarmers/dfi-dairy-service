-- Get User details by email id
CREATE OR REPLACE FUNCTION get_user_details_by_email_id(
    pEmailID CHARACTER VARYING
)
    RETURNS TABLE
            (
                lFullName     CHARACTER VARYING,
                lMobileNumber CHARACTER VARYING,
                lUserRole     CHARACTER VARYING,
                lUserRoleId   BIGINT,
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
                   ud."UserRoleId",
                   ln."LocationName"
            FROM "UserDetails" ud
                     LEFT JOIN "UserRole" ur ON ur."UserRoleId" = ud."UserRoleId"
                     LEFT JOIN "LocationDetails" ln ON ln."LocationId" = ud."UserLocationId"
            WHERE ud."E-mail" = pEmailID;

        IF NOT FOUND THEN
            RAISE EXCEPTION ''User Not Found in the database'';
        END IF;
    END;
';


-- Get purchase details by date range

-- CREATE OR REPLACE FUNCTION get_purchase_details
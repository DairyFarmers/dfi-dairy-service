-- Use this table for viewing the entire B2bRequestDetails

CREATE TABLE IF NOT EXISTS "B2bRequestDetails"(
    "B2bRequestId" BIGSERIAL,
    "B2bUserDetailId" INT,
    "B2bLocationId" INT,
    "ItemName" CHARACTER VARYING,
    "ItemId" INT,
    "ItemCount" BIGINT,
    "UnitPriceForAItem" NUMERIC(10,2),
    "RequestStatus" INT,
    "CancelReason" CHARACTER VARYING
);


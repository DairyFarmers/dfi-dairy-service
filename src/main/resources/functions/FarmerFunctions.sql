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
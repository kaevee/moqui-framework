# Sales Domain Summary

Date: `2026-04-01`
Domain index: `agents/domains/sales/SALES.md`

## Outcome

- Created a layered Sales domain dossier set using namespace scope.
- Full Detail selector declared as `namespace` with pattern `mantle.sales.*` from `SalesEntities.xml`.
- Added reference-only intersections for downstream FK consumers outside Sales namespace.

## Artifacts Added

- `agents/domains/sales/SALES.md`
- `agents/domains/sales/docs/001_sales-opportunity-core-dossier.md`
- `agents/domains/sales/docs/002_sales-forecast-dossier.md`
- `agents/domains/sales/docs/003_sales-need-dossier.md`
- `agents/domains/sales/docs/090_sales-reference-intersections-dossier.md`
- `agents/domains/sales/summaries/001_sales-domain-summary.md`

## Scope Decision

- `prefix` selector (`Sales*`) was rejected because it would miss `PartyNeed`, which is part of `mantle.sales.need`.
- `namespace` selector was used per AGENTS exception rule: `mantle.sales.*`.

## Coverage Notes

- Full-detail inventory documented for all 10 `mantle.sales.*` entities in `SalesEntities.xml`.
- Direct `mantle-usl` service hits for Sales full-detail entities: `NONE`.
- Direct stock `SimpleScreens` hits for Sales full-detail entities: `NONE`.
- Related Sales namespace entry points were documented separately:
  - `mantle.sales.AccountServices` (`create#Account`, `create#Contact`, `add#Contact`, `delete#Contact`)
  - `mantle.sales.SalesReportServices` (`get#SalesOrderSummary`, `get#SalesInvoiceSummary`, `get#SalesTopProduct`, `get#InvoicesByCustomerClass`)

## Reference-Only Intersections Captured

- `OrderItem.salesOpportunityId -> SalesOpportunity`
- `InvoiceItem.salesOpportunityId -> SalesOpportunity`

## Open Follow-up

- Re-scan and expand service/screen wiring if stock runtime begins directly operating on `SalesOpportunity*`, `SalesForecast*`, or `PartyNeed`.
# Sales Domain Dossier

Target Domain: `Sales`
Scope: `002` adjacent layer for forecast entities in `mantle.sales.forecast`.
Out of Scope: Opportunity core (`001`), need (`003`), and cross-domain reference-only intersections (`090`).

See `001_sales-opportunity-core-dossier.md` for core Sales context.

## 1. Domain Boundary

Full-detail entities in this layer:
- `SalesForecast`
- `SalesForecastDetail`

Primary source file scanned:
- `runtime/component/mantle-udm/entity/SalesEntities.xml`

## 2. Full Detail Set (Entity Inventory)

`SalesForecast` (`runtime/component/mantle-udm/entity/SalesEntities.xml`)
- PK: `salesForecastId`
- FKs and targets: `parentSalesForecastId -> mantle.sales.forecast.SalesForecast`, `organizationPartyId -> mantle.party.Party`, `internalPartyId -> mantle.party.Party`, `timePeriodId -> mantle.party.time.TimePeriod`, `currencyUomId -> moqui.basic.Uom`
- Enum/status fields and type references: `NONE`
- Key business fields: `quotaAmount`, `forecastAmount`, `bestCaseAmount`, `closedAmount`, `percentOfQuotaForecast`, `percentOfQuotaClosed`, `pipelineAmount`

`SalesForecastDetail` (`runtime/component/mantle-udm/entity/SalesEntities.xml`)
- PK: `salesForecastId`, `salesForecastDetailSeqId`
- FKs and targets: `salesForecastId -> mantle.sales.forecast.SalesForecast`, `quantityUomId -> moqui.basic.Uom`, `productId -> mantle.product.Product`, `productCategoryId -> mantle.product.category.ProductCategory`
- Enum/status fields and type references: `NONE`
- Key business fields: `amount`, `quantity`

## 2A. Commented-Out Entity/View Definitions

Scanned source:
- `runtime/component/mantle-udm/entity/SalesEntities.xml`

Commented-out `<entity ...>` / `<view-entity ...>` definitions:
- `NONE`

## 3. Reference-Only Set (FK Consumer Map)

Non-`mantle.sales.*` FK consumers discovered for forecast entities:
- `NONE`

## 4. Relationship Map (Domain-Internal Adjacency)

- `SalesForecast` supports a parent-child hierarchy via `parentSalesForecastId`.
- `SalesForecastDetail` stores forecast lines (amount/quantity) under `SalesForecast`.
- Forecast lines can be tied to either `productId` and/or `productCategoryId`.

## 5. Service Wiring (Full Detail Set)

Service files touching this layer's full detail entities in `runtime/component/mantle-usl/service`:
- `NONE`

Related Sales namespace services (indirect/reporting surfaces):
- `runtime/component/mantle-usl/service/mantle/sales/SalesReportServices.xml`

## 6. Screen Wiring (SimpleScreens only — Full Detail Set)

Stock `SimpleScreens` touching this layer's full detail entities:
- `NONE`

Related stock screens invoking Sales report services:
- `runtime/component/SimpleScreens/screen/SimpleScreens/Accounting/Reports/SalesSummary.xml`

## 7. Canonical Flows

Forecast setup:
1. Create top-level `SalesForecast` for organization/internal party and period.
2. Add child forecasts via `parentSalesForecastId` where rollup structure is needed.

Forecast detailing:
1. Add `SalesForecastDetail` rows per product/product-category with `amount` and `quantity`.
2. Track quota/forecast/closed values at header level while using detail lines for dimensional analysis.

## 8. Agent Quick-Start

- Keep `SalesForecast` hierarchy semantics stable (`parentSalesForecastId`).
- Use `SalesForecastDetail` for granular product/category projections instead of overloading header amounts.
- If forecast processing services are introduced, add them here and re-check `SalesSummary` integration assumptions.

## 9. Open Questions / Follow-ups

- No stock services/screens currently query or mutate `SalesForecast*` entities directly.

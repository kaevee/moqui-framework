# SALES.md

## Purpose

Entry index for the Sales domain dossier.

Use this file first, then load `001` + the smallest relevant layer doc.

## Scope Rule (Authoritative)

- Full Detail Set selector: `namespace`
- Full Detail Set pattern: `mantle.sales.*`
- Entity inventory source: `runtime/component/mantle-udm/entity/SalesEntities.xml`
- Coverage per Full Detail entity: entity + touching `mantle-usl` services + touching stock `SimpleScreens`.
- If service/screen does not exist: record `NONE`.
- Reference-Only Set: non-`mantle.sales.*` entities with FK references into the Full Detail Set.

## Dossier Layers

Core and Sales-owned layers:
- [001 Sales Opportunity Core](docs/001_sales-opportunity-core-dossier.md)
- [002 Sales Forecast Layer](docs/002_sales-forecast-dossier.md)
- [003 Sales Need Layer](docs/003_sales-need-dossier.md)

Reference-only downstream intersections:
- [090 Sales Reference Intersections](docs/090_sales-reference-intersections-dossier.md)

## Start Here by Task

- Opportunity records, stage model, and opportunity bridges to order/work/tracking: `001`
- Forecast tree and forecast detail amounts/quantities: `002`
- Party/customer need capture records: `003`
- Cross-domain FK impact (Order and Invoice usage of SalesOpportunity): `090`

## Canonical Sales Flows

Canonical flow definitions are distributed across layers in section `7` of:
- `001`: opportunity data model lifecycle and bridge usage
- `002`: forecast hierarchy and detail lifecycle
- `003`: party need capture lifecycle
- `090`: cross-domain SalesOpportunity attribution

## Source-of-Truth Implementation Files

Entity model files:
- `runtime/component/mantle-udm/entity/SalesEntities.xml`
- `runtime/component/mantle-udm/entity/OrderEntities.xml` (reference-only consumers)
- `runtime/component/mantle-udm/entity/AccountingAccountEntities.xml` (reference-only consumers)

Service files touching `mantle.sales.*` entities:
- `NONE` (no direct Sales-entity hits in `runtime/component/mantle-usl/service`)

Related Sales namespace service files (indirect/domain-entry surfaces):
- `runtime/component/mantle-usl/service/mantle/sales/AccountServices.xml`
- `runtime/component/mantle-usl/service/mantle/sales/SalesReportServices.xml`
- `runtime/component/mantle-usl/service/mantle.rest.xml` (REST exposes `mantle.sales.AccountServices.create#Account` and `create#Contact`; does not expose `add#Contact`/`delete#Contact`)

Stock screen/template files touching `mantle.sales.*` entities:
- `NONE` (no direct Sales-entity hits in `runtime/component/SimpleScreens`)

Related stock screens invoking Sales namespace services:
- `runtime/component/SimpleScreens/screen/SimpleScreens/Customer/FindCustomer.xml`
- `runtime/component/SimpleScreens/screen/SimpleScreens/Customer/EditCustomer.xml`
- `runtime/component/SimpleScreens/screen/SimpleScreens/Party/FindParty.xml`
- `runtime/component/SimpleScreens/screen/SimpleScreens/Party/EditParty.xml`
- `runtime/component/SimpleScreens/screen/SimpleScreens/Accounting/Reports/SalesSummary.xml`

## Maintenance Checklist

- Keep namespace selector strict to `mantle.sales.*` (captures non-prefix entity `PartyNeed`).
- Keep layer boundaries by subpackage: opportunity/forecast/need.
- Keep `090` reference-only; do not expand into full Order/Accounting downstream behavior.
- Re-verify service/screen wiring if Sales entity services or stock screens are added later.

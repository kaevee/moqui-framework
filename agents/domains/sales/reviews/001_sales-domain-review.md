# Sales Domain Dossier Review

Date: `2026-04-01`
Domain index: `agents/domains/sales/SALES.md`
Reviewed artifacts: all files under `agents/domains/sales/docs/` and `agents/domains/sales/SALES.md`

Verified all dossier claims against source files. Findings below.

## Entity Inventory: PASS

All 10 entities in `SalesEntities.xml` are accounted for across layers 001/002/003:
- `001`: 7 opportunity entities (`SalesOpportunity`, `SalesOpportunityParty`, `SalesOpportunityStage`, `SalesOpportunityWorkEffort`, `SalesOpportunityOrder`, `SalesOpportunityCompetitor`, `SalesOpportunityTracking`)
- `002`: 2 forecast entities (`SalesForecast`, `SalesForecastDetail`)
- `003`: 1 need entity (`PartyNeed`)

Field inventories (PKs, FKs, enum fields, business fields) verified correct against XML source for all 10 entities. No omitted fields or incorrect FK targets found.

## Commented-Out Definitions: PASS

`SalesEntities.xml` contains no commented-out `<entity>` or `<view-entity>` blocks. Dossier correctly records `NONE` in all layers.

## Service Wiring: PASS

- Confirmed no services in `mantle-usl/service/` directly operate on any `mantle.sales.*` entity. `NONE` is correct.
- `AccountServices.xml` has 4 services: `create#Account`, `create#Contact`, `add#Contact`, `delete#Contact` — matches dossier.
- `SalesReportServices.xml` has 4 services: `get#SalesOrderSummary`, `get#SalesInvoiceSummary`, `get#SalesTopProduct`, `get#InvoicesByCustomerClass` — matches dossier.
- REST exposure in `mantle.rest.xml` covers `create#Account` and `create#Contact` only (not all 4 AccountServices). SALES.md lists the file but doesn't clarify which services are REST-exposed. **Minor gap — not blocking.**

## Screen Wiring: PASS

- Confirmed no stock `SimpleScreens` directly reference any `mantle.sales.*` entity name or field. `NONE` is correct.
- Related screens invoking Sales namespace services verified:
  - `FindCustomer.xml`: calls `create#Account`, `create#Contact` — confirmed.
  - `EditCustomer.xml`: calls `add#Contact`, `create#Contact`, `delete#Contact` — confirmed.
  - `FindParty.xml`: calls `create#Account`, `create#Contact` — confirmed.
  - `EditParty.xml`: calls `add#Contact`, `create#Contact`, `delete#Contact` — confirmed.
  - `SalesSummary.xml`: calls all 4 `SalesReportServices` — confirmed.

## Reference-Only FK Consumers: PASS

- `OrderItem.salesOpportunityId` at `OrderEntities.xml:412,452` — confirmed.
- `InvoiceItem.salesOpportunityId` at `AccountingAccountEntities.xml:674,701` — confirmed.
- No external FK consumers found for `SalesForecast*` or `PartyNeed` outside `SalesEntities.xml` — matches dossier.

## Scope Selector Decision: PASS

Namespace selector `mantle.sales.*` is justified. Prefix `Sales*` would miss `PartyNeed` (package `mantle.sales.need`). All 10 entities share the `mantle.sales` package prefix.

## Minor Issues Found

1. **REST exposure detail**: `SALES.md` lists `mantle.rest.xml` as a related service file but doesn't specify which AccountServices are REST-exposed (`create#Account` and `create#Contact` only; `add#Contact` and `delete#Contact` are not). Low severity — could cause an agent to assume all 4 are REST-available.

2. **`SalesOpportunityCompetitor` missing FK relationship for `competitorPartyId`**: The entity XML defines `competitorPartyId` as a PK field but has no `<relationship>` to `mantle.party.Party` for it. The `001` dossier correctly notes only the `SalesOpportunity` relationship and flags `positionEnumId` as "no explicit relationship declared." This is accurate documentation of the source, not a dossier error, but agents should be aware the FK to Party is implicit/conventional rather than declared.

## Verdict

**PASS** — dossier set is accurate and complete against current source. No blocking issues. The two minor items above are informational.

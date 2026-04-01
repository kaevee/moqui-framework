# Sales Domain Dossier

Target Domain: `Sales`
Scope: `090` reference-only intersections for non-`mantle.sales.*` entities that FK into Sales Full Detail entities.
Out of Scope: Full downstream Order/Accounting behavior.

See `001_sales-opportunity-core-dossier.md` for core Sales context.

## 1. Domain Boundary

- Reference-only layer for entities outside `mantle.sales.*` that point to Sales keys.
- Full Detail selector for Sales domain remains namespace-based: `mantle.sales.*`.

Source files scanned for reference-only mappings:
- `runtime/component/mantle-udm/entity/OrderEntities.xml`
- `runtime/component/mantle-udm/entity/AccountingAccountEntities.xml`

## 2. Full Detail Set (Entity Inventory)

- `NONE` (reference-only layer)

## 2A. Commented-Out Entity/View Definitions

Scanned sources:
- `runtime/component/mantle-udm/entity/OrderEntities.xml`
- `runtime/component/mantle-udm/entity/AccountingAccountEntities.xml`

Commented-out `<entity ...>` / `<view-entity ...>` definitions:
- `NONE`

## 3. Reference-Only Set (FK Consumer Map)

`OrderItem` (`runtime/component/mantle-udm/entity/OrderEntities.xml`)
- FK field(s) to Full Detail: `salesOpportunityId -> SalesOpportunity`
- Usage note: order line attribution back to originating/related sales opportunity.

`InvoiceItem` (`runtime/component/mantle-udm/entity/AccountingAccountEntities.xml`)
- FK field(s) to Full Detail: `salesOpportunityId -> SalesOpportunity`
- Usage note: invoice-line attribution for revenue/accounting traceability to opportunity context.

## 4. Relationship Map (Domain-Internal Adjacency)

- Order-side and invoice-side item records both consume `SalesOpportunity` keys for attribution.
- No external consumers were found for `SalesForecast*` or `PartyNeed` in scanned model files.

## 5. Service Wiring (Full Detail Set)

Direct service hits in `runtime/component/mantle-usl/service` for Sales FK fields/entities (`salesOpportunityId`, `SalesOpportunity`, `SalesForecast`, `PartyNeed`):
- `NONE`

## 6. Screen Wiring (SimpleScreens only — Full Detail Set)

Direct stock `SimpleScreens` hits for Sales FK fields/entities (`salesOpportunityId`, `SalesOpportunity`, `SalesForecast`, `PartyNeed`):
- `NONE`

## 7. Canonical Flows

Order/invoice attribution:
1. `OrderItem` may carry `salesOpportunityId` at order capture/management time.
2. `InvoiceItem` may carry `salesOpportunityId` during invoicing.
3. Sales attribution can be traced from order/invoice lines back to opportunity records.

## 8. Agent Quick-Start

- Keep this layer reference-only; do not expand into full Order or Accounting domain logic.
- Use this doc when changing `SalesOpportunity` identifiers or relationship semantics.
- Re-scan downstream domains if new sales attribution fields are introduced.

## 9. Open Questions / Follow-ups

- If stock services/screens begin to surface `salesOpportunityId` directly, add those runtime paths here for traceability.

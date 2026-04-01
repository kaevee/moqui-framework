# Sales Domain Dossier

Target Domain: `Sales`
Scope: `001` core layer for opportunity entities in `mantle.sales.opportunity`.
Out of Scope: Forecast (`002`), need (`003`), and cross-domain reference-only intersections (`090`).

## 1. Domain Boundary

Full Detail Set selector (domain-wide):
- Type: `namespace`
- Pattern: `mantle.sales.*`
- Source: `runtime/component/mantle-udm/entity/SalesEntities.xml`

Full-detail entities in this layer:
- `SalesOpportunity`
- `SalesOpportunityParty`
- `SalesOpportunityStage`
- `SalesOpportunityWorkEffort`
- `SalesOpportunityOrder`
- `SalesOpportunityCompetitor`
- `SalesOpportunityTracking`

Layer map for all namespace entities:
- `001`: `SalesOpportunity`, `SalesOpportunityParty`, `SalesOpportunityStage`, `SalesOpportunityWorkEffort`, `SalesOpportunityOrder`, `SalesOpportunityCompetitor`, `SalesOpportunityTracking`
- `002`: `SalesForecast`, `SalesForecastDetail`
- `003`: `PartyNeed`
- `090` (reference-only): non-`mantle.sales.*` FK consumers

Primary source file scanned:
- `runtime/component/mantle-udm/entity/SalesEntities.xml`

## 2. Full Detail Set (Entity Inventory)

`SalesOpportunity` (`runtime/component/mantle-udm/entity/SalesEntities.xml`)
- PK: `salesOpportunityId`
- FKs and targets: `typeEnumId -> moqui.basic.Enumeration`, `accountPartyId -> mantle.party.Party`, `currencyUomId -> moqui.basic.Uom`, `opportunityStageId -> mantle.sales.opportunity.SalesOpportunityStage`, `marketingCampaignId -> mantle.marketing.campaign.MarketingCampaign`
- Enum/status fields and type references: `typeEnumId (SalesOpportunity type enum)`, `opportunityStageId (SalesOpportunityStage reference)`
- Key business fields: `opportunityName`, `description`, `nextStep`, `estimatedAmount`, `estimatedProbability`, `estimatedCloseDate`, `dataSourceId`

`SalesOpportunityParty` (`runtime/component/mantle-udm/entity/SalesEntities.xml`)
- PK: `salesOpportunityId`, `partyId`, `roleTypeId`, `fromDate`
- FKs and targets: `salesOpportunityId -> mantle.sales.opportunity.SalesOpportunity`, `partyId -> mantle.party.Party`, `roleTypeId -> mantle.party.RoleType`
- Enum/status fields and type references: `NONE`
- Key business fields: `thruDate`

`SalesOpportunityStage` (`runtime/component/mantle-udm/entity/SalesEntities.xml`)
- PK: `opportunityStageId`
- FKs and targets: `NONE`
- Enum/status fields and type references: `NONE`
- Key business fields: `description`, `defaultProbability`, `sequenceNum`

`SalesOpportunityWorkEffort` (`runtime/component/mantle-udm/entity/SalesEntities.xml`)
- PK: `salesOpportunityId`, `workEffortId`
- FKs and targets: `salesOpportunityId -> mantle.sales.opportunity.SalesOpportunity`, `workEffortId -> mantle.work.effort.WorkEffort`
- Enum/status fields and type references: `NONE`
- Key business fields: `NONE` (bridge)

`SalesOpportunityOrder` (`runtime/component/mantle-udm/entity/SalesEntities.xml`)
- PK: `salesOpportunityId`, `orderId`
- FKs and targets: `salesOpportunityId -> mantle.sales.opportunity.SalesOpportunity`, `orderId -> mantle.order.OrderHeader`
- Enum/status fields and type references: `NONE`
- Key business fields: `NONE` (bridge)

`SalesOpportunityCompetitor` (`runtime/component/mantle-udm/entity/SalesEntities.xml`)
- PK: `salesOpportunityId`, `competitorPartyId`
- FKs and targets: `salesOpportunityId -> mantle.sales.opportunity.SalesOpportunity`
- Enum/status fields and type references: `positionEnumId` (enum-like field; no explicit relationship declared in entity)
- Key business fields: `strengths`, `weaknesses`
- Modeling note: `competitorPartyId` is conventionally a party reference but no explicit `<relationship>` is declared in source.

`SalesOpportunityTracking` (`runtime/component/mantle-udm/entity/SalesEntities.xml`)
- PK: `salesOpportunityId`, `trackingCodeId`
- FKs and targets: `salesOpportunityId -> mantle.sales.opportunity.SalesOpportunity`, `trackingCodeId -> mantle.marketing.tracking.TrackingCode`
- Enum/status fields and type references: `NONE`
- Key business fields: `receivedDate`

## 2A. Commented-Out Entity/View Definitions

Scanned source:
- `runtime/component/mantle-udm/entity/SalesEntities.xml`

Commented-out `<entity ...>` / `<view-entity ...>` definitions:
- `NONE`

## 3. Reference-Only Set (FK Consumer Map)

Cross-domain consumers of opportunity keys:
- `OrderItem.salesOpportunityId -> SalesOpportunity` (`runtime/component/mantle-udm/entity/OrderEntities.xml`, see `090_sales-reference-intersections-dossier.md`)
- `InvoiceItem.salesOpportunityId -> SalesOpportunity` (`runtime/component/mantle-udm/entity/AccountingAccountEntities.xml`, see `090_sales-reference-intersections-dossier.md`)

## 4. Relationship Map (Domain-Internal Adjacency)

- `SalesOpportunity` is the opportunity root.
- `SalesOpportunityStage` provides stage lookup for `SalesOpportunity.opportunityStageId`.
- Opportunity associations are modeled through bridge entities:
  - party assignments (`SalesOpportunityParty`)
  - work links (`SalesOpportunityWorkEffort`)
  - order links (`SalesOpportunityOrder`)
  - competitor notes (`SalesOpportunityCompetitor`)
  - tracking attribution (`SalesOpportunityTracking`)

## 5. Service Wiring (Full Detail Set)

Service files touching this layer's full detail entities in `runtime/component/mantle-usl/service`:
- `NONE`

Related Sales namespace services (indirect, not direct touches of `SalesOpportunity*` entities):
- `runtime/component/mantle-usl/service/mantle/sales/AccountServices.xml`
- `runtime/component/mantle-usl/service/mantle.rest.xml` (REST exposure for `mantle.sales.AccountServices`)

## 6. Screen Wiring (SimpleScreens only — Full Detail Set)

Stock `SimpleScreens` touching this layer's full detail entities:
- `NONE`

Related stock screens invoking Sales namespace services:
- `runtime/component/SimpleScreens/screen/SimpleScreens/Customer/FindCustomer.xml`
- `runtime/component/SimpleScreens/screen/SimpleScreens/Customer/EditCustomer.xml`
- `runtime/component/SimpleScreens/screen/SimpleScreens/Party/FindParty.xml`
- `runtime/component/SimpleScreens/screen/SimpleScreens/Party/EditParty.xml`

## 7. Canonical Flows

Opportunity modeling:
1. Create `SalesOpportunity` with account, stage, and estimated values.
2. Add party assignments using `SalesOpportunityParty`.
3. Link fulfillment/execution context using `SalesOpportunityWorkEffort` and `SalesOpportunityOrder`.
4. Link attribution/lead source via `SalesOpportunityTracking`.

Stage progression:
1. Define stage records in `SalesOpportunityStage`.
2. Move `SalesOpportunity.opportunityStageId` across stages as the opportunity matures.

## 8. Agent Quick-Start

- Start with this layer when adding/changing opportunity relationships or stage semantics.
- Keep composite keys exact on all bridge entities to avoid accidental duplicate joins.
- Load `090` before changing `salesOpportunityId` semantics because Order/Invoice entities consume it.

## 9. Open Questions / Follow-ups

- No stock services currently operate directly on `SalesOpportunity*` entities; if added later, update this layer with concrete service entry points.

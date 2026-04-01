# Marketing Domain Dossier

Target Domain: `Marketing`
Scope: `001` core layer for campaign lifecycle entities in `mantle.marketing.campaign`.
Out of Scope: Contact list (`002`), segment (`003`), tracking (`004`), and cross-domain reference-only intersections (`090`).

## 1. Domain Boundary

Full Detail Set selector (domain-wide):
- Type: `namespace`
- Pattern: `mantle.marketing.*`
- Source: `runtime/component/mantle-udm/entity/MarketingEntities.xml`

Full-detail entities in this layer:
- `MarketingCampaign`
- `MarketingCampaignNote`
- `MarketingCampaignParty`

Layer map for all namespace entities:
- `001`: `MarketingCampaign`, `MarketingCampaignNote`, `MarketingCampaignParty`
- `002`: `ContactList`, `ContactListEmail`, `ContactListCommStatus`, `ContactListParty`
- `003`: `MarketInterest`, `MarketSegment`, `MarketSegmentClassification`, `MarketSegmentDimension`, `MarketSegmentGeo`, `MarketSegmentParty`
- `004`: `TrackingCode`, `TrackingCodeOrder`, `TrackingCodeOrderReturn`, `TrackingCodeVisit`
- `090` (reference-only): non-`mantle.marketing.*` FK consumers

Primary source file scanned:
- `runtime/component/mantle-udm/entity/MarketingEntities.xml`

## 2. Full Detail Set (Entity Inventory)

`MarketingCampaign` (`runtime/component/mantle-udm/entity/MarketingEntities.xml`)
- PK: `marketingCampaignId`
- FKs and targets: `parentCampaignId -> mantle.marketing.campaign.MarketingCampaign`, `statusId -> moqui.basic.StatusItem (MarketingCampaign)`, `costUomId -> moqui.basic.Uom`
- Enum/status fields and type references: `statusId (MarketingCampaign status type)`
- Key business fields: `campaignName`, `campaignSummary`, `budgetedCost`, `actualCost`, `estimatedCost`, `fromDate`, `thruDate`, `isActive`, `convertedLeads`, `expectedResponsePercent`, `expectedRevenue`, `numSent`, `startDate`

`MarketingCampaignNote` (`runtime/component/mantle-udm/entity/MarketingEntities.xml`)
- PK: `marketingCampaignId`, `noteDate`
- FKs and targets: `marketingCampaignId -> mantle.marketing.campaign.MarketingCampaign`
- Enum/status fields and type references: `NONE`
- Key business fields: `noteText`

`MarketingCampaignParty` (`runtime/component/mantle-udm/entity/MarketingEntities.xml`)
- PK: `marketingCampaignId`, `partyId`, `roleTypeId`, `fromDate`
- FKs and targets: `marketingCampaignId -> mantle.marketing.campaign.MarketingCampaign`, `partyId -> mantle.party.Party`, `roleTypeId -> mantle.party.RoleType`
- Enum/status fields and type references: `NONE`
- Key business fields: `thruDate`

## 2A. Commented-Out Entity/View Definitions

Scanned source:
- `runtime/component/mantle-udm/entity/MarketingEntities.xml`

Commented-out `<entity ...>` / `<view-entity ...>` definitions:
- `NONE`

## 3. Reference-Only Set (FK Consumer Map)

Cross-layer and downstream consumers of campaign keys:
- `ContactList.marketingCampaignId -> MarketingCampaign` (see `002_marketing-contact-list-dossier.md`)
- `TrackingCode.marketingCampaignId -> MarketingCampaign` (see `004_marketing-tracking-dossier.md`)
- `SalesOpportunity.marketingCampaignId -> MarketingCampaign` (`runtime/component/mantle-udm/entity/SalesEntities.xml`, see `090_marketing-reference-intersections-dossier.md`)

## 4. Relationship Map (Domain-Internal Adjacency)

- `MarketingCampaign` is the layer root.
- `MarketingCampaign` supports hierarchy via `parentCampaignId`.
- `MarketingCampaignNote` stores timestamped notes per campaign.
- `MarketingCampaignParty` assigns participating parties and roles over date ranges.

## 5. Service Wiring (Full Detail Set)

Service files touching this layer in `runtime/component/mantle-usl/service`:
- `NONE`

## 6. Screen Wiring (SimpleScreens only — Full Detail Set)

Stock `SimpleScreens` touching this layer:
- `NONE`

## 7. Canonical Flows

Campaign setup:
1. Create `MarketingCampaign` with campaign metadata and status.
2. Add `MarketingCampaignParty` rows for owners/stakeholders with `roleTypeId`.
3. Add `MarketingCampaignNote` rows for audit/history.

Campaign lifecycle status progression:
1. Use `MarketingCampaign.statusId` seeded states (`MKTG_CAMP_PLANNED` -> `MKTG_CAMP_APPROVED` -> `MKTG_CAMP_INPROGRESS` -> `MKTG_CAMP_COMPLETED` or cancellation path).
2. Preserve state transition semantics from seeded `StatusFlowTransition` records.

## 8. Agent Quick-Start

- Start with this layer when changes involve campaign identity, hierarchy, or ownership.
- Treat campaign status as a typed lifecycle (`MarketingCampaign` status type), not a free-form value.
- Load `004` when campaign changes affect attribution/tracking behavior.

## 9. Open Questions / Follow-ups

- No stock service/screen wiring currently references these entities directly; re-scan if Marketing feature services are introduced.

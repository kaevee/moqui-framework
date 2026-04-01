# Marketing Domain Dossier Review

Date: `2026-04-01`
Reviewed artifacts: all files listed in `agents/domains/marketing/summaries/001_marketing-domain-summary.md`

## Methodology

- Read all 7 Marketing domain artifacts (MARKETING.md, 001–004, 090, summary).
- Verified every claim against source files:
  - `runtime/component/mantle-udm/entity/MarketingEntities.xml`
  - `runtime/component/mantle-udm/entity/SalesEntities.xml`
  - `runtime/component/mantle-udm/entity/PartyEntities.xml`
  - `runtime/component/mantle-udm/entity/ProductDefinitionEntities.xml`
  - `runtime/component/mantle-udm/entity/OrderEntities.xml`
  - `runtime/component/mantle-usl/service/` (broad scan)
  - `runtime/component/SimpleScreens/` (broad scan)

## Verification Results

### PASS — Structural Compliance

- [x] Domain index file (`MARKETING.md`) exists and follows required structure.
- [x] Document layering follows `001`–`004` + `090` pattern with self-contained scope per layer.
- [x] All docs use the required section template (`1`–`9`).
- [x] Scope selector correctly declared as `namespace` with pattern `mantle.marketing.*`.
- [x] Scope selector justification is sound: Marketing entities use non-prefix names (`MarketingCampaign*`, `ContactList*`, `MarketSegment*`, `TrackingCode*`), so prefix rule would not capture them under a single prefix.
- [x] Each layer doc references `001` for shared context.

### PASS — Entity Inventory Completeness

- [x] Entity count: 17 entities claimed, 17 entities found in `MarketingEntities.xml`. Verified: `MarketingCampaign`, `MarketingCampaignNote`, `MarketingCampaignParty`, `ContactList`, `ContactListEmail`, `ContactListCommStatus`, `ContactListParty`, `MarketInterest`, `MarketSegment`, `MarketSegmentClassification`, `MarketSegmentDimension`, `MarketSegmentGeo`, `MarketSegmentParty`, `TrackingCode`, `TrackingCodeOrder`, `TrackingCodeOrderReturn`, `TrackingCodeVisit`.
- [x] Layer assignment is correct — entities are grouped by subpackage (`campaign`, `contact`, `segment`, `tracking`).
- [x] PKs verified correct for all 17 entities.
- [x] FK targets verified correct for all 17 entities.
- [x] Enum/status type references verified correct.
- [x] Commented-out entity/view definitions: `NONE` confirmed — no `<!-- <entity` or `<!-- <view-entity` blocks in `MarketingEntities.xml`.

### PASS — Reference-Only Set (FK Consumer Map)

- [x] `SalesOpportunity.marketingCampaignId -> MarketingCampaign` — confirmed at `SalesEntities.xml:40`.
- [x] `SalesOpportunityTracking.trackingCodeId -> TrackingCode` — confirmed at `SalesEntities.xml:93`.
- [x] `CommunicationEvent.contactListId -> ContactList` — confirmed at `PartyEntities.xml:1103`.
- [x] `ProductDbForm.marketSegmentId -> MarketSegment` — confirmed.
- [x] `ProductUomDimension.marketSegmentId -> MarketSegment` — confirmed.
- [x] `ProductOtherIdentification.marketSegmentId -> MarketSegment` — confirmed.
- [x] `ProductParameterOption.marketSegmentId -> MarketSegment` — confirmed.
- [x] `ProductParameterValue.marketSegmentId -> MarketSegment` — confirmed.
- [x] `ProductCategoryIdent.marketSegmentId -> MarketSegment` — confirmed.
- [x] No missed FK consumers found in `OrderEntities.xml` or `ShipmentEntities.xml` (`trackingCode` fields in Shipment are shipment tracking codes, not Marketing `TrackingCode` entity FKs).

### PASS — Service Wiring

- [x] `mantle-usl` direct `mantle.marketing.*` entity-name service hits: `NONE` — confirmed via grep across entire `service/` tree. No service file creates, updates, or queries Marketing namespace entities by entity name.
- [x] 090 reference-trace service files exist and are valid paths:
  - `CommunicationServices.xml` — exists, handles `CommunicationEvent` CRUD.
  - `DuplicateServices.xml` — exists, handles `CommunicationEvent` merge.
  - `GeneralServices.xml` — exists, does `ProductOtherIdentification` lookup.
  - `StoreServices.xml` — exists, creates `ProductOtherIdentification` and `ProductCategoryIdent`.

### PASS — Screen Wiring

- [x] `EditCategory.xml` references `mantle.marketing.segment.MarketSegment` entity-find — confirmed.
- [x] `EditProduct.xml` references `mantle.marketing.segment.MarketSegment` entity-find — confirmed.
- [x] No other stock SimpleScreens reference Marketing namespace entities directly (shipment screens reference `trackingCode` as a ShipmentPackageRouteSeg field, not the Marketing entity).

### PASS — Canonical Flows

- [x] Campaign status lifecycle matches seeded `StatusItem` / `StatusFlowTransition` records in `MarketingEntities.xml:52-65`.
- [x] ContactListParty status lifecycle matches seeded statuses in `MarketingEntities.xml:172-195`.

## Issues Found

### ISSUE-1: Missing field `convertedLeads` in 001 entity inventory (Severity: LOW)

`MarketingCampaign` has a field `convertedLeads` (type `id`, line 43 of `MarketingEntities.xml`) that is not listed in the key business fields of `001_marketing-campaign-core-dossier.md`.

**Impact**: An agent generating campaign-related code might not know this field exists.

**Fix**: Add `convertedLeads` to the `MarketingCampaign` key business fields list in `001`.

### ISSUE-2: Missing relationship `WikiBlogCategory` in 002 entity inventory (Severity: LOW)

`ContactListEmail` has a `relationship type="many" related="moqui.resource.wiki.WikiBlogCategory" short-alias="wikiBlogs"` (line 134 of `MarketingEntities.xml`) that is not documented in `002_marketing-contact-list-dossier.md`.

**Impact**: Minor — this is a `type="many"` relationship (not a FK field on the entity itself), so it is less critical for agent code generation. However, it is a relationship that could matter for content-driven contact-list email flows.

**Fix**: Add a note under `ContactListEmail` in `002` mentioning the `WikiBlogCategory` many-relationship.

### ISSUE-3: 090 service wiring is loosely attributed (Severity: LOW)

The 090 dossier lists `CommunicationServices.xml` as a "CommunicationEvent create/update surface" touching Marketing. In reality, `CommunicationServices.xml` does not reference `contactListId` or `ContactList` at all — it handles general `CommunicationEvent` CRUD. The linkage is indirect: `CommunicationEvent` has a `contactListId` field at the entity level, but the listed service file does not specifically process that field.

Similarly, `DuplicateServices.xml` updates `CommunicationEvent` rows during party merge but does not specifically handle `contactListId`.

**Impact**: An agent reading 090 might overestimate how well-wired Marketing is into these services. The data model has the FK, but the services don't specifically operate on it.

**Fix**: Add a qualifier in the 090 service section noting these are indirect/entity-level references, not services that explicitly handle the Marketing FK fields.

## Summary Verdict

**PASS with 3 low-severity issues.**

The dossier set is well-structured, correctly scoped, and accurate in its core claims. Entity inventory, FK consumer map, service NONE claims, screen references, and commented-out entity coverage are all verified correct. The three issues are minor completeness/precision gaps that do not affect the reliability of the dossier for agent use.

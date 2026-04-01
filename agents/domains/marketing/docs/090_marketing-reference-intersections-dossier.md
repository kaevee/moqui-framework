# Marketing Domain Dossier

Target Domain: `Marketing`
Scope: `090` reference-only intersections for non-`mantle.marketing.*` entities that FK into Marketing Full Detail entities.
Out of Scope: Full downstream Sales/Party/Product domain behavior.

See `001_marketing-campaign-core-dossier.md` for core Marketing context.

## 1. Domain Boundary

- Reference-only layer for entities outside `mantle.marketing.*` that point to Marketing keys.
- Full Detail selector for Marketing domain remains namespace-based: `mantle.marketing.*`.

Source files scanned for reference-only mappings:
- `runtime/component/mantle-udm/entity/SalesEntities.xml`
- `runtime/component/mantle-udm/entity/PartyEntities.xml`
- `runtime/component/mantle-udm/entity/ProductDefinitionEntities.xml`

## 2. Full Detail Set (Entity Inventory)

- `NONE` (reference-only layer)

## 2A. Commented-Out Entity/View Definitions

Scanned sources:
- `runtime/component/mantle-udm/entity/SalesEntities.xml`
- `runtime/component/mantle-udm/entity/PartyEntities.xml`
- `runtime/component/mantle-udm/entity/ProductDefinitionEntities.xml`

Commented-out `<entity ...>` / `<view-entity ...>` definitions:
- `NONE`

## 3. Reference-Only Set (FK Consumer Map)

`SalesOpportunity` (`runtime/component/mantle-udm/entity/SalesEntities.xml`)
- FK field(s) to Full Detail: `marketingCampaignId -> MarketingCampaign`
- Usage note: campaign attribution on sales opportunity records.

`SalesOpportunityTracking` (`runtime/component/mantle-udm/entity/SalesEntities.xml`)
- FK field(s) to Full Detail: `trackingCodeId -> TrackingCode`
- Usage note: ties opportunities to inbound/outbound tracking attribution.

`CommunicationEvent` (`runtime/component/mantle-udm/entity/PartyEntities.xml`)
- FK field(s) to Full Detail: `contactListId -> ContactList`
- Usage note: links communication records to marketing contact lists.

`ProductDbForm` (`runtime/component/mantle-udm/entity/ProductDefinitionEntities.xml`)
- FK field(s) to Full Detail: `marketSegmentId -> MarketSegment`
- Usage note: form applicability can be segmented by market segment.

`ProductUomDimension` (`runtime/component/mantle-udm/entity/ProductDefinitionEntities.xml`)
- FK field(s) to Full Detail: `marketSegmentId -> MarketSegment`
- Usage note: dimension values can vary by segment.

`ProductOtherIdentification` (`runtime/component/mantle-udm/entity/ProductDefinitionEntities.xml`)
- FK field(s) to Full Detail: `marketSegmentId -> MarketSegment`
- Usage note: alternate identifiers can be segment-specific.

`ProductParameterOption` (`runtime/component/mantle-udm/entity/ProductDefinitionEntities.xml`)
- FK field(s) to Full Detail: `marketSegmentId -> MarketSegment`
- Usage note: parameter options can be restricted by segment.

`ProductParameterValue` (`runtime/component/mantle-udm/entity/ProductDefinitionEntities.xml`)
- FK field(s) to Full Detail: `marketSegmentId -> MarketSegment`
- Usage note: parameter values can be selected by segment membership.

`ProductCategoryIdent` (`runtime/component/mantle-udm/entity/ProductDefinitionEntities.xml`)
- FK field(s) to Full Detail: `marketSegmentId -> MarketSegment`
- Usage note: category identifiers (such as slugs) can be segment-specific.

## 4. Relationship Map (Domain-Internal Adjacency)

- Sales references consume campaign/tracking attribution keys.
- Party communication references consume contact-list keys.
- Product definition references consume market-segment keys.

## 5. Service Wiring (Full Detail Set)

Qualifier:
- Listed files are reference-trace services around reference-only consumer entities; they do not explicitly process Marketing FK fields (for example `contactListId`, `marketSegmentId`, `trackingCodeId`) unless stated.

Reference trace service files in `runtime/component/mantle-usl/service`:
- `runtime/component/mantle-usl/service/mantle/party/CommunicationServices.xml` (generic `CommunicationEvent` create/update surface; indirect link via entity field)
- `runtime/component/mantle-usl/service/mantle/party/DuplicateServices.xml` (generic `CommunicationEvent` merge updates; indirect link via entity field)
- `runtime/component/mantle-usl/service/mantle/GeneralServices.xml` (`ProductOtherIdentification` lookup)
- `runtime/component/mantle-usl/service/mantle/product/StoreServices.xml` (`ProductOtherIdentification` / `ProductCategoryIdent` creation)

Direct `mantle.marketing.*` entity-name service hits:
- `NONE`

## 6. Screen Wiring (SimpleScreens only — Full Detail Set)

Reference trace screens:
- `runtime/component/SimpleScreens/screen/SimpleScreens/Catalog/Category/EditCategory.xml`
- `runtime/component/SimpleScreens/screen/SimpleScreens/Catalog/Product/EditProduct.xml`

## 7. Canonical Flows

Sales attribution linkage:
1. `SalesOpportunity` persists `marketingCampaignId` for campaign attribution.
2. `SalesOpportunityTracking` persists `trackingCodeId` for conversion tracing.

Communication linkage:
1. Party communication records persist `contactListId` on `CommunicationEvent`.
2. Marketing list membership and communication status can be correlated through that FK.

Product segmentation linkage:
1. Catalog/Product screens load MarketSegment options.
2. Product-side records persist `marketSegmentId` in Product definition entities.

## 8. Agent Quick-Start

- Keep this layer reference-only; avoid expanding into full Sales/Party/Product service deep-dives.
- Use this file for impact analysis when Marketing keys are changed.
- Re-scan if new FK consumers are added outside `MarketingEntities.xml`.

## 9. Open Questions / Follow-ups

- If a dedicated Marketing service module is added later, promote relevant references from trace-only to full service wiring in `001-004`.

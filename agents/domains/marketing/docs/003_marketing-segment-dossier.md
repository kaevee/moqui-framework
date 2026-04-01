# Marketing Domain Dossier

Target Domain: `Marketing`
Scope: `003` adjacent layer for segmentation entities in `mantle.marketing.segment`.
Out of Scope: Campaign core (`001`), contact list (`002`), tracking (`004`), and cross-domain reference-only intersections (`090`).

See `001_marketing-campaign-core-dossier.md` for core Marketing campaign context.

## 1. Domain Boundary

Full-detail entities in this layer:
- `MarketInterest`
- `MarketSegment`
- `MarketSegmentClassification`
- `MarketSegmentDimension`
- `MarketSegmentGeo`
- `MarketSegmentParty`

Primary source file scanned:
- `runtime/component/mantle-udm/entity/MarketingEntities.xml`

## 2. Full Detail Set (Entity Inventory)

`MarketInterest` (`runtime/component/mantle-udm/entity/MarketingEntities.xml`)
- PK: `productCategoryId`, `marketSegmentId`, `fromDate`
- FKs and targets: `productCategoryId -> mantle.product.category.ProductCategory`, `marketSegmentId -> mantle.marketing.segment.MarketSegment`
- Enum/status fields and type references: `NONE`
- Key business fields: `thruDate`

`MarketSegment` (`runtime/component/mantle-udm/entity/MarketingEntities.xml`)
- PK: `marketSegmentId`
- FKs and targets: `marketSegmentTypeEnumId -> moqui.basic.Enumeration (MarketSegmentType)`, `parentMarketSegmentId -> mantle.marketing.segment.MarketSegment`, `productStoreId -> mantle.product.store.ProductStore`, `ownerPartyId -> mantle.party.Party`
- Enum/status fields and type references: `marketSegmentTypeEnumId (MarketSegmentType)`
- Key business fields: `typeSequenceNum`, `description`, `parentMarketSegmentId`

`MarketSegmentClassification` (`runtime/component/mantle-udm/entity/MarketingEntities.xml`)
- PK: `marketSegmentId`, `partyClassificationId`
- FKs and targets: `marketSegmentId -> mantle.marketing.segment.MarketSegment`, `partyClassificationId -> mantle.party.PartyClassification`
- Enum/status fields and type references: `NONE`
- Key business fields: `NONE` (classification bridge)

`MarketSegmentDimension` (`runtime/component/mantle-udm/entity/MarketingEntities.xml`)
- PK: `marketSegmentId`, `uomDimensionTypeId`
- FKs and targets: `marketSegmentId -> mantle.marketing.segment.MarketSegment`, `uomDimensionTypeId -> moqui.basic.UomDimensionType`, `uomId -> moqui.basic.Uom`
- Enum/status fields and type references: `NONE`
- Key business fields: `minValue`, `maxValue`

`MarketSegmentGeo` (`runtime/component/mantle-udm/entity/MarketingEntities.xml`)
- PK: `marketSegmentId`, `geoId`
- FKs and targets: `marketSegmentId -> mantle.marketing.segment.MarketSegment`, `geoId -> moqui.basic.Geo`
- Enum/status fields and type references: `NONE`
- Key business fields: `NONE` (geo bridge)

`MarketSegmentParty` (`runtime/component/mantle-udm/entity/MarketingEntities.xml`)
- PK: `marketSegmentId`, `partyId`, `roleTypeId`
- FKs and targets: `marketSegmentId -> mantle.marketing.segment.MarketSegment`, `partyId -> mantle.party.Party`, `roleTypeId -> mantle.party.RoleType`
- Enum/status fields and type references: `NONE`
- Key business fields: `NONE` (party bridge)

## 2A. Commented-Out Entity/View Definitions

Scanned source:
- `runtime/component/mantle-udm/entity/MarketingEntities.xml`

Commented-out `<entity ...>` / `<view-entity ...>` definitions:
- `NONE`

## 3. Reference-Only Set (FK Consumer Map)

Non-marketing entities consuming `MarketSegment` keys:
- `ProductDbForm.marketSegmentId -> MarketSegment`
- `ProductUomDimension.marketSegmentId -> MarketSegment`
- `ProductOtherIdentification.marketSegmentId -> MarketSegment`
- `ProductParameterOption.marketSegmentId -> MarketSegment`
- `ProductParameterValue.marketSegmentId -> MarketSegment`
- `ProductCategoryIdent.marketSegmentId -> MarketSegment`

Reference source file:
- `runtime/component/mantle-udm/entity/ProductDefinitionEntities.xml` (see `090_marketing-reference-intersections-dossier.md`)

## 4. Relationship Map (Domain-Internal Adjacency)

- `MarketSegment` is the segment root and supports parent-child trees.
- Segment criteria can be attached by multiple bridges: classification, dimension ranges, geo, and explicit party assignment.
- `MarketInterest` bridges product categories to market segments with effective dating.

## 5. Service Wiring (Full Detail Set)

Service files touching this layer in `runtime/component/mantle-usl/service`:
- `NONE`

## 6. Screen Wiring (SimpleScreens only — Full Detail Set)

Stock screens/templates touching this layer:
- `runtime/component/SimpleScreens/screen/SimpleScreens/Catalog/Category/EditCategory.xml`
- `runtime/component/SimpleScreens/screen/SimpleScreens/Catalog/Product/EditProduct.xml`

## 7. Canonical Flows

Define segment taxonomy:
1. Create `MarketSegment` roots/children with `marketSegmentTypeEnumId` and `typeSequenceNum`.
2. Attach criteria using `MarketSegmentClassification`, `MarketSegmentDimension`, and `MarketSegmentGeo`.
3. Attach explicit members using `MarketSegmentParty` when rule-based classification is insufficient.

Attach segment to catalog/product context:
1. Catalog screens load `MarketSegment` list (`EditCategory.xml`, `EditProduct.xml`).
2. Product/category identity records persist `marketSegmentId` in downstream product entities (documented in `090`).

## 8. Agent Quick-Start

- Keep `MarketSegment` parent/child semantics intact when changing tree behavior.
- Prefer additive bridge rows (`*Classification`, `*Dimension`, `*Geo`, `*Party`) over custom flags.
- Load `090` before editing product-side segment references.

## 9. Open Questions / Follow-ups

- Segment evaluation/execution services are not present in stock `mantle-usl`; if added, this layer should be updated with concrete service wiring.

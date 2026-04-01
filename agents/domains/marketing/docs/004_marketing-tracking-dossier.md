# Marketing Domain Dossier

Target Domain: `Marketing`
Scope: `004` adjacent layer for tracking entities in `mantle.marketing.tracking`.
Out of Scope: Campaign core (`001`), contact list (`002`), segment (`003`), and cross-domain reference-only intersections (`090`).

See `001_marketing-campaign-core-dossier.md` for core Marketing campaign context.

## 1. Domain Boundary

Full-detail entities in this layer:
- `TrackingCode`
- `TrackingCodeOrder`
- `TrackingCodeOrderReturn`
- `TrackingCodeVisit`

Primary source file scanned:
- `runtime/component/mantle-udm/entity/MarketingEntities.xml`

## 2. Full Detail Set (Entity Inventory)

`TrackingCode` (`runtime/component/mantle-udm/entity/MarketingEntities.xml`)
- PK: `trackingCodeId`
- FKs and targets: `trackingCodeTypeEnumId -> moqui.basic.Enumeration (TrackingCodeType)`, `marketingCampaignId -> mantle.marketing.campaign.MarketingCampaign`
- Enum/status fields and type references: `trackingCodeTypeEnumId (TrackingCodeType)`
- Key business fields: `redirectUrl`, `comments`, `description`, `trackableLifetime`, `billableLifetime`, `fromDate`, `thruDate`, `groupId`, `subgroupId`

`TrackingCodeOrder` (`runtime/component/mantle-udm/entity/MarketingEntities.xml`)
- PK: `orderId`, `trackingCodeTypeEnumId`
- FKs and targets: `orderId -> mantle.order.OrderHeader`, `trackingCodeId -> mantle.marketing.tracking.TrackingCode`, `trackingCodeTypeEnumId -> moqui.basic.Enumeration`
- Enum/status fields and type references: `trackingCodeTypeEnumId (TrackingCodeType)`
- Key business fields: `isBillable`, `siteId`, `hasExported`, `affiliateReferredTimeStamp`

`TrackingCodeOrderReturn` (`runtime/component/mantle-udm/entity/MarketingEntities.xml`)
- PK: `trackingCodeTypeEnumId`, `returnId`, `orderId`
- FKs and targets: `trackingCodeTypeEnumId -> moqui.basic.Enumeration`, `returnId -> mantle.order.return.ReturnHeader`, `orderId -> mantle.order.OrderHeader`, `trackingCodeId -> mantle.marketing.tracking.TrackingCode`
- Enum/status fields and type references: `trackingCodeTypeEnumId (TrackingCodeType)`
- Key business fields: `orderItemSeqId`, `isBillable`, `siteId`, `hasExported`, `affiliateReferredTimeStamp`

`TrackingCodeVisit` (`runtime/component/mantle-udm/entity/MarketingEntities.xml`)
- PK: `trackingCodeId`, `visitId`, `fromDate`
- FKs and targets: `trackingCodeId -> mantle.marketing.tracking.TrackingCode`, `visitId -> moqui.server.Visit`, `sourceEnumId -> moqui.basic.Enumeration (TrackingCodeSource)`
- Enum/status fields and type references: `sourceEnumId (TrackingCodeSource)`
- Key business fields: `NONE`

## 2A. Commented-Out Entity/View Definitions

Scanned source:
- `runtime/component/mantle-udm/entity/MarketingEntities.xml`

Commented-out `<entity ...>` / `<view-entity ...>` definitions:
- `NONE`

## 3. Reference-Only Set (FK Consumer Map)

Non-marketing consumers of tracking keys:
- `SalesOpportunityTracking.trackingCodeId -> TrackingCode` (`runtime/component/mantle-udm/entity/SalesEntities.xml`, see `090_marketing-reference-intersections-dossier.md`)

Cross-layer producer dependency:
- `TrackingCode.marketingCampaignId -> MarketingCampaign` (campaign root in `001`)

## 4. Relationship Map (Domain-Internal Adjacency)

- `TrackingCode` is the attribution root.
- `TrackingCodeOrder` binds attribution to orders by type.
- `TrackingCodeOrderReturn` binds attribution to returns/order lines.
- `TrackingCodeVisit` binds attribution to web visits and source channels.

## 5. Service Wiring (Full Detail Set)

Service files touching this layer in `runtime/component/mantle-usl/service`:
- `NONE`

## 6. Screen Wiring (SimpleScreens only — Full Detail Set)

Stock `SimpleScreens` touching this layer:
- `NONE`

## 7. Canonical Flows

Tracking code setup:
1. Create `TrackingCode` and classify with `trackingCodeTypeEnumId`.
2. Optionally associate to a campaign using `marketingCampaignId`.

Order/return attribution:
1. Persist order attribution in `TrackingCodeOrder`.
2. Persist return attribution in `TrackingCodeOrderReturn` (optional `orderItemSeqId` granularity).

Visit attribution:
1. Persist `TrackingCodeVisit` rows for observed visits.
2. Track source via `sourceEnumId` (`TKCDSRC_COOKIE`, `TKCDSRC_URL_PARAM`).

## 8. Agent Quick-Start

- Keep PK compositions exact for `TrackingCodeOrder` and `TrackingCodeOrderReturn` before changing attribution logic.
- Preserve type-enum semantics (`TrackingCodeType`, `TrackingCodeSource`) for downstream reporting/export use.
- Load `001` when changing campaign-to-tracking attribution behavior.

## 9. Open Questions / Follow-ups

- No stock service/screen wiring currently references tracking entities directly; confirm if attribution is handled externally/custom.

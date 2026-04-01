# Inventory Domain Dossier

Target Domain: `Inventory` (operational pivot prefix `PhysicalInventory*`)
Scope: Full Detail Set = `PhysicalInventory*`; Reference-Only Set = FK consumers of these entities.
Out of Scope: Strict `Inventory*` baseline (covered in `001`) and broader `Asset*` inventory model beyond FK traceability.

See `001_inventory-domain-dossier.md` for strict baseline context and scope decision history.

## 1. Domain Boundary

Set computation result:
- Full Detail Set (`PhysicalInventory*`): `PhysicalInventory`, `PhysicalInventoryCount`
- Reference-Only Set (FK consumers outside Full Detail): `AssetDetail`, `AcctgTrans`

Entity declaration paths:
- `runtime/component/mantle-udm/entity/ProductAssetEntities.xml` (`PhysicalInventory`, `PhysicalInventoryCount`)

## 2. Full Detail Set (Entity Inventory)

`PhysicalInventory` (`runtime/component/mantle-udm/entity/ProductAssetEntities.xml`)
- PK: `physicalInventoryId`
- FKs and targets: `statusId -> moqui.basic.StatusItem (statusTypeId=PhysicalInventory)`, `facilityId -> mantle.facility.Facility`, `partyId -> mantle.party.Party`
- Enum/status fields: `statusId` (Physical Inventory lifecycle statuses: `PIInPlanning`, `PIInProgress`, `PIInValidation`, `PIComplete`)
- Key business fields: `physicalInventoryDate`, `comments`

`PhysicalInventoryCount` (`runtime/component/mantle-udm/entity/ProductAssetEntities.xml`)
- PK: `physicalInventoryCountId`
- FKs and targets: `physicalInventoryId -> mantle.product.asset.PhysicalInventory`, `facilityId -> mantle.facility.Facility`, `locationSeqId -> mantle.facility.FacilityLocation (one-nofk)`, `productId -> mantle.product.Product`, `lotId -> mantle.product.asset.Lot`
- Enum/status fields: `NONE`
- Key business fields: `countDate`, `quantityOnHand`, `comments`

## 3. Reference-Only Set (FK Consumer Map)

`AssetDetail` (`runtime/component/mantle-udm/entity/ProductAssetEntities.xml`)
- FK fields into Full Detail Set: `physicalInventoryId -> PhysicalInventory`, `physicalInventoryCountId -> PhysicalInventoryCount`
- Usage note: stores physical count/change adjustments as inventory detail deltas and variance metadata.

`AcctgTrans` (`runtime/component/mantle-udm/entity/AccountingLedgerEntities.xml`)
- FK fields into Full Detail Set: `physicalInventoryId -> PhysicalInventory`
- Usage note: ledger transaction anchor for inventory variance accounting postings.

## 4. Relationship Map (Domain-Internal Adjacency)

- `PhysicalInventory` (header/session) -> `PhysicalInventoryCount` (line-level counted records).
- `PhysicalInventory` -> `AssetDetail` (reference-only): variance/change trace attached through `physicalInventoryId`.
- `PhysicalInventoryCount` -> `AssetDetail` (reference-only): optional link through `physicalInventoryCountId` during count-driven adjustments.
- `AssetDetail` -> `AcctgTrans` posting flow (reference-only) through `AssetAutoPostServices.post#PhysicalInventoryVariance`.

## 5. Service Wiring (Full Detail Set)

Service files in `mantle-usl` touching Full Detail entities:
- `runtime/component/mantle-usl/service/mantle/product/AssetServices.xml`
- `runtime/component/mantle-usl/service/mantle/ledger/AssetAutoPostServices.xml`
- `runtime/component/mantle-usl/service/AccountingLedger.secas.xml`
- `runtime/component/mantle-usl/service/mantle.rest.xml`

Primary service wiring:
- `mantle.product.AssetServices.record#PhysicalInventoryQuantity`
- `mantle.product.AssetServices.record#PhysicalInventoryChange`
- `mantle.product.AssetServices.update#PhysicalInventoryDate`
- `mantle.ledger.AssetAutoPostServices.post#PhysicalInventoryVariance`
- `mantle.ledger.AssetAutoPostServices.repost#PhysicalInventoryVariance`

REST endpoint map:
- `POST facilities/{facilityId}/products/{productId}/physicalQuantity` -> `mantle.product.AssetServices.record#PhysicalInventoryQuantity` (`runtime/component/mantle-usl/service/mantle.rest.xml`)
- `POST facilities/{facilityId}/products/{productId}/physicalChange` -> `mantle.product.AssetServices.record#PhysicalInventoryChange` (`runtime/component/mantle-usl/service/mantle.rest.xml`)

Key service parameter summary:
- `record#PhysicalInventoryQuantity` in: required `productId, facilityId, quantity`; optional `physicalInventoryId, physicalInventoryCountId, physicalInventoryDate, comments, varianceReasonEnumId, strictLocation, strictLot, strictAll` plus asset-scope filters (`locationSeqId, lotId, ownerPartyId, assetPoolId, statusId`); out: `physicalInventoryId, quantityChange`.
- `record#PhysicalInventoryChange` in: required `productId, facilityId, quantityChange`; optional `physicalInventoryId, physicalInventoryCountId, physicalInventoryDate, comments, varianceReasonEnumId, assetList, strictLocation, strictLot, strictAll` plus asset-scope filters (`locationSeqId, lotId, ownerPartyId, assetPoolId, statusId`); out: `physicalInventoryId, quantityRemaining, assetIdList`.
- `update#PhysicalInventoryDate` in: required `physicalInventoryId, physicalInventoryDate`; optional `comments`; out: `NONE`.
- `post#PhysicalInventoryVariance` in: required `assetDetailId`; out: `acctgTransId`.
- `repost#PhysicalInventoryVariance` in: required `assetDetailId`; out: `acctgTransId`.

Entity occurrence map (service files containing the entity identifier):
- `PhysicalInventory` -> `runtime/component/mantle-usl/service/mantle/product/AssetServices.xml`, `runtime/component/mantle-usl/service/mantle/ledger/AssetAutoPostServices.xml`, `runtime/component/mantle-usl/service/AccountingLedger.secas.xml`, `runtime/component/mantle-usl/service/mantle.rest.xml`
- `PhysicalInventoryCount` -> `runtime/component/mantle-usl/service/mantle/product/AssetServices.xml`

Reference-only traceability in service flow:
- `AccountingLedger.secas.xml` hooks `create#mantle.product.asset.AssetDetail` to post physical variance accounting.
- `AssetServices.update#PhysicalInventoryDate` updates `PhysicalInventory` date, updates linked `AssetDetail` effective dates, then calls variance repost.
- `AssetServices.record#PhysicalInventoryQuantity` updates `PhysicalInventoryCount` only when `physicalInventoryCountId` is provided; this service does not create `PhysicalInventoryCount` records directly.
- No stock `mantle-usl` service or stock `SimpleScreens` transition was found that calls `create#mantle.product.asset.PhysicalInventoryCount`; if count-row creation is needed, use explicit entity-auto create patterns in custom code after validating lifecycle rules.

## 6. Screen Wiring (SimpleScreens only — Full Detail Set)

Stock SimpleScreens paths touching Full Detail entities:
- `runtime/component/SimpleScreens/screen/SimpleScreens/Asset/PhysicalInventory.xml`
- `runtime/component/SimpleScreens/screen/SimpleScreens/Asset/PhysicalInventory/FindPhysicalInventory.xml`
- `runtime/component/SimpleScreens/screen/SimpleScreens/Asset/PhysicalInventory/EditPhysicalInventory.xml`
- `runtime/component/SimpleScreens/screen/SimpleScreens/Asset/Asset/FindSummary.xml`
- `runtime/component/SimpleScreens/screen/SimpleScreens/Asset/Asset/FindSummary/PhysicalQuantity.xml`
- `runtime/component/SimpleScreens/screen/SimpleScreens/Asset/Asset/FindSummary/PhysicalChange.xml`
- `runtime/component/SimpleScreens/screen/SimpleScreens/Asset/Asset/AssetDetail.xml`
- `runtime/component/SimpleScreens/screen/SimpleScreens/Asset/Asset/DetailHistory.xml`
- `runtime/component/SimpleScreens/screen/SimpleScreens/Asset/dashboard.xml`

Entity occurrence map (screen/template files containing the entity identifier):
- `PhysicalInventory` -> all paths listed above
- `PhysicalInventoryCount` -> `NONE` (no direct stock screen reference by name)

## 7. Canonical Flows

- Create physical inventory session:
- Screen: `Asset/PhysicalInventory/FindPhysicalInventory.xml` transition `createPhysicalInventory` -> service `create#mantle.product.asset.PhysicalInventory`.
- Find and review physical inventory sessions:
- Screen: `Asset/PhysicalInventory/FindPhysicalInventory.xml` lists `mantle.product.asset.PhysicalInventory` and linked asset deltas.
- Update physical inventory date and comments:
- Screen: `Asset/PhysicalInventory/EditPhysicalInventory.xml` / `FindPhysicalInventory.xml` -> service `mantle.product.AssetServices.update#PhysicalInventoryDate`.
- Record quantity-based physical count:
- Screen: `Asset/Asset/FindSummary/PhysicalQuantity.xml` -> service `mantle.product.AssetServices.record#PhysicalInventoryQuantity`.
- Record manual quantity difference:
- Screen: `Asset/Asset/FindSummary/PhysicalChange.xml` (extends PhysicalQuantity form) -> service `mantle.product.AssetServices.record#PhysicalInventoryChange`.
- Post/repost accounting variance:
- Triggered through `AccountingLedger.secas.xml` and `AssetAutoPostServices.post#PhysicalInventoryVariance`/`repost#PhysicalInventoryVariance`.

## 8. Agent Quick-Start

- Load `001` first for scope rationale, then this `002` for operational implementation.
- Safe extension points:
- add UI/reporting behavior around existing transitions (`recordQuantity`, `recordChange`, `updatePhysicalInventoryDate`);
- extend validation/selection rules in `AssetServices` strict filters (`strictLocation`, `strictLot`, `strictAll`).
- Common pitfalls:
- changing `physicalInventoryDate` without reposting variance creates accounting/date drift (use `update#PhysicalInventoryDate` path);
- direct `AssetDetail` manipulation can bypass intended service and posting behavior;
- `PhysicalInventoryCount` has limited direct UI touchpoints and is primarily service-managed; in `AssetServices`, quantity recording updates existing count rows when `physicalInventoryCountId` is supplied and does not issue a `create#mantle.product.asset.PhysicalInventoryCount` call.

## 9. Open Questions / Follow-ups

- Keep this layer narrowly `PhysicalInventory*` or expand Inventory domain into broader `Asset*` inventory lifecycle in a new `003` layer?
- Should a dedicated stock screen for `PhysicalInventoryCount` records be added, or remain implicit through quantity/change services?

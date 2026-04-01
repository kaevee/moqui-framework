# Inventory Domain Review — Post-Review Updates (Summary 003)

Date: `2026-04-01`
Domain index: `agents/domains/inventory/INVENTORY.md`
Artifacts reviewed:
- `agents/domains/inventory/summaries/003_inventory-physicalinventory-review-summary.md`
- `agents/domains/inventory/docs/002_inventory-physicalinventory-dossier.md` (updated state)
- `agents/domains/inventory/summaries/002_inventory-physicalinventory-summary.md` (updated state)

Prior review: `agents/domains/inventory/reviews/002_inventory-physicalinventory-review.md`

## 1. Purpose

Verify that the changes claimed in summary 003 were correctly applied to the dossier and summary, and that all new content is accurate against source files.

## 2. Claimed Changes — Verification

### 2.1 REST Endpoint Detail Added to Dossier

**Claimed**: REST endpoint URLs added to dossier 002.

**Verified**: YES. Dossier section 5, lines 64-66 now include:
- `POST facilities/{facilityId}/products/{productId}/physicalQuantity` -> `record#PhysicalInventoryQuantity`
- `POST facilities/{facilityId}/products/{productId}/physicalChange` -> `record#PhysicalInventoryChange`

Confirmed against `runtime/component/mantle-usl/service/mantle.rest.xml`.

### 2.2 Service Parameter Summaries Added to Dossier

**Claimed**: Key in/out parameter summaries added for all 5 primary services.

**Verified**: YES, with findings below.

`record#PhysicalInventoryQuantity` (dossier line 69):
- Required `productId, facilityId, quantity`: confirmed at `AssetServices.xml:3366-3370`.
- Optional params including `physicalInventoryId, physicalInventoryCountId, physicalInventoryDate, comments, varianceReasonEnumId, strictLocation, strictLot, strictAll` and asset-scope filters: confirmed at lines 3363-3380.
- Out `physicalInventoryId, quantityChange`: confirmed at lines 3383-3384.

`record#PhysicalInventoryChange` (dossier line 70):
- Required `productId, facilityId, quantityChange`: confirmed at `AssetServices.xml:3432-3437`.
- Optional params including `assetList`: confirmed at lines 3442-3451.
- Out `physicalInventoryId, quantityRemaining, assetIdList`: confirmed at lines 3454-3458.

`update#PhysicalInventoryDate` (dossier line 71):
- Required `physicalInventoryId, physicalInventoryDate`; optional `comments`: confirmed at lines 3603-3605.
- Out `NONE`: confirmed (no `<out-parameters>` block in source).

`post#PhysicalInventoryVariance` (dossier line 72):
- In `assetDetailId` required: confirmed at `AssetAutoPostServices.xml:747`.
- Out `acctgTransId`: confirmed at line 748.

`repost#PhysicalInventoryVariance` (dossier line 73):
- In `assetDetailId` required: confirmed at `AssetAutoPostServices.xml:884`.
- Out `acctgTransId`: confirmed at line 885.

### 2.3 Screen-Path Precision in Summary 002

**Claimed**: `FindSummary*` wildcard expanded to explicit paths in summary 002.

**Verified**: YES. Summary 002 lines 35-37 now list:
- `FindSummary.xml`
- `FindSummary/PhysicalQuantity.xml`
- `FindSummary/PhysicalChange.xml`

### 2.4 PhysicalInventoryCount Pushback

**Claimed**: Recommendation to state that `PhysicalInventoryCount` records are created by `record#PhysicalInventoryQuantity` was rejected. Source shows only `update#PhysicalInventoryCount` (not `create#`) when `physicalInventoryCountId` is supplied.

**Verified**: YES. `AssetServices.xml:3387-3389` shows:
```xml
<if condition="physicalInventoryCountId">
    <service-call name="update#mantle.product.asset.PhysicalInventoryCount" .../>
</if>
```

No `create#mantle.product.asset.PhysicalInventoryCount` exists anywhere in `mantle-usl` service files (confirmed via broad search). The pushback was correct and the dossier text at line 82 accurately reflects this.

Additionally confirmed: `record#PhysicalInventoryChange` creates `PhysicalInventory` records (line 3464-3465) but not `PhysicalInventoryCount` records.

## 3. Issues Found

### 3.1 Minor: Remaining Wildcard in Summary 002

Summary 002 line 34 still uses `SimpleScreens/Asset/PhysicalInventory/*` with a wildcard glob. Summary 003 only expanded the `FindSummary*` wildcard but left this one. For full precision, this should be expanded to:
- `SimpleScreens/Asset/PhysicalInventory/FindPhysicalInventory.xml`
- `SimpleScreens/Asset/PhysicalInventory/EditPhysicalInventory.xml`

Severity: **low** — the dossier itself has the full paths; only the summary has the residual wildcard.

### 3.2 Observation: PhysicalInventoryCount Lifecycle Gap

The pushback correctly identified that no service in `mantle-usl` creates `PhysicalInventoryCount` records. The dossier notes this. However, neither the dossier nor the summary explicitly states *how* `PhysicalInventoryCount` records come into existence. Possible explanations:
- Direct `create#mantle.product.asset.PhysicalInventoryCount` calls from custom app code or screens (auto-CRUD).
- Screen transitions in `EditPhysicalInventory.xml` or `FindPhysicalInventory.xml` that use inline entity-auto create.

This is worth noting for agents who need to implement count-record creation — they need to know there is no dedicated mantle-usl service for it and must use the entity-auto `create#` pattern directly.

Severity: **low** — informational gap, not an error.

## 4. Verdict

**PASS** — All changes claimed in summary 003 were correctly applied. New content (REST endpoints, service parameters, PhysicalInventoryCount behavior clarification) is accurate against source. Two minor observations noted above; neither represents an error in the current artifacts.

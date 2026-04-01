# Inventory Domain Review — PhysicalInventory Dossier & Summaries

Date: `2026-04-01`
Domain index: `agents/domains/inventory/INVENTORY.md`
Artifacts reviewed:
- `agents/domains/inventory/summaries/001_inventory-domain-scope-summary.md`
- `agents/domains/inventory/summaries/002_inventory-physicalinventory-summary.md`
- `agents/domains/inventory/docs/001_inventory-domain-dossier.md`
- `agents/domains/inventory/docs/002_inventory-physicalinventory-dossier.md`
- `agents/domains/inventory/INVENTORY.md`

## 1. Verification Method

Every factual claim in the dossiers and summaries was checked against the source files listed below. Entity fields, FK targets, status values, service names, screen paths, SECA rules, and REST endpoints were all confirmed by reading the actual XML declarations.

Source files verified:
- `runtime/component/mantle-udm/entity/ProductAssetEntities.xml`
- `runtime/component/mantle-udm/entity/AccountingLedgerEntities.xml`
- `runtime/component/mantle-usl/service/mantle/product/AssetServices.xml`
- `runtime/component/mantle-usl/service/mantle/ledger/AssetAutoPostServices.xml`
- `runtime/component/mantle-usl/service/AccountingLedger.secas.xml`
- `runtime/component/mantle-usl/service/mantle.rest.xml`
- All 9 SimpleScreens paths listed in `002` dossier section 6.

## 2. Scope & Structure Assessment

### 001 Baseline (strict `Inventory*` prefix)

Correct. No `Inventory*` entity or view-entity exists in `mantle-udm`. The dossier correctly records `NONE` for all sections and documents the out-of-scope implementation pointers for traceability. The decision to keep this as a strict baseline is sound — it preserves the AGENTS.md prefix rule and provides a clear audit trail for the scope pivot.

### 002 Operational Pivot (`PhysicalInventory*`)

Correct. The pivot to `PhysicalInventory*` produces a non-empty, actionable dossier. The Full Detail Set (`PhysicalInventory`, `PhysicalInventoryCount`) and Reference-Only Set (`AssetDetail`, `AcctgTrans`) are complete and accurately scoped.

### INVENTORY.md Index

Well-structured. Task-based routing, layer directory, canonical flows, and source path list are all present and accurate.

## 3. Entity Inventory — Findings

### PhysicalInventory
- PK, FKs, status values, business fields: **all verified correct**.
- Status lifecycle: `PIInPlanning` -> `PIInProgress` -> `PIInValidation` -> `PIComplete` confirmed in entity seed data.

### PhysicalInventoryCount
- PK, FKs, business fields: **all verified correct**.
- `locationSeqId` relationship type `one-nofk`: confirmed.
- No status/enum fields: confirmed.

### AssetDetail (Reference-Only)
- FK fields `physicalInventoryId` and `physicalInventoryCountId`: confirmed.
- Usage note accurate.

### AcctgTrans (Reference-Only)
- FK field `physicalInventoryId`: confirmed.
- Usage note accurate.

### No missing Reference-Only entities detected
- Broad search across all `mantle-udm` entity files for `physicalInventoryId` and `physicalInventoryCountId` FK references found no entities beyond `AssetDetail` and `AcctgTrans`.

## 4. Service Wiring — Findings

All 5 primary services verified at their documented paths:

| Service | File | Verified |
|---------|------|----------|
| `record#PhysicalInventoryQuantity` | `AssetServices.xml` | YES |
| `record#PhysicalInventoryChange` | `AssetServices.xml` | YES |
| `update#PhysicalInventoryDate` | `AssetServices.xml` | YES |
| `post#PhysicalInventoryVariance` | `AssetAutoPostServices.xml` | YES |
| `repost#PhysicalInventoryVariance` | `AssetAutoPostServices.xml` | YES |

SECA rule in `AccountingLedger.secas.xml`: `AssetDetailPhysicalVarianceGlPost` triggers on `create#mantle.product.asset.AssetDetail` and calls `post#PhysicalInventoryVariance`. Confirmed.

REST endpoints in `mantle.rest.xml`:
- POST `facilities/{facilityId}/products/{productId}/physicalQuantity` -> `record#PhysicalInventoryQuantity`: confirmed.
- POST `facilities/{facilityId}/products/{productId}/physicalChange` -> `record#PhysicalInventoryChange`: confirmed.

**No missing services detected.** Broad search across all `mantle-usl` service files found no additional services touching `PhysicalInventory` or `PhysicalInventoryCount` beyond those documented.

## 5. Screen Wiring — Findings

All 9 screen paths confirmed to exist and to reference `PhysicalInventory`.

Claim that `PhysicalInventoryCount` has no direct stock screen reference by name: **confirmed**. Zero SimpleScreens files reference `PhysicalInventoryCount` by entity name. Count records are managed indirectly through the `record#PhysicalInventoryQuantity` and `record#PhysicalInventoryChange` service calls.

**No missing screens detected.** Broad search across all SimpleScreens files found no additional files referencing `PhysicalInventory` beyond those documented.

## 6. Canonical Flows — Findings

All documented flows trace correctly from screen transition -> service -> entity. The create/find/update/record-quantity/record-change/post-variance chain is complete and accurate.

## 7. Issues Found

### Issues: NONE

All factual claims in both dossiers and both summaries are accurate against the current source files. No missing entities, services, screens, or FK references were detected.

## 8. Observations & Recommendations

### Minor improvements (non-blocking)

1. **REST endpoint detail in dossier 002**: The REST endpoints are listed by service file path but not by endpoint URL. Adding the actual REST paths (`facilities/{facilityId}/products/{productId}/physicalQuantity` and `physicalChange`) to section 5 or section 7 would help agents building API integrations.

2. **Service input/output parameters**: The dossier documents service names and file paths but not the key in/out parameters for each service (e.g., `record#PhysicalInventoryQuantity` takes `facilityId`, `productId`, `quantity`, etc.). Adding a compact parameter summary per service would reduce the need for agents to read the full service XML before calling services.

3. **PhysicalInventoryCount implicit management note**: The dossier correctly notes in section 8 that `PhysicalInventoryCount` is "primarily service-managed" and has limited UI touchpoints. This is a useful pitfall note. Consider also noting that count records are created internally by `record#PhysicalInventoryQuantity` — this clarifies the lifecycle for agents who might otherwise try to create `PhysicalInventoryCount` records directly.

4. **Summary 002 screen list**: The summary lists `SimpleScreens/Asset/Asset/FindSummary*` with a wildcard. For agent precision, expanding to the two specific files (`PhysicalQuantity.xml`, `PhysicalChange.xml`) would be marginally better, though the dossier itself already has the full paths.

### Structural observations

- The 001/002 layering split is clean and follows AGENTS.md conventions correctly.
- The INVENTORY.md index provides effective task-based routing.
- Cross-references between 001 and 002 are present and correct.

## 9. Verdict

**PASS** — All artifacts are accurate, complete within their stated scope, and structurally compliant with AGENTS.md conventions. The minor recommendations above are optional improvements for agent usability but do not represent errors or gaps.

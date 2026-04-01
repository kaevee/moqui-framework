# Inventory PhysicalInventory Summary

Date: `2026-04-01`
Domain index: `agents/domains/inventory/INVENTORY.md`
Layer dossier: `agents/domains/inventory/docs/002_inventory-physicalinventory-dossier.md`

## Outcome

- Approved scope pivot was applied from strict `Inventory*` to operational `PhysicalInventory*`.
- Full Detail Set is now documented as:
- `PhysicalInventory`
- `PhysicalInventoryCount`
- Reference-Only Set captured:
- `AssetDetail` (FKs: `physicalInventoryId`, `physicalInventoryCountId`)
- `AcctgTrans` (FK: `physicalInventoryId`)

## Service Wiring Validated

- `runtime/component/mantle-usl/service/mantle/product/AssetServices.xml`
- `runtime/component/mantle-usl/service/mantle/ledger/AssetAutoPostServices.xml`
- `runtime/component/mantle-usl/service/AccountingLedger.secas.xml`
- `runtime/component/mantle-usl/service/mantle.rest.xml`

Primary operational services documented:
- `record#PhysicalInventoryQuantity`
- `record#PhysicalInventoryChange`
- `update#PhysicalInventoryDate`
- `post#PhysicalInventoryVariance`
- `repost#PhysicalInventoryVariance`

## Screen Wiring Validated

- Physical inventory stock screens and dialogs are documented from:
- `SimpleScreens/Asset/PhysicalInventory.xml`
- `SimpleScreens/Asset/PhysicalInventory/FindPhysicalInventory.xml`
- `SimpleScreens/Asset/PhysicalInventory/EditPhysicalInventory.xml`
- `SimpleScreens/Asset/Asset/FindSummary.xml`
- `SimpleScreens/Asset/Asset/FindSummary/PhysicalQuantity.xml`
- `SimpleScreens/Asset/Asset/FindSummary/PhysicalChange.xml`
- `SimpleScreens/Asset/Asset/AssetDetail.xml`
- `SimpleScreens/Asset/Asset/DetailHistory.xml`
- `SimpleScreens/Asset/dashboard.xml`

## Current Domain State

- `001` remains the strict baseline (empty by prefix design).
- `002` is now the actionable implementation layer for physical inventory work.

# INVENTORY.md

## Purpose

Entry index for the Inventory domain dossier.

Use this file first, then load `001` + the smallest relevant layer doc.

## Scope Rule (Authoritative)

- Full Detail Set: every entity/view-entity with prefix `Inventory*`.
- Coverage per Full Detail entity: entity + touching `mantle-usl` services + touching stock `SimpleScreens`.
- If service/screen does not exist: record `NONE`.
- Reference-Only Set: non-`Inventory*` entities with FK links to Full Detail entities.

Baseline strict-prefix result in this codebase:
- Full Detail Set: `NONE`
- Reference-Only Set: `NONE` (no Full Detail anchors)

User-approved operational pivot:
- Target prefix for actionable inventory documentation: `PhysicalInventory*`
- Pivot Full Detail Set: `PhysicalInventory`, `PhysicalInventoryCount`

## Dossier Layers

Core:
- [001 Core Inventory Layer](docs/001_inventory-domain-dossier.md)

Operational inventory layer:
- [002 Physical Inventory Layer](docs/002_inventory-physicalinventory-dossier.md)

## Start Here by Task

- Confirm strict Inventory prefix baseline (`Inventory*`): `001`
- Work on physical inventory sessions/counting/variance flows: `001` + `002`
- Assess downstream impact of physical inventory changes (accounting and asset history): `002`, sections `3`, `5`, `6`

## Canonical Inventory Flows

Strict `Inventory*` scope:
- `NONE`

Operational (`PhysicalInventory*`) scope:
- Physical inventory session create/find/update date
- Record physical quantity/change against assets
- Repost accounting transactions after physical inventory date corrections

## Source-of-Truth Implementation Files

Strict-prefix lookup anchors:
- `runtime/component/mantle-udm/entity/`
- `runtime/component/mantle-usl/entity/`
- `runtime/component/mantle-usl/service/`
- `runtime/component/SimpleScreens/screen/`
- `runtime/component/SimpleScreens/template/`

Operational physical inventory implementation files:
- `runtime/component/mantle-udm/entity/ProductAssetEntities.xml`
- `runtime/component/mantle-udm/entity/AccountingLedgerEntities.xml`
- `runtime/component/mantle-usl/service/mantle/product/AssetServices.xml`
- `runtime/component/mantle-usl/service/mantle/ledger/AssetAutoPostServices.xml`
- `runtime/component/mantle-usl/service/AccountingLedger.secas.xml`
- `runtime/component/mantle-usl/service/mantle.rest.xml`
- `runtime/component/SimpleScreens/screen/SimpleScreens/Asset/PhysicalInventory.xml`
- `runtime/component/SimpleScreens/screen/SimpleScreens/Asset/PhysicalInventory/FindPhysicalInventory.xml`
- `runtime/component/SimpleScreens/screen/SimpleScreens/Asset/PhysicalInventory/EditPhysicalInventory.xml`
- `runtime/component/SimpleScreens/screen/SimpleScreens/Asset/Asset/FindSummary.xml`
- `runtime/component/SimpleScreens/screen/SimpleScreens/Asset/Asset/FindSummary/PhysicalQuantity.xml`
- `runtime/component/SimpleScreens/screen/SimpleScreens/Asset/Asset/FindSummary/PhysicalChange.xml`
- `runtime/component/SimpleScreens/screen/SimpleScreens/Asset/Asset/AssetDetail.xml`
- `runtime/component/SimpleScreens/screen/SimpleScreens/Asset/Asset/DetailHistory.xml`
- `runtime/component/SimpleScreens/screen/SimpleScreens/Asset/dashboard.xml`

## Maintenance Checklist

- Keep `001` as strict-prefix baseline and avoid mutating it into operational scope.
- Keep `002` aligned with `PhysicalInventory*` and list any non-prefix reference-only entities explicitly.
- If scope broadens to `Asset*`, add `003+` layers instead of overloading `002`.
- Keep source path list aligned with filesystem when files move.

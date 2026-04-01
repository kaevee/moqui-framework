# Inventory Domain Dossier

Target Domain: `Inventory`
Scope: Full Detail Set = strict prefix `Inventory*`; Reference-Only Set = FK consumers of Full Detail Set.
Out of Scope: All entities not matching strict prefix, including `PhysicalInventory*`, unless scope is explicitly changed.

## 1. Domain Boundary

Set computation result:
- Full Detail Set (`Inventory*`): `NONE`
- Reference-Only Set (FK consumers of Full Detail Set): `NONE`

Validation notes:
- No entity/view-entity declarations found with `entity-name="Inventory..."`
- Only inventory-named entity declarations found are `PhysicalInventory` and `PhysicalInventoryCount`, which do not match strict prefix

Source paths checked:
- `runtime/component/mantle-udm/entity/`
- `runtime/component/mantle-usl/entity/`
- `runtime/component/mantle-udm/entity/ProductAssetEntities.xml`

## 2. Full Detail Set (Entity Inventory)

`NONE`

## 3. Reference-Only Set (FK Consumer Map)

`NONE`

## 4. Relationship Map (Domain-Internal Adjacency)

`NONE` under strict `Inventory*` scope (no Full Detail entities).

## 5. Service Wiring (Full Detail Set)

`NONE`

No `mantle-usl` services can be mapped to Full Detail entities because the Full Detail Set is empty.

## 6. Screen Wiring (SimpleScreens only — Full Detail Set)

`NONE`

No stock `SimpleScreens` can be mapped to Full Detail entities because the Full Detail Set is empty.

## 7. Canonical Flows

Under strict `Inventory*` scope:
- `NONE`

Likely operational inventory flows observed in implementation (currently out of strict scope):
- Create/find/edit physical inventory sessions
- Record physical inventory quantity
- Record physical inventory difference/variance

Primary out-of-scope implementation paths:
- `runtime/component/mantle-udm/entity/ProductAssetEntities.xml`
- `runtime/component/mantle-usl/service/mantle/product/AssetServices.xml`
- `runtime/component/SimpleScreens/screen/SimpleScreens/Asset/PhysicalInventory/FindPhysicalInventory.xml`
- `runtime/component/SimpleScreens/screen/SimpleScreens/Asset/PhysicalInventory/EditPhysicalInventory.xml`
- `runtime/component/SimpleScreens/screen/SimpleScreens/Asset/Asset/FindSummary/PhysicalQuantity.xml`
- `runtime/component/SimpleScreens/screen/SimpleScreens/Asset/Asset/FindSummary/PhysicalChange.xml`

## 8. Agent Quick-Start

- Load this file first to confirm strict scope output.
- Operational pivot documented in `002_inventory-physicalinventory-dossier.md`.
- Use `001 + 002` for active physical inventory implementation work.

## 9. Open Questions / Follow-ups

- Should Inventory domain stay split as strict baseline (`001`) + physical inventory layer (`002`)?
- If future scope expands, should broader `Asset*` inventory lifecycle be captured in a new `003` layer?

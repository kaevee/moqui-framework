# Sales Domain Dossier

Target Domain: `Sales`
Scope: `003` adjacent layer for need-capture entity in `mantle.sales.need`.
Out of Scope: Opportunity core (`001`), forecast (`002`), and cross-domain reference-only intersections (`090`).

See `001_sales-opportunity-core-dossier.md` for core Sales context.

## 1. Domain Boundary

Full-detail entities in this layer:
- `PartyNeed`

Primary source file scanned:
- `runtime/component/mantle-udm/entity/SalesEntities.xml`

## 2. Full Detail Set (Entity Inventory)

`PartyNeed` (`runtime/component/mantle-udm/entity/SalesEntities.xml`)
- PK: `partyNeedId`
- FKs and targets: `needTypeEnumId -> moqui.basic.Enumeration`, `partyId -> mantle.party.Party`, `roleTypeId -> mantle.party.RoleType`, `communicationEventId -> mantle.party.communication.CommunicationEvent`, `productId -> mantle.product.Product`, `productCategoryId -> mantle.product.category.ProductCategory`, `visitId -> moqui.server.Visit`
- Enum/status fields and type references: `needTypeEnumId (NeedType enumeration)`
- Key business fields: `datetimeRecorded`, `description`

## 2A. Commented-Out Entity/View Definitions

Scanned source:
- `runtime/component/mantle-udm/entity/SalesEntities.xml`

Commented-out `<entity ...>` / `<view-entity ...>` definitions:
- `NONE`

## 3. Reference-Only Set (FK Consumer Map)

Non-`mantle.sales.*` FK consumers discovered for `PartyNeed`:
- `NONE`

## 4. Relationship Map (Domain-Internal Adjacency)

- `PartyNeed` captures a sales-relevant need signal for a party/role.
- The need can be correlated to communication context (`communicationEventId`), product context (`productId` / `productCategoryId`), and browsing context (`visitId`).

## 5. Service Wiring (Full Detail Set)

Service files touching this layer's full detail entity in `runtime/component/mantle-usl/service`:
- `NONE`

## 6. Screen Wiring (SimpleScreens only — Full Detail Set)

Stock `SimpleScreens` touching this layer's full detail entity:
- `NONE`

## 7. Canonical Flows

Need capture:
1. Create `PartyNeed` when a customer/account need is identified from communication, sales conversation, or visit behavior.
2. Populate party, role, and optional product/category context for downstream sales prioritization.
3. Use `datetimeRecorded` and `needTypeEnumId` for timeline and type-based filtering.

## 8. Agent Quick-Start

- Preserve `PartyNeed` as an event-style record (single PK, timestamped capture).
- Use explicit context FKs (`communicationEventId`, `visitId`, product/category) rather than free-text-only tracking.
- Add service/screen wiring here if future stock implementations operationalize need intake.

## 9. Open Questions / Follow-ups

- No stock services/screens currently operate directly on `PartyNeed`.

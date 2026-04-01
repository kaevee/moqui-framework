# Order Domain Summary

Date: `2026-04-01`
Domain index: `agents/domains/order/ORDER.md`

## Scope Delivered

- Created Order domain index:
  - `agents/domains/order/ORDER.md`
- Created layered Order dossiers:
  - `agents/domains/order/docs/001_order-domain-dossier.md`
  - `agents/domains/order/docs/002_order-content-communication-dossier.md`
  - `agents/domains/order/docs/003_order-decision-execution-dossier.md`
  - `agents/domains/order/docs/090_order-return-reference.md`
- Applied AGENTS scope model:
  - Full Detail Set selector: strict `prefix` pattern `Order*`
  - Reference-Only Set: non-`Order*` consumers in scanned source

## Layer Coverage

- `001` core lifecycle:
  - `OrderHeader`, `OrderPart`, `OrderItem`
  - `OrderPartParty`, `OrderPartContactMech`, `OrderPartTerm`
- `002` content/communications:
  - `OrderCommunicationEvent`, `OrderContent`, `OrderEmailMessage`, `OrderNote`, `OrderPromoCode`
- `003` decision/execution/integration:
  - `OrderDecision`, `OrderDecisionReason`
  - `OrderServiceJobRun`, `OrderSystemMessage`
  - `OrderItemWorkEffort`, `OrderItemBilling`, `OrderItemFormResponse`, `OrderItemParty`
- `090` reference-only intersections:
  - `ReturnItem`
  - `SystemMessage` extend-entity linkage

## Coverage Included

- Full-detail entity inventories with PK/FK/enum-status/key-business fields by layer.
- Explicit commented-out entity/view scan result (`NONE`) for `OrderEntities.xml`.
- Service wiring inventories with explicit `NONE` where no direct references were found.
- Stock SimpleScreens wiring inventories with explicit `NONE` where applicable.
- Canonical flows per layer and routing guidance via `ORDER.md`.

## Notes

- Current scope is intentionally anchored to:
  - `runtime/component/mantle-udm/entity/OrderEntities.xml`
- Return-domain lifecycle details remain reference-only in `090` until `Return` is requested as a target domain.

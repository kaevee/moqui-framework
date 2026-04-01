# Order Domain Dossier

Target Domain: `Order`  
Scope: `090` reference-only intersection for non-`Order*` consumers in `OrderEntities.xml`.  
Out of Scope: Full return-domain behavior and non-reference downstream deep dives.

See `001_order-domain-dossier.md` for core Order context.

## 1. Domain Boundary

- Reference-only layer for entities/extensions outside `Order*` that point to `Order*` keys.
- Source file scanned: `runtime/component/mantle-udm/entity/OrderEntities.xml`
- Scope disclaimer: `OrderEntities.xml` also defines `ReturnContactMech`, `ReturnHeader`, `ReturnSystemMessage`, `ReturnItemBilling`, and a second `SystemMessage` extend-entity (`returnId`) that are intentionally deferred from this Order dossier because they do not directly FK/join into `Order*` entities. They are expected to be full-detail only when `Return` is targeted as its own domain.

## 2. Full Detail Set (Entity Inventory)

- `NONE` (reference-only layer)

## 2A. Commented-Out Entity/View Definitions

Scanned source:
- `runtime/component/mantle-udm/entity/OrderEntities.xml`

Commented-out `<entity ...>` / `<view-entity ...>` definitions:
- `NONE`

## 3. Reference-Only Set (FK Consumer Map)

`ReturnItem` (`runtime/component/mantle-udm/entity/OrderEntities.xml`)
- FK field(s) to Full Detail: `orderId + orderItemSeqId -> OrderItem`, `orderId -> OrderHeader (one-nofk)`, `replacementOrderId -> OrderHeader`
- Usage note: return line references original and replacement order context.

`SystemMessage` extension (`runtime/component/mantle-udm/entity/OrderEntities.xml`, `<extend-entity entity-name="SystemMessage" package="moqui.service.message">`)
- FK/join field(s) to Full Detail: `orderId -> OrderHeader`, `(orderId, orderPartSeqId) -> OrderPart (one-nofk)`
- Usage note: integration message envelope carries order/order-part keys for traceability.

Deferred non-Order entities in the same source file (reason: no direct FK/join to `Order*`):
- `ReturnContactMech`
- `ReturnHeader`
- `ReturnSystemMessage`
- `ReturnItemBilling`
- `SystemMessage` extend-entity (`returnId`)

## 4. Relationship Map (Domain-Internal Adjacency)

- `ReturnItem` consumes `OrderHeader` and `OrderItem`.
- `SystemMessage` extension consumes `OrderHeader` and `OrderPart`.

## 5. Service Wiring (Full Detail Set)

Reference trace services:
- `runtime/component/mantle-usl/service/OrderReturn.secas.xml`
- `runtime/component/mantle-usl/service/mantle.rest.xml`
- `runtime/component/mantle-usl/service/mantle/account/PaymentServices.xml`
- `runtime/component/mantle-usl/service/mantle/order/OrderInfoServices.xml`
- `runtime/component/mantle-usl/service/mantle/order/ReturnServices.xml`

## 6. Screen Wiring (SimpleScreens only — Full Detail Set)

Reference trace screens:
- `runtime/component/SimpleScreens/screen/SimpleScreens/Order/OrderDetail.xml`
- `runtime/component/SimpleScreens/screen/SimpleScreens/Order/OrderDetail/ReturnItem.xml`
- `runtime/component/SimpleScreens/screen/SimpleScreens/Order/OrderItems.xml`
- `runtime/component/SimpleScreens/screen/SimpleScreens/Return/AddOrderItems.xml`
- `runtime/component/SimpleScreens/screen/SimpleScreens/Return/EditReturn.xml`
- `runtime/component/SimpleScreens/screen/SimpleScreens/Return/EditReturnItems.xml`
- `runtime/component/SimpleScreens/screen/SimpleScreens/Return/FindReturn.xml`
- `runtime/component/SimpleScreens/screen/SimpleScreens/Shipment/ShipmentDetail/ReceiveItem.xml`

## 7. Canonical Flows

Return-from-order mapping:
1. Return flows locate eligible order lines.
2. `ReturnItem` persists the linkage to original/replacement orders/items.
3. Payment/shipment side effects run in return/account services.

Integration-message mapping:
1. System message records carry `orderId` and optional `orderPartSeqId`.
2. Order/system-message bridge entities track external display/origin IDs.

## 8. Agent Quick-Start

- Keep this layer reference-only; do not expand into full return-domain behavior here.
- For functional Order changes, use `001-003`; use `090` only for impact analysis.
- Verify key compatibility when editing return or system-message services that reference Order keys.

## 9. Open Questions / Follow-ups

- If Return domain is requested as a target domain, promote `Return*` from reference-only to full-detail in a dedicated `return` domain dossier set.

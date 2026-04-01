# Order Domain Dossier

Target Domain: `Order`  
Scope: `002` adjacent layer for Order content/communication/promotional attachments.  
Out of Scope: Core order lifecycle (`001`), decision/execution structures (`003`), and reference-only return intersections (`090`).

See `001_order-domain-dossier.md` for core `OrderHeader`/`OrderPart`/`OrderItem` context.

## 1. Domain Boundary

Full-detail entities in this layer:
- `OrderCommunicationEvent`
- `OrderContent`
- `OrderEmailMessage`
- `OrderNote`
- `OrderPromoCode`

Source file:
- `runtime/component/mantle-udm/entity/OrderEntities.xml`

## 2. Full Detail Set (Entity Inventory)

`OrderCommunicationEvent` (`runtime/component/mantle-udm/entity/OrderEntities.xml`)
- PK: `orderId`, `communicationEventId`
- FKs and targets: `orderId -> mantle.order.OrderHeader`, `communicationEventId -> mantle.party.communication.CommunicationEvent`
- Enum/status fields and type references: `NONE`
- Key business fields: `NONE` (junction entity)

`OrderContent` (`runtime/component/mantle-udm/entity/OrderEntities.xml`)
- PK: `orderContentId`
- FKs and targets: `orderId -> mantle.order.OrderHeader`, `(orderId, orderItemSeqId) -> mantle.order.OrderItem (one-nofk)`, `orderContentTypeEnumId -> moqui.basic.Enumeration (OrderContentType)`, `partyId -> mantle.party.Party`, `roleTypeId -> mantle.party.RoleType`, `userId -> moqui.security.UserAccount`
- Enum/status fields and type references: `orderContentTypeEnumId (OrderContentType)`
- Key business fields: `contentLocation`, `description`, `contentDate`, `viewedDate`

`OrderEmailMessage` (`runtime/component/mantle-udm/entity/OrderEntities.xml`)
- PK: `orderId`, `emailMessageId`
- FKs and targets: `orderId -> mantle.order.OrderHeader`, `emailMessageId -> moqui.basic.email.EmailMessage`, `partyId -> mantle.party.Party`, `roleTypeId -> mantle.party.RoleType`
- Enum/status fields and type references: `NONE`
- Key business fields: `orderRevision`

`OrderNote` (`runtime/component/mantle-udm/entity/OrderEntities.xml`)
- PK: `orderId`, `noteDate`
- FKs and targets: `orderId -> mantle.order.OrderHeader`, `userId -> moqui.security.UserAccount`
- Enum/status fields and type references: `NONE`
- Key business fields: `noteText`, `internalNote`

`OrderPromoCode` (`runtime/component/mantle-udm/entity/OrderEntities.xml`)
- PK: `orderId`, `promoCodeId`
- FKs and targets: `orderId -> mantle.order.OrderHeader`, `promoCodeId -> mantle.product.store.ProductStorePromoCode`
- Enum/status fields and type references: `NONE`
- Key business fields: `NONE` (promo-code bridge)

## 2A. Commented-Out Entity/View Definitions

Scanned source:
- `runtime/component/mantle-udm/entity/OrderEntities.xml`

Commented-out `<entity ...>` / `<view-entity ...>` definitions:
- `NONE`

## 3. Reference-Only Set (FK Consumer Map)

Reference-only in this layer:
- `NONE` (cross-domain consumers are covered in `090_order-return-reference.md`)

Shared-context references:
- `OrderContent` depends on core entities `OrderHeader` and `OrderItem`.
- `OrderCommunicationEvent`, `OrderEmailMessage`, `OrderNote`, `OrderPromoCode` depend on `OrderHeader`.

## 4. Relationship Map (Domain-Internal Adjacency)

- `OrderHeader` -> `OrderCommunicationEvent`
- `OrderHeader` -> `OrderContent` (optionally item-scoped by `orderItemSeqId`)
- `OrderHeader` -> `OrderEmailMessage`
- `OrderHeader` -> `OrderNote`
- `OrderHeader` -> `OrderPromoCode`

## 5. Service Wiring (Full Detail Set)

Service files touching this layer:
- `runtime/component/mantle-usl/service/mantle/order/OrderServices.xml`
- `runtime/component/mantle-usl/service/mantle/order/OrderInfoServices.xml`
- `runtime/component/mantle-usl/service/mantle/order/OrderBulkServices.xml`
- `runtime/component/mantle-usl/service/mantle/product/PromotionServices.xml`

Primary services:
- `mantle.order.OrderServices.create#OrderContent`
- `mantle.order.OrderServices.update#OrderContent`
- `mantle.order.OrderServices.save#OrderContentFile`
- entity-auto transitions for `create#mantle.order.OrderNote`, `update#mantle.order.OrderNote`
- promo-code services through `mantle.product.PromotionServices.add#OrderPromoCode` and `remove#OrderPromoCode`

Entity occurrence map (service files):
- `OrderCommunicationEvent` -> `NONE`
- `OrderContent` -> `runtime/component/mantle-usl/service/mantle/order/OrderServices.xml`
- `OrderEmailMessage` -> `runtime/component/mantle-usl/service/mantle/order/OrderInfoServices.xml`
- `OrderNote` -> `runtime/component/mantle-usl/service/mantle/order/OrderBulkServices.xml`, `runtime/component/mantle-usl/service/mantle/order/OrderInfoServices.xml`, `runtime/component/mantle-usl/service/mantle/order/OrderServices.xml`
- `OrderPromoCode` -> `runtime/component/mantle-usl/service/mantle/product/PromotionServices.xml`

## 6. Screen Wiring (SimpleScreens only — Full Detail Set)

Stock screens touching this layer:
- `runtime/component/SimpleScreens/screen/SimpleScreens/Order/OrderDetail.xml`
- `runtime/component/SimpleScreens/screen/SimpleScreens/Shipment/ShipmentDetail.xml`

Entity occurrence map (screen/template files):
- `OrderCommunicationEvent` -> `NONE`
- `OrderContent` -> `runtime/component/SimpleScreens/screen/SimpleScreens/Order/OrderDetail.xml`
- `OrderEmailMessage` -> `NONE`
- `OrderNote` -> `runtime/component/SimpleScreens/screen/SimpleScreens/Order/OrderDetail.xml`, `runtime/component/SimpleScreens/screen/SimpleScreens/Shipment/ShipmentDetail.xml`
- `OrderPromoCode` -> `runtime/component/SimpleScreens/screen/SimpleScreens/Order/OrderDetail.xml`

## 7. Canonical Flows

Attach and update order content:
1. `mantle.order.OrderServices.create#OrderContent`
2. `mantle.order.OrderServices.update#OrderContent`
3. `mantle.order.OrderServices.save#OrderContentFile`
4. UI: `runtime/component/SimpleScreens/screen/SimpleScreens/Order/OrderDetail.xml`

Create/update order notes:
1. `create#mantle.order.OrderNote`
2. `update#mantle.order.OrderNote`
3. UI: `runtime/component/SimpleScreens/screen/SimpleScreens/Order/OrderDetail.xml`

Apply/remove promo codes:
1. `mantle.product.PromotionServices.add#OrderPromoCode`
2. `mantle.product.PromotionServices.remove#OrderPromoCode`
3. UI: `runtime/component/SimpleScreens/screen/SimpleScreens/Order/OrderDetail.xml`

## 8. Agent Quick-Start

- For attachments and notes, start in `OrderDetail.xml` transitions, then jump to `OrderServices.xml`.
- `OrderContent` can be order-level or item-level; preserve `orderItemSeqId` semantics when present.
- `OrderEmailMessage` is modeled as linkage; generation/sending behavior is largely service-driven in `OrderInfoServices.xml`.

## 9. Open Questions / Follow-ups

- `OrderCommunicationEvent` has no direct stock service/screen references by entity name in this scan; verify runtime usage before extending.
- If custom messaging workflows expand, split email vs content/notes into separate higher-number layers.

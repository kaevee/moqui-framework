# Order Domain Dossier

Target Domain: `Order`  
Scope: `003` adjacent layer for decision, execution, integration, and line-level bridge entities.  
Out of Scope: Core order lifecycle (`001`), content/comms (`002`), and reference-only return intersections (`090`).

See `001_order-domain-dossier.md` for core `OrderHeader`/`OrderPart`/`OrderItem` context.

## 1. Domain Boundary

Full-detail entities in this layer:
- `OrderDecision`
- `OrderDecisionReason`
- `OrderServiceJobRun`
- `OrderSystemMessage`
- `OrderItemWorkEffort`
- `OrderItemBilling`
- `OrderItemFormResponse`
- `OrderItemParty`

Source file:
- `runtime/component/mantle-udm/entity/OrderEntities.xml`

## 2. Full Detail Set (Entity Inventory)

`OrderDecision` (`runtime/component/mantle-udm/entity/OrderEntities.xml`)
- PK: `orderId`, `decisionDate`
- FKs and targets: `orderId -> mantle.order.OrderHeader`, `decisionByPartyId -> mantle.party.Party`, `statusId -> moqui.basic.StatusItem (OrderHeader)`
- Enum/status fields and type references: `statusId (OrderHeader)`
- Key business fields: `invalidatedDate`, `approvedAmount`

`OrderDecisionReason` (`runtime/component/mantle-udm/entity/OrderEntities.xml`)
- PK: `orderId`, `decisionDate`, `decisionReasonEnumId`
- FKs and targets: `orderId -> mantle.order.OrderHeader`, `(orderId, decisionDate) -> mantle.order.OrderDecision`, `decisionReasonEnumId -> moqui.basic.Enumeration (OrderDecisionReason)`
- Enum/status fields and type references: `decisionReasonEnumId (OrderDecisionReason)`
- Key business fields: `NONE` (decision reason junction)

`OrderServiceJobRun` (`runtime/component/mantle-udm/entity/OrderEntities.xml`)
- PK: `orderId`, `jobRunId`
- FKs and targets: `orderId -> mantle.order.OrderHeader`, `jobRunId -> moqui.service.job.ServiceJobRun`
- Enum/status fields and type references: `NONE`
- Key business fields: `NONE` (job-run bridge)

`OrderSystemMessage` (`runtime/component/mantle-udm/entity/OrderEntities.xml`)
- PK: `orderId`, `systemMessageId`
- FKs and targets: `orderId -> mantle.order.OrderHeader`, `systemMessageId -> moqui.service.message.SystemMessage`
- Enum/status fields and type references: `NONE`
- Key business fields: `externalId`, `originId`, `displayId`

`OrderItemWorkEffort` (`runtime/component/mantle-udm/entity/OrderEntities.xml`)
- PK: `orderId`, `orderItemSeqId`, `workEffortId`
- FKs and targets: `(orderId, orderItemSeqId) -> mantle.order.OrderItem`, `workEffortId -> mantle.work.effort.WorkEffort`, `forStatusId -> moqui.basic.StatusItem (OrderHeader)`
- Enum/status fields and type references: `forStatusId (OrderHeader)`
- Key business fields: `requiredWork`

`OrderItemBilling` (`runtime/component/mantle-udm/entity/OrderEntities.xml`)
- PK: `orderItemBillingId`
- FKs and targets: `(orderId, orderItemSeqId) -> mantle.order.OrderItem`, `(invoiceId, invoiceItemSeqId) -> mantle.account.invoice.InvoiceItem`, `assetReceiptId -> mantle.product.receipt.AssetReceipt`, `assetIssuanceId -> mantle.product.issuance.AssetIssuance`, `shipmentId -> mantle.shipment.Shipment`
- Enum/status fields and type references: `NONE`
- Key business fields: `quantity`, `amount`

`OrderItemFormResponse` (`runtime/component/mantle-udm/entity/OrderEntities.xml`)
- PK: `orderId`, `orderItemSeqId`, `formResponseId`
- FKs and targets: `(orderId, orderItemSeqId) -> mantle.order.OrderItem`, `formResponseId -> moqui.screen.form.FormResponse`, `partyId -> mantle.party.Party`, `roleTypeId -> mantle.party.RoleType`
- Enum/status fields and type references: `NONE`
- Key business fields: `NONE` (form-response bridge)

`OrderItemParty` (`runtime/component/mantle-udm/entity/OrderEntities.xml`)
- PK: `orderId`, `orderItemSeqId`, `partyId`, `roleTypeId`
- FKs and targets: `(orderId, orderItemSeqId) -> mantle.order.OrderItem`, `partyId -> mantle.party.Party`, `roleTypeId -> mantle.party.RoleType`
- Enum/status fields and type references: `NONE`
- Key business fields: `NONE` (party-role bridge)

## 2A. Commented-Out Entity/View Definitions

Scanned source:
- `runtime/component/mantle-udm/entity/OrderEntities.xml`

Commented-out `<entity ...>` / `<view-entity ...>` definitions:
- `NONE`

## 3. Reference-Only Set (FK Consumer Map)

Reference-only in this layer:
- `NONE` (cross-domain consumers are covered in `090_order-return-reference.md`)

Shared-context references:
- Most entities in this layer attach to core entities `OrderHeader` and `OrderItem` from `001`.

## 4. Relationship Map (Domain-Internal Adjacency)

- `OrderHeader` -> `OrderDecision` -> `OrderDecisionReason`
- `OrderHeader` -> `OrderSystemMessage`
- `OrderHeader` -> `OrderServiceJobRun`
- `OrderItem` -> `OrderItemWorkEffort`
- `OrderItem` -> `OrderItemBilling`
- `OrderItem` -> `OrderItemFormResponse`
- `OrderItem` -> `OrderItemParty`

## 5. Service Wiring (Full Detail Set)

Service files touching this layer:
- `runtime/component/mantle-usl/service/OrderReturn.secas.xml`
- `runtime/component/mantle-usl/service/mantle.rest.xml`
- `runtime/component/mantle-usl/service/mantle/account/InvoiceServices.xml`
- `runtime/component/mantle-usl/service/mantle/account/PaymentServices.xml`
- `runtime/component/mantle-usl/service/mantle/order/OrderInfoServices.xml`
- `runtime/component/mantle-usl/service/mantle/order/OrderServices.xml`
- `runtime/component/mantle-usl/service/mantle/order/ReturnServices.xml`
- `runtime/component/mantle-usl/service/mantle/product/AssetServices.xml`
- `runtime/component/mantle-usl/service/mantle/shipment/ShipmentServices.xml`

Primary service entry points:
- `mantle.order.OrderServices.update#OrderStatus` (decision/status lifecycle integration point)
- `mantle.order.OrderServices.handle#OrderItemChange`
- `mantle.order.OrderServices.handle#OrderPartChange`
- `mantle.order.OrderServices.handle#OrderMajorChange`
- `mantle.order.OrderInfoServices.get#OrderDisplayInfo`
- `mantle.order.OrderInfoServices.get#OrderItemDisplayInfo`

Direct vs orchestration clarification:
- Direct entity consumers in this layer are concentrated on `OrderItemBilling`, `OrderItemWorkEffort`, and `OrderSystemMessage` service references (see occurrence map below).
- The entry-point services listed above are primarily orchestration services on core order lifecycle entities that may indirectly trigger behavior involving this layer.

Entity occurrence map (service files):
- `OrderDecision` -> `NONE`
- `OrderDecisionReason` -> `NONE`
- `OrderServiceJobRun` -> `NONE`
- `OrderSystemMessage` -> `runtime/component/mantle-usl/service/mantle/order/OrderInfoServices.xml`
- `OrderItemWorkEffort` -> `runtime/component/mantle-usl/service/OrderReturn.secas.xml`, `runtime/component/mantle-usl/service/mantle.rest.xml`, `runtime/component/mantle-usl/service/mantle/order/OrderServices.xml`
- `OrderItemBilling` -> `runtime/component/mantle-usl/service/mantle.rest.xml`, `runtime/component/mantle-usl/service/mantle/account/InvoiceServices.xml`, `runtime/component/mantle-usl/service/mantle/account/PaymentServices.xml`, `runtime/component/mantle-usl/service/mantle/order/OrderInfoServices.xml`, `runtime/component/mantle-usl/service/mantle/order/OrderServices.xml`, `runtime/component/mantle-usl/service/mantle/order/ReturnServices.xml`, `runtime/component/mantle-usl/service/mantle/product/AssetServices.xml`, `runtime/component/mantle-usl/service/mantle/shipment/ShipmentServices.xml`
- `OrderItemFormResponse` -> `NONE`
- `OrderItemParty` -> `NONE`

## 6. Screen Wiring (SimpleScreens only — Full Detail Set)

Stock screens touching this layer by entity identifier:
- `NONE`

Entity occurrence map (screen/template files):
- `OrderDecision` -> `NONE`
- `OrderDecisionReason` -> `NONE`
- `OrderServiceJobRun` -> `NONE`
- `OrderSystemMessage` -> `NONE`
- `OrderItemWorkEffort` -> `NONE`
- `OrderItemBilling` -> `NONE`
- `OrderItemFormResponse` -> `NONE`
- `OrderItemParty` -> `NONE`

## 7. Canonical Flows

Decision/status adjunct flow:
1. Decision context captured via `OrderDecision` + `OrderDecisionReason`.
2. Status transition executed through Order status services (`update#OrderStatus`, place/approve/reject flows in `OrderServices.xml`).

Execution linkage flow:
1. `OrderItemWorkEffort` links tasks/work requirements to order items.
2. Fulfillment path evaluates required work/status before fulfillment completion.

Billing linkage flow:
1. `OrderItemBilling` links invoicing/issuance/receipt/shipment artifacts to order items.
2. Accounting/shipment services consume these bridges during invoice and shipment lifecycle.

## 8. Agent Quick-Start

- Treat this layer as bridge entities; most behavior is orchestrated from core order/account/shipment services.
- If you need UI changes, start in core order or shipment screens and trace service calls, because this layer has no direct stock screen references by entity name.
- Keep bridge keys stable; do not collapse or denormalize composite references.

## 9. Open Questions / Follow-ups

- If direct UI maintenance for these entities is introduced, create a dedicated higher-number layer and migrate screen wiring there.
- Consider adding machine-readable maps for bridge entities when service touchpoints change frequently.

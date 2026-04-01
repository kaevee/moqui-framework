# Order Domain Dossier

Target Domain: `Order`  
Scope: `001` core layer for Order lifecycle (`OrderHeader`, `OrderPart`, `OrderItem`) and immediate structural entities.  
Out of Scope: Detailed coverage of content/comms and decision/integration entities (see `002` and `003`), plus reference-only return intersections (`090`).

## 1. Domain Boundary

Full Detail Set selector (domain-wide):
- Type: `prefix`
- Pattern: `Order*`
- Source: `runtime/component/mantle-udm/entity/OrderEntities.xml`

Core entities in full detail in this layer:
- `OrderHeader`
- `OrderPart`
- `OrderItem`
- `OrderPartParty`
- `OrderPartContactMech`
- `OrderPartTerm`

Layer map for all `Order*` entities:
- `001`: `OrderHeader`, `OrderPart`, `OrderItem`, `OrderPartParty`, `OrderPartContactMech`, `OrderPartTerm`
- `002`: `OrderCommunicationEvent`, `OrderContent`, `OrderEmailMessage`, `OrderNote`, `OrderPromoCode`
- `003`: `OrderDecision`, `OrderDecisionReason`, `OrderServiceJobRun`, `OrderSystemMessage`, `OrderItemWorkEffort`, `OrderItemBilling`, `OrderItemFormResponse`, `OrderItemParty`
- `090` (reference-only): `ReturnItem`, `SystemMessage` extension

Primary source file scanned:
- `runtime/component/mantle-udm/entity/OrderEntities.xml`

## 2. Full Detail Set (Entity Inventory)

`OrderHeader` (`runtime/component/mantle-udm/entity/OrderEntities.xml`)
- PK: `orderId`
- FKs and targets: `statusId -> moqui.basic.StatusItem (OrderHeader)`, `processingStatusId -> moqui.basic.StatusItem (OrderProcessing)`, `currencyUomId -> moqui.basic.Uom`, `billingAccountId -> mantle.account.billing.BillingAccount`, `productStoreId -> mantle.product.store.ProductStore`, `salesChannelEnumId -> moqui.basic.Enumeration (SalesChannel)`, `syncStatusId -> moqui.basic.StatusItem (Sync)`, `systemMessageRemoteId -> moqui.service.message.SystemMessageRemote`, `visitId -> moqui.server.Visit`, `enteredByPartyId -> mantle.party.Party`, `parentOrderId -> mantle.order.OrderHeader`
- Enum/status fields and type references: `statusId (OrderHeader)`, `processingStatusId (OrderProcessing)`, `salesChannelEnumId (SalesChannel)`, `syncStatusId (Sync)`
- Key business fields: `orderName`, `entryDate`, `placedDate`, `approvedDate`, `completedDate`, `orderRevision`, `displayId`, `externalId`, `originId`, `originUrl`, `terminalId`, `recurCronExpression`, `lastOrderedDate`, `recurAutoInvoice`, `remainingSubTotal`, `grandTotal`

`OrderPart` (`runtime/component/mantle-udm/entity/OrderEntities.xml`)
- PK: `orderId`, `orderPartSeqId`
- FKs and targets: `orderId -> mantle.order.OrderHeader`, `(orderId, parentPartSeqId) -> mantle.order.OrderPart`, `statusId -> moqui.basic.StatusItem (OrderHeader)`, `vendorPartyId -> mantle.party.Party`, `customerPartyId -> mantle.party.Party`, `facilityId -> mantle.facility.Facility`, `carrierPartyId -> mantle.party.Party`, `shipmentMethodEnumId -> moqui.basic.Enumeration (ShipmentMethod)`, `tradeTermEnumId -> moqui.basic.Enumeration (TermType)`, `settlementTermId -> mantle.account.invoice.SettlementTerm`, `postalContactMechId -> mantle.party.contact.ContactMech`, `telecomContactMechId -> mantle.party.contact.ContactMech`, `signatureRequiredEnumId -> moqui.basic.Enumeration`, `reservationAutoEnumId -> moqui.basic.Enumeration`
- Enum/status fields and type references: `statusId (OrderHeader)`, `shipmentMethodEnumId (ShipmentMethod)`, `tradeTermEnumId (TermType)`, `signatureRequiredEnumId (SignatureRequired)`, `reservationAutoEnumId (AssetReservationAuto)`
- Key business fields: `partName`, `otherPartyOrderId`, `otherPartyOrderDate`, `trackingNumber`, `shippingInstructions`, `maySplit`, `giftMessage`, `isGift`, `isNewCustomer`, `partTotal`, `priority`, `shipAfterDate`, `shipBeforeDate`, `estimatedShipDate`, `estimatedDeliveryDate`, `estimatedPickUpDate`, `validFromDate`, `validThruDate`, `autoCancelDate`, `disablePromotions`, `disableShippingCalc`, `disableTaxCalc`

`OrderItem` (`runtime/component/mantle-udm/entity/OrderEntities.xml`)
- PK: `orderId`, `orderItemSeqId`
- FKs and targets: `orderId -> mantle.order.OrderHeader`, `(orderId, orderPartSeqId) -> mantle.order.OrderPart`, `(orderId, parentItemSeqId) -> mantle.order.OrderItem (one-nofk)`, `itemTypeEnumId -> moqui.basic.Enumeration`, `productId -> mantle.product.Product`, `productFeatureId -> mantle.product.feature.ProductFeature`, `productParameterSetId -> mantle.product.ProductParameterSet`, `quantityUomId -> moqui.basic.Uom`, `fromAssetId -> mantle.product.asset.Asset`, `productPriceId -> mantle.product.ProductPrice`, `productCategoryId -> mantle.product.category.ProductCategory`, `storePromotionId -> mantle.product.store.ProductStorePromotion`, `promoCodeId -> mantle.product.store.ProductStorePromoCode`, `subscriptionId -> mantle.product.subscription.Subscription`, `finAccountId -> mantle.account.financial.FinancialAccount`, `finAccountTransId -> mantle.account.financial.FinancialAccountTrans`, `overrideGlAccountId -> mantle.ledger.account.GlAccount`, `salesOpportunityId -> mantle.sales.opportunity.SalesOpportunity`, `taxAuthorityId -> mantle.other.tax.TaxAuthority`
- Enum/status fields and type references: `itemTypeEnumId (item-type enumeration)`
- Key business fields: `itemDescription`, `comments`, `quantity`, `quantityCancelled`, `selectedAmount`, `requiredByDate`, `unitAmount`, `unitListPrice`, `standardCost`, `isPromo`, `promoQuantity`, `promoTimesUsed`, `promoCodeText`, `sourceReferenceId`, `sourcePercentage`, `amountAlreadyIncluded`, `exemptAmount`, `customerReferenceId`

`OrderPartParty` (`runtime/component/mantle-udm/entity/OrderEntities.xml`)
- PK: `orderId`, `orderPartSeqId`, `partyId`, `roleTypeId`
- FKs and targets: `(orderId, orderPartSeqId) -> mantle.order.OrderPart`, `partyId -> mantle.party.Party`, `roleTypeId -> mantle.party.RoleType`
- Enum/status fields and type references: `NONE`
- Key business fields: `sequenceNum`

`OrderPartContactMech` (`runtime/component/mantle-udm/entity/OrderEntities.xml`)
- PK: `orderId`, `orderPartSeqId`, `contactMechPurposeId`, `contactMechId`
- FKs and targets: `(orderId, orderPartSeqId) -> mantle.order.OrderPart`, `contactMechPurposeId -> mantle.party.contact.ContactMechPurpose`, `contactMechId -> mantle.party.contact.ContactMech`
- Enum/status fields and type references: `NONE`
- Key business fields: `NONE` (contact bridge)

`OrderPartTerm` (`runtime/component/mantle-udm/entity/OrderEntities.xml`)
- PK: `orderId`, `orderPartSeqId`, `settlementTermId`
- FKs and targets: `(orderId, orderPartSeqId) -> mantle.order.OrderPart`, `settlementTermId -> mantle.account.invoice.SettlementTerm`
- Enum/status fields and type references: `NONE`
- Key business fields: `NONE` (term bridge)

## 2A. Commented-Out Entity/View Definitions

Scanned source:
- `runtime/component/mantle-udm/entity/OrderEntities.xml`

Commented-out `<entity ...>` / `<view-entity ...>` definitions:
- `NONE`

## 3. Reference-Only Set (FK Consumer Map)

Reference-only and adjacent layers for core entities:
- See `002_order-content-communication-dossier.md` (content/comms/promo)
- See `003_order-decision-execution-dossier.md` (decision/integration/work/billing/form/party item structures)
- See `090_order-return-reference.md` (cross-domain return/system-message consumers)

## 4. Relationship Map (Domain-Internal Adjacency)

- `OrderHeader` is lifecycle root.
- `OrderHeader` -> many `OrderPart`.
- `OrderHeader` -> many `OrderItem` (direct and/or via `OrderPart`).
- `OrderPart` -> `OrderPartParty`, `OrderPartContactMech`, `OrderPartTerm`.
- `OrderItem` and `OrderPart` both support parent-child structure via `parentItemSeqId` and `parentPartSeqId`.

## 5. Service Wiring (Full Detail Set)

Service files touching this core layer:
- `runtime/component/mantle-usl/service/AccountingPayment.secas.xml`
- `runtime/component/mantle-usl/service/OrderReturn.secas.xml`
- `runtime/component/mantle-usl/service/ProductAsset.secas.xml`
- `runtime/component/mantle-usl/service/ProductSubscription.secas.xml`
- `runtime/component/mantle-usl/service/mantle.rest.xml`
- `runtime/component/mantle-usl/service/mantle/GeneralServices.xml`
- `runtime/component/mantle-usl/service/mantle/account/InvoiceServices.xml`
- `runtime/component/mantle-usl/service/mantle/account/PaymentServices.xml`
- `runtime/component/mantle-usl/service/mantle/order/OrderBulkServices.xml`
- `runtime/component/mantle-usl/service/mantle/order/OrderInfoServices.xml`
- `runtime/component/mantle-usl/service/mantle/order/OrderServices.xml`
- `runtime/component/mantle-usl/service/mantle/order/OrderTestServices.xml`
- `runtime/component/mantle-usl/service/mantle/order/ReturnServices.xml`
- `runtime/component/mantle-usl/service/mantle/other/TaxServices.xml`
- `runtime/component/mantle-usl/service/mantle/party/ContactServices.xml`
- `runtime/component/mantle-usl/service/mantle/party/DuplicateServices.xml`
- `runtime/component/mantle-usl/service/mantle/party/TimeServices.xml`
- `runtime/component/mantle-usl/service/mantle/product/AssetServices.xml`
- `runtime/component/mantle-usl/service/mantle/product/PriceServices.xml`
- `runtime/component/mantle-usl/service/mantle/product/PromotionServices.xml`
- `runtime/component/mantle-usl/service/mantle/product/StoreServices.xml`
- `runtime/component/mantle-usl/service/mantle/product/SubscriptionServices.xml`
- `runtime/component/mantle-usl/service/mantle/sales/SalesReportServices.xml`
- `runtime/component/mantle-usl/service/mantle/shipment/CarrierServices.xml`
- `runtime/component/mantle-usl/service/mantle/shipment/ShipmentInfoServices.xml`
- `runtime/component/mantle-usl/service/mantle/shipment/ShipmentServices.xml`
- `runtime/component/mantle-usl/service/mantle/work/ManufacturingServices.xml`
- `runtime/component/mantle-usl/service/mantle/work/ShipmentWorkServices.xml`

Core Order services:
- `mantle.order.OrderServices.create#Order`
- `mantle.order.OrderServices.update#OrderHeader`
- `mantle.order.OrderServices.create#OrderPart`
- `mantle.order.OrderServices.update#OrderPart`
- `mantle.order.OrderServices.create#OrderItem`
- `mantle.order.OrderServices.update#OrderItem`
- `mantle.order.OrderServices.delete#OrderItem`
- `mantle.order.OrderServices.set#OrderBillingShippingInfo`
- `mantle.order.OrderServices.update#OrderStatus`
- `mantle.order.OrderServices.place#Order`
- `mantle.order.OrderServices.approve#Order`
- `mantle.order.OrderServices.complete#OrderPart`
- `mantle.order.OrderServices.complete#Order`
- `mantle.order.OrderServices.cancel#Order`
- `mantle.order.OrderServices.cancel#OrderPart`
- `mantle.order.OrderServices.cancel#OrderItem`
- `mantle.order.OrderServices.reject#Order`
- `mantle.order.OrderServices.reject#OrderPart`
- `mantle.order.OrderServices.reject#OrderItem`
- `mantle.order.OrderServices.clone#Order`
- `mantle.order.OrderServices.update#OrderRecur`

Supporting helper/validation/reporting note:
- `runtime/component/mantle-usl/service/mantle/order/OrderServices.xml` also contains additional helper and validation services (for example inventory checks, merge, fulfillment checks, and auto-approve paths) beyond the primary entry points listed above.
- `runtime/component/mantle-usl/service/mantle/sales/SalesReportServices.xml` consumes Order status values/document types for sales reporting projections.

## 6. Screen Wiring (SimpleScreens only — Full Detail Set)

Stock screens/templates touching core entities:
- `runtime/component/SimpleScreens/screen/SimpleScreens/Accounting/Reports/OrderIssuedInvoiced.xml`
- `runtime/component/SimpleScreens/screen/SimpleScreens/Accounting/Reports/OrderItemSummary.xml`
- `runtime/component/SimpleScreens/screen/SimpleScreens/Customer/EditCustomer.xml`
- `runtime/component/SimpleScreens/screen/SimpleScreens/Order/FindOrder.xml`
- `runtime/component/SimpleScreens/screen/SimpleScreens/Order/OrderDetail.xml`
- `runtime/component/SimpleScreens/screen/SimpleScreens/Order/OrderDetail/EditItem.xml`
- `runtime/component/SimpleScreens/screen/SimpleScreens/Order/OrderDetail/ItemReserve.xml`
- `runtime/component/SimpleScreens/screen/SimpleScreens/Order/OrderDetail/ReturnItem.xml`
- `runtime/component/SimpleScreens/screen/SimpleScreens/Order/OrderItems.xml`
- `runtime/component/SimpleScreens/screen/SimpleScreens/Order/QuickItems.xml`
- `runtime/component/SimpleScreens/screen/SimpleScreens/Party/EditParty.xml`
- `runtime/component/SimpleScreens/screen/SimpleScreens/Party/FinancialInfo.xml`
- `runtime/component/SimpleScreens/screen/SimpleScreens/QuickLookup.xml`
- `runtime/component/SimpleScreens/screen/SimpleScreens/Shipment/FindShipment.xml`
- `runtime/component/SimpleScreens/screen/SimpleScreens/Shipment/ShipmentDetail/ItemReserve.xml`
- `runtime/component/SimpleScreens/screen/SimpleScreens/Shipment/ShipmentDetail/ReceiveItem.xml`
- `runtime/component/SimpleScreens/screen/SimpleScreens/Shipping/PackShipment/PackSummary/PackCompleted.xml`
- `runtime/component/SimpleScreens/screen/SimpleScreens/Shipping/Picklist/AddOrder.xml`
- `runtime/component/SimpleScreens/screen/SimpleScreens/Shipping/Picklist/AddShipment.xml`
- `runtime/component/SimpleScreens/screen/SimpleScreens/Shipping/Picklist/PicklistDetail.xml`
- `runtime/component/SimpleScreens/template/product/ProductTransitions.xml`

## 7. Canonical Flows

Create order with part and items:
1. `mantle.order.OrderServices.create#Order`
2. `mantle.order.OrderServices.create#OrderPart`
3. `mantle.order.OrderServices.create#OrderItem`
4. UI: `runtime/component/SimpleScreens/screen/SimpleScreens/Order/OrderDetail.xml`, `runtime/component/SimpleScreens/screen/SimpleScreens/Order/OrderItems.xml`

Update quantities/prices and recalc:
1. `mantle.order.OrderServices.update#OrderItem`
2. `mantle.order.OrderServices.recalc#OrderItemAmount`
3. `mantle.order.OrderServices.recalc#OrderPartItemAmounts`

Status lifecycle:
1. `mantle.order.OrderServices.place#Order`
2. `mantle.order.OrderServices.approve#Order`
3. `mantle.order.OrderServices.complete#OrderPart` / `complete#Order`
4. `mantle.order.OrderServices.cancel#Order` / `cancel#OrderPart` / `cancel#OrderItem`
5. `mantle.order.OrderServices.reject#Order` / `reject#OrderPart` / `reject#OrderItem`

## 8. Agent Quick-Start

- Start any Order behavior change in `runtime/component/mantle-usl/service/mantle/order/OrderServices.xml`.
- Keep composite keys exact: `(orderId, orderPartSeqId)` and `(orderId, orderItemSeqId)`.
- Do not mutate status fields directly; use status services/transitions.
- Load this doc plus one adjacent layer (`002` or `003`) when touching non-core entities.

## 9. Open Questions / Follow-ups

- If core-order changes frequently touch return rules, promote `090` into a fuller cross-domain impact checklist.
- Keep `001` limited to core lifecycle entities; avoid reabsorbing `002/003` details here.

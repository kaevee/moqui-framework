# ORDER.md

## Purpose

Entry index for the Order domain dossier.

Use this file first, then load `001` + the smallest relevant layer doc.

## Scope Rule (Authoritative)

- Full Detail Set selector: `prefix`
- Full Detail Set pattern: `Order*`
- Entity inventory source: `runtime/component/mantle-udm/entity/OrderEntities.xml`
- Coverage per Full Detail entity: entity + touching `mantle-usl` services + touching stock `SimpleScreens`.
- If service/screen does not exist: record `NONE`.
- Reference-Only Set: non-`Order*` entities (and entity extensions) with FK/join references to Full Detail entities.

## Dossier Layers

Core and Order-owned layers:
- [001 Order Domain Dossier](docs/001_order-domain-dossier.md)
- [002 Order Content/Comms Dossier](docs/002_order-content-communication-dossier.md)
- [003 Order Decision/Execution Dossier](docs/003_order-decision-execution-dossier.md)

Reference-only downstream intersections:
- [090 Order Return/SystemMessage Reference](docs/090_order-return-reference.md)

## Start Here by Task

- Order create/update/line-item + status lifecycle: `001`
- Order content, notes, communications, promo code links: `002`
- Decision reasons, system-message links, work-effort/billing/form/party item bridges: `003`
- Return/system-message FK consumer impact analysis: `090`

## Canonical Order Flows

Canonical flow definitions are distributed across layers in section `7` of:
- `001`: order creation, part/item updates, status transitions
- `002`: content/note/email/promo interactions
- `003`: decision + execution/integration structures
- `090`: return/system-message intersections

## Source-of-Truth Implementation Files

Entity model files:
- `runtime/component/mantle-udm/entity/OrderEntities.xml`

Service files touching `Order*` entities:
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

Stock screen/template files touching `Order*` entities:
- `runtime/component/SimpleScreens/screen/SimpleScreens/Accounting/Reports/OrderIssuedInvoiced.xml`
- `runtime/component/SimpleScreens/screen/SimpleScreens/Accounting/Reports/OrderItemSummary.xml`
- `runtime/component/SimpleScreens/screen/SimpleScreens/Accounting/Payment/EditPayment.xml`
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
- `runtime/component/SimpleScreens/screen/SimpleScreens/Return/AddOrderItems.xml`
- `runtime/component/SimpleScreens/screen/SimpleScreens/Shipment/FindShipment.xml`
- `runtime/component/SimpleScreens/screen/SimpleScreens/Shipment/ShipmentDetail.xml`
- `runtime/component/SimpleScreens/screen/SimpleScreens/Shipment/ShipmentDetail/ItemReserve.xml`
- `runtime/component/SimpleScreens/screen/SimpleScreens/Shipment/ShipmentDetail/ReceiveItem.xml`
- `runtime/component/SimpleScreens/screen/SimpleScreens/Shipping/PackShipment/PackSummary/PackCompleted.xml`
- `runtime/component/SimpleScreens/screen/SimpleScreens/Shipping/Picklist/AddOrder.xml`
- `runtime/component/SimpleScreens/screen/SimpleScreens/Shipping/Picklist/AddShipment.xml`
- `runtime/component/SimpleScreens/screen/SimpleScreens/Shipping/Picklist/PicklistDetail.xml`
- `runtime/component/SimpleScreens/template/product/ProductTransitions.xml`

## Maintenance Checklist

- Keep `001-003` layer boundaries aligned with natural Order subgraphs.
- Keep service and screen inventories exhaustive with explicit `NONE`.
- Keep `090` reference-only; no downstream deep dives there.
- Re-verify against implementation files before generating/modifying code.

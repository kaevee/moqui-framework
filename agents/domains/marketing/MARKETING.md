# MARKETING.md

## Purpose

Entry index for the Marketing domain dossier.

Use this file first, then load `001` + the smallest relevant layer doc.

## Scope Rule (Authoritative)

- Full Detail Set selector: `namespace`
- Full Detail Set pattern: `mantle.marketing.*`
- Entity inventory source: `runtime/component/mantle-udm/entity/MarketingEntities.xml`
- Coverage per Full Detail entity: entity + touching `mantle-usl` services + touching stock `SimpleScreens`.
- If service/screen does not exist: record `NONE`.
- Reference-Only Set: non-`mantle.marketing.*` entities with FK references to Full Detail entities.

## Dossier Layers

Core and Marketing-owned layers:
- [001 Marketing Campaign Core](docs/001_marketing-campaign-core-dossier.md)
- [002 Contact List Layer](docs/002_marketing-contact-list-dossier.md)
- [003 Market Segment Layer](docs/003_marketing-segment-dossier.md)
- [004 Tracking Layer](docs/004_marketing-tracking-dossier.md)

Reference-only downstream intersections:
- [090 Marketing Reference Intersections](docs/090_marketing-reference-intersections-dossier.md)

## Start Here by Task

- Campaign lifecycle, participant assignment, campaign notes: `001`
- Contact list modeling, opt-in status, communication status joins: `002`
- Segment definition (classification/dimension/geo/party) and catalog UI wiring: `003`
- Tracking code attribution across order/return/visit: `004`
- Cross-domain FK impact (Sales, Party communication, Product definitions): `090`

## Canonical Marketing Flows

Canonical flow definitions are distributed across layers in section `7` of:
- `001`: campaign lifecycle
- `002`: contact list lifecycle
- `003`: segment lifecycle and catalog integration points
- `004`: tracking attribution lifecycle
- `090`: cross-domain FK traceability

## Source-of-Truth Implementation Files

Entity model files:
- `runtime/component/mantle-udm/entity/MarketingEntities.xml`
- `runtime/component/mantle-udm/entity/SalesEntities.xml` (reference-only consumers)
- `runtime/component/mantle-udm/entity/PartyEntities.xml` (reference-only consumers)
- `runtime/component/mantle-udm/entity/ProductDefinitionEntities.xml` (reference-only consumers)

Service files touching `mantle.marketing.*` entities:
- `NONE` (no direct `mantle.marketing.*` / Marketing entity-name hits under `runtime/component/mantle-usl/service`)

Stock screen/template files touching `mantle.marketing.*` entities:
- `runtime/component/SimpleScreens/screen/SimpleScreens/Catalog/Category/EditCategory.xml`
- `runtime/component/SimpleScreens/screen/SimpleScreens/Catalog/Product/EditProduct.xml`

## Maintenance Checklist

- Keep namespace selector strict to `mantle.marketing.*`.
- Keep layer boundaries by subpackage: campaign/contact/segment/tracking.
- Keep `090` reference-only; do not expand into full Sales/Party/Product downstream behavior.
- Re-verify `mantle-usl` service coverage if Marketing services are added in future.

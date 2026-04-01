# Marketing Domain Summary

Date: `2026-04-01`
Domain index: `agents/domains/marketing/MARKETING.md`

## Outcome

- Created a layered Marketing domain dossier set using namespace scope.
- Full Detail selector declared as `namespace` with pattern `mantle.marketing.*` from `MarketingEntities.xml`.
- Added dedicated reference-only intersections for non-marketing FK consumers.

## Artifacts Added

- `agents/domains/marketing/MARKETING.md`
- `agents/domains/marketing/docs/001_marketing-campaign-core-dossier.md`
- `agents/domains/marketing/docs/002_marketing-contact-list-dossier.md`
- `agents/domains/marketing/docs/003_marketing-segment-dossier.md`
- `agents/domains/marketing/docs/004_marketing-tracking-dossier.md`
- `agents/domains/marketing/docs/090_marketing-reference-intersections-dossier.md`
- `agents/domains/marketing/summaries/001_marketing-domain-summary.md`

## Scope Decision

- `prefix` selector was rejected because Marketing entities are split across multiple non-prefix names (`MarketingCampaign*`, `ContactList*`, `Market*`, `TrackingCode*`).
- `namespace` selector was used per AGENTS exception rule: `mantle.marketing.*`.

## Coverage Notes

- Full-detail entity inventory documented for all 17 Marketing namespace entities.
- `mantle-usl` direct service hits for Marketing entities: `NONE`.
- Stock `SimpleScreens` direct Marketing entity hits are limited to MarketSegment list usage in:
  - `runtime/component/SimpleScreens/screen/SimpleScreens/Catalog/Category/EditCategory.xml`
  - `runtime/component/SimpleScreens/screen/SimpleScreens/Catalog/Product/EditProduct.xml`

## Reference-Only Intersections Captured

- Sales: `SalesOpportunity`, `SalesOpportunityTracking`
- Party communication: `CommunicationEvent`
- Product definition: `ProductDbForm`, `ProductUomDimension`, `ProductOtherIdentification`, `ProductParameterOption`, `ProductParameterValue`, `ProductCategoryIdent`

## Open Follow-up

- Re-scan and promote service wiring sections if a Marketing-focused `mantle-usl` service module is added later.

# Inventory PhysicalInventory Review Summary

Date: `2026-04-01`
Review source: `agents/domains/inventory/reviews/002_inventory-physicalinventory-review.md`

## Outcome

- Review verdict (`PASS`) accepted.
- Optional usability improvements were applied to the `002` dossier and summary.
- One recommendation was partially pushed back based on source evidence.

## Changes Applied

1. REST endpoint detail added to dossier:
- `POST facilities/{facilityId}/products/{productId}/physicalQuantity`
- `POST facilities/{facilityId}/products/{productId}/physicalChange`

2. Service input/output parameter summaries added to dossier section `5` for:
- `record#PhysicalInventoryQuantity`
- `record#PhysicalInventoryChange`
- `update#PhysicalInventoryDate`
- `post#PhysicalInventoryVariance`
- `repost#PhysicalInventoryVariance`

3. Screen-path precision improvement applied in summary `002`:
- Expanded `FindSummary*` wildcard into explicit file paths:
- `FindSummary.xml`
- `FindSummary/PhysicalQuantity.xml`
- `FindSummary/PhysicalChange.xml`

## Pushback / Clarification

- Recommendation to state that `PhysicalInventoryCount` records are created internally by `record#PhysicalInventoryQuantity` was not adopted as-is.
- Source check in `runtime/component/mantle-usl/service/mantle/product/AssetServices.xml` shows:
- `record#PhysicalInventoryQuantity` performs `update#mantle.product.asset.PhysicalInventoryCount` when `physicalInventoryCountId` is supplied.
- No `create#mantle.product.asset.PhysicalInventoryCount` call exists in that service.
- Dossier text now clarifies this exact behavior.

# Order Domain Audit Summary

Date: `2026-04-01`
Review: `agents/domains/order/reviews/001_order-domain-audit.md`
Domain index: `agents/domains/order/ORDER.md`

## Outcome

- Audit reviewed and actioned.
- Findings were handled as a mix of direct fixes and scoped pushback (where expansion would violate current domain boundary intent).

## Fixes Applied

1. Finding 1 (Return* coverage gap):
- Added explicit scope disclaimer in:
  - `agents/domains/order/docs/090_order-return-reference.md`
- Documented deferred entities in the same source file:
  - `ReturnContactMech`, `ReturnHeader`, `ReturnSystemMessage`, `ReturnItemBilling`, and `SystemMessage` extend-entity (`returnId`)

2. Finding 2 (missing service file):
- Added:
  - `runtime/component/mantle-usl/service/mantle/sales/SalesReportServices.xml`
- Updated in:
  - `agents/domains/order/ORDER.md`
  - `agents/domains/order/docs/001_order-domain-dossier.md`

3. Finding 3 (missing screens):
- Added `runtime/component/SimpleScreens/screen/SimpleScreens/Return/AddOrderItems.xml` to:
  - `agents/domains/order/ORDER.md`
  - `agents/domains/order/docs/090_order-return-reference.md`
- Added `runtime/component/SimpleScreens/screen/SimpleScreens/Accounting/Payment/EditPayment.xml` to:
  - `agents/domains/order/ORDER.md`

4. Finding 4 (003 service wiring clarity):
- Added explicit direct-vs-orchestration clarification in:
  - `agents/domains/order/docs/003_order-decision-execution-dossier.md`

5. Finding 6 (helper services informational):
- Added helper/validation/reporting note in:
  - `agents/domains/order/docs/001_order-domain-dossier.md`

## Pushback / Intentional Non-Changes

- Finding 1 expansion option (listing all Return* as reference-only entries) was not fully adopted.
  - Reason: current `090` layer is scoped to non-`Order*` records that directly FK/join into `Order*` entities.
  - Resolution: explicit deferred-scope disclaimer was added instead.

- Finding 3 optional promotion screen (`ProductStore/Promotion/EditPromotion.xml`) was not added.
  - Reason: Order reference appears inside a commented block; no active runtime wiring.

- Finding 5 template strictness (repeat reference-only FK maps in every layer) was not applied.
  - Reason: layering intentionally centralizes cross-domain FK consumer mapping in `090` to avoid duplication.

## Files Updated in This Audit Cycle

- `agents/domains/order/ORDER.md`
- `agents/domains/order/docs/001_order-domain-dossier.md`
- `agents/domains/order/docs/003_order-decision-execution-dossier.md`
- `agents/domains/order/docs/090_order-return-reference.md`
- `agents/domains/order/summaries/002_order-domain-audit-summary.md`

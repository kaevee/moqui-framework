# Order Domain Audit Follow-up Review

Date: `2026-04-01`
Reviewed artifact: `agents/domains/order/summaries/002_order-domain-audit-summary.md`
Prior review: `agents/domains/order/reviews/001_order-domain-audit.md`

Verified against current state of:
- `agents/domains/order/ORDER.md`
- `agents/domains/order/docs/001_order-domain-dossier.md`
- `agents/domains/order/docs/003_order-decision-execution-dossier.md`
- `agents/domains/order/docs/090_order-return-reference.md`

---

## Verdict: PASS — all claimed fixes verified, pushback decisions reasonable

---

## Fix Verification

### Finding 1 (Return* coverage gap) — VERIFIED

- `090` line 13: scope disclaimer added stating that `ReturnContactMech`, `ReturnHeader`, `ReturnSystemMessage`, `ReturnItemBilling`, and the second `SystemMessage` extend-entity (`returnId`) are intentionally deferred.
- `090` lines 37-42: deferred entities listed explicitly under a "Deferred non-Order entities" subsection in section 3.
- All 5 items from the audit finding are present.

### Finding 2 (missing SalesReportServices.xml) — VERIFIED

- `ORDER.md` line 71: `runtime/component/mantle-usl/service/mantle/sales/SalesReportServices.xml` added to source-of-truth service list.
- `001` line 117: same file added to section 5 service file list.
- `001` lines 148-149: supporting note added explaining that `SalesReportServices.xml` consumes Order status values/document types for sales reporting projections.

### Finding 3 (missing screens) — VERIFIED

- `ORDER.md` line 93: `Return/AddOrderItems.xml` added to source-of-truth screen list.
- `ORDER.md` line 81: `Accounting/Payment/EditPayment.xml` added to source-of-truth screen list.
- `090` line 64: `Return/AddOrderItems.xml` added to section 6 reference trace screens.

### Finding 4 (003 service wiring clarity) — VERIFIED

- `003` lines 121-123: "Direct vs orchestration clarification" paragraph added to section 5, explaining that direct entity consumers are concentrated on `OrderItemBilling`, `OrderItemWorkEffort`, and `OrderSystemMessage`, while the listed entry-point services are orchestration services that may indirectly trigger behavior.

### Finding 6 (helper services note) — VERIFIED

- `001` lines 147-149: "Supporting helper/validation/reporting note" added to section 5, noting that `OrderServices.xml` contains additional helper/validation services beyond the primary entry points.

---

## Pushback Verification

### Finding 1 expansion (full Return* as reference-only entries) — ACCEPTED

Decision to use a scope disclaimer instead of full reference-only entries is consistent with the `090` layer's stated scope of "non-`Order*` records that directly FK/join into `Order*` entities." The deferred Return* entities (`ReturnContactMech`, `ReturnHeader`, `ReturnSystemMessage`, `ReturnItemBilling`) do not FK directly into `Order*` entities — they are Return-domain internal. Only `ReturnItem` has direct `Order*` FKs.

### Finding 3 EditPromotion.xml — ACCEPTED

Correct to omit — the Order reference is inside a commented XML block with no active runtime wiring.

### Finding 5 template strictness — ACCEPTED

The layered delegation approach (centralizing FK consumer mapping in `090`) avoids duplication and is a reasonable interpretation of the template requirement.

---

## Remaining Observations

No new issues found. The summary accurately reflects the changes applied, and the pushback decisions are well-reasoned and documented. The dossier set is now consistent with all actionable findings from the initial audit.

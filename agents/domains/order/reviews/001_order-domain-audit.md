# Order Domain Dossier Audit

Date: `2026-04-01`
Reviewed artifacts:
- `agents/domains/order/ORDER.md`
- `agents/domains/order/docs/001_order-domain-dossier.md`
- `agents/domains/order/docs/002_order-content-communication-dossier.md`
- `agents/domains/order/docs/003_order-decision-execution-dossier.md`
- `agents/domains/order/docs/090_order-return-reference.md`
- `agents/domains/order/summaries/001_order-domain-summary.md`

Verified against:
- `runtime/component/mantle-udm/entity/OrderEntities.xml`
- `runtime/component/mantle-usl/service/` (full tree)
- `runtime/component/SimpleScreens/` (full tree)

---

## Verdict: PASS with findings

The dossier is structurally sound and accurate in what it covers. All claimed file paths exist, all named services are verified, and all Order* entities are accounted for. The findings below are coverage gaps, not errors.

---

## Finding 1 (Medium): Missing Return* entities from Reference-Only Set

**Severity:** Medium
**Layer:** `090_order-return-reference.md`

`OrderEntities.xml` contains 4 additional Return-package entities that live in the same source file but are not captured anywhere in the dossier:

| Entity | Line | Notes |
|---|---|---|
| `ReturnHeader` | 698 | Root return entity; FK to Party, ProductStore, Facility |
| `ReturnContactMech` | 690 | Return-level contact bridge |
| `ReturnSystemMessage` | 815 | Return-to-SystemMessage bridge |
| `ReturnItemBilling` | 921 | Return item billing bridge; FK to InvoiceItem, AssetReceipt |

Additionally, there is a **second `extend-entity` for `SystemMessage`** at line 811 (for Return context with `returnId` field) that is not documented. The dossier only captures the Order-context extend-entity.

**Impact:** An agent scanning `OrderEntities.xml` to understand its full contents will find entities not mentioned in any dossier layer. The `090` doc's scope statement says it covers "non-`Order*` consumers in `OrderEntities.xml`" but only lists `ReturnItem` and one `SystemMessage` extension.

**Recommendation:** Either:
- (a) Expand `090` to list all Return* entities and the second SystemMessage extend-entity as reference-only entries, or
- (b) Add a scope disclaimer to `090` stating that Return* entities beyond `ReturnItem` are intentionally deferred until a `Return` target domain is requested.

Option (b) is lighter and consistent with the summary's stated intent.

---

## Finding 2 (Medium): Missing service file — SalesReportServices.xml

**Severity:** Medium
**Layer:** `ORDER.md` source-of-truth list, `001`

The file `runtime/component/mantle-usl/service/mantle/sales/SalesReportServices.xml` references Order entities (status enums like `OrderPlaced`, `OrderApproved`, `OrderCompleted`, and document type `MantleSalesOrderItem`) but is absent from:
- The `ORDER.md` source-of-truth service file list
- All layer service wiring sections

**Impact:** An agent extending Order status enums or modifying the sales-order reporting pipeline would not be directed to this file.

**Recommendation:** Add to `ORDER.md` source-of-truth list. Optionally add a one-line entry to the `001` service wiring section noting it as a reporting consumer.

---

## Finding 3 (Low-Medium): Missing screen files

**Severity:** Low-Medium
**Layer:** `ORDER.md` source-of-truth list, `090`

Three SimpleScreens files reference Order entities but are not listed in the dossier or `ORDER.md`:

| Screen | Entity/Service References | Notes |
|---|---|---|
| `runtime/component/SimpleScreens/screen/SimpleScreens/Return/AddOrderItems.xml` | `mantle.order.ReturnServices.add#OrderItemToReturn`, `find#ReturnableOrderItems` | Return-to-order item selection screen |
| `runtime/component/SimpleScreens/screen/SimpleScreens/Accounting/Payment/EditPayment.xml` | `OrderPartNameTemplate`, link to `orderDetail` | Payment screen with order-part display |
| `runtime/component/SimpleScreens/screen/SimpleScreens/ProductStore/Promotion/EditPromotion.xml` | `mantle.order.OrderItemAndPartSummary` (in commented section) | Minimal/commented reference |

**Impact:** `AddOrderItems.xml` is the most significant gap — it's a functional screen in the return-from-order workflow. `EditPayment.xml` provides order context in payment views. The promotion screen reference is commented and low priority.

**Recommendation:**
- Add `AddOrderItems.xml` to `090` screen wiring (it fits the return-order intersection).
- Add `EditPayment.xml` to `ORDER.md` source-of-truth screen list.
- `EditPromotion.xml` can be omitted (commented reference).

---

## Finding 4 (Low): 003 layer service wiring lists files not specific to its entities

**Severity:** Low
**Layer:** `003_order-decision-execution-dossier.md` section 5

The `003` service wiring section lists 9 service files as "touching this layer." However, the entity occurrence map shows that 4 of the 8 entities in this layer have `NONE` for service references (`OrderDecision`, `OrderDecisionReason`, `OrderServiceJobRun`, `OrderItemFormResponse`, `OrderItemParty`). The "primary service entry points" listed (`update#OrderStatus`, `handle#OrderItemChange`, etc.) are actually defined in `OrderServices.xml` and operate on core `001` entities — they are upstream orchestrators, not services that directly touch `003` entities.

**Impact:** An agent reading `003` might expect those services to directly manipulate decision/execution entities, when in reality they operate on the core order lifecycle and may trigger downstream effects.

**Recommendation:** Add a clarifying note in section 5 distinguishing between:
- Services that directly read/write `003` entities (primarily `OrderItemBilling` and `OrderItemWorkEffort` consumers)
- Orchestration services that indirectly affect `003` entities through cascading logic

---

## Finding 5 (Low): Template compliance — minor section gaps

**Severity:** Low
**Layer:** All docs

The required template from CLAUDE.md specifies a section `3. Reference-Only Set (FK Consumer Map)` with entity-level detail (entity name, FK fields, usage note) for non-Full-Detail FK consumers. In `001`, `002`, and `003`, this section delegates entirely to other layer docs or `090` rather than listing any reference-only entities inline.

This is arguably correct since each layer focuses on its own Full Detail entities and cross-references are handled by `090`. However, per the AGENTS.md checklist step 3 ("Scan all entities and include any entity that has FK field(s) pointing to any Full Detail entity"), each layer could list its own FK consumers rather than deferring entirely.

**Impact:** Low. The current delegation approach works but deviates slightly from the letter of the template.

**Recommendation:** Acceptable as-is. If strict template compliance is desired, add brief inline FK consumer lists per layer (e.g., `001` could note that `OrderContent.orderId` -> `OrderHeader`, etc., but these are domain-internal and already covered in `002`/`003`).

---

## Finding 6 (Info): Additional services in OrderServices.xml not individually named

**Severity:** Informational
**Layer:** `001`

`OrderServices.xml` contains many additional services beyond those listed in the `001` core services section, including:
- `autoApprove#Order`
- `isFulfilled#OrderPart`
- `checkComplete#OrderPart`
- `add#OrderProductQuantity`
- `merge#OrderItems`
- Various helper/validation services

The dossier lists the primary CRUD and lifecycle services but does not exhaustively enumerate every service in the file.

**Impact:** Low. The dossier's stated purpose is to provide routing and lookup, not to replace reading the actual file. The core services listed cover the main entry points.

**Recommendation:** Acceptable as-is. Optionally add a note in `001` section 5 that the listed services are primary entry points and the file contains additional helper/validation services.

---

## Structural Assessment

| Criterion | Status |
|---|---|
| Required section template (1-9) followed | PASS — all docs follow the template |
| Layer boundaries follow natural entity groupings | PASS |
| Each layer is self-contained for its scope | PASS |
| Cross-references between layers present | PASS |
| Domain index (ORDER.md) has all required sections | PASS |
| Explicit NONE recorded where no services/screens exist | PASS |
| Entity inventory includes PK/FK/enum/key fields | PASS |
| Source file paths are exact and verified | PASS (all exist) |
| Summary matches delivered scope | PASS |

---

## Action Items (Priority Order)

1. Add scope disclaimer to `090` for undocumented Return* entities (Finding 1)
2. Add `SalesReportServices.xml` to `ORDER.md` source-of-truth list (Finding 2)
3. Add `AddOrderItems.xml` and `EditPayment.xml` to screen inventories (Finding 3)
4. Clarify `003` service wiring: direct vs orchestration (Finding 4)
5. (Optional) Enumerate additional OrderServices.xml helpers in `001` (Finding 6)

# Summary: qvt2 Framework Review Follow-up

Date: 2026-03-31
Source review: `agents/reviews/2026-03-31/001_kaevee-qvt2-framework-review.md`

## Assessment

The review is directionally strong and mostly accurate:

- The `getMenuData()` mounted-path rewrite in `ScreenRenderImpl` is safer than regex `replaceFirst()` and correctly handles boundaries (`/`, `?`, `#`) with exact-prefix behavior.
- The missing test-file concern is valid for this repo: `ScreenRenderImplPathRewriteTests.groovy` is referenced in the guide but is not present.
- The guide/example mismatch (`qvue` example vs `qvue2` canonical path) was a legitimate clarity issue.

I agree with the review verdict: approve with minor follow-ups.

## Fixes Applied

Updated `docs/qvt2-rendering-guide.md` to remove confusion and stale claims:

1. Switched the example section to `qvue2` naming:
   - heading updated to "Add qvue2 component content"
   - sample uses `render-modes="vue,qvue2"` and `type="qvue2"`
2. Added explicit legacy-alias note for migration context (`qvue`, `qjs`, `qvt`).
3. Changed "is covered by" test language to a TODO-style instruction:
   - now says to add a regression test at the target path instead of implying it already exists.

## Remaining Follow-up

- Implement `framework/src/test/groovy/org/moqui/impl/screen/ScreenRenderImplPathRewriteTests.groovy` to lock behavior and prevent regressions.

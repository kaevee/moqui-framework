# QVT2 Rendering Guide (Vue 3 + Quasar 2)

This guide explains how qvt2 rendering works in this codebase and how to build screens/components that run under Vue 3 and Quasar 2.

## What qvt2 is

`qvt2` is the Quasar 2 / Vue 3 rendering stack, mounted through `/qapps2`.

It coexists with the existing `qvt` stack (`/qapps`, Vue 2 + Quasar 1).

## Runtime Flow

1. Request comes to `/qapps2/...`.
2. `qapps2.xml` loads Vue 3, Quasar 2, Pinia, `httpVueLoader`, and `WebrootVue.qvt2.js`.
3. `WebrootVue.qvt2.js` drives client navigation and component loading.
4. Menu data is fetched from `/menuData...` with:
   - `qvt2Path` (for example `/qapps2`)
   - `screenMountedPath` (for example `/apps`)
5. Server rewrites menu paths safely in `ScreenRenderImpl.getMenuData(...)` so links stay on `/qapps2` while screen paths still resolve under `/apps`.

## Key Files

- Render mode registration: `framework/src/main/resources/MoquiDefaultConf.xml`
- Server menu path rewrite: `framework/src/main/groovy/org/moqui/impl/screen/ScreenRenderImpl.groovy`
- qvt2 shell: `runtime/base-component/webroot/screen/webroot/qapps2.xml`
- qvt2 page wrapper: `runtime/base-component/webroot/screen/includes/WebrootVue.qvt2.ftl`
- qvt2 client app: `runtime/base-component/webroot/screen/webroot/js/WebrootVue.qvt2.js`
- qvt2 macro template: `runtime/template/screen-macro/DefaultScreenMacros.qvt2.ftl`
- Pinia store bridge: `runtime/base-component/webroot/screen/webroot/js/MoquiStore.js`

## Render Modes and Extensions

Server render outputs include:

- `qvt2` (macro template rendering for Vue 3/Quasar 2)
- `qvue2`
- `qjs2`

The qvt2 client loader (`WebrootVue.qvt2.js`) uses canonical qapps2 extensions:

- `qvue2` for Vue SFC-like components
- `qjs2` for JS component modules
- `qvt2` for template fragments

It also supports legacy aliases (`qvue`, `qjs`, `qvt`) for backward compatibility.

When loading components, the client resolves extension preference from:

1. explicit extension in the path
2. `renderModes`
3. canonical default for qapps2

For JS-backed components that require a template fetch, the loader will try the resolved template extension and then fallback candidates (including aliases) on `404`.

## Creating qvt2 Screens

### 1) Mount and open under qapps2

Use URLs under `/qapps2/...` for Vue 3 + Quasar 2 rendering.

The qvt2 wrapper sets:

- `confBasePath = <context>/apps`
- `confLinkBasePath = <context>/qapps2`

This allows screen resolution via `/apps` while browser URLs stay under `/qapps2`.

### 2) Use qvt2-compatible templates

When writing or updating macro templates for qvt2, use Vue 3 slot syntax:

- Use `v-slot:name`
- Do not use legacy `slot=""` / `slot-scope=""`

Example:

```html
<template v-slot:header="header">
  ...
</template>
```

### 3) Add qvue2 component content

A typical pattern is a screen with qvue2 output:

```xml
<screen render-modes="vue,qvue2" server-static="vue,qvue2">
  <widgets>
    <render-mode>
      <text type="qvue2"><![CDATA[
        <template><q-btn label="Run" @click="run"/></template>
        <script>
        module.exports = {
          methods: {
            run: function () { this.$root.setUrl('/apps/Tools'); }
          }
        };
        </script>
      ]]></text>
    </render-mode>
  </widgets>
</screen>
```

If you are migrating older screens, legacy aliases (`qvue`, `qjs`, `qvt`) are still supported by the qvt2 loader.

## Using Pinia (moqui.store)

`MoquiStore.js` defines a Pinia store named `moqui` and exposes it as `moqui.store`.

In qvt2:

- Pinia is installed before Quasar and `httpVueLoader`.
- Root state is synced into Pinia on path/parameter/session/loading/locale changes.
- Root state now also syncs navigation context (`currentLinkUrl`, `navMenuList`).

Use from components:

```js
var store = moqui.store();
var token = store.moquiSessionToken;
```

For new app-level code, prefer store reads over direct `$root` access.

## qapps2 Debug Mode

Enable targeted loader/navigation diagnostics with query parameter:

- `?_qapps2Debug=true` (or `1`) to enable
- `?_qapps2Debug=false` (or `0`) to disable

Debug mode is persisted in `localStorage` (`moqui.qapps2.debug`) and logs under the `[qapps2-debug]` prefix.

Current debug logs include:

- extension resolution for `loadComponent()`
- template fallback attempts
- final `menuData` request URL (including `qvt2Path` and `screenMountedPath`)

## Readiness and CI Checks

A qapps2 readiness script is available at:

- `runtime/base-component/webroot/tools/check-qapps2-readiness.sh`

It validates:

- required qapps2 library declarations in `build.gradle`
- script load order in `qapps2.xml` (Vue -> vue-demi -> Pinia -> Quasar; store before WebrootVue)
- loader extension map and plugin install order in `WebrootVue.qvt2.js`
- menuData rewrite parameters
- robots disallow list includes `qapps2`

Run manually:

```bash
bash runtime/base-component/webroot/tools/check-qapps2-readiness.sh
```

CI integration:

- `.travis.yml` runs the readiness script before `load` and `test`.

## Server Rewrite Regression Test

Mounted-path rewrite logic is covered by:

- `framework/src/test/groovy/org/moqui/impl/screen/ScreenRenderImplPathRewriteTests.groovy`

This verifies:

- exact mount-path rewrite
- path segment boundary behavior
- query/hash handling
- no false rewrite for `/qapps` vs `/qapps2`

## Migration Notes (qvt -> qvt2)

- Move from Vue 2 APIs to Vue 3-safe APIs (`Vue.extend` -> `Vue.defineComponent`).
- Replace slot syntax (`slot`, `slot-scope`) with `v-slot`.
- Ensure fetch error paths handle non-`2xx` responses.
- Keep menu path rewrite parameters in any custom menu-data request implementation:
  - `qvt2Path`
  - `screenMountedPath`

## Troubleshooting

- If navigation links jump to `/apps` instead of `/qapps2`, verify `menuData` requests include `qvt2Path` and `screenMountedPath`.
- If Quasar components render as plain HTML tags, verify plugin load order:
  - Vue -> vue-demi -> Pinia -> Quasar -> `WebrootVue.qvt2.js`
- If slot content does not render, check for legacy slot syntax.

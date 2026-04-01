# Marketing Domain Dossier

Target Domain: `Marketing`
Scope: `002` adjacent layer for contact-list entities in `mantle.marketing.contact`.
Out of Scope: Campaign core (`001`), segment (`003`), tracking (`004`), and cross-domain reference-only intersections (`090`).

See `001_marketing-campaign-core-dossier.md` for core Marketing campaign context.

## 1. Domain Boundary

Full-detail entities in this layer:
- `ContactList`
- `ContactListEmail`
- `ContactListCommStatus`
- `ContactListParty`

Primary source file scanned:
- `runtime/component/mantle-udm/entity/MarketingEntities.xml`

## 2. Full Detail Set (Entity Inventory)

`ContactList` (`runtime/component/mantle-udm/entity/MarketingEntities.xml`)
- PK: `contactListId`
- FKs and targets: `contactListTypeEnumId -> moqui.basic.Enumeration (ContactListType)`, `contactMechTypeEnumId -> moqui.basic.Enumeration`, `marketingCampaignId -> mantle.marketing.campaign.MarketingCampaign`, `ownerPartyId -> mantle.party.Party`
- Enum/status fields and type references: `contactListTypeEnumId (ContactListType)`, `contactMechTypeEnumId (ContactMechType)`
- Key business fields: `contactListName`, `description`, `comments`, `isPublic`, `singleUse`, `verifyEmailFrom`, `verifyEmailScreen`, `verifyEmailSubject`, `verifyEmailWebSiteId`, `optOutScreen`

`ContactListEmail` (`runtime/component/mantle-udm/entity/MarketingEntities.xml`)
- PK: `contactListId`, `emailTypeEnumId`, `fromDate`
- FKs and targets: `contactListId -> mantle.marketing.contact.ContactList`, `emailTypeEnumId -> moqui.basic.Enumeration (ContactListEmailType)`, `emailTemplateId -> moqui.basic.email.EmailTemplate`, `wikiPageCategoryId -> moqui.resource.wiki.WikiPageCategory`
- Enum/status fields and type references: `emailTypeEnumId (ContactListEmailType)`
- Key business fields: `thruDate`
- Many-relationship note: `wikiBlogs -> moqui.resource.wiki.WikiBlogCategory` (relationship-only; not a local FK field)

`ContactListCommStatus` (`runtime/component/mantle-udm/entity/MarketingEntities.xml`)
- PK: `contactListId`, `communicationEventId`, `contactMechId`
- FKs and targets: `contactListId -> mantle.marketing.contact.ContactList`, `communicationEventId -> mantle.party.communication.CommunicationEvent`, `contactMechId -> mantle.party.contact.ContactMech`, `partyId -> mantle.party.Party`, `statusId -> moqui.basic.StatusItem`
- Enum/status fields and type references: `statusId (StatusItem)`
- Key business fields: `messageId`

`ContactListParty` (`runtime/component/mantle-udm/entity/MarketingEntities.xml`)
- PK: `contactListId`, `partyId`, `fromDate`
- FKs and targets: `contactListId -> mantle.marketing.contact.ContactList`, `partyId -> mantle.party.Party`, `statusId -> moqui.basic.StatusItem (ContactListParty)`, `preferredContactMechId -> mantle.party.contact.ContactMech`
- Enum/status fields and type references: `statusId (ContactListParty status type)`
- Key business fields: `thruDate`, `optInVerifyCode`

## 2A. Commented-Out Entity/View Definitions

Scanned source:
- `runtime/component/mantle-udm/entity/MarketingEntities.xml`

Commented-out `<entity ...>` / `<view-entity ...>` definitions:
- `NONE`

## 3. Reference-Only Set (FK Consumer Map)

Cross-layer and downstream consumers:
- `ContactList.marketingCampaignId -> MarketingCampaign` (campaign root from `001`)
- `CommunicationEvent.contactListId -> ContactList` (`runtime/component/mantle-udm/entity/PartyEntities.xml`, see `090_marketing-reference-intersections-dossier.md`)

## 4. Relationship Map (Domain-Internal Adjacency)

- `ContactList` is the layer root and may optionally attach to `MarketingCampaign`.
- `ContactListEmail` configures time-bounded email templates by list/email-type.
- `ContactListParty` stores list membership and opt-in/out lifecycle state.
- `ContactListCommStatus` records per-message delivery/status joins for list communications.

## 5. Service Wiring (Full Detail Set)

Service files touching this layer in `runtime/component/mantle-usl/service`:
- `NONE`

## 6. Screen Wiring (SimpleScreens only — Full Detail Set)

Stock `SimpleScreens` touching this layer:
- `NONE`

## 7. Canonical Flows

Contact-list setup:
1. Create `ContactList` with type and optional linked `marketingCampaignId`.
2. Add `ContactListEmail` rows to bind template behavior (`emailTypeEnumId`) over time windows.

List membership lifecycle:
1. Insert `ContactListParty` membership rows (`fromDate`).
2. Advance `statusId` through seeded statuses (`CLPT_PENDING`, `CLPT_ACCEPTED`, `CLPT_IN_USE`, `CLPT_UNSUBSCRIBED`, etc).
3. Optionally set `preferredContactMechId` and verify code fields.

Communication status ingestion:
1. Persist outbound/inbound message linkage in `ContactListCommStatus` using `communicationEventId` + `contactMechId`.
2. Update `statusId` and `messageId` for delivery/bounce tracking.

## 8. Agent Quick-Start

- Keep `ContactListParty` composite key shape exact (`contactListId`, `partyId`, `fromDate`).
- Model list membership state with `statusId` transitions, not ad-hoc booleans.
- Load `090` when changes affect `CommunicationEvent.contactListId` usage.

## 9. Open Questions / Follow-ups

- No stock service/screen coverage currently exists for contact-list entities; behavior appears data-model ready but not stock-UI wired.

# Pharmacy POS/ERP — Context Map & Event Catalog (New Product)

> Scope: Microservices + DDD for multi-pharmacy network; event-storming output focused on **Create New Product** lifecycle.

---

## 1) Context Map (first cut)

**Domains:**

* **Core**: Product Catalog, Regulatory & Compliance, Formulary & Clinical, Pricing & Reimbursement, Inventory Foundation
* **Supporting**: Supplier & Item Onboarding, Sales Channel Enablement, Finance
* **Generic**: Identity/Access, Audit & Lifecycle, Notification

**Relationships (Upstream → Downstream):**

* **Regulatory & Compliance** (Upstream, Published Language) → Product Catalog, Pricing, Sales Channels
* **Product Catalog** (Upstream) → Inventory, Pricing, Channels, Finance
* **Supplier & Item Onboarding** (Upstream) → Pricing, Inventory, Finance
* **Pricing & Reimbursement** (Upstream) → POS/E‑comm, Finance
* **Inventory Foundation** (Upstream) → POS/E‑comm, Finance
* **Audit & Lifecycle** (Observer) ← all

**Integration styles:** Event-driven (Kafka/AMQP), anti‑corruption layers between ERPs, Outbox pattern per service, schema registry.

```
[Regulatory & Compliance] --pl--> [Product Catalog] --pl--> [Inventory]
                                    |                        |
                                    v                        v
                               [Pricing & Reimb] ----pl--> [POS / E‑comm]
                                    |                        |
                                    v                        v
                                [Finance] <--------------- [Sales Channel]

[Supplier & Onboarding] --pl--> [Pricing] & [Inventory]

[Audit & Lifecycle] <--- subscribes to all events
```

Legend: `--pl-->` Published Language (Upstream provides vocabulary/contracts). Others: Conformist where noted (e.g., Channels conform to Pricing formats).

---

## 2) Bounded Context Responsibilities & Ownership

### Product Catalog

* **Owns**: product master, SKUs/packs, barcodes, attributes, activation status
* **Publishes**: ProductDraftCreated, SKUPackDefined, BarcodeAssigned, ProductActivated, ProductSuspended, ProductRetired
* **Consumes**: ComplianceApproved, SupplierItemCreated, PriceListEntriesCreated

### Regulatory & Compliance

* **Owns**: Rx/OTC classes, controlled schedules, serialization rules, country authorization
* **Publishes**: RegulatoryClassificationEvaluated, RegulatoryApprovalsVerified, SerializationRequirementsDetermined, ComplianceApproved/Rejected
* **Consumes**: ProductDraftCreated, SKUPackDefined

### Formulary & Clinical

* **Owns**: substance/strength, dose form, therapeutic class, substitutions, warnings
* **Publishes**: ActiveSubstanceConfirmed, DoseFormConfirmed, TherapeuticClassAssigned, SubstitutionRulesDefined, ClinicalWarningsAdded
* **Consumes**: ProductDraftCreated

### Supplier & Item Onboarding

* **Owns**: supplier product, case pack, lead time, MOQ, contracts
* **Publishes**: PreferredSupplierAssigned, SupplierItemCreated, PurchaseContractLinked, InitialLandedCostSet
* **Consumes**: ProductActivated (to validate sourcing readiness)

### Pricing & Reimbursement

* **Owns**: tax category, pricing policies, retail/base prices, payer codes/rules
* **Publishes**: TaxCategoryAssigned, RetailPricePolicyChosen, BaseRetailPriceSet, PriceListEntriesCreated, ReimbursementCodeMapped
* **Consumes**: InitialLandedCostSet, ProductActivated

### Inventory Foundation

* **Owns**: stock item per org/site, storage/expiry/lot policies, reorder settings
* **Publishes**: InventoryItemCreated, StorageConditionsSet, ExpiryLotPolicySet, ReorderPolicySet
* **Consumes**: ProductActivated, ComplianceApproved

### Sales Channel Enablement (POS/E‑comm)

* **Owns**: channel listings, availability rules, geofencing
* **Publishes**: POSListingPublished, EcommerceListingPublished, AvailabilityRulesSet
* **Consumes**: ProductActivated, PriceListEntriesCreated, InventoryItemCreated, ComplianceApproved

### Finance

* **Owns**: GL mapping, COGS defaults, revenue accounts
* **Publishes**: COGSAccountMapped, RevenueAccountMapped, PriceChangeNotified
* **Consumes**: BaseRetailPriceSet, InitialLandedCostSet, ProductActivated

### Audit & Lifecycle

* **Owns**: approvals, versioning, suspensions/retirements, audit log
* **Publishes**: ProductOnboardingSubmittedForApproval, ProductVersionApproved/Rejected
* **Consumes**: All domain events for traceability

---

## 3) Ubiquitous Language (selected)

* **Product vs Trade Item**: Product is generic definition; Trade Item is market/brand presentation.
* **SKU/Pack**: sellable unit with pack size and barcodes.
* **Activation**: product eligible to appear in downstream contexts; not equal to channel listing.
* **Compliance Approved**: legal/regulatory green light for a geography.

---

## 4) Command → Policy → Event (C→P→E) Mappings (New Product)

**Happy Path**

1. **C:** CreateProductDraft → **E:** ProductDraftCreated
2. **C:** DefineClinicalProfile → **P:** ValidateClinicalCompleteness → **E:** ActiveSubstanceConfirmed, DoseFormConfirmed, TherapeuticClassAssigned
3. **C:** EvaluateRegulatoryProfile → **P:** CheckCountryAuthorization, DetermineSerialization → **E:** RegulatoryApprovalsVerified, RegulatoryClassificationEvaluated, SerializationRequirementsDetermined
4. **C:** DefinePackAndBarcodes → **P:** ValidatePackUoM, CheckBarcodeUniqueness → **E:** SKUPackDefined, UnitsOfMeasureSet, BarcodeAssigned
5. **C:** EnrichProductAttributes → **E:** ProductAttributesEnriched
6. **C:** LinkSupplierItem → **P:** VerifyContractCoverage → **E:** PreferredSupplierAssigned, SupplierItemCreated, PurchaseContractLinked, InitialLandedCostSet
7. **C:** SetPricingAndReimbursement → **P:** ApplyPricingPolicy, MapReimbursement → **E:** TaxCategoryAssigned, RetailPricePolicyChosen, BaseRetailPriceSet, PriceListEntriesCreated, ReimbursementCodeMapped
8. **C:** InitializeInventoryPolicies → **P:** ApplyStorageAndExpiryRules → **E:** InventoryItemCreated, StorageConditionsSet, ExpiryLotPolicySet, ReorderPolicySet
9. **C:** SubmitForOnboardingApproval → **P:** Multi‑RoleApprovalRule → **E:** ProductOnboardingSubmittedForApproval
10. **C:** ApproveProductVersion → **P:** GateOnComplianceAndPricing → **E:** ProductVersionApproved, ProductActivated
11. **C:** PublishToChannels → **P:** ChannelEligibilityRules → **E:** POSListingPublished / EcommerceListingPublished

**Key Guards & Reactions**

* **Policy:** On ComplianceApproved **and** ProductVersionApproved → command ActivateProduct.
* **Policy:** On ProductActivated → commands: CreateInventoryItem (if not present), GeneratePriceLists, PublishChannelListings.
* **Policy:** If SerializationRequirementsDetermined = true → block ApproveProductVersion until BarcodeAssigned with serializable GTIN.

---

## 5) Event Catalog (selected)

Each event immutable; include identifiers, actor, occurredAt, version.

### ProductDraftCreated

* **When**: On CreateProductDraft
* **Why**: Seed product lifecycle
* **Attributes**: productId, country, organizationId, createdBy, source, notes

### ActiveSubstanceConfirmed

* **When**: After clinical profile validation
* **Attributes**: productId, substance, strength, doseForm

### RegulatoryApprovalsVerified

* **When**: Authorization verified
* **Attributes**: productId, country, authNumber, validFrom/To

### SKUPackDefined

* **When**: Pack and UoM set
* **Attributes**: productId, skuId, packSize, uomSell, uomDispense

### BarcodeAssigned

* **When**: Barcode attached
* **Attributes**: productId, skuId, barcodeType, value

### SupplierItemCreated

* **When**: Supplier item linked
* **Attributes**: productId, supplierId, supplierSku, casePack, leadTimeDays, moq

### BaseRetailPriceSet

* **When**: Price derived
* **Attributes**: productId, skuId, currency, amount, policyId, effectiveFrom

### PriceListEntriesCreated

* **When**: Channel/store pricing written
* **Attributes**: productId, skuId, priceListId, entries\[]

### InventoryItemCreated

* **When**: Stock-keeping entity initialized
* **Attributes**: productId, orgId, siteId, fefoEnabled

### ProductVersionApproved

* **When**: Final approval issued
* **Attributes**: productId, approver, approvalWorkflowId

### ProductActivated

* **When**: Product ready for downstream use
* **Attributes**: productId, activatedBy, activationReason

### POSListingPublished / EcommerceListingPublished

* **When**: Channels enabled
* **Attributes**: productId, channelId, availabilityRules

---

## 6) Message Contracts (JSON sketches)

```json
{
  "eventType": "ProductActivated",
  "version": 1,
  "occurredAt": "2025-09-19T10:12:03Z",
  "actor": {"service": "lifecycle-service"},
  "ids": {"productId": "PRD-12345", "country": "DE"},
  "data": {"activationReason": "OnboardingApproved"}
}
```

```json
{
  "eventType": "SKUPackDefined",
  "version": 2,
  "occurredAt": "2025-09-19T10:15:44Z",
  "actor": {"service": "catalog-service"},
  "ids": {"productId": "PRD-12345", "skuId": "SKU-12345-20TAB"},
  "data": {"packSize": 20, "uomSell": "pack", "uomDispense": "tablet"}
}
```

---

## 7) Topics & Subscriptions (suggested)

* **topic.product**: ProductDraftCreated, ProductActivated, ProductSuspended, ProductRetired
* **topic.compliance**: RegulatoryApprovalsVerified, ComplianceApproved, ComplianceRejected
* **topic.catalog**: SKUPackDefined, BarcodeAssigned, UnitsOfMeasureSet
* **topic.pricing**: BaseRetailPriceSet, PriceListEntriesCreated, ReimbursementCodeMapped
* **topic.inventory**: InventoryItemCreated, StorageConditionsSet, ExpiryLotPolicySet
* **topic.channels**: POSListingPublished, EcommerceListingPublished
* **topic.audit**: All events mirrored for ledgering

---

## 8) Versioning & Evolution

* Use semantic event versions; never change meaning of existing fields.
* Additive changes only; use `version` and soft-required fields gated by feature flags.
* Deprecate topics with sunsetting timeline; dual-publish during migration.

---

## 9) Read Models (by consumer)

* **POS Product View**: join ProductActivated + PriceListEntriesCreated + InventoryItemCreated + ComplianceApproved → denormalized sellable card
* **Regulatory Dashboard**: list of products awaiting approvals by country
* **Onboarding SLA**: time between ProductDraftCreated → ProductActivated

---

## 10) Non-Functional Notes

* Idempotency keys = eventId; exactly-once via outbox + inbox tables
* PII minimal; clinical warnings are non-identifying text snippets
* Multi-tenancy: partition by organizationId + country
* Observability: trace `productId` across spans

---

## 11) Open Questions / Policy Decisions

* Do channels allow listing before price finalization? (likely **no**)
* Are serialized items required to be receivable before activation? (usually **no**, but barcode must be present)
* One global Product with localized overlays vs per-country product? (recommend: global product + regional overlays)
* Who is the Policy Owner for substitution rules—Clinical or Catalog? (recommend: Clinical)

---

## 12) Next Steps

* Validate the happy-path with pharmacy ops & compliance
* Fill event attributes with exact enumerations (Rx schedule, tax categories)
* Stand up schema registry; register v1 events above
* Implement outbox/inbox + policy handlers per mapping in §4

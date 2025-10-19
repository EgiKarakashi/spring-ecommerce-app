# Azure Full Stack Deployment Context — E‑Commerce Monorepo

> **File:** `context/azure-ecommerce-plan.md`  
> **Purpose:** Single-source context for infrastructure & DevOps of the `spring-ecommerce-app` monorepo on **Microsoft Azure**.  
> **Audience:** You (learning), future contributors, and Codex-like assistants.  
> **Scope:** Learning stack (free-tier + $200 credit) **and** enterprise upgrade path.  
> **Envs:** `dev`, `test`, `prod` in one subscription (for learning).

---

## Table of Contents
- [1. Overview](#1-overview)
- [2. Repo Layout (conventions)](#2-repo-layout-conventions)
- [3. Environments & Cost Strategy](#3-environments--cost-strategy)
- [4. Terraform — Modules, Variables, and Apply Flow](#4-terraform--modules-variables-and-apply-flow)
  - [4.1 Module Map](#41-module-map)
  - [4.2 Per-environment Structure](#42-per-environment-structure)
  - [4.3 Example `dev.tfvars`](#43-example-devtfvars)
  - [4.4 Core Module Examples](#44-core-module-examples)
- [5. Kubernetes Platform via Argo CD (GitOps)](#5-kubernetes-platform-via-argo-cd-gitops)
  - [5.1 Bootstrap Argo CD](#51-bootstrap-argo-cd)
  - [5.2 App-of-Apps Layout](#52-app-of-apps-layout)
  - [5.3 Platform Apps (Ingress/Certs/Secrets/KEDA/Velero/Policies)](#53-platform-apps-ingresscertssecretskedaveleropolicies)
- [6. API Gateway & Load Balancer](#6-api-gateway--load-balancer)
- [7. Applications — Helm Conventions](#7-applications--helm-conventions)
  - [7.1 Backend Values Template](#71-backend-values-template)
  - [7.2 Frontend (SPA) Options](#72-frontend-spa-options)
- [8. Elasticsearch (Learning via ECK)](#8-elasticsearch-learning-via-eck)
- [9. CI/CD — GitHub Actions](#9-cicd--github-actions)
  - [9.1 Backend Workflow (Kotlin/Gradle)](#91-backend-workflow-kotlingradle)
  - [9.2 Frontend Workflow (Node/PNPM)](#92-frontend-workflow-nodepnpm)
  - [9.3 Promotion Workflow (test → prod)](#93-promotion-workflow-test--prod)
- [10. Ingress & DNS Scheme](#10-ingress--dns-scheme)
- [11. Stop/Resume & Teardown Playbook](#11-stopresume--teardown-playbook)
- [12. Cost Profile (Learning Stack)](#12-cost-profile-learning-stack)
- [13. Enterprise Upgrade Path](#13-enterprise-upgrade-path)
  - [13.1 Multi-Subscription & Hub/Spoke](#131-multi-subscription--hubspoke)
  - [13.2 AGIC + Front Door Premium + Private Link](#132-agic--front-door-premium--private-link)
  - [13.3 API Management](#133-api-management)
  - [13.4 Security, Observability, DR](#134-security-observability-dr)
  - [13.5 Upgrade Sequence](#135-upgrade-sequence)
- [14. Naming, Tags, and Conventions](#14-naming-tags-and-conventions)
- [15. Changelog](#15-changelog)

---

## 1. Overview
- **Goal:** Learn and deploy a production-shaped stack using **free/always-free tiers** where possible, and the **$200 Azure credit** for the rest. Easily **pause/stop** or **destroy/recreate**.
- **Core Stack:** Terraform → AKS (Kubernetes) → Argo CD (GitOps) → NGINX Ingress (learning) / AGIC (enterprise) → Azure Front Door → Key Vault + External Secrets → ACR → Postgres → (optional) Redis, Event Hubs → Elasticsearch via ECK (learning).
- **Out-of-scope (learning):** Multi-zone HA, Private Link everywhere, full SOC2/ISO controls — reserved for Enterprise path.

---

## 2. Repo Layout (conventions)
```
repo-root/
  apps/
    backend-*/               # each microservice (Spring Boot/Kotlin)
      Dockerfile
      helm/
        Chart.yaml
        templates/
        values.yaml
        values-dev.yaml
        values-test.yaml
        values-prod.yaml
    frontend-*/              # SPA(s) or SSR (Next.js)
      Dockerfile
      helm/ (if containerized)
  platform/
    argocd/
      bootstrap/application.yaml
      apps/
        platform/            # ingress, cert-manager, ESO, keda, velero, policies, monitoring
        workloads/           # Argo CD Applications per service per env
  infra/
    terraform/
      modules/
        aks/ acr/ keyvault/ vnet/ frontdoor/ postgres/ redis/ log_analytics/ event_hubs/
      envs/
        dev/   (main.tf, providers.tf, backend.tf, variables.tf, dev.tfvars)
        test/  (…)
        prod/  (…)
  .github/workflows/
    ci-<service>.yml
    ci-frontend.yml
    promote.yml
  context/
    azure-ecommerce-plan.md   # THIS FILE
```

---

## 3. Environments & Cost Strategy
- **Subscription:** Single subscription for learning.
- **Resource Groups:** `rg-ecom-dev`, `rg-ecom-test`, `rg-ecom-prod`.
- **Dev/Test:** Shared AKS cluster (namespaces `dev`, `test`), tiny nodes, low caps.
- **Prod:** Separate AKS (may be created later).
- **Cost knobs:** B-series nodes, autoscale min=0, Log Analytics 1 GB/day cap, Storage Static Website for SPA, Front Door Standard (optional in dev/test).

---

## 4. Terraform — Modules, Variables, and Apply Flow

### 4.1 Module Map
- `vnet`: VNets/Subnets (+ optional Private DNS later)
- `aks`: AKS cluster, node pools, OIDC, workload identity
- `acr`: Container registry
- `keyvault`: Secrets and certs
- `postgres`: Postgres Flexible Server (per env)
- `redis`: Redis Cache (optional)
- `event_hubs`: Event Hubs (Kafka endpoint) (optional)
- `frontdoor`: Front Door Standard (learning) / Premium (enterprise)
- `log_analytics`: Log Analytics workspace + caps

### 4.2 Per-environment Structure
Each env folder has its own `backend.tf` (remote state) and `*.tfvars`:
```
infra/terraform/envs/<env>/
  main.tf
  providers.tf
  backend.tf    # remote state for this env
  variables.tf
  <env>.tfvars
```

### 4.3 Example `dev.tfvars`
```hcl
location                    = "westeurope"
env                         = "dev"

# AKS
aks_name                    = "aks-ecom-dev"
aks_system_vm_size          = "Standard_B2s"
aks_apps_vm_size            = "Standard_B2s"
aks_apps_min                = 0
aks_apps_max                = 2
aks_spot_for_apps           = true
aks_enable_private_cluster  = false   # learning: false; enterprise: true

# ACR / KV
acr_name                    = "acrecomshared"
kv_name                     = "kv-ecom-dev"

# Networking (simple for learning)
vnet_address_space          = ["10.60.0.0/16"]
subnets = {
  aks_system = "10.60.0.0/20"
  aks_user   = "10.60.16.0/20"
}

# Data
postgres_name               = "pg-ecom-dev"
postgres_sku                = "B_Standard_B1ms"
postgres_storage_gb         = 32

redis_enabled               = false
event_hubs_enabled          = false

# Edge
enable_frontdoor            = true
frontdoor_sku               = "Standard_AzureFrontDoor"

# Observability
log_analytics_daily_cap_gb  = 1
log_retention_days          = 14
```

### 4.4 Core Module Examples

**AKS (snippet inside `modules/aks/main.tf`)**
```hcl
resource "azurerm_kubernetes_cluster" "this" {
  name                = var.name
  location            = var.location
  resource_group_name = var.resource_group_name
  dns_prefix          = "${var.name}-dns"

  oidc_issuer_enabled       = true
  workload_identity_enabled = true

  default_node_pool {
    name                = "system"
    vm_size             = var.system_vm_size
    node_count          = 1
    orchestrator_version= var.k8s_version
    mode                = "System"
    upgrade_settings { max_surge = "33%" }
  }

  identity { type = "SystemAssigned" }
  network_profile { network_plugin = "azure" load_balancer_sku = "standard" }

  # Optional: private cluster in enterprise
  private_cluster_enabled = var.private_cluster
}

resource "azurerm_kubernetes_cluster_node_pool" "apps" {
  name                  = "apps"
  kubernetes_cluster_id = azurerm_kubernetes_cluster.this.id
  vm_size               = var.apps_vm_size
  enable_auto_scaling   = true
  min_count             = var.apps_min
  max_count             = var.apps_max
  orchestrator_version  = var.k8s_version
  mode                  = "User"
  priority              = var.apps_spot ? "Spot" : "Regular"
}
```

**Front Door Standard (learning)**
```hcl
# Simplified: one endpoint, two routes: SPA + API
resource "azurerm_cdn_frontdoor_profile" "std" {
  name                = "fd-ecom-${var.env}"
  resource_group_name = var.resource_group_name
  sku_name            = var.frontdoor_sku # "Standard_AzureFrontDoor"
}

# Define endpoints, origins (Storage static site + AKS Ingress public IP) and routes...
```

**PostgreSQL Flexible Server**
```hcl
resource "azurerm_postgresql_flexible_server" "this" {
  name                   = var.name
  resource_group_name    = var.resource_group_name
  location               = var.location
  administrator_login    = "pgadmin"
  administrator_password = var.admin_password
  version                = "16"
  sku_name               = var.sku    # "B_Standard_B1ms"
  storage_mb             = var.storage_gb * 1024
}
```

---

## 5. Kubernetes Platform via Argo CD (GitOps)

### 5.1 Bootstrap Argo CD
```bash
kubectl create ns argocd
helm repo add argo https://argoproj.github.io/argo-helm
helm upgrade --install argocd argo/argo-cd -n argocd \
  --set configs.params."server\.insecure"=true
kubectl apply -f platform/argocd/bootstrap/application.yaml
```

### 5.2 App-of-Apps Layout
`platform/argocd/bootstrap/application.yaml`
```yaml
apiVersion: argoproj.io/v1alpha1
kind: Application
metadata:
  name: platform-root
  namespace: argocd
spec:
  project: default
  source:
    repoURL: https://github.com/<you>/<repo>.git
    targetRevision: main
    path: platform/argocd/apps
    directory: { recurse: true }
  destination:
    server: https://kubernetes.default.svc
    namespace: argocd
  syncPolicy:
    automated: { prune: true, selfHeal: true }
    syncOptions:
      - CreateNamespace=true
```

### 5.3 Platform Apps (Ingress/Certs/Secrets/KEDA/Velero/Policies)

**cert-manager ClusterIssuer (HTTP‑01 example)**
```yaml
apiVersion: cert-manager.io/v1
kind: ClusterIssuer
metadata:
  name: letsencrypt-http01
spec:
  acme:
    server: https://acme-v02.api.letsencrypt.org/directory
    email: you@example.com
    privateKeySecretRef: { name: acme-account-key }
    solvers:
      - http01:
          ingress:
            class: nginx
```

**External Secrets — ClusterSecretStore (Key Vault)**
```yaml
apiVersion: external-secrets.io/v1beta1
kind: ClusterSecretStore
metadata:
  name: azure-kv
spec:
  provider:
    azurekv:
      authType: WorkloadIdentity
      vaultUrl: https://kv-ecom-dev.vault.azure.net/
```

**KEDA ScaledObject (sample for Kafka/Event Hubs)**  
```yaml
apiVersion: keda.sh/v1alpha1
kind: ScaledObject
metadata:
  name: orders-consumer
  namespace: backend
spec:
  scaleTargetRef:
    name: orders
  triggers:
    - type: kafka  # For Event Hubs use kafka-compatible settings
      metadata:
        bootstrapServers: "<eventhubs-namespace>.servicebus.windows.net:9093"
        consumerGroup: orders-dev
        topic: orders
        lagThreshold: "50"
```

**Velero (backup) — notes**
- Write backup storage location to Azure Blob (dedicated account).  
- Nightly schedule for `dev/test` namespaces.  
- Restore drills at least once a quarter.

**Gatekeeper (policy) — minimal example**
```yaml
apiVersion: constraints.gatekeeper.sh/v1beta1
kind: K8sRequiredLabels
metadata:
  name: ns-must-have-owner
spec:
  match: { kinds: [{ apiGroups: [""], kinds: ["Namespace"] }] }
  parameters:
    labels: ["owner","env"]
```

---

## 6. API Gateway & Load Balancer
- **Learning:** **NGINX Ingress** as API Gateway + L7 LB. Public IP via Azure LB.  
- **Edge:** **Front Door Standard** routes:
  - `/` → SPA (Azure Storage Static Website + CDN)
  - `/api/*` → Ingress public IP / hostname
- **Enterprise:** swap to **AGIC** and **Front Door Premium + Private Link**.

---

## 7. Applications — Helm Conventions

### 7.1 Backend Values Template
`apps/<svc>/helm/values-dev.yaml`
```yaml
image:
  repository: <acr>.azurecr.io/<svc>
  tag: "sha-<gitsha>"

service:
  type: ClusterIP
  port: 8080

ingress:
  enabled: true
  className: nginx
  hosts:
    - host: api.dev.example.com
      paths:
        - path: /<svc>
          pathType: Prefix
  tls:
    - hosts: [api.dev.example.com]
      secretName: tls-api-dev

resources:
  requests: { cpu: 200m, memory: 256Mi }
  limits:   { cpu: 500m, memory: 512Mi }

autoscaling:
  enabled: true
  minReplicas: 1
  maxReplicas: 5
  targetCPUUtilizationPercentage: 70

envFromSecret: backend-env

# External Secrets to source configuration
externalSecret:
  enabled: true
  name: backend-env
  data:
    - secretKey: DB_URL
      remoteRef: { key: dev-postgres-url }
    - secretKey: REDIS_URL
      remoteRef: { key: dev-redis-url }
```

### 7.2 Frontend (SPA) Options
- **Cheapest:** build static → **Azure Storage Static Website** + **CDN**; Front Door routes `/` here.  
- **If containerized:** Helm chart similar to backend; ingress host `app.dev.example.com`.

---

## 8. Elasticsearch (Learning via ECK)
Install ECK operator and deploy **single-node** ES + Kibana in `elasticsearch` namespace.  
- Access inside cluster: `http://es-dev-es-http.elasticsearch.svc:9200`
- Store credentials in **Key Vault** (bootstrap Job reads secret → writes KV) → ESO maps back to app `Secret`.

ECK Elasticsearch CR (single-node):
```yaml
apiVersion: elasticsearch.k8s.elastic.co/v1
kind: Elasticsearch
metadata:
  name: es-dev
  namespace: elasticsearch
spec:
  version: 8.12.0
  nodeSets:
    - name: default
      count: 1
      config:
        node.roles: ["master","data","ingest"]
        xpack.security.enabled: true
      podTemplate:
        spec:
          containers:
            - name: elasticsearch
              resources:
                requests: { cpu: "500m", memory: "1Gi" }
                limits:   { cpu: "1",    memory: "2Gi" }
      volumeClaimTemplates:
        - metadata: { name: elasticsearch-data }
          spec:
            accessModes: ["ReadWriteOnce"]
            resources: { requests: { storage: 32Gi } }
```

Kibana CR:
```yaml
apiVersion: kibana.k8s.elastic.co/v1
kind: Kibana
metadata:
  name: kb-dev
  namespace: elasticsearch
spec:
  version: 8.12.0
  count: 1
  elasticsearchRef: { name: es-dev }
```

---

## 9. CI/CD — GitHub Actions

### 9.1 Backend Workflow (Kotlin/Gradle)
`.github/workflows/ci-<svc>.yml`
```yaml
name: ci-<svc>
on:
  push:
    paths: ["apps/<svc>/**"]
jobs:
  build-scan-push:
    runs-on: ubuntu-latest
    permissions:
      contents: write
      id-token: write
    steps:
      - uses: actions/checkout@v4

      - name: Set up JDK 21
        uses: actions/setup-java@v4
        with: { distribution: temurin, java-version: "21" }

      - name: Build (Gradle)
        working-directory: apps/<svc>
        run: ./gradlew clean build -x test

      - name: Run tests
        working-directory: apps/<svc>
        run: ./gradlew test

      - name: Azure login (OIDC)
        uses: azure/login@v2
        with:
          client-id: ${{ secrets.AZURE_CLIENT_ID }}
          tenant-id: ${{ secrets.AZURE_TENANT_ID }}
          subscription-id: ${{ secrets.AZURE_SUBSCRIPTION_ID }}

      - name: ACR login
        run: az acr login --name ${{ secrets.ACR_NAME }}

      - name: Build & push image
        uses: docker/build-push-action@v6
        with:
          context: apps/<svc>
          push: true
          tags: ${{ secrets.ACR_NAME }}.azurecr.io/<svc>:sha-${{ github.sha }}

      - name: Scan image (Trivy)
        uses: aquasecurity/trivy-action@0.24.0
        with:
          image-ref: ${{ secrets.ACR_NAME }}.azurecr.io/<svc>:sha-${{ github.sha }}
          format: 'table'
          exit-code: '0'

      - name: Bump Helm values (dev)
        run: |
          pipx install yq || pip install yq
          yq -i '.image.tag = "sha-${{ github.sha }}"' apps/<svc>/helm/values-dev.yaml
          git config user.name "ci-bot"
          git config user.email "ci@users.noreply.github.com"
          git commit -am "<svc>(dev): deploy sha ${{ github.sha }}"
          git push
```

- TODO: Re-enable SonarCloud analysis once the token is provisioned and secrets are populated in GitHub.

### 9.2 Frontend Workflow (Node/PNPM)
`.github/workflows/ci-frontend.yml`
```yaml
name: ci-frontend
on:
  push:
    paths: ["apps/frontend-*/**"]
jobs:
  build-deploy-storage:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4

      - uses: actions/setup-node@v4
        with: { node-version: 20 }
      - name: Install PNPM
        run: corepack enable && corepack prepare pnpm@9.0.0 --activate

      - name: Build
        working-directory: apps/frontend-storefront
        run: pnpm install && pnpm build

      - name: Azure login
        uses: azure/login@v2
        with:
          client-id: ${{ secrets.AZURE_CLIENT_ID }}
          tenant-id: ${{ secrets.AZURE_TENANT_ID }}
          subscription-id: ${{ secrets.AZURE_SUBSCRIPTION_ID }}

      - name: Upload to Storage Static Website
        run: |
          az storage blob upload-batch \
            --account-name ${{ secrets.STORAGE_ACCOUNT }} \
            --auth-mode login \
            -s apps/frontend-storefront/.next/static \
            -d '$web' --overwrite
```

### 9.3 Promotion Workflow (test → prod)
`.github/workflows/promote.yml`
```yaml
name: promote
on:
  workflow_dispatch:
    inputs:
      service: { description: 'service name', required: true }
      tag:     { description: 'image tag (sha-xxxx)', required: true }
jobs:
  promote:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - name: Promote to test
        run: |
          yq -i '.image.tag = "${{ inputs.tag }}"' apps/${{ inputs.service }}/helm/values-test.yaml
          git config user.name "release-bot"
          git config user.email "release@users.noreply.github.com"
          git commit -am "promote(${{ inputs.service }}): test -> ${{ inputs.tag }}"
          git push
      - name: Promote to prod (requires approval)
        if: false  # flip true when ready, or use protected branches
        run: |
          yq -i '.image.tag = "${{ inputs.tag }}"' apps/${{ inputs.service }}/helm/values-prod.yaml
          git commit -am "promote(${{ inputs.service }}): prod -> ${{ inputs.tag }}"
          git push
```

---

## 10. Ingress & DNS Scheme
| Env | Hostnames | Route |
|-----|-----------|-------|
| dev | `app.dev.example.com` | Front Door → Storage Static Website |
| dev | `api.dev.example.com` | Front Door → NGINX Ingress public IP |
| test | `app.test.example.com` | same pattern |
| prod | `app.example.com` | Front Door (later Premium) |
| prod | `api.example.com` | Front Door → Ingress/AGIC |

TLS: cert-manager (Let’s Encrypt) for dev/test; Key Vault or cert-manager DNS‑01 for prod.

---

## 11. Stop/Resume & Teardown Playbook
- **Pause AKS nodes:** `az aks stop -g <rg> -n <aks-name>` (deallocates VMs).  
- **Scale to zero (apps pool):** set `min=0` (autoscaler).  
- **Elasticsearch/Kibana:** set `spec.nodeSets[].count=0`, `spec.count=0`.  
- **Reduce logs:** lower Log Analytics daily cap, shorten retention.  
- **Destroy dev/test:** `terraform destroy` in `infra/terraform/envs/dev|test`.  
- **Backups:** ensure Velero / DB backups (PITR) before destroying.

---

## 12. Cost Profile (Learning Stack)
| Service | SKU | Est. Monthly |
|---------|-----|--------------|
| AKS nodes | 1–2× B2s + tiny system | $35–$80 |
| ACR Basic | — | ~$5 |
| Key Vault Std | — | $1–$5 |
| Postgres Flex | B1ms + 32GB | $15–$30 |
| Redis (opt) | Basic C0 | $15–$20 |
| Event Hubs (opt) | Basic 1 TU | $15–$25 |
| Log Analytics | 1 GB/day | $5–$15 |
| Front Door Standard | light use | $30–$50 |
| Storage Static Web | — | $1–$3 |
**Total (typical dev/test):** ~$120–$180 within $200 credits (traffic-dependent).

---

## 13. Enterprise Upgrade Path

### 13.1 Multi-Subscription & Hub/Spoke
- Subscriptions: `dev`, `test`, `prod`.
- Hub VNet with Firewall/Bastion; Spokes per env (AKS, data).  
- Private DNS zones and Private Endpoints for ACR, Postgres, Key Vault.

### 13.2 AGIC + Front Door Premium + Private Link
- Swap NGINX → **Application Gateway Ingress Controller (AGIC)**.  
- **Front Door Premium** with Private Link origins → AGIC (private).  
- WAF at Front Door; AppGW WAF optional.

### 13.3 API Management
- Add **APIM** (Std/Premium) for API keys, rate limiting, versioning, and developer portal.  
- Place APIM in front of Front Door or behind (both patterns are used).

### 13.4 Security, Observability, DR
- **Defender for Cloud**; Azure Policy enforcement; Pod Security baseline/restricted.  
- **Managed Prometheus + Grafana**; Azure Monitor & App Insights for traces.  
- **Velero** backups to Blob; DB Geo‑replica + PITR; DR runbooks & quarterly drills.

### 13.5 Upgrade Sequence
1. Create dedicated **prod** AKS (private, zonal).  
2. Introduce **Front Door Premium** + Private Link origins.  
3. Swap to **AGIC** for prod ingress.  
4. Add **APIM** (policies/keys).  
5. Migrate **Elasticsearch** → Elastic Cloud (managed).  
6. Enable **Defender** and **Azure Policy** enforcement.  
7. Add **Managed Prometheus/Grafana**.  
8. Validate DR (Velero restore + DB failover).

---

## 14. Naming, Tags, and Conventions
- **Names:** `aks-ecom-<env>`, `pg-ecom-<env>`, `kv-ecom-<env>`, `rg-ecom-<env>`, `fd-ecom-<env>`  
- **Tags:** `env`, `owner`, `app=ecommerce`, `costCenter`, `managedBy=terraform`  
- **Branches:** trunk + PRs; SemVer tags trigger promotions.  
- **Images:** `<acr>.azurecr.io/<service>:sha-<gitsha>`

---

## 15. Changelog
- v1.0 — Initial learning stack + enterprise path; includes Terraform/AKS/ArgoCD/FrontDoor/ECK/CI-CD.

variable "location" {
  description = "Azure region for all resources."
  type        = string
}

variable "environment" {
  description = "Short environment name (dev/test/prod)."
  type        = string
}

variable "resource_group_name" {
  description = "Primary resource group for this environment."
  type        = string
}

variable "name_prefix" {
  description = "Prefix applied to resource names to keep them unique."
  type        = string
}

variable "aks_dns_prefix" {
  description = "AKS public DNS prefix."
  type        = string
}

variable "aks_kubernetes_version" {
  description = "Desired Kubernetes version for AKS."
  type        = string
}

variable "aks_nodepools" {
  description = "Node pool definitions keyed by pool name."
  type = map(object({
    vm_size : string
    node_count : number
    min_count : number
    max_count : number
    mode : string
  }))
}

variable "acr_name" {
  description = "Unique Azure Container Registry name."
  type        = string
}

variable "acr_sku" {
  description = "ACR SKU (Basic/Standard/Premium)."
  type        = string
}

variable "key_vault_name" {
  description = "Azure Key Vault name."
  type        = string
}

variable "postgres_server_name" {
  description = "Postgres Flexible Server name."
  type        = string
}

variable "postgres_version" {
  description = "Postgres major version."
  type        = string
}

variable "postgres_sku_name" {
  description = "Postgres compute SKU."
  type        = string
}

variable "postgres_storage_mb" {
  description = "Allocated storage (MB)."
  type        = number
}

variable "postgres_admin_username" {
  description = "Admin username for Postgres."
  type        = string
}

variable "log_analytics_workspace_name" {
  description = "Log Analytics workspace name."
  type        = string
}

variable "tags" {
  description = "Resource tags."
  type        = map(string)
}
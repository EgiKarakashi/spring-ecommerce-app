location            = "centralus"
environment         = "dev"
resource_group_name = "rg-ecom-centralus-dev"
name_prefix         = "ecomdev"

aks_dns_prefix         = "ecom-dev-aks"
aks_kubernetes_version = "1.32.7"
aks_nodepools = {
  system = {
    vm_size    = "Standard_B4ms"
    node_count = 1
    min_count  = 1
    max_count  = 2
    mode       = "System"
  }
}

acr_name = "acrecomdev87463"
acr_sku  = "Basic"

key_vault_name          = "kv-ecom-dev-87463a"
postgres_server_name    = "pg-ecom-dev"
postgres_version        = "15"
postgres_sku_name       = "B_Standard_B1ms"
postgres_storage_mb     = 32768
postgres_admin_username = "pgadmin"

log_analytics_workspace_name = "law-ecom-dev"

tags = {
  env       = "dev"
  owner     = "egi"
  app       = "ecommerce"
  managedBy = "terraform"
}

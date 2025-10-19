terraform {
  backend "azurerm" {
    resource_group_name  = "rg-ecom-tfstate"
    storage_account_name = "ecomtfstate87463"
    container_name       = "tfstate"
    key                  = "dev.terraform.tfstate"
  }
}
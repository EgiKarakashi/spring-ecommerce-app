import apiClientService from "@/common/services/ApiClientService";
import {ProductOption} from "@/modules/catalog/models/ProductOption";

const baseUrl = '/api/product/backoffice/product-options';

export async function getProductOptions(): Promise<ProductOption[]> {
  return (await apiClientService.get(baseUrl)).json();
}

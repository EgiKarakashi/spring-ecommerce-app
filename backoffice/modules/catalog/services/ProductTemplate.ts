import {ProductTemplate} from "@/modules/catalog/models/ProductTemplate";
import apiClientService from "@/common/services/ApiClientService";

const baseUrl = '/api/product/backoffice/product-template';

export async function getProductTemplates(): Promise<ProductTemplate[]> {
  return (await apiClientService.get(baseUrl)).json();
}

export async function getProductTemplate(id: number) {
  const url = `${baseUrl}/${id}`;
  return (await apiClientService.get(url)).json();
}

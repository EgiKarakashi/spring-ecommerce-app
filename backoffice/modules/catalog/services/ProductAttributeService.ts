import {ProductAttribute} from "@/modules/catalog/models/ProductAttribute";
import apiClientService from "@/common/services/ApiClientService";

const baseUrl = '/api/product/backoffice/product-attribute';

interface ProductAttributeId {
  name: string;
  productAttributeGroupId: string;
}
export async function getProductAttributes(): Promise<ProductAttribute[]> {
  return (await apiClientService.get(baseUrl)).json();
}

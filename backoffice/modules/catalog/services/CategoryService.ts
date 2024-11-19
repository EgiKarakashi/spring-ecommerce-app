import {Category} from "@/modules/catalog/models/Category";
import apiClientService from "@/common/services/ApiClientService";

const baseUrl = '/api/product/backoffice/categories';

export async function getCategories(): Promise<Category[]> {
  return (await apiClientService.get(baseUrl)).json();
}

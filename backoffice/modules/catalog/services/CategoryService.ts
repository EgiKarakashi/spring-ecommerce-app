import {Category} from "@/modules/catalog/models/Category";
import apiClientService from "@/common/services/ApiClientService";
import {ProductThumbnails} from "@/modules/catalog/models/ProductThumbnails";

const baseUrl = '/api/product/backoffice/categories';

export async function getCategories(): Promise<Category[]> {
  return (await apiClientService.get(baseUrl)).json();
}

export async function getCategory(id: number): Promise<Category> {
  return (await apiClientService.get(`${baseUrl}/${id}`)).json();
}

export async function getProductsByCategory(
  pageNo: number,
  categorySlug: string
): Promise<ProductThumbnails> {
  const url = `/api/product/storefront/category/${categorySlug}/products?pageNo=${pageNo}`;
  return (await apiClientService.get(url)).json();
}

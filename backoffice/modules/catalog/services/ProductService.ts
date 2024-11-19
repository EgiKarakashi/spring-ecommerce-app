import {Product} from "@/modules/catalog/models/Product";
import apiClientService from "@/common/services/ApiClientService";
import {Products} from "@/modules/catalog/models/Products";
import {ProductPayload} from "@/modules/catalog/models/ProductPayload";
import {Variation} from "@/modules/catalog/models/ProductVariation";


const baseUrl = "/api/product/backoffice"

export async function getProducts(
  pageNo: number,
  productName: string,
  brandName: string
): Promise<Products> {
  const url = `${baseUrl}/products?pageNo=${pageNo}&product-name=${productName}&brand-name=${brandName}`;
  return (await apiClientService.get(url)).json()
}

export async function getLatestProducts(count: number): Promise<Product[]> {
  const url = `${baseUrl}/products/latest/${count}`
  const response = await apiClientService.get(url);
  if (response.status >= 200 && response.status < 300) return response.json()
  return Promise.reject(new Error(response.statusText))
}

export async function exportProducts(productName: string, brandName: string) {
  const url = `${baseUrl}/export/products?product-name=${productName}&brand-name=${brandName}`;
  return (await apiClientService.get(url)).json();
}

export async function deleteProduct(id: number) {
  const url = `${baseUrl}/products/${id}`
  const response = await apiClientService.delete(url)
  if (response.status === 204) return response;
  else return await response.json();
}

export async function createProduct(product: ProductPayload) {
  const url = `${baseUrl}/products`;
  return await apiClientService.post(url, JSON.stringify(product));
}

export async function getProduct(id: number) {
  const url = `${baseUrl}/products/${id}`;
  return (await apiClientService.get(url)).json();
}

export async function getVariationsByProductId(productId: number): Promise<Variation[]> {
  const url = `${baseUrl}/product-variations/${productId}`;
  const response = await apiClientService.get(url);
  if (response.status >= 200 && response.status < 300) return await response.json();
  return Promise.reject(new Error(response.statusText));
}

export async function getRelatedProductByProductId(productId: number): Promise<Product[]> {
  const url = `${baseUrl}/products/related-products/${productId}`;
  const response = await apiClientService.get(url);
  if (response.status >= 200 && response.status < 300) return await response.json();
  return Promise.reject(new Error(response.statusText));
}

export async function updateProduct(id: number, product: ProductPayload) {
  const url = `${baseUrl}/products/${id}`;
  const response = await apiClientService.put(url, JSON.stringify(product));
  if (response.status === 204) return response;
  else return await response.json();
}

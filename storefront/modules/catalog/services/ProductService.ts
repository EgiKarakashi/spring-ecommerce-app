import {ProductThumbnail} from "@/modules/catalog/models/ProductThumbnail";
import apiClientService from "@/common/services/ApiClientService";
import {EcommerceError} from "@/common/services/errors/EcommerceError";
import {ProductAll, ProductFeature} from "@/modules/catalog/models/ProductFeature";
import {ProductVariation} from "@/modules/catalog/models/ProductVariation";
import {ProductDetail} from "@/modules/catalog/models/ProductDetail";
import {ProductOptionValueDisplay, ProductOptionValueGet} from "@/modules/catalog/models/ProductOptionValueGet";
import {ProductsGet} from "@/modules/catalog/models/ProductsGet";
import {SimilarProduct} from "@/modules/catalog/models/SimilarProduct";

const baseUrl = '/api/product/storefront';
const serverSideRenderUrl = `${process.env.API_BASE_PATH}/product/storefront`;

export async function getProductsByIds(ids: number[]): Promise<ProductThumbnail[]> {
  const response = await apiClientService.get(`${baseUrl}/products/list-featured?productId=${ids}`);
  const jsonResponse = await response.json();
  if (!response.ok) {
    throw new EcommerceError(jsonResponse);
  }
  return jsonResponse;
}

export async function getFeaturedProducts(pageNo: number): Promise<ProductFeature> {
  const response = await apiClientService.get(`${baseUrl}/products/featured?pageNo=${pageNo}`);
  return response.json();
}

export async function getProductDetail(slug: string): Promise<ProductDetail> {
  const response = await apiClientService.get(`${serverSideRenderUrl}/product/${slug}`);
  return response.json();
}

export async function getProductOptionValues(productId: number): Promise<ProductOptionValueGet[]> {
  const res = await apiClientService.get(
    `${serverSideRenderUrl}/product-option-combinations/${productId}/values`
  );
  if (res.status >= 200 && res.status < 300) return res.json();
  throw new Error(await res.json());
}

export async function getProductVariationsByParentId(productId: number) : Promise<ProductVariation[]> {
  const res = await apiClientService.get(`${serverSideRenderUrl}/product-variations/${productId}`);
  if (res.status >= 200 && res.status < 300) return res.json();
  throw new Error(await res.json())
}

export async function getProductOptionValueByProductId(
  productId: number
): Promise<ProductOptionValueDisplay[]> {
  const url = `${baseUrl}/product-option-values/${productId}`;
  console.log(url);

  const response = await apiClientService.get(url);
  if (response.status >= 200 && response.status < 300) return await response.json();
  return Promise.reject(new Error(response.statusText));
}

export async function getRelatedProductsByProductId(productId: number): Promise<ProductsGet> {
  const res = await apiClientService.get(`${baseUrl}/products/related-products/${productId}`);
  if (res.status >= 200 && res.status < 300) return res.json();
  throw new Error(await res.json());
}

export async function getSimilarProductsByProductId(productId: number): Promise<SimilarProduct[]> {
  const res = await apiClientService.get(
    `/api/recommendation/embedding/product/${productId}/similarity`
  );
  if (res.status >= 200 && res.status < 300) return res.json();
  throw new Error(await res.json());
}

export async function getProductByMultiParams(queryString: string): Promise<ProductAll> {
  const res = await apiClientService.get(`${baseUrl}/products?${queryString}`);
  return res.json();
}

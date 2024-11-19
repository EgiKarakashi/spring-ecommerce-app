import {Brand} from "@/modules/catalog/models/Brand";
import apiClientService from "@/common/services/ApiClientService";

const baseUrl = '/api/product/backoffice/brands';

export async function getBrands(): Promise<Brand[]> {
  return (await apiClientService.get(baseUrl)).json();
}

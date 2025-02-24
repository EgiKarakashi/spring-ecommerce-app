import {Address} from "@/modules/address/models/AddressModel";
import apiClientService from "@/common/services/ApiClientService";
import {EcommerceError} from "@/common/services/errors/EcommerceError";

const baseUrl = '/api/location/storefront/addresses'

export async function createAddress(address: Address) {
  const response = await apiClientService.post(baseUrl, JSON.stringify(address))
  return response.json()
}

export async function updateAddress(id: string, address: Address) {
  return await apiClientService.put(`${baseUrl}/${id}`, JSON.stringify(address));
}

export async function getAddress(id: string) {
  const response = await apiClientService.get(`${baseUrl}/${id}`);
  const jsonResult = await response.json();
  if (!response.ok) {
    throw new EcommerceError(jsonResult);
  }
  return jsonResult;
}

export async function deleteAddress(id: number) {
  return await apiClientService.delete(`${baseUrl}/${id}`);
}

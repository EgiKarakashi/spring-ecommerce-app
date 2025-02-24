import {Address} from "@/modules/address/models/AddressModel";
import {UserAddressVm} from "@/modules/customer/models/UserAddressVm";
import apiClientService from "@/common/services/ApiClientService";
import {EcommerceError} from "@/common/services/errors/EcommerceError";

const userAddressUrl = '/api/customer/storefront/user-address'

export async function createUserAddress(address: Address): Promise<UserAddressVm> {
  const response = await apiClientService.post(userAddressUrl, JSON.stringify(address))
  const jsonResult = await response.json()
  if (!response.ok) {
    throw new EcommerceError(jsonResult.message)
  }
  return jsonResult
}

export async function getUserAddress() {
  const response = await apiClientService.get(userAddressUrl)
  return response.json()
}

export async function getUserAddressDefault(): Promise<Address> {
  const response = await apiClientService.get(`${userAddressUrl}/default-address`);
  if (response.status >= 200 && response.status < 300) {
    return await response.json();
  }
  throw new Error(response.statusText);
}

export async function deleteUserAddress(id: number) {
  return await apiClientService.delete(`${userAddressUrl}/${id}`);
}

export async function chooseDefaultAddress(id: number) {
  return await apiClientService.put(`${userAddressUrl}/${id}`, null);
}

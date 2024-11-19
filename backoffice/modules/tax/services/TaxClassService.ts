import {TaxClass} from "@/modules/tax/models/TaxClass";
import apiClientService from "@/common/services/ApiClientService";

const baseUrl = '/api/tax/backoffice/tax-classes';

export async function getTaxClasses(): Promise<TaxClass[]> {
  return (await apiClientService.get(baseUrl)).json();
}

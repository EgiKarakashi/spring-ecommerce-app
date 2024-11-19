import {Media} from "@/modules/catalog/models/Media";
import apiClientService from "@/common/services/ApiClientService";

const baseUrl = '/api/medias';

export async function uploadMedia(image: File): Promise<Media> {
  const body = new FormData();
  body.append('multipartFile', image);
  const response = await apiClientService.post(baseUrl, body);
  if (response.status >= 200 && response.status < 300) return await response.json();
  return Promise.reject(response);
}

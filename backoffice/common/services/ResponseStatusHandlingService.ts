import {
  CREATE_FAILED, CREATE_SUCCESSFULLY,
  DELETE_FAILED,
  HAVE_BEEN_DELETED,
  ResponseStatus, ResponseTitle, UPDATE_FAILED, UPDATE_SUCCESSFULLY
} from "@/constants/Common";
import {toastError, toastSuccess} from "@/common/services/ToastService";

export const handleDeletingResponse = (response: any, itemName: string | number) => {
  if (response.status === ResponseStatus.SUCCESS) {
    toastSuccess(itemName + HAVE_BEEN_DELETED)
  } else if (response.title === ResponseTitle.NOT_FOUND) {
    toastError(response.detail);
  } else if (response.title === ResponseTitle.BAD_REQUEST) {
    toastError(response.detail);
  } else {
    toastError(DELETE_FAILED);
  }
}

export const handleUpdatingResponse = (response: any) => {
  if (response.status === ResponseStatus.SUCCESS) {
    toastSuccess(UPDATE_SUCCESSFULLY);
  } else if (response.title === ResponseTitle.BAD_REQUEST) {
    toastError(response.detail);
  } else if (response.title === ResponseTitle.NOT_FOUND) {
    toastError(response.detail);
  } else {
    toastError(UPDATE_FAILED);
  }
};

export const handleCreatingResponse = async (response: any) => {
  if (response.status === ResponseStatus.CREATED) {
    toastSuccess(CREATE_SUCCESSFULLY);
  } else if (response.status === ResponseStatus.BAD_REQUEST) {
    response = await response.json();
    toastError(response.detail);
  } else {
    toastError(CREATE_FAILED);
  }
};

export const handleResponse = (response: any, successMsg: string, errorMsg: string) => {
  if (response.ok) {
    toastSuccess(successMsg);
  } else {
    toastError(errorMsg);
  }
};
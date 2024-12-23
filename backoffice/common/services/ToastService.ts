import { ToastOptions, toast } from "react-toastify";

const toastOptionDefault: ToastOptions = {
  position: "top-right",
  autoClose: 3000,
  closeOnClick: true,
  pauseOnHover: false,
  theme: "colored",
};

export const toastSuccess = (
  message: string,
  toastOption: ToastOptions = toastOptionDefault
): void => {
  toast.success(message, { ...toastOptionDefault, ...toastOption });
};

export const toastError = (
  message: string,
  toastOption: ToastOptions = toastOptionDefault
): void => {
  toast.error(message, { ...toastOptionDefault, ...toastOption });
};

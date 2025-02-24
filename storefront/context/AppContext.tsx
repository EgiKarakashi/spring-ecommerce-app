import React from "react";
import {UserInfoProvider, useUserInfoContext} from "@/context/UserInfoContext";
import {CartProvider, useCartContext} from "@/context/CartContext";

export const AppContext = React.createContext({});

export function AppProvider({ children }: React.PropsWithChildren<{}>) {
  return (
    <UserInfoProvider>
      <CartProvider>{children}</CartProvider>
    </UserInfoProvider>
  );
}

export const useAppContext = () => {
  const cartContext = useCartContext();
  const userInfoContext = useUserInfoContext();

  return {
    ...cartContext,
    ...userInfoContext,
  };
};

export default AppContext;

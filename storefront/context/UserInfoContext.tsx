import React, {createContext, useCallback, useEffect, useMemo, useState} from "react";
import {getMyProfile} from "@/modules/profile/services/ProfileService";

export const UserInfoContext = createContext({
  firstName: '',
  lastName: '',
  email: '',
  fetchUserInfo: () => {}
});

export function UserInfoProvider({children}: React.PropsWithChildren) {
  const [email, setEmail] = useState('');
  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');

  useEffect(() => {
    fetchUserInfo();
  }, []);

  const fetchUserInfo = useCallback(() => {
    getMyProfile()
      .then((res) => {
        setFirstName(res.firstName)
        setLastName(res.lastName)
        setEmail(res.email)
      })
      .catch((error) => {
        console.log(error)
      })
  }, [])

  const value = useMemo(() => ({
    email,
    firstName,
    lastName,
    fetchUserInfo
  }), [email, firstName, lastName, fetchUserInfo])

  return <UserInfoContext.Provider value={value}>{children}</UserInfoContext.Provider>
}

export const useUserInfoContext = () => {
  const {email, firstName, lastName, fetchUserInfo} = React.useContext(UserInfoContext);
  return {email, firstName, lastName, fetchUserInfo}
}

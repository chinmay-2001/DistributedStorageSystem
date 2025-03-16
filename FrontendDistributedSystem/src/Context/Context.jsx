import { useState, useEffect, createContext, useCallback } from "react";

const AppContext = createContext({
  isError: "",
  userInfo: {},
  isLoggedIn: false,
  updateLoggedIn: (val) => {},
  updateUserInfo: (userInfo) => {},
});

export const AppProvider = ({ children }) => {
  const [isError, setIsError] = useState("");
  const [userInfo, setUserInfo] = useState({});
  const [isLoggedIn, setIsLoggedIn] = useState(false);

  const updateLoggedIn = (val) => {
    setIsLoggedIn(val);
  };
  const updateUserInfo = (data) => {
    setUserInfo(data);
  };

  return (
    <AppContext.Provider
      value={{
        isError,
        isLoggedIn,
        userInfo,
        updateLoggedIn,
        updateUserInfo,
      }}
    >
      {children}
    </AppContext.Provider>
  );
};

export default AppContext;

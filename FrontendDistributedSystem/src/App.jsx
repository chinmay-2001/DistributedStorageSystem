import { useContext } from "react";
import AppContext from "./Context/Context";
import "./App.css";
import { AppProvider } from "./Context/Context";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Login from "./components/Login";
import { Signup } from "./components/Signup";
import Home from "./components/Home";
import { Navigate, Outlet } from "react-router-dom";
import OverlayComponent from "./components/OverlayComponent";

function App() {
  const ProtectedRoutes = () => {
    const { isLoggedIn } = useContext(AppContext);
    return isLoggedIn ? <Outlet /> : <Navigate to="/login" replace />;
  };

  return (
    <AppProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/Login" element={<Login />} />
          <Route path="/SignUp" element={<Signup />} />

          <Route element={<ProtectedRoutes />}>
            <Route path="/" element={<Home />} />
          </Route>
        </Routes>
        <OverlayComponent />
      </BrowserRouter>
    </AppProvider>
  );
}

export default App;

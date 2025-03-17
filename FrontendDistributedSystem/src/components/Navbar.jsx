import React, { useContext, useEffect, useState } from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import axios from "axios";
import AppContext from "../Context/Context";
import FileUploader from "./FileUpoader";

const NavbarComponent = ({ setFiles, files }) => {
  const [isLogIn, setIsLogIn] = useState(false);
  const { isLoggedIn } = useContext(AppContext);
  const handleAuth = () => {
    setIsLogIn(!isLoggedIn);
  };

  useEffect(() => {
    setIsLogIn(isLoggedIn);
  }, []);

  return (
    <nav className="navbar navbar-expand-lg navbar-dark bg-dark">
      <div className="container">
        <a className="navbar-brand" href="#">
          MyApp
        </a>

        <button
          className="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#navbarNav"
        >
          <span className="navbar-toggler-icon"></span>
        </button>

        <div className="collapse navbar-collapse" id="navbarNav">
          <ul className="navbar-nav me-auto">
            <li className="nav-item">
              <a className="nav-link" href="#">
                Home
              </a>
            </li>
            <li className="nav-item">
              <a className="nav-link" href="#">
                About
              </a>
            </li>
            <li className="nav-item">
              <a className="nav-link" href="#">
                Contact
              </a>
            </li>
          </ul>

          <div className="ml-auto">
            <FileUploader setFiles={setFiles} files={files} />
          </div>

          <button className="btn btn-outline-light" onClick={handleAuth}>
            {isLogIn ? "Logout" : "Login"}
          </button>
        </div>
      </div>
    </nav>
  );
};

export default NavbarComponent;

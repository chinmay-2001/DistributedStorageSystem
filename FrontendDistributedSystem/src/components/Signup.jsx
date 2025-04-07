import { useState, useCallback } from "react";
import { Lock, Mail, User } from "lucide-react";
import "bootstrap/dist/css/bootstrap.min.css";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import AppContext from "../Context/Context";
import { useContext } from "react";

export function Signup() {
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const navigate = useNavigate();
  const { isLoggedIn, updateLoggedIn, updateUserInfo } = useContext(AppContext);

  const handleSubmit = async (e) => {
    e.preventDefault();
    const response = await axios.post(
      `https://distributedstorage.site/api/signUp`,
      {
        name,
        email,
        password,
      }
    );
    if (response.data) {
      updateLoggedIn(true);

      updateUserInfo({
        ...response.data,
      });
      navigate("/");
    }
  };

  return (
    <div
      className="d-flex justify-content-center align-items-center vh-100 position-relative"
      style={{
        backgroundImage:
          "url('https://images.unsplash.com/photo-1523821741446-edb2b68bb7a0?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D')",
        backgroundSize: "cover",
        backgroundPosition: "center",
      }}
    >
      <div
        className="position-absolute top-0 start-0 w-100 h-100"
        style={{
          backdropFilter: "blur(8px)",
          WebkitBackdropFilter: "blur(8px)",
        }}
      ></div>
      <div
        className="card shadow-lg p-4 position-relative"
        style={{ width: "24rem" }}
      >
        <div className="card-body">
          <h2 className="text-center mb-4">Create an Account</h2>
          <form onSubmit={handleSubmit}>
            <div className="mb-3 position-relative">
              <User className="position-absolute top-50 start-0 translate-middle-y ms-3 text-secondary" />
              <input
                type="text"
                className="form-control ps-5"
                placeholder="Full Name"
                value={name}
                onChange={(e) => setName(e.target.value)}
                required
              />
            </div>
            <div className="mb-3 position-relative">
              <Mail className="position-absolute top-50 start-0 translate-middle-y ms-3 text-secondary" />
              <input
                type="email"
                className="form-control ps-5"
                placeholder="Email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                required
              />
            </div>
            <div className="mb-3 position-relative">
              <Lock className="position-absolute top-50 start-0 translate-middle-y ms-3 text-secondary" />
              <input
                type="password"
                className="form-control ps-5"
                placeholder="Password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
              />
            </div>
            <button type="submit" className="btn btn-primary w-100">
              Sign Up
            </button>
          </form>
          <p className="text-center text-muted mt-3">
            Already have an account?{" "}
            <a href="/Login" className="text-primary">
              Login
            </a>
          </p>
        </div>
      </div>
    </div>
  );
}

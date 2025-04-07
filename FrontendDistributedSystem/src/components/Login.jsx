import { useCallback, useState } from "react";
import { Lock, Mail } from "lucide-react";
import "bootstrap/dist/css/bootstrap.min.css";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { useContext } from "react";
import AppContext from "../Context/Context";

export default function Login() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const navigate = useNavigate();
  const { updateLoggedIn, updateUserInfo } = useContext(AppContext);

  const handleSubmit = async (e) => {
    e.preventDefault();
    const response = await axios.post(`http://65.0.105.158:8080/api/login`, {
      email,
      password,
    });

    if (response.data) {
      updateLoggedIn(true);

      updateUserInfo({
        ...response.data,
      });
      navigate("/");
    }

    console.log(response);
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
          <h2 className="text-center mb-4">Welcome Back</h2>
          <form onSubmit={handleSubmit}>
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
              Login
            </button>
          </form>
          <p className="text-center text-muted mt-3">
            Don't have an account?{" "}
            <a href="/SignUp" className="text-primary">
              Sign Up
            </a>
          </p>
        </div>
      </div>
    </div>
  );
}

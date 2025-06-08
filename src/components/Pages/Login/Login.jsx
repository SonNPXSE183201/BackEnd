import React, { useState, useEffect, useContext } from "react";
import axios from "axios";
import "./Login.css";
import "bootstrap/dist/css/bootstrap.min.css";
import { GoogleOAuthProvider, GoogleLogin } from "@react-oauth/google";
import { useNavigate } from "react-router-dom";
import { UserContext } from "../../../context/UserContext";
import { decodeGoogleToken } from "../../../helpers/decodeGoogleToken";
import apiLogin from "../../../api/apiLogin";

export default function Login() {
  const [taikhoan, setTaikhoan] = useState("");
  const [matkhau, setMatkhau] = useState("");
  const [message, setMessage] = useState("");

  const clientId =
    "298912881431-a0l5ibtfk8jd44eh51b3f4vre3gr4pu3.apps.googleusercontent.com";
  const { setUser, loginWithGoogle } = useContext(UserContext);
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      const user = await apiLogin(taikhoan, matkhau);
      setIsLoggedIn(true);
      setUser(user);
      localStorage.setItem("user", JSON.stringify(user));
      navigate("/");
    } catch (error) {
      console.error("🔥 Lỗi khi login:", error);
      setMessage(
        "❌ Đăng nhập thất bại. Vui lòng kiểm tra lại tài khoản hoặc mật khẩu."
      );
    }
  };

  const handleGoogleLoginSuccess = (credentialResponse) => {
    const user = decodeGoogleToken(credentialResponse.credential);
    if (user) {
      loginWithGoogle(user);
      setIsLoggedIn(true);
      navigate("/");
    } else {
      setMessage("❌ Không thể xác thực từ Google.");
    }
  };

  const handleGoogleLoginError = () => {
    setMessage("❌ Đăng nhập Google thất bại.");
  };

  useEffect(() => {
    console.log("🌐 Current origin:", window.location.origin);
    console.log("🔑 Google Client ID:", clientId);
  }, []);

  return (
    <GoogleOAuthProvider clientId={clientId}>
      <div className="login fullpage">
        <div className="wrapper">
          <form className="action" onSubmit={handleLogin}>
            <h1>Login</h1>

            <div className="input-box">
              <input
                type="text"
                placeholder="Username/Email"
                required
                value={taikhoan}
                onChange={(e) => setTaikhoan(e.target.value)}
              />
            </div>

            <div className="input-box">
              <input
                type="password"
                placeholder="Password"
                required
                value={matkhau}
                onChange={(e) => setMatkhau(e.target.value)}
              />
            </div>

            <div className="remember-forgot">
              <label>
                <input type="checkbox" /> Remember me
              </label>
              <a href="#">Forgot password?</a>
            </div>

            <button type="submit" className="btn">
              Login
            </button>

            {message && (
              <p style={{ marginTop: "10px", color: "#f00" }}>{message}</p>
            )}

            <div style={{ textAlign: "center", margin: "20px 0" }}>
              <p>Hoặc đăng nhập với Google</p>
              <GoogleLogin
                onSuccess={handleGoogleLoginSuccess}
                onError={handleGoogleLoginError}
              />
            </div>

            <div className="register-link">
              <p>
                Bạn chưa có tài khoản?{" "}
                <span
                  onClick={() => navigate("/register")}
                  style={{
                    cursor: "pointer",
                    color: "#c2185b",
                    fontWeight: "600",
                  }}
                >
                  Đăng ký
                </span>
              </p>
            </div>
          </form>
        </div>
      </div>
    </GoogleOAuthProvider>
  );
}

import { useEffect } from "react";
import { useLocation, useNavigate } from "react-router-dom";

export default function CallbackPage() {
    const location = useLocation();
    const navigate = useNavigate();

    useEffect(() => {
        const params = new URLSearchParams(location.search);
        const code = params.get("code");
        if (code) {
            fetch("/api/callback", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ code }),
            })
                .then(res => res.json())
                .then(data => {
                    // JWTをlocalStorageやCookieに保存して遷移
                    // localStorage.setItem('accessToken', data.accessToken)
                    // localStorage.setItem('refreshToken', data.refreshToken)
                    // navigate("/"); // or to next page
                    console.log(data)
                });
        }
    }, [location, navigate]);

    return <div>ログイン処理中...</div>;
}

import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";

const CallbackPage = () => {
    const navigate = useNavigate();

    useEffect(() => {
        const url = new URL(window.location.href);
        const code = url.searchParams.get("code");
        console.log(code)
        // if (code) {
        //     // Spring BootのAPIエンドポイントにcodeをPOST
        //     axios.post("/api/auth/oidc-callback", { code })
        //         .then(res => {
        //             // ログインセッションセットなど（例：localStorage.setItem…）
        //             navigate("/"); // トップに戻るなど
        //         })
        //         .catch(e => {
        //             alert("ログイン失敗");
        //         });
        // }
    }, [navigate]);

    return <div>ログイン処理中...</div>;
};

export default CallbackPage;
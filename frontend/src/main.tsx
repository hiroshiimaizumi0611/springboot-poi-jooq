import { BrowserRouter } from 'react-router-dom'
import { App } from './App'
import './index.css'
import * as React from 'react'
import { createRoot } from 'react-dom/client'
import { AuthProvider } from "react-oidc-context";

const cognitoAuthConfig = {
  authority: "https://cognito-idp.ap-northeast-1.amazonaws.com/ap-northeast-1_js0rZmDGL",
  client_id: "7n3n3u8mvl07ptvtskoussnm1f",
  redirect_uri: "https://estimate-app.com/callback",
  response_type: "code",
  scope: "email openid profile",
};


createRoot(document.getElementById('root') as HTMLElement).render(
  <React.StrictMode>
    <BrowserRouter>
      <AuthProvider {...cognitoAuthConfig}>
        <App />
      </AuthProvider>
    </BrowserRouter>
  </React.StrictMode>,
)

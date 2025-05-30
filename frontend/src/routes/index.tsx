import { Route, Routes } from 'react-router-dom'
import RequireAuth from '../components/auth/RequireAuth'
import { AppLayout } from '../components/layout/AppLayout'
import { EstimateDownloadPage } from '../pages/EstimateDownloadPage'
import { Estimates } from '../pages/Estimates'
import { HomePage } from '../pages/HomePage'
import LoginPage from '../pages/LoginPage'

export default function AppRoutes() {
  return (
    <Routes>
      <Route path="/login" element={<LoginPage />} />
      <Route
        path="/"
        element={
          <RequireAuth>
            <AppLayout />
          </RequireAuth>
        }
      >
        <Route index element={<HomePage />} />
        <Route path="estimateDownload" element={<EstimateDownloadPage />} />
        <Route path="estimates" element={<Estimates />} />
      </Route>
    </Routes>
  )
}

import { Route, Routes } from 'react-router-dom'
import RequireAuth from '../components/auth/RequireAuth'
import { AppLayout } from '../components/layout/AppLayout'
import { EstimatePage } from '../pages/EstimatePage'
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
        <Route path="estimate" element={<EstimatePage />} />
      </Route>
    </Routes>
  )
}

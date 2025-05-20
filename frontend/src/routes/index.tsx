import { Route, Routes } from 'react-router-dom'
import { EstimatePage } from '../pages/EstimatePage'
import { HomePage } from '../pages/HomePage'

export default function AppRoutes() {
  return (
    <Routes>
      <Route path="/" element={<HomePage />} />
      <Route path="/estimate" element={<EstimatePage />} />
    </Routes>
  )
}

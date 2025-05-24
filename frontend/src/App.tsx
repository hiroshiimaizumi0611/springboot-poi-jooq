import { AppLayout } from './components/layout/AppLayout'
import AppRoutes from './routes'

export const App = () => {
  return (
    <AppLayout>
      <AppRoutes />
    </AppLayout>
  )
}

import { Footer } from './Footer'
import { Header } from './Header'
import { SideBar } from './SideBar'

export const AppLayout = ({ children }: { children: React.ReactNode }) => (
  <div className="flex flex-col min-h-screen">
    <Header />
    <div className="flex flex-1">
      <SideBar />
      <main className="flex-1 bg-gray-50 p-8">{children}</main>
    </div>
    <Footer />
  </div>
)

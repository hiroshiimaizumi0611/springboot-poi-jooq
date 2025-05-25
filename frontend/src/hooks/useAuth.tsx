import { useEffect, useState } from 'react'

// 必要なら型を拡張
export function useAuth() {
  const [isAuthenticated, setIsAuthenticated] = useState<boolean | null>(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const token = localStorage.getItem('jwt_token')
    setIsAuthenticated(!!token)
    setLoading(false)
  }, [])

  return { isAuthenticated, loading }
}

import { api } from '../lib/axios'
import { useAuthStore } from '../store/authStore'

export interface LoginResponse {
  accessToken: string
  refreshToken: string
}

export async function login(username: string, password: string): Promise<void> {
  const res = await api.post<LoginResponse>('/login', {
    username,
    password,
  })
  localStorage.setItem('accessToken', res.data.accessToken)
  localStorage.setItem('refreshToken', res.data.refreshToken)
  useAuthStore.getState().setIsAuthenticated(true)
}

export async function logout(): Promise<void> {
  const refreshToken = localStorage.getItem('refreshToken')
  const accessToken = localStorage.getItem('accessToken')
  await api.post(
    '/logout',
    { refreshToken },
    {
      headers: {
        Authorization: accessToken ? `Bearer ${accessToken}` : undefined,
      },
    },
  )
  localStorage.removeItem('accessToken')
  localStorage.removeItem('refreshToken')
  useAuthStore.getState().setIsAuthenticated(false)
  window.location.href = '/login'
}

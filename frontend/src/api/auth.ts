import { api } from '../lib/axios'

export async function login(username: string, password: string) {
  const res = await api.post('/api/login', { username, password })
  return res.data
}

export async function logout() {
  await api.post('/api/logout')
  localStorage.removeItem('jwt_token')
}

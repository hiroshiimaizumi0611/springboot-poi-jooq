import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { login } from '../../api/auth'
import { Button } from '../../components/ui/button'
import { Input } from '../../components/ui/input'

export default function LoginForm() {
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')
  const navigate = useNavigate()

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setError('')
    try {
      await login(username, password)
      navigate('/')
    } catch {
      setError('Invalid username or password')
    }
  }

  return (
    <form className="space-y-6" onSubmit={handleSubmit}>
      <div>
        <Input
          placeholder="Username"
          value={username}
          onChange={e => setUsername(e.target.value)}
          required
        />
      </div>
      <div>
        <Input
          type="password"
          placeholder="Password"
          value={password}
          onChange={e => setPassword(e.target.value)}
          required
        />
      </div>
      {error && <div className="text-red-500 text-sm">{error}</div>}
      <Button type="submit" className="w-full">
        Login
      </Button>
    </form>
  )
}

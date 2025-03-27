import type AuthRequest from '@/interfaces/auth/auth-request'
import { config } from '@/config'
import api from '@/utils/axios'

const API_URL: string = `${config.api.host}:${config.api.port}`

export async function login(request: AuthRequest) {
    const response = await api.post(`${API_URL}/auth/login`, request)
    return response.data
}

export async function register(request: AuthRequest) {
    const response = await api.post(`${API_URL}/auth/register`, request)
    return response.data
}

export async function logout(refreshToken: string, accessToken: string) {
    await api.post(`${API_URL}/auth/logout`,
        { refresh_token: refreshToken },
        {
            headers: {
                Authorization: `Bearer ${accessToken}`
            }
        }
    );
}

export async function refresh(refreshToken: string) {
    const response = await api.post(`${API_URL}/auth/refresh`, { refresh_token: refreshToken })
    return response.data
}

export async function checkAuth(token: string) {
    await api.get(`${API_URL}/auth/check`, {
        headers: { Authorization: `Bearer ${token}` }
      });
}
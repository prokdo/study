import type AuthRequest from '@/interfaces/auth/auth-request'
import api from '@/utils/axios'

export async function login(request: AuthRequest) {
    const response = await api.post(`/auth/login`, request)
    return response.data
}

export async function register(request: AuthRequest) {
    const response = await api.post(`/auth/register`, request)
    return response.data
}

export async function logout(refreshToken: string, accessToken: string) {
    await api.post(`/auth/logout`,
        { refresh_token: refreshToken },
        {
            headers: {
                Authorization: `Bearer ${accessToken}`
            },
            withCredentials: true
        }
    );
}

export async function refresh(refreshToken: string) {
    const response = await api.post(`/auth/refresh`, { refresh_token: refreshToken })
    return response.data
}

export async function checkAuth(token: string) {
    await api.get(`/auth/check`, {
        headers: { Authorization: `Bearer ${token}` },
        withCredentials: true
      });
}
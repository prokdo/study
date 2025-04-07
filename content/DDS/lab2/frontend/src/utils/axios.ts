import axios from 'axios'
import store from '@/store'
import type { AxiosInstance, AxiosRequestHeaders, AxiosResponse, InternalAxiosRequestConfig } from 'axios'
import { config } from '@/config'

// const API_URL: string = `${config.api.host}:${config.api.port}/api`
const API_URL: string = `${config.api.host}`

let isRefreshing = false
let failedQueue: Array<{ resolve: (value: unknown) => void; reject: (reason?: any) => void }> = []

const api: AxiosInstance = axios.create({
  baseURL: API_URL
})

api.interceptors.request.use((config: InternalAxiosRequestConfig) => {
  const token = localStorage.getItem('access_token');
  const isAuthRequest = config.url?.includes('/auth');

  if (token && !isAuthRequest) {
    config.headers = {
      ...config.headers,
      Authorization: `Bearer ${token}`
    } as AxiosRequestHeaders;
    config.withCredentials = true;
  }

  return config;
});

const handleAuthSuccess = (response: AxiosResponse) => {
  const { access_token, refresh_token } = response.data

  if (access_token && refresh_token) {
    localStorage.setItem('access_token', access_token)
    localStorage.setItem('refresh_token', refresh_token)
    store.commit('auth/setTokens', { access_token, refresh_token })
  }
}

api.interceptors.response.use(
  response => {
    if (response.config.url?.startsWith(`/auth`)) {
      handleAuthSuccess(response)
    }
    return response
  },
  async error => {
    const originalRequest = error.config
    const isAuthRequest = originalRequest.url?.startsWith(`/auth`)

    if (isAuthRequest || error.response?.status !== 401) {
      return Promise.reject(error)
    }

    if (originalRequest._retry) {
      return Promise.reject(error)
    }

    if (isRefreshing) {
      return new Promise((resolve, reject) => {
        failedQueue.push({
          resolve: (token: unknown) => {
            originalRequest.headers.Authorization = `Bearer ${token}`
            originalRequest.withCredentials = true
            resolve(api(originalRequest))
          },
          reject
        })
      })
    }

    originalRequest._retry = true
    isRefreshing = true

    try {
      const refreshToken = localStorage.getItem('refresh_token')

      if (!refreshToken) throw new Error('Refresh token not found')

      const { data } = await axios.post(`/auth/refresh`, { refresh_token: refreshToken })

      localStorage.setItem('access_token', data.access_token)
      localStorage.setItem('refresh_token', data.refresh_token)
      store.commit('auth/setTokens', data)

      failedQueue.forEach(req => req.resolve(data.access_token))
      originalRequest.headers.Authorization = `Bearer ${data.access_token}`
      originalRequest.withCredentials = true

      return api(originalRequest)
    } catch (refreshError) {
      failedQueue.forEach(req => req.reject(refreshError))

      localStorage.removeItem('access_token')
      localStorage.removeItem('refresh_token')
      store.commit('auth/clearAuth')

      return Promise.reject(refreshError)
    } finally {
      isRefreshing = false
      failedQueue = []
    }
  }
)

export default api
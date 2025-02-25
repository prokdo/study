import axiosInstance from './axiosInstance';
import { RegisterRequest, LoginRequest } from '../types/request/';
import { RegisterResponse, LoginResponse } from '../types/response';

/**
 * Регистрация пользователя
 * @param userData - Данные пользователя для регистрации
 * @returns Сообщение об успешной регистрации
 */
export async function registerUser(userData: RegisterRequest): Promise<RegisterResponse> {
    try {
        const response = await axiosInstance.post<RegisterResponse>('/register', userData);
        return response.data; // { "message": "User registered successfully" }
    } catch (error: any) {
        throw new Error(error.response?.data?.message || 'Registration failed');
    }
}

/**
 * Аутентификация пользователя
 * @param credentials - Учетные данные пользователя
 * @returns Токен и сообщение об успешной аутентификации
 */
export async function loginUser(credentials: LoginRequest): Promise<LoginResponse> {
    try {
        const response = await axiosInstance.post<LoginResponse>('/login', credentials);
        return response.data; // { message: "Login successful", token: "..." }
    } catch (error: any) {
        throw new Error(error.response?.data?.message || 'Login failed');
    }
}

/**
 * Проверка аутентификации
 * @returns Сообщение об успешной проверке
 */
export async function checkAuth(): Promise<AuthResponse> {
    try {
        const response = await axiosInstance.get<AuthResponse>('/api/check-auth');
        return response.data; // { message: "Authorized" }
    } catch (error: any) {
        throw new Error(error.response?.data?.message || 'Authentication failed');
    }
}
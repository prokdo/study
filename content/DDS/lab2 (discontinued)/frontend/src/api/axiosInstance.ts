import axios from "axios"

const BASE_URL = import.meta.env.VITE_APP_API_BASE_URL;

if (!BASE_URL) {
    throw new Error('REACT_APP_API_BASE_URL is not defined in the environment variables');
}

const axiosInstance = axios.create({
    baseURL: BASE_URL
})

axiosInstance.interceptors.request.use((config) => {
    const token = localStorage.getItem('token');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

axiosInstance.interceptors.response.use(
    (response) => {
        return response;
    },
    (error) => {
        if (error.response?.status === 401) {
            const currentPath = window.location.pathname + window.location.search;
            localStorage.setItem('redirectPath', currentPath);

            localStorage.removeItem('token');

            window.location.href = '/login';
        }

        // Пробрасываем ошибку дальше, чтобы её можно было обработать в компонентах
        return Promise.reject(error);
    }
);

export default axiosInstance;
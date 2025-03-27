import { config } from "@/config"
import api from "@/utils/axios"

const API_URL: string = `${config.api.host}:${config.api.port}`

export async function fetchUser() {
    const response = await api.get(`${API_URL}/users/me`)
    return response.data
}
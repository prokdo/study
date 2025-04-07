import api from "@/utils/axios"

export async function fetchUser() {
    const response = await api.get(`/users/me`)
    return response.data
}
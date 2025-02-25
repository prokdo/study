import axiosInstance from './axiosInstance'

export async function getUserById(id: number) {
    try {
        const response = await axiosInstance.get(`/api/users/id?id=${id}`);
        return response.data; // { id: number, name: string, email: string }
    } catch (error: any) {
        throw new Error(error.response?.data?.message || 'Failed to fetch user');
    }
}

export async function getUserByEmail(email: string) {
    try {
        const response = await axiosInstance.get(`/api/users/email?email=${email}`);
        return response.data; // { id: number, name: string, email: string }
    } catch (error: any) {
        throw new Error(error.response?.data?.message || 'Failed to fetch user');
    }
}

export async function checkUserExistsByEmail(email: string) {
    try {
        const response = await axiosInstance.get(`/api/check-email?email=${email}`);
        return response.data; // { message: "User exists" }
    } catch (error: any) {
        throw new Error(error.response?.data?.message || 'Failed to fetch user');
    }
}
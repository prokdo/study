import type User from "@/interfaces/dto/user"

export default interface AuthState {
    access_token:   string | null
    refresh_token:  string | null
    user:           User | null
    error:          string | null
    isLoading:      boolean
}
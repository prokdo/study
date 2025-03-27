import type { Role } from "@/enums/roles";

export default interface User {
    id:         number,
    username:   string,
    role:       Role,
    createdAt:  Date
}
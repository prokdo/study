export interface APIConfig {
    readonly host: string
    readonly port: number
}

export interface AppConfig {
    readonly port: number
}

export interface Config {
    readonly api:   APIConfig
    readonly app:   AppConfig
}

export const config: Config = {
    api: {
        host: import.meta.env.VITE_API_HOST,
        port: Number(import.meta.env.VITE_API_PORT),
    },
    app: {
        port: Number(import.meta.env.VITE_PORT)
    }
}
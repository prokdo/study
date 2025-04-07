export interface APIConfig {
    readonly host: string
}

export interface Config {
    readonly api:  APIConfig
}

export const config: Config = {
    api: {
        host: `${import.meta.env.VITE_API_HOST}`,
    },
}
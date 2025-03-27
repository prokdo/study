interface ImportMetaEnv {
    readonly VITE_API_HOST: string
    readonly VITE_API_PORT: number
    readonly VITE_APP_PORT: number
}

interface ImportMeta {
    readonly env: ImportMetaEnv
}
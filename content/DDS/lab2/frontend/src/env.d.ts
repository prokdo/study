interface ImportMetaEnv {
    readonly VITE_API_PROTOCOL: string
    readonly VITE_API_HOST:     string
    readonly VITE_API_PORT:     number
}

interface ImportMeta {
    readonly env: ImportMetaEnv
}
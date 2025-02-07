import { ZodSchema } from 'zod';

export function validateOrThrow<T>(schema: ZodSchema<T>, data: unknown): T {
    const result = schema.safeParse(data);
    if (!result.success) {
        console.log(result.error);
        throw new Error('Field validation failed');
    }
    return result.data;
}

export async function fetchAndValidate<T>(schema: ZodSchema<T>, url: string): Promise<T> {
    const response = await fetch(urlWithPrefix(url, false));
    const data = await response.json();
    return validateOrThrow(schema, data);
}

export function urlWithPrefix(url: string, ws: boolean): string {
    const prefix = localStorage.getItem('prefix');
    const prefixed = prefix ? prefix + url : url;

    if (ws) {
        if (!prefixed.startsWith('http')) {
            return prefixed;
        }
        const parsed = new URL(prefixed);
        parsed.protocol = parsed.protocol === 'http:' ? 'ws:' : 'wss:';
        return parsed.href;
    }

    return prefixed;
}
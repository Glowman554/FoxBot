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
    const prefix = localStorage.getItem('prefix');
    const realUrl = prefix ? prefix + url : url;

    const response = await fetch(realUrl);
    const data = await response.json();
    return validateOrThrow(schema, data);
}

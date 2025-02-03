import { z } from 'zod';

export namespace FromServer {
    const replySchema = z.object({
        type: z.literal('reply'),
        message: z.string(),
    });
    export type Reply = z.infer<typeof replySchema>;

    const replyFileSchema = z.object({
        type: z.literal('replyFile'),
        file: z.string(),
        fileName: z.string(),
        fileType: z.union([z.literal('IMAGE'), z.literal('VIDEO'), z.literal('AUDIO'), z.literal('DOCUMENT')]),
        nsfw: z.boolean(),
        caption: z.string().nullable(),
    });
    export type ReplyFile = z.infer<typeof replyFileSchema>;

    const authenticateSchema = z.object({
        type: z.literal('authenticate'),
        string: z.string(),
    });
    export type Authenticate = z.infer<typeof authenticateSchema>;


    const optionSchema = z.object({
        name: z.string(),
        value: z.union([z.number(), z.string()]),
    });
    export type Option = z.infer<typeof optionSchema>;

    const argumentSchema = z.object({
        type: z.union([z.literal('STRING'), z.literal('INTEGER'), z.literal('BOOLEAN'), z.literal('NUMBER'), z.literal('ATTACHMENT')]),
        name: z.string(),
        description: z.string(),
        optional: z.boolean(),
        options: z.array(optionSchema),
    });
    export type Argument = z.infer<typeof argumentSchema>;

    const schemaSchema = z.object({
        name: z.string(),
        description: z.string(),
        arguments: z.array(argumentSchema),
    });
    export type Schema = z.infer<typeof schemaSchema>;

    const infoSchema = z.object({
        type: z.literal('info'),
        prefix: z.string(),
        schemas: z.array(schemaSchema),
    });

   

    export const packetSchema = z.union([replySchema, replyFileSchema, authenticateSchema, infoSchema]);
    export type Packet = z.infer<typeof packetSchema>;

    export const messagesSchema = z.union([replySchema, replyFileSchema, authenticateSchema]);
    export type Messages = z.infer<typeof messagesSchema>;
}

export namespace ToServer {
    export interface UploadedFile {
        name: string;
        file: string;
    }

    export interface Message {
        type: 'message';
        message: string;
        files: UploadedFile[];
    }

    export interface Authenticate {
        type: 'authenticate';
        user: string;
    }

    export interface SchemaMessage {
        type: 'schemaMessage';
        schemaCommandName: string;
        [argument: string]: number | string | UploadedFile;
    }

    export type Schema = Message | Authenticate | SchemaMessage;
}

export interface InformationMessage {
    type: 'infoMessage';
    message: string;
}

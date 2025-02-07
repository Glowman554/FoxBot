import { createSignal, For, onCleanup, onMount } from 'solid-js';
import { urlWithPrefix, validateOrThrow } from '../../validatedFetch';
import { FromServer, type InformationMessage, type ToServer } from './types';
import ChatEntry from './ChatEntry';
import UploadButton from './UploadButton';
import SchemaCommandContainer from './SchemaCommandContainer';

function plural(length: number, word: string) {
    if (length == 1 || length == -1) {
        return word;
    } else {
        return word + 's';
    }
}

function SchemaCommandTable(props: { schemas: FromServer.Schema[]; send: (message: ToServer.SchemaMessage) => void; visible: boolean }) {
    return (
        <>
            <div class="center" style={{ display: props.visible ? undefined : 'none' }}>
                <div class="m-8 w-2/3 rounded-xl bg-black p-8 max-sm:w-full max-sm:p-2">
                    <table class="w-full bg-black">
                        <tbody>
                            <For each={props.schemas}>{(schema) => <SchemaCommandContainer schema={schema} send={props.send} />}</For>
                        </tbody>
                    </table>
                </div>
            </div>
        </>
    );
}

export default function () {
    const [websocket, setWebsocket] = createSignal<WebSocket | null>(null);
    const [history, setHistory] = createSignal<(FromServer.Messages | InformationMessage)[]>([]);
    const [prefix, setPrefix] = createSignal('');
    const [commandInput, setCommandInput] = createSignal('');
    const [uploadedFiles, setUploadedFiles] = createSignal<ToServer.UploadedFile[]>([]);
    const [schemas, setSchemas] = createSignal<FromServer.Schema[]>([]);
    const [displaySchemaTable, setDisplaySchemaTable] = createSignal(false);

    const connect = () => {
        const ws = new WebSocket(urlWithPrefix('/web', true));
        ws.onopen = () => {
            console.log('websocket opened');
            setWebsocket(ws);
        };
        ws.onmessage = (event) => {
            const data = JSON.parse(event.data);
            console.log('websocket message', data);

            const validated = validateOrThrow(FromServer.packetSchema, data);
            switch (validated.type) {
                case 'reply':
                case 'replyFile':
                case 'authenticate':
                    setHistory([...history(), validated]);
                    break;
                case 'info':
                    setPrefix(validated.prefix);
                    setCommandInput(validated.prefix);
                    setSchemas(validated.schemas);
                    break;
            }
        };
        ws.onclose = () => {
            console.log('websocket closed');
            setWebsocket(null);
            setTimeout(connect, 1000);
        };
    };

    const onCommandSend = () => {
        const files = uploadedFiles();
        if (files.length == 0) {
            setHistory([...history(), { type: 'infoMessage', message: commandInput() } satisfies InformationMessage]);
        } else {
            setHistory([
                ...history(),
                {
                    type: 'infoMessage',
                    message: `[${files.length} ${plural(files.length, 'file')} attached] ` + commandInput(),
                } satisfies InformationMessage,
            ]);
        }

        const ws = websocket();
        if (!ws) {
            return;
        }

        ws.send(
            JSON.stringify({
                type: 'message',
                message: commandInput(),
                files,
            } satisfies ToServer.Message)
        );

        setUploadedFiles([]);
        setCommandInput(prefix());
    };

    const onAuthenticate = () => {
        const id = prompt("What is your userId on the platform you want to authenticate with (use 'owo!whoami' to check)");
        if (id == null) {
            return;
        }

        const ws = websocket();
        if (!ws) {
            return;
        }

        ws.send(
            JSON.stringify({
                type: 'authenticate',
                user: id,
            } satisfies ToServer.Authenticate)
        );
    };

    const sendSchemaCommand = (message: ToServer.SchemaMessage) => {
        const ws = websocket();
        if (!ws) {
            return;
        }

        ws.send(JSON.stringify(message));
    };

    onMount(() => {
        connect();
    });

    onCleanup(() => {
        const ws = websocket();
        if (ws) {
            console.log('closing websocket');
            ws.close();
        }
    });

    return (
        <>
            <div class="pb-8">
                <div class="center">
                    <button class="button w-1/4 text-center max-sm:w-3/4" onClick={() => setDisplaySchemaTable(!displaySchemaTable())}>
                        Toggle schema commands
                    </button>
                    <button class="button w-1/4 text-center max-sm:w-3/4" onClick={onAuthenticate}>
                        Authenticate
                    </button>
                    <UploadButton callback={setUploadedFiles} />
                </div>
            </div>

            <SchemaCommandTable schemas={schemas()} send={sendSchemaCommand} visible={displaySchemaTable()} />

            <For each={history()}>{(message) => <ChatEntry message={message} />}</For>
            <input
                disabled={websocket() == null}
                value={commandInput()}
                class="mb-8 w-full rounded-xl p-2 text-black"
                type="text"
                autocomplete="off"
                autocapitalize="off"
                onKeyDown={(e) => {
                    setCommandInput(e.currentTarget.value);
                    if (e.key == 'Enter') {
                        onCommandSend();
                    }
                }}
            ></input>
        </>
    );
}

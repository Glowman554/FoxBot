import { createSignal, For, Match, onCleanup, onMount, Show, Switch } from 'solid-js';
import { validateOrThrow } from '../../validatedFetch';
import { FromServer, type InformationMessage, type ToServer } from './types';
import ChatEntry from './ChatEntry';
import UploadButton from './UploadButton';

function plural(length: number, word: string) {
    if (length == 1 || length == -1) {
        return word;
    } else {
        return word + 's';
    }
}

export default function () {
    const [websocket, setWebsocket] = createSignal<WebSocket | null>(null);
    const [history, setHistory] = createSignal<(FromServer.Messages | InformationMessage)[]>([]);
    const [prefix, setPrefix] = createSignal('');
    const [commandInput, setCommandInput] = createSignal('');
    const [uploadedFiles, setUploadedFiles] = createSignal<ToServer.UploadedFile[]>([]);

    const connect = () => {
        const ws = new WebSocket('wss://foxbot.glowman554.de/web');
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
            <button onClick={onAuthenticate}>Authenticate</button>
            <UploadButton callback={setUploadedFiles} />
            <For each={history()}>{(message) => <ChatEntry message={message} />}</For>
            <input
                disabled={websocket() == null}
                value={commandInput()}
                class="text-black"
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

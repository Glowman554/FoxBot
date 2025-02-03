import { createSignal, For, Match, Show, Switch } from 'solid-js';
import type { FromServer, ToServer } from './types';
import { createStore } from 'solid-js/store';
import UploadButton from './UploadButton';

interface ArgumentDispatch {
    name: string;
    value: string | number | ToServer.UploadedFile;
}

export default function (props: { schema: FromServer.Schema; send: (message: ToServer.SchemaMessage) => void }) {
    const [display, setDisplay] = createSignal(false);
    return (
        <>
            <tr>
                <td onClick={() => setDisplay(!display())}>
                    <p class="cursor-pointer hover:border-b">{props.schema.name}</p>
                </td>
            </tr>
            <tr style={{ display: display() ? undefined : 'none' }}>
                <td>
                    <SchemaCommand schema={props.schema} send={props.send} />
                </td>
            </tr>
        </>
    );
}

function SchemaCommand(props: { schema: FromServer.Schema; send: (message: ToServer.SchemaMessage) => void }) {
    const [message, setMessage] = createStore<ToServer.SchemaMessage>({ type: 'schemaMessage', schemaCommandName: props.schema.name });

    return (
        <div class="rounded-xl bg-zinc-800 p-4">
            <h2>{props.schema.name}</h2>
            <p>{props.schema.description}</p>

            <table>
                <For each={props.schema.arguments}>{(argument) => <Argument argument={argument} setArgument={(argument) => setMessage(argument.name, argument.value)} />}</For>
            </table>

            <button
                class="button"
                onClick={() => {
                    for (const argument of props.schema.arguments) {
                        if (!message[argument.name] && !argument.optional) {
                            alert('Missing argument ' + argument.name);
                            return;
                        }
                    }
                    props.send(message);
                }}
            >
                Send
            </button>
        </div>
    );
}

function Argument(props: { argument: FromServer.Argument; setArgument: (argument: ArgumentDispatch) => void }) {
    return (
        <tr>
            <td class="text-nowrap">{props.argument.description}</td>
            <td class="w-full">
                <ArgumentInput argument={props.argument} setArgument={props.setArgument} />
            </td>
        </tr>
    );
}

function ArgumentInput(props: { argument: FromServer.Argument; setArgument: (argument: ArgumentDispatch) => void }) {
    return (
        <Switch>
            <Match when={props.argument.type === 'STRING'}>
                <Show
                    when={props.argument.options.length != 0}
                    fallback={
                        <input
                            type="text"
                            class="ml-3 w-full rounded-lg p-1 text-black"
                            onChange={(e) => {
                                props.setArgument({ name: props.argument.name, value: e.currentTarget.value });
                            }}
                        />
                    }
                >
                    <For each={props.argument.options}>
                        {(option) => (
                            <button class="button" onClick={() => props.setArgument({ name: props.argument.name, value: option.value })}>
                                {option.name}
                            </button>
                        )}
                    </For>
                </Show>
            </Match>
            <Match when={props.argument.type === 'INTEGER'}>
                <input
                    type="number"
                    class="ml-3 w-full rounded-lg p-1 text-black"
                    onChange={(e) => {
                        props.setArgument({ name: props.argument.name, value: parseInt(e.currentTarget.value) });
                    }}
                />
            </Match>
            <Match when={props.argument.type === 'ATTACHMENT'}>
                <UploadButton
                    callback={(files) => {
                        for (const file of files) {
                            props.setArgument({ name: props.argument.name, value: file });
                        }
                    }}
                />
            </Match>
        </Switch>
    );
}

import Query from '@glowman554/base-components/src/query/Query';
import { createSignal, For } from 'solid-js';
import { z } from 'zod';
import { fetchAndValidate } from '../validatedFetch';

const helpEntrySchema = z.object({
    command: z.string(),
    shortHelp: z.string(),
    longHelp: z.string(),
    permission: z.string().nullable(),
    group: z.string(),
});

type HelpEntry = z.infer<typeof helpEntrySchema>;

function capitalizeFirstLetterOnly(string: string) {
    return string.charAt(0).toUpperCase() + string.slice(1).toLowerCase();
}

function Entry(props: { command: HelpEntry }) {
    const [visible, setVisible] = createSignal(false);
    return (
        <>
            <tr onClick={() => setVisible(!visible())} class="cursor-pointer hover:border-b">
                <td>{props.command.command}</td>
                <td>
                    {props.command.shortHelp.endsWith('.')
                        ? props.command.shortHelp.substring(0, props.command.shortHelp.length - 1)
                        : props.command.shortHelp}
                </td>
                <td>{capitalizeFirstLetterOnly(props.command.group)}</td>
            </tr>
            <tr
                style={{
                    display: visible() ? undefined : 'none',
                }}
            >
                <td colSpan={3} class="rounded-lg bg-zinc-800 p-2">
                    <pre>{props.command.longHelp}</pre>
                </td>
            </tr>
        </>
    );
}

export default function () {
    return (
        <Query f={() => fetchAndValidate(z.array(helpEntrySchema), '/api/help')}>
            {(entries) => (
                <div class="center">
                    <div class="m-8 w-2/3 rounded-xl bg-black p-8">
                        <table class="w-full bg-black">
                            <thead>
                                <tr class="border-b">
                                    <td class="text-2xl">Command</td>
                                    <td class="text-2xl">Description</td>
                                    <td class="text-2xl">Group</td>
                                </tr>
                            </thead>
                            <tbody>
                                <For each={entries}>{(entry) => <Entry command={entry} />}</For>
                            </tbody>
                        </table>
                    </div>
                </div>
            )}
        </Query>
    );
}

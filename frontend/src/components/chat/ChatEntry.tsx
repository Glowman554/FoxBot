import { Match, Switch } from 'solid-js';
import type { FromServer, InformationMessage } from './types';

function File(props: { file: FromServer.ReplyFile }) {
    return (
        <Switch fallback={<div>Unknown file type</div>}>
            <Match when={props.file.fileType === 'IMAGE'}>
                <img src={props.file.file} />
            </Match>
            <Match when={props.file.fileType === 'VIDEO'}>
                <video src={props.file.file} controls></video>
            </Match>
            <Match when={props.file.fileType === 'AUDIO'}>
                <audio src={props.file.file} controls></audio>
            </Match>
            <Match when={props.file.fileType === 'DOCUMENT'}>
                <a href={props.file.file} download={props.file.fileName.split('/').pop()}>
                    Open file
                </a>
            </Match>
        </Switch>
    );
}

export interface Props {
    message: FromServer.Messages | InformationMessage;
}

export default function (props: Props) {
    return (
        <div class="pb-4">
            <Switch fallback={<div>Unknown message type</div>}>
                <Match when={props.message.type === 'reply'}>
                    <pre innerHTML={(props.message as FromServer.Reply).message}></pre>
                </Match>
                <Match when={props.message.type === 'infoMessage'}>
                    <div>{(props.message as InformationMessage).message}</div>
                </Match>
                <Match when={props.message.type === 'replyFile'}>
                    <File file={props.message as FromServer.ReplyFile} />
                </Match>
                <Match when={props.message.type === 'authenticate'}>
                    <pre>Send {(props.message as FromServer.Authenticate).string} to the bot to authenticate this session.</pre>
                </Match>
            </Switch>
        </div>
    );
}

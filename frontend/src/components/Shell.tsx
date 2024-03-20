"use client";

import React, { useCallback, useReducer } from "react";
import useWebSocket from "react-use-websocket";

namespace FromServer {
	export interface BaseMessage {
		type: "reply" | "replyFile" | "authenticate";
	}

	export interface Reply extends BaseMessage {
		message: string;
	}

	export interface ReplyFile extends BaseMessage {
		file: string;
		fileType: "IMAGE" | "VIDEO" | "AUDIO" | "DOCUMENT";
		nsfw: boolean;
		caption: string | null;
	}

	export interface Authenticate extends BaseMessage {
		string: string;
	}
}

namespace ToServer {
	export interface BaseMessage {
		type: "message" | "authenticate" | "displayName";
	}

	export interface Message extends BaseMessage {
		message: string;
	}

	export interface Authenticate extends BaseMessage {
		user: string;
	}
}


function entriesReducer(state: {entries: JSX.Element[]}, action: JSX.Element) {
	return {
		entries: [...state.entries, action]
	};
}


export function Shell() {
    const [entriesState, dispatchEntry] = useReducer(entriesReducer, { entries: [] });
	const {sendMessage} = useWebSocket(useCallback(() => (location.protocol == "https:" ? "wss://" : "ws://") + location.host + "/web", []), {
		onOpen: () => {
			dispatchEntry(<p>Connection successful.</p>);
		},
		onMessage: (message) => {
			const json = JSON.parse(message.data) as FromServer.BaseMessage;
			switch (json.type) {
				case "reply":
					const reply = json as FromServer.Reply;
					dispatchEntry(<p dangerouslySetInnerHTML={{__html: reply.message.replaceAll("\n", "<br />")}}></p>);
					break;
				case "replyFile":
					const replyFile = json as FromServer.ReplyFile;
					if (replyFile.caption) {
						dispatchEntry(<p dangerouslySetInnerHTML={{__html: replyFile.caption.replaceAll("\n", "<br />")}}></p>)
					}
					switch (replyFile.fileType) {
						case "IMAGE":
							dispatchEntry(<img src={"/" + replyFile.file} />);
							break;
						case "VIDEO":
							dispatchEntry(<video src={"/" + replyFile.file} controls/>);
							break;
						case "AUDIO":
							dispatchEntry(<audio src={"/" + replyFile.file} controls/>);
							break;
						case "DOCUMENT":
							dispatchEntry(<><a href={"/" + replyFile.file}>Open file</a><br /></>);
							break;
					}
                    break;
				case "authenticate":
					const authenticate = json as FromServer.Authenticate;
					dispatchEntry(<p>Send {authenticate.string} to the bot to authenticate this session.</p>);
					break;
			}
		}

	}, true);

	return (
		<div className="glow-text">
			<button className="glow-auth-button" onClick={() => {
				const id = prompt("What is your userId on the platform you want to authenticate with (use 'owo!whoami' to check)");
				sendMessage(JSON.stringify({
					type: "authenticate",
					user: id
				} as ToServer.Authenticate));
			}}>Authenticate</button>
			<div className="glow-shell">
				{entriesState.entries.map((comp, i) => React.cloneElement(comp, { key: i }))}
			</div>

			<input className="glow-input" style={{
			}} type="text" autoComplete="off" autoCapitalize="off" onKeyDown={(e) => {
					if (e.key == "Enter") {
						dispatchEntry(<p>{(e.target as any).value}</p>)
						sendMessage(JSON.stringify({
							type: "message",
							message: (e.target as any).value
						} as ToServer.Message));
						(e.target as any).value = "";
					}
				}
			}></input>
		</div>
	);
}
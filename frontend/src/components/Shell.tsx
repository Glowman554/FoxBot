"use client";

import React, { ChangeEvent, useCallback, useReducer, useState } from "react";
import useWebSocket from "react-use-websocket";

namespace FromServer {
	export interface BaseMessage {
		type: "reply" | "replyFile" | "authenticate" | "info";
	}

	export interface Reply extends BaseMessage {
		message: string;
	}

	export interface ReplyFile extends BaseMessage {
		file: string;
		fileName: string;
		fileType: "IMAGE" | "VIDEO" | "AUDIO" | "DOCUMENT";
		nsfw: boolean;
		caption: string | null;
	}

	export interface Authenticate extends BaseMessage {
		string: string;
	}

	export interface Info extends BaseMessage {
		prefix: string;
	}
}

namespace ToServer {
	
	export interface BaseMessage {
		type: "message" | "authenticate" | "displayName";
	}

	export interface UploadedFile {
		name: string;
		file: string;
	}

	export interface Message extends BaseMessage {
		message: string;
		files: UploadedFile[];
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

function plural(word: string, length: number) {
	if (length > 1) {
		return word + "s";
	} else {
		return word;
	}
}

export function Shell() {
    const [entriesState, dispatchEntry] = useReducer(entriesReducer, { entries: [] });
	const [prefix, setPrefix] = useState("");
	const [uploadedFiles, setUploadedFiles] = useState([] as ToServer.UploadedFile[]);
	const {sendMessage} = useWebSocket(useCallback(() => (location.protocol == "https:" ? "wss://" : "ws://") + location.host + "/web", []), {
		onOpen: () => {
			dispatchEntry(<p>Connection successful.</p>);
		},
		onMessage: (message) => {
			if (message.data == "pong") {
				return;
			}
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
							dispatchEntry(<img src={replyFile.file} />);
							break;
						case "VIDEO":
							dispatchEntry(<video src={replyFile.file} controls/>);
							break;
						case "AUDIO":
							dispatchEntry(<audio src={replyFile.file} controls/>);
							break;
						case "DOCUMENT":
							dispatchEntry(<><a href={replyFile.file} download={replyFile.fileName.split("/").pop()}>Open file</a><br /></>);
							break;
					}
                    break;
				case "authenticate":
					const authenticate = json as FromServer.Authenticate;
					dispatchEntry(<p>Send {authenticate.string} to the bot to authenticate this session.</p>);
					break;
				case "info":
					const info = json as FromServer.Info;
					setPrefix(info.prefix);
					break;
			}
		},
		heartbeat: {
			interval: 1000,
		},
		shouldReconnect: (closeEvent) => true,
		retryOnError: true,
		reconnectInterval: 1000
	}, true);

	const processUpload = (e: ChangeEvent<HTMLInputElement>) => {
		const target = e.target;

		const promises: Promise<ToServer.UploadedFile>[] = [];

		if (target.files) {
			const files = target.files;
			
			for (let i = 0; i < files.length; i++) {
				promises.push(new Promise<ToServer.UploadedFile>((resolve, reject) => {
					const reader = new FileReader();
					reader.readAsDataURL(files[i]);
					reader.onload = () => resolve({ name: files[i].name, file: (reader.result as string).split(",").pop() as string });
					reader.onerror = (error) => reject(error);
					// reader.onprogress = (progress) => prg((progress.loaded / progress.total) * 100);
				}));
			}
		}


		Promise.all(promises).then(f => {
			setUploadedFiles(f);
		});
	}

	return (
		<div className="glow-text">
			<button className="glow-auth-button" onClick={() => {
				const id = prompt("What is your userId on the platform you want to authenticate with (use 'owo!whoami' to check)");
				sendMessage(JSON.stringify({
					type: "authenticate",
					user: id
				} as ToServer.Authenticate));
			}}>Authenticate</button>

			<div className="glow-upload-field">
				<label htmlFor="file">{uploadedFiles.length + " " + plural("file", uploadedFiles.length) +  " uploaded."}</label>
				<br />
				<input type="file" name="file" onChange={processUpload} multiple />
			</div>

			<div className="glow-shell">
				{entriesState.entries.map((comp, i) => React.cloneElement(comp, { key: i }))}
			</div>

			<input className="glow-input" style={{
			}} type="text" autoComplete="off" autoCapitalize="off" defaultValue={prefix} onKeyDown={(e) => {
					if (e.key == "Enter") {
						if (uploadedFiles.length == 0) {
							dispatchEntry(<p>{(e.target as any).value}</p>)
						} else {
							dispatchEntry(<p><code>[{uploadedFiles.length} {plural("file", uploadedFiles.length)} attached.]</code> {(e.target as any).value}</p>)
						}

						sendMessage(JSON.stringify({
							type: "message",
							message: (e.target as any).value,
							files: uploadedFiles
						} as ToServer.Message));
						
						setUploadedFiles([]);
						(e.target as any).value = prefix;
					}
				}
			}></input>
		</div>
	);
}
"use client";

import { EXTERNAL, EXTERNAL_API } from "@/environment";
import { useEffect, useState } from "react";

interface HelpEntry {
    command: string;
    shortHelp: string;
    longHelp: string;
    permission: string | null;
    group: string;
}

export function CommandEntry(props: { command: HelpEntry }) {
    const [show, setShow] = useState(false);

    return (
        <>
            <tr className="glow-tr" onClick={() => setShow(!show)}>
                <td className="glow-td">{props.command.command}</td>
                <td className="glow-td">{props.command.shortHelp.endsWith(".") ? props.command.shortHelp.substring(0, props.command.shortHelp.length - 1) : props.command.shortHelp}</td>
                <td className="glow-td">{props.command.group}</td>
            </tr>
            <tr className="glow-tr" style={{
                    display: show ? undefined : "none"
                }}>
                    <td className="glow-td" colSpan={3} style={{
                        backgroundColor: "grey"
                    }} dangerouslySetInnerHTML={{__html: props.command.longHelp.replaceAll("\n", "<br />")}}>

                    </td>
            </tr>
            
        </>
    );
}

export function Commands() {
    const [entries, setEntries] = useState([] as HelpEntry[]);
    useEffect(() => {
        fetch(EXTERNAL ? EXTERNAL_API + "/api/help" : "/api/help").then(r => r.json().then(r => setEntries(r as HelpEntry[])));
    }, []);

	return (
        <table className="glow-table">
            <thead>
                <tr className="glow-tr">
                    <td className="glow-td">Command</td>
                    <td className="glow-td">Description</td>
                    <td className="glow-td">Group</td>
                </tr>
            </thead>
            <tbody>
                {
                    entries.map((e, i) => <CommandEntry command={e} key={i} />)
                }
            </tbody>
        </table>
	);
}
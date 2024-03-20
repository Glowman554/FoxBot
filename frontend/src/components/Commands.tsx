"use client";

import { useEffect, useState } from "react";

interface HelpEntry {
    command: string;
    shortHelp: string;
    longHelp: string;
    permission: string | null;
    group: string;
}

export function Commands(props: { hostUrl: string }) {
    const [entries, setEntries] = useState([] as HelpEntry[]);
    useEffect(() => {
        fetch(props.hostUrl + "/api/help").then(r => r.json().then(r => setEntries(r as HelpEntry[])));
    }, []);

	return (
        <table className="glow-table">
            <thead>
                <tr className="glow-tr">
                    <td className="glow-td">Command</td>
                    <td className="glow-td">Description</td>
                </tr>
            </thead>
            <tbody>
                {
                    entries.map((e, i) => 
                        <tr key={i} className="glow-tr">
                            <td className="glow-td">{e.command}</td>
                            <td className="glow-td">{e.shortHelp.endsWith(".") ? e.shortHelp.substring(0, e.shortHelp.length - 1) : e.shortHelp}</td>
                        </tr>
                    )
                }
            </tbody>
        </table>
	);
}
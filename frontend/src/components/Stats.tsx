"use client";

import { useEffect, useState } from "react";
import { Time } from "./Time";

interface StatsObject {
    commands: number;
    platforms: number;
    uptime: number;
}

export function Stats(props: { hostUrl: string }) {
    const [stats, setStats] = useState({
        commands: 1,
        platforms: 1,
        uptime: 1
    } as StatsObject);

    useEffect(() => {
        fetch(props.hostUrl + "/api/stats").then(r => r.json().then(r => setStats(r as StatsObject)));
    }, []);

    return (
        <table className="glow-table">
            <tbody>
                <tr className="glow-tr">
                    <td className="glow-td">Uptime</td>
                    <td className="glow-td"><Time seconds={stats.uptime / 1000}/></td>
                </tr>
                <tr className="glow-tr">
                    <td className="glow-td">Available commands</td>
                    <td className="glow-td">{stats.commands}</td>
                </tr>
                <tr className="glow-tr">
                    <td className="glow-td">Supported platforms</td>
                    <td className="glow-td">{stats.platforms}</td>
                </tr>
            </tbody>
        </table>
    )
}
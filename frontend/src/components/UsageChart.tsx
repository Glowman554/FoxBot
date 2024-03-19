"use client";

import { useEffect, useState } from "react";
import { Bar, BarChart, CartesianGrid, Legend, Rectangle, ResponsiveContainer, Tooltip, XAxis, YAxis } from "recharts";

type UsageObject = { [key: string]: number };
type UsageChart = { command: string, usage: number }[];


export default function UsageChart(props: { baseUrl: string }) {
    const [chart, setChart] = useState([] as UsageChart);

    useEffect(() => {
        async function update() {
            const req = await fetch(props.baseUrl + "/api/usage");
            const json = await req.json() as UsageObject;

            const newChart: UsageChart = [];
            for (const key of Object.keys(json)) {
                const entry = {
                    command: key,
                    usage: json[key]
                };

                newChart.push(entry);
            }
            setChart(newChart);
        }
        update();
        const interval = setInterval(update, 1000);
        return () => clearInterval(interval);    
    }, []);

    return (
        <ResponsiveContainer width="100%" height="100%">
            <BarChart data={chart}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="command" />
                <YAxis />
                <Tooltip />
                <Legend />
                <Bar dataKey="usage" fill="#777777" />
            </BarChart>
        </ResponsiveContainer>
    );
}
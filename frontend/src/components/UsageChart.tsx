"use client";

import { useEffect, useState } from "react";
import { VictoryChart, VictoryBar, VictoryTheme, VictoryAxis, VictoryContainer } from "victory";

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
        <VictoryChart theme={VictoryTheme.material}>
            <VictoryBar
                data={chart}
                x="command"
                y="usage"
            />
        </VictoryChart>
    )
}
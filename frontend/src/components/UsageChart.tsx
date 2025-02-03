import { SolidApexCharts } from 'solid-apexcharts';
import { createSignal, onCleanup, onMount } from 'solid-js';
import { z } from 'zod';
import { fetchAndValidate } from '../validatedFetch';

const usageObjectSchema = z.record(z.number());
type UsageObject = z.infer<typeof usageObjectSchema>;

export default function () {
    const [timer, setTimer] = createSignal(0);
    const [usage, setUsage] = createSignal<UsageObject>({});

    const refetch = async () => {
        setUsage(await fetchAndValidate(usageObjectSchema, '/api/usage'));
    };

    onMount(() => {
        setTimer(
            setInterval(async () => {
                refetch();
            }, 5000)
        );

        refetch();
    });

    onCleanup(() => {
        clearInterval(timer());
    });

    return (
        <div class="center">
            <div class="-z-10 h-[80dvh] w-3/4 max-sm:w-full">
                <SolidApexCharts
                    width="100%"
                    height="100%"
                    type="bar"
                    options={{
                        xaxis: {
                            categories: Object.keys(usage()),
                        },
                        theme: {
                            mode: 'dark',
                        },
                    }}
                    series={[
                        {
                            data: Object.values(usage()),
                        },
                    ]}
                />
            </div>
        </div>
    );
}

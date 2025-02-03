import Query from '@glowman554/base-components/src/query/Query';
import { fetchAndValidate } from '../validatedFetch';
import Time from './Time';
import { z } from 'zod';

const statisticsSchema = z.object({
    commands: z.number(),
    platforms: z.number(),
    uptime: z.number(),
    prefix: z.string(),
});

export default function () {
    return (
        <Query f={() => fetchAndValidate(statisticsSchema, '/api/stats')}>
            {(statistics) => (
                <div class="center">
                    <div class="m-8 w-2/3 rounded-xl bg-black p-8">
                        <table class="w-full bg-black">
                            <tbody>
                                <tr class="border-b">
                                    <td>Uptime</td>
                                    <td>
                                        <Time seconds={statistics.uptime / 1000} />
                                    </td>
                                </tr>
                                <tr class="border-b">
                                    <td>Available commands</td>
                                    <td>{statistics.commands}</td>
                                </tr>
                                <tr class="border-b">
                                    <td>Supported platforms</td>
                                    <td>{statistics.platforms}</td>
                                </tr>
                                <tr class="border-b">
                                    <td>Command prefix</td>
                                    <td>{statistics.prefix}</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            )}
        </Query>
    );
}

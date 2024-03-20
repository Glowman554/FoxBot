import UsageChart from "@/components/UsageChart";
import { PREFIX } from "@/environment";
import { Metadata } from "next";

export const metadata: Metadata = {
	title: PREFIX + "Usage"
};

export default function Home() {
	return (
        <div className="glow-text" style={{
            height: "70vh"
        }}>
            <UsageChart />
        </div>
	);
}

import { PREFIX } from "@/environment";
import { Metadata } from "next";

export const metadata: Metadata = {
	title: PREFIX + "Home"
};

export default function Home() {
	return (
		<div className="glow-text">
			<h2>Welcome to the website of FoxBot!</h2>
		</div>
	);
}

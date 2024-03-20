import { Shell } from "@/components/Shell";
import { PREFIX } from "@/environment";
import { Metadata } from "next";


export const metadata: Metadata = {
	title: PREFIX + "Shell"
};



export default function Home() {
	return (
		<Shell />
	)
}

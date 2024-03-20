import { Commands } from "@/components/Commands";
import { PREFIX } from "@/environment";
import { Metadata } from "next";


export const metadata: Metadata = {
	title: PREFIX + "Help"
};



export default function Home() {
	return (
        <div className="glow-text">
    		<Commands />
        </div>
	)
}

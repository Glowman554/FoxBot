import { Commands } from "@/components/Commands";
import { HOST_URL, PREFIX, SOCKET_URL } from "@/environment";
import { Metadata } from "next";


export const metadata: Metadata = {
	title: PREFIX + "Help"
};



export default function Home() {
	return (
        <div className="glow-text">
    		<Commands hostUrl={HOST_URL} />
        </div>
	)
}

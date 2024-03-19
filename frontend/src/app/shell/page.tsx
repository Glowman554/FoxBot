import { Shell } from "@/components/Shell";
import { HOST_URL, PREFIX, SOCKET_URL } from "@/environment";
import { Metadata } from "next";


export const metadata: Metadata = {
	title: PREFIX + "Shell"
};



export default function Home() {
	return (
		<Shell socketUrl={SOCKET_URL} hostUrl={HOST_URL} />
	)
}

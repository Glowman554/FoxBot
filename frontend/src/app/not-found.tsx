import { Metadata } from "next";

export const metadata: Metadata = {
	title: "404 - Page not found"
};

export default function Home() {
	return (
		<div className="glow-text">
			<h1>404 - Page not found</h1>
			<p>
				The page you were looking for doesn't exist.
			</p>
			<a href="/" className="underline">Go back home</a>
		</div>
	);
}

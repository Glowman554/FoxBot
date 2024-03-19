import { Navigation } from "@/components/Navigation";
import { JetBrains_Mono } from "next/font/google";
import "./global.css";
import { Footer } from "@/components/Footer";
import { Header } from "@/components/Header";

const font = JetBrains_Mono({ subsets: ["latin"] });

export default function RootLayout({
	children,
}: Readonly<{
	children: React.ReactNode;
}>) {
	return (
		<html lang="en">
			<body className={font.className}>
				<div className="glow-content">
					<Navigation />
					<Header />
					{children}
				</div>
				<Footer />
			</body>
		</html>
	);
}

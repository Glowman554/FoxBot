import { Navigation } from "@/components/Navigation";
import { Inter } from "next/font/google";
import "./global.css";
import { Footer } from "@/components/Footer";
import { Header } from "@/components/Header";

const inter = Inter({ subsets: ["latin"] });

export default function RootLayout({
	children,
}: Readonly<{
	children: React.ReactNode;
}>) {
	return (
		<html lang="en">
			<body className={inter.className}>
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

"use client";

import { useState } from "react";

export function Navigation() {
	const [mainBarClass, setMainBarClass] = useState("glow-bar");

	return (
		<div className={mainBarClass}>
			<a href="/">Home</a>
			<a href="/usage">Usage</a>
			<a href="/shell">Shell</a>
			<a href="/commands">Commands</a>
			
			<a onClick={() => {
				if (mainBarClass == "glow-bar") {
					setMainBarClass("glow-bar responsive");
				} else {
					setMainBarClass("glow-bar");
				}
			}} className="icon">
				<img src="/menu.svg" alt="Menu" style={{width: "2rem"}} />
			</a>
		</div>
	);
}
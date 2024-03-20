export function secondsToDhms(seconds: number): string {
	let d = Math.floor(Number(seconds) / (3600 * 24));
	let h = Math.floor(Number(seconds) % (3600 * 24) / 3600);
	let m = Math.floor(Number(seconds) % 3600 / 60);
	let s = Math.floor(Number(seconds) % 60);

	let dDisplay = d > 0 ? d + (d == 1 ? " day, " : " days, ") : "";
	let hDisplay = h > 0 ? h + (h == 1 ? " hour, " : " hours, ") : "";
	let mDisplay = m > 0 ? m + (m == 1 ? " minute, " : " minutes, ") : "";
	let sDisplay = s > 0 ? s + (s == 1 ? " second" : " seconds") : "";
	return dDisplay + hDisplay + mDisplay + sDisplay;
}



export function Time(props: { seconds: number }) {
    return (
        <>{secondsToDhms(props.seconds)}</>
    );
}
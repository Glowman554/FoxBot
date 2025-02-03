import Navigation, { Entry, Home } from '@glowman554/base-components/src/generic/Navigation';

export default function () {
    return (
        <Navigation>
            <Home href="/">Home</Home>
            <Entry href="/usage">Usage</Entry>
            <Entry href="/shell">Shell</Entry>
            <Entry href="/commands">Commands</Entry>
        </Navigation>
    );
}

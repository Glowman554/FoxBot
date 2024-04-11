import { useReducer, useState } from "react";
import { FromServer, ToServer, processUploadFile } from "./Shell";

interface ArgumentDispatch {
    name: string;
    value: string | number | ToServer.UploadedFile;
};

function messageReducer(state: ToServer.SchemaMessage, newArgument: ArgumentDispatch) {
    const newMessage = {...state};
    newMessage[newArgument.name] = newArgument.value;
	return newMessage;
}

export function SchemaTableContainer(props: {schema: FromServer.Schema, send: (message: ToServer.SchemaMessage) => void}) {
    const [display, setDisplay] = useState(false);
    return (
        <>
            <tr className="glow-tr">
                <td className="glow-td" onClick={() => setDisplay(!display)}>
                    {props.schema.name}
                </td>
            </tr>
            <tr className="glow-tr" style={{ display: display ? undefined : "none" }}>
                <td className="glow-td">
                    <SchemaCommand schema={props.schema} send={props.send} />
                </td>
            </tr>
        </>
    )
}

export function SchemaCommand(props: {schema: FromServer.Schema, send: (message: ToServer.SchemaMessage) => void}) {
    const [message, dispatchArgument] = useReducer(messageReducer, { type: "schemaMessage", schemaCommandName: props.schema.name } as ToServer.SchemaMessage);

    return (
        <div className="glow-schema-field">
            <h2>{props.schema.name}</h2>
            <p>{props.schema.description}</p>

            <table className="glow-table">
            {
                props.schema.arguments.map((argument, i) => <Argument key={i} argument={argument} dispatch={dispatchArgument}/>)
            }
            </table>

            <button onClick={() => {
                for (const argument of props.schema.arguments) {
                    if (!message[argument.name] && !argument.optional) {
                        alert("Missing argument " + argument.name);
                        return;
                    }
                }
                props.send(message);
            }}>Send</button>
        </div>
    );
}

function Argument(props: {argument: FromServer.Argument, dispatch: (argument: ArgumentDispatch) => void}) {
    return (
        <tr className="glow-tr">
            <td className="glow-td">
                {props.argument.description}
            </td>
            <td className="glow-td">
                <ArgumentInput argument={props.argument} dispatch={props.dispatch} />
            </td>
        </tr>
    );
}


function ArgumentInput(props: {argument: FromServer.Argument, dispatch: (argument: ArgumentDispatch) => void}) {
    if (props.argument.options.length != 0) {
        switch (props.argument.type) {
            case "STRING": return (
                <div>
                    {
                        props.argument.options.map((option, i) => <button key={i} onClick={() => props.dispatch({name: props.argument.name, value: option.value})}>{option.name}</button>)
                    }
                </div>
            );
            default: return <p>ERROR: {props.argument.type} does not support options</p>;
        }
    }
    switch (props.argument.type) {
        case "STRING": return <input type="text" onChange={(e) => {props.dispatch({ name: props.argument.name, value: (e.target as any).value as string })}}/>;
        case "INTEGER": return <input type="number" onChange={(e) => {props.dispatch({ name: props.argument.name, value: parseInt((e.target as any).value as string) })}}/>;
        case "ATTACHMENT": return <input type="file" onChange={(e) => {
            if (e.target.files) {
                processUploadFile(e.target.files[0]).then(file => props.dispatch({ name: props.argument.name, value: file }));
            }
        }} />;
        default: return <p>ERROR: {props.argument.type} unknown</p>;
    }
}

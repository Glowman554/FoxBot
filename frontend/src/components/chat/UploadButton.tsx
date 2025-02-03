import type { ToServer } from './types';

export interface Props {
    callback: (files: ToServer.UploadedFile[]) => void;
}

function processUploadFile(file: File): Promise<ToServer.UploadedFile> {
    return new Promise<ToServer.UploadedFile>((resolve, reject) => {
        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = () => resolve({ name: file.name, file: (reader.result as string).split(',').pop() as string });
        reader.onerror = (error) => reject(error);
        reader.onprogress = (progress) => console.log((progress.loaded / progress.total) * 100);
    });
}

export default function (props: Props) {
    let fileInput: HTMLInputElement | undefined;

    const handleFileChange = async () => {
        const files = fileInput?.files;
        if (!files) {
            return;
        }

        const promises: Promise<ToServer.UploadedFile>[] = [];

        for (let i = 0; i < files.length; i++) {
            const file = files.item(i)!;
            promises.push(processUploadFile(file));
        }

        Promise.all(promises).then((f) => {
            props.callback(f);
        });
    };

    return (
        <>
            <button class="button w-1/4 text-center max-sm:w-3/4" type="button" onClick={() => fileInput?.click()}>
                Upload
            </button>
            <input type="file" ref={fileInput} class="hidden" onInput={handleFileChange} />
        </>
    );
}

import { create } from "zustand";
import { fileTypeConvertion } from "../constants";
import axios from "axios";

export const useDownloadStore = create((set) => ({
  downloads: {},
  isOverlayVisible: false,

  startDownload: (fileId, filename, filetype) =>
    set((state) => ({
      downloads: {
        ...state.downloads,
        [fileId]: {
          progress: 0,
          status: "downloading",
          filename,
          filetype,
          startTime: Date.now(),
        },
      },
      isOverlayVisible: true,
    })),

  updateProgress: (fileId, progress) =>
    set((state) => ({
      downloads: {
        ...state.downloads,
        [fileId]: { ...state.downloads[fileId], progress },
      },
    })),

  setStatus: (fileId, status) =>
    set((state) => ({
      downloads: {
        ...state.downloads,
        [fileId]: { ...state.downloads[fileId], status },
      },
    })),

  removeDownload: (fileId) =>
    set((state) => {
      const { [fileId]: _, ...remaining } = state.downloads;
      const stillHasDownload = Object.values(remaining) > 0;
      return { downloads: remaining, isOverlayVisible: stillHasDownload };
    }),

  toggleOverlay: () =>
    set((state) => ({
      isOverlayVisible: !state.isOverlayVisible,
    })),

  minimizeOverlay: () => set({ isOverlayVisible: false }),

  maximiazeOverlay: () => set({ isOverlayVisible: true }),
}));

const getFiletype = (fileType) => {
  if (fileTypeConvertion.hasOwnProperty(fileType)) {
    return fileTypeConvertion[fileType];
  } else {
    return fileType;
  }
};

export const downloadFileWithProgress = async (
  fileId,
  filename,
  filetype,
  filesize
) => {
  const downloadStore = useDownloadStore.getState();
  downloadStore.startDownload(fileId, filename, filetype);
  try {
    const response = await axios.get(
      `http://localhost:8080/files/download-file/${fileId}/${filetype}`
    );
    const urls = response.data;

    if (!window.showSaveFilePicker) {
      alert("your browser does not support direct file saving");
    }

    filetype = getFiletype(filetype);
    const fileHandle = await window.showSaveFilePicker({
      suggestedName: filename + "." + filetype,
      types: [
        {
          description: filetype + "File",
          accept: { [`application/${filetype}`]: ["." + filetype] },
        },
      ],
    });

    const writableStream = await fileHandle.createWritable();
    const totalUrls = urls.length;
    let processedUrls = 0;
    for (const url of urls) {
      const response = await fetch(url);
      const reader = response.body.getReader();
      let done = false;
      processedUrls += 1;
      while (!done) {
        const { value, done: readerDone } = await reader.read();
        if (value) {
          await writableStream.write(value);
          const progressPresent = Math.round((processedUrls / totalUrls) * 100);
          downloadStore.updateProgress(fileId, progressPresent);
        }
        done = readerDone;
      }
    }

    await writableStream.close();
    downloadStore.setStatus(fileId, "complete");

    // const totalSize = filesize;

    // const writableStream = await fileHandle.createWritable();
    // const reader = response.body.getReader();

    // let receivedSize = 0;

    // while (true) {
    //   const { done, value } = await reader.read();
    //   if (done) break;

    //   receivedSize += value.length;

    //   await writableStream.write(value);
    //   if (totalSize) {
    //     const progressPresent = Math.round((receivedSize / totalSize) * 100);
    //     downloadStore.updateProgress(fileId, progressPresent);
    //   } else {
    //     downloadStore.updateProgress(fileId, receivedSize);
    //   }
    // }

    // await writableStream.close();

    return true;
  } catch (error) {
    downloadStore.setStatus(fileId, "failed");
    setTimeout(() => {
      downloadStore.removeDownload(fileId);
    }, 5000);
    throw error;
  }
};

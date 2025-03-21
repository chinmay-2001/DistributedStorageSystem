import React, { useState, useContext } from "react";
import axios from "axios";
import AppContext from "../Context/Context";
import { FileOperationService } from "../APIServices/FileOperationService";
import { FileMetadataService } from "../APIServices/FileApiServices";

function FileUploader({ setFiles, files }) {
  const [selectedFile, setSelectedFile] = useState(null);
  const CHUNK_SIZE = 5 * 1024 * 1024; // 10MB
  const MAX_RETRIES = 1;
  const CONCURRENCY_LIMIT = 5;

  const {
    userInfo: { id },
  } = useContext(AppContext);

  const handleFileChange = async (event) => {
    if (event.target.files[0]) {
      await uploadFile(event.target.files[0]);
    }
  };

  const uploadChunk = async (chunk, chunkIndex, fileId, attempt = 1) => {
    try {
      await FileOperationService.uploadFile({ fileId, chunkIndex, chunk });

      console.log(`✅ Chunk ${chunkIndex} uploaded successfully`);
      return true;
    } catch (error) {
      console.warn(`⚠️ Chunk ${chunkIndex} failed (Attempt ${attempt})`, error);
      if (attempt < MAX_RETRIES) {
        const delay = Math.pow(2, attempt - 1) * 1000; // Exponential backoff delay
        console.log(`Retrying chunk ${chunkIndex} in ${delay / 1000}s...`);
        await new Promise((res) => setTimeout(res, delay));

        return uploadChunk(chunk, chunkIndex, fileId, attempt + 1);
      } else {
        console.error(
          `❌ Chunk ${chunkIndex} failed after ${MAX_RETRIES} attempts.`
        );
        return false;
      }
    }
  };

  const uploadFile = async (file) => {
    let chunks = [];
    let start = 0;

    console.log(file);
    const fileSize = file.size;
    const totalChunks = Math.ceil(fileSize / CHUNK_SIZE);

    const metadata = {
      userId: id,
      filename: file.name,
      totalchunk: totalChunks,
      fileType: file.type.split("/")[1],
      fileSize,
    };
    const newFile = await FileMetadataService.createFileMetadata(metadata);
    const fileId = newFile.id;
    setFiles([...files, newFile]);

    if (fileId) {
      while (start <= file.size) {
        chunks.push(file.slice(start, start + CHUNK_SIZE));
        start += CHUNK_SIZE;
      }

      let index = 0;
      let failedChunks = [];

      const uploadNextBatch = async () => {
        while (index <= chunks.length) {
          const batch = chunks.slice(index, index + CONCURRENCY_LIMIT);
          const chunkIndexes = Array.from(
            { length: batch.length },
            (_, i) => index + i
          );
          index += CONCURRENCY_LIMIT;
          const results = await Promise.all(
            batch.map((chunk, index) =>
              uploadChunk(chunk, chunkIndexes[index], fileId)
            )
          );

          results.forEach((success, i) => {
            if (!success) failedChunks.push(chunkIndexes[i]);
          });
        }
      };

      await uploadNextBatch();

      if (failedChunks.length == 0) {
        await FileOperationService.conformUpload(fileId);
      }
    }
  };

  const handleUploadClick = () => {
    document.getElementById("fileInput").click();
  };

  return (
    <div className="file-uploader">
      <input
        type="file"
        id="fileInput"
        style={{ display: "none" }}
        onChange={handleFileChange}
      />
      <button onClick={handleUploadClick} className="btn btn-primary">
        Upload File
      </button>
      {selectedFile && <p>Selected: {selectedFile.name}</p>}
    </div>
  );
}

export default FileUploader;

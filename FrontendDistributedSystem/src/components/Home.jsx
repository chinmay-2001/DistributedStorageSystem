import React, { useEffect, useState, useContext } from "react";
import AppContext from "../Context/Context";
import NavbarComponent from "./Navbar";
import FileTable from "./FileTable";
import "../styles/Home.css";
import axios from "axios";

function Home() {
  const [files, setFiles] = useState([]);
  const { userInfo } = useContext(AppContext);

  const handleDownload = async (fileId, filetype) => {
    const response = await fetch(
      `http://localhost:8080/files/download-file/${fileId}/${filetype}`
    );

    const fileHandle = await window.showSaveFilePicker({
      suggestedName: "downloaded-file" + "." + filetype,
      types: [
        {
          description: filetype + "File",
          accept: { [`application/${filetype}`]: ["." + filetype] },
        },
      ],
    });

    const writableStream = await fileHandle.createWritable();
    const reader = response.body.getReader();

    while (true) {
      const { done, value } = await reader.read();
      if (done) break;
      await writableStream.write(value); // Directly write to disk (No RAM storage)
    }

    await writableStream.close();
  };

  const handleDelete = (file) => {
    setFiles(files.filter((f) => f.name !== file.name));
  };

  useEffect(() => {
    const fetchFiles = async () => {
      const { data } = await axios.get(
        `http://localhost:8080/files/file-metadata/${userInfo.id}`
      );
      setFiles(data);
    };

    fetchFiles();
  }, []);

  return (
    <div className="home-container">
      <NavbarComponent setFiles={setFiles} files={files} />
      <div className="content-box">
        <FileTable
          files={files}
          onDownload={handleDownload}
          onDelete={handleDelete}
        />
      </div>
    </div>
  );
}

export default Home;

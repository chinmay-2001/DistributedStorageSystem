import React, { useEffect, useState, useContext } from "react";
import AppContext from "../Context/Context";
import NavbarComponent from "./Navbar";
import FileTable from "./FileTable";
import "../styles/Home.css";
import axios from "axios";
import { FileMetadataService } from "../APIServices/FileApiServices";

function Home() {
  const [files, setFiles] = useState([]);
  const { userInfo } = useContext(AppContext);

  const handleDownload = async (fileId, filetype) => {
    try {
      await downloadWithProgress(fileId, filetype);
    } catch (error) {
      console.error("Download Failed", error);
    }
  };

  const handleDelete = (file) => {
    setFiles(files.filter((f) => f.name !== file.name));
  };

  useEffect(() => {
    const fetchFiles = async () => {
      const data = await FileMetadataService.getFilemetaDataByUserId(
        userInfo.id
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

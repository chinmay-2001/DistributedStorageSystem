import React, { useEffect, useState, useContext } from "react";
import AppContext from "../Context/Context";
import NavbarComponent from "./Navbar";
import FileTable from "./FileTable";
import "../styles/Home.css";
import axios from "axios";

function Home() {
  const [files, setFiles] = useState([]);
  const { userInfo } = useContext(AppContext);

  const handleDownload = (file) => {
    alert(`Downloading: ${file.name}`);
  };

  const handleDelete = (file) => {
    setFiles(files.filter((f) => f.name !== file.name));
  };

  useEffect(() => {
    const fetchFiles = async () => {
      const response = await axios.get(
        `http://localhost:8080/files/file-metadata/${userInfo.id}`
      );
      console.log(response);
    };

    fetchFiles();
  }, []);

  return (
    <div className="home-container">
      <NavbarComponent />
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

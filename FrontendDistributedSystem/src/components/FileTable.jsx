import React, { useState } from "react";
import { Modal, Button } from "react-bootstrap";
import "bootstrap/dist/css/bootstrap.min.css";
import FileDetailsModal from "./FileDetailModal";
import { downloadFileWithProgress } from "../Utils.js/Download";

const FileTable = ({ files, onDelete }) => {
  const [showModal, setShowModal] = useState(false);
  const [selectedFile, setSelectedFile] = useState(null);

  const handleDetails = (file) => {
    setSelectedFile(file);
    setShowModal(true);
  };

  const onDownload = async (fileId, filetype, filename, filesize) => {
    try {
      await downloadFileWithProgress(fileId, filename, filetype, filesize);
    } catch (error) {
      console.error(error);
    }
  };

  return (
    <div className="container mt-4">
      <h2>File List</h2>
      <table className="table table-striped table-bordered">
        <thead className="table-dark">
          <tr>
            <th>Filename</th>
            <th>Download</th>
            <th>Delete</th>
            <th>Details</th>
          </tr>
        </thead>
        <tbody>
          {files && files?.length > 0 ? (
            files.map((file, index) => (
              <tr key={index}>
                <td>{file.filename}</td>
                <td>
                  <button
                    className="btn btn-success"
                    onClick={() =>
                      onDownload(
                        file.id,
                        file.filetype,
                        file.filename,
                        file.filesize
                      )
                    }
                  >
                    Download
                  </button>
                </td>
                <td>
                  <button
                    className="btn btn-danger"
                    onClick={() => onDelete(file)}
                  >
                    Delete
                  </button>
                </td>
                <td>
                  <button
                    className="btn btn-info"
                    onClick={() => handleDetails(file)}
                  >
                    Details
                  </button>
                </td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan="4" className="text-center">
                No files available
              </td>
            </tr>
          )}
        </tbody>
      </table>
      <FileDetailsModal
        show={showModal}
        onClose={() => setShowModal(false)}
        file={selectedFile}
      />
    </div>
  );
};

export default FileTable;

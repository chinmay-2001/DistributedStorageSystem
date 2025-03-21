import React from "react";
import { Modal, Button } from "react-bootstrap";

const FileDetailsModal = ({ show, onClose, file }) => {
  if (!file) return null;

  return (
    <Modal show={show} onHide={onClose}>
      <Modal.Header closeButton>
        <Modal.Title>File Details</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <p>
          <strong>Filename:</strong> {file.filename}
        </p>
        <p>
          <strong>File Type:</strong> {file.filetype}
        </p>
        <p>
          <strong>File Size:</strong> {formatFileSize(file.fileSize)}
        </p>
        <p>
          <strong>Uploaded At:</strong> {file.createdAt}
        </p>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={onClose}>
          Close
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

const formatFileSize = (bytes) => {
  if (bytes < 1024) return bytes + " B";
  else if (bytes < 1048576) return (bytes / 1024).toFixed(2) + " KB";
  else if (bytes < 1073741824) return (bytes / 1048576).toFixed(2) + " MB";
  else return (bytes / 1073741824).toFixed(2) + " GB";
};

export default FileDetailsModal;

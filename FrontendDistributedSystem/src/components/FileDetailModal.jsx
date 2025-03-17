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
          <strong>File Size:</strong> {file.fileSize} MB
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

export default FileDetailsModal;

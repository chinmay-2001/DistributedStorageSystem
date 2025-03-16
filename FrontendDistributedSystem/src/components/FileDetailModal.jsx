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
          <strong>Filename:</strong> {file.name}
        </p>
        <p>
          <strong>File Type:</strong> {file.type}
        </p>
        <p>
          <strong>File Size:</strong> {file.size} MB
        </p>
        <p>
          <strong>Uploaded On:</strong> {file.uploadDate}
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

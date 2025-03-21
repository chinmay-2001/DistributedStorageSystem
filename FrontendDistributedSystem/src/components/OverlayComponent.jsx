import React from "react";
import { useDownloadStore } from "../Utils.js/Download";
import "../styles/Overlay.css";

function OverlayComponent() {
  const { downloads, isOverlayVisible, minimizeOverlay, maximizeOverlay } =
    useDownloadStore();

  if (Object.keys(downloads).length === 0) {
    return null;
  }

  const formatTime = (startTime) => {
    const elapsed = Math.floor((Date.now() - startTime) / 1000);
    const minutes = Math.floor(elapsed / 60);
    const seconds = elapsed % 60;
    return `${minutes}:${seconds.toString().padStart(2, "0")}`;
  };

  return (
    <div
      className={`download-overlay ${
        isOverlayVisible ? "expanded" : "minimized"
      }`}
    >
      <div className="download-overlay-header">
        <span>Downloads ({Object.keys(downloads).length})</span>
        <div className="download-overlay-controls">
          {isOverlayVisible ? (
            <button className="minimize-button" onClick={minimizeOverlay}>
              −
            </button>
          ) : (
            <button className="maximize-button" onClick={maximizeOverlay}>
              ↑
            </button>
          )}
        </div>
      </div>

      {isOverlayVisible && (
        <div className="download-items-container">
          {Object.entries(downloads).map(([fileId, download]) => (
            <div key={fileId} className="download-item">
              <div className="download-item-info">
                <div className="download-item-name">
                  {download.filename}.{download.filetype}
                </div>
                <div className="download-item-meta">
                  {download.status === "downloading" && (
                    <span className="download-time">
                      {formatTime(download.startTime)}
                    </span>
                  )}
                  <span className={`download-status ${download.status}`}>
                    {download.status === "downloading" &&
                      `${download.progress}%`}
                    {download.status === "complete" && "Complete"}
                    {download.status === "failed" && "Failed"}
                  </span>
                </div>
              </div>
              <div className="download-progress-container">
                <div
                  className={`download-progress-bar ${download.status}`}
                  style={{ width: `${download.progress}%` }}
                />
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

export default OverlayComponent;

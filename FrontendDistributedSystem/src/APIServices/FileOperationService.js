import { apiClient } from "../CreateApi";

export const FileOperationService = {
  /**
   * Download file
   * @param {string} fileId - File ID
   * @param {string} filetype - File type/extension
   * @returns {Promise<Response>} - Fetch API Response object
   */
  downloadFile: async (fileId, filetype) => {
    try {
      return await apiClient.get(`/files/download-file/${fileId}/${filetype}`);
    } catch (error) {
      console.error(`Failed to download File:${error.message}`);
    }
  },

  /**
   * Upload file chunk
   * @param {Object} chunkData - Chunk data
   * @param {string} chunkData.fileId - File ID
   * @param {number} chunkData.chunkIndex - Chunk index
   * @param {Blob} chunkData.chunk - Chunk data blob
   * @returns {Promise<Object>} - Upload result
   */
  uploadFile: async ({ fileId, chunkIndex, chunk }) => {
    try {
      const formData = new FormData();
      formData.append("file", chunk, `chunk-${chunkIndex}`);
      formData.append("fileId", fileId);
      formData.append("chunkIndex", chunkIndex);

      const response = await apiClient.post(`/files/chunk-upload`, formData, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      });
      return response.data;
    } catch (error) {
      throw new Error(`Failed to upload chunk ${chunkIndex}: ${error.message}`);
    }
  },

  conformUpload: async (fileId) => {
    try {
      await apiClient.patch(`/files/confirm-upload/${fileId}`, {});
    } catch (error) {
      throw new Error(`Failed to conform Upload:${error.message}`);
    }
  },
};

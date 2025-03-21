import axios from "axios";
import { apiClient } from "../CreateApi";

export const FileMetadataService = {
  /**
   * Create file metadata record
   * @param {Object} metadata - File metadata object
   * @param {string} metadata.userId - User ID
   * @param {string} metadata.filename - Original filename
   * @param {number} metadata.totalchunk - Total number of chunks
   * @param {string} metadata.fileType - File type/extension
   * @param {number} metadata.fileSize - File size in bytes
   * @returns {Promise<Object>} - Created file metadata
   */

  createFileMetadata: async (metadata) => {
    try {
      const response = await apiClient.post("/files/FileMetadata", metadata);
      return response?.data || {};
    } catch (error) {
      throw new Error(`Filed to create File Metadata: ${error.message}`);
    }
  },

  getFilemetaDataByUserId: async (userId) => {
    try {
      const response = await apiClient.get(`/files/file-metadata/${userId}`);
      return response.data;
    } catch (error) {
      throw new Error(`File to get File Metadata: ${error.message}`);
    }
  },

  /**
   * Delete file metadata
   * @param {string} fileId - File ID to delete
   * @returns {Promise<Object>} - Deletion result
   */
  deleteFileMetadata: async (fileId) => {
    try {
      const response = await apiClient.delete(`/files/file-metadata/${fileId}`);
      return response.data;
    } catch (error) {
      throw new Error(`Failed to delete file: ${error.message}`);
    }
  },
};

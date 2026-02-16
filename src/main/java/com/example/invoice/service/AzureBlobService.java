package com.example.invoice.service;

import com.azure.storage.blob.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;

@Service
public class AzureBlobService {

    @Value("${AZURE_STORAGE_CONNECTION_STRING}")
    private String connectionString;

    @Value("${AZURE_STORAGE_CONTAINER_NAME}")
    private String containerName;

    public String upload(byte[] data, String fileName) {

        // Clean values
        String cleanContainer = containerName.trim().toLowerCase();
        String cleanConnection = connectionString.trim();

        // 🔹 Debug logs (safe)
        System.out.println("==== AZURE DEBUG START ====");
        System.out.println("Container name: [" + cleanContainer + "]");
        System.out.println("File name: [" + fileName + "]");
        System.out.println("Connection string length: " + cleanConnection.length());
        System.out.println("==== AZURE DEBUG END ====");

        BlobServiceClient serviceClient =
                new BlobServiceClientBuilder()
                        .connectionString(cleanConnection)
                        .buildClient();

        BlobContainerClient containerClient =
                serviceClient.getBlobContainerClient(cleanContainer);

        // Check if container exists
        if (!containerClient.exists()) {
            throw new RuntimeException("Container does NOT exist: " + cleanContainer);
        }

        BlobClient blobClient =
                containerClient.getBlobClient(fileName);

        blobClient.upload(new ByteArrayInputStream(data), data.length, true);

        String blobUrl = blobClient.getBlobUrl();

        // 🔹 Log final URL
        System.out.println("Uploaded successfully to: " + blobUrl);

        return blobUrl;
    }
}

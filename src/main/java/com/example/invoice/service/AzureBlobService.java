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

        BlobServiceClient serviceClient =
                new BlobServiceClientBuilder()
                        .connectionString(connectionString)
                        .buildClient();

        BlobContainerClient containerClient =
                serviceClient.getBlobContainerClient(containerName);

        BlobClient blobClient =
                containerClient.getBlobClient(fileName);

        blobClient.upload(new ByteArrayInputStream(data), data.length, true);

        return blobClient.getBlobUrl();
    }
}

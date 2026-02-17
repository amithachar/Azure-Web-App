<img width="1024" height="1024" alt="image" src="https://github.com/user-attachments/assets/6cf2ba1e-d2fa-4f84-aaa7-6fe2d98c4b3b" />


# 📑 Stateless Invoice Generation System

A cloud-native, stateless architecture built on **Azure** using **Spring Boot** to generate and manage invoices without local storage overhead.

---

## 🏗️ Architecture Overview

### 1️⃣ User Layer (Frontend)
**Browser → HTML UI**

The user interacts with the interface to trigger the following actions:
* **Enters** customer name and amount.
* **Clicks** “Generate Invoice”.
* **Dispatches** a request to: `POST /api/invoices`

### 2️⃣ Application Layer (Azure App Service)
**Spring Boot Application** hosted on **Azure App Service**.

Inside the application, the logic is decoupled into specific components:
* **Controller:** Handles incoming REST requests.
* **InvoiceService:** Orchestrates the business logic.
* **PdfService:** Manages in-memory PDF generation (using **iText**).
* **AzureBlobService:** Handles communication with Azure Storage.
* **InvoiceRepository:** Interacts with the database.

**Key Logic Flow:**
1. Receive request.
2. Generate unique **Invoice Number**.
3. Generate **PDF in-memory**.
4. **Upload** PDF to Blob Storage.
5. **Store** invoice record in Azure SQL.
6. Return response to UI.

### 3️⃣ Database Layer (Azure SQL Database)
Stores **structured metadata** only. This is your system of record.

**Invoice Table Schema:**
| Column | Description |
| :--- | :--- |
| `id` | Primary Key |
| `invoiceNumber` | Unique Identifier |
| `customerName` | String |
| `amount` | Decimal/Float |
| `blobUrl` | **Reference link to storage** |
| `createdAt` | Timestamp |

> ⚠️ **Note:** Blob storage is not your database. It stores files; SQL stores metadata. This separation is critical for performance and scalability.

### 4️⃣ Storage Layer (Azure Blob Storage)
Acts as the **Cloud File System**.
* **Stores:** `invoice-101.pdf`, `invoice-102.pdf`, etc.
* **Example URL:** `https://<account>.blob.core.windows.net/invoices/invoice-101.pdf`

---

## 🔄 Step-by-Step Execution Flow



1. **User submits form**
2. **Spring Boot receives request**
3. **PDF is generated in memory** (No local disk usage)
4. **PDF uploaded to Blob Storage**
5. **Blob URL returned** to application
6. **Invoice metadata saved in SQL** (including the URL)
7. **Response returned to UI**
8. **User clicks download**
9. **Browser fetches file directly from Blob Storage**

**Result:** No file ever lives permanently inside the App Service. This is **Clean Stateless Architecture**.

---

## 🏛️ Logical Layering (Enterprise Design)

The system follows a standard **N-Tier enterprise design**:

**Presentation Layer** (UI)
  ↓
**Controller Layer** (API Endpoints)
  ↓
**Service Layer** (Business Logic)
  ↓
**Persistence Layer** (Data Access)
  ↓
**Infrastructure Layer** (Azure SQL + Azure Blob Storage)



* steps

  # 📘 INVOICE SYSTEM – COMPLETE CREATION & EXECUTION DOCUMENTATION

This document provides a step-by-step guide to deploying and executing the cloud-native Invoice System on Azure.

---

## 🔹 PHASE 1 — Azure Resource Creation

### Step 1 — Create Resource Group
**Azure Portal** → **Resource Groups** → **Create**
* **Name:** `invoice-rg`
* **Region:** (Use the same region for all resources)
* *Note: This keeps everything logically grouped.*

### Step 2 — Create Azure SQL Database
**Azure Portal** → **SQL Databases** → **Create**
* **Database name:** `invoice-db`
* **Server:** Create new
* **Authentication:** SQL Authentication
* **Important:** Save your **Server name**, **Username**, and **Password**.
* **Networking:** * Enable: `Allow Azure services and resources to access this server`

### Step 3 — Create Storage Account
**Azure Portal** → **Storage Accounts** → **Create**
* **Name:** `invoiceblobstore`
* **Performance:** Standard
* **Redundancy:** LRS
* **Container Creation:** After creation, go to **Containers** → **+ Container**
    * **Name:** `invoices`
    * **Access level:** Private

### Step 4 — Create App Service
**Azure Portal** → **App Services** → **Create**
* **Name:** `invoice-backend`
* **Runtime:** `Java 17`
* **OS:** `Linux`
* **Publish:** `Code`
* **Pricing tier:** `Basic`

---

## 🔹 PHASE 2 — Important Connections

### 1️⃣ SQL Connection String
Navigate to **SQL Database** → **Connection Strings** → **JDBC**.
In **App Service Settings** → **Environment Variables**, add the following:
* `SPRING_DATASOURCE_URL`
* `SPRING_DATASOURCE_USERNAME`
* `SPRING_DATASOURCE_PASSWORD`

> ⚠️ **Pro Tip:** Never hardcode credentials in `application.properties` in production.

### 2️⃣ Storage Connection String
Navigate to **Storage Account** → **Access Keys** → **Show Keys**.
In **App Service** → **Environment Variables**, add the following:
* `AZURE_STORAGE_CONNECTION_STRING`
* `AZURE_STORAGE_CONTAINER_NAME` = `invoices`

*Note: Restart App Service after saving these variables.*

---

## 🔹 PHASE 3 — Application Architecture



**The Flow:**
**Browser** → **App Service (Spring Boot)** → **Azure SQL** (Metadata) + **Azure Blob** (PDF File)

**Separation of Responsibilities:**
* **Azure SQL:** Structured data & Metadata.
* **Blob Storage:** Physical file storage.
* **App Service:** Stateless business logic.

---

## 🔹 PHASE 4 — Local Development Execution

1. **Clone Project**
   ```bash
   git clone <repo-url>
   cd invoice-app

## 🏗️ Phase 4 — Local Development & Execution

Before deploying to the cloud, ensure your local environment is configured correctly for testing.

### 1. Configure `application.properties` (Local Only)
Set your local environment variables or update the properties file. Use your Azure SQL (with your local IP whitelisted) and Storage Connection Strings.

```properties
# Database Configuration
spring.datasource.url=jdbc:sqlserver://<server>.database.windows.net:1433;database=invoice-db
spring.datasource.username=<your-username>
spring.datasource.password=<your-password>

# Azure Blob Storage Configuration
azure.storage.connection-string=<your-connection-string>
azure.storage.container-name=invoices

```
##  App Service.
<img width="1881" height="847" alt="image" src="https://github.com/user-attachments/assets/8bf60130-3196-47d4-9fe8-e81bdcdb6356" />

##  App UI.
<img width="1896" height="953" alt="image" src="https://github.com/user-attachments/assets/bfc75496-21c2-4bcf-b556-36be475f8572" />

## App Storage.
<img width="1863" height="841" alt="image" src="https://github.com/user-attachments/assets/f1a60a6a-2391-4779-b077-6bb144d6d41a" />

## App SQL Server.
<img width="1895" height="731" alt="image" src="https://github.com/user-attachments/assets/fa6eef9b-c2a9-4065-a638-f0808f8cbb30" />

## App SQL Server Database.

<img width="1898" height="898" alt="image" src="https://github.com/user-attachments/assets/8a8fa4fd-0d7d-4ffe-a5f2-9f4077e88cfd" />


## Resource visualizer
![invoice-rg](https://github.com/user-attachments/assets/8a20f2b7-5e8c-41ec-a4c7-32e2ceafdd2b)


## Adding extra column in current 

```
SELECT * FROM invoices;

ALTER TABLE invoices
ADD invoice_date DATE,
    po_number VARCHAR(100);
```
## Environment variables
<img width="1491" height="723" alt="image" src="https://github.com/user-attachments/assets/0177893e-88af-41d6-b648-96af87246b9b" />

## Azure Application & Database Configuration

```
APPLICATIONINSIGHTS_CONNECTION_STRING

AZURE_STORAGE_CONNECTION_STRING

AZURE_STORAGE_CONTAINER_NAME

spring.datasource.password

spring.datasource.url

spring.datasource.username
```

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

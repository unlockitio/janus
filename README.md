# Distributed Storage Architecture

A Spring Boot application that provides a unified storage abstraction layer supporting multiple storage backends (AWS S3, IPFS via Pinata, and IPFS via Kubo). Organizations can configure their own storage backends, enabling hybrid setups where different entities rely on different storage providers.

## Architecture

```
StorageController (unified REST API)
        │
    StorageService (routing layer)
        │
        ├── S3Service         → AWS S3
        ├── PinataService     → IPFS via Pinata (hosted)
        └── KuboService       → IPFS via Kubo (self-managed)
```

Each organization can configure one or more storage backends. When a file operation is performed, the system routes to the appropriate provider(s) based on the organization's configuration.

## Prerequisites

- Java 21+
- Maven 3.8+
- AWS Account (for S3 provider)
- Pinata Account (for hosted IPFS provider)
- Kubo/IPFS (for self-managed IPFS node, optional)
- Docker & Docker Compose (optional, for containerized deployment)

## Configuration

### Application Properties

Add the following to `src/main/resources/application.properties`:

```properties
spring.application.name=s3learning

# H2 Database (file-based persistence)
spring.datasource.url=jdbc:h2:file:./data/storage
spring.jpa.hibernate.ddl-auto=update

# Pinata JWT (default, can be overridden per-org)
jwt=your_pinata_jwt_token_here
```

### AWS S3 Setup

1. Create an AWS account and an S3 bucket
2. Generate an IAM access key with S3 permissions
3. The access key and secret are stored per-organization in the database via the config API

### Pinata (Hosted IPFS) Setup

1. Sign up at [pinata.cloud](https://pinata.cloud)
2. Go to API Keys → create a new key with `pinFileToIPFS` permission
3. Copy the JWT token
4. Store the JWT per-organization via the config API

### IPFS Node (Kubo) Setup

1. Download Kubo from [github.com/ipfs/kubo/releases](https://github.com/ipfs/kubo/releases)
2. Initialize the node:
   ```
   ipfs init
   ```
3. Start the daemon:
   ```
   ipfs daemon
   ```
4. The Kubo API will be available at `http://localhost:5001`

## Running the Application

### Local

```bash
mvn spring-boot:run
```

The application starts on `http://localhost:8080`.

### Docker

```bash
docker-compose up --build
```

This starts the Spring Boot application and a Kubo IPFS node.

## API Endpoints

### Organization Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /org/upload | Create a new organization |
| GET | /orgs | List all organizations |
| DELETE | /org/delete/{orgid} | Delete an organization |

Create an organization:
```bash
curl -X POST http://localhost:8080/org/upload \
  -H "Content-Type: application/json" \
  -d '{"name": "OrgA"}'
```

### Storage Configuration

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /orgs/{orgId}/createconfig | Add a storage backend to an organization |

Add S3 backend:
```bash
curl -X POST http://localhost:8080/orgs/1/createconfig \
  -H "Content-Type: application/json" \
  -d '{"servicename": "s3", "credentials": "your_aws_access_key"}'
```

Add IPFS (Pinata) backend:
```bash
curl -X POST http://localhost:8080/orgs/1/createconfig \
  -H "Content-Type: application/json" \
  -d '{"servicename": "ipfs", "credentials": "your_pinata_jwt"}'
```

Add IPFS (Kubo) backend:
```bash
curl -X POST http://localhost:8080/orgs/1/createconfig \
  -H "Content-Type: application/json" \
  -d '{"servicename": "kubo"}'
```

### File Operations

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /upload/{orgId} | Upload a file |
| DELETE | /delete/{orgId}/{id} | Delete a file |
| GET | /list/{orgId} | List all files |

Upload a file:
```bash
curl -X POST http://localhost:8080/upload/1 \
  -F "file=@/path/to/file.pdf"
```

The upload endpoint returns identifiers from all configured backends:
```json
[
  "file.pdf",
  "QmceDFJtDgH8R154MCYgjG9izbATLoC7M5mQ6vpjE8wsXB"
]
```

## Project Structure

```
src/main/java/org/example/s3learning/
├── kubo/
│   ├── kuboconfig.java            # Kubo HTTP client configuration
│   └── kuboservice.java           # Kubo IPFS provider
├── orgs/
│   ├── orgconfig.java             # Org storage config repository wrapper
│   ├── orgcontroller.java         # Organization management API
│   ├── orgentity.java             # Organization table
│   ├── orgsconfigentity.java      # Per-org storage backend config
│   ├── orgservice.java            # Organization CRUD operations
│   └── orgsrep.java               # Organization data access
├── pinata/
│   ├── pinataconfig.java          # Pinata WebClient configuration
│   └── pinataservice.java         # Pinata IPFS provider
├── s3/
│   ├── s3coonfig.java             # AWS S3 client configuration
│   └── s3service.java             # AWS S3 provider
├── storage/
│   ├── storagecontroller.java     # Unified file operations API
│   ├── storageinterface.java      # Common interface: upload, delete, list
│   └── storageservice.java        # Routing layer (loops through org backends)
└── S3learningApplication.java     # Main application entry point
```

## Storage Providers

| Provider | Type | Backend | Identifier |
|----------|------|---------|------------|
| S3Service | Cloud storage | AWS S3 | S3 object key (filename) |
| PinataService | Hosted IPFS | Pinata API | CID (content hash) |
| KuboService | Self-managed IPFS | Kubo node | CID (content hash) |

## How Routing Works

1. An organization is created via the API
2. Storage backends are configured for that organization (S3, IPFS, or both)
3. When a file operation is requested with an `orgId`, the system looks up the org's configured backends
4. The operation is executed on all configured backends (redundancy support)

## Technology Stack

- Java 21
- Spring Boot 4.0.6
- Spring WebFlux (WebClient for Pinata/Kubo HTTP calls)
- AWS SDK v2 (S3 integration)
- H2 Database (organization and config persistence)
- Docker & Docker Compose (containerized deployment)

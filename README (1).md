Distributed Storage Architecture
A Spring Boot application that provides a unified storage abstraction layer supporting multiple storage backends (AWS S3, IPFS via Pinata, and IPFS via Kubo). Organizations can configure their own storage backends, enabling hybrid setups where different entities rely on different storage providers.
Architecture
StorageController (unified REST API)

        │

    StorageService (routing layer)

        │

        ├── S3Service         → AWS S3

        ├── PinataService     → IPFS via Pinata (hosted)

        └── KuboService       → IPFS via Kubo (self-managed)

Each organization can configure one or more storage backends. When a file operation is performed, the system routes to the appropriate provider(s) based on the organization's configuration.
Prerequisites
Java 21+
Maven 3.8+
AWS Account (for S3 provider)
Pinata Account (for hosted IPFS provider)
Kubo/IPFS (for self-managed IPFS node, optional)
Docker & Docker Compose (optional, for containerized deployment)
Configuration
Application Properties
Add the following to src/main/resources/application.properties:

spring.application.name=s3learning

# H2 Database (file-based persistence)

spring.datasource.url=jdbc:h2:file:./data/storage

spring.jpa.hibernate.ddl-auto=update

# Pinata JWT (default, can be overridden per-org)

jwt=your_pinata_jwt_token_here
AWS S3 Setup
Create an AWS account and an S3 bucket
Generate an IAM access key with S3 permissions
The access key and secret are stored per-organization in the database via the config API
Pinata (Hosted IPFS) Setup
Sign up at pinata.cloud
Go to API Keys → create a new key with pinFileToIPFS permission
Copy the JWT token
Store the JWT per-organization via the config API
IPFS Node (Kubo) Setup
Download Kubo from github.com/ipfs/kubo/releases
Initialize the node:

ipfs init

Start the daemon:

ipfs daemon

The Kubo API will be available at http://localhost:5001
Running the Application
Local
mvn spring-boot:run

The application starts on http://localhost:8080.
Docker
docker-compose up --build

This starts the Spring Boot application and a Kubo IPFS node.
API Endpoints
Organization Management
Method
Endpoint
Description
POST
/org/upload
Create a new organization
GET
/orgs
List all organizations
DELETE
/org/delete/{id}
Delete an organization


Create an organization:

curl -X POST http://localhost:8080/org/upload \

  -H "Content-Type: application/json" \

  -d '{"name": "OrgA"}'
Storage Configuration
Method
Endpoint
Description
POST
/orgs/{orgId}/createconfig
Add a storage backend to an organization


Add S3 backend:

curl -X POST http://localhost:8080/orgs/1/createconfig \

  -H "Content-Type: application/json" \

  -d '{"servicename": "s3", "credentials": "your_aws_access_key"}'

Add IPFS (Pinata) backend:

curl -X POST http://localhost:8080/orgs/1/createconfig \

  -H "Content-Type: application/json" \

  -d '{"servicename": "ipfs", "credentials": "your_pinata_jwt"}'
File Operations
Method
Endpoint
Description
POST
/upload
Upload a file (multipart form: orgId + file)
DELETE
/delete/{id}
Delete a file
GET
/list
List all files


Upload a file:

curl -X POST http://localhost:8080/upload \

  -F "orgId=1" \

  -F "file=@/path/to/file.pdf"

The upload endpoint returns identifiers from all configured backends:

[

  "file.pdf",

  "QmceDFJtDgH8R154MCYgjG9izbATLoC7M5mQ6vpjE8wsXB"

]
Project Structure
src/main/java/org/example/s3learning/

├── config/

│   ├── S3Config.java              # AWS S3 client configuration

│   └── PinataConfig.java          # Pinata WebClient configuration

├── controller/

│   ├── StorageController.java     # Unified file operations API

│   └── OrgController.java         # Organization management API

├── entity/

│   ├── OrgEntity.java             # Organization table

│   └── OrgStorageConfigEntity.java # Per-org storage backend config

├── repository/

│   ├── OrgRepository.java         # Organization data access

│   └── OrgConfigRepository.java   # Storage config data access

├── service/

│   ├── StorageService.java        # Routing layer (loops through org backends)

│   ├── OrgService.java            # Organization CRUD operations

│   ├── S3Service.java             # AWS S3 provider

│   └── PinataService.java         # Pinata IPFS provider

├── StorageInterface.java          # Common interface: upload, delete, list

└── S3learningApplication.java     # Main application entry point
Storage Providers
Provider         Type           Backend        Identifier
S3Service       Cloud storage    AWS S3         S3 object key (filename)
PinataService   Hosted IPFS      Pinata API      CID (content hash)
KuboService      Self-managed    IPFS Kubo node  CID (content hash)

How Routing Works
An organization is created via the API
Storage backends are configured for that organization (S3, IPFS, or both)
When a file operation is requested with an orgId, the system looks up the org's configured backends
The operation is executed on all configured backends (redundancy support)
Technology Stack
Java 21
Spring Boot 4.0.6
Spring WebFlux (WebClient for Pinata/Kubo HTTP calls)
AWS SDK v2 (S3 integration)
H2 Database (organization and config persistence)
Docker & Docker Compose (containerized deployment)


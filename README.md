# Overview

The **Event Ticketing Manager** is a complete, scalable and secure REST API built with **Spring Boot** designed to manage the full lifecycle of events, tickets, QRcode delivery and staff ticket validation.  
It provides all backend functionality required for a modern ticketing system, including authentication, authorization, user provisioning, event management, ticket purchasing, and QR-based entry validation.

This backend supports three main user roles **Organizers**, **Attendees** and **Staff** each with dedicated workflows and permissions. Users authenticate through **Keycloak** and the backend automatically provisions new users into the local database using the information contained in their JWT eliminating the need for manual account creation.

---

## 👩‍💼 Organizers

Organizers can manage every aspect of their events:

- Create, update and delete events  
- Configure ticket types (VIP, Standard, Early Bird…)  
- Set event date, venue and ticket sales
- View event listings (with pagination & sorting)  
- Manage attendees and staff  

---

## 🎫 Attendees

- Browse and search **published events**  
- View detailed information about any event  
- Purchase tickets  
- View their purchased tickets  
- Retrieve automatically generated QR codes for entry  

---

## 🛂 Staff

- Validate tickets via **QR scan**  
- Validate tickets **manually** using ticket ID  
- View complete ticket validation history for an event  
- Prevent ticket reuse through double scan protection  

---

## 🔐 Authentication & Automatic User Provisioning

Authentication is handled via **Keycloak** using OAuth2/OIDC:

- Users log in through Keycloak  
- The backend validates JWT access tokens  
- Roles are extracted (`ROLE_ATTENDEE`, `ROLE_ORGANIZER`, `ROLE_STAFF`)  
- A custom security filter automatically creates the user in the database on first login, using details from the JWT (name, email, Keycloak ID)

This ensures user records in the backend always remain synchronized with Keycloak.

---

# Tech Stack

| Layer       | Technology                |
|-------------|----------------------------|
| **Language** | Java 21                    |
| **Framework** | Spring Boot 3.4.x          |
| **Database** | PostgreSQL                  |
| **ORM**      | Spring Data JPA             |
| **Auth**     | Keycloak (OIDC + JWT)       |
| **Build**    | Maven                       |
| **Mapping**  | MapStruct                   |
| **QR Codes** | ZXing                       |
| **Dev Tools** | Docker / Adminer           |

# Getting Started

Follow these steps to set up and run the backend locally.

## Clone the Repository

```bash
git clone https://github.com/tamaraghosn/springboot-event-ticketing-manager.git
cd springboot-event-ticketing-manager
```
## Start Infrastructure (Postgres + Adminer + Keycloak)

Start all required services using Docker:

```bash
docker-compose up -d
```

## Run Spring Boot

Start the backend using Maven:

```bash
mvn spring-boot:run
```

## Configuration (application.properties)

```properties
spring.application.name=EventTicketingManager
spring.datasource.url=jdbc:postgresql://localhost:5433/${POSTGRES_DB}
spring.datasource.username=${POSTGRES_USER}
spring.datasource.password=${POSTGRES_PASSWORD}
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost:9090/realms/event-ticket-platform
```
# Domain Model

The backend follows a clear, relational domain model to manage users, events, tickets, and validation workflows.

---

## 🧑 User

| Field | Type | Description |
|-------|------|-------------|
| `id` | UUID | Primary identifier |
| `name` | String | Full name of the user |
| `email` | String | Unique user email |
| `createdAt` | Timestamp | Audit: creation time |
| `updatedAt` | Timestamp | Audit: last update |

**Relations:**
- `organizedEvents` → Events created by the user  
- `attendingEvents` → Events the user purchased tickets for  
- `staffingEvents` → Events where the user is assigned as staff  

---

## 🗓 Event

| Field | Type | Description |
|-------|------|-------------|
| `id` | UUID | Primary identifier |
| `name` | String | Event title |
| `startDateTime` | DateTime | Event start |
| `endDateTime` | DateTime | Event end |
| `venue` | String | Event location |
| `ticketSalesStart` | DateTime | When ticket sales open |
| `ticketSalesEnd` | DateTime | When ticket sales close |
| `status` | EventStatusEnum | DRAFT / PUBLISHED / ARCHIVED |

**Relations:**
- `organizer` → User who created the event  
- `attendees` → Users attending the event  
- `staff` → Staff assigned to the event  
- `ticketTypes` → List of ticket categories for the event  

---

## 🎟 TicketType

| Field | Type | Description |
|-------|------|-------------|
| `name` | String | Ticket type name (e.g., VIP, Standard) |
| `description` | String | Optional details |
| `price` | BigDecimal | Ticket price |
| `totalAvailable` | Integer | Quantity available for sale |

**Relation:**
- `event` → The event this ticket type belongs to  

---

## 🎫 Ticket

| Field | Type | Description |
|-------|------|-------------|
| `status` | TicketStatusEnum | ACTIVE / USED / CANCELLED |

**Relations:**
- `ticketType` → The purchased ticket type  
- `purchaser` → The user who bought the ticket  
- `validations` → History of validation attempts  
- `qrCodes` → QR codes generated for this ticket  

---

## 📷 QrCode

| Field | Type | Description |
|-------|------|-------------|
| `imageBase64` | String | Base64-encoded PNG QR code |

**Relation:**
- `ticket` → Ticket this QR code corresponds to  

---

## 📝 TicketValidation

| Field | Type | Description |
|-------|------|-------------|
| `status` | TicketValidationStatusEnum | VALID / INVALID / REVOKED |
| `method` | TicketValidationMethod | QR / MANUAL |

**Relation:**
- `ticket` → Ticket being validated  



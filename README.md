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

---

## 1️⃣ Clone the Repository

```bash
git clone 
cd 


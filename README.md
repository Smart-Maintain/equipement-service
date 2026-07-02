# Equipment Service

The Equipment Service handles the core business logic of the SmartMaintain platform.

## Overview
It manages the lifecycle of industrial equipment, maintenance tasks, inventory, and reporting. It also processes AI prediction alerts and triggers system notifications.

## Key Domains
- **Equipment & Teams:** `Equipement`, `Taxonomie`, `Equipe`
- **Maintenance & Tasks:** `Maintenance`, `Tache`, `SubTask`
- **Inventory:** `Piece`, `PieceRequest`, `MaintenancePiece`
- **Reporting & Comm:** `Rapport`, `PredictionAlert`, `Notification`, `BugFeedback`

## Technical Details
- **Port:** `8082`
- **Framework:** Spring Boot 3.2, Spring Data JPA
- **Database:** PostgreSQL (shared `smartmaintain_db`)

## Running Locally
```bash
../mvnw spring-boot:run -DskipTests
```

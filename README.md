# RAG Incident Desk - Backend (Spring Boot)

## Présentation

Le backend du projet RAG Incident Desk est une API REST développée avec Spring Boot permettant :

- l’authentification JWT,
- l’ingestion de documents PDF,
- la vectorisation des chunks de documentation,
- la recherche sémantique via pgvector,
- la génération de réponses contextualisées via un modèle LLM exécuté avec Ollama.

Le backend constitue le cœur du système RAG (Retrieval-Augmented Generation).

---

## Stack technique

### Frameworks et bibliothèques

- Java 21
- Spring Boot 3
- Spring Security
- Spring Data JPA
- Hibernate
- LangChain4j
- JWT (JSON Web Token)
- Maven

### IA / RAG

- Ollama
- llama3.2:1b
- nomic-embed-text
- pgvector

### Base de données

- PostgreSQL 17
- Extension pgvector

### Conteneurisation

- Docker
- Docker Compose

---

## Architecture backend

Le backend suit une architecture en couches inspirée du modèle MVC :

```text
Controller
   ↓
Service
   ↓
Repository
   ↓
PostgreSQL / pgvector
```

### Principaux modules

#### Authentification

- gestion des utilisateurs,
- génération de JWT,
- contrôle d’accès RBAC.

#### Ingestion documentaire

- upload PDF,
- parsing des documents,
- découpage en chunks,
- génération des embeddings,
- stockage vectoriel.

#### RAG

- récupération des chunks pertinents,
- enrichissement du prompt,
- génération de réponse via Ollama.

---

## Sécurité

### Authentification JWT

Le backend utilise Spring Security avec JWT stateless.

Le token contient :

- l’email de l’utilisateur,
- le rôle,
- la date d’expiration.

### RBAC

Deux rôles sont définis :

#### ADMIN

Peut :

- ingérer des documents,
- gérer la base documentaire,
- accéder au dashboard d’administration,
- agit en tant que USER

#### USER

Peut :

- poser des questions au moteur RAG,
- consulter les réponses générées.

### Endpoints sécurisés

| Endpoint                | Accès |
|-------------------------|---|
| /api/auth/**            | Public |
| /api/admin/documents/** | ADMIN |
| /api/rag/**             | USER / ADMIN |

---

## Setup local backend

### Prérequis

- Docker Desktop
- Java 21
- Maven

### Lancement

Le backend est normalement lancé via l’orchestration Docker globale.

Pour un lancement standalone :

```bash
mvn spring-boot:run
```

### Variables d’environnement

Exemple :

```env
POSTGRES_DB=incident_desk
POSTGRES_USER=user
POSTGRES_PASSWORD=password
APP_JWT_SECRET=change-this-secret
APP_JWT_EXPIRATION_MS=86400000
```

---

## API principales

### Authentification

#### POST /api/auth/register

Création d’un compte.

#### POST /api/auth/login

Connexion et récupération du JWT.

---

### RAG

#### POST /api/rag/ask

Permet de poser une question au moteur RAG.

Exemple :

```json
{
  "question": "Qu'est-ce que la doc dit sur les livrables attendus?"
}
```

---

### Ingestion documentaire

#### POST /api/documents/upload

Upload d’un document PDF.

Type : multipart/form-data.

---
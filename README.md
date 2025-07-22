# 📁 Spring Boot File Upload & Download App

This project is a RESTful web service built with **Spring Boot** that allows users to:

- ✅ Upload files
- ✅ Download files
- ✅ Integrate with any frontend easily (React, Angular, etc.)
- ✅ Deploy as a **Docker container** 🐳

---

## 🚀 Features

- Upload any file to a server
- Download files by filename
- Auto-handle storage inside a local folder
- Dockerized and easy to deploy

---

## 🐳 Docker Hub

➡️ [Docker Hub - jhona1dev/upload_file](https://hub.docker.com/repository/docker/jhona1dev/upload_file)

You can run the container directly using Docker:

```bash
docker pull jhona1dev/upload_file:latest
docker run -p 8080:8080 jhona1dev/upload_file


## 📂 API Endpoints Overview

| Method | Endpoint         | Description               |
|--------|------------------|---------------------------|
| POST   | `/upload`        | Upload a file             |
| GET    | `/download/{id}` | Download a file by ID     |
| GET    | `/all`           | Get all uploaded files    |

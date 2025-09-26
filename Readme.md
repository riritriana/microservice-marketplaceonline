# Proyek Microservice Online Marketplace

Proyek ini adalah implementasi arsitektur microservice untuk sebuah marketplace online sederhana, sesuai dengan tantangan dari Simple Journey Indonesia.

## Arsitektur & Teknologi
[cite_start]Proyek ini terdiri dari 3 service utama yang saling berinteraksi[cite: 3]:
1.  [cite_start]**User Management Service** (Port 8080): Mengelola registrasi, login (JWT), role (Admin/Staff) [cite: 8][cite_start], dan integrasi email[cite: 7].
2.  [cite_start]**Product Management Service** (Port 8081): Mengelola data produk (CRUD), hanya bisa dimodifikasi oleh Admin[cite: 8].
3.  **Cart Service** (Port 8082): Mengelola keranjang belanja pengguna dan proses checkout.

**Teknologi yang Digunakan:**
* [cite_start]Framework: **Java Spring Boot** [cite: 5]
* Database: **MariaDB**
* [cite_start]Containerization: **Docker & Docker Compose** 

## Prasyarat
Untuk menjalankan proyek ini, Anda hanya membutuhkan:
* Docker
* Docker Compose (biasanya sudah termasuk dalam instalasi Docker Desktop)

## Cara Menjalankan Proyek
Semua service, termasuk database, dapat dijalankan dengan satu perintah dari folder root proyek.

1.  Clone repository ini.
2.  Buka terminal di folder root proyek.
3.  Jalankan perintah berikut:
    ```bash
    docker-compose up --build
    ```
4.  Aplikasi akan berjalan dan siap diakses.

## Dokumentasi API (Swagger)
[cite_start]Setelah proyek berjalan, dokumentasi API interaktif untuk setiap service dapat diakses melalui URL berikut[cite: 9]:

* **User Service**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
* **Product Service**: [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)
* **Cart Service**: [http://localhost:8082/swagger-ui.html](http://localhost:8082/swagger-ui.html)
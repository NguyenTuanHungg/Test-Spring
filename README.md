# Hướng Dẫn Sử Dụng Chức Năng Đăng Nhập Spring Security

## Tổng quan
Ứng dụng Spring Boot với chức năng đăng nhập sử dụng Spring Security, lấy thông tin email và password từ bảng `candidates` trong PostgreSQL.

## Cấu trúc Database

### Bảng candidates
```sql
CREATE TABLE IF NOT EXISTS candidates (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(255),
    phone VARCHAR(20),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);
```

## Setup Database

1. Kết nối với PostgreSQL database (đã cấu hình trong application.properties):
```
Host: 10.1.45.181:5432
Database: yst_swimmy
Username: admin
Password: yst_swimmy
```

2. Chạy file `database.sql` để tạo bảng và thêm dữ liệu mẫu:
```bash
psql -h 10.1.45.181 -U admin -d yst_swimmy -f database.sql
```

## API Endpoints

### 1. Đăng ký tài khoản mới
**POST** `/api/auth/candidate/register`

**Request Body:**
```json
{
  "email": "newuser@example.com",
  "password": "yourpassword",
  "fullName": "Nguyen Van A",
  "phone": "0123456789"
}
```

**Response (201 Created):**
```json
{
  "message": "Registration successful",
  "candidateId": 1,
  "email": "newuser@example.com"
}
```

### 2. Đăng nhập
**POST** `/api/auth/candidate/login`

**Request Body:**
```json
{
  "email": "test@example.com",
  "password": "123456"
}
```

**Response (200 OK):**
```json
{
  "message": "Login successful",
  "candidateId": 1,
  "email": "test@example.com",
  "fullName": "Nguyen Van A"
}
```

**Response (401 Unauthorized) - Sai email/password:**
```json
{
  "error": "Invalid email or password"
}
```

## Test với cURL

### Đăng ký:
```bash
curl -X POST http://localhost:8080/api/auth/candidate/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "123456",
    "fullName": "Nguyen Van A",
    "phone": "0123456789"
  }'
```

### Đăng nhập:
```bash
curl -X POST http://localhost:8080/api/auth/candidate/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "123456"
  }'
```

## Test với Postman

1. Mở Postman
2. Tạo request mới với method POST
3. URL: `http://localhost:8080/api/auth/candidate/login`
4. Headers: 
   - Content-Type: application/json
5. Body (raw JSON):
```json
{
  "email": "test@example.com",
  "password": "123456"
}
```

## Tài khoản test mẫu

Sau khi chạy file `database.sql`, bạn có 2 tài khoản test:
- Email: `test@example.com` / Password: `123456`
- Email: `user@test.com` / Password: `123456`

## Chạy ứng dụng

```bash
./mvnw spring-boot:run
```

hoặc

```bash
./mvnw clean package
java -jar target/test-swimmy-0.0.1-SNAPSHOT.jar
```

## Công nghệ sử dụng

- **Spring Boot 4.0.1** (Spring Security 7)
- **Spring Security** - Authentication & Authorization
- **Spring Data JPA** - Database access
- **PostgreSQL** - Database
- **Lombok** - Reduce boilerplate code
- **BCrypt** - Password encoding

## Cấu trúc code

```
src/main/java/com/example/testswimmy/
├── config/
│   └── security/
│       └── WebSecurityConfig.java          # Cấu hình Spring Security
├── controller/
│   └── CandidateAuthController.java        # REST API endpoints
├── dto/
│   ├── LoginRequest.java                   # DTO cho login request
│   └── LoginResponse.java                  # DTO cho login response
├── entity/
│   └── Candidate.java                      # JPA Entity
├── repository/
│   └── CandidateRepository.java            # JPA Repository
├── service/
│   ├── AuthService.java                    # Service xử lý authentication
│   └── CandidateUserDetailsService.java    # UserDetailsService implementation
└── TestSwimmyApplication.java              # Main application
```

## Security Features

1. **Password Encryption**: Mật khẩu được mã hóa bằng BCrypt trước khi lưu vào database
2. **Authentication**: Sử dụng Spring Security AuthenticationManager
3. **Authorization**: Endpoint login/register public, các endpoint khác yêu cầu authenticated
4. **CSRF**: Disabled (phù hợp cho REST API)


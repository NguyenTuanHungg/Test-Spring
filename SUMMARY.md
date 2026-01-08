# TỔNG HỢP CÁC THÀNH PHẦN ĐÃ TẠO

## 1. Entity Layer

### Candidate.java
- Entity JPA mapping với bảng `candidates`
- Các field: id, email, password, fullName, phone, createdAt, updatedAt
- Tự động set timestamp khi tạo mới và cập nhật

## 2. Repository Layer

### CandidateRepository.java
- Extends JpaRepository
- Các method:
  - `findByEmail(String email)`: Tìm candidate theo email
  - `existsByEmail(String email)`: Kiểm tra email đã tồn tại

## 3. DTO Layer

### LoginRequest.java
- DTO nhận request đăng nhập
- Fields: email, password

### LoginResponse.java
- DTO trả về khi đăng nhập thành công
- Fields: message, candidateId, email, fullName

## 4. Service Layer

### CandidateUserDetailsService.java
- Implement interface UserDetailsService của Spring Security
- Load thông tin user từ database theo email
- Trả về UserDetails cho Spring Security authentication

### AuthService.java
- Service xử lý business logic cho authentication
- Method `login()`: Xác thực và trả về thông tin candidate
- Method `register()`: Đăng ký candidate mới, mã hóa password bằng BCrypt

## 5. Controller Layer

### CandidateAuthController.java
- REST Controller với base path: `/api/auth/candidate`
- Endpoints:
  - `POST /login`: Đăng nhập
  - `POST /register`: Đăng ký tài khoản mới
- Xử lý exception và trả về response phù hợp

## 6. Security Configuration

### WebSecurityConfig.java
- Cấu hình Spring Security
- Bean configurations:
  - `SecurityFilterChain`: Cấu hình authorization rules
  - `PasswordEncoder`: BCryptPasswordEncoder
  - `DaoAuthenticationProvider`: Provider xác thực từ database
  - `AuthenticationManager`: Manager xử lý authentication
- Public endpoints: `/api/auth/candidate/login`, `/api/auth/candidate/register`
- Authenticated endpoints: `/api/candidate/**`
- CSRF disabled (cho REST API)

## 7. Database

### database.sql
- Script tạo bảng candidates
- Dữ liệu mẫu với 2 tài khoản test:
  - test@example.com / 123456
  - user@test.com / 123456
- Password đã được BCrypt hash

## 8. Documentation

### README.md
- Hướng dẫn setup và sử dụng
- Mô tả API endpoints
- Ví dụ request/response
- Cách test với cURL và Postman

### test-api.sh
- Script bash để test tất cả các API endpoints
- Test cases: register, login success, login fail, wrong password, etc.

## FLOW HOẠT ĐỘNG

### Flow Đăng ký:
1. Client gửi POST request đến `/api/auth/candidate/register`
2. CandidateAuthController nhận request
3. AuthService.register() được gọi
4. Kiểm tra email đã tồn tại chưa
5. Mã hóa password bằng BCrypt
6. Lưu candidate vào database
7. Trả về thông tin candidate đã tạo

### Flow Đăng nhập:
1. Client gửi POST request đến `/api/auth/candidate/login` với email/password
2. CandidateAuthController nhận LoginRequest
3. AuthService.login() được gọi
4. AuthenticationManager.authenticate() xác thực
5. Spring Security gọi CandidateUserDetailsService.loadUserByUsername()
6. Load thông tin candidate từ database
7. So sánh password (BCrypt)
8. Nếu đúng: trả về LoginResponse với thông tin candidate
9. Nếu sai: throw AuthenticationException → trả về 401

## CÔNG NGHỆ & BEST PRACTICES

✅ **Spring Security** - Framework bảo mật chuẩn
✅ **BCrypt** - Mã hóa password an toàn
✅ **JPA/Hibernate** - ORM mapping database
✅ **DTO Pattern** - Tách biệt entity và API contract
✅ **Repository Pattern** - Tách biệt data access logic
✅ **Service Layer** - Business logic tập trung
✅ **RESTful API** - API design chuẩn REST
✅ **Exception Handling** - Xử lý lỗi tập trung
✅ **Lombok** - Giảm boilerplate code

## CÁC ĐIỂM CẦN LƯU Ý

1. **Password Security**: 
   - Password được mã hóa BCrypt trước khi lưu
   - Không bao giờ trả password về client

2. **Email là Username**: 
   - Sử dụng email làm username cho authentication
   - Email phải unique trong database

3. **Public vs Authenticated Endpoints**:
   - Login/Register: public (không cần auth)
   - Các endpoint khác: cần authenticated

4. **CSRF**: 
   - Disabled vì đây là REST API
   - Nếu có web form, nên enable CSRF

5. **Spring Security Version**:
   - Spring Boot 4.0.1 sử dụng Spring Security 7
   - API khác với version cũ (antMatchers → requestMatchers)
   - DaoAuthenticationProvider constructor nhận UserDetailsService

## CÁCH SỬ DỤNG

1. **Setup Database**:
```bash
psql -h 10.1.45.181 -U admin -d yst_swimmy -f database.sql
```

2. **Compile & Run**:
```bash
./mvnw clean compile
./mvnw spring-boot:run
```

3. **Test API**:
```bash
./test-api.sh
```

hoặc dùng cURL:
```bash
curl -X POST http://localhost:8080/api/auth/candidate/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"123456"}'
```

## KẾT QUẢ

✅ Chức năng đăng nhập hoàn chỉnh với Spring Security
✅ Lấy thông tin từ bảng candidates trong database
✅ Mã hóa password an toàn
✅ API endpoints đầy đủ (login, register)
✅ Documentation chi tiết
✅ Test scripts sẵn sàng
✅ Code structure chuẩn Spring Boot
✅ Compile thành công không lỗi


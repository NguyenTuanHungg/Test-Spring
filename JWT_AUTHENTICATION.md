# JWT Authentication Guide

## Giới thiệu
Hệ thống đã được cấu hình để sử dụng JWT (JSON Web Token) cho xác thực. JWT là một phương thức bảo mật hiện đại cho RESTful API.

## Cấu trúc JWT đã triển khai

### 1. **JwtUtil** - Lớp xử lý JWT
- Tạo JWT token từ email người dùng
- Xác thực JWT token
- Trích xuất thông tin từ token
- Token hết hạn sau 24 giờ (cấu hình trong application.properties)

### 2. **JwtAuthenticationFilter** - Filter xác thực
- Chặn mọi request và kiểm tra JWT token trong header
- Tự động xác thực người dùng nếu token hợp lệ
- Cho phép request tiếp tục nếu xác thực thành công

### 3. **WebSecurityConfig** - Cấu hình Security
- Tắt CSRF (không cần cho stateless API)
- Cấu hình sessionManagement là STATELESS (không lưu session)
- Public endpoints: `/api/auth/candidate/login` và `/api/auth/candidate/register`
- Protected endpoints: `/api/candidate/**` (yêu cầu JWT token)
- Thêm JwtAuthenticationFilter vào filter chain

## API Endpoints

### 1. Register - Đăng ký tài khoản mới
```bash
POST http://localhost:8080/api/auth/candidate/register
Content-Type: application/json

{
  "email": "test@example.com",
  "password": "password123",
  "name": "John Doe",
  "phone": "0123456789"
}
```

**Response:**
```json
{
  "message": "Registration successful",
  "candidateId": 1,
  "email": "test@example.com"
}
```

### 2. Login - Đăng nhập
```bash
POST http://localhost:8080/api/auth/candidate/login
Content-Type: application/json

{
  "email": "test@example.com",
  "password": "password123"
}
```

**Response:**
```json
{
  "message": "Login successful",
  "candidateId": 1,
  "email": "test@example.com",
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "tokenType": "Bearer"
}
```

### 3. Sử dụng Protected Endpoints
Để truy cập các endpoint được bảo vệ, cần thêm JWT token vào header:

```bash
GET http://localhost:8080/api/candidate/profile
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

## Cấu hình JWT (application.properties)

```properties
# JWT Secret Key - Thay đổi trong production
jwt.secret=mySecretKeyForJWTTokenGenerationMustBeAtLeast256BitsLongForHS256Algorithm

# JWT Token expiration time (milliseconds) - 24 hours
jwt.expiration=86400000
```

## Cách hoạt động

1. **Register/Login**: Người dùng đăng ký hoặc đăng nhập với email/password
2. **Nhận JWT Token**: Server xác thực thông tin và trả về JWT token
3. **Lưu Token**: Client lưu token (localStorage, sessionStorage, hoặc memory)
4. **Gửi Request**: Client gửi request kèm token trong Authorization header
5. **Xác thực**: JwtAuthenticationFilter kiểm tra token và xác thực người dùng
6. **Truy cập**: Nếu token hợp lệ, request được xử lý

## Filter Chain Flow

```
Request → JwtAuthenticationFilter → Authentication → Controller
```

**JwtAuthenticationFilter** kiểm tra:
1. Header "Authorization" có tồn tại không?
2. Token có format "Bearer {token}" không?
3. Token có hợp lệ không? (signature, expiration)
4. User có tồn tại trong database không?
5. Nếu tất cả OK → Set authentication vào SecurityContext

## Bảo mật

1. **Stateless**: Không lưu session trên server
2. **Mã hóa Password**: Sử dụng BCrypt
3. **Token Expiration**: Token tự động hết hạn sau 24 giờ
4. **HTTPS**: Nên sử dụng HTTPS trong production
5. **Secret Key**: Thay đổi jwt.secret trong production

## Testing với curl

```bash
# Register
curl -X POST http://localhost:8080/api/auth/candidate/register \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"password123","name":"Test User"}'

# Login
curl -X POST http://localhost:8080/api/auth/candidate/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"password123"}'

# Use protected endpoint (replace {token} with actual token)
curl -X GET http://localhost:8080/api/candidate/profile \
  -H "Authorization: Bearer {token}"
```

## Notes
- Token được lưu trong response của login API
- Client cần lưu token và gửi kèm trong mọi request sau đó
- Token hết hạn sau 24 giờ, sau đó cần login lại
- Nếu token không hợp lệ, server sẽ trả về 401 Unauthorized


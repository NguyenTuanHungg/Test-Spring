# JWT Quick Reference

## 🚀 Khởi động ứng dụng
```bash
mvn spring-boot:run
```

## 📝 API Endpoints

### 1. Register (Đăng ký)
```bash
curl -X POST http://localhost:8080/api/auth/candidate/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "password123",
    "name": "John Doe",
    "phone": "0123456789"
  }'
```

### 2. Login (Đăng nhập - Nhận JWT token)
```bash
curl -X POST http://localhost:8080/api/auth/candidate/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "password123"
  }'
```

**Response sẽ chứa JWT token:**
```json
{
  "message": "Login successful",
  "candidateId": 1,
  "email": "user@example.com",
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "tokenType": "Bearer"
}
```

### 3. Sử dụng Protected Endpoints
```bash
# Lưu token vào biến
export TOKEN="eyJhbGciOiJIUzI1NiJ9..."

# Gọi protected endpoint
curl -X GET http://localhost:8080/api/candidate/profile \
  -H "Authorization: Bearer $TOKEN"
```

## 🧪 Test Script
```bash
# Chạy test tự động
./test-jwt.sh
```

## 🔑 Key Files

| File | Mục đích |
|------|----------|
| `JwtUtil.java` | Tạo và validate JWT token |
| `JwtAuthenticationFilter.java` | Filter kiểm tra token trong mọi request |
| `WebSecurityConfig.java` | Cấu hình Spring Security + JWT |
| `AuthService.java` | Business logic login/register + generate token |
| `application.properties` | Cấu hình JWT secret và expiration |

## ⚙️ Cấu hình (application.properties)

```properties
# JWT Secret (Đổi trong production!)
jwt.secret=mySecretKeyForJWTTokenGenerationMustBeAtLeast256BitsLongForHS256Algorithm

# Token hết hạn sau 24 giờ (milliseconds)
jwt.expiration=86400000
```

## 🔒 Security Flow

```
Client                    Server
  │                         │
  │  1. POST /login         │
  ├────────────────────────>│
  │  email + password       │
  │                         │
  │  2. JWT Token          │
  │<────────────────────────┤
  │                         │
  │  3. GET /api/candidate  │
  ├────────────────────────>│
  │  Bearer {token}         │
  │                         │
  │  JwtFilter validates   │
  │  token automatically   │
  │                         │
  │  4. Response           │
  │<────────────────────────┤
```

## 📋 Public vs Protected Endpoints

### Public (Không cần token):
- ✅ `POST /api/auth/candidate/login`
- ✅ `POST /api/auth/candidate/register`

### Protected (Cần JWT token):
- 🔒 `GET/POST/PUT/DELETE /api/candidate/**`
- 🔒 Tất cả endpoints khác

## 🛠️ Troubleshooting

### Lỗi "401 Unauthorized"
- ✅ Kiểm tra header: `Authorization: Bearer {token}`
- ✅ Token có đúng format không?
- ✅ Token có hết hạn chưa? (24 giờ)

### Lỗi "403 Forbidden"
- ✅ Endpoint có được protect không?
- ✅ Token có hợp lệ không?

### Lỗi "Cannot resolve symbol 'validation'"
- ✅ Đảm bảo có dependency: `spring-boot-starter-validation`
- ✅ Chạy: `mvn clean compile`

## 📚 Documentation Files

1. `JWT_AUTHENTICATION.md` - Hướng dẫn chi tiết
2. `JWT_IMPLEMENTATION_SUMMARY.md` - Tổng quan implementation
3. `FILTERCHAIN_EXPLAINED_JWT.md` - Giải thích FilterChain
4. `JWT_QUICK_REFERENCE.md` - File này (Quick reference)

## 🎯 Token Format

```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyQGV4YW1wbGUuY29tIiwiaWF0IjoxNzA0NzE0MDAwLCJleHAiOjE3MDQ4MDA0MDB9.signature
│              │                                                                                                          │
│              └──────────────────────────────────────────────────────────────────────────────────────────────────────────┘
│                                                        JWT Token
└── Token Type (Bearer)
```

## ⏱️ Token Lifecycle

```
Login → Generate Token → Token Valid (24h) → Token Expired → Login Again
         (iat)            Can use token       (exp)           New token needed
```

## 🔐 Password Encoding

- ✅ Sử dụng BCrypt
- ✅ Tự động hash khi register
- ✅ Tự động so sánh khi login
- ❌ Không bao giờ lưu plaintext password

## ✅ Production Checklist

- [ ] Đổi `jwt.secret` thành secret key mạnh
- [ ] Bật HTTPS
- [ ] Cấu hình CORS
- [ ] Add refresh token mechanism
- [ ] Implement rate limiting
- [ ] Add logging cho authentication events
- [ ] Implement token blacklist (logout)
- [ ] Set up monitoring & alerts


# Hướng dẫn sử dụng API GET List Candidates

## Tóm tắt
Bạn đã có API hoàn chỉnh để lấy danh sách candidates với:
- ✅ Phân trang (pagination)
- ✅ Tìm kiếm theo tên (search by name)
- ✅ Sắp xếp theo ngày tạo (sort by createdAt DESC)
- ✅ Bảo mật với JWT

## Cấu trúc code đã tạo

### 1. Entity
- `Candidate.java` - Entity đã có sẵn

### 2. Repository
- `CandidateRepository.java` - Đã thêm method:
  ```java
  Page<Candidate> findByNameContainingIgnoreCase(String name, Pageable pageable);
  ```

### 3. DTOs
- `CandidateDto.java` - DTO cho response (không trả về password)
- `PageResponse.java` - Generic wrapper cho pagination response

### 4. Service
- `CandidateService.java` - Business logic:
  - Method `getCandidates(String name, int page, int size)`
  - Convert Entity to DTO
  - Xử lý tìm kiếm và phân trang

### 5. Controller
- `CandidateController.java` - REST endpoint:
  - `GET /api/candidates` với parameters: name, page, size

## Cách sử dụng

### Endpoint: GET /api/candidates

**Parameters:**
- `name` (optional): Tên candidate cần tìm
- `page` (optional, default=0): Số trang
- `size` (optional, default=10): Số items/trang

### Ví dụ requests:

#### 1. Lấy tất cả candidates (trang 1, 10 items)
```bash
curl -X GET "http://localhost:8080/api/candidates" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

#### 2. Lấy với phân trang tùy chỉnh
```bash
curl -X GET "http://localhost:8080/api/candidates?page=0&size=20" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

#### 3. Tìm kiếm theo tên
```bash
curl -X GET "http://localhost:8080/api/candidates?name=Nguyen" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

#### 4. Tìm kiếm + phân trang
```bash
curl -X GET "http://localhost:8080/api/candidates?name=John&page=1&size=5" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Response format:
```json
{
  "content": [
    {
      "id": 1,
      "email": "user@example.com",
      "phone": "0123456789",
      "name": "Nguyen Van A",
      "createdAt": "2026-01-08T10:30:00",
      "updatedAt": "2026-01-08T10:30:00"
    }
  ],
  "pageNumber": 0,
  "pageSize": 10,
  "totalElements": 50,
  "totalPages": 5,
  "last": false,
  "first": true
}
```

## Testing

### 1. Chạy ứng dụng:
```bash
mvn spring-boot:run
```

### 2. Login để lấy JWT token:
```bash
curl -X POST "http://localhost:8080/api/auth/candidate/login" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "your-email@example.com",
    "password": "your-password"
  }'
```

### 3. Sử dụng token để gọi API:
```bash
TOKEN="your-jwt-token-here"
curl -X GET "http://localhost:8080/api/candidates?page=0&size=10" \
  -H "Authorization: Bearer $TOKEN"
```

### 4. Hoặc sử dụng script có sẵn:
```bash
./test-candidate-api.sh
```

## Lưu ý quan trọng

1. **JWT Token**: API này được bảo vệ bởi JWT, cần login trước để lấy token
2. **Tìm kiếm**: Không phân biệt hoa thường (case-insensitive)
3. **Sắp xếp**: Mặc định theo createdAt DESC (mới nhất trước)
4. **Page index**: Bắt đầu từ 0 (không phải 1)
5. **Password**: Không được trả về trong response (chỉ có trong Entity, không có trong DTO)

## Các files quan trọng

1. **Controller**: `/src/main/java/com/example/testswimmy/controller/CandidateController.java`
2. **Service**: `/src/main/java/com/example/testswimmy/service/CandidateService.java`
3. **Repository**: `/src/main/java/com/example/testswimmy/repository/CandidateRepository.java`
4. **DTOs**: 
   - `/src/main/java/com/example/testswimmy/model/dto/CandidateDto.java`
   - `/src/main/java/com/example/testswimmy/model/dto/PageResponse.java`

## Build và Deploy

```bash
# Clean và compile
mvn clean compile

# Package
mvn clean package

# Run
mvn spring-boot:run
```

## Mở rộng trong tương lai

Có thể thêm:
- Tìm kiếm theo email, phone
- Sắp xếp theo nhiều trường khác nhau
- Filter theo ngày tạo
- Export to CSV/Excel
- Thêm caching với Redis


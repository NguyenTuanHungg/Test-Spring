# API Get List Candidates với Phân Trang và Tìm Kiếm

## Tổng Quan
API này cho phép lấy danh sách candidates với tính năng phân trang và tìm kiếm theo tên.

## Endpoint

### GET /api/candidates

Lấy danh sách candidates với phân trang và tìm kiếm theo tên.

#### Request Parameters

| Parameter | Type   | Required | Default | Description                                      |
|-----------|--------|----------|---------|--------------------------------------------------|
| name      | String | No       | null    | Tên candidate cần tìm (tìm kiếm không phân biệt hoa thường) |
| page      | int    | No       | 0       | Số trang (bắt đầu từ 0)                         |
| size      | int    | No       | 10      | Số lượng items trên mỗi trang                   |

#### Response

```json
{
  "content": [
    {
      "id": 1,
      "email": "candidate1@example.com",
      "phone": "0123456789",
      "name": "Nguyen Van A",
      "createdAt": "2026-01-08T10:30:00",
      "updatedAt": "2026-01-08T10:30:00"
    },
    {
      "id": 2,
      "email": "candidate2@example.com",
      "phone": "0987654321",
      "name": "Tran Thi B",
      "createdAt": "2026-01-08T11:00:00",
      "updatedAt": "2026-01-08T11:00:00"
    }
  ],
  "pageNumber": 0,
  "pageSize": 10,
  "totalElements": 2,
  "totalPages": 1,
  "last": true,
  "first": true
}
```

#### Response Fields

| Field          | Type           | Description                                    |
|----------------|----------------|------------------------------------------------|
| content        | Array          | Danh sách candidates trong trang hiện tại    |
| pageNumber     | int            | Số trang hiện tại (bắt đầu từ 0)             |
| pageSize       | int            | Số lượng items trên mỗi trang                |
| totalElements  | long           | Tổng số candidates                            |
| totalPages     | int            | Tổng số trang                                 |
| last           | boolean        | Có phải trang cuối cùng không                |
| first          | boolean        | Có phải trang đầu tiên không                 |

## Ví dụ sử dụng

### 1. Lấy tất cả candidates (trang đầu tiên)
```bash
GET /api/candidates?page=0&size=10
```

### 2. Tìm kiếm theo tên
```bash
GET /api/candidates?name=Nguyen&page=0&size=10
```

### 3. Lấy trang thứ 2
```bash
GET /api/candidates?page=1&size=10
```

### 4. Tìm kiếm và phân trang
```bash
GET /api/candidates?name=Tran&page=0&size=20
```

## Curl Examples

### Với JWT Token
```bash
curl -X GET "http://localhost:8080/api/candidates?page=0&size=10" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Tìm kiếm theo tên
```bash
curl -X GET "http://localhost:8080/api/candidates?name=Nguyen&page=0&size=10" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## Các tính năng

1. **Phân trang**: Hỗ trợ phân trang với page và size tùy chọn
2. **Tìm kiếm**: Tìm kiếm không phân biệt hoa thường theo tên
3. **Sắp xếp**: Tự động sắp xếp theo ngày tạo (createdAt) giảm dần
4. **Bảo mật**: Yêu cầu JWT token để truy cập

## Implementation Details

### Files Created/Modified:

1. **CandidateController.java** - REST Controller xử lý HTTP requests
2. **CandidateService.java** - Service layer chứa business logic
3. **CandidateRepository.java** - Repository với method tìm kiếm
4. **CandidateDto.java** - DTO cho response
5. **PageResponse.java** - Generic wrapper cho paginated responses

### Tìm kiếm
- Tìm kiếm sử dụng `LIKE %name%` không phân biệt hoa thường
- Nếu không có tham số name, trả về tất cả candidates

### Sắp xếp
- Mặc định sắp xếp theo `createdAt` giảm dần (candidates mới nhất trước)
- Có thể mở rộng để cho phép custom sorting

## Testing

Chạy script test:
```bash
./test-candidate-api.sh
```

Hoặc test thủ công với curl hoặc Postman.


#!/bin/bash

# Base URL
BASE_URL="http://localhost:8080"

echo "=== Test Candidate API với phân trang và tìm kiếm ==="
echo

# 1. Get all candidates with pagination (page 0, size 10)
echo "1. Lấy danh sách candidates (trang đầu tiên, 10 items):"
curl -X GET "$BASE_URL/api/candidates?page=0&size=10" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  | jq '.'
echo
echo "---"
echo

# 2. Search candidates by name
echo "2. Tìm kiếm candidates theo tên (name=John):"
curl -X GET "$BASE_URL/api/candidates?name=John&page=0&size=10" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  | jq '.'
echo
echo "---"
echo

# 3. Get second page
echo "3. Lấy trang thứ 2 (page=1, size=5):"
curl -X GET "$BASE_URL/api/candidates?page=1&size=5" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  | jq '.'
echo
echo "---"
echo

# 4. Search with pagination
echo "4. Tìm kiếm và phân trang (name=Nguyen, page=0, size=20):"
curl -X GET "$BASE_URL/api/candidates?name=Nguyen&page=0&size=20" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  | jq '.'
echo

echo "=== Hoàn thành ==="


#!/bin/bash

# Script test API đăng nhập và đăng ký

BASE_URL="http://localhost:8080"

echo "=================================="
echo "Test Spring Security Authentication"
echo "=================================="
echo ""

# Test 1: Đăng ký tài khoản mới
echo "1. Test Đăng ký tài khoản mới"
echo "POST $BASE_URL/api/auth/candidate/register"
curl -X POST "$BASE_URL/api/auth/candidate/register" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "newuser@test.com",
    "password": "password123",
    "fullName": "Nguyen Van Test",
    "phone": "0909123456"
  }'
echo -e "\n"

# Test 2: Đăng nhập với tài khoản vừa tạo
echo "2. Test Đăng nhập với tài khoản mới"
echo "POST $BASE_URL/api/auth/candidate/login"
curl -X POST "$BASE_URL/api/auth/candidate/login" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "newuser@test.com",
    "password": "password123"
  }'
echo -e "\n"

# Test 3: Đăng nhập với tài khoản có sẵn (nếu đã chạy database.sql)
echo "3. Test Đăng nhập với tài khoản mẫu"
echo "POST $BASE_URL/api/auth/candidate/login"
curl -X POST "$BASE_URL/api/auth/candidate/login" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "123456"
  }'
echo -e "\n"

# Test 4: Đăng nhập sai password
echo "4. Test Đăng nhập sai password (should fail)"
echo "POST $BASE_URL/api/auth/candidate/login"
curl -X POST "$BASE_URL/api/auth/candidate/login" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "wrongpassword"
  }'
echo -e "\n"

# Test 5: Đăng nhập với email không tồn tại
echo "5. Test Đăng nhập với email không tồn tại (should fail)"
echo "POST $BASE_URL/api/auth/candidate/login"
curl -X POST "$BASE_URL/api/auth/candidate/login" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "notexist@example.com",
    "password": "123456"
  }'
echo -e "\n"

echo "=================================="
echo "Test hoàn tất!"
echo "=================================="


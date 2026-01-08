#!/bin/bash

# JWT Authentication Test Script
# Usage: ./test-jwt.sh

BASE_URL="http://localhost:8080"
EMAIL="test$(date +%s)@example.com"  # Unique email for each test
PASSWORD="password123"
NAME="Test User"

echo "🚀 JWT Authentication Test Script"
echo "=================================="
echo ""

# Colors
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Check if server is running
echo "🔍 Checking if server is running..."
if ! curl -s "$BASE_URL/api/auth/candidate/login" > /dev/null 2>&1; then
    echo -e "${RED}❌ Server is not running on $BASE_URL${NC}"
    echo "Please start the server first: mvn spring-boot:run"
    exit 1
fi
echo -e "${GREEN}✅ Server is running${NC}"
echo ""

# Test 1: Register
echo "📝 Test 1: Register new candidate"
echo "=================================="
REGISTER_RESPONSE=$(curl -s -X POST "$BASE_URL/api/auth/candidate/register" \
  -H "Content-Type: application/json" \
  -d "{\"email\":\"$EMAIL\",\"password\":\"$PASSWORD\",\"name\":\"$NAME\"}")

echo "Request:"
echo "  POST $BASE_URL/api/auth/candidate/register"
echo "  Body: {\"email\":\"$EMAIL\",\"password\":\"$PASSWORD\",\"name\":\"$NAME\"}"
echo ""
echo "Response:"
echo "$REGISTER_RESPONSE" | jq '.' 2>/dev/null || echo "$REGISTER_RESPONSE"
echo ""

if echo "$REGISTER_RESPONSE" | grep -q "Registration successful"; then
    echo -e "${GREEN}✅ Registration successful${NC}"
else
    echo -e "${RED}❌ Registration failed${NC}"
fi
echo ""

# Test 2: Login
echo "🔐 Test 2: Login with credentials"
echo "=================================="
LOGIN_RESPONSE=$(curl -s -X POST "$BASE_URL/api/auth/candidate/login" \
  -H "Content-Type: application/json" \
  -d "{\"email\":\"$EMAIL\",\"password\":\"$PASSWORD\"}")

echo "Request:"
echo "  POST $BASE_URL/api/auth/candidate/login"
echo "  Body: {\"email\":\"$EMAIL\",\"password\":\"$PASSWORD\"}"
echo ""
echo "Response:"
echo "$LOGIN_RESPONSE" | jq '.' 2>/dev/null || echo "$LOGIN_RESPONSE"
echo ""

# Extract JWT token
TOKEN=$(echo "$LOGIN_RESPONSE" | jq -r '.token' 2>/dev/null)

if [ "$TOKEN" != "null" ] && [ -n "$TOKEN" ]; then
    echo -e "${GREEN}✅ Login successful${NC}"
    echo "JWT Token: ${TOKEN:0:50}..."
else
    echo -e "${RED}❌ Login failed - No token received${NC}"
    exit 1
fi
echo ""

# Test 3: Login with wrong password
echo "🚫 Test 3: Login with wrong password"
echo "====================================="
WRONG_LOGIN=$(curl -s -X POST "$BASE_URL/api/auth/candidate/login" \
  -H "Content-Type: application/json" \
  -d "{\"email\":\"$EMAIL\",\"password\":\"wrongpassword\"}")

echo "Request:"
echo "  POST $BASE_URL/api/auth/candidate/login"
echo "  Body: {\"email\":\"$EMAIL\",\"password\":\"wrongpassword\"}"
echo ""
echo "Response:"
echo "$WRONG_LOGIN" | jq '.' 2>/dev/null || echo "$WRONG_LOGIN"
echo ""

if echo "$WRONG_LOGIN" | grep -q "error"; then
    echo -e "${GREEN}✅ Correctly rejected wrong password${NC}"
else
    echo -e "${RED}❌ Should have rejected wrong password${NC}"
fi
echo ""

# Test 4: Access protected endpoint without token
echo "🛡️  Test 4: Access protected endpoint WITHOUT token"
echo "===================================================="
NO_AUTH_RESPONSE=$(curl -s -w "\nHTTP_STATUS:%{http_code}" -X GET "$BASE_URL/api/candidate/1")
HTTP_STATUS=$(echo "$NO_AUTH_RESPONSE" | grep "HTTP_STATUS" | cut -d: -f2)
BODY=$(echo "$NO_AUTH_RESPONSE" | sed '/HTTP_STATUS/d')

echo "Request:"
echo "  GET $BASE_URL/api/candidate/1"
echo "  (No Authorization header)"
echo ""
echo "Response:"
echo "  HTTP Status: $HTTP_STATUS"
if [ -n "$BODY" ]; then
    echo "$BODY" | jq '.' 2>/dev/null || echo "$BODY"
fi
echo ""

if [ "$HTTP_STATUS" = "401" ] || [ "$HTTP_STATUS" = "403" ]; then
    echo -e "${GREEN}✅ Correctly rejected request without token (HTTP $HTTP_STATUS)${NC}"
else
    echo -e "${YELLOW}⚠️  Expected 401/403, got HTTP $HTTP_STATUS${NC}"
fi
echo ""

# Test 5: Access protected endpoint with token
echo "🎫 Test 5: Access protected endpoint WITH token"
echo "================================================"
AUTH_RESPONSE=$(curl -s -w "\nHTTP_STATUS:%{http_code}" -X GET "$BASE_URL/api/candidate/1" \
  -H "Authorization: Bearer $TOKEN")
HTTP_STATUS=$(echo "$AUTH_RESPONSE" | grep "HTTP_STATUS" | cut -d: -f2)
BODY=$(echo "$AUTH_RESPONSE" | sed '/HTTP_STATUS/d')

echo "Request:"
echo "  GET $BASE_URL/api/candidate/1"
echo "  Authorization: Bearer ${TOKEN:0:50}..."
echo ""
echo "Response:"
echo "  HTTP Status: $HTTP_STATUS"
if [ -n "$BODY" ]; then
    echo "$BODY" | jq '.' 2>/dev/null || echo "$BODY"
fi
echo ""

if [ "$HTTP_STATUS" = "200" ]; then
    echo -e "${GREEN}✅ Successfully accessed protected endpoint with token${NC}"
elif [ "$HTTP_STATUS" = "404" ]; then
    echo -e "${YELLOW}⚠️  Token works but resource not found (HTTP 404) - Normal if /api/candidate/1 doesn't exist yet${NC}"
else
    echo -e "${RED}❌ Failed to access protected endpoint (HTTP $HTTP_STATUS)${NC}"
fi
echo ""

# Summary
echo "📊 Test Summary"
echo "==============="
echo "Base URL: $BASE_URL"
echo "Test Email: $EMAIL"
echo "JWT Token (first 50 chars): ${TOKEN:0:50}..."
echo ""
echo "You can use this token to test other protected endpoints:"
echo "  export JWT_TOKEN=\"$TOKEN\""
echo "  curl -H \"Authorization: Bearer \$JWT_TOKEN\" $BASE_URL/api/candidate/..."
echo ""
echo -e "${GREEN}🎉 All tests completed!${NC}"


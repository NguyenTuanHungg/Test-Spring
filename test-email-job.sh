#!/bin/bash

# Script to test the email reminder job
# Usage: ./test-email-job.sh

echo "========================================="
echo "Testing Email Reminder Job"
echo "========================================="
echo ""

# Configuration
BASE_URL="http://localhost:8080"
ENDPOINT="/api/test/send-reminders"

echo "Triggering email reminder job..."
echo "URL: ${BASE_URL}${ENDPOINT}"
echo ""

# Send POST request
response=$(curl -s -w "\nHTTP_STATUS:%{http_code}" -X POST "${BASE_URL}${ENDPOINT}" \
  -H "Content-Type: application/json")

# Extract body and status
http_body=$(echo "$response" | sed -e 's/HTTP_STATUS\:.*//g')
http_status=$(echo "$response" | tr -d '\n' | sed -e 's/.*HTTP_STATUS://')

echo "Response Status: $http_status"
echo "Response Body:"
echo "$http_body" | jq '.' 2>/dev/null || echo "$http_body"
echo ""

if [ "$http_status" -eq 200 ]; then
    echo "✓ Email job triggered successfully!"
    echo "✓ Check application logs for detailed email sending status"
else
    echo "✗ Failed to trigger email job"
    echo "✗ Status code: $http_status"
fi

echo ""
echo "========================================="
echo "Tip: Check the application logs for detailed email sending information"
echo "========================================="


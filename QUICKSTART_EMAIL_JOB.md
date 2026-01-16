# 🚀 Quick Start - Email Reminder Job

## Tóm tắt
Chức năng gửi email tự động nhắc candidates đi làm lúc 5 giờ sáng hàng ngày đã được tích hợp thành công!

---

## ✅ Đã hoàn thành

### 1. **Dependencies** 
- ✅ Thêm `spring-boot-starter-mail` vào `pom.xml`

### 2. **Services**
- ✅ `EmailService.java` - Service gửi email
- ✅ `CandidateReminderJob.java` - Scheduled job chạy lúc 5 AM

### 3. **Configuration**
- ✅ Cấu hình email trong `application.properties`
- ✅ Enable scheduling trong `TestSwimmyApplication.java`

### 4. **Testing**
- ✅ `TestController.java` - API test thủ công
- ✅ `test-email-job.sh` - Script test nhanh

### 5. **Documentation**
- ✅ `EMAIL_JOB_GUIDE.md` - Hướng dẫn chi tiết
- ✅ `SCHEDULED_EMAIL_JOB.md` - Tài liệu kỹ thuật

---

## 📝 CẦN LÀM NGAY

### 1. Cấu hình Email (QUAN TRỌNG!)

Mở file `src/main/resources/application.properties` và sửa:

```properties
spring.mail.username=your-email@gmail.com      # ← SỬA DÒNG NÀY
spring.mail.password=your-app-password         # ← SỬA DÒNG NÀY
app.email.from=your-email@gmail.com            # ← SỬA DÒNG NÀY
```

### 2. Tạo App Password cho Gmail

1. Vào: https://myaccount.google.com/security
2. Bật "2-Step Verification"
3. Vào "App passwords"
4. Tạo password mới cho "Mail"
5. Copy password 16 ký tự

### 3. Test thử

```bash
# Start ứng dụng
mvn spring-boot:run

# Trigger email (terminal khác)
./test-email-job.sh

# Hoặc dùng curl
curl -X POST http://localhost:8080/api/test/send-reminders
```

---

## 📂 Cấu trúc Files mới

```
test-swimmy/
├── src/main/java/com/example/testswimmy/
│   ├── service/
│   │   └── EmailService.java              ← Service gửi email
│   ├── job/
│   │   └── CandidateReminderJob.java      ← Scheduled job (5 AM)
│   └── controller/
│       └── TestController.java            ← API test thủ công
├── src/main/resources/
│   └── application.properties             ← ĐÃ CẬP NHẬT (cần sửa email)
├── test-email-job.sh                      ← Script test
├── EMAIL_JOB_GUIDE.md                     ← Hướng dẫn chi tiết
└── SCHEDULED_EMAIL_JOB.md                 ← Tài liệu kỹ thuật
```

---

## ⏰ Lịch chạy

- **Thời gian:** 5:00 AM mỗi ngày
- **Múi giờ:** Asia/Ho_Chi_Minh (GMT+7)
- **Cron:** `0 0 5 * * ?`

---

## 🧪 Test API

### Endpoint
```
POST http://localhost:8080/api/test/send-reminders
```

### Response thành công
```json
{
  "status": "success",
  "message": "Email reminders sent successfully. Check logs for details."
}
```

---

## 📧 Email Template

Email gửi đi sẽ có:
- ✅ Tiêu đề: "Nhắc nhở: Đi làm hôm nay"
- ✅ Giao diện HTML đẹp mắt
- ✅ Nội dung nhắc nhở thân thiện
- ✅ Checklist công việc
- ✅ Cá nhân hóa theo tên candidate

---

## 🔍 Kiểm tra Logs

Khi job chạy, bạn sẽ thấy:
```
INFO  - Starting daily work reminder job at 5:00 AM
INFO  - Found 10 candidates. Sending reminder emails...
INFO  - Sent reminder email to: Nguyen Van A (email@example.com)
INFO  - Daily work reminder job completed. Success: 10, Failed: 0
```

---

## ⚙️ Thay đổi thời gian

Sửa file `CandidateReminderJob.java`:

```java
// Từ 5 AM sang 7 AM
@Scheduled(cron = "0 0 7 * * ?", zone = "Asia/Ho_Chi_Minh")

// Chỉ chạy thứ 2 đến thứ 6
@Scheduled(cron = "0 0 5 * * MON-FRI", zone = "Asia/Ho_Chi_Minh")

// Test: Chạy mỗi phút
@Scheduled(cron = "0 */1 * * * ?", zone = "Asia/Ho_Chi_Minh")
```

---

## 🛡️ Bảo mật

**⚠️ KHÔNG commit email/password thật vào Git!**

Tốt nhất:
1. Dùng environment variables
2. Hoặc tạo file `application-local.properties` (thêm vào .gitignore)

---

## 📚 Đọc thêm

- **Hướng dẫn chi tiết:** `EMAIL_JOB_GUIDE.md`
- **Tài liệu kỹ thuật:** `SCHEDULED_EMAIL_JOB.md`

---

## ✨ Tính năng

✅ Tự động gửi email 5 AM hàng ngày  
✅ Email HTML đẹp mắt  
✅ Log chi tiết  
✅ Xử lý lỗi tốt  
✅ API test thủ công  
✅ Script test nhanh  
✅ Documentation đầy đủ  

---

## 🎯 Next Steps

1. ✏️ Sửa email config trong `application.properties`
2. 🔐 Tạo Gmail App Password
3. ▶️ Start ứng dụng: `mvn spring-boot:run`
4. 🧪 Test: `./test-email-job.sh`
5. ✅ Done!

---

**Hoàn thành! Chúc bạn thành công! 🎉**

_Nếu có vấn đề gì, check file EMAIL_JOB_GUIDE.md để troubleshooting._


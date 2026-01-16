# Hướng dẫn sử dụng Email Reminder Job

## 📧 Tổng quan
Hệ thống đã được tích hợp chức năng **gửi email tự động** nhắc nhở candidates đi làm vào lúc **5 giờ sáng hàng ngày**.

---

## 🎯 Chức năng

- ✅ Tự động gửi email cho tất cả candidates trong database
- ✅ Chạy vào lúc 5:00 AM mỗi ngày (múi giờ Việt Nam)
- ✅ Email HTML đẹp mắt với nội dung nhắc nhở thân thiện
- ✅ Log chi tiết quá trình gửi email
- ✅ Xử lý lỗi và báo cáo thống kê

---

## 🚀 Cấu hình

### Bước 1: Cấu hình Gmail

#### 1.1. Tạo App Password cho Gmail

1. Truy cập: https://myaccount.google.com/security
2. Bật **2-Step Verification** (nếu chưa bật)
3. Tìm **App passwords** và click vào
4. Chọn:
   - App: **Mail**
   - Device: **Other (Custom name)** → nhập "Test Swimmy"
5. Click **Generate**
6. Copy mật khẩu 16 ký tự (định dạng: xxxx xxxx xxxx xxxx)

#### 1.2. Cập nhật file `application.properties`

```properties
# Email Configuration
spring.mail.username=your-email@gmail.com          # Thay bằng email của bạn
spring.mail.password=xxxx xxxx xxxx xxxx           # Thay bằng App Password
app.email.from=your-email@gmail.com                # Thay bằng email của bạn
```

**⚠️ Lưu ý quan trọng:**
- Dùng **App Password**, KHÔNG dùng mật khẩu Gmail thường
- App Password có 16 ký tự, có thể có hoặc không có khoảng trắng
- Email phải bật 2-Step Verification mới tạo được App Password

---

## 📋 Cấu trúc code

### 1. EmailService.java
```
src/main/java/com/example/testswimmy/service/EmailService.java
```
- Service xử lý gửi email
- Tạo nội dung email HTML đẹp mắt
- Log chi tiết quá trình gửi

### 2. CandidateReminderJob.java
```
src/main/java/com/example/testswimmy/job/CandidateReminderJob.java
```
- Scheduled job chạy lúc 5:00 AM hàng ngày
- Lấy danh sách candidates từ database
- Gửi email cho từng candidate
- Báo cáo thống kê kết quả

### 3. TestController.java
```
src/main/java/com/example/testswimmy/controller/TestController.java
```
- API để test gửi email thủ công
- Không cần đợi đến 5 giờ sáng

---

## 🧪 Test chức năng

### Cách 1: Sử dụng script (Khuyến nghị)

```bash
./test-email-job.sh
```

### Cách 2: Sử dụng curl

```bash
curl -X POST http://localhost:8080/api/test/send-reminders
```

### Cách 3: Sử dụng Postman

- Method: **POST**
- URL: `http://localhost:8080/api/test/send-reminders`
- Headers: Không cần
- Body: Không cần

---

## 📊 Kết quả mong đợi

### Response thành công:
```json
{
  "status": "success",
  "message": "Email reminders sent successfully. Check logs for details."
}
```

### Logs trong console:
```
INFO  - Starting daily work reminder job at 5:00 AM
INFO  - Found 5 candidates. Sending reminder emails...
INFO  - Sent reminder email to: Nguyen Van A (nguyenvana@gmail.com)
INFO  - Sent reminder email to: Tran Thi B (tranthib@gmail.com)
INFO  - Daily work reminder job completed. Success: 5, Failed: 0
```

---

## 📧 Nội dung Email

Email được gửi đi có giao diện đẹp với:
- **Header màu xanh:** "🌅 Chào buổi sáng!"
- **Nội dung:** Lời nhắc nhở thân thiện
- **Checklist:** Các việc cần làm trong ngày
  - ✓ Kiểm tra email công việc
  - ✓ Xem lại lịch họp trong ngày
  - ✓ Chuẩn bị tài liệu cần thiết
  - ✓ Đến văn phòng đúng giờ
- **Footer:** Thông tin hệ thống

---

## ⏰ Thay đổi thời gian chạy

### Cú pháp Cron Expression
```
┌───────────── giây (0-59)
│ ┌───────────── phút (0-59)
│ │ ┌───────────── giờ (0-23)
│ │ │ ┌───────────── ngày trong tháng (1-31)
│ │ │ │ ┌───────────── tháng (1-12 hoặc JAN-DEC)
│ │ │ │ │ ┌───────────── ngày trong tuần (0-7 hoặc MON-SUN)
│ │ │ │ │ │
* * * * * *
```

### Ví dụ:

#### 5:00 AM mỗi ngày (mặc định)
```java
@Scheduled(cron = "0 0 5 * * ?", zone = "Asia/Ho_Chi_Minh")
```

#### 6:30 AM mỗi ngày
```java
@Scheduled(cron = "0 30 6 * * ?", zone = "Asia/Ho_Chi_Minh")
```

#### 8:00 AM từ thứ 2 đến thứ 6
```java
@Scheduled(cron = "0 0 8 * * MON-FRI", zone = "Asia/Ho_Chi_Minh")
```

#### 7:00 AM thứ 2, 4, 6
```java
@Scheduled(cron = "0 0 7 * * MON,WED,FRI", zone = "Asia/Ho_Chi_Minh")
```

#### Mỗi phút (để test)
```java
@Scheduled(cron = "0 */1 * * * ?", zone = "Asia/Ho_Chi_Minh")
```

#### Mỗi 30 giây (để test)
```java
@Scheduled(cron = "0/30 * * * * ?", zone = "Asia/Ho_Chi_Minh")
```

---

## 🔧 Troubleshooting

### ❌ Lỗi: "Failed to send email"

**Nguyên nhân:** Cấu hình email chưa đúng

**Giải pháp:**
1. Kiểm tra App Password đã đúng chưa
2. Kiểm tra email có bật 2-Step Verification
3. Thử gửi lại sau vài phút (Gmail có thể block tạm thời)
4. Kiểm tra logs chi tiết: `tail -f logs/application.log`

### ❌ Lỗi: "Authentication failed"

**Nguyên nhân:** Sai App Password hoặc chưa bật 2-Step Verification

**Giải pháp:**
1. Xóa App Password cũ và tạo lại
2. Copy App Password mới cẩn thận (có thể bỏ khoảng trắng)
3. Cập nhật lại `application.properties`
4. Restart ứng dụng

### ❌ Job không chạy

**Nguyên nhân:** Chưa enable scheduling

**Giải pháp:**
Kiểm tra file `TestSwimmyApplication.java` có `@EnableScheduling`:
```java
@SpringBootApplication
@EnableScheduling  // ← Phải có dòng này
public class TestSwimmyApplication {
```

### ❌ Không có candidates để gửi

**Nguyên nhân:** Database trống

**Giải pháp:**
Thêm candidates vào database thông qua API hoặc SQL:
```sql
INSERT INTO candidates (email, name, password, created_at, updated_at) 
VALUES ('test@example.com', 'Test User', '$2a$10$...', NOW(), NOW());
```

---

## 🎨 Tùy chỉnh

### Thay đổi nội dung email

Sửa file: `EmailService.java` → method `buildWorkReminderEmailContent()`

### Thêm job khác

Tạo method mới trong `CandidateReminderJob.java`:
```java
@Scheduled(cron = "0 0 17 * * ?", zone = "Asia/Ho_Chi_Minh")
public void sendEndOfDayReminder() {
    // Logic gửi email cuối ngày
}
```

### Gửi cho nhóm cụ thể

Sửa query trong `CandidateReminderJob.java`:
```java
// Chỉ gửi cho candidates có email
List<Candidate> candidates = candidateRepository
    .findAll()
    .stream()
    .filter(c -> c.getEmail() != null && !c.getEmail().isEmpty())
    .collect(Collectors.toList());
```

---

## 🔐 Bảo mật

### ⚠️ QUAN TRỌNG: KHÔNG commit thông tin nhạy cảm

**Không an toàn:**
```properties
spring.mail.password=abcd efgh ijkl mnop  # ← KHÔNG làm thế này!
```

**An toàn - Dùng Environment Variables:**
```properties
spring.mail.password=${MAIL_PASSWORD}
```

Sau đó set biến môi trường:
```bash
export MAIL_PASSWORD="abcd efgh ijkl mnop"
java -jar app.jar
```

**An toàn - Dùng file riêng (không commit):**
1. Tạo file `application-local.properties` (thêm vào .gitignore)
2. Đặt thông tin nhạy cảm vào đó
3. Run với profile: `java -jar app.jar --spring.profiles.active=local`

---

## 📚 Tài liệu tham khảo

- [Spring Boot Email](https://docs.spring.io/spring-boot/docs/current/reference/html/io.html#io.email)
- [Spring Scheduling](https://docs.spring.io/spring-framework/docs/current/reference/html/integration.html#scheduling)
- [Cron Expression](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/scheduling/support/CronExpression.html)
- [Gmail App Passwords](https://support.google.com/accounts/answer/185833)

---

## 📞 Hỗ trợ

Nếu có vấn đề, check:
1. ✅ Logs trong console
2. ✅ Database có candidates
3. ✅ Email config đúng
4. ✅ Network có kết nối được smtp.gmail.com:587

---

## ✨ Demo

1. **Start ứng dụng:**
   ```bash
   mvn spring-boot:run
   ```

2. **Trigger test email:**
   ```bash
   ./test-email-job.sh
   ```

3. **Kiểm tra email:**
   - Mở hộp thư của candidates
   - Tìm email từ "Test Swimmy System"
   - Xem nội dung email đẹp mắt 🎉

---

**Chúc bạn thành công! 🚀**


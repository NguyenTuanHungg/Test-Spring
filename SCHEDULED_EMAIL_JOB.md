# Scheduled Email Reminder Job - Documentation

## Tổng quan
Hệ thống đã được tích hợp chức năng gửi email tự động nhắc nhở candidates đi làm vào lúc 5 giờ sáng hàng ngày.

## Các file đã được tạo/chỉnh sửa

### 1. Dependencies (pom.xml)
- Thêm `spring-boot-starter-mail` để hỗ trợ gửi email

### 2. Configuration (application.properties)
Các thông số cấu hình email:
```properties
# Email Configuration
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

**Lưu ý:** Bạn cần thay đổi các giá trị sau:
- `spring.mail.username`: Email của bạn (Gmail)
- `spring.mail.password`: App Password (không phải mật khẩu Gmail thường)
- `app.email.from`: Email người gửi

### 3. EmailService.java
Service xử lý việc gửi email với các tính năng:
- Gửi email HTML đẹp mắt
- Log chi tiết quá trình gửi email
- Xử lý lỗi khi gửi email thất bại

### 4. CandidateReminderJob.java
Scheduled job tự động chạy:
- **Thời gian:** 5:00 AM mỗi ngày (múi giờ Asia/Ho_Chi_Minh)
- **Chức năng:** Lấy tất cả candidates từ database và gửi email nhắc nhở đi làm
- **Cron expression:** `0 0 5 * * ?`

### 5. TestSwimmyApplication.java
- Thêm annotation `@EnableScheduling` để kích hoạt scheduled jobs

## Cấu hình Gmail

### Bước 1: Tạo App Password
1. Truy cập: https://myaccount.google.com/security
2. Bật "2-Step Verification" nếu chưa bật
3. Tìm "App passwords" và tạo mật khẩu ứng dụng mới
4. Chọn "Mail" và "Other (Custom name)"
5. Copy mật khẩu 16 ký tự được tạo

### Bước 2: Cập nhật application.properties
```properties
spring.mail.username=your-actual-email@gmail.com
spring.mail.password=your-16-char-app-password
app.email.from=your-actual-email@gmail.com
```

## Cron Expression

Định dạng: `giây phút giờ ngày tháng thứ`

Ví dụ:
- `0 0 5 * * ?` - 5:00 AM mỗi ngày
- `0 30 6 * * ?` - 6:30 AM mỗi ngày
- `0 0 8 * * MON-FRI` - 8:00 AM từ thứ 2 đến thứ 6
- `0 0 5 * * MON,WED,FRI` - 5:00 AM thứ 2, 4, 6

## Test Job

### Cách 1: Đợi đến 5 giờ sáng
Job sẽ tự động chạy vào 5 giờ sáng hàng ngày.

### Cách 2: Thay đổi thời gian tạm thời (để test)
Trong file `CandidateReminderJob.java`, thay đổi cron expression:
```java
@Scheduled(cron = "0 */1 * * * ?") // Chạy mỗi phút
// hoặc
@Scheduled(cron = "0/30 * * * * ?") // Chạy mỗi 30 giây
```

### Cách 3: Tạo endpoint test
Tạo một REST API để trigger job thủ công (khuyến nghị cho development).

## Logs

Khi job chạy, bạn sẽ thấy logs như sau:
```
INFO  - Starting daily work reminder job at 5:00 AM
INFO  - Found 10 candidates. Sending reminder emails...
INFO  - Sent reminder email to: Nguyen Van A (nguyenvana@email.com)
INFO  - Daily work reminder job completed. Success: 10, Failed: 0
```

## Email Template

Email được gửi đi có:
- Format HTML đẹp mắt
- Header với màu xanh lá
- Nội dung nhắc nhở thân thiện
- Danh sách checklist cho ngày làm việc
- Footer với thông tin hệ thống

## Troubleshooting

### Email không được gửi
1. Kiểm tra app password đã đúng chưa
2. Kiểm tra "Less secure app access" đã bật chưa (với Gmail cũ)
3. Kiểm tra logs để xem lỗi cụ thể
4. Thử gửi email test thủ công

### Job không chạy
1. Kiểm tra `@EnableScheduling` đã được thêm vào `TestSwimmyApplication`
2. Kiểm tra cron expression có đúng không
3. Kiểm tra timezone có đúng không

### Muốn dừng job
Comment hoặc xóa annotation `@Scheduled` trong `CandidateReminderJob.java`

## Mở rộng

### Gửi email cho nhóm cụ thể
Sửa query trong `CandidateReminderJob.java`:
```java
List<Candidate> candidates = candidateRepository.findByStatus("ACTIVE");
```

### Gửi vào nhiều thời điểm
Tạo thêm method với `@Scheduled` annotation khác:
```java
@Scheduled(cron = "0 0 17 * * ?") // 5 PM
public void sendEndOfDayReminder() {
    // Logic gửi email nhắc nhở cuối ngày
}
```

### Tùy chỉnh nội dung email
Sửa method `buildWorkReminderEmailContent()` trong `EmailService.java`

## Sử dụng Email Service khác

### Với Gmail:
```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
```

### Với Outlook/Office365:
```properties
spring.mail.host=smtp.office365.com
spring.mail.port=587
```

### Với SendGrid:
```properties
spring.mail.host=smtp.sendgrid.net
spring.mail.port=587
spring.mail.username=apikey
spring.mail.password=your-sendgrid-api-key
```

## Security Note
- **Không commit** `application.properties` với thông tin email thật vào Git
- Sử dụng environment variables hoặc external config cho production
- Sử dụng App Password, không dùng mật khẩu thật


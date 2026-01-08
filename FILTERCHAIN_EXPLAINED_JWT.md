# FilterChain trong Spring Security với JWT

## FilterChain là gì?

`filterChain` là một chuỗi các filter (bộ lọc) mà Spring Security sử dụng để xử lý mọi HTTP request trước khi nó đến Controller. Mỗi filter thực hiện một nhiệm vụ bảo mật cụ thể.

## Vị trí của FilterChain trong WebSecurityConfig

```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )
        .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/candidate/login", "/api/auth/candidate/register").permitAll()
                .requestMatchers("/api/candidate/**").authenticated()
                .anyRequest().authenticated()
        )
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
}
```

## Luồng xử lý Request với FilterChain

```
┌────────────────────────────────────────────────────────────────────────┐
│                         HTTP REQUEST                                    │
└────────────────────────┬───────────────────────────────────────────────┘
                         │
                         ▼
┌────────────────────────────────────────────────────────────────────────┐
│  1. JwtAuthenticationFilter (Custom Filter - được thêm vào chain)      │
│     - Kiểm tra header "Authorization"                                   │
│     - Trích xuất JWT token                                              │
│     - Validate token                                                    │
│     - Load UserDetails                                                  │
│     - Set Authentication vào SecurityContext                            │
└────────────────────────┬───────────────────────────────────────────────┘
                         │
                         ▼
┌────────────────────────────────────────────────────────────────────────┐
│  2. UsernamePasswordAuthenticationFilter (Built-in)                    │
│     - Xử lý form login (không dùng trong stateless JWT)                │
└────────────────────────┬───────────────────────────────────────────────┘
                         │
                         ▼
┌────────────────────────────────────────────────────────────────────────┐
│  3. ExceptionTranslationFilter                                         │
│     - Xử lý các exception về authentication/authorization              │
└────────────────────────┬───────────────────────────────────────────────┘
                         │
                         ▼
┌────────────────────────────────────────────────────────────────────────┐
│  4. AuthorizationFilter                                                │
│     - Kiểm tra quyền truy cập dựa trên authorizeHttpRequests config   │
│     - Cho phép public endpoints (/login, /register)                    │
│     - Yêu cầu authentication cho protected endpoints                   │
└────────────────────────┬───────────────────────────────────────────────┘
                         │
                         ▼
┌────────────────────────────────────────────────────────────────────────┐
│                         CONTROLLER                                      │
│                    (Xử lý business logic)                               │
└────────────────────────────────────────────────────────────────────────┘
```

## Chi tiết từng phần cấu hình

### 1. CSRF Protection
```java
.csrf(csrf -> csrf.disable())
```
- **Tắt CSRF** vì đang dùng JWT (stateless)
- CSRF chỉ cần cho session-based authentication
- JWT token được gửi trong header, không phải cookie

### 2. Session Management
```java
.sessionManagement(session -> session
    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
)
```
- **STATELESS**: Không tạo hoặc sử dụng HTTP session
- Mỗi request độc lập, phải có JWT token
- Server không lưu trạng thái người dùng

### 3. Authorization Rules
```java
.authorizeHttpRequests(auth -> auth
    .requestMatchers("/api/auth/candidate/login", "/api/auth/candidate/register").permitAll()
    .requestMatchers("/api/candidate/**").authenticated()
    .anyRequest().authenticated()
)
```
- **permitAll()**: Cho phép truy cập không cần authentication (public)
- **authenticated()**: Yêu cầu phải đăng nhập (có token hợp lệ)

### 4. Custom JWT Filter
```java
.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
```
- Thêm `JwtAuthenticationFilter` vào trước `UsernamePasswordAuthenticationFilter`
- Filter này chạy đầu tiên để validate JWT token
- Nếu token hợp lệ, set Authentication vào SecurityContext

## JwtAuthenticationFilter hoạt động như thế nào?

```java
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 1. Lấy header Authorization
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        // 2. Kiểm tra format "Bearer {token}"
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            try {
                username = jwtUtil.extractUsername(jwt);  // 3. Trích xuất username
            } catch (Exception e) {
                logger.error("JWT Token parsing error");
            }
        }

        // 4. Validate và set authentication
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if (jwtUtil.validateToken(jwt, userDetails)) {  // 5. Validate token
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities()
                        );
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);  // 6. Set vào context
            }
        }

        filterChain.doFilter(request, response);  // 7. Tiếp tục filter chain
    }
}
```

## Ví dụ thực tế

### Request 1: Login (Public endpoint)
```
GET /api/auth/candidate/login
No Authorization header
```
**FilterChain flow:**
1. ❌ JwtAuthenticationFilter: Không có token → Skip
2. ⏭️ UsernamePasswordAuthenticationFilter: Skip
3. ⏭️ ExceptionTranslationFilter: Pass
4. ✅ AuthorizationFilter: URL matched permitAll() → Allow
5. ✅ Controller xử lý login

### Request 2: Protected endpoint với token hợp lệ
```
GET /api/candidate/profile
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```
**FilterChain flow:**
1. ✅ JwtAuthenticationFilter: Token hợp lệ → Set Authentication
2. ⏭️ UsernamePasswordAuthenticationFilter: Skip
3. ⏭️ ExceptionTranslationFilter: Pass
4. ✅ AuthorizationFilter: User authenticated → Allow
5. ✅ Controller xử lý request

### Request 3: Protected endpoint không có token
```
GET /api/candidate/profile
(No Authorization header)
```
**FilterChain flow:**
1. ❌ JwtAuthenticationFilter: Không có token → Skip (no authentication)
2. ⏭️ UsernamePasswordAuthenticationFilter: Skip
3. ⏭️ ExceptionTranslationFilter: Pass
4. ❌ AuthorizationFilter: No authentication → **403 Forbidden**
5. ❌ Request bị chặn, không đến controller

### Request 4: Protected endpoint với token không hợp lệ
```
GET /api/candidate/profile
Authorization: Bearer invalid_token_here
```
**FilterChain flow:**
1. ❌ JwtAuthenticationFilter: Token không hợp lệ → Skip (no authentication)
2. ⏭️ UsernamePasswordAuthenticationFilter: Skip
3. ⏭️ ExceptionTranslationFilter: Pass
4. ❌ AuthorizationFilter: No authentication → **403 Forbidden**
5. ❌ Request bị chặn

## Tóm tắt

**FilterChain được dùng để:**
1. ✅ Validate JWT token trong mọi request
2. ✅ Set authentication info vào SecurityContext
3. ✅ Kiểm tra quyền truy cập endpoints
4. ✅ Cho phép public endpoints (login, register)
5. ✅ Chặn request không có authentication hợp lệ

**Ưu điểm của cách này:**
- 🔒 Bảo mật tự động cho mọi endpoint
- 🚀 Stateless - không cần session
- 📦 Dễ scale horizontally
- 🎯 Tập trung logic authentication ở một nơi
- 🔧 Dễ maintain và test

**Filter order quan trọng:**
- JwtAuthenticationFilter phải chạy **TRƯỚC** UsernamePasswordAuthenticationFilter
- Vì thế mới dùng `.addFilterBefore()`


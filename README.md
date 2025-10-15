## 1. Cấu hình file application.properties

- Sử dụng file 'applicationExample.properties' đổi thành 'application.properties' và thay thế các value trong đó

## 2. Hướng dẫn test API với Postman

    {{base-url}} = http://locahost:8080

### 2.1. Đăng nhập và lấy token

- Gửi POST tới: `{{base-url}}/ace/auth/login`
- Body (JSON):

```json
{
  "username": "your_email_or_phone",
  "password": "your_password"
}
```

- Lấy token từ response để sử dụng cho các API cần xác thực.

### 2.2. Test API sản phẩm

- **Tạo sản phẩm**

  - Method: POST
  - URL: `{{base-url}}/ace/products`
  - Body: `form-data` (có thể upload ảnh)
    - Trường: name, price, description, images (chọn file ảnh)

- **Cập nhật sản phẩm**

  - Method: PUT
  - URL: `{{base-url}}/ace/products/{id}`
  - Body: `form-data` (có thể upload ảnh mới)

- **Lấy danh sách sản phẩm**
  - Method: GET
  - URL: `{{base-url}}/ace/products`

### 2.3. Test API user

- **Cập nhật thông tin cá nhân**
  - Method: PUT
  - URL: `{{base-url}}/ace/user/profile`
  - Header: `Authorization: Bearer <token>`
  - Body (JSON):

```json
{
  "firstName": "Test",
  "lastName": "User",
  "phone": "0123456789",
  "address": "Hanoi",
  "gender": "male",
  "dob": "2000-01-01"
}
```

- **Đổi mật khẩu**
  - Method: PUT
  - URL: `{{base-url}}/ace/user/change-password`
  - Header: `Authorization: Bearer <token>`
  - Body (JSON):

```json
{
  "oldPassword": "yourOldPassword",
  "newPassword": "yourNewPassword"
}
```

### 2.4. Test API admin

- **Lấy danh sách user**

  - Method: GET
  - URL: `{{base-url}}/ace/admin/users`
  - Header: `Authorization: Bearer <admin-token>`

- **Vô hiệu hóa user**

  - Method: PUT
  - URL: `{{base-url}}/ace/admin/users/{id}/disable`
  - Header: `Authorization: Bearer <admin-token>`

- **Cập nhật quyền user**
  - Method: PUT
  - URL: `{{base-url}}/ace/admin/role?userId=1&roleName=ADMIN`
  - Header: `Authorization: Bearer <admin-token>`

### 2.5. Test API lọc/tìm kiếm sản phẩm

- **Tìm kiếm sản phẩm nâng cao**
  - Method: POST
  - URL: `{{base-url}}/ace/products/search`
  - Body (JSON):

```json
{
  "keyword": "laptop",
  "category": "Gaming",
  "minPrice": 10000000,
  "maxPrice": 30000000,
  "page": 0,
  "size": 10,
  "sortBy": "price",
  "sortDir": "asc"
}
```

- Kết quả trả về là danh sách sản phẩm phù hợp với điều kiện lọc, có thể phân trang và sắp xếp.

## 3. Test API với Swagger

- Truy cập Swagger để xem chi tiết các API:
  - `http://localhost:8080/swagger-ui/index.html`
- Có thể test trực tiếp các API trên giao diện này.

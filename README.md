## 1. Cấu hình

- Sao chép `applicationExample.properties` thành `application.properties` và cập nhật các giá trị (DB, cloudinary, jwt...).

BASE URL (local):

```
{{base-url}} = http://localhost:8080
```

---

## 2. Authentication (Auth)

### 2.1. Đăng ký (Register)
- Method: POST
- URL: `{{base-url}}/ace/auth/register`
- Body (JSON):

```json
{
  "email": "user@example.com",
  "password": "P@ssw0rd",
  "firstName": "John",
  "lastName": "Doe"
}
```

### 2.2. Đăng nhập (Login)
- Method: POST
- URL: `{{base-url}}/ace/auth/login`
- Body (JSON):

```json
{
  "username": "user@example.com",
  "password": "P@ssw0rd"
}
```

- Response: chứa access token (Bearer). Dùng header `Authorization: Bearer <token>` cho các API cần auth.

### 2.3. Email verify / reset
- Verify: `GET {{base-url}}/ace/auth/verify?token=...`
- Forgot password: `POST {{base-url}}/ace/auth/forgot-password` (body contains email)
- Reset password: `POST {{base-url}}/ace/auth/reset-password` (body contains token + newPassword)

---

## 3. Products

### Create product (admin)
- Method: POST
- URL: `{{base-url}}/ace/products`
- Body: `form-data` (supports files)
  - fields: `name` (string), `price` (number), `category`, `description`, `images` (file[])

### Update product (admin)
- Method: PUT
- URL: `{{base-url}}/ace/products/{id}`
- Body: `form-data` (same fields)

### Get products
- Method: GET
- URL: `{{base-url}}/ace/products`

### Get product by id
- Method: GET
- URL: `{{base-url}}/ace/products/{id}`

### Search / filter products
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

Note: `price` fields use decimal values (BigDecimal in code). Provide numbers or decimals as appropriate.

---

## 4. User APIs

### Update profile
- Method: PUT
- URL: `{{base-url}}/ace/user/profile`
- Header: `Authorization: Bearer <token>`
- Body (JSON example):

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

### Change password
- Method: PUT
- URL: `{{base-url}}/ace/user/change-password`
- Header: `Authorization: Bearer <token>`
- Body:

```json
{
  "oldPassword": "old",
  "newPassword": "new"
}
```

---

## 5. Admin APIs

- List users: `GET {{base-url}}/ace/admin/users` (Requires ROLE_ADMIN)
- Disable user: `PUT {{base-url}}/ace/admin/users/{id}/disable` (Requires ROLE_ADMIN)
- Change role: `PUT {{base-url}}/ace/admin/role?userId=1&roleName=ADMIN` (Requires ROLE_ADMIN)

---

## 6. Cart (Giỏ hàng)

> Cart endpoints now use the authenticated user (JWT) and do not accept a `userId` path parameter. Pass the Authorization header: `Authorization: Bearer <token>`.

### Add to cart
- Method: POST
- URL: `{{base-url}}/ace/cart/add`
- Header: `Authorization: Bearer <token>`
- Body:

```json
{
  "productId": 1,
  "quantity": 2
}
```

### Get cart
- Method: GET
- URL: `{{base-url}}/ace/cart`
- Header: `Authorization: Bearer <token>`

### Update cart item quantity
- Method: PUT
- URL: `{{base-url}}/ace/cart/update/{productId}?quantity=3`
- Header: `Authorization: Bearer <token>`

### Remove item
- Method: DELETE
- URL: `{{base-url}}/ace/cart/remove/{productId}`
- Header: `Authorization: Bearer <token>`

### Clear cart
- Method: DELETE
- URL: `{{base-url}}/ace/cart/clear`
- Header: `Authorization: Bearer <token>`

---

## 7. Orders

### Create order (checkout)
- Method: POST
- URL: `{{base-url}}/ace/orders`
- Header: `Authorization: Bearer <token>`
- Body (JSON):

```json
{
  "pickup": false,
  "addressId": 1,
  "paymentMethod": "COD"  // or BANKING, MOMO, ZALOPAY
}
```

- Behavior (current implementation):
  - The service reads the authenticated user's cart, validates stock, computes totals, decrements stock, stores order and items, then clears the cart.
  - Order status is initially `PENDING`.

### List user's orders
- Method: GET
- URL: `{{base-url}}/ace/orders`
- Header: `Authorization: Bearer <token>`

### Get order detail
- Method: GET
- URL: `{{base-url}}/ace/orders/{orderId}`
- Header: `Authorization: Bearer <token>`

### Admin: update order status
- Method: PUT
- URL: `{{base-url}}/ace/admin/orders/{orderId}/status`
- Body: `{ "status": "SHIPPED" }` (Requires ROLE_ADMIN)

Notes:
- Current flow decrements stock at order creation. If you plan to integrate async payment gateways, consider switching to a reserve-and-confirm model.

---

## 8. Swagger UI

- If you enabled Swagger (springdoc), visit:

```
http://localhost:8080/swagger-ui/index.html
```

---

## 9. Quick local build & run

Build (skip tests):

```powershell
.\mvnw -DskipTests package
.\mvnw spring-boot:run
```

---

If you want, I can also add example Postman collection JSON or expand the README with request/response samples for each endpoint (including example responses). Let me know which endpoints you want full examples for.

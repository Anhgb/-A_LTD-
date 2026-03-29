# 🧢 Snapback Store API Documentation (Full Version)

## 📌 Overview

This document defines the API structure for the **Snapback Fashion Store
Android App**.

-   **Base URL:** `https://api.snapbackstore.vn/v1`
-   **Authentication:** Bearer Token (JWT)
-   **Response Format:** JSON

------------------------------------------------------------------------

## 🔐 1. Authentication

### 1.1 Register

**POST** `/auth/register`

**Request**

``` json
{
  "full_name": "Nguyen Van A",
  "email": "customer@example.com",
  "password": "hashed_password",
  "phone": "0901234567"
}
```

**Response**

``` json
{
  "status": "success",
  "message": "User registered successfully",
  "user_id": "u123"
}
```

------------------------------------------------------------------------

### 1.2 Login

**POST** `/auth/login`

**Response**

``` json
{
  "token": "access_token_string",
  "user": {
    "id": "u123",
    "name": "Nguyen Van A",
    "email": "customer@example.com",
    "avatar": "https://cdn.snapback.vn/avatars/u123.jpg"
  }
}
```

------------------------------------------------------------------------

## 👤 2. User Profile

### Get Profile

**GET** `/user/profile`

**Headers**

    Authorization: Bearer <token>

### Update Profile

**PUT** `/user/profile`

------------------------------------------------------------------------

## 🧢 3. Product

### Categories

**GET** `/categories`

### Products

**GET** `/products`

Query: - `category_id` - `search` - `sort` - `page`

### Product Detail

**GET** `/products/{id}`

------------------------------------------------------------------------

## ❤️ 4. Wishlist

**POST** `/wishlist/toggle`

------------------------------------------------------------------------

## 🛒 5. Orders

### Checkout

**POST** `/orders/checkout`

### Order History

**GET** `/orders/history`

### Tracking

**GET** `/orders/{order_id}/tracking`

------------------------------------------------------------------------

## ⚠️ 6. Error Codes

  Code   Meaning
  ------ --------------
  400    Bad Request
  401    Unauthorized
  403    Forbidden
  404    Not Found
  500    Server Error

------------------------------------------------------------------------

## 🚀 Suggested App Architecture (Android)

### Tech Stack

-   Retrofit + Gson
-   MVVM Architecture
-   ViewModel + LiveData
-   Repository Pattern

### Modules

-   Auth (Login/Register)
-   Home (Categories + Products)
-   Product Detail
-   Cart
-   Order
-   Profile

------------------------------------------------------------------------

## 📂 Project Structure

    com.snapback.app
    │── data
    │   ├── api
    │   ├── model
    │   └── repository
    │
    │── ui
    │   ├── auth
    │   ├── home
    │   ├── cart
    │   └── profile
    │
    │── utils

------------------------------------------------------------------------

## 🔑 Notes

-   All protected APIs require token
-   Use HTTPS
-   Handle token expiration (401)

------------------------------------------------------------------------

## ✅ Status

Ready for: - Android App Development - Backend Integration - Deployment

------------------------------------------------------------------------

✨ Author: Snapback Store Team

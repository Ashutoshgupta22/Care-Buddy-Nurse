package com.aspark.carebuddynurse.retrofit

enum class HttpStatusCode(val code: Int) {

   OK(200),
   UNAUTHORIZED(401),
   FORBIDDEN(403),
   FAILED(500)
}
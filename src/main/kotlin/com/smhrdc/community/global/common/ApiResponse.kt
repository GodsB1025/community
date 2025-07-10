package com.smhrdc.community.global.common

data class ApiResponse<T>(
    val status: ResultStatus,
    val data: T?,
    val message: String?
) {
    companion object{
        fun <T> success(data: T?, message: String? = null): ApiResponse<T>{
            return ApiResponse(ResultStatus.SUCCESS, data, message)
        }

        fun <T> error(message: String?): ApiResponse<T>{

            val errorMessage = if (message.isNullOrBlank()) "요청에 실패했습니다." else message
            return ApiResponse(ResultStatus.ERROR, null, message)
        }
    }
}

enum class ResultStatus{
    SUCCESS,
    ERROR
}
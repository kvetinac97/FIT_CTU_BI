package cz.wrzecond.restsys.task

import cz.wrzecond.restsys.R

enum class HttpStatus (val code: Int, val errorRes: Int = -1) {

    // Status list
    OK (200),
    CREATED (201),
    NO_CONTENT (204),

    BAD_REQUEST (400, R.string.error_bad_request),
    UNAUTHORIZED (401, R.string.error_unauthorized),
    FORBIDDEN (403, R.string.error_forbidden),
    NOT_FOUND (404, R.string.error_not_found),
    SERVER_ERROR (500, R.string.error_server),
    UNAVAILABLE (503, R.string.error_unavailable),
    METHOD_NOT_ALLOWED (505, R.string.error_method_not_allowed);

    // Helper function
    val isSuccess : Boolean
    get() = (code / 100) == 2

    companion object {
        fun fromCode (code: Int) = when (code) {
            200 -> OK
            201 -> CREATED
            204 -> NO_CONTENT
            400 -> BAD_REQUEST
            401 -> UNAUTHORIZED
            403 -> FORBIDDEN
            404 -> NOT_FOUND
            500 -> SERVER_ERROR
            503 -> UNAVAILABLE
            505 -> METHOD_NOT_ALLOWED
            else -> error("Unknown HTTP status with code $code")
        }
    }

}

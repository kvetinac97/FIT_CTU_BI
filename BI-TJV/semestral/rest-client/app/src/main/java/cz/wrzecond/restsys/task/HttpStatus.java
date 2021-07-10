package cz.wrzecond.restsys.task;

import android.util.Log;

import androidx.annotation.StringRes;

import cz.wrzecond.restsys.R;

public enum HttpStatus {

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

    private int code;
    private int errorRes;

    HttpStatus (int code) {
        this (code, -1);
    }
    HttpStatus (int code, @StringRes int errorRes) {
        this.code = code;
        this.errorRes = errorRes;
    }

    public int getErrorRes () {
        return errorRes;
    }
    public int getCode () {
        return code;
    }
    public boolean isSuccess () {
        return (code / 100) == 2;
    }

    public static HttpStatus fromCode (int code) {
        switch (code) {
            case 200:
                return OK;
            case 201:
                return CREATED;
            case 204:
                return NO_CONTENT;
            case 400:
                return BAD_REQUEST;
            case 401:
                return UNAUTHORIZED;
            case 403:
                return FORBIDDEN;
            case 404:
                return NOT_FOUND;
            case 500:
                return SERVER_ERROR;
            case 503:
                return UNAVAILABLE;
            case 505:
                return METHOD_NOT_ALLOWED;
        }
        Log.d("RestSys", "Unknown http status " + code);
        return null;
    }

}

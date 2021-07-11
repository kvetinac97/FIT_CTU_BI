package cz.cvut.fit.viewmodellivedata.networking.entity

class Resource<T>(val status: Int, val data: T?, val msg: String?) {

    val isSuccess: Boolean
        get() = status == SUCCESS_STATUS

    val isError: Boolean
        get() = status == ERROR_STATUS

    val isLoading: Boolean
        get() = status == LOADING_STATUS

    companion object {
        @JvmStatic
        val LOADING_STATUS = 0
        @JvmStatic
        val ERROR_STATUS = 1
        @JvmStatic
        val SUCCESS_STATUS = 2
    }

}

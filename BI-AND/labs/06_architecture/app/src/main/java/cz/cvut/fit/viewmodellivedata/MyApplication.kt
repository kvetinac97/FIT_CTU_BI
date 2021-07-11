package cz.cvut.fit.viewmodellivedata

import android.app.Application

/**
 * Created by gingo on 20.6.2017.
 */
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        var instance: MyApplication? = null
            private set
    }

}

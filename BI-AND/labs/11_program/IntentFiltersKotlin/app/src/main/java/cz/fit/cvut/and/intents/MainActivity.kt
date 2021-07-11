package cz.fit.cvut.and.intents

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat

class MainActivity : AppCompatActivity() {

    lateinit var txtChargingStatus: TextView

    private val chargingBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // [7] zavolejte metodu setupChargingState. V extras intentu pod klíčem BatteryManager.EXTRA_STATUS
            // naleznete status baterie, ktery metoda akceptuje
            setupChargingState(intent.getIntExtra(BatteryManager.EXTRA_STATUS, 0))
            // -- end --
        }
    }

    private fun setupChargingState (status: Int) {
        when (status) {
            BatteryManager.BATTERY_STATUS_CHARGING -> txtChargingStatus.setText(R.string.charging)
            BatteryManager.BATTERY_STATUS_DISCHARGING -> txtChargingStatus.setText(R.string.discharging)
            BatteryManager.BATTERY_STATUS_NOT_CHARGING -> txtChargingStatus.setText(R.string.not_charging)
            BatteryManager.BATTERY_STATUS_UNKNOWN -> txtChargingStatus.setText(R.string.unknown)
            BatteryManager.BATTERY_STATUS_FULL -> txtChargingStatus.setText(R.string.full)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txtChargingStatus = findViewById(R.id.txt_charging)
        val sendRecieversTextView = findViewById<TextView>(R.id.send_recievers)

        // Přečteme příjemce SEND Intentu

        // [1] přečtěte seznam Activit, které umí obsloužit Intent vracený metodou createSendIntent()
        val activityList = packageManager.queryIntentActivities(createSendIntent(), 0)
        // -- end --
        val sendReceiversText = StringBuilder()
        for (ri in activityList) {
            sendReceiversText.append(ri.loadLabel(packageManager))
            sendReceiversText.append('\n')
        }
        sendRecieversTextView.text = sendReceiversText.toString()

        findViewById<View>(R.id.share_button).setOnClickListener { share() }
    }

    private fun createSendIntent(): Intent {
        return ShareCompat.IntentBuilder.from(this)
            .setText("Srdečné pozdravy z https://fit.cvut.cz/ !")
            .setType("text/plain")
            .intent
    }

    private fun share() {
        // [3] implementujte sdílení pomocí intentu vytvořeného v createSendIntent()
        startActivity(createSendIntent())
        // -- end --
    }

    override fun onResume() {
        super.onResume()
        // [5] zaregistrujte chargingBroadcastReceiver implementovany vyse na akci Intent.ACTION_BATTERY_CHANGED
        registerReceiver(chargingBroadcastReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        // -- end --
    }

    override fun onPause() {
        super.onPause()
        // [6] nezapomeňte chargingBroadcastReceiver zase odregistrovat
        unregisterReceiver(chargingBroadcastReceiver)
        // -- end --
    }

}

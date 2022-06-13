package com.example.intentsandfilters

import android.app.SearchManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.AlarmClock
import android.provider.CalendarContract.Events
import android.provider.CalendarContract
import android.provider.ContactsContract
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.intentsandfilters.databinding.ActivityMainBinding
import com.google.android.gms.actions.NoteIntents

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        binding.alarmButton.setOnClickListener {
            createAlarm("Test alarm", 12, 0)
            Toast.makeText(this, "Alarm set", Toast.LENGTH_SHORT).show()
        }

        binding.timerButton.setOnClickListener {
            startTimer("Test timer", 12)
            Toast.makeText(this, "Timer set", Toast.LENGTH_SHORT).show()
        }

        binding.allAlarmButton.setOnClickListener {
            showAllAlarm()
            Toast.makeText(this, "All alarms", Toast.LENGTH_SHORT).show()
        }

        binding.eventButton.setOnClickListener {
            addEvent("Anna library intent practising", "Kotturpuram", 123, 125)
            Toast.makeText(this, "Event set", Toast.LENGTH_SHORT).show()
        }

        binding.addContactButton.setOnClickListener {
            insertContact("krish", "9790307426","krish71197@gmail.com")
            Toast.makeText(this, "Contact added", Toast.LENGTH_SHORT).show()
        }

        binding.composeEmailButton.setOnClickListener {
            composeEmail(arrayOf("krish71197@gmail.com"), "Sample mail", "Body of the mail for sample mail")
            Toast.makeText(this, "Email sent", Toast.LENGTH_SHORT).show()
        }

        binding.showMapButton.setOnClickListener {
            showMap(Uri.parse("geo:0,0?q=west+canal+bank+road+kottur"))
            Toast.makeText(this, "Location mapped", Toast.LENGTH_SHORT).show()
        }

        binding.addNoteButton.setOnClickListener { // It won't work because no proper app installed
            createNote("Sample Note", "This is test note message")
            Toast.makeText(this, "Note created", Toast.LENGTH_SHORT).show()
        }

        binding.callButton.setOnClickListener {
            callPhoneNumber("9790307426")  // it needs permission
            //dialPhoneNumber("9790307426")
        }

        binding.settingsButton.setOnClickListener {
            openWifiSettings()
        }

        binding.smsButton.setOnClickListener {
            composeSmsMessage("Hello buddy", "9840260652")
        }

        binding.loadUrlButton.setOnClickListener {
            openWebPage("https://www.google.com")
        }

        binding.webSearchButton.setOnClickListener {
            searchWeb("hello")
        }

        binding.selectContact.setOnClickListener {
            selectContact()
        }

    }

    private fun createAlarm(message: String, hour: Int, minutes: Int) {
        val intent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
            putExtra(AlarmClock.EXTRA_MESSAGE, message)
            putExtra(AlarmClock.EXTRA_HOUR, hour)
            putExtra(AlarmClock.EXTRA_MINUTES, minutes)
        }
       // if (intent.resolveActivity(packageManager) != null) { // from A11, package visibilty is hidden by default, so resolve activity returns null, but we can access them explicitly
        Toast.makeText(this, "${intent.resolveActivity(packageManager)}", Toast.LENGTH_SHORT).show()
            Toast.makeText(this, "intent resolved", Toast.LENGTH_SHORT).show()
            startActivity(intent) // It goes to the respective component i.e alarm app, if we don't want that then set skip ui to true
        //}
    }

    private fun startTimer(message: String, seconds: Int) { // set alarm permission needed here also
        val intent = Intent(AlarmClock.ACTION_SET_TIMER).apply {
            putExtra(AlarmClock.EXTRA_MESSAGE, message)
            putExtra(AlarmClock.EXTRA_LENGTH, seconds)
            putExtra(AlarmClock.EXTRA_SKIP_UI, true)
        }

        startActivity(intent)

    }

    private fun showAllAlarm() { // set alarm permission needed here also
        val intent = Intent(AlarmClock.ACTION_SHOW_ALARMS)

        startActivity(intent)
    }

    private fun addEvent(title: String, location: String, begin: Long, end: Long) {
        val intent = Intent(Intent.ACTION_INSERT).apply {
            data = Events.CONTENT_URI
            putExtra(Events.TITLE, title)
            putExtra(Events.EVENT_LOCATION, location)
            putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, begin)
            putExtra(CalendarContract.EXTRA_EVENT_END_TIME, end)
        }

        startActivity(intent)

    }

    private fun insertContact(name: String, phone: String, email: String) {
        val intent = Intent(Intent.ACTION_INSERT).apply {
            type = ContactsContract.Contacts.CONTENT_TYPE
            putExtra(ContactsContract.Intents.Insert.NAME, name)
            putExtra(ContactsContract.Intents.Insert.PHONE, phone)
            putExtra(ContactsContract.Intents.Insert.EMAIL, email)
        }
        startActivity(intent)
    }

    private fun composeEmail(addresses: Array<String>, subject: String, body: String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:") // only email apps should handle this
            putExtra(Intent.EXTRA_EMAIL, addresses)
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, body)
        }
        Intent()
        startActivity(intent)
    }


    private fun showMap(geoLocation: Uri) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = geoLocation
        }

        startActivity(intent)

    }

    private fun createNote(subject: String, text: String) {
        val intent = Intent(NoteIntents.ACTION_CREATE_NOTE).apply {
            putExtra(NoteIntents.EXTRA_NAME, subject)
            putExtra(NoteIntents.EXTRA_TEXT, text)
        }
        startActivity(intent)
    }

    private fun dialPhoneNumber(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }
        startActivity(intent)
    }

    private fun callPhoneNumber(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_CALL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }
        startActivity(intent)
    }


    private fun openWifiSettings() {
        val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
        startActivity(intent)
    }

    private fun searchWeb(query: String) {
        val intent = Intent(Intent.ACTION_WEB_SEARCH).apply {
            putExtra(SearchManager.QUERY, query)
        }
        startActivity(intent)

    }

    private fun composeSmsMessage(message: String, phoneNumber: String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("smsto:$phoneNumber")
            putExtra("sms_body", message) // It does not fill edit text
            //putExtra(Intent.EXTRA_STREAM, attachment)
        }

        startActivity(intent)

    }

    private fun openWebPage(url: String) {
        val webpage: Uri = Uri.parse(url)
        //val intent = Intent(Intent.ACTION_VIEW, webpage)

        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("https:$url")
        }

        startActivity(intent)
    }

    private fun selectContact(){
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = ContactsContract.Contacts.CONTENT_TYPE
        }
        selectContactLauncher.launch(intent)
    }

    private val selectContactLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val contactUri: Uri? = result.data?.data

            Toast.makeText(this, "${contactUri.toString()}", Toast.LENGTH_LONG).show()
        }

    }





}
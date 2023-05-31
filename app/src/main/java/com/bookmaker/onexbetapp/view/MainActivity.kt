package com.bookmaker.onexbetapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.bookmaker.onexbetapp.R
import com.bookmaker.onexbetapp.common.systemUtils.sharedPrefs
import com.bookmaker.onexbetapp.databinding.ActivityMainBinding
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = (supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment).findNavController()
        if(sharedPrefs.ageAskPassed){
            if (sharedPrefs.rulesConfirm){
                navigateTo(R.id.mainFragment)
            }else{
                navigateTo(R.id.conditionsAskFragment)
            }
        }
        val mDatabase: DatabaseReference = FirebaseDatabase.getInstance().reference
        mDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                try {
                    val map = dataSnapshot.value as Map<*, *>
                    if (map["AIzaSyAz8uTsY7WeE4iSb0rEm9Z-KSfYpDpTvi8"].toString() != "") {
                        Log.d("test", "url from firebase - $map")
                        if (sharedPrefs.link == ""){
                            sharedPrefs.link = "${map["url"]}"
                        }

                    }
                } catch (_: Exception) {
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

    }

    fun region(): Boolean{
        val tm = getSystemService(TelephonyManager::class.java)
        return !(resources.configuration.locales[0].country != "RU" || tm.networkCountryIso != "ru" ||
                Settings.Global.getString(contentResolver, Settings.Global.DEVICE_NAME).isBlank() || tm.simOperatorName.isBlank() || tm.simOperatorName == "T-Mobile")
    }

    fun navigateTo(id: Int){
        navController.navigate(id)
    }

    fun navigateTo(id: Int, data: Bundle){
        navController.navigate(id, data)
    }
    fun destroyApp(){
        finish()
    }
}
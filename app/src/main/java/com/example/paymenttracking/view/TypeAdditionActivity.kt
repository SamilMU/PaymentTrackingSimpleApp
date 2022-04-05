package com.example.paymenttracking.view

import android.content.DialogInterface
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.paymenttracking.bll.PaymentTypeLogic
import com.example.paymenttracking.model.PaymentTypeEntity
import com.example.paymenttracking.databinding.ActivityTypeAdditionBinding
import kotlin.collections.ArrayList

class TypeAdditionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTypeAdditionBinding

    /** Shared variables*/
    private lateinit var adap: ArrayAdapter<String>

    private var maxDaysAllowed = 0
    private var selectedSpinnerItem = ""
    private var isEditing = false
    private var receivedPaymentTypeObjId = 0

    /** Strings */
    private val gunlukStr = "Günlük"
    private val haftalikStr = "Haftalık"
    private val aylikStr = "Aylık"
    private val yillikStr = "Yıllık"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        populateSpinner()
        setActivityView()
        spinListener()
        clickListeners()
        dateETListener()

    }

    /** Views */
    private fun setActivityView() {
        binding = ActivityTypeAdditionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.spinnerAddtype.adapter = adap
    }

    /** Button click listeners */
    private fun clickListeners() {
        // Save Button
        binding.btnSaveAddtpye.setOnClickListener {
            // Check if there is a warning for time of period.
            if (binding.tvWarningAddtype.visibility == View.GONE) {

                // User input Check
                if (binding.etTitleAddtype.text.toString().isEmpty()) {
                    Toast.makeText(
                        this,
                        "Ödeme tipi isimsiz olamaz. Lütfen tipin ismini girin.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val newPaymentType = PaymentTypeEntity().apply {
                        title = binding.etTitleAddtype.text.toString()
                        period = selectedSpinnerItem
                        timeOfPeriod = 0
                    }

                    // If a period is not selected
                    if (selectedSpinnerItem == "") {
                        newPaymentType.period = null
                    }else if(selectedSpinnerItem == gunlukStr){
                        newPaymentType.timeOfPeriod = 0
                    }
                    else {
                        // Null check for timeOfPeriod,
                        if (binding.etTimeofAddtype.text.toString() != "" && !binding.etTimeofAddtype.text.toString().equals(null)
                        ) {
                            newPaymentType.timeOfPeriod =
                                binding.etTimeofAddtype.text.toString().toInt()
                        }
                    }
                    // Update or new addition check
                    val dbFuncResult : Boolean
                    if (isEditing) {
                        newPaymentType.id = receivedPaymentTypeObjId
                        dbFuncResult = PaymentTypeLogic.updatePaymentType(this, newPaymentType)
                    }else{
                        dbFuncResult = PaymentTypeLogic.addPaymentType(this, newPaymentType)
                    }

                    if (dbFuncResult){
                        val intent = Intent()
                        intent.putExtra("paymentTypeObject", newPaymentType)
                        setResult(RESULT_OK, intent)
                        finish()
                    }else{
                        Toast.makeText(
                            this,
                            "Ödeme tipini eklerken bir hata oluştu. Lütfen daha sonra tekrar deneyiniz!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                Toast.makeText(
                    this,
                    "Girilen tarih seçilen ödeme tipine uygun değildir. Lütfen uygun bir tarih giriniz!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        // Delete Button
        binding.btnDeleteAddtype.setOnClickListener {

            val adb = AlertDialog.Builder(this)
            adb.setTitle("Tip Silme")
            adb.setMessage("Bu ödeme tipini silmek istiyor musunuz ?")

            adb.setPositiveButton("Evet", DialogInterface.OnClickListener { _, _ ->

                val isDeleted = PaymentTypeLogic.deleteType(this, receivedPaymentTypeObjId)

                if (isDeleted) {
                    Toast.makeText(
                        this,
                        "Ödeme tipi silindi.",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(this, MainActivity::class.java)
                    intent.addFlags(FLAG_ACTIVITY_CLEAR_TASK)
                    intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(
                        this,
                        "Ödeme tipini silerken bir hata oluştu. Lütfen daha sonra tekrar deneyiniz!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
            adb.setNegativeButton("Hayır", null)

            adb.show()

        }
    }

    /** Populate spinner with fixed periods */
    private fun populateSpinner() {
        val periodList: ArrayList<String> = arrayListOf("", yillikStr, aylikStr, haftalikStr, gunlukStr)

        adap = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, periodList)

    }

    /** Spinner listener, used for warnings and view limitation depending on user input */
    private fun spinListener() {
        binding.spinnerAddtype.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    timeOfPeriodVisibility(true)
                    when (p2) {
                        0 -> {
                            selectedSpinnerItem = ""
                            binding.etTimeofAddtype.setText("")
                        }
                        1 -> {
                            maxDaysAllowed = 365
                            selectedSpinnerItem = yillikStr
                        }
                        2 -> {
                            maxDaysAllowed = 31
                            selectedSpinnerItem = aylikStr
                        }
                        3 -> {
                            maxDaysAllowed = 7
                            selectedSpinnerItem = haftalikStr
                        }
                        4 -> {
                            timeOfPeriodVisibility(false)
                            selectedSpinnerItem = gunlukStr
                        }
                    }
                    warningCheck()
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
    }

    /** Listener for user date input, used for realtime warning if not suitable*/
    private fun dateETListener() {
        // Text watcher to warn user realtime if input date is not suitable
        binding.etTimeofAddtype.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // If there is an input
                if (s.isNotEmpty()) {
                    // Textview of .gün
                    binding.tvTimeofAddtype.visibility = View.VISIBLE
                    warningCheck()
                } else {
                    binding.tvTimeofAddtype.visibility = View.GONE
                }
            }
        })
    }

    /** Warn user if input date is not suitable.*/
    fun warningCheck() {
        binding.tvWarningAddtype.visibility = View.GONE

        var inputInt = 0
        // Input Date in Int
        if (binding.etTimeofAddtype.text.toString() !=   "" && !binding.etTimeofAddtype.text.toString().equals(null)
        ) {
            inputInt = binding.etTimeofAddtype.text.toString().toInt()
        }

        // Comparison
        if (inputInt > maxDaysAllowed) {
            binding.tvWarningAddtype.visibility = View.VISIBLE
            binding.tvWarningAddtype.text =
                "Maksimum $maxDaysAllowed girilebilir! \nLütfen ödeme tipi veya tarihini düzenleyiniz!"
        }
    }

    /** Set visibility of timeOfPeriod eT depending on period choice (Disable if Daily) */
    fun timeOfPeriodVisibility(visible: Boolean) {
        if (visible) {
            binding.tvStAddtypeDate.visibility = View.VISIBLE
            binding.clTimeofperiodAddtype.visibility = View.VISIBLE
        } else {
            binding.tvStAddtypeDate.visibility = View.GONE
            binding.clTimeofperiodAddtype.visibility = View.GONE
        }
    }

    /** Custom Resume in case of Type edit */
    override fun onResume() {
        isEditing = false
        // Edit check
        if (intent.hasExtra("paymentTypeObject")) {
            binding.btnDeleteAddtype.visibility = View.VISIBLE
            isEditing = true

            val tempPaymentTypeObject =
                intent.getSerializableExtra("paymentTypeObject") as PaymentTypeEntity

            receivedPaymentTypeObjId = tempPaymentTypeObject.id

            // Set Title
            binding.etTitleAddtype.setText(tempPaymentTypeObject.title)

            if (!tempPaymentTypeObject.period.isNullOrEmpty()) {
                // Set Period
                when (tempPaymentTypeObject.period!!.lowercase()) {
                    yillikStr.lowercase() -> binding.spinnerAddtype.setSelection(1)
                    aylikStr.lowercase() -> binding.spinnerAddtype.setSelection(2)
                    haftalikStr.lowercase() -> binding.spinnerAddtype.setSelection(3)
                    gunlukStr.lowercase() -> binding.spinnerAddtype.setSelection(4)
                }
                // Set Time Of Period
                if (tempPaymentTypeObject.timeOfPeriod != null && tempPaymentTypeObject.timeOfPeriod != 0) {
                    binding.etTimeofAddtype.setText(tempPaymentTypeObject.timeOfPeriod.toString())
                }
            }
        }

        super.onResume()
    }
}
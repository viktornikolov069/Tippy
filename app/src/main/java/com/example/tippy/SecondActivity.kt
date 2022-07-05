package com.example.tippy

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tippy.databinding.ActivityMainBinding
import com.example.tippy.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnPastTips.setOnClickListener {
            finish()
        }

       val tipMemory = intent.getSerializableExtra("EXTRA_TIP_MEMORY") as TipMemory
       binding.tvBaseResultLabel.text = tipMemory.baseAmount
       binding.tvPercentResultLabel.text = tipMemory.percent
       binding.tvPeopleResultLabel.text = tipMemory.peopleCount.ifEmpty { "1" }
       binding.tvTipResultlabel.text = tipMemory.tipAmount
       binding.tvTotalResultLabel.text = tipMemory.totalAmount
    }
}
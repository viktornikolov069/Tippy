package com.example.tippy

import android.animation.ArgbEvaluator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import com.example.tippy.databinding.ActivityMainBinding

private const val TAG = "MainActivity"
private const val INITIAL_TIP_PERCENT = 15
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var peopleMoreThanOne = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*Default state of app*/
        binding.tvTipPercentLabel.text = "$INITIAL_TIP_PERCENT%"
        binding.seekBarTip.progress = INITIAL_TIP_PERCENT
        updateTipDescription(INITIAL_TIP_PERCENT)

        /*User changes start from here*/
        binding.seekBarTip.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.i(TAG, "onProgressChanged $progress")
                binding.tvTipPercentLabel.text = "$progress%"
                computeTipAndTotal(peopleMoreThanOne)
                updateTipDescription(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })


        binding.etPeopleCount.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (binding.etPeopleCount.text.isNotEmpty()) {
                    peopleMoreThanOne = true
                    computeTipAndTotal(peopleMoreThanOne)
                } else {
                    peopleMoreThanOne = false
                    computeTipAndTotal(peopleMoreThanOne)
                }
            }
        })

        binding.etBaseAmount.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                Log.i(TAG, "afterTextChanged $s")
                computeTipAndTotal(peopleMoreThanOne)
            }
        })

        //binding.btnPastTips.setOnClickListener {
        //    Intent(this, SecondActivity::class.java).also {
        //        //it.putExtra("EXTRA_PERSON", person)
        //        startActivity(it)
        //    }
        //}

        binding.btnSaveTip.setOnClickListener {
            val baseAmount = binding.etBaseAmount.text.toString()
            val percent = binding.tvTipPercentLabel.text.toString()
            val peopleCount = binding.etPeopleCount.text.toString()
            val tipAmount = binding.tvTipAmount.text.toString()
            val totalAmount = binding.tvTotalAmount.text.toString()
            val tipMemory = TipMemory(baseAmount, percent, peopleCount, tipAmount, totalAmount)
            Intent(this, SecondActivity::class.java).also {
                it.putExtra("EXTRA_TIP_MEMORY", tipMemory)
                startActivity(it)
            }
        }
    }

    private fun computeTipAndTotal(peopleMoreThanOne: Boolean) {
        if(binding.etBaseAmount.text.isEmpty()) {
            binding.tvTipAmount.text = ""
            binding.tvTotalAmount.text = ""
            return
        }


        // 1. Get the value of the base and tip percent
        val baseValue = binding.etBaseAmount.text.toString().toDouble()
        val tipPercent = binding.seekBarTip.progress
        // 2. Compute the tip and total
        val tipAmount = baseValue * tipPercent / 100
        val totalAmount = baseValue + tipAmount
        // 3. Update the UI
        if (peopleMoreThanOne) {
            val peopleCount = binding.etPeopleCount.text.toString().toInt()
            binding.tvTipAmount.text = "%.2f".format(tipAmount / peopleCount)
            binding.tvTotalAmount.text = "%.2f".format(totalAmount / peopleCount)
        } else {
            binding.tvTipAmount.text = "%.2f".format(tipAmount)
            binding.tvTotalAmount.text = "%.2f".format(totalAmount)
        }
    }

    private fun updateTipDescription(tipPercent: Int) {
        val tipDescription = when (tipPercent) {
            in 0..9 -> "Poor"
            in 10..14 -> "Acceptable"
            in 15..19 -> "Good"
            in 20..24 -> "Great"
            else -> "Amazing"
        }
        binding.tvTipDescription.text = tipDescription
        // Update the color based on the tip percent
        val maxValueSeekBar = binding.seekBarTip.max
        val color = ArgbEvaluator().evaluate(
            tipPercent.toFloat() / maxValueSeekBar,
            ContextCompat.getColor(this, R.color.color_worst_tip),
            ContextCompat.getColor(this, R.color.color_best_tip)
        ) as Int
        // The function above uses linear interpolation to determine the right color for the
        // current position of the seekBar.

        //The color variable should be cast to an integer that's why we're using "as"
        binding.tvTipDescription.setTextColor(color)
    }
}




















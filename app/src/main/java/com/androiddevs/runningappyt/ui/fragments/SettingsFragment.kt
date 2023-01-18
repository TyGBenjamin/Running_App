@file:Suppress("ktlint:import-ordering")

package com.androiddevs.runningappyt.ui.fragments

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.androiddevs.runningappyt.R
import com.androiddevs.runningappyt.databinding.FragmentSettingsBinding
import com.androiddevs.runningappyt.other.Constants
import com.androiddevs.runningappyt.other.Constants.BAR_COLOR
import com.androiddevs.runningappyt.other.Constants.CHART_COLOR
import com.androiddevs.runningappyt.other.Constants.CHART_LINE_COLOR
import com.androiddevs.runningappyt.other.Constants.KEY_NAME
import com.androiddevs.runningappyt.other.Constants.KEY_WEIGHT
import com.androiddevs.runningappyt.other.Constants.RADIO_DARK_MODE
import com.androiddevs.runningappyt.other.Constants.RADIO_DEFAULT_MODE
import com.androiddevs.runningappyt.other.Constants.RADIO_LIGHT_MODE
import com.androiddevs.runningappyt.other.Constants.USE_CHART_CUSTOM_COLOR
import com.google.android.material.snackbar.Snackbar
import com.skydoves.colorpickerview.ColorEnvelope
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.flag.FlagView
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.android.synthetic.main.activity_main.tvToolbarTitle
import kotlinx.android.synthetic.main.fragment_settings.radioChartCustom
import kotlinx.android.synthetic.main.fragment_settings.radioChartDefault

/**
 * [Fragment] to manage all user settings.
 *
 * @constructor instance of [SettingsFragment].
 */
@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    val binding: FragmentSettingsBinding get() = _binding!!

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        FragmentSettingsBinding.inflate(inflater, container, false).also {
            _binding = it
        }.root

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) = with(binding) {
        super.onViewCreated(view, savedInstanceState)
        loadFieldsFromSharedPref()
        btnApplyChanges.setOnClickListener {
            val success = applyChangesToSharedPref()
            if (success) {
                Snackbar.make(view, "Saved changes", Snackbar.LENGTH_LONG).show()
            } else {
                Snackbar.make(view, "Please fill out all the fields", Snackbar.LENGTH_LONG).show()
            }
        }
        radioChartCustom.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) loadColorPicker() else layoutChartColors.visibility = View.GONE
        }
        colorChart.setOnClickListener { loadColorPicker() }
    }

    private fun loadFieldsFromSharedPref() = with(binding) {
        val name = sharedPreferences.getString(KEY_NAME, "")
        val weight =
            sharedPreferences.getFloat(KEY_WEIGHT, Constants.Default.WEIGHT)
        val defaultMode = sharedPreferences.getBoolean(RADIO_DEFAULT_MODE, false)
        val lightMode = sharedPreferences.getBoolean(RADIO_LIGHT_MODE, false)
        val darkMode = sharedPreferences.getBoolean(RADIO_DARK_MODE, false)
        radioDefaultMode.isChecked = defaultMode
        radioLightMode.isChecked = lightMode
        radioDarkMode.isChecked = darkMode

        val isChartColorCustom = sharedPreferences.getBoolean(USE_CHART_CUSTOM_COLOR, false)
        radioChartDefault.isChecked = !isChartColorCustom
        radioChartCustom.isChecked = isChartColorCustom
        if (isChartColorCustom) {
            showChartColors(
                sharedPreferences.getInt(CHART_COLOR, -1),
                sharedPreferences.getInt(BAR_COLOR, -1),
                sharedPreferences.getInt(CHART_LINE_COLOR, -1)
            )
        }

        etName.setText(name)
        etWeight.setText(weight.toString())
    }

    private fun applyChangesToSharedPref(): Boolean = with(binding) {
        val nameText = etName.text.toString()
        val weightText = etWeight.text.toString()
        val radioDefaultMode = radioDefaultMode.isChecked
        val radioLightMode = radioLightMode.isChecked
        val radioDarkMode = radioDarkMode.isChecked
        val useCustomChartColor = radioChartCustom.isChecked

        if (nameText.isEmpty() || weightText.isEmpty()) {
            return false
        }

        sharedPreferences.edit()
            .putString(KEY_NAME, nameText)
            .putFloat(KEY_WEIGHT, weightText.toFloat())
            .putBoolean(RADIO_DEFAULT_MODE, radioDefaultMode)
            .putBoolean(RADIO_LIGHT_MODE, radioLightMode)
            .putBoolean(RADIO_DARK_MODE, radioDarkMode)
            .putBoolean(USE_CHART_CUSTOM_COLOR, useCustomChartColor)
            .apply()

        if (useCustomChartColor) {
            sharedPreferences.edit()
                .putInt(CHART_COLOR, (colorChart.background as ColorDrawable).color)
                .putInt(BAR_COLOR, (colorBar.background as ColorDrawable).color)
                .putInt(CHART_LINE_COLOR, colorChart.currentTextColor)
                .apply()
        }
        val toolbarText = "Let's go $nameText"
        requireActivity().tvToolbarTitle.text = toolbarText
        requireActivity().recreate()
        return true
    }

    private fun loadColorPicker() {
        val consts = object {
            val start = 1
            val end = 3
        }
        val builder = ColorPickerDialog.Builder(this.requireContext(), R.style.AlertDialogTheme)
            .setTitle(getString(R.string.colorPickerTitle))
            .setPreferenceName(getString(R.string.colorPickerName))
            .setPositiveButton(
                getString(R.string.colorPickerPos),
                ColorEnvelopeListener() { colorEnvelope, _ ->
                    val customColor = colorEnvelope.argb.slice(consts.start..consts.end)
                    showChartColors(
                        colorEnvelope.color,
                        getComplementaryColor(customColor),
                        getContrastingColor(customColor)
                    )
                }
            )
            .setNegativeButton(getString(R.string.dialogCancel)) { _, _ ->
                radioChartCustom.isChecked = false
                radioChartDefault.isChecked = true
            }
            .attachAlphaSlideBar(false)

        val view1 = builder.colorPickerView
        view1.flagView = CustomFlag(requireContext(), R.layout.layout_color_flag)
        builder.show()
    }

    private class CustomFlag(context: Context, layout: Int) : FlagView(context, layout) {
        private val textView: TextView = findViewById(R.id.flag_color_id)
        private val view: View = findViewById(R.id.flag_color_box)
        private val hex = 6

        override fun onRefresh(colorEnvelope: ColorEnvelope) {
            textView.text = "#${colorEnvelope.hexCode.takeLast(hex)}"
            view.setBackgroundColor(colorEnvelope.color)
            this.rotation = 0F
        }
    }

    private fun getComplementaryColor(rgb: List<Int>): Int {
        if (rgb.size != MAX_SIZE) return Color.WHITE
        return Color.rgb(COLOR_MAX - rgb[0], COLOR_MAX - rgb[1], COLOR_MAX - rgb[2])
    }

    private fun getContrastingColor(rgb: List<Int>): Int {
        if (rgb.size != MAX_SIZE) return Color.WHITE

        return if (rgb[0] * RED_WEIGHT + rgb[1] * GREEN_WEIGHT + rgb[2] * BLUE_WEIGHT < BREAKPOINT) {
            Color.WHITE
        } else {
            Color.BLACK
        }
    }

    private fun showChartColors(chartColor: Int, barColor: Int, lineColor: Int) = with(binding) {
        colorChart.setBackgroundColor(chartColor)
        colorChart.setTextColor(lineColor)
        colorBar.setBackgroundColor(barColor)
        colorBar.setTextColor(barColor)

        layoutChartColors.visibility = View.VISIBLE
    }

    companion object {
        const val MAX_SIZE = 3
        const val RED_WEIGHT = .299
        const val GREEN_WEIGHT = .587
        const val BLUE_WEIGHT = .114
        const val BREAKPOINT = 150
        const val COLOR_MAX = 255
    }
}

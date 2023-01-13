@file:Suppress("ktlint:import-ordering")

package com.androiddevs.runningappyt.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.androiddevs.runningappyt.databinding.FragmentSettingsBinding
import com.androiddevs.runningappyt.other.Constants
import com.androiddevs.runningappyt.other.Constants.KEY_NAME
import com.androiddevs.runningappyt.other.Constants.KEY_WEIGHT
import com.androiddevs.runningappyt.other.Constants.RADIO_DARK_MODE
import com.androiddevs.runningappyt.other.Constants.RADIO_DEFAULT_MODE
import com.androiddevs.runningappyt.other.Constants.RADIO_LIGHT_MODE
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.android.synthetic.main.activity_main.tvToolbarTitle

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
    savedInstanceState: Bundle?,
  ): View =
    FragmentSettingsBinding.inflate(inflater, container, false).also {
      _binding = it
    }.root

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?,
  ) = with(binding) {
    super.onViewCreated(view, savedInstanceState)
    loadFieldsFromSharedPref()
    btnApplyChanges.setOnClickListener {
      val success = applyChangesToSharedPref()
      if (success) {
        Snackbar.make(view, "Saved changes", Snackbar.LENGTH_LONG).show()
      } else {
        Snackbar.make(view, "Please fill out all the fields",
          Snackbar.LENGTH_LONG).show()
      }
    }
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
    etName.setText(name)
    etWeight.setText(weight.toString())
  }

  private fun applyChangesToSharedPref(): Boolean = with(binding) {
    val nameText = etName.text.toString()
    val weightText = etWeight.text.toString()
    val radioDefaultMode = radioDefaultMode.isChecked
    val radioLightMode = radioLightMode.isChecked
    val radioDarkMode = radioDarkMode.isChecked

    if (nameText.isEmpty() || weightText.isEmpty()) {
      return false
    }

    sharedPreferences.edit()
      .putString(KEY_NAME, nameText)
      .putFloat(KEY_WEIGHT, weightText.toFloat())
      .putBoolean(RADIO_DEFAULT_MODE, radioDefaultMode)
      .putBoolean(RADIO_LIGHT_MODE, radioLightMode)
      .putBoolean(RADIO_DARK_MODE, radioDarkMode)
      .apply()
    val toolbarText = "Let's go $nameText"
    requireActivity().tvToolbarTitle.text = toolbarText
    requireActivity().recreate()
    return true
  }
}

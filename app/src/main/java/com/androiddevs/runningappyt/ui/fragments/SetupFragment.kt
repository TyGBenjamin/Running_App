@file:Suppress("ktlint:import-ordering")

package com.androiddevs.runningappyt.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.androiddevs.runningappyt.R
import com.androiddevs.runningappyt.other.Constants.KEY_FIRST_TIME_TOGGLE
import com.androiddevs.runningappyt.other.Constants.KEY_NAME
import com.androiddevs.runningappyt.other.Constants.KEY_WEIGHT
import com.androiddevs.runningappyt.other.showSnack
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import javax.inject.Inject
import kotlinx.android.synthetic.main.activity_main.tvToolbarTitle
import kotlinx.android.synthetic.main.fragment_setup.etName
import kotlinx.android.synthetic.main.fragment_setup.etWeight
import kotlinx.android.synthetic.main.fragment_setup.tvContinue

/**
 * [Fragment] to manage user detail entry.
 *
 * @constructor Create instance of [SetupFragment]
 */
@AndroidEntryPoint
class SetupFragment : Fragment(R.layout.fragment_setup) {

    @Inject
    lateinit var sharedPref: SharedPreferences

    @set:Inject
    var isFirstAppOpen = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!isFirstAppOpen) {
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.setupFragment, true)
                .build()
            findNavController().navigate(
                R.id.action_setupFragment_to_runFragment,
                savedInstanceState,
                navOptions
            )
        }

        tvContinue.setOnClickListener {
            val success = writePersonalDataToSharedPref()
            if (success) {
                findNavController().navigate(R.id.action_setupFragment_to_runFragment)
            } else {
                requireView().showSnack("Please enter all the fields")
            }
        }
    }

    private fun writePersonalDataToSharedPref(): Boolean = with(sharedPref.edit()) {
        val name = etName.text.toString()
        val weight = etWeight.text.toString()
        if (name.isEmpty() || weight.isEmpty()) return false
        putString(KEY_NAME, name)
        putFloat(KEY_WEIGHT, weight.toFloat())
        putBoolean(KEY_FIRST_TIME_TOGGLE, false)
        apply()
        requireActivity().tvToolbarTitle.text =
            String.format(Locale.getDefault(), "Let's go, %s", name)
        return true
    }
}

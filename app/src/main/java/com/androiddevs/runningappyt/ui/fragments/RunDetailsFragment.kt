package com.androiddevs.runningappyt.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * Run details fragment.
 *
 * @constructor Create empty Run details fragment
 */
@AndroidEntryPoint
class RunDetailsFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireActivity()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
//                Column(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .background(Color.Black)
//                )
            }
        }
    }
}

//@Composable
//fun ShowRunDetails(){
//
//}

@Composable
fun IconOne() {
    Box(
        modifier = Modifier.padding(5.dp)
    )
}

/**
 *
 */
@Composable
fun DividerOne() {
    Divider(
        color = Color.White.copy(alpha = 0.3f),
        thickness = 1.dp,
        modifier = Modifier.padding(top = 48.dp)
    )
}

@Composable
fun Label(typeInput: String) {
    Text(
        text = typeInput,
        fontSize = 9.sp
    )
}



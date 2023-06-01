package com.julianotalora.musicplayer.common

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.julianotalora.musicplayer.R

@Composable
fun ProgressPopup(loadingState: State<Boolean>) {
    if(loadingState.value){
        Dialog(
            onDismissRequest = {},
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            )
        ) {
            PlayerCircularProgressIndicator(loadingState = loadingState)
        }
    }
}

@Composable
fun PlayerCircularProgressIndicator(modifier: Modifier = Modifier, loadingState: State<Boolean>) {
    if (loadingState.value) {
        val progressIndicatorContentDescription = stringResource(id = R.string.progress_description)
        CircularProgressIndicator(
            modifier = modifier
                .size(dimensionResource(id = R.dimen.progress_indicator_size))
                .semantics { contentDescription = progressIndicatorContentDescription },
            color = colorResource(id = R.color.purple_200),
            strokeWidth = dimensionResource(id = R.dimen.progress_indicator_stroke_width)
        )
    } else {
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.progress_indicator_size)))
    }
}
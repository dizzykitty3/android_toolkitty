package me.dizzykitty3.androidtoolkitty.ui.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer
import me.dizzykitty3.androidtoolkitty.R
import me.dizzykitty3.androidtoolkitty.ui_components.Tip
import me.dizzykitty3.androidtoolkitty.ui_components.TopPadding

@Composable
fun Licenses() {
    Column {
        val screenPadding = dimensionResource(id = R.dimen.padding_screen)
        TopPadding()
        Box(
            Modifier.padding(
                start = screenPadding,
                end = screenPadding,
                bottom = screenPadding
            )
        ) { Tip(R.string.auto_generated_by_about_libraries) }
        LibrariesContainer()
    }
}
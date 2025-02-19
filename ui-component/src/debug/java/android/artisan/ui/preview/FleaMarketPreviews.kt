package android.artisan.ui.preview

import android.artisan.ui.component.shared.SharedUIController
import android.artisan.ui.component.snackbar.model.SnackbarDetails
import android.artisan.ui.compositionlocal.LocalNavController
import android.artisan.ui.compositionlocal.LocalSharedUIController
import android.artisan.ui.compositionlocal.LocalWindowSizeClass
import android.artisan.ui.theme.FleaMarketTheme
import android.content.res.Configuration
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.material.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.navigation.compose.rememberNavController

@Preview(group = "Day", name = "Phone - Portrait", device = "spec:width=411dp,height=891dp")
@Preview(
    group = "Night",
    name = "Phone - Portrait",
    device = "spec:width=411dp,height=891dp",
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Preview(
    group = "Day",
    name = "Phone - Landscape",
    device = "spec:width=411dp,height=891dp,orientation=landscape"
)
@Preview(
    group = "Night",
    name = "Phone - Landscape",
    device = "spec:width=411dp,height=891dp,orientation=landscape",
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Preview(
    group = "Day",
    name = "Tablet - Landscape",
    device = "spec:width=1280dp,height=800dp,dpi=240"
)
@Preview(
    group = "Night",
    name = "Tablet - Landscape",
    device = "spec:width=1280dp,height=800dp,dpi=240",
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Preview(
    group = "Day",
    name = "Tablet - Portrait",
    device = "spec:width=1280dp,height=800dp,orientation=portrait"
)
@Preview(
    group = "Night",
    name = "Tablet - Portrait",
    device = "spec:width=1280dp,height=800dp,orientation=portrait",
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
public annotation class FleaMarketPreviews

@Suppress("ModifierMissing")
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
public fun FleaMarketThemePreview(content: @Composable () -> Unit = {}) {
    FleaMarketTheme {
        BoxWithConstraints {
            val calculateFromSize =
                WindowSizeClass.calculateFromSize(DpSize(width = maxWidth, height = maxHeight))
            CompositionLocalProvider(
                LocalWindowSizeClass provides calculateFromSize,
                LocalNavController provides rememberNavController(),
                LocalSharedUIController provides sharedUIController()
            ) {
                Surface {
                    content()
                }
            }
        }
    }
}

private fun sharedUIController() = object : SharedUIController {
    override fun showSnackbar(snackbarDetails: SnackbarDetails) {
        // do nothing
    }

    override fun resetSnackbarDetails() {
        // do nothing
    }
}

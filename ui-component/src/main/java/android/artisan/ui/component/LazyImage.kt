package android.artisan.ui.component

import android.artisan.ui.modifier.shimmer
import android.artisan.ui.theme.extraColors
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.SubcomposeAsyncImage

@Composable
public fun LazyImage(
    url: String,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    contentScale: ContentScale = ContentScale.Crop
) {
    SubcomposeAsyncImage(
        model = url,
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = contentScale,
        loading = { Box(modifier = Modifier.shimmer()) },
        error = { Box(modifier = Modifier.background(MaterialTheme.extraColors.darkGrey)) }
    )
}

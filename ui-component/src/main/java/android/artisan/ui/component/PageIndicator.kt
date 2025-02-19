package android.artisan.ui.component

import android.artisan.ui.theme.extraColors
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
public fun PageIndicator(totalPages: Int, currentPage: Int, modifier: Modifier = Modifier) {
    if (totalPages == 1) return
    Row(modifier = modifier, horizontalArrangement = Arrangement.Center) {
        repeat(totalPages) {
            val color = if (currentPage == it) {
                MaterialTheme.colors.primary
            } else {
                MaterialTheme.extraColors.deselectedPageIndicator
            }
            Box(
                Modifier
                    .size(24.dp)
                    .padding(6.dp)
                    .clip(CircleShape)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colors.secondary,
                        shape = CircleShape
                    )
                    .background(color)
            )
        }
    }
}

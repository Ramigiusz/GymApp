package com.example.gymapp.ui.components

import android.content.Context
import android.net.Uri
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerView

@Composable
fun VideoPlayer(videoUrl: String) {
    val context = LocalContext.current
    val density = LocalDensity.current
    val videoHeightPx = with(density) { 200.dp.toPx() }

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            // Konwersja videoUrl na Uri i ustawienie jako zasobu raw
            val uri = Uri.parse(videoUrl)
            setMediaItem(MediaItem.fromUri(uri))
            prepare()
            playWhenReady = true
        }
    }

    AndroidView(
        factory = { ctx ->
            PlayerView(ctx).apply {
                player = exoPlayer
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    videoHeightPx.toInt()
                )
            }
        }
    )

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }
}
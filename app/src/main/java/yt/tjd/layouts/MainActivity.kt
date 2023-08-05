@file:OptIn(ExperimentalTextApi::class)

package yt.tjd.layouts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import yt.tjd.layouts.ui.theme.LayoutsTheme
import kotlin.math.PI
import kotlin.random.Random

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      LayoutsTheme {
        // A surface container using the 'background' color from the theme
        Surface(modifier = Modifier.systemBarsPadding()) {


          var slices by rememberSaveable { mutableStateOf(6) }

          Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {

            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
              IconButton(onClick = { slices -= 1 }) {
                Icon(imageVector = Icons.Default.Remove, contentDescription = null)
              }
              Text(text = slices.toString())
              IconButton(onClick = { slices += 1 }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
              }
            }

            Spacer(modifier = Modifier.height(24.dp))

            RadialGrid(
              modifier = Modifier.weight(1f)
            ) {

              val sweep by remember { derivedStateOf { (360f / slices) }}

              repeat(slices) {
                val color = Color(Random.nextInt())
                PieSlice(
                  sweep = sweep,
                  color = color,
                  modifier = Modifier.size(100.dp).sweep(sweep)
                )
              }
            }
          }
        }
      }
    }
  }
}

@Composable
fun PieSlice(
  sweep: Float,
  color: Color,
  modifier: Modifier = Modifier
) {

  val textMeasurer = rememberTextMeasurer()

  val textResult = textMeasurer.measure("${((sweep / 360f) * 100).toInt()} Percent")

  Canvas(
    modifier = modifier
  ) {
    drawArc(color = color, 360f - (sweep / 2f), sweep, useCenter = true, topLeft = Offset(x = -size.width, y = -size.height / 2), size = size * 2f)
    drawText(textResult, topLeft = Offset((size.width - textResult.size.width) / 2f, (size.height / 2f) - (textResult.size.height / 2f)))
  }
}

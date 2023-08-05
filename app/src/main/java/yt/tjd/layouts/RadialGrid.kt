package yt.tjd.layouts

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt


@Composable
fun RadialGrid(
  modifier: Modifier = Modifier,
  content: @Composable RadialGridScope.() -> Unit
) {

  val radialContent = @Composable { RadialGridScope().content() }

  Layout(content = radialContent, modifier = modifier) { measurables, constraints ->

    val placeables = measurables.map { it.measure(constraints.copy(minWidth = 0, minHeight = 0)) }

    layout(constraints.maxWidth, constraints.maxHeight) {
      var currentRadius = 0f
      val center = IntOffset(constraints.maxWidth / 2, constraints.maxHeight / 2)

      var currentAngle = 0f // in Rads

      var nextRadius = currentRadius
      placeables.forEach {

        if (currentAngle >= (2f * PI).toFloat() - 0.001f) {
          currentAngle -= (2f * PI).toFloat()
          currentRadius = nextRadius
        }

        val x = (currentRadius + (it.width / 2)) * cos(currentAngle)
        val y = (currentRadius + (it.height / 2)) * sin(currentAngle)

        it.placeWithLayer(center.x + x.toInt() - (it.width / 2), center.y + y.toInt() - (it.height / 2)) {
          rotationZ = (currentAngle * 180f / PI).toFloat()
        }

        currentAngle += (it.parentData as RadialGridChildData?)?.sweep ?: (sin((currentRadius) / (it.height / 2)) * 2)

        val nextRadiusCandidate = currentRadius + sqrt((it.height).toDouble().pow(2) + (it.width).toDouble().pow(2)).toFloat()

        if (nextRadiusCandidate > nextRadius) nextRadius = nextRadiusCandidate
      }
    }
  }
}

class RadialGridScope {
  fun Modifier.sweep(sweep: Float) = then(RadialGridChildData((sweep * PI / 180f).toFloat()))
}

data class RadialGridChildData(val sweep: Float) : ParentDataModifier {
  override fun Density.modifyParentData(parentData: Any?) = this@RadialGridChildData
}
package com.example.housingroperty.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun BackArrowIcon(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFF1E293B),
    size: Dp = 24.dp
) {
    Canvas(modifier = modifier.size(size)) {
        val strokeWidth = 2.dp.toPx()
        val path = Path().apply {
            moveTo(size.toPx() * 0.65f, size.toPx() * 0.25f)
            lineTo(size.toPx() * 0.35f, size.toPx() * 0.5f)
            lineTo(size.toPx() * 0.65f, size.toPx() * 0.75f)
        }
        drawPath(
            path = path,
            color = color,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )
        drawLine(
            color = color,
            start = Offset(size.toPx() * 0.35f, size.toPx() * 0.5f),
            end = Offset(size.toPx() * 0.75f, size.toPx() * 0.5f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
    }
}

@Composable
fun CheckmarkIcon(
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    size: Dp = 16.dp
) {
    Canvas(modifier = modifier.size(size)) {
        val strokeWidth = 2.2f.dp.toPx()
        val path = Path().apply {
            moveTo(size.toPx() * 0.22f, size.toPx() * 0.5f)
            lineTo(size.toPx() * 0.42f, size.toPx() * 0.72f)
            lineTo(size.toPx() * 0.78f, size.toPx() * 0.28f)
        }
        drawPath(
            path = path,
            color = color,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )
    }
}

@Composable
fun GoogleLogoIcon(
    modifier: Modifier = Modifier,
    size: Dp = 22.dp
) {
    Canvas(modifier = modifier.size(size)) {
        val s = size.toPx()
        val center = Offset(s / 2f, s / 2f)
        val strokeW = s * 0.18f

        // Draw Google styled arcs
        // Red top arc
        drawArc(
            color = Color(0xFFEA4335),
            startAngle = 180f,
            sweepAngle = 140f,
            useCenter = false,
            topLeft = Offset(strokeW / 2, strokeW / 2),
            size = Size(s - strokeW, s - strokeW),
            style = Stroke(strokeW, cap = StrokeCap.Butt)
        )
        // Yellow left arc
        drawArc(
            color = Color(0xFFFBBC05),
            startAngle = 120f,
            sweepAngle = 60f,
            useCenter = false,
            topLeft = Offset(strokeW / 2, strokeW / 2),
            size = Size(s - strokeW, s - strokeW),
            style = Stroke(strokeW, cap = StrokeCap.Butt)
        )
        // Green bottom arc
        drawArc(
            color = Color(0xFF34A853),
            startAngle = 30f,
            sweepAngle = 90f,
            useCenter = false,
            topLeft = Offset(strokeW / 2, strokeW / 2),
            size = Size(s - strokeW, s - strokeW),
            style = Stroke(strokeW, cap = StrokeCap.Butt)
        )
        // Blue right arc & bar
        drawArc(
            color = Color(0xFF4285F4),
            startAngle = -40f,
            sweepAngle = 70f,
            useCenter = false,
            topLeft = Offset(strokeW / 2, strokeW / 2),
            size = Size(s - strokeW, s - strokeW),
            style = Stroke(strokeW, cap = StrokeCap.Butt)
        )
        // Horizontal bar for G
        drawLine(
            color = Color(0xFF4285F4),
            start = Offset(center.x, center.y),
            end = Offset(s - strokeW * 0.4f, center.y),
            strokeWidth = strokeW,
            cap = StrokeCap.Square
        )
    }
}

@Composable
fun CustomerAgentIcon(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFF334155),
    size: Dp = 26.dp
) {
    CustomerBuyerIcon(modifier = modifier, color = color, size = size)
}

@Composable
fun CustomerBuyerIcon(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFF334155),
    size: Dp = 26.dp
) {
    Canvas(modifier = modifier.size(size)) {
        val s = size.toPx()
        val strokeW = 1.8f.dp.toPx()

        // Head
        drawCircle(
            color = color,
            radius = s * 0.18f,
            center = Offset(s * 0.5f, s * 0.32f),
            style = Stroke(width = strokeW)
        )

        // Shoulders
        val bodyPath = Path().apply {
            moveTo(s * 0.22f, s * 0.85f)
            cubicTo(
                s * 0.24f, s * 0.62f,
                s * 0.76f, s * 0.62f,
                s * 0.78f, s * 0.85f
            )
        }
        drawPath(
            path = bodyPath,
            color = color,
            style = Stroke(width = strokeW, cap = StrokeCap.Round)
        )
    }
}

@Composable
fun PartnerAgentIcon(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFF334155),
    size: Dp = 26.dp
) {
    Canvas(modifier = modifier.size(size)) {
        val s = size.toPx()
        val strokeW = 1.8f.dp.toPx()

        // Briefcase body
        drawRoundRect(
            color = color,
            topLeft = Offset(s * 0.16f, s * 0.38f),
            size = Size(s * 0.68f, s * 0.48f),
            cornerRadius = CornerRadius(3.dp.toPx(), 3.dp.toPx()),
            style = Stroke(width = strokeW)
        )

        // Briefcase handle
        val handlePath = Path().apply {
            moveTo(s * 0.36f, s * 0.38f)
            lineTo(s * 0.36f, s * 0.24f)
            lineTo(s * 0.64f, s * 0.24f)
            lineTo(s * 0.64f, s * 0.38f)
        }
        drawPath(
            path = handlePath,
            color = color,
            style = Stroke(width = strokeW, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )

        // Center latch line
        drawLine(
            color = color,
            start = Offset(s * 0.5f, s * 0.52f),
            end = Offset(s * 0.5f, s * 0.64f),
            strokeWidth = strokeW,
            cap = StrokeCap.Round
        )
    }
}

@Composable
fun ShieldAdminIcon(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFF334155),
    size: Dp = 26.dp
) {
    Canvas(modifier = modifier.size(size)) {
        val s = size.toPx()
        val strokeW = 1.8f.dp.toPx()

        val shieldPath = Path().apply {
            moveTo(s * 0.5f, s * 0.15f)
            lineTo(s * 0.82f, s * 0.28f)
            cubicTo(
                s * 0.82f, s * 0.62f,
                s * 0.65f, s * 0.82f,
                s * 0.5f, s * 0.90f
            )
            cubicTo(
                s * 0.35f, s * 0.82f,
                s * 0.18f, s * 0.62f,
                s * 0.18f, s * 0.28f
            )
            close()
        }
        drawPath(
            path = shieldPath,
            color = color,
            style = Stroke(width = strokeW, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )
    }
}

@Composable
fun LockIcon(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFF1E293B),
    size: Dp = 32.dp
) {
    Canvas(modifier = modifier.size(size)) {
        val s = size.toPx()
        val strokeW = 2.2f.dp.toPx()

        // Shackle
        val shacklePath = Path().apply {
            moveTo(s * 0.32f, s * 0.44f)
            lineTo(s * 0.32f, s * 0.3f)
            cubicTo(s * 0.32f, s * 0.16f, s * 0.68f, s * 0.16f, s * 0.68f, s * 0.3f)
            lineTo(s * 0.68f, s * 0.44f)
        }
        drawPath(
            path = shacklePath,
            color = color,
            style = Stroke(width = strokeW, cap = StrokeCap.Round)
        )

        // Body
        drawRoundRect(
            color = color,
            topLeft = Offset(s * 0.22f, s * 0.44f),
            size = Size(s * 0.56f, s * 0.46f),
            cornerRadius = CornerRadius(s * 0.08f, s * 0.08f),
            style = Stroke(width = strokeW)
        )

        // Keyhole
        drawCircle(
            color = color,
            radius = s * 0.055f,
            center = Offset(s * 0.5f, s * 0.62f),
            style = Fill
        )
        drawLine(
            color = color,
            start = Offset(s * 0.5f, s * 0.62f),
            end = Offset(s * 0.5f, s * 0.74f),
            strokeWidth = strokeW,
            cap = StrokeCap.Round
        )
    }
}

@Composable
fun IndiaFlagIcon(
    modifier: Modifier = Modifier,
    width: Dp = 24.dp,
    height: Dp = 16.dp
) {
    Box(
        modifier = modifier
            .size(width, height)
            .clip(androidx.compose.foundation.shape.RoundedCornerShape(3.dp))
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val h = size.height
            val w = size.width
            val bandHeight = h / 3f

            // Saffron
            drawRect(
                color = Color(0xFFFF9933),
                topLeft = Offset.Zero,
                size = Size(w, bandHeight)
            )
            // White
            drawRect(
                color = Color(0xFFFFFFFF),
                topLeft = Offset(0f, bandHeight),
                size = Size(w, bandHeight)
            )
            // Green
            drawRect(
                color = Color(0xFF138808),
                topLeft = Offset(0f, bandHeight * 2),
                size = Size(w, bandHeight)
            )
            // Ashoka Chakra Navy Circle
            drawCircle(
                color = Color(0xFF000080),
                radius = bandHeight * 0.4f,
                center = Offset(w / 2, h / 2),
                style = Stroke(width = 1.dp.toPx())
            )
        }
    }
}

@Composable
fun LogoutDialogIcon(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFF1E293B),
    size: Dp = 32.dp
) {
    Canvas(modifier = modifier.size(size)) {
        val s = size.toPx()
        val strokeW = 2.2f.dp.toPx()

        // Bracket / Door opening: left vertical bar with top and bottom horizontal segments
        val doorPath = Path().apply {
            moveTo(s * 0.45f, s * 0.2f)
            lineTo(s * 0.25f, s * 0.2f)
            lineTo(s * 0.25f, s * 0.8f)
            lineTo(s * 0.45f, s * 0.8f)
        }
        drawPath(
            path = doorPath,
            color = color,
            style = Stroke(width = strokeW, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )

        // Arrow pointing right
        drawLine(
            color = color,
            start = Offset(s * 0.25f, s * 0.5f),
            end = Offset(s * 0.82f, s * 0.5f),
            strokeWidth = strokeW,
            cap = StrokeCap.Round
        )
        val arrowHead = Path().apply {
            moveTo(s * 0.64f, s * 0.32f)
            lineTo(s * 0.82f, s * 0.5f)
            lineTo(s * 0.64f, s * 0.68f)
        }
        drawPath(
            path = arrowHead,
            color = color,
            style = Stroke(width = strokeW, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )
    }
}

@Composable
fun ErrorCircleIcon(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFF1E293B),
    size: Dp = 34.dp
) {
    Canvas(modifier = modifier.size(size)) {
        val s = size.toPx()
        val strokeW = 2.2f.dp.toPx()
        val radius = (s - strokeW) / 2f

        // Outer circle
        drawCircle(
            color = color,
            radius = radius,
            center = Offset(s / 2f, s / 2f),
            style = Stroke(width = strokeW)
        )

        // X inside
        val xRadius = radius * 0.42f
        val center = s / 2f
        drawLine(
            color = color,
            start = Offset(center - xRadius, center - xRadius),
            end = Offset(center + xRadius, center + xRadius),
            strokeWidth = strokeW,
            cap = StrokeCap.Round
        )
        drawLine(
            color = color,
            start = Offset(center + xRadius, center - xRadius),
            end = Offset(center - xRadius, center + xRadius),
            strokeWidth = strokeW,
            cap = StrokeCap.Round
        )
    }
}

@Composable
fun ProhibitedCircleIcon(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFF1E293B),
    size: Dp = 34.dp
) {
    Canvas(modifier = modifier.size(size)) {
        val s = size.toPx()
        val strokeW = 2.2f.dp.toPx()
        val radius = (s - strokeW) / 2f
        val center = Offset(s / 2f, s / 2f)

        // Outer circle
        drawCircle(
            color = color,
            radius = radius,
            center = center,
            style = Stroke(width = strokeW)
        )

        // Diagonal slash through center (at 45 degree angle: top-right to bottom-left)
        val offset = radius * 0.7071f
        drawLine(
            color = color,
            start = Offset(center.x + offset, center.y - offset),
            end = Offset(center.x - offset, center.y + offset),
            strokeWidth = strokeW,
            cap = StrokeCap.Round
        )
    }
}

@Composable
fun WarningTriangleIcon(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFF1E293B),
    size: Dp = 34.dp
) {
    Canvas(modifier = modifier.size(size)) {
        val s = size.toPx()
        val strokeW = 2.2f.dp.toPx()

        // Outline triangle
        val path = Path().apply {
            moveTo(s * 0.5f, s * 0.16f)
            lineTo(s * 0.86f, s * 0.82f)
            lineTo(s * 0.14f, s * 0.82f)
            close()
        }
        drawPath(
            path = path,
            color = color,
            style = Stroke(width = strokeW, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )

        // Exclamation vertical line
        drawLine(
            color = color,
            start = Offset(s * 0.5f, s * 0.40f),
            end = Offset(s * 0.5f, s * 0.60f),
            strokeWidth = strokeW,
            cap = StrokeCap.Round
        )

        // Exclamation dot
        drawCircle(
            color = color,
            radius = strokeW * 0.6f,
            center = Offset(s * 0.5f, s * 0.72f),
            style = Fill
        )
    }
}

@Composable
fun BellOutlineIcon(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFF1E293B),
    size: Dp = 36.dp
) {
    Canvas(modifier = modifier.size(size)) {
        val s = size.toPx()
        val strokeW = 2.2f.dp.toPx()

        // Bell Body
        val bellPath = Path().apply {
            // Top hanger loop
            moveTo(s * 0.44f, s * 0.18f)
            cubicTo(s * 0.44f, s * 0.12f, s * 0.56f, s * 0.12f, s * 0.56f, s * 0.18f)

            // Bell dome
            moveTo(s * 0.5f, s * 0.18f)
            cubicTo(s * 0.30f, s * 0.22f, s * 0.28f, s * 0.55f, s * 0.20f, s * 0.68f)
            lineTo(s * 0.80f, s * 0.68f)
            cubicTo(s * 0.72f, s * 0.55f, s * 0.70f, s * 0.22f, s * 0.5f, s * 0.18f)
        }
        drawPath(
            path = bellPath,
            color = color,
            style = Stroke(width = strokeW, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )

        // Rim line
        drawLine(
            color = color,
            start = Offset(s * 0.16f, s * 0.68f),
            end = Offset(s * 0.84f, s * 0.68f),
            strokeWidth = strokeW,
            cap = StrokeCap.Round
        )

        // Bell clapper
        val clapperPath = Path().apply {
            moveTo(s * 0.42f, s * 0.72f)
            cubicTo(s * 0.42f, s * 0.84f, s * 0.58f, s * 0.84f, s * 0.58f, s * 0.72f)
        }
        drawPath(
            path = clapperPath,
            color = color,
            style = Stroke(width = strokeW, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )
    }
}

/**
 * Trash Outline Icon for Delete Confirm Dialog (AD15)
 */
@Composable
fun TrashOutlineIcon(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFF0F172A),
    size: Dp = 36.dp
) {
    Canvas(modifier = modifier.size(size)) {
        val s = size.toPx()
        val strokeW = 2.2f.dp.toPx()

        // Top lid handle arch
        val handlePath = Path().apply {
            moveTo(s * 0.40f, s * 0.20f)
            lineTo(s * 0.40f, s * 0.13f)
            cubicTo(s * 0.40f, s * 0.09f, s * 0.60f, s * 0.09f, s * 0.60f, s * 0.13f)
            lineTo(s * 0.60f, s * 0.20f)
        }
        drawPath(handlePath, color, style = Stroke(strokeW, cap = StrokeCap.Round, join = StrokeJoin.Round))

        // Lid horizontal line
        drawLine(
            color = color,
            start = Offset(s * 0.18f, s * 0.20f),
            end = Offset(s * 0.82f, s * 0.20f),
            strokeWidth = strokeW,
            cap = StrokeCap.Round
        )

        // Can body
        val bodyPath = Path().apply {
            moveTo(s * 0.26f, s * 0.24f)
            lineTo(s * 0.31f, s * 0.84f)
            cubicTo(s * 0.31f, s * 0.90f, s * 0.37f, s * 0.90f, s * 0.42f, s * 0.90f)
            lineTo(s * 0.58f, s * 0.90f)
            cubicTo(s * 0.63f, s * 0.90f, s * 0.69f, s * 0.90f, s * 0.69f, s * 0.84f)
            lineTo(s * 0.74f, s * 0.24f)
        }
        drawPath(bodyPath, color, style = Stroke(strokeW, cap = StrokeCap.Round, join = StrokeJoin.Round))

        // Vertical bars
        drawLine(
            color = color,
            start = Offset(s * 0.42f, s * 0.36f),
            end = Offset(s * 0.42f, s * 0.76f),
            strokeWidth = strokeW * 0.9f,
            cap = StrokeCap.Round
        )
        drawLine(
            color = color,
            start = Offset(s * 0.58f, s * 0.36f),
            end = Offset(s * 0.58f, s * 0.76f),
            strokeWidth = strokeW * 0.9f,
            cap = StrokeCap.Round
        )
    }
}

/**
 * Rupee Currency Icon in Circle for Price Change Dialog (AD16)
 */
@Composable
fun RupeeCoinDialogIcon(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFF0F172A),
    size: Dp = 38.dp
) {
    Canvas(modifier = modifier.size(size)) {
        val s = size.toPx()
        val strokeW = 2.2f.dp.toPx()
        val radius = (s - strokeW) / 2f
        val center = Offset(s / 2f, s / 2f)

        // Outer circle
        drawCircle(
            color = color,
            radius = radius,
            center = center,
            style = Stroke(strokeW)
        )

        // Top horizontal bar
        drawLine(
            color = color,
            start = Offset(s * 0.34f, s * 0.30f),
            end = Offset(s * 0.66f, s * 0.30f),
            strokeWidth = strokeW,
            cap = StrokeCap.Round
        )

        // Second horizontal bar
        drawLine(
            color = color,
            start = Offset(s * 0.34f, s * 0.42f),
            end = Offset(s * 0.60f, s * 0.42f),
            strokeWidth = strokeW,
            cap = StrokeCap.Round
        )

        // Stem & upper loop
        val rPath = Path().apply {
            moveTo(s * 0.44f, s * 0.30f)
            lineTo(s * 0.44f, s * 0.55f)
            cubicTo(s * 0.65f, s * 0.55f, s * 0.65f, s * 0.30f, s * 0.44f, s * 0.30f)
        }
        drawPath(rPath, color, style = Stroke(strokeW, cap = StrokeCap.Round, join = StrokeJoin.Round))

        // Diagonal leg
        drawLine(
            color = color,
            start = Offset(s * 0.45f, s * 0.55f),
            end = Offset(s * 0.66f, s * 0.73f),
            strokeWidth = strokeW,
            cap = StrokeCap.Round
        )
    }
}

/**
 * Circular Refresh/Sync Arrows Icon for Status Update Dialog (AD17)
 */
@Composable
fun SyncRefreshDialogIcon(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFF0F172A),
    size: Dp = 38.dp
) {
    Canvas(modifier = modifier.size(size)) {
        val s = size.toPx()
        val strokeW = 2.2f.dp.toPx()
        val r = s * 0.32f
        val center = Offset(s / 2f, s / 2f)

        // Upper arc
        drawArc(
            color = color,
            startAngle = 205f,
            sweepAngle = 135f,
            useCenter = false,
            topLeft = Offset(center.x - r, center.y - r),
            size = Size(r * 2f, r * 2f),
            style = Stroke(strokeW, cap = StrokeCap.Round)
        )
        // Arrowhead on top right
        val arrow1 = Path().apply {
            moveTo(s * 0.74f, s * 0.28f)
            lineTo(s * 0.85f, s * 0.40f)
            lineTo(s * 0.68f, s * 0.41f)
        }
        drawPath(arrow1, color, style = Stroke(strokeW, cap = StrokeCap.Round, join = StrokeJoin.Round))

        // Lower arc
        drawArc(
            color = color,
            startAngle = 25f,
            sweepAngle = 135f,
            useCenter = false,
            topLeft = Offset(center.x - r, center.y - r),
            size = Size(r * 2f, r * 2f),
            style = Stroke(strokeW, cap = StrokeCap.Round)
        )
        // Arrowhead on bottom left
        val arrow2 = Path().apply {
            moveTo(s * 0.26f, s * 0.72f)
            lineTo(s * 0.15f, s * 0.60f)
            lineTo(s * 0.32f, s * 0.59f)
        }
        drawPath(arrow2, color, style = Stroke(strokeW, cap = StrokeCap.Round, join = StrokeJoin.Round))
    }
}

/**
 * Clean Stroked Magnifying Glass Icon for Empty State (AD14)
 */
@Composable
fun SearchOutlineIcon(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFF1E293B),
    size: Dp = 48.dp
) {
    Canvas(modifier = modifier.size(size)) {
        val s = size.toPx()
        val strokeW = 2.5f.dp.toPx()
        val r = s * 0.26f
        val center = Offset(s * 0.42f, s * 0.42f)

        // Circle lens
        drawCircle(
            color = color,
            radius = r,
            center = center,
            style = Stroke(strokeW)
        )

        // Diagonal handle pointing down-right
        drawLine(
            color = color,
            start = Offset(center.x + r * 0.7071f, center.y + r * 0.7071f),
            end = Offset(s * 0.82f, s * 0.82f),
            strokeWidth = strokeW,
            cap = StrokeCap.Round
        )
    }
}

/**
 * Filter Sliders Icon for Filter Sheet Button
 */
@Composable
fun FilterSlidersIcon(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFF64748B),
    size: Dp = 20.dp
) {
    Canvas(modifier = modifier.size(size)) {
        val s = size.toPx()
        val strokeW = 1.8f.dp.toPx()
        val circleR = 2.4f.dp.toPx()

        // Top line & circle
        drawLine(color, Offset(s * 0.15f, s * 0.30f), Offset(s * 0.85f, s * 0.30f), strokeWidth = strokeW, cap = StrokeCap.Round)
        drawCircle(Color.White, radius = circleR + 1.dp.toPx(), center = Offset(s * 0.40f, s * 0.30f))
        drawCircle(color, radius = circleR, center = Offset(s * 0.40f, s * 0.30f), style = Stroke(strokeW))

        // Bottom line & circle
        drawLine(color, Offset(s * 0.15f, s * 0.70f), Offset(s * 0.85f, s * 0.70f), strokeWidth = strokeW, cap = StrokeCap.Round)
        drawCircle(Color.White, radius = circleR + 1.dp.toPx(), center = Offset(s * 0.65f, s * 0.70f))
        drawCircle(color, radius = circleR, center = Offset(s * 0.65f, s * 0.70f), style = Stroke(strokeW))
    }
}



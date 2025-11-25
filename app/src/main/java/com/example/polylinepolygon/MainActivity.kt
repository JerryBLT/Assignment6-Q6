package com.example.polylinepolygon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.polylinepolygon.ui.theme.PolylinePolygonTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.*
import com.google.maps.android.compose.*
import kotlinx.coroutines.launch
import androidx.compose.ui.graphics.Color

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PolylinePolygonTheme {
                // Apply app theme and content screen composable
                PolylinePolygonScreen()
            }
        }
    }
}

@Composable
fun PolylinePolygonScreen() {

    // Snackbar host state for showing transient messages on interaction
    val snackbarHostState = remember { SnackbarHostState() }
    // Coroutine scope needed for showing snackbar asynchronously
    val scope = rememberCoroutineScope()

    // Mutable states controlling polyline width, polygon stroke width, and color hue
    var polylineWidth by remember { mutableFloatStateOf(12f) }
    var polygonStrokeWidth by remember { mutableFloatStateOf(8f) }
    var hue by remember { mutableFloatStateOf(210f) } // Blue-ish default

    // Compose Color instances used for polyline and polygon colors on the map, with separate fill color supporting transparency
    val polylineColor = Color.hsv(hue = hue, saturation = 1f, value = 1f)
    val polygonStrokeColor = Color.hsv(hue = hue, saturation = 1f, value = 1f)
    val polygonFillColor = Color.hsv(hue = hue, saturation = 0.4f, value = 1f, alpha = 0.5f)

    // Coordinates forming a polyline path representing a "hiking trail"
    val trailPoints = listOf(
        LatLng(37.7749, -122.4194), // SF
        LatLng(37.3382, -121.8863), // San Jose
        LatLng(36.7783, -119.4179), // Fresno
        LatLng(36.1162, -115.1745)  // Las Vegas
    )

    // Coordinates defining a polygon area representing a park boundary
    val polygonPoints = listOf(
        LatLng(37.78, -122.42),
        LatLng(37.80, -122.41),
        LatLng(37.81, -122.43),
        LatLng(37.79, -122.44)
    )

    // Camera position state initialized to center map on first polyline point with zoom level 10
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(trailPoints.first(), 10f)
    }

    // Scaffold provides basic app structure including snackbar host for messages
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->

        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            // Box holding the Google Map UI, filling available space proportionally
            Box(Modifier.weight(1f)) {

                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState
                ) {

                    // Polyline overlay with specified points, color, width, and click interaction
                    Polyline(
                        points = trailPoints,
                        color = polylineColor,
                        width = polylineWidth,
                        clickable = true,
                        onClick = {
                            // Show snackbar indicating trail length on polyline click
                            scope.launch {
                                snackbarHostState.showSnackbar("Trail: 4.2 miles total")
                            }
                        }
                    )

                    // Polygon overlay with specified boundary points, stroke and fill colors, width, and click handler
                    Polygon(
                        points = polygonPoints,
                        strokeColor = polygonStrokeColor,
                        fillColor = polygonFillColor,
                        strokeWidth = polygonStrokeWidth,
                        clickable = true,
                        // Show snackbar with park info when polygon is clicked
                        onClick = {
                            scope.launch {
                                snackbarHostState.showSnackbar("Park area: Wildlife protected zone")
                            }
                        }
                    )
                }
            }

            // UI controls below map to adjust polyline width, polygon stroke width, and color hue dynamically
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {

                // Slider and label to control polyline width in pixels
                Text("Polyline Width: ${polylineWidth.toInt()} px")
                Slider(
                    value = polylineWidth,
                    onValueChange = { polylineWidth = it },
                    valueRange = 2f..20f
                )

                // Slider and label to control polygon stroke width in pixels
                Text("Polygon Stroke Width: ${polygonStrokeWidth.toInt()} px")
                Slider(
                    value = polygonStrokeWidth,
                    onValueChange = { polygonStrokeWidth = it },
                    valueRange = 2f..20f
                )

                // Slider and label to adjust hue for polyline and polygon colors
                Text("Color Hue")
                Slider(
                    value = hue,
                    onValueChange = { hue = it },
                    valueRange = 0f..360f
                )
            }
        }
    }
}

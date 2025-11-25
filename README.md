# Assignment6-Q6
This Android application demonstrates Google Maps Compose features by drawing a polyline (hiking trail) and a polygon (park boundary).

## Features
1. Google Map with a Hiking Trail (Polyline)
- Drawn using the Maps Compose Polyline() component
- Trail connects: San Francisco → San Jose → Fresno → Las Vegas
- Fully dynamic: width and color change in real time using sliders
2. Park Highlight Area (Polygon)
- Drawn using Polygon()
- Fill and stroke color can be modified
- Stroke width is also customizable
- Slight transparency for improved visibility
3. Customization Controls
4. Click Listeners with Snackbar

## How to Run
- Clone the github repository
- Add your Google Maps API key to AndroidManifest.xml:
`<meta-data
    android:name="com.google.android.geo.API_KEY"
    android:value="YOUR_KEY_HERE"/>`
- Build & run on an emulator or device with Google Play Services.

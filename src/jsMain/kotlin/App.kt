import androidx.compose.runtime.*
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.CancellationException
import kotlinx.dom.createElement
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.Element
import org.w3c.dom.HTMLDivElement


val centerPoint = google.maps.LatLng(0.0, 0.0)


@Composable
fun App() {

    var element by remember { mutableStateOf<Element?>(null) }

    val mapElement = remember {
        document.createElement("div") {
            this as HTMLDivElement
            style.width = "100%"
            style.height = "100%"
        }
    }

    LaunchedEffect(element) {
        val ref = element ?: return@LaunchedEffect
        ref.append(mapElement)
    }

    var marker by remember { mutableStateOf<google.maps.Marker?>(null) }

    var selected by remember { mutableStateOf(0) }

    val map = remember {
        google.maps.Map(mapElement, jso {
            center = centerPoint
            zoom = 2
        })
    }

    val service = remember {
        val service = google.maps.places.PlacesService(map)
        service
    }

    val infoWindow = remember {
        google.maps.InfoWindow()
    }

    LaunchedEffect(selected) {
        marker?.setMap(null)
        marker = null
        //doesn't always work on fast switching
        try {


            val found = service.findPlace(places[selected]) as Array<dynamic>

            val foundObj = found.firstOrNull() ?: return@LaunchedEffect

            val location = foundObj.geometry.location

            marker = google.maps.Marker(jso {
                this.map = map
                this.position = location
            })
//            google.maps.event.addListener(marker, "click") {
//                infoWindow.setContent("${places[selected]} ${foundObj.name}")
//                infoWindow.open(map)
//
//            }

        } catch (e: FailedToFind) {
            window.alert("${e.message}")
        } catch (e: Throwable) {
            if (e is CancellationException) throw e
            else window.alert("${e.message}")
        }

    }

    Div({
        style {
            display(DisplayStyle.Flex)
            flexDirection(FlexDirection.Row)
            alignItems(AlignItems.Stretch)
            height(100.vh)
        }
    }) {
        Div({
            style {
                minWidth(200.px)
                paddingLeft(10.px)
                flex(0, 0)
                overflowY("scroll")
            }

        }) {

            places.forEachIndexed { index, it ->
                Div({
                    onClick {
                        selected = index
                    }
                    style {
                        cursor("pointer")
                        if (selected == index) fontWeight("bold")
                    }
                }) {
                    Text(it)
                }
            }

        }
        Div({
            ref {
                element = it
                onDispose { }
            }
            style {
                flex(1, 1)


            }
        }) {}

    }

}
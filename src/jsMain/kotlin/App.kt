import androidx.compose.runtime.*
import kotlinx.browser.window
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.Element


//lateinit var map: google.maps.Map
//lateinit var service: google.maps.places.PlacesService



@NoLiveLiterals
@Composable
fun App() {


    var element by remember { mutableStateOf<Element?>(null) }

    var marker by remember { mutableStateOf<google.maps.Marker?>(null) }

    var selected by remember { mutableStateOf(0) }
    val sidney = remember {
        google.maps.LatLng(0.0, 0.0)
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
    val map = remember(element) {
        val element = element ?: return@remember null
        google.maps.Map(element, jso {
            center = sidney
            zoom = 2
        })
    }


    val service = remember(map) {
        val map = map ?: return@remember null
        val service = google.maps.places.PlacesService(map)
        service
    }

    val infoWindow = remember(map) {
        val map = map ?: return@remember null
        google.maps.InfoWindow()
    }

    LaunchedEffect(service, selected) {
        val service = service ?: return@LaunchedEffect

        marker?.setMap(null)

        try {


            val found = service.findPlace(places[selected]) as Array<dynamic>
            val foundObj = found.firstOrNull() ?: return@LaunchedEffect

//            console.log(foundObj)
            val location = foundObj.geometry.location


            marker = google.maps.Marker(jso {
                this.map = map
                this.position = location
            })
//            google.maps.event.addListener(marker, "click") {
//                infoWindow!!.setContent("${juros[selected]} ${foundObj.name}")
//                infoWindow!!.open(map)
//
//            }

        } catch (e: Exception) {
            window.alert("not found")
        }

    }

}
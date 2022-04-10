import org.w3c.dom.Element
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

external class google {
    class maps {

        class InfoWindow {
            fun setContent(string: String)
            fun open(map: dynamic)
        }

        class event {
            companion object {
                fun addListener(marker: dynamic, str: String, listener: () -> Unit)
            }
        }

        class Marker(options: dynamic) {
            fun setMap(map: dynamic)
        }

        class LatLng(lat: Double, lng: Double)
        class places {
            class PlacesService(map: dynamic) {
                fun findPlaceFromQuery(request: dynamic, call: (result: dynamic, status: dynamic) -> Unit): dynamic

            }
        }

        class Map(element: Element, options: dynamic)
    }

}

suspend fun google.maps.places.PlacesService.findPlace(name: String): dynamic {
    return suspendCoroutine<dynamic> {
        findPlaceFromQuery(jso<dynamic> {
            query = "$name"
            fields = arrayOf("name", "geometry")
        }
        ) { result, status ->
            if (status == statusOk) {
                it.resume(result)
            } else {
                it.resumeWithException(Exception("failed to fetch"))
            }


        }

    }

}

val statusOk = js("google.maps.places.PlacesServiceStatus.OK")
package com.example.csi_dmce.database

import android.util.Log
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date

data class Event (
    var eventId     : String?       = null,
    var title       : String?       = null,
    var venue       : String?       = null,
    var datetime    : Date?         = null,
    var description : String?       = null,
    var poster_url  : String?       = null,
    var rsvp        : List<String>? = null,
)

class EventWrapper {
    val eventsRef = FirebaseFirestore.getInstance().collection("events")
    val dateFormat = SimpleDateFormat("dd-MM-yyyy")

    /**
     * Creates an Event ID from an event title and its date.
     * The title is shortened for the ID. If the title contains one word, then we taker alternate
     * characters. If the title has multiple words, we consider the first two letters of every
     * word in the title.
     *
     *
     * @param title the title of the event
     * @param eventDate the date of the event.
     * @return the event ID as a String, which has the format `title-timestamp`.
     */
    fun createEventId(eventTitle: String, eventDate: Date): String {
        val unixTimestamp: String = (eventDate.time / 1000).toString()
        val tokens = eventTitle.split(" ")
        return if (tokens.size == 1) {
            val titleToken = eventTitle.filterIndexed{ index, _ ->
                index % 2 == 0
            }
            "$titleToken-$unixTimestamp"
        } else {
            val titleToken = tokens.joinToString("") { it.take(2) }
            "$titleToken-$unixTimestamp"
        }
    }

    // Add, Read, Update, Delete
    suspend fun addEvent(): Void? {
        val event = Event(
            eventId = createEventId("DMCE CTF", dateFormat.parse("22-01-2022")),
            title = "DMCE CTF",
            venue = "C503",
            datetime = dateFormat.parse("22-01-2022"),
            description = "Very cool hackathon.",
            poster_url = "https://google.com",
            rsvp = null
        )
        val eventRef = eventsRef.document(event.eventId!!)
        return eventRef.set(event).await()
    }


    suspend fun getRsvpStudents(doc: DocumentSnapshot): List<String> {
        val attendees: MutableList<String> = mutableListOf()
        val attendeesRef = doc.reference.collection("attendees")
        val attendeeDocuments = attendeesRef.get().await().documents
        attendeeDocuments.map {
            attendees.add(it.id)
        }
        return attendees
    }

    suspend fun readEvent(eventId: String): Event {
        // TODO: Add clientside checks for null values.
        val eventDocRef = eventsRef.document(eventId)
        val eventDoc = eventDocRef.get().await()
        Log.d("DB_EVENT", eventDoc.toString())
        val event = eventDoc.toObject(Event::class.java)!!
        event.eventId = eventDoc.id
        event.rsvp = getRsvpStudents(eventDoc)

        return event
    }

    fun updateEvent(event: Event) {}
    fun deleteEvent(event: Event) {}
}
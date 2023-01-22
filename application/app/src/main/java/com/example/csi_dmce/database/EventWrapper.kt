package com.example.csi_dmce.database

import android.annotation.SuppressLint
import com.google.firebase.firestore.*
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import com.example.csi_dmce.R

import com.example.csi_dmce.utils.Helpers

data class Event (
    @DocumentId
    var eventId     : String?       = null,
    var title       : String?       = null,
    var venue       : String?       = null,
    var datetime    : Date?         = null,
    var description : String?       = null,
    var poster_url  : String?       = null,
    var attendees   : List<String>? = null
)

class EventWrapper {
    @SuppressLint("SimpleDateFormat")
    private val dateFormat = SimpleDateFormat(R.string.APPDATEFORMAT.toString())
    private val eventsCollectionRef = FirebaseFirestore.getInstance().collection("events")

    private fun getEventDocument(eventCollectionRef: CollectionReference, eventId: String): DocumentReference {
        return eventCollectionRef.document(eventId)
    }

    suspend fun addEvent(event: Event): Void? {
        val eventDocumentRef = eventsCollectionRef.document(event.eventId!!)
        return eventDocumentRef.set(event).await()
    }

    suspend fun getEvent(eventId: String): Event? {
        val eventDocument = getEventDocument(eventsCollectionRef, eventId).get().await()
        return eventDocument.toObject(Event::class.java)!!
    }

    suspend fun updateEvent(oldEvent: Event, newEvent: Event) {
        // If the event IDs are different, then we have to delete the old document,
        if (oldEvent.eventId != newEvent.eventId) {
            getEventDocument(eventsCollectionRef, oldEvent.eventId!!).delete()
        }

        // And a add a new one
        addEvent(newEvent)
    }

    fun deleteEvent(event: Event) {
        getEventDocument(eventsCollectionRef, event.eventId!!).delete()
    }
}
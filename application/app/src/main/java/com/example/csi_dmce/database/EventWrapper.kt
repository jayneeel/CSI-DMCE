package com.example.csi_dmce.database

import android.net.Uri
import android.util.Log
import com.example.csi_dmce.utils.Helpers
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File

data class Event (
    @DocumentId
    var eventId     : String?       = null,
    var title       : String?       = null,
    var venue       : String?       = null,
    var uuid        : String?       = null,
    var datetime    : Long?         = null,
    var description : String?       = null,
    var poster_extension  : String? = null,
    var prerequisites: String? = null,
    var poster_url  : String? = null,
    var speaker     : String?       = null,
    var registrants   : MutableList<String>? = null,
    var attendees   : MutableList<String>? = null
)

class EventWrapper {
    companion object {
        private val eventsCollectionRef = FirebaseFirestore.getInstance().collection("events")
        private val storageRef = FirebaseStorage.getInstance().reference.child("events")

        private fun getEventDocument(eventId: String): DocumentReference {
            return eventsCollectionRef.document(eventId)
        }

        /**
         * Adds an event into Firebase. While adding an event, you should provide the `eventId` as
         * null or an empty string. The wrapper creates an ID anyway.
         *
         * @param event the deserialized event
         * @return void
         */
        suspend fun addEvent(event: Event, imageUri: Uri? = null): Void? {
            event.eventId = Helpers.createEventId(event.title!!, event.datetime!!)

            if (imageUri != null) {
                // Upload to Firebase storage
                var fileExtension = imageUri.lastPathSegment.toString()
                fileExtension = fileExtension.substring(fileExtension.lastIndexOf(".") + 1)

                event.poster_extension = fileExtension

                storageRef.child("${event.eventId}/poster.${fileExtension}")
                    .putFile(imageUri)
                    .await()
            }

            val eventDocumentRef = eventsCollectionRef.document(event.eventId!!)
            return eventDocumentRef.set(event).await()
        }

        /**
         * Read an event from the database.
         *
         * @param eventId the ID of the event.
         * @return the deserialized `Event` data class
         */
        suspend fun getEvent(eventId: String): Event? {
            val eventDocument = getEventDocument(eventId).get().await()
            return eventDocument.toObject(Event::class.java)!!
        }

        suspend fun getEvents(): List<Event> {
            return eventsCollectionRef
                .get()
                .await()
                .toObjects(Event::class.java)
        }

        /**
         * Update an event in the database.
         *
         * @param oldEvent the old event, which is to be updated.
         * @param newEvent the new event, which is to be stored.
         */
        suspend fun updateEvent(oldEvent: Event, newEvent: Event) {
            // If the event IDs are equal, then we have to delete the old document. This can
            // happen when and if the event's time or title is changed.
            if (newEvent.eventId != null && oldEvent.eventId == newEvent.eventId) {
                getEventDocument(oldEvent.eventId!!).delete()
            }

            // And a add a new event.
            addEvent(newEvent)
        }

        /**
         * Delete an event from the database.
         *
         * @param event the event to be deleted.
         */
        fun deleteEvent(event: Event) {
            getEventDocument(event.eventId!!).delete()
        }


        /**
         * Return the event's UUID given its ID.
         *
         * @param eventId the event's ID
         * @return The UUID of the event.
         */
        suspend fun getEventUuid(eventId: String): String {
            val eventObject = getEventDocument(eventId)
                .get()
                .await()
                .toObject(Event::class.java)

            if (eventObject != null) {
                return eventObject.uuid!!
            }

            throw NullPointerException("No event UUID!")
        }


        suspend fun getPosterUrl(eventId: String, posterExtension: String): String {
            return storageRef
                .child("${eventId}/poster.${posterExtension}")
                .downloadUrl
                .await()
                .toString()
        }

        suspend fun deleteEventStorage(eventId: String) {
            val eventRef = storageRef.child(eventId)
            Log.d("UPDATE_FLOW_DEL_REF", eventRef.toString())
            val eventDocs = eventRef.listAll().await().items
            Log.d("UPDATE_FLOW_DEL_LIST", eventDocs.toString())
            for (doc in eventDocs) {
                Log.d("UPDATE_FLOW_DELDOC", doc.toString())
                Log.d("UPDATE_FLOW_DELDOC_PATH", doc.path.toString())
                doc.delete()
            }
            eventRef.delete()
            Log.d("UPDATE_FLOW_DEL_DONE", "DELETED")
        }

        // TODO: Extend this to other files
        suspend fun moveEventStorage(
            oldEventId: String,
            newEventId: String,
            posterExtension: String
        ) {
            val oldEventRef = storageRef.child("${oldEventId}/poster.${posterExtension}")
            val newEventRef = storageRef.child("${newEventId}/poster.${posterExtension}")

            val localFile = withContext(Dispatchers.IO) {
                File.createTempFile("poster", posterExtension)
            }

            oldEventRef.getFile(localFile).await()
            newEventRef.putFile(Uri.fromFile(localFile)).await()
        }

        suspend fun registerStudent(eventObject: Event, studentId: String) {
            if (eventObject.registrants == null) {
                eventObject.registrants = mutableListOf(studentId)
            } else {
                eventObject.registrants!!.add(studentId)
            }

            Log.d("EVENT", eventObject.toString())

            updateEvent(eventObject, eventObject)
        }

        suspend fun unregisterStudent(eventObject: Event, studentId: String) {
            eventObject.registrants?.remove(studentId)
            Log.d("EVENT", eventObject.toString())
            updateEvent(eventObject, eventObject)
        }
    }
}
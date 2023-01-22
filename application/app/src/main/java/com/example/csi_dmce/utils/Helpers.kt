package com.example.csi_dmce.utils

import java.math.BigInteger
import java.security.MessageDigest
import java.util.*

class Helpers {
    // Prepare MD5 hash of a string
    fun get_md5_hash(plaintext: String): String {
        val md = MessageDigest.getInstance("MD5")
        val bigInt = BigInteger(1, md.digest(plaintext.toByteArray(Charsets.UTF_8)))
        return String.format("%032x", bigInt)
    }

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
}
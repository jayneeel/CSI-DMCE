package com.example.csi_dmce.agenda

import com.example.csi_dmce.database.Complaint
import com.example.csi_dmce.database.ComplaintWrapper
import com.example.csi_dmce.utils.Helpers
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.Date

data class Agenda(
    var title: String?,
    var task: String?,
    var completed: Boolean = false,
    var time: Long?,
)

class AgendaWrapper{

    companion object{

        private val agendaCollectionRef = FirebaseFirestore.getInstance().collection("agendas")

        suspend fun addAgenda(agendaObject: Agenda) {
            val agendaId: String = agendaObject.time.toString()

            AgendaWrapper.agendaCollectionRef
                .document(agendaId)
                .set(agendaObject)
                .await()
        }
        }
    }



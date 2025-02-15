package com.lion.wandertrip.presentation.my_trip_note.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lion.wandertrip.model.TripNoteModel

@Composable
fun VerticalTripNoteList(tripNotes: List<TripNoteModel>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(tripNotes) { tripNote ->
            TripNoteItem(tripNote = tripNote)
        }
    }
}

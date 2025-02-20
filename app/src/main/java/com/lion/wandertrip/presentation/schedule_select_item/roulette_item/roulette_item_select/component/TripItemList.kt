package com.lion.wandertrip.presentation.schedule_select_item.roulette_item.roulette_item_select.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lion.wandertrip.model.TripItemModel

@Composable
fun TripItemList(
    tripItems: List<TripItemModel>,
    selectedItems: List<TripItemModel>,
    onItemClick: (TripItemModel) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(tripItems) { tripItem ->
            val isSelected = selectedItems.contains(tripItem)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { onItemClick(tripItem) },
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) Color(0xFFB3E5FC) else Color.White
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = tripItem.title, style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "위도: ${tripItem.mapLat}, 경도: ${tripItem.mapLong}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}
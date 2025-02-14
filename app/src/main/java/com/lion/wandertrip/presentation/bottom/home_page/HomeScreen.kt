package com.lion.wandertrip.presentation.bottom.home_page

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.lion.wandertrip.R

data class TravelSpot(val rank: Int, val title: String, val location: String, val imageUrl: String)

@Composable
fun HomeScreen() {
    val travelSpots = listOf(
        TravelSpot(1, "감자밥", "춘천", "sample_image.xml"),
        TravelSpot(2, "서귀포 매일 올레 시장", "제주", "sample_image.xml"),
        TravelSpot(3, "월영교", "포항·안동", "sample_image.xml"),
        TravelSpot(4, "매마성", "동해·거제·남해", "sample_image.xml"),
        TravelSpot(5, "경기천", "신규", "sample_image.xml"),
        TravelSpot(6, "남망 포", "여수", "sample_image.xml")
    )

    Column(modifier = Modifier.fillMaxSize()) {
        TopBar()
        TravelSpotSection("인기 급상승\n국내 여행지 Best 10", travelSpots)
        Spacer(modifier = Modifier.height(16.dp))
        PopularTripSection()
    }
}

@Composable
fun TopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("국내여행", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Row {
            Icon(Icons.Default.Search, contentDescription = "Search", modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Icon(Icons.Default.Menu, contentDescription = "Menu", modifier = Modifier.size(24.dp))
        }
    }
}


@Composable
fun TravelSpotSection(title: String, travelSpots: List<TravelSpot>) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
        LazyRow {
            items(travelSpots) { spot ->
                TravelSpotItem(spot)
            }
        }
    }
}

@Composable
fun TravelSpotItem(spot: TravelSpot) {
    Column(
        modifier = Modifier
            .padding(end = 8.dp)
            .width(150.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .clickable { }
    ) {
        AsyncImage(
            model = spot.imageUrl,  // 네트워크 이미지 URL
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        )
        Column(modifier = Modifier.padding(8.dp)) {
            Text("${spot.rank}", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text(spot.title, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text(spot.location, fontSize = 12.sp, color = Color.Gray)
        }
    }
}

@Composable
fun PopularTripSection() {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("WanderTrip 인기 여행기", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text("더보기", fontSize = 14.sp, color = Color.Blue, modifier = Modifier.clickable { })
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.img_plane), // XML 리소스 로드
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(20.dp))
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

package com.lion.wandertrip.presentation.bottom.home_page

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
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
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.lion.wandertrip.R

data class TravelSpot(val rank: Int, val title: String, val location: String, val imageUrl: String)

@Composable
fun HomeScreen() {
    val viewModel: HomeViewModel = viewModel()
    // val travelPosts by viewModel.travelPosts

    val travelPosts = listOf(
        TravelPost(
            title = "뚜벅이 혼자 겨울 제주도 여행",
            description = "혼자 떠난 겨울 제주도에서 특별한 경험을 했어요.",
            images = listOf(
                "https://example.com/trip1-1.jpg",
                "https://example.com/trip1-2.jpg"
            ),
            extraText = "제주에서의 하루는 너무 짧았어요!"
        ),
        TravelPost(
            title = "뚜벅이 혼자 겨울 제주도 여행",
            description = "혼자 떠난 겨울 제주도에서 특별한 경험을 했어요.",
            images = listOf(
                "https://example.com/trip1-1.jpg",
                "https://example.com/trip1-2.jpg"
            ),
            extraText = "제주에서의 하루는 너무 짧았어요!"
        ),
        TravelPost(
            title = "1박 2일 안동 여행코스 💕",
            description = "안동에서 좋은 추억을 남기고 왔어요.",
            images = listOf(
                "https://example.com/trip2-1.jpg",
                "https://example.com/trip2-2.jpg",
                "https://example.com/trip2-3.jpg"
            ),
            extraText = "안동의 야경은 정말 아름다웠어요!"
        ),
        TravelPost(
            title = "봄맞이 경주 벚꽃 여행 🌸",
            description = "경주의 벚꽃이 만개한 시기를 놓치지 마세요!",
            images = listOf(
                "https://example.com/trip3-1.jpg",
                "https://example.com/trip3-2.jpg",
                "https://example.com/trip3-3.jpg"
            ),
            extraText = "경주의 벚꽃은 정말 환상적이었어요!"
        ),
        TravelPost(
            title = "부산 해운대 겨울 바다 🌊",
            description = "겨울 바다의 매력을 느껴보세요!",
            images = listOf(
                "https://example.com/trip4-1.jpg",
                "https://example.com/trip4-2.jpg"
            ),
            extraText = "부산에서 겨울 바다를 즐기다 왔어요!"
        )
    )

    val travelSpots = listOf(
        TravelSpot(1, "감자밭", "춘천·홍천", "https://example.com/image1.jpg"),
        TravelSpot(2, "서귀포 매일 올레 시장", "제주", "https://example.com/image2.jpg"),
        TravelSpot(3, "월영교", "포항·안동", "https://example.com/image3.jpg"),
        TravelSpot(4, "매화성", "통영·거제·남해", "https://example.com/image4.jpg"),
        TravelSpot(5, "경기전", "전주", "https://example.com/image5.jpg"),
        TravelSpot(6, "낭만 포차", "여수", "https://example.com/image6.jpg"),
        TravelSpot(7, "한라산", "제주", "https://example.com/image7.jpg"),
        TravelSpot(8, "경복궁", "서울", "https://example.com/image8.jpg"),
        TravelSpot(9, "해운대 해수욕장", "부산", "https://example.com/image9.jpg"),
        TravelSpot(10, "남이섬", "강원", "https://example.com/image10.jpg")
    )

    Column(modifier = Modifier.fillMaxSize()) {
        // ✅ 고정된 Toolbar
        TopBar()

        // ✅ 아래 컨텐츠를 스크롤 가능하게 만들기
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                TravelSpotList(travelSpots) // 인기 여행지 (좌우 스크롤)
            }
            item {
                PopularTripSection(
                    trips = travelPosts
                ) // 인기 여행기
            }
        }
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
            CalendarIcon()
            Spacer(modifier = Modifier.width(16.dp))
            Icon(Icons.Default.Menu, contentDescription = "Menu", modifier = Modifier.size(24.dp))
        }
    }
}

@Composable
fun TravelSpotList(spots: List<TravelSpot>) {
    val oddSpots = spots.filterIndexed { index, _ -> index % 2 == 0 } // 홀수 번째 아이템 (1,3,5...)
    val evenSpots = spots.filterIndexed { index, _ -> index % 2 != 0 } // 짝수 번째 아이템 (2,4,6...)

    Column(
        modifier = Modifier.fillMaxSize().padding(8.dp)
    ) {
        Text(
            text = "인기 급상승\n국내 여행지 Best 10",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp), // 좌우 간격 설정
            modifier = Modifier.fillMaxWidth()
        ) {
            items(oddSpots.size) { index ->
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp) // 위아래 간격 설정
                ) {
                    if (index < oddSpots.size) {
                        TravelSpotItem(oddSpots[index]) // 첫 번째 줄 (1,3,5...)
                    }
                    if (index < evenSpots.size) {
                        TravelSpotItem(evenSpots[index]) // 두 번째 줄 (2,4,6...)
                    }
                }
            }
        }
    }
}



@Composable
fun TravelSpotItem(spot: TravelSpot) {
    Column(
        modifier = Modifier
            .width(170.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                model = spot.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )
            Text(
                text = spot.rank.toString(),
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(8.dp)
                    .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            )
        }
        Column(modifier = Modifier.padding(8.dp)) {
            Text(spot.title, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text(spot.location, fontSize = 12.sp, color = Color.Gray)
        }
    }
}

@Composable
fun PopularTripSection(trips: List<TravelPost>) {
    var displayedTrips by remember { mutableStateOf(trips.take(2)) }

    Column(modifier = Modifier.padding(16.dp)) {
        // 상단 "트리플 인기 여행기" 타이틀 + 더보기 버튼
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("인기 여행기", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(
                text = "더보기",
                fontSize = 14.sp,
                color = Color.Blue,
                modifier = Modifier
                    .clickable { displayedTrips = trips.take(displayedTrips.size + 2) }
                    .padding(4.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        displayedTrips.forEach { trip ->
            PopularTripItem(trip)
            Spacer(modifier = Modifier.height(16.dp))
        }

        // ✅ 추가: "인기 여행기 더보기" 버튼 (LazyColumn의 마지막에 위치)
        if (displayedTrips.size < trips.size) {
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { displayedTrips = trips.take(displayedTrips.size + 2) }, // 더보기 버튼 클릭 이벤트
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("인기 여행기 더보기", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}


@Composable
fun PopularTripItem(trip: TravelPost) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .padding(8.dp)
    ) {
        // 🔹 제목 및 설명
        Text(trip.title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Text(trip.description, fontSize = 14.sp, color = Color.Gray) // ✅ 제목 아래 추가 텍스트
        Spacer(modifier = Modifier.height(8.dp))

        // 🔹 이미지 표시 (최대 3개)
        TripImageGrid(trip.images)

        Spacer(modifier = Modifier.height(8.dp))
        Text(trip.extraText, fontSize = 14.sp, color = Color.DarkGray) // ✅ 이미지 아래 추가 텍스트
    }
}

@Composable
fun TripImageGrid(images: List<String>) {
    val displayedImages = images.take(3) // 최대 3개만 가져오기

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        displayedImages.forEach { imageUrl ->
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(8.dp))
            )
        }
    }
}

@Composable
fun CalendarIcon() {
    Image(
        painter = painterResource(id = R.drawable.calendar_add_on_24px), // ✅ XML 아이콘 적용
        contentDescription = "Calendar",
        modifier = Modifier.size(24.dp)
    )
}

data class TravelPost(
    val title: String,       // 여행기 제목
    val description: String, // 제목 아래 설명
    val images: List<String>, // 이미지 리스트 (최대 3개)
    val extraText: String    // 이미지 아래 추가 텍스트
)


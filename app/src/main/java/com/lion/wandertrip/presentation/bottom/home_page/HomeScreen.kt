package com.lion.wandertrip.presentation.bottom.home_page

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberDrawerState
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
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.lion.wandertrip.R
import com.lion.wandertrip.presentation.schedule_add.ScheduleAddScreen
import com.lion.wandertrip.presentation.search_page.SearchScreen
import kotlinx.coroutines.launch

data class TravelSpot(val rank: Int, val title: String, val location: String, val imageUrl: String)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

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

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            // 🔹 홈 화면일 때만 `TopBar` 표시
            if (currentRoute == "home") {
                TopBar(navController, onMenuClick = {
                    coroutineScope.launch { drawerState.open() }
                })
            }

            // 🔹 네비게이션 호스트
            NavHost(navController = navController, startDestination = "home") {
                composable("home") { /* 기존 홈 화면 콘텐츠 */ }
                composable("search") { SearchScreen() } // ✅ WanderTrip TopBar 숨김
                composable("calendar") { ScheduleAddScreen() } // ✅ WanderTrip TopBar 숨김
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item { TravelSpotList(travelSpots) }
                item { PopularTripSection(travelPosts) }
            }
        }

        // ✅ 오른쪽에서 나오는 Drawer
        RightDrawer(
            drawerState = drawerState,
            onClose = { coroutineScope.launch { drawerState.close() } }
        )
    }
}

@Composable
fun TopBar(navController: NavController, onMenuClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("WanderTrip", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        Row {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { navController.navigate("search") }
            )
            Spacer(modifier = Modifier.width(16.dp))

            Icon(
                painter = painterResource(id = R.drawable.ic_calendar_add_on_24px),
                contentDescription = "Calendar",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { navController.navigate("calendar") },
                tint = Color.Unspecified
            )
            Spacer(modifier = Modifier.width(16.dp))

            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Menu",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onMenuClick() }
            )
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
        painter = painterResource(id = R.drawable.ic_calendar_add_on_24px), // ✅ XML 아이콘 적용
        contentDescription = "Calendar",
        modifier = Modifier.size(24.dp)
    )
}

@Composable
fun RightDrawer(drawerState: DrawerState, onClose: () -> Unit) {
    val isVisible by remember { derivedStateOf { drawerState.isOpen } } // ✅ Drawer 상태 추적

    // ✅ 1. 슬라이드 애니메이션을 위한 offset 값 설정
    val offsetX by animateFloatAsState(
        targetValue = if (isVisible) 0f else 300f, // ✅ Drawer가 닫히면 오른쪽으로 이동
        animationSpec = tween(durationMillis = 300), label = "drawerSlide"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        // ✅ 2. 회색 배경 (Drawer 열릴 때만 표시됨, 슬라이드되지 않음)
        if (isVisible) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f)) // ✅ 배경 투명도 설정
                    .clickable { onClose() } // ✅ 바깥 클릭 시 닫기
            )
        }

        // ✅ 3. Drawer가 오른쪽에서 슬라이드되며 등장 (애니메이션 적용)
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(300.dp)
                .align(Alignment.CenterEnd) // ✅ Drawer를 항상 오른쪽에 고정
                .offset(x = offsetX.dp) // ✅ 애니메이션 적용
                .background(Color.White) // ✅ 기본 Drawer 배경색
        ) {
            // ✅ 내부 UI를 스크롤 가능하도록 설정
            LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                // 🔹 닫기 버튼
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back_24px),
                            contentDescription = "Close",
                            modifier = Modifier
                                .size(24.dp)
                                .clickable { onClose() } // ✅ 닫기 버튼 클릭 시 Drawer 닫기
                        )
                    }
                }

                // 🔹 프로필 섹션
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        AsyncImage(
                            model = "https://example.com/profile.jpg",
                            contentDescription = "Profile",
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                                .background(Color.Gray)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text("Draw", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            Text(
                                text = "프로필 편집 >",
                                fontSize = 14.sp,
                                color = Color.Gray,
                                modifier = Modifier.clickable { /* 프로필 편집 화면 이동 */ }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // 🔹 아이콘 메뉴 (내 여행, 내 저장, 내 리뷰, 내 여행기)
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        DrawerMenuItem("내 여행", R.drawable.ic_person_24px)
                        DrawerMenuItem("내 저장", R.drawable.ic_add_24px)
                        DrawerMenuItem("내 리뷰", R.drawable.ic_star_24px)
                        DrawerMenuItem("내 여행기", R.drawable.ic_calendar_month_24px)
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }

                // 🔹 주요 리스트 메뉴
                item {
                    Column {
                        DrawerListItem("내 예약")
                        DrawerListItem("쿠폰함", "43", Color.Red)
                        DrawerListItem("트리플 캐시", "0", Color.Blue)
                        DrawerListItem("여행자 클럽", "0P", Color.Black)
                        DrawerListItem("오프라인 가이드")
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }

                // 🔹 최근 본 항목 (가로 스크롤)
                item {
                    RecentViewedSection()
                }

                // ✅ 🔹 공지사항 & 고객센터 추가 (최근 본 항목 아래)
                item {
                    NoticeAndSupportSection()
                }
            }
        }
    }
}



@Composable
fun DrawerMenuItem(title: String, iconRes: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = title,
            modifier = Modifier.size(32.dp)
        )
        Text(title, fontSize = 12.sp)
    }
}

@Composable
fun DrawerListItem(title: String, badgeText: String? = null, badgeColor: Color = Color.Gray) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, fontSize = 16.sp)
        badgeText?.let {
            Text(
                text = badgeText,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = badgeColor
            )
        }
    }
}

@Composable
fun RecentViewedSection() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("최근 본 항목", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        LazyRow(modifier = Modifier.padding(top = 8.dp)) {
            items(getRecentViewedItems()) { item ->
                RecentViewedItem(item)
            }
        }
    }
}

@Composable
fun RecentViewedItem(item: RecentItem) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        AsyncImage(
            model = item.imageUrl,
            contentDescription = item.name,
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(10.dp))
        )
        Text(item.name, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Text(item.category, fontSize = 12.sp, color = Color.Gray)
    }
}

@Composable
fun NoticeAndSupportSection() {
    Column(modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
        HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 🔹 공지사항 버튼
            Row(modifier = Modifier.clickable { /* 공지사항 화면 이동 */ }) {
                Text("공지사항", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(6.dp))
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(Color.Red, CircleShape) // 🔴 공지사항 알림 표시
                )
            }

            // ✅ 공지사항과 고객센터 사이에 구분선 추가
            VerticalDivider(
                modifier = Modifier
                    .height(16.dp) // 텍스트 높이만큼 맞추기
                    .width(1.dp),
                color = Color.LightGray
            )


            // 🔹 고객센터 버튼
            Text(
                text = "고객센터",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { /* 고객센터 화면 이동 */ }
            )
        }
    }
}

data class TravelPost(
    val title: String,       // 여행기 제목
    val description: String, // 제목 아래 설명
    val images: List<String>, // 이미지 리스트 (최대 3개)
    val extraText: String    // 이미지 아래 추가 텍스트
)

data class RecentItem(
    val name: String,
    val category: String,
    val imageUrl: String
)

// 🔹 최근 본 항목 샘플 데이터
fun getRecentViewedItems() = listOf(
    RecentItem("조양방직", "카페/디저트", "https://example.com/item1.jpg"),
    RecentItem("첨성대", "관광명소", "https://example.com/item2.jpg"),
    RecentItem("매화성", "역사유적", "https://example.com/item3.jpg")
)

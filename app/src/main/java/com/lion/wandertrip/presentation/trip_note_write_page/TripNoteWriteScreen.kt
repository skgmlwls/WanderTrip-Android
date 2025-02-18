package com.lion.wandertrip.presentation.trip_note_write_page

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lion.a02_boardcloneproject.component.CustomOutlinedTextField
import com.lion.a02_boardcloneproject.component.CustomTopAppBar
import com.lion.a02_boardcloneproject.component.LikeLionOutlinedTextFieldEndIconMode
import com.lion.wandertrip.component.BlueButton
import com.lion.wandertrip.presentation.trip_note_write_page.component.AddImageButton
import com.lion.wandertrip.ui.theme.NanumSquareRound
import com.lion.wandertrip.ui.theme.NanumSquareRoundRegular
import com.lion.wandertrip.util.Tools

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripNoteWriteScreen(
    tripNoteWriteViewModel: TripNoteWriteViewModel = hiltViewModel(),
    scheduleTitle : String
) {

    val context = LocalContext.current

    // 앨범용 런처
    val albumLauncher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) {
        Tools.takeAlbumDataList(context, it, tripNoteWriteViewModel.tripNotePreviewBitmap)
    }

//    // 일정 제목을 가져오는 함수 호출
//    val scheduleTitle = tripNoteWriteViewModel.gettingTitle() ?: ""
//    // scheduleTitle을 MutableState로 감싸기
    val scheduleTitleState = remember { mutableStateOf(scheduleTitle) }


    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = tripNoteWriteViewModel.topAppBarTitle.value,
                navigationIconImage = Icons.AutoMirrored.Filled.ArrowBack,
                navigationIconOnClick = {
                    tripNoteWriteViewModel.navigationButtonClick()
                }
            )
        },
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(it)
                .padding(start = 20.dp, end = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Spacer(modifier = Modifier.padding(top = 20.dp))

            // 여행기 제목 입력 요소
            CustomOutlinedTextField(
                textFieldValue = tripNoteWriteViewModel.tripNoteTitle,
                label = "여행기 제목",
                placeHolder = "제목을 입력해 주세요",
                trailingIconMode = LikeLionOutlinedTextFieldEndIconMode.TEXT,
                singleLine = true,
            )

            Spacer(modifier = Modifier.padding(top = 33.dp))

            Text(
                text = "사진",
                fontFamily = NanumSquareRound,
                fontSize = 25.sp,
                modifier = Modifier.padding(end = 5.dp)

            )

            Spacer(modifier = Modifier.padding(top = 15.dp))


            // 사진 추가하기
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 추가된 이미지를 왼쪽에 나열
                tripNoteWriteViewModel.tripNotePreviewBitmap.forEachIndexed { index, bitmap ->
                    Box(
                        modifier = Modifier
                            .size(60.dp) // 이미지 크기
                            .padding(end = 10.dp)
                            .clip(shape = RoundedCornerShape(12.dp))
                    ) {
                        // Bitmap을 ImageBitmap으로 변환
                        val imageBitmap = bitmap?.asImageBitmap()

                        // 이미지 표시
                        if (imageBitmap != null) {
                            Image(
                                bitmap = imageBitmap,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize()
                            )

                            // X 버튼 (이미지 삭제)
                            IconButton(
                                onClick = {
                                    if (bitmap != null) {
                                        tripNoteWriteViewModel.removeTripNoteImage(bitmap)
                                    }
                                },
                                modifier = Modifier
                                    .align(Alignment.TopEnd) // 이미지 위에 X 버튼 배치
                                    .size(24.dp) // X 버튼 크기 조정
                            ) {
                                Icon(Icons.Default.Close, contentDescription = "Remove Image")
                            }
                        }
                    }
                }

                // 사진 추가 버튼 (2개 이하일 때만 활성화)
                if (tripNoteWriteViewModel.tripNotePreviewBitmap.size < 3) {
                    AddImageButton(
                        onClick = {
                            val pickVisualMediaRequest =
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            albumLauncher.launch(pickVisualMediaRequest)
                        }
                    )
                }
            }

//            // 가져온 일정 제목 텍스트 필드
//            CustomOutlinedTextField(
//                textFieldValue = scheduleTitleState,
//                label = "일정 제목",
//                trailingIconMode = LikeLionOutlinedTextFieldEndIconMode.TEXT,
//                singleLine = true,
//            )

            Spacer(modifier = Modifier.padding(top = 20.dp))

            Text(
                text= "선택한 일정 : ${scheduleTitleState.value}",
                fontFamily = NanumSquareRoundRegular,
                fontSize = 18.sp,
                modifier = Modifier.padding(end = 5.dp)

            )

            Spacer(modifier = Modifier.padding(top = 11.dp))

            // 일정 추가하기 버튼
            BlueButton(
                text = "일정 선택하기",
                paddingTop = 5.dp,
                onClick = {
                    tripNoteWriteViewModel.addTripScheduleClick()
                }
            )

            Spacer(modifier = Modifier.padding(top = 20.dp))

            // 여행기 내용 입력하기 텍스트 필드
            CustomOutlinedTextField(
                textFieldValue = tripNoteWriteViewModel.tripNoteContent,
                label = "후기",
                placeHolder = "여행기 후기를 입력해 주세요",
                trailingIconMode = LikeLionOutlinedTextFieldEndIconMode.TEXT,
                singleLine = false,
            )

            // Spacer(modifier = Modifier.padding(top = 20.dp))

            Spacer(modifier = Modifier.weight(1f))

            BlueButton(
                text = "게시하기",
                paddingTop = 5.dp,
                onClick = {
                    tripNoteWriteViewModel.tripNoteDoneClick()
                }
            )

            Spacer(modifier = Modifier.padding(top = 20.dp))
        }
    }
}

package com.lion.wandertrip.presentation.user_info_page

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lion.a02_boardcloneproject.component.CustomIconButton
import com.lion.a02_boardcloneproject.component.CustomOutlinedTextField
import com.lion.a02_boardcloneproject.component.CustomTopAppBar
import com.lion.wandertrip.R
import com.lion.wandertrip.component.BlueButton
import com.lion.wandertrip.util.Tools
import com.skydoves.landscapist.CircularReveal
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun UserInfoScreen(userInfoViewModel: UserInfoViewModel = hiltViewModel()) {

    LaunchedEffect(Unit) {
        userInfoViewModel.hasImageInApplication()
    }

    val sh = userInfoViewModel.tripApplication.screenHeight
    // 촬영된 사진의 uri를 담을 객체
    lateinit var contentUri: Uri

    val tripApplication = userInfoViewModel.tripApplication

    // 사진 촬영용 런처
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
        if (it) {
            Tools.takePictureData(tripApplication, contentUri, userInfoViewModel.imageBitmapState)
            userInfoViewModel.isImagePicked.value = true
        }
    }

    // 앨범용 런처
    val albumLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) {
            Tools.takeAlbumData(tripApplication, it, userInfoViewModel.imageBitmapState)
            if (it != null) {
                userInfoViewModel.isImagePicked.value = true
            }
        }


    Scaffold(
        topBar = {
            CustomTopAppBar(
                menuItems = {
                    // 변경사항 저장 아이콘
                    CustomIconButton(
                        ImageVector.vectorResource(R.drawable.ic_check_24px),
                        iconButtonOnClick = {
                            userInfoViewModel.onClickIconCheck()
                        }
                    )
                },
                // 뒤로가기 아이콘
                navigationIconImage = Icons.AutoMirrored.Filled.ArrowBack,
                navigationIconOnClick = {
                    userInfoViewModel.onClickNavIconBack()
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(horizontal = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally // 가로 정렬 중앙
        ) {
            Spacer(modifier = Modifier.height(40.dp)) // 상단 여백

            GlideImage(
                imageModel = when {
                    userInfoViewModel.isImagePicked.value -> userInfoViewModel.imageBitmapState.value
                    userInfoViewModel.showImageUri.value != null -> userInfoViewModel.showImageUri.value
                    else -> R.drawable.img_basic_profile_image
                },
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height((sh / 7).dp)
                    .clip(RoundedCornerShape(60)),  // 이미지 둥글게 만들기
                circularReveal = CircularReveal(duration = 250),
                placeHolder = ImageBitmap.imageResource(R.drawable.img_basic_profile_image),
            )

            Spacer(modifier = Modifier.height(20.dp)) // 상단 여백

            CustomOutlinedTextField(
                textFieldValue = userInfoViewModel.textFieldUserNicknameValue,
                singleLine = true,
            )

            Spacer(modifier = Modifier.height(20.dp)) // 상단 여백

            // 버튼 섹션
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp), // 버튼 간격 조정
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BlueButton(text = "카메라로 촬영") {
                    // 카메라 촬영 동작 추가
                    // 사진이 저정될 uri 객체를 가져온다.
                    contentUri = Tools.gettingPictureUri(tripApplication)
                    // 런처를 가동한다.
                    cameraLauncher.launch(contentUri)
                }

                BlueButton(text = "갤러리에서 가져오기") {
                    // 갤러리에서 이미지 선택 동작 추가
                    val pickVisualMediaRequest =
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    albumLauncher.launch(pickVisualMediaRequest)
                }

            }

            Spacer(modifier = Modifier.height(40.dp)) // 하단 여백
        }
    }
}
package com.lion.wandertrip.presentation.user_login_page

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lion.a02_boardcloneproject.component.CustomCheckBox
import com.lion.a02_boardcloneproject.component.LikeLionOutlinedTextFieldEndIconMode
import com.lion.a02_boardcloneproject.component.LikeLionOutlinedTextFieldInputType
import com.lion.a02_boardcloneproject.component.CustomAlertDialog
import com.lion.a02_boardcloneproject.component.CustomTopAppBar
import com.lion.a02_boardcloneproject.component.CustomDividerComponent
import com.lion.a02_boardcloneproject.component.CustomOutlinedButton
import com.lion.a02_boardcloneproject.component.CustomOutlinedTextField
import com.lion.wandertrip.component.BlueButton
import com.lion.wandertrip.R
import com.lion.wandertrip.presentation.user_login_page.components.KakaoButton
import com.lion.wandertrip.util.CustomFont

@Composable
fun UserLoginScreen(userLoginViewModel: UserLoginViewModel = hiltViewModel()) {
    Scaffold() {
        Row(
            modifier = Modifier.fillMaxWidth(), // Row가 화면의 전체 너비를 차지하도록 설정
            horizontalArrangement = Arrangement.Center // Column을 수평 가운데 정렬
        ) {
            Column(
                modifier = Modifier
                    .width(300.dp) // 너비 300dp로 설정
                    .padding(it)
                    .padding(start = 10.dp, end = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally // 수평 가운데 정렬
            ) {
                // 아이디 입력란 위에 이미지
                Image(
                    painter = painterResource(id = R.drawable.img_plane),
                    contentDescription = null,
                    modifier = Modifier
                        .size(300.dp)
                        .align(Alignment.CenterHorizontally)
                )
                Text("WanderTrip")

                // 빈공간
                Box(modifier = Modifier.height(130.dp))

                // 아이디 입력 요소
                CustomOutlinedTextField(
                    width = 280.dp,
                    textFieldValue = userLoginViewModel.textFieldUserLoginIdValue,
                    label = "아이디",
                    placeHolder = "아이디를 입력해주세요",
                    inputCondition = "[^a-zA-Z0-9_]", // 입력 조건
                    leadingIcon = ImageVector.vectorResource(R.drawable.ic_person_24px), // 아이콘
                    trailingIconMode = LikeLionOutlinedTextFieldEndIconMode.TEXT,
                    singleLine = true,
                    focusRequest = userLoginViewModel.textFieldUserLoginIdFocusRequester
                )


                // 비밀번호 입력 요소
                CustomOutlinedTextField(
                    width = 280.dp,
                    textFieldValue = userLoginViewModel.textFieldUserLoginPasswordValue,
                    label = "비밀번호",
                    placeHolder = "비밀번호를 입력해주세요",
                    inputCondition = "[^a-zA-Z0-9_]",
                    leadingIcon = ImageVector.vectorResource(R.drawable.ic_key_24px),
                    trailingIconMode = LikeLionOutlinedTextFieldEndIconMode.PASSWORD,
                    singleLine = true,
                    paddingTop = 10.dp,
                    inputType = LikeLionOutlinedTextFieldInputType.PASSWORD,
                    focusRequest = userLoginViewModel.textFieldUserLoginPasswordFocusRequester,
                )


                // 자동로그인, 회원가입 버튼
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // CustomCheckBox
                    CustomCheckBox(
                        text = "자동 로그인",
                        checkedValue = userLoginViewModel.checkBoxAutoLoginValue,
                    )

                    // 회원가입 버튼
                    TextButton(
                        onClick = {
                            // 회원가입 버튼 클릭 시 동작
                            userLoginViewModel.buttonUserJoinClick()
                        },
                    ) {
                        Text(text = "회원가입") // "회원가입" 텍스트
                    }
                }

                BlueButton(
                    buttonWidth = 200.dp,
                    text = "로그인",
                    paddingTop = 5.dp,
                    onClick = {
                        userLoginViewModel.buttonUserLoginOnClick()
                    }
                )

                KakaoButton(
                    paddingTop = 5.dp,
                    onClick = {}
                )


                // 아이디 입력 오류 다이얼로그
                CustomAlertDialog(
                    showDialogState = userLoginViewModel.alertDialogUserIdState,
                    confirmButtonTitle = "확인",
                    confirmButtonOnClick = {
                        userLoginViewModel.alertDialogUserIdState.value = false
                        userLoginViewModel.textFieldUserLoginIdFocusRequester.value.requestFocus()
                    },
                    title = "아이디 입력 오류",
                    text = "아이디를 입력해주세요"
                )

                // 비밀번호 입력 오류 다이얼로그
                CustomAlertDialog(
                    showDialogState = userLoginViewModel.alertDialogUserPwState,
                    confirmButtonTitle = "확인",
                    confirmButtonOnClick = {
                        userLoginViewModel.alertDialogUserPwState.value = false
                        userLoginViewModel.textFieldUserLoginPasswordFocusRequester.value.requestFocus()
                    },
                    title = "비밀번호 입력 오류",
                    text = "비밀번호를 입력해주세요"
                )

                // 존재하지 않는 아이디
                CustomAlertDialog(
                    showDialogState = userLoginViewModel.alertDialogLoginFail1State,
                    confirmButtonTitle = "확인",
                    confirmButtonOnClick = {
                        userLoginViewModel.textFieldUserLoginIdValue.value = ""
                        userLoginViewModel.textFieldUserLoginPasswordValue.value = ""
                        userLoginViewModel.textFieldUserLoginIdFocusRequester.value.requestFocus()
                        userLoginViewModel.alertDialogLoginFail1State.value = false
                    },
                    title = "로그인 오류",
                    text = "존재하지 않는 아이디 입니다"
                )

                // 잘못된 비밀번호
                CustomAlertDialog(
                    showDialogState = userLoginViewModel.alertDialogLoginFail2State,
                    confirmButtonTitle = "확인",
                    confirmButtonOnClick = {
                        userLoginViewModel.textFieldUserLoginPasswordValue.value = ""
                        userLoginViewModel.textFieldUserLoginPasswordFocusRequester.value.requestFocus()
                        userLoginViewModel.alertDialogLoginFail2State.value = false
                    },
                    title = "로그인 오류",
                    text = "잘못된 비밀번호 입니다"
                )


                // 잘못된 비밀번호
                CustomAlertDialog(
                    showDialogState = userLoginViewModel.alertDialogLoginFail3State,
                    confirmButtonTitle = "확인",
                    confirmButtonOnClick = {
                        userLoginViewModel.textFieldUserLoginIdValue.value = ""
                        userLoginViewModel.textFieldUserLoginPasswordValue.value = ""
                        userLoginViewModel.textFieldUserLoginIdFocusRequester.value.requestFocus()
                        userLoginViewModel.alertDialogLoginFail3State.value = false
                    },
                    title = "로그인 오류",
                    text = "탈퇴한 회원입니다"
                )
            }
        }
    }
}
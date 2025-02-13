package com.lion.wandertrip.presentation.user_login_page

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
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

@Composable
fun UserLoginScreen(userLoginViewModel: UserLoginViewModel = hiltViewModel()){

    Scaffold(
        topBar = {
            CustomTopAppBar(title = "로그인")
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(it).padding(start = 10.dp, end = 10.dp)
        ) {
            // 아이디 입력 요소
            CustomOutlinedTextField(
                textFieldValue = userLoginViewModel.textFieldUserLoginIdValue,
                label = "아이디",
                placeHolder = "아이디를 입력해주세요",
                inputCondition = "[^a-zA-Z0-9_]",
                leadingIcon = ImageVector.vectorResource(R.drawable.ic_person_24px),
                trailingIconMode = LikeLionOutlinedTextFieldEndIconMode.TEXT,
                singleLine = true,
                focusRequest = userLoginViewModel.textFieldUserLoginIdFocusRequester,
            )

            // 비밀번호 입력 요소
            CustomOutlinedTextField(
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

            CustomCheckBox(
                text = "자동 로그인",
                checkedValue = userLoginViewModel.checkBoxAutoLoginValue,
                paddingTop = 10.dp,
            )

            CustomOutlinedButton(
                text = "로그인",
                paddingTop = 10.dp,
                onClick = {
                    userLoginViewModel.buttonUserLoginOnClick()
                }
            )


            CustomDividerComponent(
                paddingTop = 10.dp,
                color = Color.DarkGray
            )

            CustomOutlinedButton(
                text = "회원가입",
                paddingTop = 10.dp,
                onClick = {
                    userLoginViewModel.buttonUserJoinClick()
                }
            )

            BlueButton(
                text = "text",
                onClick={
                    Log.d("test100","onClick")
                }
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
package com.lion.wandertrip.presentation.trip_note_select_down_page

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.lion.a02_boardcloneproject.component.CustomAlertDialog
import com.lion.wandertrip.TripApplication
import com.lion.wandertrip.model.TripNoteModel
import com.lion.wandertrip.model.TripScheduleModel
import com.lion.wandertrip.service.TripNoteService
import com.lion.wandertrip.util.MainScreenName
import com.lion.wandertrip.util.ScheduleScreenName
import com.lion.wandertrip.util.TripNoteScreenName
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.TimeZone
import javax.inject.Inject


@HiltViewModel
class TripNoteSelectDownViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    val tripNoteService: TripNoteService,
) : ViewModel() {

    // 내 일정
    var tripNoteMyScheduleList = mutableStateListOf<TripScheduleModel?>()

    val tripApplication = context as TripApplication
    val userNickName = tripApplication.loginUserModel.userNickName

    // 클릭 일정 문서id
    var scheduleDocId = mutableStateOf("")


    // 리사이클러뷰 데이터 리스트 (다가오는 내 일정 리스트)
    fun gettingTripNoteDetailData() {

        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO){
                tripNoteService.gettingUpcomingScheduleList(userNickName)
            }
            val recyclerViewList  = work1.await()

            // 상태 관리 변수에 담아준다.
            tripNoteMyScheduleList.clear()
            tripNoteMyScheduleList.addAll(recyclerViewList)
        }

    }

    // 뒤로 가기 버튼
    fun navigationButtonClick(){
        tripApplication.navHostController.popBackStack()
    }

    // 일정 제목 입력 화면으로 이동
    fun goScheduleTitleButtonClick(tripNoteScheduleDocId : String, documentId : String){
        // 일정 제목 + 날짜선탸ㅐㄱ 입력 화면으로 이동 - 추후에 tripNoteScheduleDocId 얘도 같이 전달해야됨
        tripApplication.navHostController.navigate(ScheduleScreenName.SCHEDULE_ADD_SCREEN.name)

        // 일정 담기면 그 여행기의 tripNoteScrapCount 증가시키기
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO){
                tripNoteService.addTripNoteScrapCount(documentId)
            }
            work1.join()
        }
    }

    // 다가오는 일정 클릭하면,, 그 일정 문서id 받기
    fun gettingSelectId(tripScheduleDocId: String){
        scheduleDocId.value = tripScheduleDocId
    }


    // ✅ Timestamp -> "YYYY.MM.DD" 형식 변환 함수
    @RequiresApi(Build.VERSION_CODES.O)
    fun formatTimestampToDateString(timestamp: Timestamp): String {
        val localDate = Instant.ofEpochMilli(timestamp.seconds * 1000)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()

        val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd") // ✅ 년-월-일 포맷 적용
        return localDate.format(formatter)
    }

    // 다이얼로그 상태 관리
    val showDialogState = mutableStateOf(false)


    // 다이얼로그 확인 버튼 클릭 시 동작
    fun onConfirmClick() {
        showDialogState.value = false
    }

    // 완료 버튼 클릭 시 다이얼로그 상태 변경 , 담은 일정 문서id랑 내 일정 중에 담아갈 일정 id
    fun selectFinishButtonClick(tripNoteScheduleDocId : String, scheduleDocId : String, documentId : String ) {


        Log.d("TripNoteSelectDownViewModel - 담아갈 일정", "tripNoteScheduleDocId: ${tripNoteScheduleDocId}")
        Log.d("TripNoteSelectDownViewModel - 내 일정 중에 담아가려고 일정", "scheduleDocId: $scheduleDocId")
        Log.d("TripNoteSelectDownViewModel - 담아갈 여행기", "documentId: $documentId")




//        // 스케쥴 디테일 페이지로 이동 - 추후에 tripNoteScheduleDocId, scheduleDocId 각각 전달..
//        tripApplication.navHostController.navigate(
//            "${ScheduleScreenName.SCHEDULE_DETAIL_SCREEN.name}?" +
//                  "tripScheduleDocId=${scheduleDocId}")



        // 일정 담기면 그 여행기의 tripNoteScrapCount 증가시키기
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO){
                tripNoteService.addTripNoteScrapCount(documentId)
            }
            work1.join()
        }

    }

//    fun selectFinishButtonClick(
//        tripNoteScheduleDocId: String,
//        scheduleDocId: String,
//        documentId: String
//    ) {
//        Log.d("TripNoteSelectDownViewModel - 담아갈 일정", "tripNoteScheduleDocId: $tripNoteScheduleDocId")
//        Log.d("TripNoteSelectDownViewModel - 내 일정 중에 담아가려고 일정", "scheduleDocId: $scheduleDocId")
//        Log.d("TripNoteSelectDownViewModel - 담아갈 여행기", "documentId: $documentId")
//
//        val firestore = FirebaseFirestore.getInstance()
//
//        // 1. 원본 일정 문서와 그 서브컬렉션 가져오기
//        val tripNoteScheduleRef = firestore.collection("TripSchedule").document(tripNoteScheduleDocId)
//        val scheduleRef = firestore.collection("TripSchedule").document(scheduleDocId)
//
//        tripNoteScheduleRef.get().addOnSuccessListener { originalScheduleDoc ->
//            if (originalScheduleDoc.exists()) {
//                val originalScheduleStartDate = (originalScheduleDoc.get("scheduleStartDate") as? Timestamp)?.seconds ?: return@addOnSuccessListener
//                val originalScheduleEndDate = (originalScheduleDoc.get("scheduleEndDate") as? Timestamp)?.seconds ?: return@addOnSuccessListener
//
//                // 2. TripScheduleItem 서브컬렉션 데이터 가져오기
//                tripNoteScheduleRef.collection("TripScheduleItem").get().addOnSuccessListener { snapshot ->
//                    val itemDates = mutableMapOf<Long, Int>() // 날짜별로 itemIndex 개수를 기록
//                    val totalOriginalDuration = originalScheduleEndDate - originalScheduleStartDate
//
//                    snapshot.documents.forEach { document ->
//                        val itemData = document.data?.toMutableMap() ?: return@forEach
//                        val itemDate = (itemData["itemDate"] as? Timestamp)?.seconds ?: return@forEach
//                        val itemDateOnly = getDateOnly(itemDate) // 날짜만 추출
//                        val currentIndexCount = itemDates.getOrDefault(itemDateOnly, 0)
//                        itemDates[itemDateOnly] = maxOf(currentIndexCount, (itemData["itemIndex"] as? Long ?: 0).toInt()) // 최대 itemIndex 값을 기록
//                    }
//
//                    // 3. 날짜 변환 및 itemIndex 갱신
//                    snapshot.documents.forEach { document ->
//                        val itemData = document.data?.toMutableMap() ?: return@forEach
//                        val itemDate = (itemData["itemDate"] as? Timestamp)?.seconds ?: return@forEach
//
//                        // 4. 기존 일정의 날짜 변환 및 itemIndex 갱신
//                        getAdjustedItemDate(originalScheduleStartDate, originalScheduleEndDate, itemDate, scheduleRef) { newItemDate ->
//                            // itemDate 수정
//                            itemData["itemDate"] = Timestamp(newItemDate, 0)
//
//                            // 날짜별로 itemIndex 갱신
//                            val newItemDateOnly = getDateOnly(newItemDate) // 새로운 날짜 추출
//                            val newItemIndex = itemDates.getOrDefault(newItemDateOnly, 0) + 1 // 해당 날짜에서 가장 큰 itemIndex 값 + 1
//
//                            // itemIndex 갱신
//                            itemData["itemIndex"] = newItemIndex
//
//                            // 새 문서 생성 후 itemDocId 필드에 문서 아이디 추가
//                            scheduleRef.collection("TripScheduleItem").add(itemData).addOnSuccessListener { newDocument ->
//                                // 생성된 새 문서 아이디를 itemDocId 필드에 저장
//                                scheduleRef.collection("TripScheduleItem").document(newDocument.id).update("itemDocId", newDocument.id).addOnSuccessListener {
//                                    Log.d("Firestore", "✅ 새 일정에 항목 추가 완료: ${newDocument.id} 아이디 복사 완료")
//                                }.addOnFailureListener { e ->
//                                    Log.e("Firestore", "❌ 아이디 복사 실패: ${e.message}")
//                                }
//                            }.addOnFailureListener { e ->
//                                Log.e("Firestore", "❌ 새 일정 항목 추가 실패: ${e.message}")
//                            }
//
//                            // 날짜별 itemIndex 리스트 갱신
//                            itemDates[newItemDateOnly] = newItemIndex
//                        }
//                    }
//
//                    // 5. 기존 일정의 itemIndex 갱신
//                    snapshot.documents.forEach { document ->
//                        val itemData = document.data?.toMutableMap() ?: return@forEach
//                        val itemDate = (itemData["itemDate"] as? Timestamp)?.seconds ?: return@forEach
//                        val itemDateOnly = getDateOnly(itemDate)
//
//                        // 기존 일정에서 동일 날짜의 itemIndex 갱신
//                        scheduleRef.collection("TripScheduleItem").whereEqualTo("itemDate", Timestamp(itemDateOnly, 0)).get()
//                            .addOnSuccessListener { querySnapshot ->
//                                querySnapshot.forEach { doc ->
//                                    // itemIndex 갱신
//                                    scheduleRef.collection("TripScheduleItem").document(doc.id)
//                                        .update("itemIndex", itemDates[itemDateOnly] ?: 0)
//                                }
//                            }
//                    }
//                }
//            } else {
//                Log.e("Firestore", "❌ 원본 일정 문서를 찾을 수 없음.")
//            }
//        }.addOnFailureListener { e ->
//            Log.e("Firestore", "❌ 일정 가져오기 실패: ${e.message}")
//        }
//    }

    // 날짜만 추출 (시간을 제외한 날짜)
    fun getDateOnly(timestamp: Long): Long {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC+9")) // 서울 시간대 설정
        calendar.timeInMillis = timestamp * 1000 // milliseconds로 변환
        calendar.set(Calendar.HOUR_OF_DAY, 0) // 시간을 00:00:00로 설정
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        return calendar.timeInMillis / 1000 // 초 단위로 변환
    }

    fun getAdjustedItemDate(
        originalStartDate: Long,
        originalEndDate: Long,
        itemDate: Long,
        scheduleRef: DocumentReference,
        callback: (Long) -> Unit
    ) {
        scheduleRef.get().addOnSuccessListener { scheduleDoc ->
            val newScheduleStartDate = (scheduleDoc.get("scheduleStartDate") as? Timestamp)?.seconds ?: return@addOnSuccessListener
            val newScheduleEndDate = (scheduleDoc.get("scheduleEndDate") as? Timestamp)?.seconds ?: return@addOnSuccessListener

            // 원본 일정과 새 일정의 범위 차이를 계산
            val totalOriginalDuration = originalEndDate - originalStartDate
            val totalNewDuration = newScheduleEndDate - newScheduleStartDate

            // 원본 일정이 새 일정보다 짧을 때
            if (totalOriginalDuration <= totalNewDuration) {
                when {
                    itemDate in originalStartDate..originalEndDate -> {
                        // 원본 일정 날짜가 새 일정에 맞춰 배치
                        val adjustedDate = newScheduleStartDate + (itemDate - originalStartDate)
                        callback(adjustedDate)
                    }
                    itemDate > originalEndDate -> {
                        // 원본 일정 끝난 날짜 이후로 이어서 배치 (새 일정 끝날자에 맞게 배치)
                        callback(newScheduleEndDate) // 새로운 일정 끝날자에 맞추기
                    }
                }
            } else {
                // 원본 일정이 새 일정보다 길 경우
                when {
                    itemDate in originalStartDate..originalEndDate -> {
                        // 원본 일정의 첫날부터 새 일정 첫날로 맞춰 배치
                        val adjustedDate = newScheduleStartDate + (itemDate - originalStartDate)
                        callback(adjustedDate)
                    }
                    itemDate > originalEndDate -> {
                        // 남은 날짜는 새 일정의 끝날자에 맞춰 모두 배치
                        callback(newScheduleEndDate) // 남은 날짜를 새 일정 끝날자에 맞추기
                    }
                }
            }
        }.addOnFailureListener { e ->
            Log.e("Firestore", "❌ 일정 문서 가져오기 실패: ${e.message}")
        }
    }






























    // 새 일정 담기 버튼 다이얼로그 취소 버튼 클릭 시 동작
    fun onDismissClick() {
        showDialogState.value = false
    }


    // 다이얼로그 상태 관리
    val showDialogNotState = mutableStateOf(false)

    // 새 일정 담기 버튼 다이얼로그 취소 버튼 클릭 시 동작
    fun onDismissNotClick() {
        showDialogNotState.value = false
    }


    // 다이얼로그 확인 버튼 클릭 시 동작
    fun onConfirmNotClick() {
        showDialogNotState.value = false
    }


    // 다이얼로그 상태 관리
    val showDialogStateNew = mutableStateOf(false)



    // 새 일정 담기 버튼 클릭 시 다이얼로그 상태 변경
    fun selectNewButtonClick() {
        showDialogStateNew.value = true
    }

    // 새 일정 담기 버튼 다이얼로그 확인 버튼 클릭 시 동작
    fun onConfirmNewClick() {
        showDialogStateNew.value = false
        // 일정 제목 그거로 이동
    }

    // 새 일정 담기 버튼 다이얼로그 취소 버튼 클릭 시 동작
    fun onDismissNewClick() {
        showDialogStateNew.value = false
    }





    //    // 만약 일정 담기 화면에서 새 일정 만들어 담기로 온 경우,,, (tripNoteScheduleDocId - 복사할 일정 문서 id)
//    fun moveToScheduleDetailScreen(tripNoteScheduleDocId: String) {
//        val firestore = FirebaseFirestore.getInstance()
//
//        val formattedTitle = scheduleTitle.value
//        val startTimestamp = Timestamp(scheduleStartDate.value.seconds, 0) // 🔹 Long → Timestamp 변환
//        val endTimestamp = Timestamp(scheduleEndDate.value.seconds, 0)
//        val scheduleDateList = generateDateList(startTimestamp, endTimestamp) // 날짜 리스트 생성
//
//        val tripScheduleRef = firestore.collection("TripSchedule").document(tripNoteScheduleDocId)
//
//        tripScheduleRef.get().addOnSuccessListener { document ->
//            if (document.exists()) {
//                val newTripScheduleRef = firestore.collection("TripSchedule").document()
//
//                val newTripData = document.data?.toMutableMap() ?: mutableMapOf()
//
//                newTripData["scheduleTitle"] = formattedTitle
//                newTripData["scheduleStartDate"] = startTimestamp
//                newTripData["scheduleEndDate"] = endTimestamp
//                newTripData["scheduleTimeStamp"] = Timestamp.now()
//                newTripData["scheduleDateList"] = scheduleDateList
//                newTripData["userID"] = application.loginUserModel.userId
//                newTripData["userNickName"] = application.loginUserModel.userNickName
//                newTripData["scheduleInviteList"] = null
//                newTripData["tripScheduleDocId"] = newTripScheduleRef
//
//                newTripScheduleRef.set(newTripData).addOnSuccessListener {
//                    Log.d("Firestore", "새 일정 문서 생성 완료: ${newTripScheduleRef.id}")
//                    copySubcollections(tripScheduleRef, newTripScheduleRef, scheduleStartDate.value.seconds, scheduleEndDate.value.seconds)
//
//                    // 일정 상세로 이동
//                  application.navHostController.navigate(
//                  "${ScheduleScreenName.SCHEDULE_DETAIL_SCREEN.name}?" +
//                  "tripScheduleDocId=${newTripScheduleRef}"
//                  )
//                }.addOnFailureListener { e ->
//                    Log.e("Firestore", "새 문서 생성 실패: ${e.message}")
//                }
//            } else {
//                Log.e("Firestore", "원본 문서를 찾을 수 없음.")
//            }
//        }.addOnFailureListener { e ->
//            Log.e("Firestore", "문서 가져오기 실패: ${e.message}")
//        }
//    }
//
//    // startTimestamp ~ endTimestamp까지의 날짜 리스트 생성
//    fun generateDateList(startTimestamp: Timestamp, endTimestamp: Timestamp): List<Timestamp> {
//        val dateList = mutableListOf<Timestamp>()
//        var current = startTimestamp.seconds
//
//        while (current <= endTimestamp.seconds) {
//            dateList.add(Timestamp(current, 0))
//            current += 86400 // 하루(24시간) 증가 (초 단위)
//        }
//
//        return dateList
//    }
//
//    // 서브컬렉션 복사 함수 (날짜 변환 & itemIndex 정렬 추가)
//    fun copySubcollections(sourceDocRef: DocumentReference, targetDocRef: DocumentReference, oldStartDate: Long, oldEndDate: Long) {
//        sourceDocRef.collection("TripScheduleItem").get().addOnSuccessListener { snapshot ->
//            val newItems = mutableListOf<Pair<Map<String, Any>, Timestamp>>() // (데이터, 변환된 날짜) 저장용
//
//            for (document in snapshot.documents) {
//                val itemData = document.data?.toMutableMap() ?: continue
//                val itemDate = (itemData["itemDate"] as? Timestamp)?.seconds ?: continue
//
//                // 날짜 변환: 원본 날짜 범위 기준으로 상대적 이동
//                val newItemDateSeconds = when {
//                    itemDate <= oldStartDate -> scheduleStartDate.value.seconds
//                    itemDate >= oldEndDate -> scheduleEndDate.value.seconds
//                    else -> scheduleStartDate.value.seconds + (itemDate - oldStartDate)
//                }
//                val newItemDate = Timestamp(newItemDateSeconds, 0)
//
//                itemData["itemDate"] = newItemDate
//                newItems.add(itemData to newItemDate)
//            }
//
//            //날짜별 그룹화 후 itemIndex 재설정
//            newItems.groupBy { it.second }.forEach { (date, items) ->
//                items.sortedBy { it.first["itemIndex"] as? Long ?: 0 }
//                    .forEachIndexed { index, (data, _) ->
//                        data["itemIndex"] = index.toLong() // 로운 index 부여
//
//                        val newItemRef = targetDocRef.collection("TripScheduleItem").document()
//                        newItemRef.set(data).addOnSuccessListener {
//                            Log.d("Firestore", "서브컬렉션 항목 복사 완료: ${newItemRef.id}")
//                        }.addOnFailureListener { e ->
//                            Log.e("Firestore", "서브컬렉션 복사 실패: ${e.message}")
//                        }
//                    }
//            }
//        }.addOnFailureListener { e ->
//            Log.e("Firestore", " 서브컬렉션 가져오기 실패: ${e.message}")
//        }
//    }


}
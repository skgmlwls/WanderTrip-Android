package com.lion.wandertrip.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import androidx.compose.runtime.MutableState
import androidx.core.content.FileProvider
import com.google.firebase.Timestamp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lion.wandertrip.model.RecentTripItemModel
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit

class Tools {
    companion object{

        // 이미지를 회전시키는 메서드
        fun rotateBitmap(bitmap: Bitmap, degree:Int): Bitmap {
            // 회전 이미지를 구하기 위한 변환 행렬
            val matrix = Matrix()
            matrix.postRotate(degree.toFloat())
            // 회전 행렬을 적용하여 회전된 이미지를 생성한다.
            val resultBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, false)
            return resultBitmap
        }

        // 회전 각도값을 구하는 메서드
        fun getDegree(context: Context, uri: Uri):Int{

            // 이미지의 태그 정보에 접근할 수 있는 객체를 생성한다.
            // andorid 10 버전 이상이라면
            val exifInterface = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                // 이미지 데이터를 가져올 수 있는 Content Provider의 Uri를 추출한다.
                val photoUri = MediaStore.setRequireOriginal(uri)
                // 컨텐츠 프로바이더를 통해 파일에 접근할 수 있는 스트림을 추출한다.
                val inputStream = context.contentResolver.openInputStream(photoUri)
                // ExifInterface 객체를 생성한다.
                ExifInterface(inputStream!!)
            } else {
                ExifInterface(uri.path!!)
            }

            // ExifInterface 정보 중 회전 각도 값을 가져온다
            val ori = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1)

            // 회전 각도값을 담는다.
            val degree = when(ori){
                ExifInterface.ORIENTATION_ROTATE_90 -> 90
                ExifInterface.ORIENTATION_ROTATE_180 -> 180
                ExifInterface.ORIENTATION_ROTATE_270 -> 270
                else -> 0
            }

            return degree
        }


        // 이미지의 사이즈를 줄이는 메서드
        fun resizeBitmap(targetWidth:Int, bitmap:Bitmap):Bitmap{
            // 이미지의 축소/확대 비율을 구한다.
            val ratio = targetWidth.toDouble() / bitmap.width.toDouble()
            // 세로 길이를 구한다.
            val targetHeight = (bitmap.height.toDouble() * ratio).toInt()
            // 크기를 조절한 Bitmap 객체를 생성한다.
            val result = Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, false)
            return result
        }

        // 촬영된 사진 데이터를 읽어와 가공하고 상태관리 변수에 담아준다.
        fun takePictureData(context: Context, contentUri: Uri, previewBitmap:MutableState<Bitmap?>){
            // uri를 통해 저장된 사진 데이터를 가져온다.
            val bitmap = BitmapFactory.decodeFile(contentUri.path)
            // 회전 각도값을 가져온다.
            val degree = getDegree(context, contentUri)
            // 회전 시킨 이미지를 가져온다.
            val rotateBitmap = rotateBitmap(bitmap, degree)
            // 사이즈를 줄여준다.
            val resizingBitmap = resizeBitmap(1024, rotateBitmap)

            previewBitmap.value = resizingBitmap

            // 사진 파일은 삭제한다.
            val file = File(contentUri.path!!)
            file.delete()
        }

        // 촬영된 사진이 저장될 경로를 반환하는 메서드
        fun gettingPictureUri(context: Context) : Uri{
            // 외부 저장소의 경로를 가져온다.
            val filePath = context.getExternalFilesDir(null).toString()
            // 카메라 런처를 실행한다.
            // 촬영한 사진이 저장될 파일 이름
            val fileName = "/temp_${System.currentTimeMillis()}.jpg"
            // 경로 + 파일이름
            val picPath = "${filePath}${fileName}"
            val file = File(picPath)

            // 사진이 저장될 위치를 관리하는 Uri 객체를 생성ㅎ
            val contentUri = FileProvider.getUriForFile(context, "com.lion.wandertrip.camera", file)
            return contentUri
        }

        fun takeAlbumData(context: Context, previewUri:Uri?, previewBitmap:MutableState<Bitmap?>){
            // 가져온 사진이 있다면
            if(previewUri != null){
                val bitmap = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                    val source = ImageDecoder.createSource(context.contentResolver, previewUri)
                    ImageDecoder.decodeBitmap(source)
                } else {
                    val cursor = context.contentResolver.query(previewUri, null, null, null, null)
                    cursor?.moveToNext()
                    val idx = cursor?.getColumnIndex(MediaStore.Images.Media.DATA)
                    val source = cursor?.getString(idx!!)

                    BitmapFactory.decodeFile(source)
                }

                val resizeBitmap = resizeBitmap(1024, bitmap)
                previewBitmap.value = resizeBitmap
            }
        }

        fun takeAlbumDataList(context: Context, previewUri: Uri?, previewBitmap: MutableList<Bitmap?>) {
            // 가져온 사진이 있다면
            if (previewUri != null) {
                val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    val source = ImageDecoder.createSource(context.contentResolver, previewUri)
                    ImageDecoder.decodeBitmap(source)
                } else {
                    val cursor = context.contentResolver.query(previewUri, null, null, null, null)
                    cursor?.moveToNext()
                    val idx = cursor?.getColumnIndex(MediaStore.Images.Media.DATA)
                    val source = cursor?.getString(idx!!)

                    BitmapFactory.decodeFile(source)
                }

                val resizeBitmap = resizeBitmap(1024, bitmap)
                previewBitmap.add(resizeBitmap)  // 리스트에 추가
            }
        }


        // 이미지 뷰에 있는 이미지를 파일로 저장한다.
        fun saveBitmap(context: Context, bitmap: Bitmap){
            // 외부 저장소의 경로를 가져온다.
            val filePath = context.getExternalFilesDir(null).toString()
            // 저장할 파일의 경로
            val file = File("${filePath}/uploadTemp.jpg")
            // 파일로 저장한다.
            val fileOutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()
        }
         // timeStamp -> String 변환
         private fun convertToDate(timeStamp: Timestamp): String {
             // Firestore Timestamp를 Date 객체로 변환
             val date = timeStamp.toDate()

             // 한국 시간대 (Asia/Seoul)로 설정
             val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA)
             dateFormat.timeZone = TimeZone.getTimeZone("Asia/Seoul")

             return dateFormat.format(date)
         }

         // 날짜 타입 변경 String-> Timestamp
             // DB에 넣을때 오후 12시로 넣기위해, kst(한국시간 오후 12시) -> utc(세계기준시간 으로 변환)
             // 시간 기준이 달라서 31일을 저장해도 30일로 저장되는 문제를 해결
             // 아마 00시면 분단위로 짤려서 날짜가 조정됨 그래서 안전하게 오후 12시로 저장함
             fun convertToTimestamp(dueDate: String): Timestamp {
                 // 날짜 포맷터 생성
                 val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN)
                 dateFormatter.timeZone = TimeZone.getTimeZone("Asia/Seoul")

                 return try {
                     // 문자열을 Date 객체로 변환
                     val parsedDate = dateFormatter.parse(dueDate)

                     if (parsedDate != null) {
                         // 시간을 오후 12시(정오)로 설정
                         val calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"))
                         calendar.time = parsedDate
                         calendar.set(Calendar.HOUR_OF_DAY, 12)
                         calendar.set(Calendar.MINUTE, 0)
                         calendar.set(Calendar.SECOND, 0)
                         calendar.set(Calendar.MILLISECOND, 0)

                         // 변경된 시간을 UTC로 변환하여 Timestamp 객체 생성
                         val utcCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                         utcCalendar.timeInMillis = calendar.timeInMillis
                         Timestamp(utcCalendar.time)
                     } else {
                         Timestamp.now()  // 날짜가 잘못된 경우 현재 시간을 반환
                     }
                 } catch (e: Exception) {
                     e.printStackTrace()
                     Timestamp.now()  // 예외 발생 시 현재 시간 반환
                 }
             }

        // 남은 일정 가져오기
        fun getRemainingDays(targetDate: Timestamp): Int {
            val now = System.currentTimeMillis() // 현재 시간 (밀리초)
            val target = targetDate.toDate().time // Timestamp -> Date 변환 후 밀리초 값 가져오기

            val difference = target - now // 남은 시간 (밀리초)
            return TimeUnit.MILLISECONDS.toDays(difference).toInt() // 밀리초 -> 일(day) 변환
        }

        val areaCodeMap = mapOf(

            "1" to "서울",
            "2" to "인천",
            "3" to "대전",
            "4" to "대구",
            "5" to "광주",
            "6" to "부산",
            "7" to "울산",
            "8" to "세종특별자치시",
            "31" to "경기도",
            "32" to "강원특별자치도",
            "33" to "충청북도",
            "34" to "충청남도",
            "35" to "경상북도",
            "36" to "경상남도",
            "37" to "전북특별자치도",
            "38" to "전라남도",
            "39" to "제주도"
        )

        // 지역코드와 시군구 코드를 다루는 맵
        val AreaCityMap = mapOf(
            "1" to mapOf(
                "name" to "서울",
                "subAreas" to mapOf(
                    "1" to "강남구",
                    "2" to "강동구",
                    "3" to "강북구",
                    "4" to "강서구",
                    "5" to "관악구",
                    "6" to "광진구",
                    "7" to "구로구",
                    "8" to "금천구",
                    "9" to "노원구",
                    "10" to "도봉구",
                    "11" to "동대문구",
                    "12" to "동작구",
                    "13" to "마포구",
                    "14" to "서대문구",
                    "15" to "서초구",
                    "16" to "성동구",
                    "17" to "성북구",
                    "18" to "송파구",
                    "19" to "양천구",
                    "20" to "영등포구",
                    "21" to "용산구",
                    "22" to "은평구",
                    "23" to "종로구",
                    "24" to "중구",
                    "25" to "중랑구"
                )
            ),
            "2" to mapOf(
                "name" to "인천",
                "subAreas" to mapOf(
                    "1" to "강화군",
                    "2" to "계양구",
                    "3" to "미추홀구",
                    "4" to "남동구",
                    "5" to "동구",
                    "6" to "부평구",
                    "7" to "서구",
                    "8" to "연수구",
                    "9" to "옹진군",
                    "10" to "중구"
                )
            ),
            "3" to mapOf(
                "name" to "대전",
                "subAreas" to mapOf(
                    "1" to "대덕구",
                    "2" to "동구",
                    "3" to "서구",
                    "4" to "유성구",
                    "5" to "중구"
                )
            ),
            "4" to mapOf(
                "name" to "대구",
                "subAreas" to mapOf(
                    "1" to "남구",
                    "2" to "달서구",
                    "3" to "달성군",
                    "4" to "동구",
                    "5" to "북구",
                    "6" to "서구",
                    "7" to "수성구",
                    "8" to "중구"
                )
            ),
            "5" to mapOf(
                "name" to "광주",
                "subAreas" to mapOf(
                    "1" to "광산구",
                    "2" to "동구",
                    "3" to "북구",
                    "4" to "서구",
                    "5" to "남구"
                )
            ),
            "6" to mapOf(
                "name" to "부산",
                "subAreas" to mapOf(
                    "1" to "강서구",
                    "2" to "금정구",
                    "3" to "기장군",
                    "4" to "남구",
                    "5" to "동구",
                    "6" to "동래구",
                    "7" to "부산진구",
                    "8" to "북구",
                    "9" to "사상구",
                    "10" to "사하구",
                    "11" to "서구",
                    "12" to "수영구",
                    "13" to "연제구",
                    "14" to "영도구",
                    "15" to "중구",
                    "16" to "해운대구"
                )
            ),
            "7" to mapOf(
                "name" to "울산",
                "subAreas" to mapOf(
                    "1" to "중구",
                    "2" to "남구",
                    "3" to "동구",
                    "4" to "북구",
                    "5" to "울주군"
                )
            ),
            "8" to mapOf(
                "name" to "세종특별자치시",
                "subAreas" to mapOf(
                    "1" to "세종특별자치시"
                )
            ),
            "35" to mapOf(
                "name" to "경상북도",
                "subAreas" to mapOf(
                    "1" to "경산시",
                    "2" to "경주시",
                    "3" to "고령군",
                    "4" to "구미시",
                    "6" to "김천시",
                    "7" to "문경시",
                    "8" to "봉화군",
                    "9" to "상주시",
                    "10" to "성주군",
                    "11" to "안동시",
                    "12" to "영덕군",
                    "13" to "영양군",
                    "14" to "영주시",
                    "15" to "영천시",
                    "16" to "예천군",
                    "17" to "울릉군",
                    "18" to "울진군",
                    "19" to "의성군",
                    "20" to "청도군",
                    "21" to "청송군",
                    "22" to "칠곡군",
                    "23" to "포항시"
                )
            ),
            "36" to mapOf(
                "name" to "경상남도",
                "subAreas" to mapOf(
                    "1" to "거제시",
                    "2" to "거창군",
                    "3" to "고성군",
                    "4" to "김해시",
                    "5" to "남해군",
                    "6" to "마산시",
                    "7" to "밀양시",
                    "8" to "사천시",
                    "9" to "간청군",
                    "10" to "양산시",
                    "12" to "의령군",
                    "13" to "진주시",
                    "14" to "진해시",
                    "15" to "창녕군",
                    "16" to "창원시",
                    "17" to "통영시",
                    "18" to "하동군",
                    "19" to "함안군",
                    "20" to "함양군",
                    "21" to "합천군"
                )
            ),
            "37" to mapOf(
                "name" to "전북특별자치도",
                "subAreas" to mapOf(
                    "1" to "고창군",
                    "2" to "군산시",
                    "3" to "김제시",
                    "4" to "남원시",
                    "5" to "무주군",
                    "6" to "부안군",
                    "7" to "순창군",
                    "8" to "완주군",
                    "9" to "익산시",
                    "10" to "임실군",
                    "11" to "장수군",
                    "12" to "전주시",
                    "13" to "정읍시",
                    "14" to "진안군"
                )
            ),
            "38" to mapOf(
                "name" to "전라남도",
                "subAreas" to mapOf(
                    "1" to "강진군",
                    "2" to "고흥군",
                    "3" to "곡성군",
                    "4" to "광양시",
                    "5" to "구례군",
                    "6" to "나주시",
                    "7" to "담양군",
                    "8" to "목포시",
                    "9" to "무안군",
                    "10" to "보성군",
                    "11" to "순천시",
                    "12" to "신안군",
                    "13" to "여수시",
                    "16" to "영광군",
                    "17" to "영암군",
                    "18" to "완도군",
                    "19" to "장성군",
                    "20" to "장흥군",
                    "21" to "진도군",
                    "22" to "함평군",
                    "23" to "해남군",
                    "24" to "화순군"
                )
            ),
            "39" to mapOf(
                "name" to "제주도",
                "subAreas" to mapOf(
                    "1" to "남제주군",
                    "2" to "북제주군",
                    "3" to "서귀포시",
                    "4" to "제주시"
                )
            )
        )
        // 코드로 지역 출력 하는 메서드,
        fun getAreaDetails(areaCode: String, subAreaCode: String? = null): String {
            val areaDetails = AreaCityMap[areaCode]
            return if (areaDetails != null) {
                val cityName = areaDetails["name"]

                if (subAreaCode == null) {
                    // 에어리어 코드만 주어진 경우, 도시 이름 리턴
                    "도시명: $cityName"
                } else {
                    val subAreas = areaDetails["subAreas"] as Map<String, String>
                    val subAreaName = subAreas[subAreaCode]

                    if (subAreaName != null) {
                        // 시군구 코드가 주어진 경우 해당 구 리턴
                        "구명: $subAreaName"
                    } else {
                        "해당 시군구를 찾을 수 없습니다."
                    }
                }
            } else {
                "해당 지역을 찾을 수 없습니다."
            }
        }

        // 검색어로 sigunguCode 와, areaCode 얻는 메서드
        // 강남 --> [1][1] 서울 --> [1][0]
        fun getAreaAndSubAreaCode(areaName: String): String? {
            for ((areaCode, areaData) in AreaCityMap) {
                val cityName : String = areaData["name"].toString()
                val subAreas = areaData["subAreas"] as Map<String, String>

                for ((subAreaCode, subAreaName) in subAreas) {
                    if (subAreaName.contains(areaName)) {
                        return "$areaCode,$subAreaCode" // 첫 번째 일치 항목만 반환
                    }
                }

                // 도시 이름이 일치할 경우
                if (cityName.contains(areaName)) {
                    return "$areaCode,0" // 첫 번째 일치 항목만 반환
                }
            }
            return null // 해당 지역을 찾지 못한 경우 null 반환
        }

        // 최근 목록 더미 저장
        fun addRecentItemList(context: Context, newItem: RecentTripItemModel) {
            // SharedPreferences 인스턴스 가져오기
            val sharedPreferences = context.getSharedPreferences("RecentItem", Context.MODE_PRIVATE)

            // 현재 저장된 리스트 가져오기
            val currentList = getRecentItemList(context).toMutableList()
            // Log.d("test100","currentList : $currentList")

            // 리스트의 크기가 20을 넘으면 가장 오래된 항목(19번)을 삭제
            if (currentList.size >= 20) {
                currentList.removeAt(19)  // 19번 인덱스 항목 삭제
            }

            // 새로운 아이템을 리스트의 첫 번째 위치에 추가
            currentList.add(0, newItem)

            // Gson을 이용하여 리스트를 JSON으로 변환
            val gson = Gson()
            val json = gson.toJson(currentList)

            // SharedPreferences에 최신 리스트 저장
            val editor = sharedPreferences.edit()
            editor.putString("recentItemList", json)
            editor.apply()
        }

        fun getRecentItemList(context: Context): List<RecentTripItemModel> {
            // SharedPreferences 인스턴스 가져오기
            val sharedPreferences = context.getSharedPreferences("RecentItem", Context.MODE_PRIVATE)

            // 저장된 JSON 문자열을 가져오기
            val json = sharedPreferences.getString("recentItemList", null)

            // JSON이 없으면 빈 리스트 반환
            if (json == null) {
                return emptyList()
            }

            // Gson을 사용해 JSON을 List<RecentTripItemModel>로 변환
            val gson = Gson()
            val type = object : TypeToken<List<RecentTripItemModel>>() {}.type
            return gson.fromJson(json, type)
        }

    }
}
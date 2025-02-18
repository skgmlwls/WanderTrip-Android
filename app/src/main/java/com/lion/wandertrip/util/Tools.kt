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
import android.widget.ImageView
import androidx.compose.runtime.MutableState
import androidx.core.content.FileProvider
import com.google.firebase.Timestamp
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
            val contentUri = FileProvider.getUriForFile(context, "com.lion.a02_boardcloneproject.camera", file)
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
            "0" to "전체도시",
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
    }
}
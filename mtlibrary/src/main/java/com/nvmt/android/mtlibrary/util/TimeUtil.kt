package com.nvmt.android.mtlibrary.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object TimeUtil {

    const val DATE_FORMAT_1 = "hh:mm a"
    const val DATE_FORMAT_2 = "h:mm a"
    const val DATE_FORMAT_3 = "yyyy-MM-dd"
    const val DATE_FORMAT_4 = "yyyy-MM-dd HH:mm:ss"
    const val DATE_FORMAT_5 = "yyyy-MM-dd HH:mm"
    const val DATE_FORMAT_6 = "dd MMMM yyyy zzzz"
    const val DATE_FORMAT_7 = "EEE, MMM d, ''yy"
    const val DATE_FORMAT_9 = "h:mm a dd MMMM yyyy"
    const val DATE_FORMAT_10 = "K:mm a, z"
    const val DATE_FORMAT_11 = "hh 'o''clock' a, zzzz"
    const val DATE_FORMAT_12 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    const val DATE_FORMAT_13 = "E, dd MMM yyyy HH:mm:ss z"
    const val DATE_FORMAT_14 = "yyyy.MM.dd G 'at' HH:mm:ss z"
    const val DATE_FORMAT_15 = "yyyyy.MMMMM.dd GGG hh:mm aaa"
    const val DATE_FORMAT_16 = "EEE, d MMM yyyy HH:mm:ss Z"
    const val DATE_FORMAT_17 = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
    const val DATE_FORMAT_18 = "HH:mm dd/MM/yyyy"
    const val DATE_FORMAT_19 = "dd/MM/yyyy"
    const val DATE_FORMAT_20 = "HH:mm"
    const val DATE_FORMAT_21 = "MM/dd"
    const val DATE_FORMAT_22 = "dd_MM_yyyy_HH_mm_ss"
    const val DATE_FORMAT_23 = "dd.MM.yyyy"
    const val DATE_FORMAT_24 = "dd-MM-yyyy"

    const val DATE_FORMAT_MONTH = "MM"
    const val DATE_FORMAT_YEAR = "yyyy"

    const val OUTPUT_DATE_FORMAT = "HH:mm:ss - MM/dd/yyyy"
    const val OUTPUT_DATE_FORMAT2 = "dd/MM/yyyy, HH:mm:ss aa"
    const val OUTPUT_DATE_FORMAT3 = "HH'h':mm, dd/MM"
    const val OUTPUT_DATE_FORMAT_SHORT = "dd/MM/yyyy"

    const val yyyy_MM_dd__HH_mm_ss = "yyyy-MM-dd HH:mm:ss"
    const val HH_mm__dd_MM_yyyy = "HH'h':mm dd/MM/yyyy"

    /**
     * @param dateFormat SimpleDateFormat
     * @return
     */
    fun getCurrentTime(dateFormat: String): String? {
        val sdf = SimpleDateFormat(dateFormat, Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        val today = Calendar.getInstance().time
        return sdf.format(today)
    }

    /**
     * @param timeStamp        in milliseconds (Timestamp)
     * @param dateFormat SimpleDateFormat
     * @return
     */
    fun getDateStringFromTimeStamp(
        timeStamp: Long?,
        dateFormat: String?
    ): String? {
        return try {
            val sdf = SimpleDateFormat(dateFormat, Locale.getDefault())
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            val dateTime = Date(timeStamp!!)
            sdf.format(dateTime)
        } catch (e: Exception) {
            timeStamp.toString()
        }
    }

    /**
     * Get Timestamp from date and time
     *
     * @param dateString   datetime String
     * @param dateFormat Date Format
     * @return
     * @throws ParseException
     */
    @Throws(ParseException::class)
    fun getTimeStampFromDateString(
        dateString: String?,
        dateFormat: String?
    ): Long {
        return try {
            val sdf = SimpleDateFormat(dateFormat, Locale.getDefault())
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            val date = sdf.parse(dateString)
            date.time
        } catch (e: Exception) {
            -1L
        }
    }

    /**
     * Convert one date format string  to another date format string in android
     *
     * @param inputDateFormat  Input SimpleDateFormat
     * @param outputDateFormat Output SimpleDateFormat
     * @param inputDate        input Date String
     * @return
     * @throws ParseException
     */
    fun getDateStringFromDateString(
        inputDateFormat: String?,
        outputDateFormat: String?,
        inputDate: String?
    ): String? {
        return try {
            val mParsedDate: Date
            val mOutputDateString: String
            val mInputDateFormat =
                SimpleDateFormat(inputDateFormat, Locale.getDefault())
            mInputDateFormat.timeZone = TimeZone.getTimeZone("UTC")
            val mOutputDateFormat =
                SimpleDateFormat(outputDateFormat, Locale.getDefault())
            mOutputDateFormat.timeZone = TimeZone.getTimeZone("UTC")
            mParsedDate = mInputDateFormat.parse(inputDate)
            mOutputDateString = mOutputDateFormat.format(mParsedDate)
            mOutputDateString
        } catch (e: ParseException) {
            inputDate
        }
    }

    fun getDateStringFromCalendar(format: String, cal: Calendar?): String {
        if (cal == null) return format
        return try {
            val sdf = SimpleDateFormat(format, Locale.getDefault())
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            sdf.format(cal.time)
        } catch (e: Exception) {
            format
        }
    }

    fun getCalendarFromDateString(format: String, str: String?): Calendar? {
        return try {
            val sdf = SimpleDateFormat(format, Locale.getDefault())
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            val date = sdf.parse(str)
            val cal = Calendar.getInstance()
            cal.time = date
            cal
        } catch (e: Exception) {
            null
        }
    }

    fun getTimePast(timeFormat: String, input: String?): String? {
        val inputcal = getCalendarFromDateString(timeFormat, input)
        val inputTimeMili = inputcal?.timeInMillis ?: return null

        val currentCal = Calendar.getInstance()
        val currentTimeMili = currentCal.timeInMillis

        val timeMili = (currentTimeMili - inputTimeMili) / 1000

        if (timeMili >= 86400) {
            val numDay = timeMili / 86400
            return "$numDay ngày trước"
        }
        if (timeMili >= 3600) {
            val numDay = timeMili / 3600
            return "$numDay giờ trước"
        }
        if (timeMili >= 60) {
            val numDay = timeMili / 60
            return "$numDay phút trước"
        }
        return "Ngay bây giờ"
    }

    fun getTimeLeft(timeFormat: String, input: String?): String? {
        val inputcal = getCalendarFromDateString(timeFormat, input)
        val inputTimeMili = inputcal?.timeInMillis ?: return null
        val currentCal = Calendar.getInstance()
        val currentTimeMili = currentCal.timeInMillis

        val timeMili = (inputTimeMili - currentTimeMili) / 1000
//
//        if (timeMili >= 864000) {
//            return getStringFromCalendar(DATE_FORMAT_19, inputcal)
//        }

        if (timeMili >= 86400) {
            val numDay = timeMili / 86400
            return "$numDay ngày nữa"
        }
        if (timeMili >= 3600) {
            val numDay = timeMili / 3600
            return "$numDay giờ nữa"
        }
        if (timeMili >= 60) {
            val numDay = timeMili / 60
            return "$numDay phút nữa"
        }
        return ""
    }


    fun getCalendarFromMyServer(timeStr: String?): Calendar? {
        try {
            val sdf = SimpleDateFormat(DATE_FORMAT_4, Locale.getDefault())
            val date = sdf.parse(timeStr)

            val cal = Calendar.getInstance()
            cal.time = date
            return cal
        } catch (e: Exception) {
            return null
        }
    }

    fun getStringFromTimeMyServer(format: String, timeStr: String?): String? {
        try {
            val cal = getCalendarFromMyServer(timeStr) ?: return timeStr
            val sdfOutput = SimpleDateFormat(format, Locale.getDefault())
            return sdfOutput.format(cal.time)
        } catch (e: Exception) {
            return timeStr
        }
    }

}
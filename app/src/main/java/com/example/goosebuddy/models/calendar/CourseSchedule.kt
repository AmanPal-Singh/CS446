package com.example.goosebuddy.models.calendar

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.coroutines.awaitStringResponseResult
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

data class TermData(
    val termCode: String
)

data class ClassScheduleData(
    val classNumber: String,
    val scheduleData: List<ScheduleData>,
)

data class ScheduleData(
    val scheduleStartDate: String,
    val scheduleEndDate: String,
    val classMeetingStartTime: String,
    val classMeetingEndTime: String,
    val classMeetingWeekPatternCode: String
)

private val API_KEY_HEADER = "X-API-KEY"
private val API_KEY = "0DFD62ADA40F4B4680AE7EFE4B15CE5A" // TODO: Should extract from an env var
private val TERM_URL = "https://openapi.data.uwaterloo.ca/v3/Terms/current"

suspend fun getScheduleInfo(subject: String, courseNumber: String, classNumber: String) : ScheduleData {
    // First, get current term code
    val (_, _, termResult) = Fuel.get(TERM_URL)
        .header(API_KEY_HEADER, API_KEY)
        .awaitStringResponseResult()
    // By the time this var is used, should be overridden. Or an exception would have been thrown
    var termData = TermData("")

    termResult.fold(
        { data ->
            val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            val jsonAdapter = moshi.adapter(TermData::class.java)
            val nullableTermData = jsonAdapter.nullSafe().lenient().fromJson(data)
            nullableTermData ?.let {
                termData = nullableTermData
            } ?: {
                throw Exception("Deserializing term data resulted in null!")
            }
        },
        { error ->
            throw Exception("Error getting term data from UWaterloo API: ${error.message}")
        }
    )

    val termCode = termData.termCode
    val classScheduleUrl = "https://openapi.data.uwaterloo.ca/v3/ClassSchedules/${termCode}/${subject}/${courseNumber}"
    val (_, _, classScheduleResult) = Fuel.get(classScheduleUrl)
        .header(API_KEY_HEADER, API_KEY)
        .awaitStringResponseResult()
    // By the time this var is used, should be overridden. Or an exception would have been thrown
    var classScheduleData: List<ClassScheduleData> = listOf()

    classScheduleResult.fold(
        { data ->
            val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            val listType = Types.newParameterizedType(List::class.java, ClassScheduleData::class.java)
            val jsonAdapter: JsonAdapter<List<ClassScheduleData>> = moshi.adapter(listType)
            val nullableClassScheduleData = jsonAdapter.nullSafe().lenient().fromJson(data)
            nullableClassScheduleData ?.let {
                classScheduleData = nullableClassScheduleData
            } ?: {
                throw Exception("Deserializing class schedule data resulted in null!")
            }
        },
        { error -> throw Exception("Error getting class schedule data from UWaterloo API: ${error.message}") }
    )

    // Match the provided class number with the available class offerings
    val nullableFound = classScheduleData.find {it ->
        it.classNumber == classNumber
    }
    nullableFound ?.let {
        return nullableFound.scheduleData[0]
    } ?: run {
        throw Exception("No ${subject} ${courseNumber} class found with class number ${classNumber}")
    }
}
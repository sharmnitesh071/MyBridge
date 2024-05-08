package com.databridge.mybridge.common

object Conts {
    // url
    //Dev
    private const val M_BASE_URL = "https://api.bybk.org/"
//    private const val M_BASE_URL = "https://api.mybridge.me/" // live url

    //Live
//    private const val mBaseUrl = "https://api.mybridge.me/"
    const val CREATE_ROOM_URL = "https://chat.mybridge.me/_matrix/client/v3/createRoom"

    //
    const val MAIN_URL = M_BASE_URL
    const val IP_URL = "https://api.ipify.org/?format=json"
    const val SHARE_POST_URL = "https://mybridge.me?post="

    // room database name
    const val DATABASE_NAME = "app.database"

    //Onboarding steps
    const val O_STEP1 = "profile"
    const val O_STEP2 = "employment"
    const val O_STEP3 = "photo"
    const val O_STEP4 = "job_availabilty"
    const val O_STEP5 = "photo"
    const val O_STEP6 = "finish"

    const val RESPONSE_AUTH_FAIL_CODE = 401

    //Shared Preference
    const val PREF_ID = "prefId"
    const val PREF_EMAIL = "prefEmail"
    const val PREF_DISPLAY_NAME = "prefDisplayName"
    const val PREF_FIRST_NAME = "prefFirstName"
    const val PREF_LAST_NAME = "prefLastName"
    const val PREF_COUNTRY = "prefCountry"
    const val PREF_STATE = "prefState"
    const val PREF_CITY = "prefCity"
    const val PREF_PROFILE_PIC = "prefProfilePic"
    const val PREF_PROFILE_PIC_CHARS = "profilePicChars"
    const val PREF_IS_ONBOARDING_COMPLETED = "prefIsOnboardingCompleted"
    const val PREF_IS_EMAIL_VERIFIED = "prefIsEmailVerified"
    const val PREF_EXP_TITLE = "prefExpTitle"
    const val PREF_EMPLOYMENT_TYPE = "prefEmploymentType"
    const val PREF_EXP_COMPANY = "prefEmpCompany"
    const val PREF_EDU_START_YEAR = "prefEduStartYear"
    const val PREF_EDU_END_YEAR = "prefEduEndYear"
    const val PREF_EDU_CLG_NAME = "prefEduClgName"

    const val PREF_PROFILE_ROLE = "prefProfileRole"
    const val PREF_PROFILE_LOCATION = "prefProfileLocation"

    const val EVENT_GENERAL = "general"
    const val EVENT_WEBINAR = "webinar"

    const val POST_TYPE_NORMAL = 1
    const val POST_TYPE_EVENT = 2
    const val POST_TYPE_ARTICLE = 3
}
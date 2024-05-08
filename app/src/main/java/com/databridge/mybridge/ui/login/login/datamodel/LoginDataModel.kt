package com.databridge.mybridge.ui.login.login.datamodel

class LoginDataModel {

    constructor()
    constructor(email: String?, password: String?) {
        this.email = email
        this.password = password
    }


    var email: String? = null
    var password: String? = null


//    fun login(activity: Activity): MutableLiveData<UserData> {
//        val apInterface: APIinterface = APIClient.newRequestRetrofit().create(APIinterface::class.java)
//        val userRepository = UserRepository(apInterface)
//        var map: HashMap<String, String> = HashMap()
//        map.put("email", email.toString())
//        map.put("password", password.toString())
//        map.put("device_token", Utils.getDeviceId(activity))
//        return userRepository.login(map,activity)
//    }


}
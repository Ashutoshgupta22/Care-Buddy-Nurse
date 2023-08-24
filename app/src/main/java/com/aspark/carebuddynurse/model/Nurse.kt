package com.aspark.carebuddynurse.model

class Nurse private constructor(){

    var id =  0
    var firstName: String? = null
    var lastName: String = ""
    var age = 0
    var profilePic: String = ""
    var email: String = ""
    var password: String? = null
    var pincode: String? = null
    var firebaseToken: String? = null
    var latitude = 0.0
    var longitude = 0.0
    var userRole : String? = null
    var locked = false
    var enabled = false
    var biography: String = ""
    var experience: Int = 0
    var qualifications: String = ""
    var specialities: ArrayList<String> = arrayListOf()

    companion object{
        var currentNurse = Nurse()
    }

    operator fun component1() = id
    operator fun component2() = firstName
    operator fun component3() = lastName
    operator fun component4() = email


}
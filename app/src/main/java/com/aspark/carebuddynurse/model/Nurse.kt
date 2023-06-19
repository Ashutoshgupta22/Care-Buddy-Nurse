package com.aspark.carebuddynurse.model

class Nurse {

    var id =  0
    var name: String? = null
    var age = 0
    var email: String? = null
    var password: String? = null
    var pincode: String? = null
    var firebaseToken: String? = null
    var latitude = 0.0
    var longitude = 0.0
    var userRole : String? = null
    var locked = false
    var enabled = false


    companion object{
        var currentNurse = Nurse()
    }

}
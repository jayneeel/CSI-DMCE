package com.example.csi_dmce.profile


class profileCheck(nameParameter:String,branchParameter:String,rollnoParameter:Int,emailParameter:String,passwordParameter:String){

    var name :String = nameParameter
    get() {
        return field
    }
    set(value) {
        if (value.isEmpty())
        {
            field = "Enter a valid Name"
        }
        else {
            field = value
        }
    }


    var branch :String = branchParameter
        get() {
            return field
        }
        set(value) {
            if (value.isEmpty())
            {
             field = "Enter a valid branch"
            }
            else {
                field = value
            }
        }



    var roll :Int = rollnoParameter
        get() {
            return field
        }
        set(value) {
            if (value.equals(1 until 1000))
            {
                field = value
            }
            else {
              field = 0
            }
        }




    var email :String = emailParameter
        get() {
            return field
        }
        set(value) {
            if (value.isEmpty())
            {
             field = "Enter a valid email"
            }
            else {
                field = value
            }
        }



    var password :String= passwordParameter
        get() {
            return field
        }
        set(value) {
            if (value.isEmpty())
            {
              field =  "Enter a valid password"
            }
            else {
                field = value
            }
        }


}



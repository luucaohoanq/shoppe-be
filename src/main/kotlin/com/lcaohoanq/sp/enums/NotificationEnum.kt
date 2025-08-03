package com.lcaohoanq.sp.enums


class NotificationEnum {


    enum class NotificationType {
        INFO,
        WARNING,
        SUCCESS,
        ERROR
    }

    enum class NotificationIcon{
        MESSAGE,
        ALERT,
        CHECK,
        ERROR
    }

    enum class NotificationColor(hex: String){
        BLUE("#2196F3"),
        YELLOW("#FFEB3B"),
        GREEN("#4CAF50"),
        RED("#F44336");
    }


}
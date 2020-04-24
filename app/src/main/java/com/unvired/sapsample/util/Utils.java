package com.unvired.sapsample.util;


import com.unvired.sapsample.be.PERSON_HEADER;

/**
 * Created by nishchith on 24/07/17.
 */

public class Utils {

    public static String getPersonName(PERSON_HEADER personHeader) {
        String name = "";
        String fName = personHeader.getFIRST_NAME();
        String lName = personHeader.getLAST_NAME();
        if (fName != null) {
            name += fName + " ";
        }

        if (lName != null) {
            name += lName;
        }

        return name.trim();
    }

    public static Long getPersonNumber(String message) {
        if (message == null || message.isEmpty()) {
            return 0l;
        }

        try {
            return Long.parseLong(message.substring(message.indexOf("=") + 1, message.lastIndexOf(")")));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0l;
    }
}


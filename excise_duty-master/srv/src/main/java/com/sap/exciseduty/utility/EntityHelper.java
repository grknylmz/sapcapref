package com.sap.exciseduty.utility;

import java.time.LocalDate;

public final class EntityHelper {

    public static LocalDate convertDate(String postingDate) {
        // 2017-12-23T03:34:40 --> 2017-12-23
        return LocalDate.parse(postingDate.substring(0, postingDate.indexOf("T")));
    }

}

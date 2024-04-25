package com.example.auth.domain.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Utils {
    private static final String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
    private static final String DATE_TIME_PATTERN = "dd-MM-yyyy HH:mm:ss.SSS";
    private static final ZoneId ZONE_ID = ZoneId.of("America/Sao_Paulo");

    public static LocalDateTime formatDateTime(LocalDateTime localDateTime) {
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZONE_ID);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
        String formattedDateTime = zonedDateTime.format(dateTimeFormatter);

        return LocalDateTime.parse(formattedDateTime, dateTimeFormatter);
    }

    public static LocalDateTime getDateTimeNowFormatted() {
        return formatDateTime(LocalDateTime.now());
    }

    public static Boolean isStringPresent(String string) {
        return string != null && !string.isBlank();
    }

    public static Boolean isCpfValid(String cpf) {
        return cpf.length() == 11;
    }

    public static Boolean isEmailValid(String email) {
        return email.matches(emailRegex);
    }
}

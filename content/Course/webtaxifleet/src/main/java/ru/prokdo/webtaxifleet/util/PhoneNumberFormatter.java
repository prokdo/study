package ru.prokdo.webtaxifleet.util;

public class PhoneNumberFormatter {
    private PhoneNumberFormatter() {

    }

    public static String format(String phoneNumber) {
        return phoneNumber.replaceAll("[^0-9+]", "");
    }
}

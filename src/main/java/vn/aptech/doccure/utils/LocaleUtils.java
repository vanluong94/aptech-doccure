package vn.aptech.doccure.utils;

import java.util.*;

public class LocaleUtils {

    public static LinkedList<Locale> getAvailableCountries() {

        Set<Locale> locales = new TreeSet<>(new Comparator<Locale>() {
            @Override
            public int compare(Locale l1, Locale l2) {
                return l1.getDisplayCountry().compareToIgnoreCase(l2.getDisplayCountry());
            }

        });

        for (String isoCountry : Locale.getISOCountries()) {
            Locale locale = new Locale("", isoCountry);
            locales.add(locale);
        }

        return new LinkedList<>(locales);

    }

}

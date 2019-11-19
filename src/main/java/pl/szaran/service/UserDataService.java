package pl.szaran.service;

import pl.szaran.exceptions.MyException;
import pl.szaran.service.enums.SortType;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public final class UserDataService {

    private static Scanner scanner = new Scanner(System.in);

    public static SortType getSortType() {
        SortType[] sortTypes = SortType.values();
        AtomicInteger counter = new AtomicInteger(1);

        Arrays.stream(sortTypes).forEach(sortType -> System.out.println(counter.getAndAdd(1) + ". " + sortType));
        System.out.println("Wybierz numer kryterium sortowaina:");
        String text = scanner.nextLine();

        if (!text.matches("[1-" + sortTypes.length + "]")) {
            throw new MyException("Sort types option value is not correct");
        }
        System.out.println("Wybrano: " + sortTypes[Integer.parseInt(text) - 1]);

        return sortTypes[Integer.parseInt(text) - 1];
    }

    public static boolean getSortOrder() {
        System.out.println("1. ROSNĄCO\n2. MALEJĄCO\nWybierz porządek sortowania:");
        String text = scanner.nextLine();

        if (!text.matches("[1-2]")) {
            throw new MyException("Boolean value is not correct");
        }
        return (text.matches("1")) ? true : false;
    }

    public static String getString(String message, String regex) {
        System.out.println(message);
        String text = scanner.nextLine();

        if (!text.matches(regex)) {
            throw new MyException("String value is not correct");
        }

        return text;
    }

    public static BigDecimal getBigDecimal(String message) {
        System.out.println(message);
        String text = scanner.nextLine();

        if (!text.matches("\\d+")) {
            throw new MyException("BigDecimal value is not correct");
        }
        return new BigDecimal(text);

    }

    public static Long getLong(String message) {
        System.out.println(message);
        String text = scanner.nextLine();

        if (!text.matches("\\d+")) {
            throw new MyException("Long value is not correct");
        }
        return new Long(text);
    }

    public static int getInt(String message) {
        System.out.println(message);
        String text = scanner.nextLine();

        if (!text.matches("\\d+")) {
            throw new MyException("Int value is not correct");
        }

        return Integer.parseInt(text);
    }

    public static void close() {
        if (scanner != null) {
            scanner.close();
            scanner = null;
        }
    }
}

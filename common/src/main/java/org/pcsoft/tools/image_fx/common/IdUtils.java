package org.pcsoft.tools.image_fx.common;

import java.util.Random;

/**
 * Created by pfeifchr on 15.07.2014.
 */
public final class IdUtils {

    private static final Random RANDOM = new Random();

    public static String generateId() {
        return generateId(5);
    }

    public static String generateId(int blocks) {
        return generateId(blocks, 5);
    }

    public static String generateId(int blocks, int blockLength) {
        if (blocks <= 0)
            return "";

        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i<blocks; i++) {
            sb.append(generateRandomizedString(blockLength));
            sb.append('-');
        }
        sb.delete(sb.length()-1, sb.length());

        return sb.toString();
    }

    private static String generateRandomizedString(int length) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i<length; i++) {
            sb.append(generateRandomizedCharacter());
        }

        return sb.toString();
    }

    private static char generateRandomizedCharacter() {
        switch (Math.abs(RANDOM.nextInt() % 3)) {
            case 0: {
                final int letter = Math.abs(RANDOM.nextInt() % 26);
                return (char) ('A' + letter);
            }
            case 1: {
                final int letter = Math.abs(RANDOM.nextInt() % 26);
                return (char) ('a' + letter);
            }
            case 2: {
                final int number = Math.abs(RANDOM.nextInt() % 10);
                return (char) ('1' + number);
            }
            default:
                throw new RuntimeException();
        }
    }

    private IdUtils() {
    }
}

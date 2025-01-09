package com.onedreamus.project.global.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class NumberUtils {

    /**
     * 0 ~ max 사이의 수에서
     * n개의 랜덤 수를 뽑기
     */
    public static List<Long> pickRandomNumber(long max, int n) {

        if (n > max) {
            throw new RuntimeException("숫자 배열의 크기보다 뽑으려는 숫자 개수가 더 큽니다.");
        }

        List<Long> numbers = new ArrayList<>();
        for (long i = 0; i < max; i++) {
            numbers.add(i);
        }

        Collections.shuffle(numbers);
        return numbers.subList(0, n);
    }

    public static List<Long> pickRandomNumber(long max, int n, Set<Long> excludedNumbers) {

        if (n > max) {
            throw new RuntimeException("숫자 배열의 크기보다 뽑으려는 숫자 개수가 더 큽니다.");
        }

        List<Long> numbers = new ArrayList<>();
        for (long i = 0; i < max; i++) {
            if (excludedNumbers.contains(i)) {
                continue;
            }
            numbers.add(i);
        }

        Collections.shuffle(numbers);
        return numbers.subList(0, n);
    }

    public static List<Integer> shuffleNumber(int start, int n) {
        List<Integer> numbers = new ArrayList<>();
        for (int i = start; i < start + n; i++) {
            numbers.add(i);
        }

        Collections.shuffle(numbers);
        return numbers;
    }
}

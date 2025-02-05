package com.onedreamus.project.global.util;

import java.util.*;

public class NumberUtils {

    /**
     * 0 ~ max 사이의 수에서
     * n개의 랜덤 수를 뽑기
     */
    public static List<Integer> pickRandomNumByShuffle(int min, int max, int n) {

        if (n > max - min + 1) {
            throw new RuntimeException("숫자 배열의 크기보다 뽑으려는 숫자 개수가 더 큽니다.");
        }

        List<Integer> numbers = new ArrayList<>();
        for (int i = min; i <= max; i++) {
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

    public static List<Integer> pickRandomNumList(int min, int max, int n) {
        List<Integer> randomNumList;

        if (n < 15) {
            randomNumList = pickRandomNumByShuffle(min, max, n);
        } else {
            Set<Integer> randomNumSet = new HashSet<>();
            while (randomNumSet.size() < n) {
                randomNumSet.add(pickRandomNum(min, max));
            }

            randomNumList = new ArrayList<>(randomNumSet);
        }

        return randomNumList;
    }

    /**
     * <p>[랜덤 숫자 획득]</p>
     * min 으로 시작해서 max 로 끝나는 연속된 수 사이에서 랜덤한 수를 뽑음
     *
     * @param min
     * @param max
     * @return
     */
    public static int pickRandomNum(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }
}

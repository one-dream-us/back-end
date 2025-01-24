package com.onedreamus.project.global.util;

import java.util.*;

public class NumberUtils {

    /**
     * 0 ~ max 사이의 수에서
     * n개의 랜덤 수를 뽑기
     */
    public static List<Long> pickRandomNumList(long max, int n) {

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

    public static List<Integer> shuffleNumber(int start, int n) {
        List<Integer> numbers = new ArrayList<>();
        for (int i = start; i < start + n; i++) {
            numbers.add(i);
        }

        Collections.shuffle(numbers);
        return numbers;
    }

    public static List<Integer> pickRandomNumList(int minNum, int maxNum, int cnt) {
        Set<Integer> randomNumSet = new HashSet<>();
        Random random = new Random();
        while (randomNumSet.size() < cnt) {
            randomNumSet.add(pickRandomNum(minNum, maxNum));
        }

        return new ArrayList<>(randomNumSet);
    }

    /**
     * <p>[랜덤 숫자 획득]</p>
     * minNum으로 시작해서 maxNum으로 끝나는 연속된 수 사이에서 랜덤한 수를 뽑음
     * @param minNum
     * @param maxNum
     * @return
     */
    public static int pickRandomNum(int minNum, int maxNum) {
        Random random = new Random();
        return random.nextInt(maxNum) + minNum;
    }
}

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * LeetCode 401 - Binary Watch (비트 조작)
 * https://leetcode.com/problems/binary-watch/
 * [문제]
 * 이진 시계는 위쪽 LED 4 개로 시(0 ~ 11), 아래쪽 LED 6 개로 분(0 ~ 59)을 나타낸다.
 * 켜진 LED 의 개수 turnedOn 이 주어질 때, 시계가 나타낼 수 있는 모든 시각을 반환한다. 순서는 상관없다.
 * - 시는 앞에 0 을 붙이지 않는다. ("01:00" 이 아니라 "1:00")
 * - 분은 항상 두 자리다. ("10:2" 가 아니라 "10:02")
 * [제한사항]
 * - 0 ≤ turnedOn ≤ 10
 *
 * [풀이]
 * 1) LED 가 켜진 자리는 시와 분을 이진수로 썼을 때 1 인 자리와 같다.
 * 그래서 "켜진 LED 개수" = 시의 1 의 개수 + 분의 1 의 개수 이다.
 * 2) 나올 수 있는 시각은 12 × 60 = 720 개뿐이므로 전부 확인한다.
 * 3) Integer.bitCount(h) + Integer.bitCount(m) 이 turnedOn 과 같으면 "시:분" 으로 만들어 답에 넣는다.
 * [시간 복잡도] O(1)  turnedOn 과 상관없이 항상 720 개를 본다
 * [공간 복잡도] O(1)  답을 담는 공간은 제외
 */
public class P20261023 {

    public List<String> readBinaryWatch(int turnedOn) {
        List<String> result = new ArrayList<>();

        for (int h = 0; h < 12; h++) {
            for (int m = 0; m < 60; m++) {
                // 시와 분을 이진수로 썼을 때 1 의 개수가 곧 켜진 LED 의 개수
                if (Integer.bitCount(h) + Integer.bitCount(m) == turnedOn) {
                    result.add(String.format("%d:%02d", h, m));
                }
            }
        }

        return result;
    }

    public static void main(String[] args) {
        P20261023 s = new P20261023();

        // LeetCode 예제
        check(s.readBinaryWatch(1),
                new String[]{"0:01", "0:02", "0:04", "0:08", "0:16", "0:32", "1:00", "2:00", "4:00", "8:00"});
        check(s.readBinaryWatch(9), new String[]{});

        // LED 를 하나도 켜지 않으면 0:00 하나뿐이다
        check(s.readBinaryWatch(0), new String[]{"0:00"});
    }

    /** 순서는 상관없으므로 정렬해서 비교한다 */
    private static void check(List<String> actual, String[] expected) {
        String[] actualArr = actual.toArray(new String[0]);
        String[] expectedArr = expected.clone();
        Arrays.sort(actualArr);
        Arrays.sort(expectedArr);
        boolean ok = Arrays.equals(expectedArr, actualArr);
        System.out.printf("%s expected=%s actual=%s%n", ok ? "PASS" : "FAIL",
                Arrays.toString(expectedArr), Arrays.toString(actualArr));
    }
}

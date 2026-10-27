import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * LeetCode 1 - Two Sum (해시맵)
 * https://leetcode.com/problems/two-sum/
 * [문제]
 * 정수 배열 nums 와 정수 target 이 주어질 때, 더해서 target 이 되는 두 수의 인덱스를 반환한다.
 * 답은 정확히 하나 있고, 같은 원소를 두 번 쓸 수 없다. 인덱스의 순서는 상관없다.
 * [제한사항]
 * - 2 ≤ nums.length ≤ 10^4
 * - -10^9 ≤ nums[i] ≤ 10^9
 * - -10^9 ≤ target ≤ 10^9
 * - 답은 정확히 하나만 존재한다
 *
 * [풀이]
 * 1) 두 수를 모두 짝지어 보면 O(N^2) 이다. 대신 nums[i] 를 볼 때 "짝이 되는 수 target - nums[i] 를
 * 앞에서 본 적이 있는가" 만 알면 되므로, 지금까지 본 수와 그 인덱스를 해시맵에 담아 O(1) 로 찾는다.
 * 2) 배열을 앞에서부터 한 번 돌며 짝이 맵에 있으면 두 인덱스를 바로 반환한다.
 * 3) 없으면 nums[i] 를 맵에 넣는다. 짝을 먼저 찾고 나서 넣으므로 같은 원소를 두 번 쓰지 않는다.
 * [시간 복잡도] O(N)  배열을 한 번 돌고, 맵 조회·삽입은 평균 O(1) 이다
 * [공간 복잡도] O(N)  최악의 경우 모든 수를 맵에 담는다
 */
public class P20261027 {

    public int[] twoSum(int[] nums, int target) {
        // 지금까지 본 수 -> 그 수의 인덱스
        Map<Integer, Integer> seen = new HashMap<>();

        for (int i = 0; i < nums.length; i++) {
            int need = target - nums[i];

            // 짝이 되는 수를 앞에서 이미 봤으면 답이다
            if (seen.containsKey(need)) {
                return new int[]{seen.get(need), i};
            }

            seen.put(nums[i], i);
        }

        // 답이 항상 있으므로 여기까지 오지 않는다
        return new int[0];
    }

    public static void main(String[] args) {
        P20261027 s = new P20261027();

        // LeetCode 예제
        check(s.twoSum(new int[]{2, 7, 11, 15}, 9), new int[]{0, 1});
        check(s.twoSum(new int[]{3, 2, 4}, 6), new int[]{1, 2});
        check(s.twoSum(new int[]{3, 3}, 6), new int[]{0, 1});
    }

    /** 인덱스의 순서는 상관없으므로 정렬해서 비교한다 */
    private static void check(int[] actual, int[] expected) {
        int[] actualArr = actual.clone();
        int[] expectedArr = expected.clone();
        Arrays.sort(actualArr);
        Arrays.sort(expectedArr);
        boolean ok = Arrays.equals(expectedArr, actualArr);
        System.out.printf("%s expected=%s actual=%s%n", ok ? "PASS" : "FAIL",
                Arrays.toString(expectedArr), Arrays.toString(actualArr));
    }
}

/**
 * LeetCode 33 - Search in Rotated Sorted Array (이진 탐색)
 * https://leetcode.com/problems/search-in-rotated-sorted-array/
 *
 * [문제]
 * 오름차순으로 정렬된 배열을 어딘가에서 한 번 회전시킨 배열 nums 와 찾을 값 target 이 주어진다.
 * target 이 있으면 그 인덱스를, 없으면 -1 을 반환한다. 시간 복잡도는 O(log N) 이어야 한다.
 * 예) [0, 1, 2, 4, 5, 6, 7] 을 3 칸 회전시키면 [4, 5, 6, 7, 0, 1, 2]
 *
 * [제한사항]
 * - 1 ≤ nums.length ≤ 5,000
 * - -10^4 ≤ nums[i] ≤ 10^4, 모든 값은 서로 다르다 (중복이 없다)
 * - nums 는 회전된 정렬 배열이다 (0 칸 회전, 즉 그냥 정렬된 배열일 수도 있다)
 * - -10^4 ≤ target ≤ 10^4
 * [풀이]
 * lo = 0, hi = N-1 로 두고 lo <= hi 인 동안 반복한다.
 * 1) mid 값이 target 이면 mid 를 반환
 * 2) 왼쪽 절반이 정렬된 경우 (nums[lo] <= nums[mid])
 *    - nums[lo] <= target < nums[mid] 이면 target 은 왼쪽에 있다 → hi = mid - 1
 *    - 아니면 오른쪽으로 → lo = mid + 1
 * 3) 오른쪽 절반이 정렬된 경우
 *    - nums[mid] < target <= nums[hi] 이면 target 은 오른쪽에 있다 → lo = mid + 1
 *    - 아니면 왼쪽으로 → hi = mid - 1
 * 4) 다 좁혀도 못 찾으면 -1
 * [시간 복잡도] O(log N)  한 번에 구간을 절반으로 줄인다
 * [공간 복잡도] O(1)      인덱스 몇 개만 쓴다
 */
public class P20261009 {

    public int search(int[] nums, int target) {
        int lo = 0;
        int hi = nums.length - 1;

        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;

            if (nums[mid] == target) {
                return mid;
            }

            if (nums[lo] <= nums[mid]) {
                // 왼쪽 절반(lo..mid)이 정렬되어 있다
                if (nums[lo] <= target && target < nums[mid]) {
                    hi = mid - 1;
                } else {
                    lo = mid + 1;
                }
            } else {
                // 오른쪽 절반(mid..hi)이 정렬되어 있다
                if (nums[mid] < target && target <= nums[hi]) {
                    lo = mid + 1;
                } else {
                    hi = mid - 1;
                }
            }
        }

        return -1;
    }

    public static void main(String[] args) {
        P20261009 s = new P20261009();

        // LeetCode 예제
        check(s.search(new int[]{4, 5, 6, 7, 0, 1, 2}, 0), 4);
        check(s.search(new int[]{4, 5, 6, 7, 0, 1, 2}, 3), -1);
        check(s.search(new int[]{1}, 0), -1);
    }

    private static void check(int actual, int expected) {
        boolean ok = expected == actual;
        System.out.printf("%s expected=[%d] actual=[%d]%n", ok ? "PASS" : "FAIL", expected, actual);
    }
}

/**
 * LeetCode 198 - House Robber (DP)
 * https://leetcode.com/problems/house-robber/
 *
 * [문제]
 * 집들이 한 줄로 늘어서 있고 nums[i] 는 i 번째 집에 있는 돈이다.
 * 붙어 있는 두 집을 연달아 털면 경보가 울리므로, 인접한 두 집을 동시에 털 수는 없다.
 * 털 수 있는 돈의 최댓값을 반환한다.
 *
 * [제한사항]
 * - 1 ≤ nums.length ≤ 100
 * - 0 ≤ nums[i] ≤ 400  (음수가 없다. 그래서 "빈 선택"이 최선인 경우는 없다)
 *
 * [자료구조/알고리즘이 왜 DP 인가]
 * - i 번째 집 앞에서 선택지는 둘뿐이다. "턴다" 또는 "안 턴다".
 *   - 털면: i-1 번 집은 못 털므로 i-2 번까지의 최선 + nums[i]
 *   - 안 털면: i-1 번까지의 최선 그대로
 *   둘 중 큰 값이 i 번까지의 최선이다.
 *      best(i) = max(best(i-1), best(i-2) + nums[i])
 * - 계단 오르기(P20261014)와 식의 모양이 같다. 거기서는 두 경우를 "더했고"(경우의 수),
 *   여기서는 두 경우 중 "큰 쪽을 고른다"(최댓값). 세는 문제와 고르는 문제의 차이다.
 * - best(i) 를 구할 때 필요한 것은 바로 앞 두 개뿐이므로 배열 없이 변수 두 개로 충분하다. → 공간 O(1)
 * - 재귀로 그대로 쓰면 같은 구간을 다시 계산해서 O(2^N) 이 된다. 앞에서부터 한 번에 밀고 가면 O(N)
 * [풀이]
 * skip = best(i-1) (i 번 집을 털지 않았을 때의 최선), take = best(i-2) + nums[i] 를 밀고 간다.
 * 1) prev2 = best(i-2), prev1 = best(i-1) 을 각각 0 에서 시작한다.
 * 2) 집을 앞에서부터 하나씩 보며 curr = max(prev1, prev2 + nums[i]) 를 구하고,
 *    prev2 <- prev1, prev1 <- curr 로 한 칸 민다.
 * 3) 다 보고 나면 prev1 이 답이다.
 * → 0 에서 시작하므로 집이 1 개뿐인 경우도 따로 처리할 필요가 없다.
 * [시간 복잡도] O(N)  집을 한 번씩만 본다
 * [공간 복잡도] O(1)  변수 두 개만 쓴다
 */
public class P20261015 {

    public int rob(int[] nums) {
        int prev2 = 0;
        int prev1 = 0;

        for (int money : nums) {
            // 이 집을 안 털면 prev1, 털면 prev2 + money
            int curr = Math.max(prev1, prev2 + money);
            prev2 = prev1;
            prev1 = curr;
        }

        return prev1;
    }

    public static void main(String[] args) {
        P20261015 s = new P20261015();

        // LeetCode 예제
        check(s.rob(new int[]{1, 2, 3, 1}), 4);         // 1번째(1)와 3번째(3)
        check(s.rob(new int[]{2, 7, 9, 3, 1}), 12);     // 2 + 9 + 1
    }

    private static void check(int actual, int expected) {
        boolean ok = expected == actual;
        System.out.printf("%s expected=[%d] actual=[%d]%n", ok ? "PASS" : "FAIL", expected, actual);
    }
}

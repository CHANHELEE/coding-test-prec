/**
 * LeetCode 746 - Min Cost Climbing Stairs (DP)
 * https://leetcode.com/problems/min-cost-climbing-stairs/
 * [문제]
 * 계단마다 밟는 비용 cost[i] 가 정해져 있다. 비용을 내고 밟으면 1 칸 또는 2 칸 올라갈 수 있다.
 * 출발은 0 번 칸이나 1 번 칸 중 어디서든 할 수 있고, 계단 "맨 위"(마지막 칸을 넘어선 자리)까지
 * 올라가는 데 드는 비용의 최솟값을 반환한다.
 * [제한사항]
 * - 2 ≤ cost.length ≤ 1,000
 * - 0 ≤ cost[i] ≤ 999
 *
 * [자료구조/알고리즘이 왜 DP 인가]
 * - 맨 위에 도착하기 직전에 밟은 칸은 "마지막 칸" 아니면 "마지막에서 두 번째 칸" 둘 중 하나다.
 * 어떤 칸 i 에 도착하는 경로도 똑같이 i-1 에서 1 칸, 또는 i-2 에서 2 칸 올라온 것뿐이다.
 * best(i) = min(best(i-1) + cost[i-1], best(i-2) + cost[i-2])
 * (i-1 번 칸까지 온 비용 + 그 칸을 밟고 떠나는 비용) 과 (i-2 번에서 두 칸 뛰는 경우) 중 작은 쪽
 * - 바닥: best(0) = best(1) = 0. 0 번이나 1 번에서 "출발"하는 것은 공짜이기 때문이다.
 * 비용은 그 칸을 "떠날 때" 내는 것으로 보면 식이 깔끔해진다.
 * - 계단 오르기(P20261014)는 같은 모양의 식에서 두 경우를 "더해" 경우의 수를 셌고,
 * 집 도둑(P20261015)은 두 경우 중 "큰 쪽"을 골랐다. 이 문제는 "작은 쪽"을 고른다.
 * 세기 / 최댓값 / 최솟값으로 이어지는 같은 틀의 세 번째 문제다.
 * - best(i) 에 필요한 것은 바로 앞 두 개뿐이라 변수 두 개로 충분하다. → 공간 O(1)
 *
 * [풀이]
 * prev2 = best(i-2), prev1 = best(i-1) 을 모두 0 에서 시작한다.
 * 1) i 를 2 부터 n(= cost.length) 까지 늘려 가며
 * curr = min(prev1 + cost[i-1], prev2 + cost[i-2]) 를 구하고 prev2 <- prev1 <- curr 로 민다.
 * 2) i 가 n 일 때의 값이 "맨 위"까지의 최솟값이다.
 * [시간 복잡도] O(N)  칸을 한 번씩만 본다
 * [공간 복잡도] O(1)  변수 두 개만 쓴다
 */
public class P20261016 {

    public int minCostClimbingStairs(int[] cost) {
        int prev2 = 0;   // best(i-2), 처음에는 best(0) = 0
        int prev1 = 0;   // best(i-1), 처음에는 best(1) = 0

        // 맨 위(= cost.length 번 자리)까지 올라간다
        for (int i = 2; i <= cost.length; i++) {
            // 한 칸 아래에서 올라오거나, 두 칸 아래에서 뛰어오거나
            int curr = Math.min(prev1 + cost[i - 1], prev2 + cost[i - 2]);
            prev2 = prev1;
            prev1 = curr;
        }

        return prev1;
    }

    public static void main(String[] args) {
        P20261016 s = new P20261016();

        // LeetCode 예제
        check(s.minCostClimbingStairs(new int[]{10, 15, 20}), 15);
        check(s.minCostClimbingStairs(new int[]{1, 100, 1, 1, 1, 100, 1, 1, 100, 1}), 6);
    }

    private static void check(int actual, int expected) {
        boolean ok = expected == actual;
        System.out.printf("%s expected=[%d] actual=[%d]%n", ok ? "PASS" : "FAIL", expected, actual);
    }
}

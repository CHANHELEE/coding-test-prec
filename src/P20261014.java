/**
 * LeetCode 70 - Climbing Stairs (DP)
 * https://leetcode.com/problems/climbing-stairs/
 *
 * [문제]
 * n 개의 계단을 오른다. 한 번에 1 칸 또는 2 칸을 오를 수 있다.
 * 맨 위까지 오르는 서로 다른 방법의 수를 반환한다.
 * 예) n = 3 → (1,1,1) (1,2) (2,1) 세 가지
 *
 * [제한사항]
 * - 1 ≤ n ≤ 45
 *
 * [자료구조/알고리즘이 왜 DP 인가]
 * - n 번째 칸에 도착하는 마지막 한 걸음은 "1 칸 오르기" 아니면 "2 칸 오르기" 둘 중 하나다.
 *   → n-1 번 칸까지 온 방법 전부 + n-2 번 칸까지 온 방법 전부
 *      ways(n) = ways(n-1) + ways(n-2)
 *   두 경우는 마지막 걸음이 다르므로 절대 겹치지 않는다. 그래서 그냥 더하면 된다.
 * - 바닥: ways(1) = 1, ways(2) = 2 ((1,1) 과 (2))
 * - 식이 피보나치와 똑같다. ways(n) 은 피보나치 수열의 n+1 번째 항이다.
 * - 이 식을 재귀로 그대로 쓰면 같은 값을 수없이 다시 계산해서 O(2^N) 이 된다.
 *   (ways(5) 를 구하려고 ways(3) 을 두 번, ways(2) 를 세 번 계산하는 식이다)
 *   작은 값부터 차례로 구해 두면 각 값을 한 번만 계산한다. → 이게 DP 다.
 * - 더 줄이면, ways(n) 을 구할 때 필요한 것은 바로 앞 두 개뿐이다.
 *   그래서 배열 없이 변수 두 개만 들고 가도 된다. → 공간 O(1)
 *
 * [풀이]
 * prev = ways(i-2), curr = ways(i-1) 로 두고 i 를 늘려 가며 앞으로 밀어낸다.
 * 1) n 이 1 이면 1, 2 이면 2 (바닥)
 * 2) prev = 1(ways(1)), curr = 2(ways(2)) 에서 시작해 i = 3..n 까지
 *    next = prev + curr 를 구하고, prev <- curr, curr <- next 로 한 칸 민다
 * 3) curr 가 ways(n)
 * [시간 복잡도] O(N)  계단 수만큼 한 번씩 더한다
 * [공간 복잡도] O(1)  변수 두 개만 쓴다
 */
public class P20261014 {

    public int climbStairs(int n) {
        // 바닥: 1 칸은 (1) 한 가지, 2 칸은 (1,1) (2) 두 가지
        if (n <= 2) {
            return n;
        }

        int prev = 1;   // ways(i-2), 처음에는 ways(1)
        int curr = 2;   // ways(i-1), 처음에는 ways(2)

        for (int i = 3; i <= n; i++) {
            int next = prev + curr;   // ways(i) = ways(i-2) + ways(i-1)
            prev = curr;
            curr = next;
        }

        return curr;
    }

    public static void main(String[] args) {
        P20261014 s = new P20261014();

        // LeetCode 예제
        check(s.climbStairs(2), 2);     // (1,1) (2)
        check(s.climbStairs(3), 3);     // (1,1,1) (1,2) (2,1)
    }

    private static void check(int actual, int expected) {
        boolean ok = expected == actual;
        System.out.printf("%s expected=[%d] actual=[%d]%n", ok ? "PASS" : "FAIL", expected, actual);
    }
}

/**
 * LeetCode 64 - Minimum Path Sum (DP)
 * https://leetcode.com/problems/minimum-path-sum/
 * [문제]
 * 음이 아닌 정수로 채워진 m x n 격자 grid 가 주어진다. 왼쪽 위에서 오른쪽 아래까지 가는 경로 중
 * 지나는 칸의 수의 합이 가장 작은 경로를 찾아 그 합을 반환한다.
 * 한 번에 오른쪽 또는 아래로만 움직일 수 있다.
 * [제한사항]
 * - m == grid.length, n == grid[i].length
 * - 1 ≤ m, n ≤ 200
 * - 0 ≤ grid[i][j] ≤ 200
 *
 * [풀이]
 * 1) 오른쪽·아래로만 움직이므로 (i, j) 에 들어오는 칸은 위 (i-1, j) 아니면 왼쪽 (i, j-1) 뿐이다.
 * best(i, j) = grid[i][j] + min(best(i-1, j), best(i, j-1))
 * 경로 수가 C(m+n-2, m-1) 로 폭발하지만, 각 칸의 최솟값은 한 번만 구하면 되므로 DP 가 맞다.
 * 2) 맨 윗줄은 왼쪽에서만, 맨 왼쪽 줄은 위에서만 들어올 수 있다.
 * 3) 한 줄을 구할 때 바로 윗줄만 필요하므로 길이 n 인 배열 dp 하나를 줄마다 덮어쓴다.
 * dp[j] 를 갱신하기 직전의 값이 윗줄 best(i-1, j), 이미 갱신된 dp[j-1] 이 왼쪽 best(i, j-1) 이다.
 * 4) 입력 grid 를 직접 덮어쓰면 공간 O(1) 이지만, 넘겨받은 입력을 바꾸지 않도록 dp 배열을 따로 쓴다.
 * [시간 복잡도] O(M · N)  칸을 한 번씩만 본다
 * [공간 복잡도] O(N)  한 줄 크기의 dp 배열
 */
public class P20261118 {

    public int minPathSum(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        // dp[j] = 지금 줄의 (i, j) 까지 오는 최소 합
        int[] dp = new int[n];

        // 맨 윗줄은 왼쪽에서만 들어온다
        dp[0] = grid[0][0];
        for (int j = 1; j < n; j++) {
            dp[j] = dp[j - 1] + grid[0][j];
        }

        for (int i = 1; i < m; i++) {
            // 맨 왼쪽 칸은 위에서만 들어온다
            dp[0] += grid[i][0];
            for (int j = 1; j < n; j++) {
                // dp[j] 는 아직 윗줄 값, dp[j - 1] 은 이미 이번 줄 값
                dp[j] = grid[i][j] + Math.min(dp[j], dp[j - 1]);
            }
        }

        return dp[n - 1];
    }

    public static void main(String[] args) {
        P20261118 s = new P20261118();

        // LeetCode 예제
        check(s.minPathSum(new int[][]{{1, 3, 1}, {1, 5, 1}, {4, 2, 1}}), 7);
        check(s.minPathSum(new int[][]{{1, 2, 3}, {4, 5, 6}}), 12);
    }

    private static void check(int actual, int expected) {
        boolean ok = expected == actual;
        System.out.printf("%s expected=[%d] actual=[%d]%n", ok ? "PASS" : "FAIL", expected, actual);
    }
}

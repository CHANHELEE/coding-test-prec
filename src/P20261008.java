import java.util.*;

/**
 * LeetCode 994 - Rotting Oranges (BFS)
 * https://leetcode.com/problems/rotting-oranges/
 *
 * [문제]
 * m x n 격자 grid 가 주어진다. 각 칸은 0(빈 칸), 1(신선한 오렌지), 2(썩은 오렌지) 중 하나다.
 * 1 분이 지날 때마다 썩은 오렌지와 상하좌우로 붙은 신선한 오렌지가 썩는다.
 * 모든 오렌지가 썩는 데 걸리는 최소 시간(분)을 반환한다. 끝까지 썩지 않는 오렌지가 남으면 -1 을 반환한다.
 *
 * [제한사항]
 * - 1 ≤ grid.length, grid[0].length ≤ 10
 * - grid[i][j] 는 0, 1, 2 중 하나
 *
 * [자료구조/알고리즘이 왜 BFS 인가]
 * - "1 분에 한 칸씩 동시에 퍼진다"는 것이 곧 BFS 가 한 층씩 퍼져 나가는 모습 그대로다.
 *   → 한 층 처리할 때마다 1 분이 지난 것으로 세면 된다.
 * - 중요한 차이는 시작점이 여러 개라는 점이다. 처음부터 썩어 있던 오렌지를 "전부" 큐에 넣고 시작한다.
 *   (다중 시작점 BFS) 이렇게 하면 여러 곳에서 동시에 퍼지는 상황이 자연스럽게 표현된다.
 * - 썩은 오렌지 하나하나에서 따로 BFS 를 돌려 최솟값을 구하려 하면 훨씬 복잡하고 느리다.
 *   어차피 "가장 먼저 닿는 썩은 오렌지"가 그 칸을 썩히므로, 다 같이 출발시키면 한 번의 BFS 로 끝난다.
 * - 격자 BFS 자체는 게임 맵 최단거리(P20260918)와 같은 모양이다. 시작점이 여러 개인 것만 다르다.
 *
 * [풀이]
 * 1) 격자를 한 번 훑어 신선한 오렌지 수(fresh)를 세고, 썩은 오렌지는 모두 큐에 넣는다.
 * 2) 큐가 빌 때까지 "한 층씩" 처리한다. 한 층 = 지금 큐에 들어있는 것 전체 = 같은 분에 썩은 오렌지들.
 *    각 칸의 상하좌우에 신선한 오렌지가 있으면 썩게 바꾸고(2 로) fresh-- 한 뒤 큐에 넣는다.
 *    한 층을 다 처리했고 새로 썩은 것이 있었으면 minutes++
 * 3) 끝났을 때 fresh 가 0 이면 minutes, 남아 있으면 -1
 *
 * [주의할 점]
 * 1) 신선한 오렌지가 처음부터 없으면 답은 0 이다. 썩은 오렌지가 하나도 없어도 0 이다. (예: [[0, 2]], [[0]])
 *    이 경우를 놓치면 -1 을 반환하는 실수를 한다.
 * 2) minutes 를 "층을 처리할 때마다" 무조건 늘리면 1 이 더 커진다.
 *    마지막 층의 오렌지들은 자기 차례에 아무것도 썩히지 못하기 때문이다.
 *    그래서 "새로 썩은 것이 있을 때만" 분을 센다. (아래 코드에서는 fresh > 0 인 동안만 층을 돈다)
 * 3) 빈 칸(0)은 오렌지가 지나갈 수 없는 벽처럼 다룬다. 빈 칸 뒤에 갇힌 오렌지는 영원히 안 썩어 -1 이 된다.
 * 4) 대각선으로는 번지지 않는다. 방향은 상하좌우 4개뿐이다.
 *
 * [시간 복잡도] O(N * M)  칸마다 한 번씩 큐에 들어가고, 꺼낼 때 이웃 4 칸만 본다
 * [공간 복잡도] O(N * M)  큐
 */
public class P20261008 {

    public int orangesRotting(int[][] grid) {
        // 상, 하, 좌, 우 이동
        int[] dr = {-1, 1, 0, 0};
        int[] dc = {0, 0, -1, 1};

        int n = grid.length;
        int m = grid[0].length;

        Queue<int[]> queue = new ArrayDeque<>();
        int fresh = 0;

        // 썩은 오렌지는 모두 시작점으로 넣고, 신선한 오렌지는 개수만 세어 둔다
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < m; c++) {
                if (grid[r][c] == 2) {
                    queue.offer(new int[]{r, c});
                } else if (grid[r][c] == 1) {
                    fresh++;
                }
            }
        }

        int minutes = 0;

        // 신선한 오렌지가 남아 있는 동안만 분을 센다 (마지막 층은 아무것도 썩히지 못하므로 세지 않는다)
        while (!queue.isEmpty() && fresh > 0) {
            // 지금 큐에 들어있는 것이 "같은 분에 썩은 오렌지" 전체다. 그만큼만 꺼내면 1 분을 처리한 셈이 된다.
            int levelSize = queue.size();
            for (int i = 0; i < levelSize; i++) {
                int[] cur = queue.poll();
                int r = cur[0];
                int c = cur[1];

                for (int d = 0; d < 4; d++) {
                    int nr = r + dr[d];
                    int nc = c + dc[d];

                    // 격자 밖이거나, 신선한 오렌지가 아니면(빈 칸이거나 이미 썩었으면) 건너뛴다
                    if (nr < 0 || nr >= n || nc < 0 || nc >= m) continue;
                    if (grid[nr][nc] != 1) continue;

                    grid[nr][nc] = 2;
                    fresh--;
                    queue.offer(new int[]{nr, nc});
                }
            }
            minutes++;
        }

        // 못 썩은 오렌지가 남아 있으면 -1
        return fresh == 0 ? minutes : -1;
    }

    public static void main(String[] args) {
        P20261008 s = new P20261008();

        // LeetCode 예제
        check(s.orangesRotting(new int[][]{{2, 1, 1}, {1, 1, 0}, {0, 1, 1}}), 4);
        check(s.orangesRotting(new int[][]{{2, 1, 1}, {0, 1, 1}, {1, 0, 1}}), -1);
        check(s.orangesRotting(new int[][]{{0, 2}}), 0);
    }

    private static void check(int actual, int expected) {
        boolean ok = expected == actual;
        System.out.printf("%s expected=[%d] actual=[%d]%n", ok ? "PASS" : "FAIL", expected, actual);
    }
}

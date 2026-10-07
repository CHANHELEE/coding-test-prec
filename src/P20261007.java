import java.util.*;

/**
 * LeetCode 200 - Number of Islands (BFS)
 * https://leetcode.com/problems/number-of-islands/
 *
 * [문제]
 * '1'(땅)과 '0'(물)로 이루어진 2차원 격자 grid 가 주어진다. 섬의 개수를 반환한다.
 * 섬은 물로 둘러싸인 땅이며, 상하좌우로 붙은 땅끼리 하나의 섬이다. (대각선은 붙은 것으로 보지 않는다)
 * 격자의 바깥은 모두 물이라고 가정한다.
 *
 * [제한사항]
 * - 1 ≤ grid.length, grid[0].length ≤ 300
 * - grid[i][j] 는 '0' 또는 '1' (문자다. 숫자 0, 1 이 아니다)
 *
 * [자료구조/알고리즘이 왜 BFS 인가]
 * - 격자를 그래프로 보면 땅 칸이 정점, 상하좌우로 붙은 관계가 간선이다.
 *   그러면 "섬의 개수" = "연결 요소(connected component) 개수"가 된다. 네트워크 문제(P20260917)와 똑같은 구조다.
 * - 아직 방문하지 않은 땅 칸에서 탐색을 시작하면 그 섬에 속한 땅을 전부 방문 처리하게 된다.
 *   → 탐색을 "새로 시작한 횟수"가 곧 섬의 개수다.
 * - 붙어 있는 것을 모두 훑기만 하면 되므로 BFS 든 DFS 든 상관없다. 여기서는 큐를 쓰는 BFS 로 풀었다.
 *   DFS 로 쓰면 코드는 짧아지지만, 300 x 300 이 전부 땅이면 재귀 깊이가 9만까지 갈 수 있어 스택이 위험하다.
 *   BFS 는 재귀를 쓰지 않으니 그 걱정이 없다.
 * - 칸은 최대 9만 개이고 각 칸을 한 번만 방문하므로 충분히 빠르다.
 *
 * [풀이]
 * 1) 모든 칸을 차례로 보면서, 땅('1')이고 아직 방문하지 않은 칸을 만나면 BFS 를 돌리고 count++
 * 2) BFS: 큐에서 칸을 꺼내 상하좌우를 본다.
 *    격자 안이고, 땅이고, 아직 방문하지 않았으면 방문 처리 후 큐에 넣는다.
 * [주의할 점]
 * 1) grid 의 값은 문자다. grid[r][c] == 1 이 아니라 grid[r][c] == '1' 로 비교해야 한다.
 * 2) 대각선은 연결이 아니다. 방향은 상하좌우 4개뿐이다.
 * 3) 방문 처리는 큐에서 "꺼낼 때"가 아니라 "넣을 때" 한다. 꺼낼 때 하면 같은 칸이 큐에 여러 번 들어간다.
 * 4) 방문 표시를 grid 에 직접 '0' 으로 덮어쓰는 풀이도 많다. 메모리를 아낄 수 있지만 입력이 망가져서
 *    같은 grid 로 두 번 호출하면 두 번째는 0 이 나온다. 여기서는 visited 배열을 따로 두어 입력을 건드리지 않는다.
 * 5) 격자가 정사각형이 아닐 수 있다. 행 수와 열 수를 따로 구해야 한다.
 *
 * [시간 복잡도] O(N * M)  칸마다 한 번씩 큐에 들어가고, 꺼낼 때 이웃 4 칸만 본다
 * [공간 복잡도] O(N * M)  visited 배열과 큐
 */
public class P20261007 {

    public int numIslands(char[][] grid) {
        // 상, 하, 좌, 우 이동
        int[] dr = {-1, 1, 0, 0};
        int[] dc = {0, 0, -1, 1};

        int n = grid.length;
        int m = grid[0].length;
        boolean[][] visited = new boolean[n][m];
        int count = 0;

        for (int r = 0; r < n; r++) {
            for (int c = 0; c < m; c++) {
                // 물이거나 이미 다른 섬을 세면서 방문한 칸은 건너뛴다
                if (grid[r][c] == '0' || visited[r][c]) continue;

                // 새로운 섬을 발견했다 → 이 섬에 붙은 땅을 전부 방문 처리한다
                bfs(grid, visited, r, c, dr, dc);
                count++;
            }
        }

        return count;
    }

    private void bfs(char[][] grid, boolean[][] visited, int startRow, int startCol, int[] dr, int[] dc) {
        int n = grid.length;
        int m = grid[0].length;

        Queue<int[]> queue = new ArrayDeque<>();
        queue.offer(new int[]{startRow, startCol});
        visited[startRow][startCol] = true;

        while (!queue.isEmpty()) {
            int[] cur = queue.poll();
            int r = cur[0];
            int c = cur[1];

            for (int d = 0; d < 4; d++) {
                int nr = r + dr[d];
                int nc = c + dc[d];

                // 격자 밖이거나, 물이거나, 이미 방문한 칸이면 건너뛴다
                if (nr < 0 || nr >= n || nc < 0 || nc >= m) continue;
                if (grid[nr][nc] == '0' || visited[nr][nc]) continue;

                visited[nr][nc] = true;
                queue.offer(new int[]{nr, nc});
            }
        }
    }

    public static void main(String[] args) {
        P20261007 s = new P20261007();

        // LeetCode 예제
        check(s.numIslands(grid(
                "11110",
                "11010",
                "11000",
                "00000")), 1);
        check(s.numIslands(grid(
                "11000",
                "11000",
                "00100",
                "00011")), 3);
    }

    /** "11110" 같은 문자열 여러 개를 char[][] 격자로 만든다 */
    private static char[][] grid(String... rows) {
        char[][] grid = new char[rows.length][];
        for (int i = 0; i < rows.length; i++) {
            grid[i] = rows[i].toCharArray();
        }
        return grid;
    }

    private static void check(int actual, int expected) {
        boolean ok = expected == actual;
        System.out.printf("%s expected=[%d] actual=[%d]%n", ok ? "PASS" : "FAIL", expected, actual);
    }
}

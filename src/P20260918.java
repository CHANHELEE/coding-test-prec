import java.util.*;

/**
 * 프로그래머스 - 게임 맵 최단거리 (Level 2, 깊이/너비 우선 탐색(DFS/BFS))
 * https://school.programmers.co.kr/learn/courses/30/lessons/1844
 *
 * [문제]
 * n x m 크기의 맵 maps 가 주어진다. 0 은 벽, 1 은 지나갈 수 있는 칸이다.
 * 캐릭터는 (1, 1)(왼쪽 위)에서 출발해 상대 진영 (n, m)(오른쪽 아래)으로 가야 하며, 동서남북으로 한 칸씩 움직인다.
 * 도착할 때까지 지나가는 칸 개수의 최솟값을 반환한다. 도착할 수 없으면 -1 을 반환한다.
 * (출발 칸과 도착 칸도 개수에 포함한다)
 *
 * [제한사항]
 * - n, m 은 1 이상 100 이하 (n 과 m 이 둘 다 1 인 경우는 없다)
 * - maps 는 0 과 1 로만 이루어져 있다
 * - 출발 칸과 도착 칸은 항상 1 이다
 *
 * [자료구조/알고리즘이 왜 BFS 인가]
 * - 한 칸 움직이는 비용이 모두 1 로 같은 격자에서 "최단 거리"를 구하는 문제다.
 * - BFS 는 출발점에서 거리 1 인 칸들 → 거리 2 인 칸들 → ... 순서로 퍼져 나가므로,
 *   어떤 칸에 "처음" 도착했을 때의 거리가 곧 그 칸까지의 최단 거리다.
 *   → 도착 칸을 처음 방문한 순간의 거리가 정답이다.
 * - DFS 는 한 갈래를 끝까지 파고들기 때문에 먼저 찾은 경로가 최단이라는 보장이 없다.
 *   모든 경로를 다 봐야 해서 100 x 100 맵에서는 시간 초과가 난다. (실제로 효율성 테스트에서 떨어진다)
 * - 칸은 최대 1만 개이고 BFS 는 칸마다 한 번씩만 방문하므로 충분히 빠르다.
 *
 * [풀이]
 * dist[r][c] = (0, 0) 에서 (r, c) 까지 지나간 칸 수. 0 이면 아직 방문하지 않은 칸.
 * 1) dist[0][0] = 1 로 두고 (0, 0) 을 큐에 넣는다.
 * 2) 큐에서 칸을 하나 꺼내 상하좌우 네 칸을 본다.
 *    맵 안이고, 벽(0)이 아니고, 아직 방문하지 않았으면 dist = 현재 dist + 1 로 두고 큐에 넣는다.
 * 3) 큐가 빌 때까지 반복한 뒤 dist[n-1][m-1] 을 본다. 0 이면 도착하지 못했으므로 -1.
 *
 * [예시] maps = [[1, 1, 0],
 *               [0, 1, 0],
 *               [0, 1, 1]]
 *   dist 가 채워지는 모습
 *     [1, 2, 0]
 *     [0, 3, 0]
 *     [0, 4, 5]
 *   → 답은 5
 *
 * [시간 복잡도] O(N * M)  칸마다 한 번씩 큐에 들어가고, 꺼낼 때 이웃 4 칸만 본다
 * [공간 복잡도] O(N * M)  dist 배열과 큐
 */
public class P20260918 {

    public int solution(int[][] maps) {
        // 상, 하, 좌, 우 이동
        int[] dr = {-1, 1, 0, 0};
        int[] dc = {0, 0, -1, 1};

        int n = maps.length;
        int m = maps[0].length;
        int[][] dist = new int[n][m];

        Queue<int[]> queue = new ArrayDeque<>();
        queue.offer(new int[]{0, 0});
        dist[0][0] = 1;

        while (!queue.isEmpty()) {
            int[] cur = queue.poll();
            int r = cur[0];
            int c = cur[1];

            for (int d = 0; d < 4; d++) {
                int nr = r + dr[d];
                int nc = c + dc[d];

                // 맵 밖이거나, 벽이거나, 이미 방문한 칸이면 건너뛴다
                if (nr < 0 || nr >= n || nc < 0 || nc >= m) continue;
                if (maps[nr][nc] == 0 || dist[nr][nc] != 0) continue;

                dist[nr][nc] = dist[r][c] + 1;
                queue.offer(new int[]{nr, nc});
            }
        }

        return dist[n - 1][m - 1] == 0 ? -1 : dist[n - 1][m - 1];
    }

    public static void main(String[] args) {
        P20260918 s = new P20260918();

        // 프로그래머스 입출력 예제
        check(s.solution(new int[][]{
                {1, 0, 1, 1, 1},
                {1, 0, 1, 0, 1},
                {1, 0, 1, 1, 1},
                {1, 1, 1, 0, 1},
                {0, 0, 0, 0, 1}}), 11);
        check(s.solution(new int[][]{
                {1, 0, 1, 1, 1},
                {1, 0, 1, 0, 1},
                {1, 0, 1, 1, 1},
                {1, 1, 1, 0, 0},
                {0, 0, 0, 0, 1}}), -1);


        // 성능 확인: 100 x 100 이 전부 1 → 최단 거리 = 100 + 100 - 1 = 199
        int[][] open = new int[100][100];
        for (int[] row : open) Arrays.fill(row, 1);
        long start = System.currentTimeMillis();
        int result = s.solution(open);
        long elapsed = System.currentTimeMillis() - start;
        check(result, 199);
        System.out.printf("100x100 탐색 시간=%dms%n", elapsed);
    }

    private static void check(int actual, int expected) {
        boolean ok = expected == actual;
        System.out.printf("%s expected=[%d] actual=[%d]%n", ok ? "PASS" : "FAIL", expected, actual);
    }
}

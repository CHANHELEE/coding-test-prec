import java.util.*;

/**
 * 프로그래머스 - 네트워크 (Level 3, 깊이/너비 우선 탐색(DFS/BFS))
 * https://school.programmers.co.kr/learn/courses/30/lessons/43162
 *
 * [문제]
 * 컴퓨터 n 대와 연결 정보 computers 가 주어진다. computers[i][j] == 1 이면 i 번과 j 번 컴퓨터가 직접 연결되어 있다.
 * A-B, B-C 가 연결되어 있으면 A 와 C 도 같은 네트워크다. 네트워크의 개수를 반환한다.
 * 예) n = 3, [[1, 1, 0], [1, 1, 0], [0, 0, 1]] → {0, 1}, {2} 로 네트워크 2 개
 *
 * [제한사항]
 * - 1 ≤ n ≤ 200
 * - computers[i][i] 는 항상 1
 * - computers[i][j] == computers[j][i] (연결은 양방향)
 *
 * [자료구조/알고리즘이 왜 BFS 인가]
 * - 컴퓨터를 정점, 연결을 간선으로 보면 "네트워크 개수" = 그래프의 "연결 요소(connected component) 개수"다.
 * - 아직 방문하지 않은 컴퓨터 하나에서 탐색을 시작하면, 그 컴퓨터와 이어진 컴퓨터를 전부 방문 처리하게 된다.
 *   → 탐색을 "새로 시작한 횟수"가 곧 네트워크 개수다.
 * - 연결된 것을 모두 훑기만 하면 되므로 BFS 든 DFS 든 상관없다. 여기서는 큐를 쓰는 BFS 로 풀었다.
 * - 입력이 인접 행렬이라 한 정점의 이웃을 찾을 때 n 칸을 다 봐야 한다. n ≤ 200 이면 n^2 = 4만 번이라 충분히 빠르다.
 *
 * [풀이]
 * 1) 0 번부터 n-1 번 컴퓨터까지 차례로 보면서, 방문하지 않은 컴퓨터를 만나면
 *    그 컴퓨터에서 BFS 를 돌리고 answer++
 * 2) BFS: 큐에서 컴퓨터 target 을 꺼내 computers[target] 행을 훑는다.
 *    연결(1)되어 있고 방문하지 않은 컴퓨터는 방문 처리 후 큐에 넣는다.
 *
 * [예시] n = 3, [[1, 1, 0], [1, 1, 0], [0, 0, 1]]
 *   i=0 : 미방문 → BFS(0) → 0, 1 방문 처리   answer=1
 *   i=1 : 이미 방문 → 건너뜀
 *   i=2 : 미방문 → BFS(2) → 2 방문 처리      answer=2
 *   → 답은 2
 *
 *
 * [시간 복잡도] O(N^2)  각 컴퓨터가 큐에서 한 번씩 꺼내지고, 꺼낼 때마다 행 하나(N 칸)를 훑는다
 * [공간 복잡도] O(N)    visited 배열과 큐
 */
public class P20260917 {
    public int solution(int n, int[][] computers) {
        boolean [] visited = new boolean [n];
        int answer = 0;

        for (int i = 0 ; i < n ; i++) {
            if (!visited[i]) {
                bfs(i, visited, computers, n);
                answer++;
            }
        }
        return answer;
    }

    public void bfs(int start, boolean[] visited, int[][] computers, int computerAmount) {
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(start);

        while(!queue.isEmpty()) {
            int target = queue.poll();
            int[] targetNetworkMap = computers[target];
            for (int i = 0 ; i < computerAmount ; i++) {
                if (targetNetworkMap[i] == 1 && !visited[i]) {
                    visited[i] = true;
                    queue.offer(i);
                }
            }
        }

    }

    public static void main(String[] args) {
        P20260917 s = new P20260917();

        // 프로그래머스 입출력 예제
        check(s.solution(3, new int[][]{{1, 1, 0}, {1, 1, 0}, {0, 0, 1}}), 2);
        check(s.solution(3, new int[][]{{1, 1, 0}, {1, 1, 1}, {0, 1, 1}}), 1);

        // 추가 테스트 (함정 / 경계값)
        check(s.solution(1, new int[][]{{1}}), 1);                                   // 컴퓨터 1대
        check(s.solution(3, new int[][]{{1, 0, 0}, {0, 1, 0}, {0, 0, 1}}), 3);       // 아무것도 연결 안 됨
        check(s.solution(3, new int[][]{{1, 0, 1}, {0, 1, 0}, {1, 0, 1}}), 2);       // 인접하지 않은 번호끼리 연결
        check(s.solution(4, new int[][]{{1, 0, 0, 1}, {0, 1, 1, 0}, {0, 1, 1, 1}, {1, 0, 1, 1}}), 1); // 0-3-2-1 간접 연결

        // 성능 확인: n = 200, 일렬로 연결된 체인 (0-1-2-...-199)
        int n = 200;
        int[][] chain = new int[n][n];
        for (int i = 0; i < n; i++) {
            chain[i][i] = 1;
            if (i + 1 < n) {
                chain[i][i + 1] = 1;
                chain[i + 1][i] = 1;
            }
        }
        long start = System.currentTimeMillis();
        int result = s.solution(n, chain);
        long elapsed = System.currentTimeMillis() - start;
        check(result, 1);
        System.out.printf("n=200 체인 탐색 시간=%dms%n", elapsed);
    }

    private static void check(int actual, int expected) {
        boolean ok = expected == actual;
        System.out.printf("%s expected=[%d] actual=[%d]%n", ok ? "PASS" : "FAIL", expected, actual);
    }
}

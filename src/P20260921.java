import java.util.*;

/**
 * 프로그래머스 - 여행경로 (Level 3, 깊이/너비 우선 탐색(DFS/BFS))
 * https://school.programmers.co.kr/learn/courses/30/lessons/43164
 *
 * [문제]
 * 항공권 정보 tickets 가 주어진다. tickets[i] = [a, b] 는 a 공항에서 b 공항으로 가는 항공권이 1장 있다는 뜻이다.
 * 항상 "ICN" 공항에서 출발하며, 주어진 항공권을 "모두" 사용해서 여행하는 경로를 방문 순서대로 반환한다.
 * 가능한 경로가 2개 이상이면 알파벳 순서가 앞서는 경로를 반환한다.
 *
 * [제한사항]
 * - 모든 공항은 알파벳 대문자 3글자로 이루어진다
 * - 주어진 공항 수는 3개 이상 10,000개 이하
 * - 항공권은 모두 사용해야 한다
 * - 모든 도시를 방문할 수 없는 경우는 주어지지 않는다 (답이 반드시 존재한다)
 *
 * [자료구조/알고리즘이 왜 DFS 인가]
 * - "모든 항공권을 한 번씩 사용하는 경로"를 찾는 문제다. 공항이 정점, 항공권이 간선인 그래프에서
 *   모든 간선을 한 번씩 지나는 경로(오일러 경로)를 찾는 것과 같다.
 * - 같은 공항을 여러 번 방문해도 되므로 "방문한 공항"이 아니라 "사용한 항공권"을 표시해야 한다.
 * - 한 공항에서 갈 수 있는 곳이 여러 개면, 하나를 골라 끝까지 가 보고 실패하면 되돌아와서
 *   다음 것을 고르는 백트래킹이 필요하다. → 한 갈래를 끝까지 파고드는 DFS 가 맞다.
 * - BFS 는 "경로 전체"를 상태로 들고 다녀야 해서(어떤 항공권을 썼는지 큐에 다 담아야 한다) 메모리가 크게 든다.
 *
 * [알파벳 순서를 보장하는 방법]
 * - tickets 를 미리 알파벳 순으로 정렬해 두면, DFS 가 항상 알파벳이 앞선 항공권부터 시도한다.
 * - 그래서 "모든 항공권을 다 쓴 경로"를 가장 먼저 찾은 순간, 그게 바로 알파벳 순으로 가장 앞선 답이다.
 *   → 답을 찾자마자 멈추면 되고, 여러 경로를 모아 놓고 비교할 필요가 없다.
 *
 * [풀이]
 * dfs(현재 공항, 지금까지 쓴 항공권 수) = 남은 항공권으로 경로를 완성할 수 있으면 true
 * 1) 쓴 항공권 수가 전체 장수와 같으면 경로 완성 → true
 * 2) 아니면 정렬된 tickets 를 앞에서부터 훑으며, 아직 안 쓴 항공권 중 출발지가 현재 공항인 것을 고른다.
 *    그 항공권을 사용 표시하고 도착지를 경로에 추가한 뒤 dfs 를 이어간다.
 *    true 가 돌아오면 그대로 true (더 볼 필요 없다).
 *    false 면 사용 표시와 경로 추가를 되돌리고(백트래킹) 다음 항공권을 시도한다.
 *
 * [예시] tickets = [["ICN", "SFO"], ["ICN", "ATL"], ["SFO", "ATL"], ["ATL", "ICN"], ["ATL", "SFO"]]
 *   정렬 후: [ATL,ICN] [ATL,SFO] [ICN,ATL] [ICN,SFO] [SFO,ATL]
 *   ICN 에서 알파벳이 앞선 ATL 을 먼저 시도 → ICN-ATL-ICN-SFO-ATL-SFO 로 5장을 다 쓴다 → 정답
 *   (만약 ICN-SFO 를 먼저 골랐다면 ICN-SFO-ATL-ICN-ATL-SFO 도 되지만 알파벳 순서가 뒤진다)
 * [시간 복잡도] 최악에는 지수 시간이지만, 정렬 덕분에 대부분 첫 갈래에서 답을 찾아 실제로는 O(N^2) 수준이다
 *               (한 단계마다 항공권 N 장을 훑고, 단계는 N 번이다)
 * [공간 복잡도] O(N)  used 배열, 경로, 재귀 스택
 */
public class P20260921 {

    public String[] solution(String[][] tickets) {
        // 알파벳이 앞선 항공권부터 시도하도록 미리 정렬한다 (출발지가 같으면 도착지로 비교)
        Arrays.sort(tickets, (a, b) -> a[0].equals(b[0]) ? a[1].compareTo(b[1]) : a[0].compareTo(b[0]));

        boolean[] used = new boolean[tickets.length];
        List<String> route = new ArrayList<>();
        route.add("ICN");

        dfs(tickets, used, route, "ICN", 0);

        return route.toArray(new String[0]);
    }

    private boolean dfs(String[][] tickets, boolean[] used, List<String> route, String current, int usedCount) {
        // 항공권을 모두 사용했으면 경로 완성
        if (usedCount == tickets.length) {
            return true;
        }

        for (int i = 0; i < tickets.length; i++) {
            if (used[i] || !tickets[i][0].equals(current)) continue;

            used[i] = true;
            route.add(tickets[i][1]);

            // 알파벳 순으로 먼저 찾은 경로가 답이므로 더 볼 필요 없이 끝낸다
            if (dfs(tickets, used, route, tickets[i][1], usedCount + 1)) {
                return true;
            }

            // 막다른 길이었으니 되돌린다
            used[i] = false;
            route.remove(route.size() - 1);
        }

        return false;
    }

    public static void main(String[] args) {
        P20260921 s = new P20260921();

        // 프로그래머스 입출력 예제
        check(s.solution(new String[][]{{"ICN", "JFK"}, {"HND", "IAD"}, {"JFK", "HND"}}),
                new String[]{"ICN", "JFK", "HND", "IAD"});
        check(s.solution(new String[][]{{"ICN", "SFO"}, {"ICN", "ATL"}, {"SFO", "ATL"}, {"ATL", "ICN"}, {"ATL", "SFO"}}),
                new String[]{"ICN", "ATL", "ICN", "SFO", "ATL", "SFO"});
        

        // 성능 확인: 1000 장이 한 줄로 이어진 경로 (ICN → AAA → AAB → ...)
        int n = 1000;
        String[][] chain = new String[n][2];
        String from = "ICN";
        for (int i = 0; i < n; i++) {
            String to = code(i);
            chain[i] = new String[]{from, to};
            from = to;
        }
        long start = System.currentTimeMillis();
        String[] result = s.solution(chain);
        long elapsed = System.currentTimeMillis() - start;
        check(result.length, n + 1);
        System.out.printf("항공권 %d장 탐색 시간=%dms%n", n, elapsed);
    }

    /** 0, 1, 2... 를 AAA, AAB, AAC... 같은 공항 코드로 바꾼다 (ICN 과 겹치지 않는다) */
    private static String code(int index) {
        return "" + (char) ('A' + index / 676) + (char) ('A' + (index / 26) % 26) + (char) ('A' + index % 26);
    }

    private static void check(String[] actual, String[] expected) {
        boolean ok = Arrays.equals(expected, actual);
        System.out.printf("%s expected=%s actual=%s%n", ok ? "PASS" : "FAIL",
                Arrays.toString(expected), Arrays.toString(actual));
    }

    private static void check(int actual, int expected) {
        boolean ok = expected == actual;
        System.out.printf("%s expected=[%d] actual=[%d]%n", ok ? "PASS" : "FAIL", expected, actual);
    }
}

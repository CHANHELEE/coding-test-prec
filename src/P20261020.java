import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * LeetCode 77 - Combinations (백트래킹)
 * https://leetcode.com/problems/combinations/
 * [문제]
 * 두 정수 n 과 k 가 주어질 때, 1 부터 n 까지의 수 중 k 개를 고르는 모든 조합을 반환한다.
 * 순서는 상관없다.
 * [제한사항]
 * - 1 ≤ n ≤ 20
 * - 1 ≤ k ≤ n
 *
 * [풀이]
 * 1) dfs(start) 에서 path 길이가 k 가 되면 복사해서 답에 넣고 돌아간다.
 * 2) i 를 start 부터 올려 가며 i 를 path 에 넣고 dfs(i + 1) 로 내려간다.
 * 앞에서 고른 수보다 큰 수만 고르므로 [1,2] 와 [2,1] 이 둘 다 나오지 않는다.
 * 3) 남은 수로 k 개를 채울 수 없으면 더 볼 필요가 없으므로 i 는 n - (k - path 길이) + 1 까지만 돈다.
 * 4) 돌아오면 i 를 빼서 다음 후보를 시도한다.
 * [시간 복잡도] O(k · C(n, k))  조합 C(n, k) 개를 각각 길이 k 로 복사한다
 * [공간 복잡도] O(k)  재귀 깊이와 path 길이 (답을 담는 공간은 제외)
 */
public class P20261020 {

    public List<List<Integer>> combine(int n, int k) {
        List<List<Integer>> result = new ArrayList<>();
        dfs(n, k, 1, new ArrayList<>(), result);
        return result;
    }

    private void dfs(int n, int k, int start, List<Integer> path, List<List<Integer>> result) {
        // k 개를 다 골랐으면 조합 하나 완성
        if (path.size() == k) {
            result.add(new ArrayList<>(path));
            return;
        }

        // 남은 자리를 채울 만큼 수가 남아 있는 곳까지만 고른다
        int last = n - (k - path.size()) + 1;
        for (int i = start; i <= last; i++) {
            path.add(i);
            dfs(n, k, i + 1, path, result);
            // 넣었던 수를 빼고 다음 후보를 시도한다
            path.remove(path.size() - 1);
        }
    }

    public static void main(String[] args) {
        P20261020 s = new P20261020();

        // LeetCode 예제
        check(s.combine(4, 2),
                "[[1,2],[1,3],[1,4],[2,3],[2,4],[3,4]]");
        check(s.combine(1, 1),
                "[[1]]");
    }

    /** 순서는 상관없으므로 각 조합과 전체 목록을 정렬한 문자열로 비교한다 */
    private static void check(List<List<Integer>> actual, String expected) {
        String actualStr = normalize(actual);
        String expectedStr = normalize(parse(expected));
        boolean ok = expectedStr.equals(actualStr);
        System.out.printf("%s expected=%s actual=%s%n", ok ? "PASS" : "FAIL", expectedStr, actualStr);
    }

    private static String normalize(List<List<Integer>> sets) {
        List<String> parts = new ArrayList<>();
        for (List<Integer> set : sets) {
            List<Integer> sorted = new ArrayList<>(set);
            sorted.sort(null);
            parts.add(sorted.toString());
        }
        parts.sort(null);
        return parts.toString();
    }

    /** "[[1,2],[1,3]]" 같은 문자열을 List<List<Integer>> 로 바꾼다 */
    private static List<List<Integer>> parse(String text) {
        List<List<Integer>> sets = new ArrayList<>();
        String body = text.substring(1, text.length() - 1);
        for (String part : body.split("(?<=\\]),")) {
            String inner = part.substring(1, part.length() - 1);
            List<Integer> set = new ArrayList<>();
            if (!inner.isEmpty()) {
                Arrays.stream(inner.split(",")).forEach(x -> set.add(Integer.parseInt(x)));
            }
            sets.add(set);
        }
        return sets;
    }
}

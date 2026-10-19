import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * LeetCode 78 - Subsets (백트래킹)
 * https://leetcode.com/problems/subsets/
 * [문제]
 * 서로 다른 정수로 이루어진 배열 nums 가 주어질 때, 만들 수 있는 모든 부분집합(멱집합)을 반환한다.
 * 같은 부분집합이 두 번 들어가면 안 되며, 순서는 상관없다.
 * [제한사항]
 * - 1 ≤ nums.length ≤ 10
 * - -10 ≤ nums[i] ≤ 10
 * - nums 의 원소는 모두 서로 다르다
 *
 * [풀이]
 * 1) dfs(start) 에 들어오면 지금의 path 를 복사해서 답에 넣는다. (처음엔 빈 집합)
 * 2) i 를 start 부터 끝까지 돌면서 nums[i] 를 path 에 넣고 dfs(i + 1) 로 내려간다.
 * 3) 돌아오면 nums[i] 를 빼서 다음 후보를 시도한다.
 * [시간 복잡도] O(N · 2^N)  부분집합 2^N 개를 각각 최대 N 길이로 복사한다
 * [공간 복잡도] O(N)  재귀 깊이와 path 길이 (답을 담는 공간은 제외)
 */
public class P20261019 {

    public List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        dfs(nums, 0, new ArrayList<>(), result);
        return result;
    }

    private void dfs(int[] nums, int start, List<Integer> path, List<List<Integer>> result) {
        // 재귀 트리의 노드마다 서로 다른 부분집합이므로 들어오자마자 기록한다
        result.add(new ArrayList<>(path));

        // 앞에서 고른 원소보다 뒤에 있는 것만 골라야 중복이 생기지 않는다
        for (int i = start; i < nums.length; i++) {
            path.add(nums[i]);
            dfs(nums, i + 1, path, result);
            // 넣었던 원소를 빼고 다음 후보를 시도한다
            path.remove(path.size() - 1);
        }
    }

    public static void main(String[] args) {
        P20261019 s = new P20261019();

        // LeetCode 예제
        check(s.subsets(new int[]{1, 2, 3}),
                "[[],[1],[2],[1,2],[3],[1,3],[2,3],[1,2,3]]");
        check(s.subsets(new int[]{0}),
                "[[],[0]]");

    }

    /** 순서는 상관없으므로 각 부분집합과 전체 목록을 정렬한 문자열로 비교한다 */
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

    /** "[[],[1],[1,2]]" 같은 문자열을 List<List<Integer>> 로 바꾼다 */
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

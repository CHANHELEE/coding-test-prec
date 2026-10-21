import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * LeetCode 46 - Permutations (백트래킹)
 * https://leetcode.com/problems/permutations/
 * [문제]
 * 서로 다른 정수로 이루어진 배열 nums 가 주어질 때, 만들 수 있는 모든 순열을 반환한다.
 * 순서는 상관없다.
 * [제한사항]
 * - 1 ≤ nums.length ≤ 6
 * - -10 ≤ nums[i] ≤ 10
 * - nums 의 원소는 모두 서로 다르다
 *
 * [풀이]
 * 1) dfs 에서 path 길이가 n 이 되면 복사해서 답에 넣고 돌아간다.
 * 2) 순열은 [1,2] 와 [2,1] 이 서로 다른 답이므로 start 없이 매번 처음부터 모든 원소를 후보로 본다.
 * 대신 이미 path 에 넣은 원소는 used[i] 로 표시해 두고 건너뛴다.
 * 3) nums[i] 를 넣고 used[i] 를 켠 뒤 내려가고, 돌아오면 둘 다 되돌려 다음 후보를 시도한다.
 * [시간 복잡도] O(N · N!)  순열 N! 개를 각각 길이 N 으로 복사한다
 * [공간 복잡도] O(N)  재귀 깊이, path, used (답을 담는 공간은 제외)
 */
public class P20261021 {

    public List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        dfs(nums, new boolean[nums.length], new ArrayList<>(), result);
        return result;
    }

    private void dfs(int[] nums, boolean[] used, List<Integer> path, List<List<Integer>> result) {
        // 모든 원소를 한 번씩 썼으면 순열 하나 완성
        if (path.size() == nums.length) {
            result.add(new ArrayList<>(path));
            return;
        }

        // 순서가 다르면 다른 답이므로 매번 처음부터 보되, 이미 쓴 원소만 건너뛴다
        for (int i = 0; i < nums.length; i++) {
            if (used[i]) continue;

            used[i] = true;
            path.add(nums[i]);
            dfs(nums, used, path, result);
            // 넣었던 원소를 빼고 다음 후보를 시도한다
            path.remove(path.size() - 1);
            used[i] = false;
        }
    }

    public static void main(String[] args) {
        P20261021 s = new P20261021();

        // LeetCode 예제
        check(s.permute(new int[]{1, 2, 3}),
                "[[1,2,3],[1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1]]");
        check(s.permute(new int[]{0, 1}),
                "[[0,1],[1,0]]");
        check(s.permute(new int[]{1}),
                "[[1]]");
    }

    /** 답끼리의 순서는 상관없으므로 전체 목록만 정렬한 문자열로 비교한다 (순열 안의 순서는 그대로 둔다) */
    private static void check(List<List<Integer>> actual, String expected) {
        String actualStr = normalize(actual);
        String expectedStr = normalize(parse(expected));
        boolean ok = expectedStr.equals(actualStr);
        System.out.printf("%s expected=%s actual=%s%n", ok ? "PASS" : "FAIL", expectedStr, actualStr);
    }

    private static String normalize(List<List<Integer>> lists) {
        List<String> parts = new ArrayList<>();
        for (List<Integer> list : lists) {
            parts.add(list.toString());
        }
        parts.sort(null);
        return parts.toString();
    }

    /** "[[1,2],[2,1]]" 같은 문자열을 List<List<Integer>> 로 바꾼다 */
    private static List<List<Integer>> parse(String text) {
        List<List<Integer>> lists = new ArrayList<>();
        String body = text.substring(1, text.length() - 1);
        for (String part : body.split("(?<=\\]),")) {
            String inner = part.substring(1, part.length() - 1);
            List<Integer> list = new ArrayList<>();
            if (!inner.isEmpty()) {
                Arrays.stream(inner.split(",")).forEach(x -> list.add(Integer.parseInt(x)));
            }
            lists.add(list);
        }
        return lists;
    }
}

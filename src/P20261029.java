import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * LeetCode 39 - Combination Sum (백트래킹 + 가지치기)
 * https://leetcode.com/problems/combination-sum/
 * [문제]
 * 서로 다른 정수 배열 candidates 와 정수 target 이 주어질 때, 고른 수의 합이 target 이 되는
 * 서로 다른 조합을 모두 반환한다. 같은 수는 몇 번이든 골라도 된다. 순서는 상관없다.
 * 두 조합은 적어도 한 수의 고른 횟수가 다르면 서로 다른 조합이다.
 * [제한사항]
 * - 1 ≤ candidates.length ≤ 30
 * - 2 ≤ candidates[i] ≤ 40
 * - candidates 의 모든 원소는 서로 다르다
 * - 1 ≤ target ≤ 40
 * - 답이 되는 조합은 150 개 미만이다
 *
 * [풀이]
 * 1) 답이 "모든 조합" 이므로 하나씩 만들어 가는 백트래킹으로 푼다.
 * 2) 같은 조합을 순서만 바꿔 여러 번 만들지 않도록, dfs(start, remain) 에서는 candidates[start] 부터만 고른다.
 * 같은 수를 다시 고를 수 있으므로 다음 호출에도 start 를 i 그대로 넘긴다.
 * 3) candidates 를 오름차순 정렬해 두면, candidates[i] 가 남은 합 remain 보다 클 때
 * 그 뒤의 수도 모두 크므로 반복을 바로 끝낸다. (가지치기)
 * 4) remain 이 0 이 되면 path 를 복사해 답에 넣는다.
 * [시간 복잡도] O(N^(T/M))  T = target, M = 가장 작은 수. 재귀 깊이가 최대 T/M 이고 각 단계에서 최대 N 개를 고른다
 * [공간 복잡도] O(T/M)  재귀 깊이와 path 길이 (답을 담는 공간은 제외)
 */
public class P20261029 {

    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        int[] sorted = candidates.clone();
        Arrays.sort(sorted);

        List<List<Integer>> result = new ArrayList<>();
        dfs(sorted, 0, target, new ArrayList<>(), result);
        return result;
    }

    private void dfs(int[] candidates, int start, int remain, List<Integer> path, List<List<Integer>> result) {
        // 합이 정확히 target 이 되었으면 조합 하나 완성
        if (remain == 0) {
            result.add(new ArrayList<>(path));
            return;
        }

        for (int i = start; i < candidates.length; i++) {
            // 정렬되어 있으므로 이 수가 남은 합보다 크면 뒤의 수도 전부 넘친다
            if (candidates[i] > remain) {
                break;
            }

            path.add(candidates[i]);
            // 같은 수를 또 고를 수 있으므로 i 부터 다시 시작한다
            dfs(candidates, i, remain - candidates[i], path, result);
            // 골랐던 수를 빼고 다음 수를 시도한다
            path.remove(path.size() - 1);
        }
    }

    public static void main(String[] args) {
        P20261029 s = new P20261029();

        // LeetCode 예제
        check(s.combinationSum(new int[]{2, 3, 6, 7}, 7), new int[][]{{2, 2, 3}, {7}});
        check(s.combinationSum(new int[]{2, 3, 5}, 8), new int[][]{{2, 2, 2, 2}, {2, 3, 3}, {3, 5}});
        check(s.combinationSum(new int[]{2}, 1), new int[][]{});
    }

    /** 조합끼리의 순서와 조합 안의 순서는 상관없으므로 둘 다 정렬해서 비교한다 */
    private static void check(List<List<Integer>> actual, int[][] expected) {
        List<String> actualList = new ArrayList<>();
        for (List<Integer> combo : actual) {
            int[] arr = combo.stream().mapToInt(Integer::intValue).toArray();
            Arrays.sort(arr);
            actualList.add(Arrays.toString(arr));
        }
        List<String> expectedList = new ArrayList<>();
        for (int[] combo : expected) {
            int[] arr = combo.clone();
            Arrays.sort(arr);
            expectedList.add(Arrays.toString(arr));
        }
        actualList.sort(null);
        expectedList.sort(null);
        boolean ok = actualList.equals(expectedList);
        System.out.printf("%s expected=%s actual=%s%n", ok ? "PASS" : "FAIL", expectedList, actualList);
    }
}

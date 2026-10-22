import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * LeetCode 784 - Letter Case Permutation (백트래킹)
 * https://leetcode.com/problems/letter-case-permutation/
 * [문제]
 * 문자열 s 의 각 글자를 소문자 또는 대문자로 바꿀 수 있을 때, 만들 수 있는 모든 문자열을 반환한다.
 * 숫자는 바꿀 수 없다. 순서는 상관없다.
 * [제한사항]
 * - 1 ≤ s.length ≤ 12
 * - s 는 영어 소문자, 대문자, 숫자로만 이루어져 있다
 *
 * [풀이]
 * 1) 글자를 앞에서부터 하나씩 보며, 숫자면 그대로 두고 다음 칸으로 내려간다.
 * 2) 알파벳이면 소문자로 바꿔 내려가고, 돌아오면 대문자로 바꿔 한 번 더 내려간다.
 * Subsets 의 "넣는다 / 안 넣는다" 처럼 알파벳마다 두 갈래가 생긴다.
 * 3) 끝 칸을 넘어가면 지금의 글자 배열을 문자열로 만들어 답에 넣는다.
 * 같은 char 배열을 계속 고쳐 쓰므로, 두 갈래 모두 그 칸을 직접 덮어쓰면 따로 되돌릴 필요가 없다.
 * [시간 복잡도] O(N · 2^L)  알파벳 L 개로 문자열 2^L 개가 나오고 각각 길이 N 으로 만든다
 * [공간 복잡도] O(N)  재귀 깊이와 글자 배열 (답을 담는 공간은 제외)
 */
public class P20261022 {

    public List<String> letterCasePermutation(String s) {
        List<String> result = new ArrayList<>();
        dfs(s.toCharArray(), 0, result);
        return result;
    }

    private void dfs(char[] chars, int index, List<String> result) {
        // 모든 칸을 정했으면 문자열 하나 완성
        if (index == chars.length) {
            result.add(new String(chars));
            return;
        }

        // 숫자는 바꿀 수 없으니 그대로 다음 칸으로
        if (Character.isDigit(chars[index])) {
            dfs(chars, index + 1, result);
            return;
        }

        // 알파벳은 소문자 / 대문자 두 갈래
        chars[index] = Character.toLowerCase(chars[index]);
        dfs(chars, index + 1, result);

        chars[index] = Character.toUpperCase(chars[index]);
        dfs(chars, index + 1, result);
    }

    public static void main(String[] args) {
        P20261022 s = new P20261022();

        // LeetCode 예제
        check(s.letterCasePermutation("a1b2"), new String[]{"a1b2", "a1B2", "A1b2", "A1B2"});
        check(s.letterCasePermutation("3z4"), new String[]{"3z4", "3Z4"});
    }

    /** 순서는 상관없으므로 정렬해서 비교한다 */
    private static void check(List<String> actual, String[] expected) {
        String[] actualArr = actual.toArray(new String[0]);
        String[] expectedArr = expected.clone();
        Arrays.sort(actualArr);
        Arrays.sort(expectedArr);
        boolean ok = Arrays.equals(expectedArr, actualArr);
        System.out.printf("%s expected=%s actual=%s%n", ok ? "PASS" : "FAIL",
                Arrays.toString(expectedArr), Arrays.toString(actualArr));
    }
}

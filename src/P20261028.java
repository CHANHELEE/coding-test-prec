import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * LeetCode 17 - Letter Combinations of a Phone Number (백트래킹)
 * https://leetcode.com/problems/letter-combinations-of-a-phone-number/
 * [문제]
 * 숫자 2 ~ 9 로 이루어진 문자열 digits 가 주어질 때, 전화기 자판에서 각 숫자에 적힌 글자를 하나씩 골라
 * 만들 수 있는 모든 문자열을 반환한다. 순서는 상관없다.
 * (2: abc, 3: def, 4: ghi, 5: jkl, 6: mno, 7: pqrs, 8: tuv, 9: wxyz)
 * [제한사항]
 * - 0 ≤ digits.length ≤ 4
 * - digits[i] 는 '2' ~ '9' 중 하나다
 *
 * [풀이]
 * 1) 답이 "모든 조합" 이고 그 개수가 최대 4^N 개라, 하나씩 만들어 가는 백트래킹이 가장 맞다.
 * 2) dfs(index) 에서 digits[index] 에 적힌 글자를 하나씩 path 에 붙이고 dfs(index + 1) 로 내려간다.
 * 3) 숫자를 모두 처리했으면 path 를 문자열로 만들어 답에 넣는다.
 * 4) 돌아오면 붙였던 글자를 떼고 다음 글자를 시도한다.
 * 5) digits 가 비어 있으면 만들 문자열이 없으므로 빈 목록을 반환한다.
 * [시간 복잡도] O(N · 4^N)  문자열이 최대 4^N 개 나오고 각각 길이 N 으로 만든다
 * [공간 복잡도] O(N)  재귀 깊이와 path 길이 (답을 담는 공간은 제외)
 */
public class P20261028 {

    // 숫자 -> 자판에 적힌 글자 (0, 1 에는 글자가 없다)
    private static final String[] KEYPAD = {"", "", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"};

    public List<String> letterCombinations(String digits) {
        List<String> result = new ArrayList<>();
        if (digits.isEmpty()) {
            return result;
        }

        dfs(digits, 0, new StringBuilder(), result);
        return result;
    }

    private void dfs(String digits, int index, StringBuilder path, List<String> result) {
        // 모든 숫자에서 글자를 하나씩 골랐으면 문자열 하나 완성
        if (index == digits.length()) {
            result.add(path.toString());
            return;
        }

        for (char letter : KEYPAD[digits.charAt(index) - '0'].toCharArray()) {
            path.append(letter);
            dfs(digits, index + 1, path, result);
            // 붙였던 글자를 떼고 다음 글자를 시도한다
            path.deleteCharAt(path.length() - 1);
        }
    }

    public static void main(String[] args) {
        P20261028 s = new P20261028();

        // LeetCode 예제
        check(s.letterCombinations("23"), new String[]{"ad", "ae", "af", "bd", "be", "bf", "cd", "ce", "cf"});
        check(s.letterCombinations("2"), new String[]{"a", "b", "c"});
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

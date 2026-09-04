/**
 * 프로그래머스 - 특정 문자열로 끝나는 가장 긴 부분 문자열 찾기 (Level 0)
 * https://school.programmers.co.kr/learn/courses/30/lessons/181872
 *
 * [문제]
 * 문자열 myString 의 부분 문자열 중 pat 으로 끝나는 "가장 긴" 부분 문자열을 반환한다.
 *
 * [제한사항]
 * - 5 ≤ myString 의 길이 ≤ 20
 * - 1 ≤ pat 의 길이 ≤ 5
 * - pat 은 반드시 myString 의 부분 문자열로 주어진다. (즉, 항상 답이 존재)
 * - 대소문자를 구분한다.
 *
 * [풀이]
 * 1) 부분 문자열의 "시작"은 항상 0번 인덱스로 잡는 것이 가장 길다.
 *    → 결국 정해야 할 것은 "어디서 끝낼지" 하나뿐이다.
 * 2) pat 으로 끝나야 하므로, pat 이 등장하는 위치 중 가장 뒤쪽을 골라야 가장 길어진다.
 *    → String.lastIndexOf(pat) 로 마지막 등장 위치를 찾는다.
 * 3) 자른 문자열이 pat 을 포함하며 끝나야 하므로 끝 인덱스는 (마지막 등장 위치 + pat 의 길이).
 *    substring(0, end) 는 end 를 포함하지 않으므로 그대로 넘기면 된다.
 *
 * [예시] myString = "AbCdEFG", pat = "dE"
 *   인덱스 : A(0) b(1) C(2) d(3) E(4) F(5) G(6)
 *   lastIndexOf("dE") = 3, pat.length() = 2  →  end = 5
 *   substring(0, 5) = "AbCdE"
 *
 * [예시] myString = "AAAAaaaa", pat = "a"
 *   소문자 'a' 의 마지막 위치 = 7, pat.length() = 1  →  end = 8
 *   substring(0, 8) = "AAAAaaaa"  (대소문자를 구분하므로 앞의 'A' 들은 매칭되지 않는다.)
 *
 * [시간 복잡도] O(N * M)  (N = myString 의 길이, M = pat 의 길이)
 *   lastIndexOf 는 뒤에서부터 훑으며 비교하므로 최악의 경우 N * M 이지만,
 *   N ≤ 20, M ≤ 5 이므로 사실상 상수 시간이다.
 */
public class P20260904 {

    public String solution(String myString, String pat) {
        int end = myString.lastIndexOf(pat) + pat.length();
        return myString.substring(0, end);
    }

    public static void main(String[] args) {
        P20260904 sol = new P20260904();

        // 프로그래머스 입출력 예제
        check(sol.solution("AbCdEFG", "dE"), "AbCdE");
        check(sol.solution("AAAAaaaa", "a"), "AAAAaaaa");

        // 추가 검증
        check(sol.solution("abcabcabc", "abc"), "abcabcabc"); // 여러 번 등장 → 마지막 위치 기준
        check(sol.solution("abcabcabc", "bca"), "abcabca");   // 겹쳐서 등장하는 경우
        check(sol.solution("hello", "h"), "h");               // 맨 앞에서 끝나는 경우
    }

    private static void check(String actual, String expected) {
        boolean ok = expected.equals(actual);
        System.out.printf("%s expected=[%s] actual=[%s]%n", ok ? "PASS" : "FAIL", expected, actual);
    }
}

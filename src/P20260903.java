/**
 * 프로그래머스 - 시저 암호 (Level 1)
 * https://school.programmers.co.kr/learn/courses/30/lessons/12926?language=java
 *
 * [문제]
 * 문자열 s의 각 알파벳을 n만큼 뒤로 밀어서(시저 암호) 반환한다.
 * 공백은 그대로 두고, 대문자는 대문자로 소문자는 소문자로 유지한다.
 *
 * [풀이]
 * 1) 문자를 하나씩 순회하면서 세 가지 경우로 분기한다.
 *    - 공백(' ')      : 그대로 붙인다.
 *    - 소문자(a~z)    : n만큼 더한 뒤 'z'를 넘으면 26을 빼서 'a'쪽으로 되돌린다.
 *    - 대문자(A~Z)    : n만큼 더한 뒤 'Z'를 넘으면 26을 빼서 'A'쪽으로 되돌린다.
 * 2) char 에 int 를 더하면 int 로 승격되므로 c += n (복합 대입 = 암묵적 캐스팅) 을 사용한다.
 * 3) 알파벳은 26자이므로 한 번 밀어서 범위를 벗어나 봐야 최대 26을 넘지 않는다.
 *    (n은 1 이상 25 이하이므로 26을 한 번만 빼면 항상 범위 안으로 들어온다.)
 *
 * [ASCII 코드 참고]
 *   ' ' (공백) = 32
 *   'A' = 65 ~ 'Z' = 90   (대문자 26자)
 *   'a' = 97 ~ 'z' = 122  (소문자 26자)
 *   소문자 = 대문자 + 32  (예: 'a'(97) = 'A'(65) + 32)
 *   → 'z'(122)를 넘어가면 26을 빼서 'a'(97)부터 다시 시작하게 만든다.
 *     예) 'z'(122) + 1 = 123 → 123 - 26 = 97 = 'a'
 *
 * [시간 복잡도] O(N)  (N = s의 길이)
 *
 */
public class P20260903 {

    public String solution(String s, int n) {
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            if (c == ' ') {
                sb.append(c);
            } else if (Character.isLowerCase(c)) {
                c += n;
                if (c > 'z') c -= 26;
                sb.append(c);
            } else {
                c += n;
                if (c > 'Z') c -= 26;
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        P20260903 sol = new P20260903();

        // 프로그래머스 입출력 예제
        check(sol.solution("AB", 1), "BC");
        check(sol.solution("z", 1), "a");
        check(sol.solution("a B z", 4), "e F d");
    }

    private static void check(String actual, String expected) {
        boolean ok = expected.equals(actual);
        System.out.printf("%s expected=[%s] actual=[%s]%n", ok ? "PASS" : "FAIL", expected, actual);
    }
}

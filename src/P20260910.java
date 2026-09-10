import java.util.ArrayDeque;
import java.util.Deque;

/**
 * 프로그래머스 - 짝지어 제거하기 (Level 2, 스택 / 2017 팁스타운)
 * https://school.programmers.co.kr/learn/courses/30/lessons/12973
 *
 * [문제]
 * 문자열에서 "같은 알파벳이 2개 붙어 있는 짝"을 찾아 제거하고 앞뒤를 이어 붙인다.
 * 이 작업을 반복해서 문자열을 완전히 비울 수 있으면 1, 아니면 0 을 반환한다.
 *
 * [제한사항]
 * - 문자열의 길이 ≤ 1,000,000
 * - 모두 소문자 알파벳
 *
 * [먼저 떠오르는(하지만 틀린) 풀이]
 * "aa" 같은 짝을 찾아 replace 로 지우고, 더 이상 지울 게 없을 때까지 반복하는 방법.
 * 한 번 지울 때마다 문자열 전체를 다시 훑고 새로 만들어야 하므로 O(N²) 이고,
 * N 이 100만이면 10¹² 번 → 시간 초과. 제한사항의 100만이 곧 "완전탐색 하지 말라"는 신호다.
 *
 * [자료구조가 왜 스택인가]
 * 짝을 지우면 "지운 자리의 앞 글자와 뒤 글자"가 새로 맞닿는다.  예) ba|aa|a  →  ba + a
 * 즉 방금 확정한 글자하고만 비교하면 되고, 지워지면 그 이전 글자로 되돌아간다 → LIFO.
 * 왼쪽부터 한 글자씩 보면서
 *   - 스택의 top 과 같으면  → 짝이 성립하므로 pop (두 글자가 함께 사라짐)
 *   - 다르면(또는 비었으면) → push (아직 짝을 못 찾은 글자로 보류)
 * 를 하면 한 번의 순회로 끝난다. 마지막에 스택이 비어 있으면 전부 지워진 것.
 *
 * [예시] s = "baabaa"
 *   b : 스택 비었음        → push        [b]
 *   a : top(b) != a        → push        [b, a]
 *   a : top(a) == a        → pop         [b]
 *   b : top(b) == b        → pop         []
 *   a : 스택 비었음        → push        [a]
 *   a : top(a) == a        → pop         []
 *   → 스택이 비었으므로 1
 *
 * [예시] s = "cdcd"
 *   c, d, c, d 모두 top 과 달라서 계속 push → [c, d, c, d] 가 남으므로 0
 *
 * [자바 참고: ArrayDeque 로 스택 쓰기]
 * - 어제 인형뽑기(P20260909)와 마찬가지로 java.util.Stack 이 아니라 ArrayDeque 를 쓴다.
 *   Stack 은 Vector 를 상속한 레거시 클래스이고 Javadoc 자체가 Deque 를 권고한다.
 *   ArrayDeque 는 push / pop / peek / isEmpty 를 그대로 제공한다.
 * - new ArrayDeque<>(s.length()) 처럼 예상 크기를 미리 넘기면 내부 배열을 키우며
 *   복사하는 일(resize)을 피할 수 있다. 스택에 쌓이는 글자는 절대 입력 길이를 넘지 않는다.
 * - stack.peek() == c 는 한쪽이 char 라서 Character 가 auto-unboxing 되어 값 비교가 된다.
 *   양쪽 다 Character 였다면 참조 비교가 되어 위험하다. (캐시 범위는 0~127 뿐)
 * - 순서를 반드시 isEmpty() 먼저, peek() 나중으로 둔다. 스택이 비면 peek() 은 null 을 주는데
 *   그걸 char 로 언박싱하는 순간 NullPointerException 이 난다.
 * - 단점도 알고 쓰자. char 를 담으려면 Character 로 박싱되므로 100만 개면 래퍼 참조를 그만큼
 *   따라가야 한다. 아래 main 의 벤치마크에서 char[] 직접 구현과 비교해 두었다.
 *   (물론 둘 다 O(N) 이라 이 문제의 제한시간에는 여유롭게 통과한다.)
 *
 * [먼저 걸러낼 수 있는 경우]
 * 한 번 지울 때마다 길이가 2씩 줄어드므로, 길이가 홀수면 절대 0 이 될 수 없다.
 * 순회하지 않고 바로 0 을 반환할 수 있다. (없어도 정답이지만 있으면 깔끔하다)
 *
 * [시간 복잡도] O(N)  각 글자는 최대 한 번 push, 한 번 pop 된다.
 * [공간 복잡도] O(N)  최악의 경우(예: "abcdef...") 모든 글자가 스택에 쌓인다.
 */
public class P20260910 {

    public int solution(String s) {
        // 길이가 홀수면 2개씩 아무리 지워도 한 글자가 남는다
        if (s.length() % 2 != 0) {
            return 0;
        }

        Deque<Character> stack = new ArrayDeque<>(s.length()); // 최대 크기를 미리 잡아 resize 방지

        for (char c : s.toCharArray()) {
            if (!stack.isEmpty() && stack.peek() == c) {
                stack.pop();    // 짝이 맞으므로 두 글자 제거
            } else {
                stack.push(c);  // 짝을 기다리며 보류
            }
        }
        return stack.isEmpty() ? 1 : 0;
    }

    /**
     * [참고] 배열로 스택을 직접 만든 버전.
     * 쌓이는 글자 수가 입력 길이를 넘지 않는다는 걸 알고 있으므로
     * char[] 하나와 top 인덱스(= 현재 쌓인 개수) 하나면 스택이 된다.
     *   push : stack[top++] = c
     *   peek : stack[top - 1]
     *   pop  : top--   (값을 지울 필요 없이 인덱스만 줄이면 논리적으로 사라진다)
     * 박싱이 없어 더 빠르지만, 크기가 고정이라 상한을 모르면 쓸 수 없다.
     * CLAUDE 추천 방식.
     */
    public int solutionByArray(String s) {
        if (s.length() % 2 != 0) {
            return 0;
        }

        char[] stack = new char[s.length()];
        int top = 0;

        for (char c : s.toCharArray()) {
            if (top > 0 && stack[top - 1] == c) {
                top--;
            } else {
                stack[top++] = c;
            }
        }
        return top == 0 ? 1 : 0;
    }

    public static void main(String[] args) {
        P20260910 sol = new P20260910();

        // 프로그래머스 입출력 예제
        check(sol.solution("baabaa"), 1);
        check(sol.solution("cdcd"), 0);

        // 추가 테스트 (경계값)
        check(sol.solution("aa"), 1);        // 최소 성공 케이스
        check(sol.solution("a"), 0);         // 홀수 길이는 무조건 실패
        check(sol.solution("abba"), 1);      // 안쪽부터 지워지며 바깥이 맞닿는 경우
        check(sol.solution("abab"), 0);      // 길이는 짝수지만 짝이 안 맞는 경우
        check(sol.solution("aabbaabb"), 1);  // 여러 짝이 나란히 있는 경우
        check(sol.solution("baabaab"), 0);   // 마지막에 한 글자가 남는 경우

        // 두 구현이 같은 답을 내는지 확인
        for (String t : new String[]{"baabaa", "cdcd", "aa", "a", "abba", "abab", "aabbaabb", "baabaab"}) {
            check(sol.solutionByArray(t), sol.solution(t));
        }

        // 성능 확인: 100만 글자
        //  - "abab...ab" 는 하나도 안 지워져 스택이 최대로 쌓이는 최악의 공간 케이스
        //  - "aabb...", 즉 두 글자씩 짝지어진 입력은 계속 pop 되는 케이스
        int n = 1_000_000;
        StringBuilder worst = new StringBuilder(n);
        StringBuilder best = new StringBuilder(n);
        for (int i = 0; i < n / 2; i++) {
            worst.append("ab");
            best.append(i % 2 == 0 ? "aa" : "bb");
        }
        String worstCase = worst.toString();
        String bestCase = best.toString();

        long start = System.currentTimeMillis();
        check(sol.solution(worstCase), 0);
        check(sol.solution(bestCase), 1);
        long deque = System.currentTimeMillis() - start;

        start = System.currentTimeMillis();
        check(sol.solutionByArray(worstCase), 0);
        check(sol.solutionByArray(bestCase), 1);
        long array = System.currentTimeMillis() - start;

        System.out.printf("100만 글자 2건 처리 시간 : ArrayDeque=%dms, char[]=%dms%n", deque, array);
    }

    private static void check(int actual, int expected) {
        boolean ok = expected == actual;
        System.out.printf("%s expected=[%d] actual=[%d]%n", ok ? "PASS" : "FAIL", expected, actual);
    }
}

import java.util.HashMap;
import java.util.Map;

/**
 * 프로그래머스 - 완주하지 못한 선수 (Level 1, 해시)
 * https://school.programmers.co.kr/learn/courses/30/lessons/42576
 *
 * [문제]
 * 마라톤에 참여한 선수 이름 배열 participant 와 완주한 선수 이름 배열 completion 이 주어질 때,
 * 완주하지 못한 선수 한 명의 이름을 반환한다.
 *
 * [제한사항]
 * - 1 ≤ participant 의 길이 ≤ 100,000
 * - completion 의 길이 = participant 의 길이 - 1  (완주 못 한 사람은 정확히 한 명)
 * - 1 ≤ 이름 길이 ≤ 20, 알파벳 소문자로만 구성
 * - 참가자 중에는 동명이인이 있을 수 있다.  ← 이 문제의 핵심 함정
 *
 * [풀이]  HashMap 으로 "이름별 개수"를 세는 방식
 * 1) completion 을 순회하며 이름별 등장 횟수를 Map 에 담는다.  (완주자 카운트)
 * 2) participant 를 순회하며 Map 에서 카운트를 1씩 깎는다.
 *    - Map 에 없거나(카운트 0) 이미 다 깎인 이름이 나오면 그 사람이 완주하지 못한 선수다.
 * 3) 문제 조건상 답은 반드시 존재하므로 마지막 return 은 도달하지 않는다.
 *
 * [왜 Set 이나 List.remove 가 아니라 카운트인가]
 * - Set 을 쓰면 동명이인("eden", "eden")을 구분하지 못한다.
 *   3명 중 "eden" 2명이 참가하고 1명만 완주하면 답은 "eden" 인데, Set 은 이미 포함되어 있다고 판단한다.
 * - List.remove(Object) 는 동명이인을 한 명씩 지워주긴 하지만 매번 O(N) 탐색이라
 *   전체 O(N²) → 10만 건에서 시간 초과가 난다.
 * - HashMap 카운팅은 조회/수정이 평균 O(1) 이라 전체 O(N) 으로 끝난다.
 *
 *
 * [시간 복잡도] O(N)      (N = participant 의 길이)
 * [공간 복잡도] O(N)      (완주자 이름을 담는 Map)
 *
 */
public class P20260908 {

    public String solution(String[] participant, String[] completion) {
        Map<String, Integer> count = new HashMap<>();

        // 1) 완주자 이름별 인원 수 세기
        for (String name : completion) {
            count.merge(name, 1, Integer::sum);
        }

        // 2) 참가자에서 완주자를 한 명씩 상쇄 → 남는 사람이 답
        for (String name : participant) {
            int remain = count.getOrDefault(name, 0);
            if (remain == 0) {
                return name;
            }
            count.put(name, remain - 1);
        }

        // 3) 문제 조건상 도달하지 않음
        return "";
    }

    public static void main(String[] args) {
        P20260908 sol = new P20260908();

        // 프로그래머스 입출력 예제
        check(sol.solution(
                new String[]{"leo", "kiki", "eden"},
                new String[]{"eden", "kiki"}), "leo");
        check(sol.solution(
                new String[]{"marina", "josipa", "nikola", "vinko", "filipa"},
                new String[]{"josipa", "filipa", "marina", "nikola"}), "vinko");
        check(sol.solution(
                new String[]{"mislav", "stanko", "mislav", "ana"},
                new String[]{"stanko", "ana", "mislav"}), "mislav"); // 동명이인 케이스
    }

    private static void check(String actual, String expected) {
        boolean ok = expected.equals(actual);
        System.out.printf("%s expected=[%s] actual=[%s]%n", ok ? "PASS" : "FAIL", expected, actual);
    }
}

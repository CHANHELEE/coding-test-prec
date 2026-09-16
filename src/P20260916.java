import java.util.Arrays;

/**
 * 프로그래머스 - 타겟 넘버 (Level 2, 깊이/너비 우선 탐색(DFS/BFS))
 * https://school.programmers.co.kr/learn/courses/30/lessons/43165
 *
 * [문제]
 * 음이 아닌 정수들이 주어진다. 각 숫자 앞에 + 또는 - 를 붙여 순서대로 더했을 때
 * 결과가 target 이 되는 경우의 수를 반환한다.
 * 예) [1, 1, 1, 1, 1] 로 3 을 만드는 방법은 5 가지다.
 *   -1+1+1+1+1 = 3,  +1-1+1+1+1 = 3,  +1+1-1+1+1 = 3,  +1+1+1-1+1 = 3,  +1+1+1+1-1 = 3
 *
 * [제한사항]
 * - 2 ≤ numbers 의 길이 ≤ 20
 * - 1 ≤ numbers 의 원소 ≤ 50
 * - 1 ≤ target ≤ 1000
 *
 * [자료구조/알고리즘이 왜 DFS 인가]
 * - 숫자 하나마다 선택지가 "+" 와 "-" 딱 두 개뿐이고, 앞 숫자의 선택이 뒤 숫자의 선택지를 제한하지 않는다.
 *   → 높이가 N, 각 노드의 자식이 2개인 이진 트리가 되고, 잎(leaf) 하나가 부호 조합 하나에 대응한다.
 * - 잎에 도착했을 때(모든 숫자를 다 쓴 순간) 합이 target 인지 보면 되므로,
 *   한 갈래를 끝까지 내려갔다가 돌아오는 DFS(재귀)가 가장 자연스럽다.
 * - 경우의 수를 "세는" 문제라서 최단 거리를 찾는 BFS 의 장점이 필요 없다. BFS 로도 풀리지만
 *   각 단계의 중간 합들을 큐에 모두 담아야 해서 메모리를 더 쓴다.
 * - 잎의 개수는 2^20 = 약 100만 개라서 모든 경우를 다 돌아도 충분히 빠르다.
 *   (제한사항의 "20" 이 곧 "완전탐색해도 된다"는 신호다)
 *
 * [풀이]
 * dfs(index, currentSum) = index 번째 숫자부터 부호를 정할 차례이고, 지금까지의 합이 currentSum
 * 1) index 가 numbers.length 면 모든 숫자를 다 썼다는 뜻 → currentSum == target 이면 count++
 * 2) 아니면 현재 숫자를 더한 경우와 뺀 경우로 각각 내려간다.
 *      dfs(index + 1, currentSum + numbers[index])
 *      dfs(index + 1, currentSum - numbers[index])
 *
 * [예시] numbers = [1, 1], target = 0  (트리를 그려보면)
 *                    (0,합0)
 *              +1 /          \ -1
 *            (1,합1)        (1,합-1)
 *          +1/    \-1     +1/     \-1
 *      (2,합2) (2,합0) (2,합0)  (2,합-2)
 *   잎 4개 중 합이 0 인 것이 2개 → 답은 2
 *
 * [주의할 점]
 * 1) 재귀를 끝내는 조건은 "합이 target 이 되는 순간"이 아니라 "숫자를 전부 쓴 순간"이다.
 *    중간에 합이 target 이 되어도 남은 숫자를 반드시 써야 하므로 멈추면 안 된다.
 *    예) [1, 2, 2], target = 1 → 첫 숫자에서 이미 1 이지만 뒤의 2, 2 를 +2-2 로 처리해야 완성된다.
 * 2) 합이 음수로도 내려가므로 currentSum 을 배열 인덱스처럼 쓰려 하면 안 된다.
 *    (최소 -1000, 최대 1000 까지 나올 수 있다)
 *
 * [시간 복잡도] O(2^N)  잎이 2^N 개 (N = 20 이면 약 100만)
 * [공간 복잡도] O(N)    재귀 호출 스택의 깊이만큼만 쓴다
 */
public class P20260916 {

    private int count = 0;

    public int solution(int[] numbers, int target) {
        dfs(numbers, target, 0, 0);
        return count;
    }

    private void dfs(int[] numbers, int target, int index, int currentSum) {
        // 모든 숫자를 사용했을 때
        if (index == numbers.length) {
            if (currentSum == target) {
                count++;
            }
            return;
        }

        // 현재 숫자를 더하는 경우
        dfs(numbers, target, index + 1, currentSum + numbers[index]);
        // 현재 숫자를 빼는 경우
        dfs(numbers, target, index + 1, currentSum - numbers[index]);
    }

    public static void main(String[] args) {
        // 프로그래머스 입출력 예제
        check(run(new int[]{1, 1, 1, 1, 1}, 3), 5);
        check(run(new int[]{4, 1, 2, 1}, 4), 2);

        // 추가 테스트 (함정 / 경계값)
        check(run(new int[]{1, 1}, 2), 1);        // +1+1 하나뿐
        check(run(new int[]{1, 1}, 0), 2);        // +1-1, -1+1 두 가지
        check(run(new int[]{1, 1}, 5), 0);        // 만들 수 없는 경우
        check(run(new int[]{1, 2, 2}, 1), 2);     // 중간에 target 이 되어도 끝까지 써야 한다
        check(run(new int[]{50, 50}, 100), 1);    // 원소 최댓값
        check(run(new int[]{1, 1, 1}, 2), 0);     // 홀수 개의 홀수 → 합의 홀짝이 안 맞아 0

        // 성능 확인: 최대 길이 20 → 잎 2^20 = 1,048,576 개
        int[] worst = new int[20];
        Arrays.fill(worst, 1);
        long start = System.currentTimeMillis();
        int result = run(worst, 0);
        long elapsed = System.currentTimeMillis() - start;
        // 20개 중 +가 10개, -가 10개인 경우의 수 = C(20,10) = 184,756
        check(result, 184_756);
        System.out.printf("길이 20 (2^20 가지) 탐색 시간=%dms%n", elapsed);
    }

    /** count 가 인스턴스 필드라서 누적되지 않도록, 채점 서버처럼 호출마다 새 객체를 쓴다. */
    private static int run(int[] numbers, int target) {
        return new P20260916().solution(numbers, target);
    }

    private static void check(int actual, int expected) {
        boolean ok = expected == actual;
        System.out.printf("%s expected=[%d] actual=[%d]%n", ok ? "PASS" : "FAIL", expected, actual);
    }
}

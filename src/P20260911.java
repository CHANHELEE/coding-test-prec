import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

/**
 * 프로그래머스 - 기능개발 (Level 2, 스택/큐)
 * https://school.programmers.co.kr/learn/courses/30/lessons/42586
 *
 * [문제]
 * 기능마다 현재 진도(progresses)와 하루 개발 속도(speeds)가 주어진다. 진도가 100%가 되면 배포할 수 있지만,
 * 앞 기능이 배포될 때 함께 배포되어야 한다. (뒤 기능이 먼저 끝나도 앞 기능을 기다린다)
 * 각 배포마다 몇 개의 기능이 배포되는지 배열로 반환한다.
 *
 * [제한사항]
 * - 1 ≤ 작업 개수 ≤ 100
 * - 1 ≤ 작업 진도 < 100 (자연수), 1 ≤ 작업 속도 ≤ 100 (자연수)
 * - 배포는 하루에 한 번, 하루의 끝에 이루어진다.  → 남은 일수는 "올림"으로 계산해야 한다.
 *
 * [자료구조가 왜 큐인가]
 * - 배포는 반드시 "앞에서부터" 순서대로 나간다 → FIFO.
 * - 맨 앞 기능이 나갈 때, 그 뒤에서 이미 끝나 있는 기능들을 앞에서부터 같이 빼면 된다.
 *
 * [풀이]
 * 1) 기능별로 "완료까지 걸리는 일수"를 계산해 큐에 넣는다.
 *    남은 진도 = 100 - progress,  일수 = ceil(남은 진도 / speed)
 * 2) 큐에서 맨 앞을 꺼내 이번 배포일(deployDay)로 삼는다. count = 1
 * 3) 큐의 맨 앞이 deployDay 이하인 동안 계속 꺼내며 count++.
 *    (이미 끝났지만 앞 기능을 기다리던 애들)
 * 4) deployDay 보다 오래 걸리는 기능을 만나면 이번 배포 종료 → count 를 결과에 추가하고 2)로.
 *
 * [예시] progresses = [95, 90, 99, 99, 80, 99], speeds = [1, 1, 1, 1, 1, 1]
 *   일수 = [5, 10, 1, 1, 20, 1]
 *   5 배포          → [1]
 *   10 배포, 1, 1 동반 → [1, 3]
 *   20 배포, 1 동반    → [1, 3, 2]
 *
 * [주의할 점]
 * 1) 올림 계산: (100 - p) / s 는 정수 나눗셈이라 버림이 된다.
 *    예) p = 93, s = 30 → 7 / 30 = 0 (틀림, 실제로는 1일 필요)
 *    → (a + b - 1) / b 로 정수 올림을 한다. Math.ceil((double) a / b) 도 되지만 형변환이 번거롭다.
 * 2) 비교 기준은 "직전 기능"이 아니라 "이번 배포를 시작한 맨 앞 기능(deployDay)"이다.
 *    예) 일수 = [7, 3, 5] → 3, 5 모두 7일째에 같이 나가므로 답은 [3].
 *        직전 값과 비교하면 5 > 3 이라서 [2, 1] 로 잘못 끊긴다.
 *
 * [다른 풀이 - 큐 없이 한 번 순회]
 *   일수 배열을 앞에서부터 보면서 현재 배포일보다 큰 값이 나올 때만 끊어도 된다. 로직은 완전히 같다.
 *
 * [자바 참고]
 * - 큐 용도로도 ArrayDeque 를 쓴다. offer(뒤에 넣기) / poll(앞에서 빼기) / peek(앞 확인).
 * - 결과 개수를 미리 알 수 없으므로 List 에 모은 뒤 stream().mapToInt(Integer::intValue).toArray() 로
 *   int[] 로 변환한다.
 *
 * [시간 복잡도] O(N)   (N = 작업 개수, 각 기능은 큐에 한 번 들어가고 한 번 나온다)
 * [공간 복잡도] O(N)
 *
 */
public class P20260911 {

    public int[] solution(int[] progresses, int[] speeds) {
        // 1) 기능별 완료까지 걸리는 일수 (정수 올림)
        Queue<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < progresses.length; i++) {
            int remain = 100 - progresses[i];
            int days = (remain + speeds[i] - 1) / speeds[i];
            queue.offer(days);
        }

        List<Integer> answer = new ArrayList<>();
        while (!queue.isEmpty()) {
            // 2) 맨 앞 기능이 끝나는 날이 이번 배포일
            int deployDay = queue.poll();
            int count = 1;

            // 3) 이미 끝나서 기다리던 뒤 기능들을 같이 배포
            while (!queue.isEmpty() && queue.peek() <= deployDay) {
                queue.poll();
                count++;
            }

            // 4) 이번 배포 종료
            answer.add(count);
        }

        return answer.stream().mapToInt(Integer::intValue).toArray();
    }

    public static void main(String[] args) {
        P20260911 sol = new P20260911();

        // 프로그래머스 입출력 예제
        check(sol.solution(new int[]{93, 30, 55}, new int[]{1, 30, 5}), new int[]{2, 1});
        check(sol.solution(new int[]{95, 90, 99, 99, 80, 99}, new int[]{1, 1, 1, 1, 1, 1}), new int[]{1, 3, 2});

        // 추가 테스트 (함정 / 경계값)
        check(sol.solution(new int[]{93, 97, 95}, new int[]{1, 1, 1}), new int[]{3});       // 일수 [7,3,5] → 직전 값이 아니라 맨 앞과 비교
        check(sol.solution(new int[]{93}, new int[]{30}), new int[]{1});                    // 올림 필요 (7/30 → 1일)
        check(sol.solution(new int[]{10, 20, 30}, new int[]{1, 1, 1}), new int[]{3});       // 일수 [90,80,70] → 맨 앞이 제일 늦어서 전부 같이
        check(sol.solution(new int[]{90, 50, 10}, new int[]{1, 1, 1}), new int[]{1, 1, 1}); // 일수 [10,50,90] → 매번 끊김
        check(sol.solution(new int[]{99, 99, 99}, new int[]{1, 1, 1}), new int[]{3});       // 전부 같은 날
    }

    private static void check(int[] actual, int[] expected) {
        boolean ok = Arrays.equals(expected, actual);
        System.out.printf("%s expected=%s actual=%s%n", ok ? "PASS" : "FAIL",
                Arrays.toString(expected), Arrays.toString(actual));
    }
}

import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Random;

/**
 * 프로그래머스 - 더 맵게 (Level 2, 힙)
 * https://school.programmers.co.kr/learn/courses/30/lessons/42626
 *
 * [문제]
 * 모든 음식의 스코빌 지수를 K 이상으로 만들고 싶다. 스코빌 지수가 가장 낮은 두 음식을 골라
 *   섞은 음식의 스코빌 지수 = 가장 맵지 않은 음식 + (두 번째로 맵지 않은 음식 × 2)
 * 로 하나로 합친다. 모든 음식이 K 이상이 될 때까지 반복할 때 섞어야 하는 최소 횟수를 반환한다.
 * 모든 음식을 K 이상으로 만들 수 없으면 -1 을 반환한다.
 *
 * [제한사항]
 * - 2 ≤ scoville 의 길이 ≤ 1,000,000
 * - 0 ≤ K ≤ 1,000,000,000
 * - 0 ≤ scoville 의 원소 ≤ 1,000,000
 *
 * [먼저 떠오르는(하지만 틀린) 풀이]
 * 매번 배열을 정렬해서 앞의 두 개를 꺼내 섞는 방법.
 * 섞을 때마다 정렬(N log N)을 다시 하므로 최악 O(N² log N) 이고, N 이 100만이면 시간 초과.
 * 필요한 건 "전체 정렬 상태"가 아니라 "지금 가장 작은 값 두 개"뿐이다.
 *
 * [자료구조가 왜 힙인가]
 * - 매번 "가장 작은 값"을 꺼내고, 새로 만든 값을 다시 넣어야 한다.
 * - 새 값이 어디에 들어갈지 모르므로 정렬된 배열/큐로는 삽입이 O(N) 이다.
 * - 최소 힙은 꺼내기(poll)와 넣기(offer)가 모두 O(log N) 이다. → 딱 이 문제를 위한 자료구조.
 *
 * [풀이]
 * 1) 모든 스코빌 지수를 최소 힙에 넣는다.
 * 2) 힙의 최솟값(peek)이 K 이상이면 나머지도 전부 K 이상이므로 종료.
 * 3) 아니면 두 개를 꺼내 섞은 값을 다시 넣고 count++.
 *    이때 꺼낼 게 하나밖에 없다면(힙 크기 1) 더 이상 섞을 수 없으므로 -1.
 *
 * [예시] scoville = [1, 2, 3, 9, 10, 12], K = 7
 *   1 + 2×2 = 5   → [3, 5, 9, 10, 12]   count = 1
 *   3 + 5×2 = 13  → [9, 10, 12, 13]     count = 2
 *   최솟값 9 ≥ 7 이므로 답은 2
 *
 * [주의할 점]
 * 1) -1 판정: "힙에 원소가 하나만 남았는데 그게 K 미만"일 때다.
 *    예) [1, 1], K = 10 → 1 + 1×2 = 3 하나만 남고 3 < 10 이므로 -1.
 *    두 번째 poll() 전에 크기를 확인하지 않으면 null 이 나와 언박싱에서 NullPointerException.
 * 2) 이미 모든 음식이 K 이상이면 한 번도 섞지 않으므로 0. (K = 0 이면 항상 0)
 * 3) int 오버플로우: 섞기 직전 두 값은 K 미만일 수 있으므로 결과는 최대 약 3K = 30억 → int 범위(약 21억) 초과.
 *    예) 100만짜리 음식 128개, K = 10억
 *        100만 → 300만 → 900만 → 2700만 → 8100만 → 2.43억 → 7.29억 (2개 남음, 아직 K 미만)
 *        7.29억 + 7.29억×2 = 21.87억 → int 로 계산하면 음수가 되어 -1 을 잘못 반환한다.
 *    → 계산은 long 으로 하고, K 이상인 값은 정확한 크기가 필요 없으므로 K 로 잘라서 int 로 저장한다.
 *      (K 이상인 음식은 "가장 작은 값"으로 뽑힐 일이 없고, 두 번째 값으로 뽑혀도 결과는 어차피 K 이상이다.)
 *    프로그래머스 테스트케이스에는 이런 입력이 없어서 int 로도 통과하지만, 제한사항상 가능한 입력이다.
 *
 * [자바 참고]
 * - PriorityQueue 는 기본이 최소 힙이다. 최대 힙이 필요하면 new PriorityQueue<>(Comparator.reverseOrder()).
 * - offer(넣기) / poll(최솟값 꺼내기) / peek(최솟값 확인) 모두 비었으면 null 을 준다.
 * - new PriorityQueue<>(초기 용량) 으로 크기를 미리 잡아 resize 를 피한다.
 * - 원소를 하나씩 offer 하면 O(N log N) 이다. PriorityQueue(Collection) 생성자는 한 번에 heapify 해서 O(N) 이지만,
 *   int[] 를 List<Integer> 로 바꾸는 과정이 추가되어 이 문제에선 체감 차이가 거의 없다.
 *
 * [시간 복잡도] O(N log N)  힙 구성 N log N + 섞기 최대 N-1 번 × log N
 * [공간 복잡도] O(N)
 */
public class P20260915 {

    public int solution(int[] scoville, int K) {
        // 1) 최소 힙 구성
        PriorityQueue<Integer> heap = new PriorityQueue<>(scoville.length);
        for (int s : scoville) {
            heap.offer(s);
        }

        int count = 0;
        // 2) 가장 맵지 않은 음식이 K 이상이 될 때까지 반복
        while (heap.peek() < K) {
            // 섞을 짝이 없으면 불가능
            if (heap.size() < 2) {
                return -1;
            }

            // 3) 가장 작은 두 개를 섞어서 다시 넣는다
            int first = heap.poll();
            int second = heap.poll();
            long mixed = first + 2L * second;          // int 오버플로우 방지
            heap.offer((int) Math.min(mixed, K));      // K 이상이면 정확한 값은 필요 없다
            count++;
        }
        return count;
    }

    public static void main(String[] args) {
        P20260915 sol = new P20260915();

        // 프로그래머스 입출력 예제
        check(sol.solution(new int[]{1, 2, 3, 9, 10, 12}, 7), 2);

        // 추가 테스트 (함정 / 경계값)
        check(sol.solution(new int[]{7, 8, 9}, 7), 0);      // 이미 전부 K 이상
        check(sol.solution(new int[]{0, 0}, 0), 0);         // K = 0 이면 항상 0
        check(sol.solution(new int[]{1, 2}, 5), 1);         // 1 + 2×2 = 5, 정확히 K 에 도달
        check(sol.solution(new int[]{1, 1}, 10), -1);       // 섞어도 3 → 하나 남았는데 K 미만
        check(sol.solution(new int[]{0, 0, 0}, 1), -1);     // 0 끼리는 아무리 섞어도 0
        check(sol.solution(new int[]{0, 1}, 2), 1);         // 0 + 1×2 = 2
        check(sol.solution(new int[]{10, 1, 1}, 5), 2);     // 1,1 → 3 → 3,10 → 23 (정렬 안 된 입력)

        // int 오버플로우 함정: 100만 × 128개, K = 10억 → 127번 섞어야 한다
        int[] overflow = new int[128];
        Arrays.fill(overflow, 1_000_000);
        check(sol.solution(overflow, 1_000_000_000), 127);

        // 성능 확인: 최대 길이 100만
        //  - 전부 0 이 아닌 작은 값이면 거의 N-1 번을 섞어야 하는 최악 케이스
        int n = 1_000_000;
        int[] worst = new int[n];
        Random random = new Random(42);
        for (int i = 0; i < n; i++) {
            worst[i] = random.nextInt(10) + 1;
        }
        long start = System.currentTimeMillis();
        int result = sol.solution(worst, 1_000_000_000);
        long elapsed = System.currentTimeMillis() - start;
        System.out.printf("100만 개 처리 결과=%d, 시간=%dms%n", result, elapsed);
    }

    private static void check(int actual, int expected) {
        boolean ok = expected == actual;
        System.out.printf("%s expected=[%d] actual=[%d]%n", ok ? "PASS" : "FAIL", expected, actual);
    }
}

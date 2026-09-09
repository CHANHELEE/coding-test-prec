import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * 프로그래머스 - 크레인 인형뽑기 게임 (Level 1, 스택)
 * https://school.programmers.co.kr/learn/courses/30/lessons/64061
 *
 * [문제]
 * N x N 격자(board)에 인형이 쌓여 있고, 크레인이 moves 순서대로 해당 열의 "가장 위" 인형을 하나 집어
 * 바구니에 담는다. 바구니에서 같은 인형이 연속으로 쌓이면 두 개가 터져서 사라진다.
 * 모든 moves 를 수행한 뒤 터져서 사라진 인형의 총 개수를 반환한다.
 *
 * [제한사항]
 * - 5 ≤ board 의 행/열 크기 ≤ 30 (정사각형)
 * - board 의 각 칸: 0 = 빈 칸, 1~100 = 인형 번호
 * - 1 ≤ moves 의 길이 ≤ 1,000, 각 원소는 1 이상 board 크기 이하의 "열 번호"(1-based)
 * - 인형이 없는 열을 집으면 아무 일도 일어나지 않는다.  ← 빠뜨리기 쉬운 조건
 *
 * [자료구조가 왜 스택인가]
 * - 각 "열"은 위에서부터 꺼내므로 LIFO → 열마다 스택 하나.
 * - "바구니"도 방금 넣은 인형하고만 비교하면 되므로 LIFO → 바구니도 스택.
 *   두 스택의 top 을 비교하는 것이 이 문제의 전부다.
 *
 * [풀이]
 * 1) board 를 열 단위 스택으로 바꾼다.
 *    board 는 [행][열] 이고 0행이 제일 위이므로, 아래 행(N-1)부터 위로 올라가며 push 한다.
 *    → 스택의 top 에 "가장 위에 있는 인형"이 오게 된다. 0(빈 칸)은 넣지 않는다.
 * 2) moves 를 순회하며 해당 열 스택에서 pop.
 *    - 열이 비어 있으면 건너뛴다.
 *    - 바구니가 비어 있지 않고 top 이 같은 인형이면 → 바구니 pop, 사라진 개수 += 2.
 *    - 아니면 바구니에 push.
 * 3) 누적한 개수를 반환한다. (터질 때 2개씩 사라지므로 항상 짝수)
 *
 * [자바 참고]
 * - 스택 용도로는 java.util.Stack 이 아니라 ArrayDeque 를 쓴다.
 *   Stack 은 Vector 를 상속한 JDK 1.0 레거시 클래스이고, Javadoc 자체가
 *   "Deque 와 그 구현체를 대신 쓰라"고 권고한다. 이유는 속도가 아니라 설계다.
 *     1) 순회 순서가 아래(bottom) -> 위(top) 다. pop/peek 은 위에서부터 꺼내지만
 *        for-each / toString / stream 은 바닥부터 나오므로 헷갈리기 쉽다.
 *        push 1,2,3 후 -> Stack: [1, 2, 3] / ArrayDeque: [3, 2, 1]
 *     2) Vector 상속이라 get(i), add(i, e) 같은 인덱스 접근이 노출되어 LIFO 추상화가 깨진다.
 *     3) 메서드에 synchronized 가 붙어 있지만 isEmpty() 후 pop() 같은 복합 연산은 어차피
 *        안전하지 않고, 경쟁 없는 락은 이 정도 규모에서 성능 차이가 사실상 없다.
 *   ArrayDeque 는 push / pop / peek / isEmpty 를 그대로 제공한다. (단, null 은 담을 수 없다)
 * - basket.peek() == doll 은 한쪽이 int 라 Integer 가 auto-unboxing 되어 값 비교가 된다.
 *   양쪽 다 Integer 였다면 참조 비교가 되어(캐시 범위 -128~127 밖에서) 틀릴 수 있으니 주의.
 *
 * [시간 복잡도] O(N² + M)   (N = board 한 변, M = moves 의 길이)
 *   board 를 스택으로 옮기는 데 N², moves 처리에 M.
 * [공간 복잡도] O(N²)       (인형을 담는 열 스택들)
 *
 */
public class P20260909 {

    public int solution(int[][] board, int[] moves) {
        int n = board.length;

        // 1) 열 단위 스택 만들기 (아래 행 -> 위 행 순서로 push 해야 top 이 최상단 인형이 된다)
        List<Deque<Integer>> columns = new ArrayList<>();
        for (int c = 0; c < n; c++) {
            Deque<Integer> column = new ArrayDeque<>();
            for (int r = n - 1; r >= 0; r--) {
                if (board[r][c] != 0) {
                    column.push(board[r][c]);
                }
            }
            columns.add(column);
        }

        // 2) 크레인 동작
        Deque<Integer> basket = new ArrayDeque<>();
        int removed = 0;
        for (int move : moves) {
            Deque<Integer> column = columns.get(move - 1); // moves 는 1-based
            if (column.isEmpty()) {
                continue; // 빈 열을 집으면 아무 일도 없음
            }
            int doll = column.pop();
            if (!basket.isEmpty() && basket.peek() == doll) {
                basket.pop();
                removed += 2; // 두 개가 함께 사라진다
            } else {
                basket.push(doll);
            }
        }
        return removed;
    }

    public static void main(String[] args) {
        P20260909 sol = new P20260909();

        // 프로그래머스 입출력 예제
        int[][] board = {
                {0, 0, 0, 0, 0},
                {0, 0, 1, 0, 3},
                {0, 2, 5, 0, 1},
                {4, 2, 4, 4, 2},
                {3, 5, 1, 3, 1}
        };
        check(sol.solution(board, new int[]{1, 5, 3, 5, 1, 2, 1, 4}), 4);

        // 추가 테스트 (경계값)
        int[][] two = {
                {0, 0},
                {1, 1}
        };
        check(sol.solution(two, new int[]{1, 2}), 2);          // 같은 인형 두 개 → 터짐
        check(sol.solution(two, new int[]{1, 1, 1, 2}), 2);    // 빈 열을 여러 번 집어도 무시
        check(sol.solution(two, new int[]{1}), 0);             // 하나만 담으면 터질 게 없음

        int[][] diff = {
                {0, 0},
                {1, 2}
        };
        check(sol.solution(diff, new int[]{1, 2}), 0);         // 서로 다른 인형은 쌓이기만 함

        int[][] empty = {
                {0, 0},
                {0, 0}
        };
        check(sol.solution(empty, new int[]{1, 2, 1, 2}), 0);  // 인형이 아예 없는 판
    }

    private static void check(int actual, int expected) {
        boolean ok = expected == actual;
        System.out.printf("%s expected=[%d] actual=[%d]%n", ok ? "PASS" : "FAIL", expected, actual);
    }
}

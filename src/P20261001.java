import java.util.*;

/**
 * LeetCode 100 - Same Tree (DFS)
 * https://leetcode.com/problems/same-tree/
 *
 * [문제]
 * 두 이진 트리의 루트 p, q 가 주어진다. 두 트리가 같으면 true, 다르면 false 를 반환한다.
 * 같다는 것은 "구조가 똑같고" "같은 자리의 값도 똑같다"는 뜻이다.
 *
 * [제한사항]
 * - 노드 개수는 0 이상 100 이하 (빈 트리가 들어올 수 있다)
 * - -10^4 ≤ Node.val ≤ 10^4
 *
 * [자료구조/알고리즘이 왜 DFS 인가]
 * - "두 트리가 같다" = "루트 값이 같고" + "왼쪽끼리 같고" + "오른쪽끼리 같다"
 *   → 문제가 그대로 같은 모양의 작은 문제로 쪼개진다. 재귀(DFS)로 쓰면 코드가 몇 줄로 끝난다.
 *      isSameTree(p, q) = p.val == q.val && isSameTree(p.left, q.left) && isSameTree(p.right, q.right)
 * - 두 트리를 "동시에" 같은 자리로 내려가며 비교하는 것이 핵심이다. 한쪽만 먼저 다 훑으면 자리를 맞출 수 없다.
 * - 빈 노드(null)끼리 만나면 그 자리는 같다고 보고 true → 재귀를 멈추는 바닥이 된다.
 * - BFS 로도 풀리지만 두 트리의 큐를 나란히 돌리며 null 자리까지 맞춰 담아야 해서 코드가 길어진다.
 *
 * [풀이]
 * isSameTree(p, q) = p 와 q 를 루트로 하는 두 서브트리가 같은가
 * 1) 둘 다 null 이면 같다 → true
 * 2) 한쪽만 null 이면 구조가 다르다 → false
 * 3) 값이 다르면 → false
 * 4) 왼쪽끼리, 오른쪽끼리 재귀로 비교해서 둘 다 true 여야 true
 * [주의할 점]
 * 1) 값만 비교하면 안 되고 구조도 봐야 한다. [1, 2] 와 [1, null, 2] 는 값은 같지만 자식의 좌우가 달라 false 다.
 * 2) 한쪽만 null 인 경우를 값 비교보다 "먼저" 걸러야 한다. 순서를 바꾸면 null.val 에서 NullPointerException 이 난다.
 * 3) 둘 다 빈 트리면 true 다. (비어 있는 것끼리는 같다고 본다)
 *
 * [시간 복잡도] O(N)  두 트리를 나란히 내려가며 노드를 한 번씩만 본다 (N 은 더 작은 트리의 노드 수)
 * [공간 복잡도] O(H)  재귀 스택이 트리 높이만큼 쌓인다 (최악에 한쪽으로만 이어지면 O(N))
 */
public class P20261001 {

    /** LeetCode 가 제공하는 이진 트리 노드 정의 */
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode() {}

        TreeNode(int val) {
            this.val = val;
        }

        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    public boolean isSameTree(TreeNode p, TreeNode q) {
        // 빈 자리끼리 만났으면 같다 → 재귀를 멈추는 바닥
        if (p == null && q == null) {
            return true;
        }

        // 한쪽만 비었으면 구조가 다르다 (값 비교보다 먼저 걸러야 NPE 가 안 난다)
        if (p == null || q == null) {
            return false;
        }

        // 값이 같고, 왼쪽끼리도 같고, 오른쪽끼리도 같아야 한다
        return p.val == q.val
                && isSameTree(p.left, q.left)
                && isSameTree(p.right, q.right);
    }

    public static void main(String[] args) {
        P20261001 s = new P20261001();

        // LeetCode 예제
        check(s, build(1, 2, 3), build(1, 2, 3), true);
        check(s, build(1, 2), build(1, null, 2), false);
        check(s, build(1, 2, 1), build(1, 1, 2), false);

        // 추가 테스트 (함정 / 경계값)
        check(s, build(), build(), true);                      // 둘 다 빈 트리
        check(s, build(1), build(), false);                    // 한쪽만 빈 트리
        check(s, build(1), build(2), false);                   // 루트 값만 다름
        check(s, build(1, 2, 3), build(1, 2, 4), false);       // 잎 하나만 다름
        check(s, build(1, 2, 3, 4), build(1, 2, 3), false);    // 한쪽이 더 깊다
    }

    private static void check(P20261001 s, TreeNode p, TreeNode q, boolean expected) {
        boolean actual = s.isSameTree(p, q);
        boolean ok = expected == actual;
        System.out.printf("%s expected=[%b] actual=[%b]%n", ok ? "PASS" : "FAIL", expected, actual);
    }

    /**
     * LeetCode 입력 표기([1, 2, 3])를 트리로 만든다.
     * 위에서 아래로, 왼쪽에서 오른쪽으로 채우고 null 은 빈 자리를 뜻한다.
     */
    private static TreeNode build(Integer... values) {
        if (values.length == 0 || values[0] == null) {
            return null;
        }

        TreeNode root = new TreeNode(values[0]);
        Queue<TreeNode> queue = new ArrayDeque<>();
        queue.offer(root);

        int i = 1;
        while (i < values.length && !queue.isEmpty()) {
            TreeNode node = queue.poll();

            if (i < values.length && values[i] != null) {
                node.left = new TreeNode(values[i]);
                queue.offer(node.left);
            }
            i++;

            if (i < values.length && values[i] != null) {
                node.right = new TreeNode(values[i]);
                queue.offer(node.right);
            }
            i++;
        }

        return root;
    }
}

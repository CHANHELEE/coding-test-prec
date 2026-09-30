import java.util.*;

/**
 * LeetCode 104 - Maximum Depth of Binary Tree (DFS)
 * https://leetcode.com/problems/maximum-depth-of-binary-tree/
 *
 * [문제]
 * 이진 트리의 루트 root 가 주어진다. 트리의 최대 깊이를 반환한다.
 * 최대 깊이란 루트에서 가장 먼 잎(leaf)까지 가는 동안 지나는 "노드의 개수"다.
 *
 * [제한사항]
 * - 노드 개수는 0 이상 10^4 이하 (빈 트리가 들어올 수 있다)
 * - -100 ≤ Node.val ≤ 100
 *
 * [자료구조/알고리즘이 왜 DFS 인가]
 * - "어떤 노드의 깊이"는 "왼쪽 서브트리의 깊이"와 "오른쪽 서브트리의 깊이" 중 큰 값에 자기 자신 1 을 더한 값이다.
 *   → 문제가 그대로 같은 모양의 작은 문제로 쪼개진다. 재귀(DFS)로 쓰면 코드가 세 줄로 끝난다.
 *      maxDepth(node) = 1 + max(maxDepth(node.left), maxDepth(node.right))
 * - 빈 노드(null)의 깊이는 0 이다. 이게 재귀를 멈추는 바닥이 된다.
 * - 노드가 최대 1만 개라 한쪽으로만 쭉 이어지면 재귀 깊이도 1만이 되지만,
 *   자바 기본 스택은 보통 이 정도 깊이는 견디므로 재귀로 충분하다.
 *
 * [풀이]
 * maxDepth(node) = node 를 루트로 하는 서브트리의 최대 깊이
 * 1) node 가 null 이면 0
 * 2) 아니면 왼쪽과 오른쪽 깊이를 각각 구해 더 큰 쪽에 1 을 더한다
 * [시간 복잡도] O(N)  모든 노드를 한 번씩 방문한다
 * [공간 복잡도] O(H)  재귀 스택이 트리 높이만큼 쌓인다 (최악에 한쪽으로만 이어지면 O(N))
 */
public class P20260930 {

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

    public int maxDepth(TreeNode root) {
        // 빈 노드의 깊이는 0 → 재귀를 멈추는 바닥
        if (root == null) {
            return 0;
        }

        // 자기 자신 1 + 더 깊은 쪽 서브트리의 깊이
        return 1 + Math.max(maxDepth(root.left), maxDepth(root.right));
    }

    public static void main(String[] args) {
        P20260930 s = new P20260930();

        // LeetCode 예제
        check(s, build(3, 9, 20, null, null, 15, 7), 3);
        check(s, build(1, null, 2), 2);
    }

    private static void check(P20260930 s, TreeNode root, int expected) {
        check(s.maxDepth(root), expected);
    }

    /**
     * LeetCode 입력 표기([3, 9, 20, null, null, 15, 7])를 트리로 만든다.
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

    private static void check(int actual, int expected) {
        boolean ok = expected == actual;
        System.out.printf("%s expected=[%d] actual=[%d]%n", ok ? "PASS" : "FAIL", expected, actual);
    }
}

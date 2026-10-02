import java.util.*;

/**
 * LeetCode 110 - Balanced Binary Tree (DFS)
 * https://leetcode.com/problems/balanced-binary-tree/
 *
 * [문제]
 * 이진 트리의 루트 root 가 주어진다. 이 트리가 "높이 균형 트리"면 true, 아니면 false 를 반환한다.
 * 높이 균형 트리란 "모든" 노드에서 왼쪽 서브트리와 오른쪽 서브트리의 높이 차이가 1 이하인 트리다.
 *
 * [제한사항]
 * - 노드 개수는 0 이상 5,000 이하 (빈 트리가 들어올 수 있다)
 * - -10^4 ≤ Node.val ≤ 10^4
 *
 * [자료구조/알고리즘이 왜 DFS 인가]
 * - 판정에 필요한 재료가 "서브트리의 높이"다. 높이는 자식의 높이에서 올라오는 값이므로,
 *   아래에서 위로 거슬러 올라오는 재귀(DFS)와 딱 맞는다.
 * - 조건이 "모든 노드에서" 이므로 루트만 봐서는 안 된다. 모든 노드를 각자 검사해야 한다.
 * [풀이]
 * height(node) = node 서브트리의 높이. 단, 균형이 깨진 곳을 만났으면 -1
 * 1) node 가 null 이면 높이 0
 * 2) 왼쪽 높이를 구한다. -1 이면 아래가 이미 깨졌으니 바로 -1 을 반환
 * 3) 오른쪽 높이도 같은 식으로 구한다
 * 4) 두 높이의 차가 2 이상이면 이 노드에서 깨졌으니 -1 을 반환
 * 5) 아니면 1 + max(왼쪽, 오른쪽) 을 반환
 * → 마지막에 height(root) != -1 이면 균형 트리다
 * [시간 복잡도] O(N)  노드마다 높이를 한 번만 구한다
 * [공간 복잡도] O(H)  재귀 스택이 트리 높이만큼 쌓인다 (최악에 한쪽으로만 이어지면 O(N))
 */
public class P20261003 {

    private static final int NOT_BALANCED = -1;

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

    public boolean isBalanced(TreeNode root) {
        return height(root) != NOT_BALANCED;
    }

    /** node 서브트리의 높이를 반환한다. 단, 균형이 깨진 곳을 만났으면 NOT_BALANCED(-1) 를 반환한다. */
    private int height(TreeNode node) {
        // 빈 노드의 높이는 0 → 재귀를 멈추는 바닥
        if (node == null) {
            return 0;
        }

        // 아래에서 이미 깨졌으면 더 볼 필요 없이 그대로 올려보낸다
        int leftHeight = height(node.left);
        if (leftHeight == NOT_BALANCED) {
            return NOT_BALANCED;
        }

        int rightHeight = height(node.right);
        if (rightHeight == NOT_BALANCED) {
            return NOT_BALANCED;
        }

        // 이 노드에서 높이 차가 2 이상이면 깨진 것 (1 까지는 균형)
        if (Math.abs(leftHeight - rightHeight) > 1) {
            return NOT_BALANCED;
        }

        return 1 + Math.max(leftHeight, rightHeight);
    }

    public static void main(String[] args) {
        P20261003 s = new P20261003();

        // LeetCode 예제
        check(s, build(3, 9, 20, null, null, 15, 7), true);
        check(s, build(1, 2, 2, 3, 3, null, null, 4, 4), false);
        check(s, build(), true);
    }

    private static void check(P20261003 s, TreeNode root, boolean expected) {
        check(s.isBalanced(root), expected);
    }

    private static void check(boolean actual, boolean expected) {
        boolean ok = expected == actual;
        System.out.printf("%s expected=[%b] actual=[%b]%n", ok ? "PASS" : "FAIL", expected, actual);
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
}

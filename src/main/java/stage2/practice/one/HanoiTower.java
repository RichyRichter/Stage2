package stage2.practice.one;

import java.util.Stack;

public class HanoiTower {
    private final static Stack<Integer> A = new Stack<>();
    private final static Stack<Integer> B = new Stack<>();
    private final static Stack<Integer> C = new Stack<>();

    private static void printStacks() {
        printStack(A);
        printStack(B);
        printStack(C);
        System.out.println();
    }

    private static void printStack(Stack<Integer> stack) {
        System.out.print("[");
        stack.stream()
                .sorted()
                .forEach(System.out::print);
        System.out.print("]");
    }

    public static void movementHanoi(int num) {
        for (int i = num; i > 0; i--) {
            A.push(i);
        }
        printStacks();
        moveDisks(num, A, B, C);
    }

    public static void moveDisks(int num, Stack<Integer> a, Stack<Integer> b, Stack<Integer> c) {
        if (num == 1) {
            c.push(a.pop());
            printStacks();
            return;
        }
        moveDisks(num - 1, a, c, b);
        c.push(a.pop());
        printStacks();
        moveDisks(num - 1, b, a, c);
    }

    public static void main(String[] args) {
        movementHanoi(5);
    }
}

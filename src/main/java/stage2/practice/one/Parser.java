package stage2.practice.one;

import java.util.Stack;

public class Parser {
    public static boolean checkCorrect(String input) {
        Stack<Character> stack = new Stack<>();
        Character[] symbols = input.chars()
                .mapToObj(c -> (char) c)
                .filter(x -> x == '('
                        || x == ')'
                        || x == '{'
                        || x == '}'
                        || x == '['
                        || x == ']')
                .toArray(Character[]::new);

        for (char chr : symbols) {
            if (chr == '(' || chr == '{' || chr == '[') {
                stack.push(chr);
                continue;
            }
            if (stack.isEmpty() || !isMatch(stack, chr)) {
                return false;
            }
            stack.pop();
        }
        return true;
    }

    private static boolean isMatch(Stack<Character> stack, char chr) {
        return stack.peek() == '(' && chr == ')'
                || stack.peek() == '{' && chr == '}'
                || stack.peek() == '{' && chr == '}'
                || stack.peek() == '[' && chr == ']';
    }

    public static void main(String[] args) {
        System.out.println(Parser.checkCorrect("(abc * (a)) - [a/c+{a,b,c}]"));
        System.out.println(Parser.checkCorrect("(abc*(a)-{[a/c+a,b,c}]"));
    }
}

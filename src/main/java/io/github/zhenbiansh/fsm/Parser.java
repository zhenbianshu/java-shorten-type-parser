package io.github.zhenbiansh.fsm;

import io.github.zhenbiansh.fsm.event.Event;
import io.github.zhenbiansh.fsm.state.Start;
import io.github.zhenbiansh.fsm.state.State;

import java.text.StringCharacterIterator;
import java.util.Stack;

/**
 * @author zbs
 * @date 2020/3/10
 */
public class Parser {
    public static String parseToFullType(String shortenType) throws IllegalStateException {
        StringBuilder result = new StringBuilder();
        StringCharacterIterator scanner = new StringCharacterIterator(shortenType);
        Stack<State> states = new Stack<>();
        Start start = new Start();

        states.push(start);

        for (; ; scanner.next()) {
            char currentChar = scanner.current();
            if (currentChar == '\uFFFF') {
                return result.toString();
            }
            if (states.isEmpty()) {
                throw new IllegalStateException("unexpected char '" + currentChar + "' at position " + scanner.getIndex());
            }
            Event event = Event.parseToEvent(currentChar, scanner.getIndex());
            if (event == null) {
                throw new IllegalStateException("unknown char '" + currentChar + "' at position " + scanner.getIndex());
            }
            states.peek().onEvent(event, states, result);
        }
    }

    public static void main(String[] args) {
        System.out.println(Parser.parseToFullType("i"));
        System.out.println(Parser.parseToFullType("LTL"));
        System.out.println(Parser.parseToFullType("MTDM"));
        System.out.println(Parser.parseToFullType("MTLDLM"));
        System.out.println(Parser.parseToFullType("LMTDML"));
    }
}

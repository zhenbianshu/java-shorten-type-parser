package io.github.zhenbiansh.fsm;

import io.github.zhenbiansh.fsm.event.Event;
import io.github.zhenbiansh.fsm.handler.*;
import io.github.zhenbiansh.fsm.state.State;

import java.text.StringCharacterIterator;
import java.util.EnumMap;
import java.util.Map;
import java.util.Stack;

/**
 * @author zbs
 * @date 2020/3/10
 */
public class Parser {
    private static Map<State, StateHandler> STATE_TO_HANDLER_MAPPING = new EnumMap<>(State.class);

    static {
        STATE_TO_HANDLER_MAPPING.put(State.START, new StartHandler());
        STATE_TO_HANDLER_MAPPING.put(State.LIST_START, new ListStartHandler());
        STATE_TO_HANDLER_MAPPING.put(State.LIST_ELE, new ListEleHandler());
        STATE_TO_HANDLER_MAPPING.put(State.SET_START, new SetStartHandler());
        STATE_TO_HANDLER_MAPPING.put(State.SET_ELE, new SetEleHandler());
        STATE_TO_HANDLER_MAPPING.put(State.MAP_START, new MapStartHandler());
        STATE_TO_HANDLER_MAPPING.put(State.MAP_LEFT, new MapLeftHandler());
        STATE_TO_HANDLER_MAPPING.put(State.MAP_RIGHT, new MapRightHandler());
    }

    public static String parseToFullType(String shortenType) throws IllegalStateException {
        StringBuilder result = new StringBuilder();
        StringCharacterIterator scanner = new StringCharacterIterator(shortenType);
        Stack<State> states = new Stack<>();

        states.push(State.START);

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

            STATE_TO_HANDLER_MAPPING.get(states.peek()).handle(event, states, result);
        }
    }

    public static void main(String[] args) {
        System.out.println(Parser.parseToFullType("i"));
        System.out.println(Parser.parseToFullType("LTL"));
        System.out.println(Parser.parseToFullType("MTDM"));
        System.out.println(Parser.parseToFullType("MTLDLM"));
        System.out.println(Parser.parseToFullType("LMTDML"));
        System.out.println(Parser.parseToFullType("LLTLL"));
    }
}

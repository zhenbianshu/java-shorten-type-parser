package io.github.zhenbiansh.fsm.event.handler;

import io.github.zhenbiansh.fsm.event.Event;
import io.github.zhenbiansh.fsm.state.State;

import java.util.Stack;

/**
 * @author zbs
 * @date 2020/3/10
 */
public class MapLeftHandler implements StateHandler {
    @Override
    public void handle(Event event, Stack<State> states, StringBuilder result) {
        result.append(",");

        result.append(event.getParsedVal());
        switch (event.getEventType()) {
            case MAP:
                states.push(State.MAP_START);
                break;
            case SET:
                states.push(State.SET_START);
                break;
            case LIST:
                states.push(State.LIST_START);
                break;
            case WRAPPED_ELE:
                states.pop();
                states.push(State.MAP_RIGHT);
                break;
            case PRIMITIVE_ELE:
                throw new IllegalStateException("unexpected primitive char '" + event.getCharacter() + "' at position " + event.getIndex());
            default:
        }
    }
}

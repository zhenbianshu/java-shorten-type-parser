package io.github.zhenbiansh.fsm.state;

import io.github.zhenbiansh.fsm.event.Event;

import java.util.Stack;

/**
 * @author zbs
 * @date 2020/3/10
 */
public class MapLeft implements State {
    @Override
    public void onEvent(Event event, Stack<State> states, StringBuilder result) {
        result.append(",");

        result.append(event.getParsedVal());
        switch (event.getEventType()) {
            case MAP:
                states.push(new MapStart());
                break;
            case SET:
                states.push(new SetStart());
                break;
            case LIST:
                states.push(new ListStart());
                break;
            case WRAPPED_ELE:
                states.pop();
                states.push(new MapRight());
                break;
            case PRIMITIVE_ELE:
                throw new IllegalStateException("unexpected primitive char '" + event.getCharacter() + "' at position " + event.getIndex());
            default:
        }
    }
}

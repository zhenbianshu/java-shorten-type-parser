package io.github.zhenbiansh.fsm.state;

import io.github.zhenbiansh.fsm.event.Event;

import java.util.Stack;

/**
 * @author zbs
 * @date 2020/3/10
 */
public class Start implements State {

    @Override
    public void onEvent(Event event, Stack<State> states, StringBuilder result) throws IllegalStateException {
        states.pop();
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
            default:
        }
    }

}

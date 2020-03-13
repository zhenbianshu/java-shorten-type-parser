package io.github.zhenbiansh.fsm.handler;

import io.github.zhenbiansh.fsm.event.Event;
import io.github.zhenbiansh.fsm.state.State;
import org.springframework.util.CollectionUtils;

import java.util.Stack;

/**
 * @author zbs
 * @date 2020/3/10
 */
public class SetEleHandler implements StateHandler {
    @Override
    public void handle(Event event, Stack<State> states, StringBuilder result) {
        result.append(">");

        switch (event.getEventType()) {
            case SET:
                states.pop();
                if (!CollectionUtils.isEmpty(states)) {
                    State lastState = states.pop();
                    if (State.LIST_START.equals(lastState)) {
                        states.push(State.LIST_ELE);
                    } else if (State.SET_START.equals(lastState)) {
                        states.push(State.SET_ELE);
                    } else if (State.MAP_START.equals(lastState)) {
                        states.push(State.MAP_LEFT);
                    } else if (State.MAP_LEFT.equals(lastState)) {
                        states.push(State.MAP_RIGHT);
                    } else {
                        throw new IllegalStateException("parse error, collection not closed properly.");
                    }
                }
                break;
            default:
                throw new IllegalStateException("unexpected char '" + event.getCharacter() + "' at position " + event.getIndex() + ", 'S' expected.");
        }
    }
}

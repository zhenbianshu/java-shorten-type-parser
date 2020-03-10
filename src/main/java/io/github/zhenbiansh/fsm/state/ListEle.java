package io.github.zhenbiansh.fsm.state;

import io.github.zhenbiansh.fsm.event.Event;
import org.springframework.util.CollectionUtils;

import java.util.Stack;

/**
 * @author zbs
 * @date 2020/3/10
 */
public class ListEle implements State {

    @Override
    public void onEvent(Event event, Stack<State> states, StringBuilder result) {
        result.append(">");

        switch (event.getEventType()) {
            case LIST:
                states.pop();
                if (!CollectionUtils.isEmpty(states)) {
                    State lastState = states.pop();
                    if (lastState instanceof ListStart) {
                        states.push(new ListEle());
                    } else if (lastState instanceof SetStart) {
                        states.push(new SetEle());
                    } else if (lastState instanceof MapStart) {
                        states.push(new MapLeft());
                    } else if (lastState instanceof MapLeft) {
                        states.push(new MapRight());
                    } else {
                        throw new IllegalStateException("parse error, collection not closed properly.");
                    }
                }
                break;
            default:
                throw new IllegalStateException("unexpected char '" + event.getCharacter() + "' at position " + event.getIndex() + ", 'L' expected.");
        }
    }
}

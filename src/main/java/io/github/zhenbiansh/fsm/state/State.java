package io.github.zhenbiansh.fsm.state;

import io.github.zhenbiansh.fsm.event.Event;

import java.util.Stack;

/**
 * @author zbs
 * @date 2020/3/10
 */
public interface State {
    /**
     * 各状态
     *
     * @param event
     * @param states
     * @param result
     * @return
     * @throws IllegalStateException
     */
    void onEvent(Event event, Stack<State> states, StringBuilder result);
}

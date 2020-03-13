package io.github.zhenbiansh.fsm.handler;

import io.github.zhenbiansh.fsm.event.Event;
import io.github.zhenbiansh.fsm.state.State;

import java.util.Stack;

/**
 * @author zbs
 * @date 2020/3/13
 */
public interface StateHandler {
    /**
     * 状态处理器
     *
     * @param event
     * @param states
     * @param result
     */
    void handle(Event event, Stack<State> states, StringBuilder result);
}

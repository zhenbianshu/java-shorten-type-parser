package io.github.zhenbiansh.fsm.state;

/**
 * @author zbs
 * @date 2020/3/13
 */
public enum State {
    /**
     * 解析状态
     */
    START, SET_START, SET_ELE, LIST_START, LIST_ELE, MAP_START, MAP_LEFT, MAP_RIGHT;
}

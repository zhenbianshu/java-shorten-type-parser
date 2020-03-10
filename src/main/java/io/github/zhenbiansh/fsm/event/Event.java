package io.github.zhenbiansh.fsm.event;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zbs
 * @date 2020/3/10
 */
public class Event {

    public static Map<Character, String> MARK_TO_FULL_MAPPING = new HashMap<>();

    private static Map<Character, EventType> EVENT_TYPE_MAPPING = new HashMap<>();

    static {
        MARK_TO_FULL_MAPPING.put('M', "Map");
        MARK_TO_FULL_MAPPING.put('L', "List");
        MARK_TO_FULL_MAPPING.put('S', "Set");
        MARK_TO_FULL_MAPPING.put('i', "int");
        MARK_TO_FULL_MAPPING.put('I', "Integer");
        MARK_TO_FULL_MAPPING.put('o', "long");
        MARK_TO_FULL_MAPPING.put('O', "Long");
        MARK_TO_FULL_MAPPING.put('d', "double");
        MARK_TO_FULL_MAPPING.put('D', "Double");
        MARK_TO_FULL_MAPPING.put('f', "float");
        MARK_TO_FULL_MAPPING.put('F', "Float");
        MARK_TO_FULL_MAPPING.put('T', "String");

        EVENT_TYPE_MAPPING.put('M', EventType.MAP);
        EVENT_TYPE_MAPPING.put('L', EventType.LIST);
        EVENT_TYPE_MAPPING.put('S', EventType.SET);
        EVENT_TYPE_MAPPING.put('i', EventType.PRIMITIVE_ELE);
        EVENT_TYPE_MAPPING.put('I', EventType.WRAPPED_ELE);
        EVENT_TYPE_MAPPING.put('o', EventType.PRIMITIVE_ELE);
        EVENT_TYPE_MAPPING.put('O', EventType.WRAPPED_ELE);
        EVENT_TYPE_MAPPING.put('d', EventType.PRIMITIVE_ELE);
        EVENT_TYPE_MAPPING.put('D', EventType.WRAPPED_ELE);
        EVENT_TYPE_MAPPING.put('f', EventType.PRIMITIVE_ELE);
        EVENT_TYPE_MAPPING.put('F', EventType.WRAPPED_ELE);
        EVENT_TYPE_MAPPING.put('T', EventType.WRAPPED_ELE);
    }

    private String parsedVal;

    private EventType eventType;

    private int index;

    private char character;

    public String getParsedVal() {
        return parsedVal;
    }

    public EventType getEventType() {
        return eventType;
    }

    public int getIndex() {
        return index;
    }

    public char getCharacter() {
        return character;
    }

    public Event(String parsedVal, EventType eventType, char character, int index) {
        this.parsedVal = parsedVal;
        this.eventType = eventType;
        this.character = character;
        this.index = index;
    }

    public static Event parseToEvent(char cha, int index) {
        String parsedVal = MARK_TO_FULL_MAPPING.get(cha);
        EventType eventType = EVENT_TYPE_MAPPING.get(cha);
        if (StringUtils.isBlank(parsedVal) || eventType == null) {
            return null;
        }
        return new Event(parsedVal, eventType, cha, index);
    }
}

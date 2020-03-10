package io.github.zhenbiansh.normal;

import com.google.common.collect.ImmutableSet;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

/**
 * @author zbs
 * @date 2019/12/27
 */
public class Parser {
    private final Stack<ELE_TYPE> expectedEle = new Stack<>();
    private CharacterIterator scanner;

    private static Map<Character, String> MARK_TO_FULL_MAPPING = new HashMap<>();
    private static Map<Character, ELE_TYPE> MARK_TO_INIT_TYPE = new HashMap<>();
    private static Map<ELE_TYPE, ELE_TYPE> NEXT_TYPE_MAPPING = new HashMap<>();

    static {
        MARK_TO_FULL_MAPPING.put('M', "Map<");
        MARK_TO_FULL_MAPPING.put('L', "List<");
        MARK_TO_FULL_MAPPING.put('S', "Set<");
        MARK_TO_FULL_MAPPING.put('i', "int");
        MARK_TO_FULL_MAPPING.put('I', "Integer");
        MARK_TO_FULL_MAPPING.put('o', "long");
        MARK_TO_FULL_MAPPING.put('O', "Long");
        MARK_TO_FULL_MAPPING.put('d', "double");
        MARK_TO_FULL_MAPPING.put('D', "Double");
        MARK_TO_FULL_MAPPING.put('f', "float");
        MARK_TO_FULL_MAPPING.put('F', "Float");
        MARK_TO_FULL_MAPPING.put('T', "String");

        MARK_TO_INIT_TYPE.put('M', ELE_TYPE.MAP_LEFT);
        MARK_TO_INIT_TYPE.put('L', ELE_TYPE.LIST_ELE);
        MARK_TO_INIT_TYPE.put('S', ELE_TYPE.SET_ELE);

        NEXT_TYPE_MAPPING.put(ELE_TYPE.LIST_ELE, ELE_TYPE.LIST_END);
        NEXT_TYPE_MAPPING.put(ELE_TYPE.SET_ELE, ELE_TYPE.SET_END);
        NEXT_TYPE_MAPPING.put(ELE_TYPE.MAP_LEFT, ELE_TYPE.MAP_RIGHT);
        NEXT_TYPE_MAPPING.put(ELE_TYPE.MAP_RIGHT, ELE_TYPE.MAP_END);
    }

    private static final Set<Character> PRIMITIVE_MARKS = ImmutableSet.of('i', 'o', 'd', 'f');
    private static final Set<Character> WRAPPED_MARKS = ImmutableSet.of('I', 'O', 'D', 'F', 'T');
    private static final Set<ELE_TYPE> COLLECTION_ELE_TYPES = ImmutableSet.of(ELE_TYPE.LIST_ELE, ELE_TYPE.SET_ELE, ELE_TYPE.MAP_LEFT, ELE_TYPE.MAP_RIGHT);
    private static final Set<ELE_TYPE> COLLECTION_END_TYPES = ImmutableSet.of(ELE_TYPE.LIST_END, ELE_TYPE.SET_END, ELE_TYPE.MAP_END);

    public Parser(String shortenType) {
        this.scanner = new StringCharacterIterator(shortenType);
        this.expectedEle.push(ELE_TYPE.END);
    }

    public String parseToFullType() throws IllegalStateException {
        StringBuilder sb = new StringBuilder();

        for (; ; this.scanner.next()) {
            Character currentChar = scanner.current();
            if (currentChar == '\uFFFF') {
                return sb.toString();
            }
            if (expectedEle.isEmpty()) {
                throw new IllegalStateException("shorted type parse error, unexpected char '" + currentChar + "' at position " + scanner.getIndex());
            }

            ELE_TYPE currentEleType = expectedEle.peek();
            if (COLLECTION_ELE_TYPES.contains(currentEleType) && PRIMITIVE_MARKS.contains(currentChar)) {
                throw new IllegalStateException("collection element should be wrapped types, unexpected char '" + currentChar + "' at position " + scanner.getIndex());
            }

            // 1.没闭合的   2.连续不对的
            if (COLLECTION_END_TYPES.contains(currentEleType)) {
                if (collectionEleEnd(currentEleType, currentChar)) {
                    dealCollectionEleEnd(sb, currentEleType);
                }else {
                    throw new IllegalStateException("unexpected char '" + currentChar + "' at position " + scanner.getIndex());
                }
            } else if (WRAPPED_MARKS.contains(currentChar)) {
                dealSingleEleEnd(sb, currentChar, currentEleType);
            } else if (MARK_TO_FULL_MAPPING.containsKey(currentChar)) {
                sb.append(MARK_TO_FULL_MAPPING.get(currentChar));
                if (MARK_TO_INIT_TYPE.containsKey(currentChar)) {
                    expectedEle.push(MARK_TO_INIT_TYPE.get(currentChar));
                }
            } else {
                throw new IllegalStateException("unknown char '" + currentChar + "' at position " + scanner.getIndex());
            }
        }
    }

    private boolean collectionEleEnd(ELE_TYPE currentEleType, Character currentChar) {
        if (ELE_TYPE.SET_END.equals(currentEleType) && 'S' == currentChar) {
            return true;
        }
        if (ELE_TYPE.LIST_END.equals(currentEleType) && 'L' == currentChar) {
            return true;
        }
        if (ELE_TYPE.MAP_END.equals(currentEleType) && 'M' == currentChar) {
            return true;
        }
        return false;
    }

    private void dealCollectionEleEnd(StringBuilder sb, ELE_TYPE currentEleType) {
        expectedEle.pop();
        sb.append(">");

        // 一个元素结束，可能会导致外一层的元素结束
        if (COLLECTION_ELE_TYPES.contains(expectedEle.peek())) {
            if (ELE_TYPE.MAP_LEFT.equals(currentEleType)) {
                sb.append(",");
            }
            ELE_TYPE nextEleType = NEXT_TYPE_MAPPING.get(expectedEle.peek());
            expectedEle.pop();
            if (nextEleType != null) {
                expectedEle.push(nextEleType);
            }
        }

        ELE_TYPE nextEleType = NEXT_TYPE_MAPPING.get(currentEleType);
        if (nextEleType != null) {
            expectedEle.push(nextEleType);
        }
    }

    private void dealSingleEleEnd(StringBuilder sb, Character currentChar, ELE_TYPE currentEleType) {
        expectedEle.pop();
        sb.append(MARK_TO_FULL_MAPPING.get(currentChar));

        if (ELE_TYPE.MAP_LEFT.equals(currentEleType)) {
            sb.append(",");
        }

        ELE_TYPE nextEleType = NEXT_TYPE_MAPPING.get(currentEleType);
        if (nextEleType != null) {
            expectedEle.push(nextEleType);
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println(new Parser("i").parseToFullType());
        System.out.println(new Parser("LTL").parseToFullType());
        System.out.println(new Parser("MTDM").parseToFullType());
        System.out.println(new Parser("MTLDLM").parseToFullType());
        System.out.println(new Parser("LMTDML").parseToFullType());
        System.out.println(new Parser("LMLTDML").parseToFullType());
    }

    private  enum ELE_TYPE {
        MAP_LEFT,
        MAP_RIGHT,
        MAP_END,
        LIST_ELE,
        SET_ELE,
        LIST_END,
        SET_END,
        END,
    }

}

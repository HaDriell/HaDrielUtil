package fr.hadriel.serialization.json;

import fr.hadriel.serialization.SerialException;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by glathuiliere setOn 18/10/2016.
 */
public final class Json {

    public static JsPrimitive2 deserialize(String input) throws SerialException {
        String json = input.trim();
        char c = json.charAt(0);
        switch (c) {
            case '[':
                return JsArray.deserialize(json);
            case '{':
                return JsObject.deserialize(json);
            case '\"':
            case '\'':
                return JsString.deserialize(json);
            case '.':
            case '+':
            case '-':
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                return JsNumber.deserialize(json);
            case 't':
            case 'f':
                return JsBoolean.deserialize(json);
            case 'n':
                return JsNull.deserialize(json);
            default: throw new SerialException("Unknown data structure starting with " + c);
        }
    }

    public static String getJsonStringValue(String jsonString) throws SerialException {
        String str = jsonString.trim();
        char c = str.charAt(0);
        if((c != '\"' && c != '\'') || c != str.charAt(str.length() - 1)) throw new SerialException("Invalid Json String : " + jsonString);
        return str.substring(1, str.length() - 1);
    }

    public static double getJsonNumberValue(String jsonString) throws SerialException {
        return Double.parseDouble(jsonString.trim());
    }

    public static String[] splitPair(String jsonPair) throws SerialException {
        int fcb = jsonPair.indexOf('{');
        if(fcb < 0) fcb = Integer.MAX_VALUE;
        int fsb = jsonPair.indexOf('[');
        if(fsb < 0) fsb = Integer.MAX_VALUE;
        int feq = jsonPair.indexOf('=');
        if(feq < 0) feq = Integer.MAX_VALUE;
        int fec = jsonPair.indexOf(':');
        if(fec < 0) fec = Integer.MAX_VALUE;

        int limit = Math.min(fcb, fsb);
        int splitIndex = Math.min(feq, fec);

        if(splitIndex == -1 || splitIndex >= limit)
            throw new SerialException("Invalid Pair cannot find ':' or '='");

        //Key parsing
        String key = getJsonStringValue(jsonPair.substring(0, splitIndex));
        String value = jsonPair.substring(splitIndex + 1);
        return new String[]{key, value};
    }

    public static List<String> splitJson(String input) {
        List<String> values = new ArrayList<>();
        Stack<Character> stack = new Stack<>();
        StringBuilder buffer = new StringBuilder();
        char c, top;
        int depth;
        int len = input.length();
        for(int i = 0; i < len; i++) {
            c = input.charAt(i);
            if(Character.isWhitespace(c)) continue; //discard whitespaces
            top = stack.empty() ? 0 : stack.peek();
            depth = stack.size();
            switch (c) {
                //Escape with " or ' is special case.
                case '\'':
                case '"':
                    if (depth == 1)
                        stack.push(c);
                    else if (depth == 2 && top == c)
                        stack.pop();
                    break;

                //Push in Depth (will continue buffering but will ignore ',' if not in depth 1)
                case '{':
                case '[':
                    stack.push(c);
                    if(depth == 0) continue; //stack was empty
                    break;

                //Pop depth if in accordance with the openning bracket name (else should throw errors !)
                case '}':
                case ']':
                    if(c-2 == top) stack.pop(); // In ASCII Table, chars are ... { / } ... and ... [ \ ] ...
                    if(depth == 1) continue; //stack is empty now
                    break;

                //When ',' is encoutered, if depth is the toplevel Array peeked will push the value to the result list
                case ',':
                    if(depth == 1) {
                        values.add(buffer.toString());
                        buffer = new StringBuilder();
                        continue;
                    }
                    break;
            }
            buffer.append(c);
        }
        if(buffer.length() > 0) // there was angle last item (1/Last)
            values.add(buffer.toString());
        return values;
    }
}
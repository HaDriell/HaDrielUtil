package fr.hadriel.serialization.json;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Pattern;

/**
 * Created by glathuiliere on 18/10/2016.
 */
public class Json {

    public static final Pattern P_QUOTED_STRING = Pattern.compile("^(\"(([^\"]|\")*)\"|'(([^']|')*)')$");
    public static final Pattern P_ARRAY_CONTENT = Pattern.compile("^\\[(.*?)\\]$");
    public static final Pattern P_OBJECT_CONTENT = Pattern.compile("^\\{(.*?)\\}$");

    public static List<String> splitjsonArray(String jsonArray) {
        List<String> values = new ArrayList<>();
        Stack<Character> stack = new Stack<>();
        StringBuilder sb = new StringBuilder();
        char c, p;
        int depth;
        int len = jsonArray.length();
        for(int i = 0; i < len; i++) {
            c = jsonArray.charAt(i);
            if(Character.isWhitespace(c)) continue; //discard whitespaces
            p = stack.empty() ? 0 : stack.peek();
            depth = stack.size();
            switch (c) {
                //Push in Depth (will continue buffering but will ignore ',' if not in depth 1)
                case '{':
                case '[':
                    stack.push(c);
                    if(depth == 0) continue; //stack was empty
                    break;

                //Pop depth if in accordance with the openning bracket type (else should throw errors !)
                case '}':
                case ']':
                    if(c-2 == p) stack.pop(); // In ASCII Table, chars are ... { / } ... and ... [ \ ] ...
                    if(depth == 1) continue; //stack is empty now
                    break;

                //When ',' is encoutered, if depth is the toplevel Array peeked will push the value to the result list
                case ',':
                    if(depth == 1) {
                        values.add(sb.toString());
                        sb = new StringBuilder();
                    }
                    continue;
            }
            sb.append(c);
        }
        if(sb.length() > 0) // there was a last item (1/Last)
            values.add(sb.toString());
        return values;
    }
}
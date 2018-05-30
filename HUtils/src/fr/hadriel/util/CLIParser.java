package fr.hadriel.util;

import java.util.ArrayList;

public class CLIParser {
    private String cmdString;

    public CLIParser(String cmdString) {
        this.cmdString = cmdString;
    }

    public ArrayList<String> getTokens()  {
        ArrayList<String> finalTokens = new ArrayList<>();
        ArrayList<StringBuffer> tokens = new ArrayList<>();
        char inArray[] = this.cmdString.toCharArray();
        StringBuffer token = new StringBuffer();
        if (!validateInput(inArray))
            throw new RuntimeException("Input has nvalid quotes");

        for (int i = 0; i <= inArray.length; i++) {

            if (i != inArray.length) {
                if ((inArray[i] != ' ') && (inArray[i] != '"')) {
                    token.append(inArray[i]);
                }

                if ((inArray[i] == '"') && (inArray[i - 1] != '\\')) {
                    i = i + 1;
                    while (checkIfLastQuote(inArray, i)) {
                        token.append(inArray[i]);
                        i++;
                    }
                }
            }
            if (i == inArray.length) {
                tokens.add(token);
                token = new StringBuffer();
            } else if (inArray[i] == ' ' && inArray[i] != '"') {
                tokens.add(token);
                token = new StringBuffer();
            }
        }

        for(StringBuffer tok:tokens){
            finalTokens.add(tok.toString());
        }
        return finalTokens;
    }

    private static boolean validateInput(char[] inArray) {
        boolean quoted = false;
        int pos = 0;
        for (int i = 0; i < inArray.length; i++) {
            if (inArray[i] == '"' && inArray[i - 1] != '\\') {
                pos = i;
                quoted = !quoted;
            }
        }
        return !quoted;
    }

    private static boolean checkIfLastQuote(char inArray[], int i) {
        if (inArray[i] == '"') {
            if (inArray[i - 1] == '\\') {
                return true;
            } else
                return false;
        } else
            return true;
    }
}
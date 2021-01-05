package heshan.compilertheory.parser;

import java.util.Iterator;
import java.util.regex.Pattern;

public class Tokenizer implements Iterator<String> {
    private InputScanner inputScanner;
    private String currentLine;
    private int idx;
    private boolean symbolMode = false;

    public Tokenizer(InputScanner inputScanner) {
        this.inputScanner = inputScanner;
        idx = 0;
        if(inputScanner.hasNext()){
            currentLine = inputScanner.next();
        } else {
            throw new RuntimeException(String.format("Input file %s is empty", inputScanner.getFileName()));
        }
    }

    private void advanceLine(){
        if (inputScanner.hasNext()) {
            currentLine = inputScanner.next();
            idx = 0;
        } else {
            idx = -1;
        }
    }

    private String getNextToken(){
        StringBuilder buffer = new StringBuilder();
        while(idx < currentLine.length()){
            char current = currentLine.charAt(idx);
            if(current == ' '){
                idx++;
                break;
            }
            if(current == '(' || current == ')'){
                if(buffer.length() == 0){
                    buffer.append(current);
                    idx++;
                }
                break;
            }
            if(!symbolMode){
                if(Character.isLetterOrDigit(current) || current == '_'){
                    buffer.append(current);
                    idx++;
                } else {
                    symbolMode = true;
                    break;
                }
            } else {
                if(Character.isLetterOrDigit(current) || current == '_'){
                    symbolMode = false;
                    break;

                } else {
                    buffer.append(current);
                    idx++;
                }
            }
        }
        String token = buffer.toString();
        if(token.length() == 0){
            if(idx >= currentLine.length()){
                advanceLine();
            }
            return getNextToken();
        }
        return token;
    }

    @Override
    public boolean hasNext() {
        if(idx >= 0 && idx < currentLine.length()){
            return true;
        }
        advanceLine();
        return idx != -1;
    }

    @Override
    public String next() {
        if(idx < currentLine.length()){
            return getNextToken();
        }
        advanceLine();
        return getNextToken();
    }
}

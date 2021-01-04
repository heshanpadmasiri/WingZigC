package heshan.compilertheory.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class SymbolTable {
    Map<Integer, Integer> table = new TreeMap<>();

    void insert(int key, Integer value) throws SymbolAlreadyInException {
        if(table.containsKey(key)){
            throw new SymbolAlreadyInException();
        } else {
            table.put(key, value);
        }
    }

    void upsert(int key, Integer value){
        if(table.containsKey(key)){
            table.replace(key, value);
        } else {
            table.put(key, value);
        }
    }

    boolean contains(int key){
        return table.containsKey(key);
    }

    int getValue(int key){
        return table.get(key);
    }

    int getNextKey(){
        return table.size();
    }
}

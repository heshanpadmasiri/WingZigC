package heshan.compilertheory.parser;

import java.util.HashMap;
import java.util.Map;

public class SymbolTable {
    private Map<String, Integer> locationMap = new HashMap<>();
    private Map<String, DataType> typeMap = new HashMap<>();

    public void addSymbol(String name, DataType type, int stackLocation){
        if (locationMap.containsKey(name)){
            throw new RuntimeException("Duplicate declaration of " + name);
        }
        assert stackLocation >= 0;
        locationMap.put(name, stackLocation);
        typeMap.put(name, type);
    }

    public int getLocation(String name){
        return locationMap.get(name);
    }

    public DataType getType(String name){
        return typeMap.get(name);
    }

    public boolean contains(String name){
        return locationMap.containsKey(name);
    }
}

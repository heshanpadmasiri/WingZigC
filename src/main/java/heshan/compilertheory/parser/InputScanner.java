package heshan.compilertheory.parser;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Scanner;
import java.util.regex.Pattern;

public class InputScanner implements Iterator<String>{
    Scanner scanner;
    Pattern blockStart = Pattern.compile("\\{.*");
    Pattern blockEnd = Pattern.compile(".+}.*");
    Pattern blockEndPrefix = Pattern.compile(".+}");
    Pattern lineStart = Pattern.compile("#.*");
    Pattern emptyLine = Pattern.compile("\n?");
    Pattern multipleWhiteSpaces = Pattern.compile("\\s+");
    Path inputFile;

    public InputScanner(Path input_file) throws FileNotFoundException {
        this.inputFile = input_file;
        scanner = new Scanner(input_file.toFile()).useDelimiter("\n");
    }

    public String getFileName(){
        return inputFile.getFileName().toString();
    }

    private void skipComments(){
        if(scanner.hasNext(blockStart)){
            String token = scanner.next();
            while(token.charAt(token.length()-1) != '}'){
                token = scanner.next();
            }
            skipComments();
        }
        if (scanner.hasNext(lineStart) || scanner.hasNext(emptyLine)){
            scanner.next();
            skipComments();
        }
    }

    @Override
    public boolean hasNext() {
        skipComments();
        return scanner.hasNext();
    }

    @Override
    public String next() {
        skipComments();
        String token = scanner.next();
        token = lineStart.matcher(token).replaceAll("");
        if (blockStart.matcher(token).matches()){
            token = blockStart.matcher(token).replaceAll("");
            while(!scanner.hasNext(blockEnd)){
                scanner.next();
            }
        }
        token = blockEndPrefix.matcher(token).replaceAll("");
        token = multipleWhiteSpaces.matcher(token).replaceAll(" ");
        return token;
    }
}


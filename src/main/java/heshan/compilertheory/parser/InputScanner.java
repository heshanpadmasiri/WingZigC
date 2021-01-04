package heshan.compilertheory.parser;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Scanner;
import java.util.regex.Pattern;

public class InputScanner implements Iterator<String>{
    Scanner scanner;
    Pattern oneLineBlock = Pattern.compile(".*\\{.*}.*");
    Pattern blockStart = Pattern.compile(".*\\{.*");
    Pattern blockStartPostFix = Pattern.compile("\\{.*");
    Pattern blockEnd = Pattern.compile(".*}.*");
    Pattern blockEnd1 = Pattern.compile("\\{.*}");
    Pattern blockEnd2 = Pattern.compile(".*}");
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
        if(scanner.hasNext(blockStart) && !scanner.hasNext(oneLineBlock)){
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
        token = blockEnd1.matcher(token).replaceAll(""); // block comments starting and ending in the same line
        if (blockStart.matcher(token).matches()){ // block comment starting in this line
            token = blockStartPostFix.matcher(token).replaceAll("");
            while(!scanner.hasNext(blockEnd)){
                scanner.next();
            }
        }
        token = blockEnd2.matcher(token).replaceAll(""); // block comment ending in this line
        token = multipleWhiteSpaces.matcher(token).replaceAll(" ");
        if(token.length() == 0){
            if (this.hasNext()){
                return this.next();
            } else {
                return "<eof>";
            }
        }
        return token;
    }
}


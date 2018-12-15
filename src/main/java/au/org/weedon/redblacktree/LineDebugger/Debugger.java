package au.org.weedon.redblacktree.LineDebugger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

public class Debugger {

    private Map<String, ClassFileCode> classCodeMap = new HashMap<>();

    private static String getRelativeFileName(StackTraceElement stackTraceElement) {
        String className = stackTraceElement.getClassName();
        String fileName = stackTraceElement.getFileName();
        return getRelativeFileName("src\\main\\java", className, fileName);
    }

    private static String getRelativeFileName(String base, String className, String fileName) {

        System.out.println("Clazz: " + className);
        List<String> tokens = new LinkedList<>(Arrays.asList(className.split(Pattern.quote("."))));
        if(tokens.size() == 0) {
            throw new RuntimeException("Could not parse path from class name");
        }
        tokens.remove(tokens.size() - 1);
        return base + "\\" + String.join("\\", tokens) + "\\" + fileName;
    }

    private static String getAboveLine() {
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[2];
        int line = stackTraceElement.getLineNumber();
        String relativeFileName = getRelativeFileName(stackTraceElement);

        String currentLine = null;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(relativeFileName))) {
            currentLine = bufferedReader.readLine();
            for(int currentNum = 1;
                currentNum < line - 1 && currentLine != null;
                currentLine = bufferedReader.readLine(), ++currentNum) {
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return currentLine;
    }

}

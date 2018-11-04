package au.org.weedon.redblacktree.LineDebugger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

class ClassCode {
    private String className;
    private Map<String, ClassMethodCode> classMethodCodeMap;

    private ClassCode(String className, Map<String, ClassMethodCode> classMethodCodeMap) {
        this.className = className;
        this.classMethodCodeMap = classMethodCodeMap;
    }

    static ClassCode fromFile(String sourceBase, String fullyQualifiedClassName) {

        Map<String, ClassMethodCode> classMethodCodeMap;

        //ClassCode classCode = new ClassCode();

        String relativeFileName = getRelativeFileName(sourceBase, fullyQualifiedClassName);
        String currentLine = null;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(relativeFileName))) {
            int currentNum = 1;
            currentLine = bufferedReader.readLine();
            for(;currentLine != null; currentLine = bufferedReader.readLine(), ++currentNum) {

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static String getRelativeFileName(String base, String className) {

        System.out.println("Clazz: " + className);
        List<String> tokens = Arrays.asList(className.split(Pattern.quote(".")));
        if(tokens.size() == 0) {
            throw new RuntimeException("Could not parse path from class name");
        }
        return base + "\\" + String.join("\\", tokens) + ".java";
    }

}

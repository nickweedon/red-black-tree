package au.org.weedon.JavaSourceExtractor;

import au.org.weedon.JavaSource.JavaSourceLexer;
import au.org.weedon.JavaSource.JavaSourceParser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class JavaSourceExtractor {

    public static void extractSource() {

        ANTLRInputStream inputStream = null;

        try {
            FileInputStream fileInputStream = new FileInputStream("testFile.java");
            inputStream = new ANTLRInputStream(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        JavaSourceLexer javaSourceLexer = new JavaSourceLexer(inputStream);
        CommonTokenStream commonTokenStream = new CommonTokenStream(javaSourceLexer);
        JavaSourceParser javaSourceParser = new JavaSourceParser(commonTokenStream);

        JavaSourceParser.ClassdefContext classdefContext = javaSourceParser.classdef();
        JavaSourceExtractorVisitor visitor = new JavaSourceExtractorVisitor();
        visitor.visit(classdefContext);
    }
}

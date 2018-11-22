package au.org.weedon.JavaSourceExtractor;

import au.org.weedon.JavaSource.Java9Lexer;
import au.org.weedon.JavaSource.Java9Parser;
import au.org.weedon.JavaSource.Java9ParserVisitor;
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

        Java9Lexer javaSourceLexer = new Java9Lexer(inputStream);
        CommonTokenStream commonTokenStream = new CommonTokenStream(javaSourceLexer);
        Java9Parser javaSourceParser = new Java9Parser(commonTokenStream);

        Java9Parser.CompilationUnitContext classdefContext = javaSourceParser.compilationUnit();

        JavaSourceExtractorVisitor visitor = new JavaSourceExtractorVisitor(commonTokenStream);

        visitor.visit(classdefContext);
    }
}

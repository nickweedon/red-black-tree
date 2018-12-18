package au.org.weedon.JavaSourceExtractor;

import au.org.weedon.JavaSource.Java9Lexer;
import au.org.weedon.JavaSource.Java9Parser;
import au.org.weedon.redblacktree.LineDebugger.ClassFileCode;
import au.org.weedon.redblacktree.LineDebugger.ClassMethodCode;
import au.org.weedon.redblacktree.LineDebugger.CodeLine;
import au.org.weedon.redblacktree.LineDebugger.Statement;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.*;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

public class JavaSourceExtractor {

    private static final String NL = System.getProperty("line.separator");
    private static final int TAB_SPACE = 4;

    static class TabExpansionFilterInputStream extends FilterInputStream {

        private final Queue<Character> streamQueue = new LinkedList<>();
        private final int tabSpaces;

        protected TabExpansionFilterInputStream(InputStream inputStream, int tabSpaces) {
            super(inputStream);
            this.tabSpaces = tabSpaces;
        }

        @Override
        public int read() throws IOException {

            if(streamQueue.size() != 0) {
                return streamQueue.poll();
            }

            int next = super.read();

            if((char) next != '\t') {
                return next;
            }

            for(int i = 0; i < tabSpaces; i++) {
                streamQueue.add(' ');
            }

            //noinspection ConstantConditions
            return streamQueue.poll();
        }

        @Override
        public int read(byte[] byteArray) throws IOException {
            return this.read(byteArray, 0, byteArray.length);
        }

        @Override
        public int read(byte[] byteArray, int off, int len) throws IOException {
            if(byteArray == null) {
                throw new NullPointerException("byteArray is null");
            }
            if(off < 0 || len < 0 || len > byteArray.length - off) {
                throw new IndexOutOfBoundsException("offset and/or length are out of bounds");
            }

            if(len == 0) {
                return 0;
            }
            int value = this.read();

            if(value == -1) {
                return -1;
            }

            byteArray[off] = (byte) value;

            int i = off + 1;
            for(; i < byteArray.length; ++i) {
                value = this.read();
                if(value == -1) {
                    return i - off;
                }
                byteArray[i] = (byte) value;
            }

            return i - off;
        }
    }

    // convert InputStream to String
    private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }

    private static boolean isUpperStatement(CodeLine codeLine) {
        return getUpperStatementBounds(codeLine).isPresent();
    }

    private static boolean isLowerStatement(CodeLine codeLine) {
        return getLowerStatementBounds(codeLine).isPresent();
    }

    private static Optional<Integer> getUpperStatementBounds(CodeLine codeLine) {
        Statement statement = codeLine.getStatement();
        if(statement == null) {
            return Optional.empty();
        }
        if(statement.getCodeLineRange().upperEndpoint() != codeLine) {
            return Optional.empty();
        }
        return Optional.of(statement.getColumnRange().upperEndpoint());
    }

    private static Optional<Integer> getLowerStatementBounds(CodeLine codeLine) {
        Statement statement = codeLine.getStatement();
        if(statement == null) {
            return Optional.empty();
        }
        if(statement.getCodeLineRange().lowerEndpoint() != codeLine) {
            return Optional.empty();
        }
        return Optional.of(statement.getColumnRange().lowerEndpoint());
    }


    private static void printCode(ClassFileCode classFileCode) {

        for(ClassMethodCode classMethodCode : classFileCode.getClassMethodCodeMap().values()) {

            StringBuilder methodBodyText = new StringBuilder();
            StringBuilder methodBlock = new StringBuilder();
            for(CodeLine codeLine : classMethodCode.getCodeLines()) {
                //methodBlock += codeLine.getLineNumber() + ": " + codeLine.getCode();
                if(isLowerStatement(codeLine) || isUpperStatement(codeLine)) {
/*
                    methodBlock.append(codeLine.getStatementRange().toString());
                    methodBlock.append(" =>");
*/
                    int lowerIndex = getLowerStatementBounds(codeLine).orElse(-1);
                    int upperIndex = getUpperStatementBounds(codeLine).orElse(-1);

                    String code = codeLine.getCode();
                    int i = 0;
                    for (; i < code.length(); ++i) {
                        if(i == lowerIndex || i == upperIndex) {
                            methodBlock.append("**");
                        }
                        methodBlock.append(code.charAt(i));
                    }
                    if(i == upperIndex) {
                        methodBlock.append("**");
                    }
                    methodBlock.append(NL);
                } else {
/*
                    if(codeLine.getStatementRange() != null) {
                        methodBlock.append(codeLine.getStatementRange().toString());
                        methodBlock.append(" => ");
                    }
*/
                    methodBlock.append(codeLine.getCode());
                    methodBlock.append(NL);
                }

            }

            //String methodBlock = String.join("", codeLines.stream().map(CodeLine::getCode).collect(Collectors.toList()));

            //methodBlock = String.join(NL, lines);

            methodBodyText
                    .append(NL)
                    .append(classMethodCode.getReturnType())
                    .append(" ")
                    .append(classMethodCode.getFullClassName())
                    .append("#")
                    .append(classMethodCode.getMethodSignature())
                    .append(" ")
                    .append(methodBlock)
                    .append(NL);

            System.out.println(methodBodyText);
        }
    }

    public static void extractSource() {


        ANTLRInputStream inputStream = null;

        try {
            // Convert tabs to spaces before parsing
            InputStream fileInputStream = new TabExpansionFilterInputStream(new FileInputStream("testFile.java"), TAB_SPACE);
            inputStream = new ANTLRInputStream(fileInputStream);

            //System.out.println(getStringFromInputStream(fileInputStream));

        } catch (IOException e) {
            e.printStackTrace();
        }

        Java9Lexer javaSourceLexer = new Java9Lexer(inputStream);
        CommonTokenStream commonTokenStream = new CommonTokenStream(javaSourceLexer);
        Java9Parser javaSourceParser = new Java9Parser(commonTokenStream);

        Java9Parser.CompilationUnitContext classdefContext = javaSourceParser.compilationUnit();

        JavaFileMethodExtractorVisitor methodExtractorVisitor = new JavaFileMethodExtractorVisitor(commonTokenStream);

        methodExtractorVisitor.visit(classdefContext);

        ClassFileCode classFileCode = methodExtractorVisitor.getClassFileCode();

        JavaStatementRecognizerVisitor statementRecognizerVisitor = new JavaStatementRecognizerVisitor(classFileCode, commonTokenStream);

        statementRecognizerVisitor.visit(classdefContext);

        printCode(classFileCode);



    }
}

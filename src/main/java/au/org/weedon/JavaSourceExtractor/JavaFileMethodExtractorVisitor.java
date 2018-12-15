package au.org.weedon.JavaSourceExtractor;

import au.org.weedon.JavaSource.Java9Parser;
import au.org.weedon.JavaSource.Java9ParserBaseVisitor;
import au.org.weedon.redblacktree.LineDebugger.ClassFileCode;
import au.org.weedon.redblacktree.LineDebugger.ClassMethodCode;
import au.org.weedon.redblacktree.LineDebugger.CodeLine;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.apache.commons.lang.StringUtils;

import java.util.*;
import java.util.regex.Pattern;


public class JavaFileMethodExtractorVisitor extends Java9ParserBaseVisitor<String> {

    private final CommonTokenStream commonTokenStream;

    private Stack<String> classNameStack = new Stack<>();
    private String returnType;
    private String methodDeclarator;
    private String methodName;
    private int methodDeclaratorIndent;
    private String fullClassName;

    private ClassFileCode classFileCode;

    public ClassFileCode getClassFileCode() {
        return classFileCode;
    }

    public JavaFileMethodExtractorVisitor(CommonTokenStream commonTokenStream) {
        this.commonTokenStream = commonTokenStream;
    }

    @Override
    public String visitNormalClassDeclaration(Java9Parser.NormalClassDeclarationContext ctx) {


        String className = ctx.identifier().getText();

        if(classNameStack.size() == 0) {
            classFileCode = new ClassFileCode(className);
        }
        classNameStack.push(className);

        String retValue = super.visitNormalClassDeclaration(ctx);

        classNameStack.pop();

        return retValue;
    }

    private String getFullClassName() {
        return String.join(".", classNameStack.subList(0, classNameStack.size()));
    }

    private int getSpacesBeforeContext(ParserRuleContext context) {

        final int ctxStartIndex = context.start.getTokenIndex();
        final int ctxEndIndex = context.start.getStopIndex();

        int currentTokenIndex, spaceCount;

        tokenLoop: for(spaceCount = 0, currentTokenIndex = ctxStartIndex - 1;
            currentTokenIndex >= 0;
            --currentTokenIndex) {

            String currentToken = commonTokenStream.get(currentTokenIndex).getText();

            for(int i = currentToken.length() - 1; i >= 0; --i) {
                if(currentToken.charAt(i) == ' ') {
                    ++spaceCount;
                    continue;
                }
                break tokenLoop;
            }
        }

        return spaceCount;
    }

    private String fixLineIndent(String line) {
        int lineLSpace = lSpaceCount(line);
        int indent = Math.max(0, lineLSpace - methodDeclaratorIndent);
        return StringUtils.repeat(" ", indent) + ltrim(line);
    }

    private void addCodeLinesWithWhitespace(ClassMethodCode classMethodCode, ParserRuleContext context) {

        int startTokenIndex = context.start.getTokenIndex();
        int stopTokenIndex = context.stop.getTokenIndex();

        final List<CodeLine> codeLines = classMethodCode.getCodeLines();

        if(startTokenIndex == stopTokenIndex) {
            return;
        }

        StringBuilder currentLineText = new StringBuilder();
        StringBuilder linePrefix = new StringBuilder();

        int i = startTokenIndex;
        Token currentToken = commonTokenStream.get(i++);
        int lastLine = currentToken.getLine();
        currentLineText.append(currentToken.getText());
        int currentLineNum = -1;

        for(; i <= stopTokenIndex; i++) {
            currentToken = commonTokenStream.get(i);
            currentLineNum = currentToken.getLine();
            if(currentLineNum != lastLine) {
                // Need to backtrack to the last newline as the token includes all whitespace following
                // the newline at the next line.
                for(int j = currentLineText.length() - 1; currentLineText.charAt(j) != '\n'; --j) {
                    linePrefix.append(currentLineText.charAt(j));
                }

                // Strip the whitespace following the newline
                currentLineText.setLength(currentLineText.length() - linePrefix.length());
                CodeLine codeLine = new CodeLine(classMethodCode, fixLineIndent(currentLineText.toString()), codeLines.size(), lastLine);
                codeLines.add(codeLine);
                classFileCode.getCodeLineMap().put(lastLine, codeLine);
                currentLineText.setLength(0);

                // Add back the whitespace to the start of the next line
                if(linePrefix.length() != 0) {
                    currentLineText.append(linePrefix.reverse().toString());
                    linePrefix.setLength(0);
                }
            }
            currentLineText.append(currentToken.getText());
            lastLine = currentLineNum;
        }
        if(currentLineText.length() != 0) {
            CodeLine codeLine = new CodeLine(classMethodCode, fixLineIndent(currentLineText.toString()), codeLines.size(), currentLineNum);
            codeLines.add(codeLine);
            classFileCode.getCodeLineMap().put(lastLine, codeLine);
        }
    }



    private String getContextWithWhitespace(ParserRuleContext context) {

        int startTokenIndex = context.start.getTokenIndex();
        int stopTokenIndex = context.stop.getTokenIndex();

        StringBuilder textWithNewlines = new StringBuilder();

        for(int i = startTokenIndex; i <= stopTokenIndex; i++) {
            Token currentToken = commonTokenStream.get(i);
            textWithNewlines.append(currentToken.getText());
        }

        return textWithNewlines.toString();
    }

    @Override
    public String visitMethodDeclaration(Java9Parser.MethodDeclarationContext ctx) {

        Java9Parser.MethodHeaderContext methodHeaderCtx = ctx.methodHeader();

        returnType = methodHeaderCtx.result().getText();

        methodDeclaratorIndent = getSpacesBeforeContext(ctx);
        methodName = methodHeaderCtx.methodDeclarator().identifier().getText();
        methodDeclarator = getContextWithWhitespace(methodHeaderCtx.methodDeclarator());

        fullClassName = getFullClassName();

        //System.out.println(fullyQualifiedMethodName);
        //currentMethod = fullyQualifiedMethodName;

        return super.visitMethodDeclaration(ctx);
    }

    private final static Pattern LTRIM = Pattern.compile("^\\s+");

    public static String ltrim(String targetString) {
        return LTRIM.matcher(targetString).replaceAll("");
    }

    public static int lSpaceCount(String targetString) {
        for(int i = 0; i < targetString.length(); ++i) {
            if(targetString.charAt(i) != ' ') {
                return i;
            }
        }
        return 0;
    }

    @Override
    public String visitMethodBody(Java9Parser.MethodBodyContext ctx) {


        ClassMethodCode classMethodCode = new ClassMethodCode(returnType, fullClassName, methodName, methodDeclarator);
        addCodeLinesWithWhitespace(classMethodCode, ctx.block());
        classFileCode.getClassMethodCodeMap().put(methodName, classMethodCode);

        return super.visitMethodBody(ctx);
    }

    /*
    @Override
    public String visitMethoddef(Java9Parser.MethoddefContext ctx) {

        //System.out.println("Body text:\n" + ctx.CLASS_METHOD().getText());
        System.out.println("============== START METHOD ===============");

        System.out.println("Method: " + ctx.CLASS_METHOD().getText().trim());

        Token targetToken = ctx.CLASS_METHOD().getSymbol();

        int tokenIndex = targetToken.getTokenIndex();

        List<Token> methodChannel = commonTokenStream.getHiddenTokensToRight(tokenIndex, Java9Lexer.METHOD_BODY_TEXT);

        if(methodChannel != null) {
            for(Token methodText : methodChannel) {
                String text = methodText.getText();
                System.out.print(text);
            }
        }


        System.out.println("\n============== END METHOD ===============");

        //ctx.CLASS_METHOD().h

        return super.visitMethoddef(ctx);
    }
*/
}

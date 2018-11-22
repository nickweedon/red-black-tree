package au.org.weedon.JavaSourceExtractor;

import au.org.weedon.JavaSource.Java9Parser;
import au.org.weedon.JavaSource.Java9ParserBaseVisitor;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.apache.commons.lang.StringUtils;

import java.util.*;
import java.util.regex.Pattern;


public class JavaSourceExtractorVisitor extends Java9ParserBaseVisitor<String> {

    private final CommonTokenStream commonTokenStream;

    private Stack<String> classNameStack = new Stack<>();
    private String returnType;
    private String methodDeclarator;
    private int methodDeclaratorIndent;
    private String fullClassName;

    private static final String NL = System.getProperty("line.separator");

    public JavaSourceExtractorVisitor(CommonTokenStream commonTokenStream) {
        this.commonTokenStream = commonTokenStream;
    }

    @Override
    public String visitNormalClassDeclaration(Java9Parser.NormalClassDeclarationContext ctx) {


        String className = ctx.identifier().getText();
        classNameStack.push(className);

        String retValue = super.visitNormalClassDeclaration(ctx);

        classNameStack.pop();

        return retValue;
    }

    private String getFullClassName() {
        return String.join(".", classNameStack.subList(0, classNameStack.size()));
    }


    private int getSpacesBeforeContext(ParserRuleContext context, int tabSpaces) {

        final int ctxStartIndex = context.start.getTokenIndex();
        final int ctxEndIndex = context.start.getStopIndex();

        int currentTokenIndex, spaceCount;

        tokenLoop: for(spaceCount = 0, currentTokenIndex = ctxStartIndex - 1;
            currentTokenIndex >= 0;
            --currentTokenIndex) {

            String currentToken = commonTokenStream.get(currentTokenIndex).getText();

            for(int i = currentToken.length() - 1; i >= 0; --i) {
                if(currentToken.charAt(i) == '\t') {
                    spaceCount += tabSpaces;
                    continue;
                }
                if(currentToken.charAt(i) == ' ') {
                    ++spaceCount;
                    continue;
                }
                break tokenLoop;
            }
        }

        return spaceCount;
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

        methodDeclaratorIndent = getSpacesBeforeContext(ctx, 4);
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

        StringBuilder methodBodyText = new StringBuilder();

        //methodDeclaratorIndent
        //String indentString = StringUtils.repeat(" ", methodDeclaratorIndent);
        String methodBlock = getContextWithWhitespace(ctx.block());
        methodBlock = methodBlock.replace("\t", StringUtils.repeat(" ", 4));
        String lines[] = methodBlock.split("\\r?\\n");
        for(int i = 0; i < lines.length; i++) {
            String line = lines[i];
            int lineLSpace = lSpaceCount(line);
            int indent = Math.max(0, lineLSpace - methodDeclaratorIndent);
            lines[i] =  StringUtils.repeat(" ", indent) + ltrim(line);
        }
        methodBlock = String.join(NL, lines);

        methodBodyText
                .append(NL)
                .append(returnType)
                .append(" ")
                .append(fullClassName)
                .append("#")
                .append(methodDeclarator)
                .append(methodBlock)
                .append(NL);

        System.out.println(methodBodyText);

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

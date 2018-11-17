package au.org.weedon.JavaSourceExtractor;

import au.org.weedon.JavaSource.Java9BaseVisitor;
import au.org.weedon.JavaSource.Java9Lexer;
import au.org.weedon.JavaSource.Java9Parser;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;

import java.util.List;
import java.util.Stack;


public class JavaSourceExtractorVisitor extends Java9BaseVisitor<String> {

    private final CommonTokenStream commonTokenStream;

    private Stack<String> classNameStack = new Stack<>();
    private String returnType;
    private String methodDeclarator;
    private String fullClassName;

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

    @Override
    public String visitMethodDeclaration(Java9Parser.MethodDeclarationContext ctx) {

        Java9Parser.MethodHeaderContext methodHeaderCtx = ctx.methodHeader();

        returnType = methodHeaderCtx.result().getText();
        methodDeclarator = methodHeaderCtx.methodDeclarator().getText();
        fullClassName = getFullClassName();

        //System.out.println(fullyQualifiedMethodName);
        //currentMethod = fullyQualifiedMethodName;

        return super.visitMethodDeclaration(ctx);
    }

    @Override
    public String visitMethodBody(Java9Parser.MethodBodyContext ctx) {

        String fullyQualifiedMethodName = returnType + " " + fullClassName + "#" + methodDeclarator;
        String methodBody = ctx.block().getText();

        System.out.println(fullyQualifiedMethodName + " " + methodBody);


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

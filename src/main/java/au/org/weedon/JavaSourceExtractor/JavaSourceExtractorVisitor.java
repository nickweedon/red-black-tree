package au.org.weedon.JavaSourceExtractor;

import au.org.weedon.JavaSource.JavaSourceLexer;
import au.org.weedon.JavaSource.JavaSourceParser;
import au.org.weedon.JavaSource.JavaSourceParserBaseVisitor;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;

import java.util.List;


public class JavaSourceExtractorVisitor extends JavaSourceParserBaseVisitor<String> {

    private final CommonTokenStream commonTokenStream;

    public JavaSourceExtractorVisitor(CommonTokenStream commonTokenStream) {
        this.commonTokenStream = commonTokenStream;
    }

    @Override
    public String visitMethoddef(JavaSourceParser.MethoddefContext ctx) {

        //System.out.println("Body text:\n" + ctx.CLASS_METHOD().getText());
        System.out.println("============== START METHOD ===============");

        System.out.println("Method: " + ctx.CLASS_METHOD().getText().trim());

        Token targetToken = ctx.CLASS_METHOD().getSymbol();

        int tokenIndex = targetToken.getTokenIndex();

        List<Token> methodChannel = commonTokenStream.getHiddenTokensToRight(tokenIndex, JavaSourceLexer.METHOD_BODY_TEXT);

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
}

package au.org.weedon.JavaSourceExtractor;

import au.org.weedon.JavaSource.Java9Parser;
import au.org.weedon.JavaSource.Java9ParserBaseVisitor;
import au.org.weedon.redblacktree.LineDebugger.ClassFileCode;
import au.org.weedon.redblacktree.LineDebugger.CodeLine;
import com.google.common.collect.Range;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.apache.http.ParseException;

@RequiredArgsConstructor
public class JavaStatementRecognizerVisitor extends Java9ParserBaseVisitor<String> {

    private final ClassFileCode classFileCode;
    private final CommonTokenStream commonTokenStream;

    @Getter
    static class NestedStatementVisitor extends Java9ParserBaseVisitor<String> {
        private int tokenIndex = Integer.MAX_VALUE;
        private boolean isBlockStart = false;
        private boolean isStatement;

        @Override
        public String visitStatement(Java9Parser.StatementContext ctx) {

            isStatement = true;
            tokenIndex = ctx.start.getTokenIndex();
            return null;
        }

        @Override
        public String visitBlock(Java9Parser.BlockContext ctx) {
            isBlockStart = true;
            return null;
        }
    }

    private void parseStatement(ParserRuleContext ctx, int stopTokenIndex) {

        Token startToken = ctx.start;
        int nextTokenIndex = ctx.start.getTokenIndex() + 1;

        CodeLine codeLine = classFileCode.getCodeLineMap().get(startToken.getLine());

        if(codeLine == null) {
            //throw new ParseException("No code line found for statement starting at line: " + startToken.getLine() + " => " + startToken.getText());
            // Just skip this statement if we don't care about it
            return;
        }
        if(codeLine.getStatementRange() != null) {
            throw new ParseException("More than one statement found at code line: " + codeLine.getCode() + "Previous statement: '"
                    + codeLine.getCode().substring(
                            codeLine.getStatementRange().lowerEndpoint(), codeLine.getStatementRange().upperEndpoint())
                    + "' Additional statement first token: '" + startToken.getText() + "'"
            );
            // Just skip this statement if we don't care about it
            //return super.visitStatement(ctx);
        }
        Token lastToken, currentToken;
        lastToken = currentToken = startToken;

        while(currentToken.getLine() == startToken.getLine() && nextTokenIndex <= stopTokenIndex + 1) {
            lastToken = currentToken;
            currentToken = commonTokenStream.get(nextTokenIndex++);
        }
        codeLine.setStatementRange(Range.closed(
                startToken.getCharPositionInLine() - codeLine.getClassMethodCode().getMethodIndent(),
                lastToken.getCharPositionInLine() - codeLine.getClassMethodCode().getMethodIndent()
        ));
    }

    @Override
    public String visitStatement(Java9Parser.StatementContext ctx) {

        NestedStatementVisitor nestedStatementVisitor = new NestedStatementVisitor();
        if(ctx.getChildCount() > 0) {
            ctx.getChild(0).accept(nestedStatementVisitor);
        }

        if(nestedStatementVisitor.isBlockStart) {
            return super.visitStatement(ctx);
        }

        int stopTokenIndex = Math.min(ctx.stop.getTokenIndex(), nestedStatementVisitor.getTokenIndex());

        parseStatement(ctx, stopTokenIndex);
        return super.visitStatement(ctx);
    }

    @Override
    public String visitBlockStatement(Java9Parser.BlockStatementContext ctx) {

        NestedStatementVisitor nestedStatementVisitor = new NestedStatementVisitor();
        if(ctx.getChildCount() > 0) {
            ctx.getChild(0).accept(nestedStatementVisitor);
        }

        if(nestedStatementVisitor.isBlockStart) {
            return super.visitBlockStatement(ctx);
        }

        // Make sure we don't parse the same statement twice
        if(nestedStatementVisitor.isStatement) {
            return super.visitBlockStatement(ctx);
        }

        int stopTokenIndex = ctx.stop.getTokenIndex();

        parseStatement(ctx, stopTokenIndex);

        return super.visitBlockStatement(ctx);
    }
}

package au.org.weedon.JavaSourceExtractor;

import au.org.weedon.JavaSource.Java9Parser;
import au.org.weedon.JavaSource.Java9ParserBaseVisitor;
import au.org.weedon.redblacktree.LineDebugger.ClassFileCode;
import au.org.weedon.redblacktree.LineDebugger.CodeLine;
import com.google.common.collect.Range;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.http.ParseException;

import java.util.List;

@RequiredArgsConstructor
public class JavaStatementRecognizerVisitor extends Java9ParserBaseVisitor<String> {

    private final ClassFileCode classFileCode;
    private final CommonTokenStream commonTokenStream;

    @Getter
    static class NestedStatementVisitor extends Java9ParserBaseVisitor<String> {
        private int tokenIndex = Integer.MAX_VALUE;
        private boolean isBlockStatement = false;

        @Override
        public String visitStatement(Java9Parser.StatementContext ctx) {

            tokenIndex = ctx.start.getTokenIndex();
            return null;
        }

        @Override
        public String visitBlock(Java9Parser.BlockContext ctx) {
            isBlockStatement = true;
            return null;
        }
    }

    @Override
    public String visitStatement(Java9Parser.StatementContext ctx) {

        NestedStatementVisitor nestedStatementVisitor = new NestedStatementVisitor();
        if(ctx.getChildCount() > 0) {
            ctx.getChild(0).accept(nestedStatementVisitor);
        }

        if(nestedStatementVisitor.isBlockStatement) {
            return super.visitStatement(ctx);
        }

        Token startToken = ctx.start;
        int nextTokenIndex = ctx.start.getTokenIndex() + 1;
        int stopTokenIndex = Math.min(ctx.stop.getTokenIndex(), nestedStatementVisitor.getTokenIndex());

        CodeLine codeLine = classFileCode.getCodeLineMap().get(startToken.getLine());

        if(codeLine == null) {
            //throw new ParseException("No code line found for statement starting at line: " + startToken.getLine() + " => " + startToken.getText());
            // Just skip this statement if we don't care about it
            return super.visitStatement(ctx);
        }
        if(codeLine.getStatementRange() != null) {
            throw new ParseException("More than one statement found at code line: " + codeLine);
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

        return super.visitStatement(ctx);
    }
}

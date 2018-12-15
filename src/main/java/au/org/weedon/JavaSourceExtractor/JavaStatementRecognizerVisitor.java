package au.org.weedon.JavaSourceExtractor;

import au.org.weedon.JavaSource.Java9Parser;
import au.org.weedon.JavaSource.Java9ParserBaseVisitor;
import au.org.weedon.redblacktree.LineDebugger.ClassFileCode;
import au.org.weedon.redblacktree.LineDebugger.CodeLine;
import com.google.common.collect.Range;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.apache.http.ParseException;

@RequiredArgsConstructor
public class JavaStatementRecognizerVisitor extends Java9ParserBaseVisitor<String> {

    private final ClassFileCode classFileCode;
    private final CommonTokenStream commonTokenStream;

    @Override
    public String visitStatement(Java9Parser.StatementContext ctx) {

        Token startToken = ctx.start;
        int nextTokenIndex = ctx.start.getTokenIndex() + 1;
        int stopTokenIndex = ctx.stop.getTokenIndex();

        CodeLine codeLine = classFileCode.getCodeLineMap().get(startToken.getLine());

        if(codeLine == null) {
            //throw new ParseException("No code line found for statement starting at line: " + startToken.getLine() + " => " + startToken.getText());
            // Just skip this statement if we don't care about it
            return super.visitStatement(ctx);
        }
        if(codeLine.getStatementRange() != null) {
            throw new ParseException("More than one statement found at code line: " + codeLine);
        }
        Token lastToken, currentToken;
        lastToken = currentToken = startToken;

        while(currentToken.getLine() == startToken.getLine() && nextTokenIndex <= stopTokenIndex) {
            lastToken = currentToken;
            currentToken = commonTokenStream.get(nextTokenIndex++);
        }
        codeLine.setStatementRange(Range.closed(startToken.getCharPositionInLine(), lastToken.getCharPositionInLine()));

        return super.visitStatement(ctx);
    }
}

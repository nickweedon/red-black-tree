package au.org.weedon.JavaSourceExtractor;

import au.org.weedon.JavaSource.JavaSourceParser;
import au.org.weedon.JavaSource.JavaSourceParserBaseVisitor;


public class JavaSourceExtractorVisitor extends JavaSourceParserBaseVisitor<String> {

    @Override
    public String visitClassdef(JavaSourceParser.ClassdefContext ctx) {

        System.out.println("Body text:\n" + ctx.classbodyDef().methoddef().methodbodydef().getText());

        return super.visitClassdef(ctx);
    }
}

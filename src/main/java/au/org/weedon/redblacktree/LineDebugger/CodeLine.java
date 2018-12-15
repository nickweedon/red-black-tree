package au.org.weedon.redblacktree.LineDebugger;

import com.google.common.collect.Range;
import lombok.Getter;
import lombok.Setter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class CodeLine {
    private final ClassMethodCode classMethodCode;
    private final String code;
    private final int index;
    private final int lineNumber;
    private Range<Integer> statementRange;
}

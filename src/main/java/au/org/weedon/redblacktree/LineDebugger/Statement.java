package au.org.weedon.redblacktree.LineDebugger;

import com.google.common.collect.Range;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
public class Statement {
    private final Range<Integer> columnRange;
    private final Range<CodeLine> codeLineRange;
}

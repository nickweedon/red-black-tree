package au.org.weedon.redblacktree.LineDebugger;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class ClassMethodCode {
    private final String returnType;
    private final String fullClassName;
    private final String methodName;
    private final String methodSignature;
    private final List<CodeLine> codeLines = new ArrayList<>();
}

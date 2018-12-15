package au.org.weedon.redblacktree.LineDebugger;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Getter
public class ClassFileCode {
    private final String fileName;
    private final Map<String, ClassMethodCode> classMethodCodeMap = new HashMap<>();
    private final Map<Integer, CodeLine> codeLineMap = new HashMap<>();
}

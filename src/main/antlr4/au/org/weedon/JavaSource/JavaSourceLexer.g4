lexer grammar JavaSourceLexer;

///////////// Lexer rules /////////////////

// Standard alphabet, case insensitive

fragment A : ('A'|'a') ;
fragment B : ('B'|'b') ;
fragment C : ('C'|'c') ;
fragment D : ('D'|'d') ;
fragment E : ('E'|'e') ;
fragment F : ('F'|'f') ;
fragment G : ('G'|'g') ;
fragment H : ('H'|'h') ;
fragment I : ('I'|'i') ;
fragment J : ('J'|'j') ;
fragment K : ('K'|'k') ;
fragment L : ('L'|'l') ;
fragment M : ('M'|'m') ;
fragment N : ('N'|'n') ;
fragment O : ('O'|'o') ;
fragment P : ('P'|'p') ;
fragment Q : ('Q'|'q') ;
fragment R : ('R'|'r') ;
fragment S : ('S'|'s') ;
fragment T : ('T'|'t') ;
fragment U : ('U'|'u') ;
fragment V : ('V'|'v') ;
fragment W : ('W'|'w') ;
fragment X : ('X'|'x') ;
fragment Y : ('Y'|'y') ;
fragment Z : ('Z'|'z') ;


WS : SPACE+ -> skip;

START_BLOCK : '{' ;
END_BLOCK : '}' ;


// fragment WHITESPACE : (' ' | '\t' | '\r' | '\n');




CLASS : C L A S S ;

//CLASS_METHOD : METHOD_MODIFER* SPACE* IDENTIFIER SPACE* IDENTIFIER SPACE* EMPTY_PARAMS START_METHOD_BLOCK METHOD_BODY_TEXT END_METHOD_BLOCK -> pushMode(PARSE_METHOD_TEXT), popMode;
CLASS_METHOD : METHOD_MODIFER* SPACE* IDENTIFIER SPACE* IDENTIFIER SPACE* EMPTY_PARAMS SPACE* START_BLOCK  SPACE* -> pushMode(PARSE_METHOD_TEXT);



//METHOD_SIGNATURE : IDENTIFIER EMPTY_PARAMS METHOD_BLOCK;

//METHOD_BLOCK :  METHOD_BODY_TEXT -> pushMode(PARSE_METHOD_TEXT);

fragment SPACE : [ \t\r\n] ;
fragment METHOD_PARAMS : EMPTY_PARAMS ;

fragment METHOD_MODIFER : (STATIC | ACCESS_MODIFIER) ;
fragment ACCESS_MODIFIER : (PUBLIC | PRIVATE | PROTECTED) ;

fragment EMPTY_PARAMS : '()' ;
fragment PRIVATE : P R I V I T E ;
fragment PUBLIC : P U B L I C ;
fragment PROTECTED : P R O T E C T E D ;
fragment STATIC : S T A T I C ;

fragment LOWERCASE  : [a-z] ;
fragment UPPERCASE  : [A-Z] ;

IDENTIFIER : (LOWERCASE | UPPERCASE | '_')+ ;

mode PARSE_METHOD_TEXT ;

START_METHOD_BLOCK : SPACE* '{' SPACE* -> skip, pushMode(PARSE_METHOD_TEXT);
END_METHOD_BLOCK : '}' -> skip, popMode;
METHOD_BODY_TEXT : ~[{}]+ -> skip;






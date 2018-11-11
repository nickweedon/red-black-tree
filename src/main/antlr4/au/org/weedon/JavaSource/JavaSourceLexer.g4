lexer grammar JavaSourceLexer;

channels { METHOD_BODY_TEXT }

options {
}

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


fragment CLASS : C L A S S ;

CLASS_METHOD : SPACE* METHOD_MODIFER* SPACE* IDENTIFIER SPACE* IDENTIFIER SPACE* METHOD_PARAMS SPACE* START_BLOCK  SPACE* -> pushMode(PARSE_METHOD_TEXT);

fragment SPACE : [ \t\r\n] ;
fragment METHOD_PARAMS : SPACE* '(' ~[)]* ')' SPACE*;

fragment METHOD_MODIFER : (STATIC | ACCESS_MODIFIER) ;
fragment ACCESS_MODIFIER : (PUBLIC | PRIVATE | PROTECTED) ;

fragment EMPTY_PARAMS : '()' ;
fragment PRIVATE : P R I V I T E ;
fragment PUBLIC : P U B L I C ;
fragment PROTECTED : P R O T E C T E D ;
fragment STATIC : S T A T I C ;

fragment LOWERCASE  : [a-z] ;
fragment UPPERCASE  : [A-Z] ;

fragment IDENTIFIER : (LOWERCASE | UPPERCASE | '_')+ ;

CLASS_DEF : SPACE* CLASS SPACE* IDENTIFIER;

FILTER: . -> skip;

mode PARSE_METHOD_TEXT ;

START_METHOD_BLOCK : SPACE* '{' SPACE* -> channel(METHOD_BODY_TEXT), pushMode(PARSE_METHOD_TEXT);
END_METHOD_BLOCK : '}' -> channel(METHOD_BODY_TEXT), popMode;
BODY_TEXT : ~[{}]+ -> channel(METHOD_BODY_TEXT);






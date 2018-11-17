lexer grammar JavaSourceLexer;

//channels { METHOD_BODY_TEXT }

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
ENUM_DECL : ACCESS_MODIFIER? SPACE* ENUM SPACE* IDENTIFIER SPACE* START_BLOCK  ~[}]* END_BLOCK -> skip ;


START_BLOCK : '{' ;
END_BLOCK : '}' ;


fragment CLASS : C L A S S ;

fragment ENUM : E N U M ;


CLASS_METHOD : SPACE* METHOD_MODIFER* SPACE* IDENTIFIER SPACE* METHOD_NAME SPACE* METHOD_PARAMS SPACE* START_BLOCK '\n'* -> pushMode(PARSE_METHOD_TEXT);

fragment METHOD_NAME : IDENTIFIER ;

fragment SPACE : [ \t\r\n] ;

fragment SPACE_NONL : [ \t] ;

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

fragment CLASS_ATTRIBUTES : ~[{] ;

CLASS_DEF : SPACE* CLASS SPACE* IDENTIFIER SPACE* ;
//CLASS_DEF : SPACE* CLASS SPACE* IDENTIFIER SPACE* CLASS_ATTRIBUTES*;
//CLASS_DEF : SPACE* CLASS SPACE* IDENTIFIER '<V>'?;

FILTER: . -> skip;

mode PARSE_METHOD_TEXT ;

START_METHOD_BLOCK : SPACE* '{' -> skip, pushMode(PARSE_METHOD_TEXT);
END_METHOD_BLOCK : SPACE* '}' SPACE* -> skip, popMode;
NEW_LINES : [\r\n] -> skip ;
fragment STATEMENT_TEXT : ~[{}/;]+ ;
STATEMENT : (STATEMENT_TEXT | ENUM_STATEMENT) SPACE* ';' ;
COMMENT : SPACE_NONL* '//' SPACE_NONL* ~[\r\n]+ ;
COMMENT_BLOCK : START_COMMENT_BLOCK COMMENT_BLOCK_TEXT* END_COMMENT_BLOCK ;
START_COMMENT_BLOCK : SPACE_NONL* '/*' -> pushMode(COMMENT_PARSE) ;
METHOD_WS : SPACE -> skip ;

ENUM_STATEMENT : START_ENUM_BLOCK ENUM_BODY* END_ENUM_BLOCK ;
START_ENUM_BLOCK : SPACE* ACCESS_MODIFIER? SPACE* 'enum' SPACE* IDENTIFIER SPACE* '{' -> pushMode(ENUM_PARSE);


mode COMMENT_PARSE ;

END_COMMENT_BLOCK : SPACE* '*/' -> popMode;
COMMENT_BLOCK_TEXT : ~[*] | '*';


mode ENUM_PARSE ;

ENUM_BODY : ~[}] -> skip;
END_ENUM_BLOCK : SPACE* '}' -> popMode;



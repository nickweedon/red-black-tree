parser grammar JavaSourceParser;

options { tokenVocab=JavaSourceLexer; }

///////////// Parser rules /////////////////

classdef : CLASS_DEF  classbodyDef ;

classbodyDef : START_BLOCK (classdef | classmethod )* END_BLOCK;

returntype: IDENTIFIER ;

statement : STATEMENT ;
methoddef : CLASS_METHOD ;
comment : COMMENT;
commentblock : COMMENT_BLOCK ;

classmethod :  methoddef (statement | comment | commentblock)* ;


parser grammar JavaSourceParser;

options { tokenVocab=JavaSourceLexer; }

///////////// Parser rules /////////////////

classdef : CLASS_DEF  classbodyDef ;

classbodyDef : START_BLOCK (classdef | methoddef )* END_BLOCK;

methodname: IDENTIFIER ;
returntype: IDENTIFIER ;

modifiers : METHOD_MODIFER* ;

methodparams: METHOD_PARAMS ;

methoddef :  CLASS_METHOD ;


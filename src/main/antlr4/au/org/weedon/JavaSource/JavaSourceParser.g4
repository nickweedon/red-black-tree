parser grammar JavaSourceParser;

options { tokenVocab=JavaSourceLexer; }

///////////// Parser rules /////////////////

//  WHITESPACE IDENTIFIER BODY_TEXT


classdef : CLASS className  classbodyDef ;

className : IDENTIFIER ;

classbodyDef : START_BLOCK methoddef END_BLOCK;

methodname: IDENTIFIER ;
returntype: IDENTIFIER ;

modifiers : METHOD_MODIFER* ;

methodparams: METHOD_PARAMS ;

// methodBodyDef: START_METHOD_BLOCK METHOD_BODY_TEXT END_METHOD_BLOCK  ;
//methoddef :  modifiers returntype methodname EMPTY_PARAMS START_BLOCK methodbodydef END_BLOCK;
//methoddef :  modifiers returntype;
//methoddef :  modifiers returntype methodname methodparams methodBodyDef ;

methoddef :  CLASS_METHOD;


//methodbodydef : METHOD_SIGNATURE ;


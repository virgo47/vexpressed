/*
Java/Groovy/EL like expression syntax.

Class generation is part of Maven build nothing needs to be done. Following lines
describe manual generation and are only just for my personal information:

Use IDE with ANTLR v4 plugin to generate classes:
- first set-up (right-click on the file, Configure ANTLR...)
- setup output directory to match this modules src/main/java
- set package for generated classes: vexpressed.grammar
- check "generate parse tree visitor", uncheck listener above (not necessary)

Command line when using downloaded "Complete ANTLR 4.5.1 Java binaries jar" (version may vary):
- know where the JAR is downloaded
- run the command in the expr/grammar package directory (where Expr.g4 is)
- java -jar ~/Downloads/antlr-4.5.1-complete.jar -o . -package vexpressed.grammar -visitor -no-listener Expr.g4
- -o . means this directory (can be omitted), in IDEA it somehow adds package to the output directory
  but this command does not do that, it merely sets pacakage in sources

After first manual generation - don't forget:
svn add any missing *.java file (they are probably added, but just in case...)
*.tokens files are NOT necessary for runtime, don't add those!
*/
grammar Expr;

result: expr;

expr: STRING_LITERAL # stringLiteral
   	| BOOLEAN_LITERAL # booleanLiteral
   	| NUMERIC_LITERAL # numericLiteral
	| K_NULL # nullLiteral
	| op=('-' | '+') expr # unarySign
	| OP_NOT expr # logicNot
	| expr op=CUSTOM_OP expr # customOp // custom op made of symbols is higher than other binary ops
	| <assoc=right> expr op=OP_POW expr # arithmeticOp
	| expr op=(OP_MUL | OP_DIV | OP_REMAINDER) expr # arithmeticOp
	| expr op=(OP_ADD | OP_SUB) expr # arithmeticOp
	| expr ID expr # infixFunction // function made of words (typically) is after arithmetic
	| expr op=(OP_LT | OP_GT | OP_EQ | OP_NE | OP_LE | OP_GE) expr # comparisonOp
	| expr op=OP_AND expr # logicOp
	| expr op=OP_OR expr # logicOp
	| ID '(' params=paramlist? ')' # function
	| ID # variable
	| '(' expr ')' # parens
	| '[' list=listExpr? ']' # listConstructor
	;

paramlist: funarg (',' funarg)*;
funarg: expr | ID ':' expr;

listExpr: expr (',' expr)*;

OP_LT: L T | '<';
OP_GT: G T | '>';
OP_LE: L E | '<=';
OP_GE: G E | '>=';
OP_EQ: E Q | '=' '='?;
OP_NE: N E | N E Q | '!=' | '<>';
OP_AND: A N D | '&&';
OP_OR: O R | '||';
OP_NOT: N O T | '!';
OP_ADD: '+';
OP_SUB: '-';
OP_MUL: '*';
OP_DIV: '/';
OP_REMAINDER: '%'; // reminder, not modulo, e.g. -5 % 3 is -2, not 3
OP_POW: '^';

K_NULL : N U L L;

BOOLEAN_LITERAL: T R U E
	| F A L S E
	;

// dot is allowed only because syntax does not directly support attribute paths/method calls (yet)
ID: [a-zA-Z$_][a-zA-Z0-9$_.]*;

NUMERIC_LITERAL: DIGITS ( '.' (DIGITS)? )? ( E [-+]? DIGITS )?
	| '.' DIGITS ( E [-+]? DIGITS )?
	;

DIGITS: [0-9][0-9_]*;

STRING_LITERAL : '\'' ( ~'\'' | '\'\'' )* '\'';

SPACES : [ \u000B\t\r\n] -> channel(HIDDEN) ;

COMMENT : '/*' .*? '*/' -> skip ;

LINE_COMMENT : '//' ~[\r\n]* -> skip ;

// after line comment and comment and similar things not to overshadow them
CUSTOM_OP: [#+*/<>=!|.;:?~_@$%^&-]+;

UNEXPECTED_CHAR : . ;

fragment A : [aA];
fragment B : [bB];
fragment C : [cC];
fragment D : [dD];
fragment E : [eE];
fragment F : [fF];
fragment G : [gG];
fragment H : [hH];
fragment I : [iI];
fragment J : [jJ];
fragment K : [kK];
fragment L : [lL];
fragment M : [mM];
fragment N : [nN];
fragment O : [oO];
fragment P : [pP];
fragment Q : [qQ];
fragment R : [rR];
fragment S : [sS];
fragment T : [tT];
fragment U : [uU];
fragment V : [vV];
fragment W : [wW];
fragment X : [xX];
fragment Y : [yY];
fragment Z : [zZ];
/*
SQL like expression syntax (like WHERE clause), inspired by this grammar:
https://github.com/antlr/grammars-v4/blob/master/sqlite/SQLite.g4

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

DON'T FORGET: svn add any missing *.java file (they are probably added, but just in case...)
*.tokens files are NOT necessary for runtime, don't add those!
*/
grammar Expr;

result: expr;

expr: STRING_LITERAL # stringLiteral
   	| BOOLEAN_LITERAL # booleanLiteral
   	| NUMERIC_LITERAL # numericLiteral
	| K_NULL # nullLiteral
	| op=('-' | '+') expr # unarySign
	| expr op=(OP_MUL | OP_DIV | OP_MOD) expr # arithmeticOp
	| expr op=(OP_ADD | OP_SUB) expr # arithmeticOp
	| expr op=(OP_LT | OP_GT | OP_EQ | OP_NE | OP_LE | OP_GE) expr # comparisonOp
	| expr K_IS not=K_NOT? K_NULL # isNull
	| expr op=(OP_AND | OP_OR) expr # logicOp
	| ID '(' params=paramlist? ')' # function
	| expr ID expr # infixFunction
	| expr CUSTOM_OP expr # customOp
	| ID # variable
	| '(' expr ')' # parens
	;

paramlist: funarg (',' funarg)*;
funarg: expr | ID ':' expr;

OP_LT: L T | '<';
OP_GT: G T | '>';
OP_LE: L E | '<=';
OP_GE: G E | '>=';
OP_EQ: E Q | '=' '='?;
OP_NE: N E | N E Q | '!=' | '<>';
OP_AND: A N D | '&&';
OP_OR: O R | '||';
OP_ADD: '+';
OP_SUB: '-';
OP_MUL: '*';
OP_DIV: '/';
OP_MOD: '%';

// if NULL is utilized, it should follow: https://en.wikipedia.org/wiki/Null_%28SQL%29
K_IS : I S;
K_NOT : N O T;
K_NULL : N U L L;

BOOLEAN_LITERAL: T R U E | T
	| F A L S E | F
	;

// dot is allowed only because syntax does not directly support attribute paths/method calls (yet)
ID: [a-zA-Z$_][a-zA-Z0-9$_.]*;

NUMERIC_LITERAL : DIGIT+ ( '.' DIGIT* )? ( E [-+]? DIGIT+ )?
	| '.' DIGIT+ ( E [-+]? DIGIT+ )?
	;

STRING_LITERAL : '\'' ( ~'\'' | '\'\'' )* '\'';

SPACES : [ \u000B\t\r\n] -> channel(HIDDEN) ;

COMMENT : '/*' .*? '*/' -> skip ;

LINE_COMMENT : '//' ~[\r\n]* -> skip ;

// after line comment and comment and similar things not to overshadow them
CUSTOM_OP: [#+*/<>=!|.;:?~_@$%^&-]+;

UNEXPECTED_CHAR : . ;

fragment DIGIT : [0-9];

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
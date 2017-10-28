# Expression syntax

Expression is defined in [ANTLR source file](src/main/antlr4/vexpressed/grammar/Expr.g4). This
document should be sufficient for understanding the syntax fully and write any expression. If
you're interested in grammar/language applications I strongly recommend to learn more about
[ANTLR](http://www.antlr.org/) which is undisputed king in this area, especially if we're talking
about Java applications. Its author, Terence Parr, also wrote [The Definitive ANTLR 4
Reference](https://pragprog.com/book/tpantlr2/the-definitive-antlr-4-reference) which I can
strongly recommend as well.

## Syntax definition

Expression syntax is defined by a list of rules each of them with multiple possible alternatives.
Alternatives are matched from top to bottom and first suitable alternative is used - this is
convenient way how to specify operator precedence, as we will soon see. Rules can be recursive.
This all helps the engine to parse the expression and build so called "parse tree" - a data
structure that represents the logical structure of a given expression.

Rules are printed in *italics*, keywords and other literal terminals in **bold**, alternatives are
separated by | sign and grouped by parenthesis as needed. Regular expression like constructs
are also used to define multiplicity (+ for one or more, * for zero or more), optionality (?)
and character classes enclosed in square brackets [...]. All keywords (anything printed in bold)
are case-insensitive - it doesn't matter whether you write **EQ** or **eq** or **eQ** - however
identifiers (variables and function names) are case-sensitive.

Rules are presented from top to bottom. The name of the rule on the left side is separated from its
definition on the right side with a colon. By far the most complicated - and the most important -
is the top-level "expr" rule:

* *expr:*
  * *STRING_LITERAL*
  * **true** | **false**<br/>
    boolean literals
  * *NUMERIC_LITERAL*
  * **null**<br/>
    null keyword
  * (**-** | **+**) *expr*<br/>
    unary sign (minus or plus) for numeric expressions
  * *OP_NOT expr*<br/>
    logical not operation
  * *expr CUSTOM_OP expr*<br/>
    custom binary operation between two expressions
  * *expr* **^** *expr*<br/>
    exponentiation (left-side-expr powered to right-side-expr), this operation is
    right-associative as typically expected for exponentiation
  * *expr* (**&ast;** | **/** | **%**) *expr*<br/>
    multiplication, division, reminder
  * *expr* (**+** | **-**) *expr*<br/>
    addition, subtraction﻿
  * *expr ID expr*<br/>
    infix function call (function identified by the identifier ID)
  * *expr* (*OP_LT* | *OP_GT* | *OP_EQ* | *OP_NE* | *OP_LE* | *OP_GE*) *expr*<br/>
    comparison (relational) operations
  * *expr OP_AND expr*<br/>
    logical conjunction (AND) operation
  * *expr OP_OR expr*<br/>
    logical disjunction (OR) operation
  * *ID* ( *paramlist*? )<br/>
    function call syntax (paramlist is optional)
  * *ID*<br/>
    variable reference
  * **(** *expr* **)**<br/>
    parenthesis grouping (to change calculation order as needed)
  * **[** *listExpr*? **]**<br/>
    list constructor, possibly empty
* *paramlist:*
  * *funarg* (, *funarg*)*<br/>
    list of function arguments, one or more funarg rules separated by comma
* *funarg:*
  * *expr*<br/>
    unnamed function argument (position based)
  * *ID* **:** *expr*<br/>
    named function argument
* *listExpr:*
  * *expr* (, *expr*)*<br/>
    list of one or more expressions separated by comma
* *OP_LT:* **lt** | **<**
* *OP_GT:* **gt** | **>**
* *OP_LE:* **le** | **<=**
* *OP_GE:* **ge** | **>=**
* *OP_EQ:* **eq** | **=** | **==**<br/>
  both = and == are valid, there is no assignment construct in the expression syntax that could
  cause confusion
* *OP_NE:* **ne** | **neq** | **!=** | **<>**
* *OP_AND:* **and** | **&&**
* *OP_OR:* **or** | **||**
* *OP_NOT:* **not** | **!**
* *ID:* [a-zA-Z$_][a-zA-Z0-9$_.]*<br/>
  one or more characters, first must be any letter, dollar or underscore, other characters
  can be letters, numbers, dollar, underscore or dot
* *NUMERIC_LITERAL:*
  * *DIGITS* ( **.** (*DIGITS*)? )? ( **e** [-+]? *DIGITS* )?<br/>
    numbers starting with digit(s), e.g. `6`, `6.`, `6.4`, `6e3`, `6e-3`, `6e+3`, `6.4e-3`
    or `6.e-3`
  * **.** *DIGITS* ( **e** [-+]? *DIGITS* )?<br/>
    numbers starting a decimal point, e.g. `.4` or `.4e-3`
* *DIGITS:* [0-9][0-9_]*<br/>
  string of digits, must start with a digit, any other position can also contain an underscore
  to allow readable long literals (like `2_000_000`)
* *STRING_LITERAL:* **'** ( ~['] | **''** )* **'**<br/>
  string enclosed in apostrophes, any apostrophe in the string must be doubled (`~[']` means
  "anything except a single apostrophe")
* *COMMENT:* **/&ast;** .*? **&ast;/**<br/>
  multi-line comment (comments are ignored from evaluation, of course), it is recommended to
  use whitespace before the beginning of the comment
* *LINE_COMMENT:* ** #** ~[\r\n]*<br/>
  line comment, anything after # up to the first line break is ignored, it is recommended to
  use whitespace before the beginning of the comment
* *CUSTOM_OP:* [#+*/<>=!|.;:?~_@$%^&#-]+<br/>
  any combination of these symbols (except for cases introduced earlier) can be used as custom
  operation that is mapped to function names in a special manner (reserved for future use)

Whitespaces (spaces, tabs, newline characters) between tokens are ignored for evaluation purposes
but can help separate tokens (e.g. double unary operator that would otherwise be treated as
a custom operator). This also allows for multi-line expressions which may be more readable for
complicated cases or when combined with comments.

## Examples

Going through the expr rule we see that the following are valid expressions:

* any string: `'this is a string'`
* any boolean literal: `true` or `TRUE` or `FaLsE` (remember that keywords are case-insensitive)
* any number (valid according to *NUMERIC_LITERAL*): `1` or `0.` or `.0` or `0.31415E+01`
  or `3_141.5_E-3_` (it is possible but not recommended to use underscores after decimal point or
  in exponent part; also note that underscore at the start does not result in number)
* null literal: `null` or `NULL`
* number with unary sign: `-5` or with space `- 5` or `- -5` (double unary sign must be separated,
  otherwise it collides with custom operator) or `-(-5)` or `+5`
* logical not: `! true` or `! !false` (two ! must be separated not to collide with custom operator)
  or `!(!false)` or `not!true` (is true, here `!` separates `not` and `true` and there is no
  confusion for the parser although this is not really recommended)
* custom expressions are reserved for future use and not covered here
* exponentiation: `2^2` (returns 4) or `2^3^2` (right-associative, this equals to `2^9`, not `8^2`)
* multiplication, division, reminder: `2*3` or `2/5` or `-5 % 3` (returns -2)
* plus, minus: `2+3` or `2-3`<br/>
  these have lower precedence than previous operations (as expected) `2 + 3 * 3` returns 11, not 15
* infix function call: `listVariable contains 5`<br/>
  here `listVariable` is an identifier of a variable and we assume it contains a list, `contains`
  is a built-in function and `5` is a number - this naturally returns true, if the collection
  in the `listVariable` contains 5
* relational operations (all return boolean values true or false): `5 < 3` or `4 neq 5` or
  `valueDate >= '2016-09-01'` (compares date variable with string containing ISO date which
  is supported, see [supported data types](data-types.md))
* binary logical operations: `true and not false` or `true and false or true and false`
  (returns false because and has higher precedence)
* function call: `sqrt(4)` or `someFun(a: 4, b: 5)`, latter using named parameters (see
  [how to define functions](functions.md))
* variable reference as shown in examples above already (see [how variables work]) 
* parenthesis around any other expression to assure order of computation: `(x || y) && z`
  or `(2 + 3) * 3`
* list constructor: `[1, 2, 3]` or in more complex expression `5 in [1, 2, 3]` (where `in` is
  a function called with infix style)

Note that beyond list construction there is no special support for lists in expression evaluation.
They are mostly useful when combined with functions (like `in` or `contains`). Elements of the
list can be each of any type, list is not parametrized in any way (as in Java generics). All these
aspects must be considered when implementing custom functions that take in lists.

 
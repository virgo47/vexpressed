# Vexpressed

"Vexed and pressed? Use Vexpressed expression library!"

Simple to use expression evaluator and checker with support for variables and custom functions.
Built with ANTLR 4.


## Features

* Basic mathematical and logical/relational operations.
* Supported types: `INTEGER`, `DECIMAL`, `STRING`, `BOOLEAN`, `DATE`, `DATETIME`, `TIMESTAMP`
and `OBJECT`
* Simple interfaces for variable resolution and custom function execution.
* Convenient classes for definition of variable resolution.
* Easy to wrap around any Java object for variable resolution (and super-easy for `Map`).

Missing but considered for future versions:

* Operator overloading
* Method calling

## Core classes structure

![UML Class diagram - core](docs/imgs/core-classes.png)

## Examples

## TODO

* ExpressionValidatorVisitor should return not just ExpressionType, but our type + real Java type
(this allows to do better checking for comparison operator for instance)
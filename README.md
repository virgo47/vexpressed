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
* Custom operators (combination of +, -, *, /, ^, %, ...).
* Based on Java 8 and using lambdas a lot.

Missing but considered for future versions:

* Operator overloading (e.g. plus for other types of objects).
* Method calling on objects.
* Attribute path resolution.


## Why does it exist?

We needed something like this for our project and various expression languages seemed to heavy
for us. It also gave us full control over the grammar and opportunity to learn ANTLR 4.


## Core classes structure

![UML Class diagram - core](docs/imgs/core-classes.png)

## Examples

Following examples can be found in [Scrapbook.java](src/test/java/Scrapbook.java).

### Basics

Let's start with something very simple - adding two numbers together (this prints 11):
```
System.out.println("5+6 = " + VexpressedUtils.eval("5+6", NULL_VARIABLE_RESOLVER, null));
```

`VexpressedUtils.eval` does all the job for you, but because this library is not for calculating
constant results it expect you to provide some `VariableResolver` (we provide dummy
`NULL_VARIABLE_RESOLVER` here, it cannot be null) and `FunctionExecutor` (this, for a change, can
be `null`).

Normal expected precedence rules apply (power is right associative):
```
2+2^2^3/16 // returns 18 and is equivalent to: 2 + ((2^(2^3)) / 16)
```

### Variables

Let's use some variables - this is the reason why expressions are useful because variables can
change from invocation to invocation of the same expression. *Vexpressed* highest abstraction for
variable resolution is called `VariableResolver` - it takes variable name in and returns its value
(`Object`) out. In this example, any variable will return 2 and the resolver is implemented as
lambda:
```
eval("a * 3", var -> 2, null) // returns 6, because a (or any other variable) is 2
```

Of course we don't want all variables to be the same, but for that we have to define what variable
returns what. Let's say we have an object of type `Rectangle` with fields `a` and `b` and we want
to use these as variables. Firstly, we define the mapper for these variables:
```
VariableMapper<Rectangle> rectangleMapper = new VariableMapper<Rectangle>()
	.define("a", ExpressionType.INTEGER, o -> o.a)
	.define("b", ExpressionType.INTEGER, o -> o.b)
	.finish();
```

Now we can use the same mapper for the same expression, but we call `resolverFor` on the mapper
using different rectangles - this returns `VariableResolver` that resolves variables as defined
in the mapper, but "closes over" different instances and returns different values - as expected:
```
eval("a * b", rectangleMapper.resolverFor(new Rectangle(3, 4)), null) // returns 12
eval("a * b", rectangleMapper.resolverFor(new Rectangle(2, 5)), null) // returns 10
```

You can always roll in your own implementation of `VariableResolver` with the support around it,
some if it can be done easily with lambda. For instance, if you want to provide variables in
a `Map` you can simply implement it as `var -> map.get(var)`. 

### Functions

Just like with variables, functions are executed by a single very simple abstraction.
`FunctionExecutor` returns an `Object` (return value) taking function name and actual
arguments as input. Let's first implement something dummy - whatever the name of function we use
we always get the sum of the arguments (assuming they are integers).
```
eval("fun(1, 2, 3) + anyotherfun(4, 5)", NULL_VARIABLE_RESOLVER,
	(name, numbers) -> numbers.stream().mapToInt(o -> (Integer) o.value).sum());
```
This produces 15, as expected.

Using `FunctionMapper` is similar to how `VariableMapper` complements `VariableResolver`.
First we implement our function using a normal Java method spiced up with some annotations:
```
@ExpressionFunction
public static Integer sum(@FunctionParam(name = "nums") Collection<Integer> ints) {
	return ints.stream().mapToInt(i -> i).sum();
}
```

Then we can use this code:
```
FunctionMapper functionMapper = new FunctionMapper()
	.scanForFunctions(Scrapbook.class);
eval("sum([1, 2, 3, 4])", NULL_VARIABLE_RESOLVER, functionMapper.executor());
```
Obviously, we can construct function mapper only once and keep it in some field or so.

For more about functions see [this document](docs/functions.md).

### Custom operators

TODO

### `BaseExpressionEvaluator` for the rescue

TODO: the idea behind defining functions, custom operator handler and then calling it with
various `VariableMappers`

## TODO

* ExpressionValidatorVisitor should return not just ExpressionType, but our type + real Java type
(this allows to do better checking for comparison operator for instance)
# Vexpressed

"Vexed and pressed? Use Vexpressed expression library!"

Simple to use expression evaluator and checker with support for variables and custom functions.
Built with ANTLR 4. The story of this library started with blog posts on Expression evaluation in
Java:
[1](https://virgo47.wordpress.com/2015/08/19/expression-evaluation-in-java-1/),
[2](https://virgo47.wordpress.com/2015/09/15/expression-evaluation-in-java-2/),
[3](https://virgo47.wordpress.com/2015/09/27/expression-evaluation-in-java-3/) and
[4](https://virgo47.wordpress.com/2015/11/12/expression-evaluation-in-java-4/).


## Features

* Basic mathematical and logical/relational operations.
* Supported types: `INTEGER`, `DECIMAL`, `STRING`, `BOOLEAN`, `DATE`, `DATETIME`, `TIMESTAMP`
and `OBJECT`
* Simple interfaces for variable resolution and custom function execution.
* Convenient classes for definition of variable resolution.
* Easy to wrap around any Java object for variable resolution (and super-easy for `Map`).
* Custom operators (combination of +, -, *, /, ^, %, ...).
* Requires Java 8.

Missing but considered for future versions:

* Operator overloading (e.g. plus for other types of objects).
* Method calling on objects.
* Attribute path resolution.


## Why does it exist?

We needed something like this for our project and various expression languages seemed to heavy
for us. It also gave us full control over the grammar and opportunity to learn ANTLR 4.


## Core classes structure

![UML Class diagram - core](docs/imgs/core-classes.png)


## Syntax

Expression syntax can be found in [ANTLR source file](src/main/antlr4/com/virgo47/vexpressed/grammar/Expr.g4)
and is also explained in [here](docs/syntax.md). But let's see some examples first.


## Examples

Following examples can be found in [Scrapbook.java](src/test/java/Scrapbook.java).

### Basics

We'll start with something very simple - adding two numbers together (this prints 11):
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

It's time to use some variables - this is the reason why expressions are useful because variables
can change from invocation to invocation of the same expression. *Vexpressed* highest abstraction
for variable resolution is called `VariableResolver` - it takes variable name in and returns its
value (`Object`) out. In this example, any variable will return 2 and the resolver is implemented
as lambda:
```
eval("a * 3", var -> 2, null) // returns 6, because a (or any other variable) is 2
```

Of course we don't want all variables to be the same. We will use `VariableBinding` which is simple
`VariableResolver` implementation that is based internally on `Map`.
```
VariableBinding varResolver = new VariableBinding()
	.add("a", 3)
	.add("b", 6);
eval("a * b", varResolver, null);
```

There are other, more declarative ways how to define mapping from variables names to class fields
(or anything, really). Provided `VariableMapper` allows us to describe how to obtain variables
from an object of a specific type. Later we can ask the mapper for a `VariableResolver` based
on mapper's configuration and a particluar "evaluation context" (of that specfic type).

For more about variables see [this document](docs/variables.md).

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

Using `FunctionMapper` is similar to how `VariableMapper` complements `VariableResolver`. It
acts as a definiton of various functions. First we implement our function using a normal Java
method spiced up with some annotations:
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

### `BaseExpressionEvaluator` for the rescue

We want more from our expression solution. For concrete use case we want to be able to specify
what variables and functions will be available and we also want to be able to verify that the
expression is valid (before we let user to save it, for example). This always start with
a particular place where we want to use it (use case).

Instead of using `VexpressedUtils.eval` which must be always provided both variable and function
resolvers we may use `BaseExpressionEvaluator` which contains its `FunctionMapper`. Methods
`addFunctionsFrom(delegate)` and `addFunction` allow us to initialize the evaluator with proper
mix of supported functions. Evaluator also by default cache parse trees for most used expressions:
```
expressionEvaluator = new BaseExpressionEvaluator()
	.addFunctionsFrom(this);
```

Here we create evaluator that caches 50 most recently used expressions (only their parse trees,
not their results, of course!) and scans `this` object for defined functions. In this case we
use simple logic that relevant functions are defined close to the point where they are used.

Note that the internal function mapper is initialized with predefined functions from
[BasicFunctions](src/main/java/com/virgo47/vexpressed/BasicFunctions.java).

When we want to evaluate an expression we simply call:
```
result = expressionEvaluator.eval(expression, variableResolver);
```

We provide variable resolver for each expression invocation, but because in many cases we want
to use `VariableMapper` for our variable definitions (see [Variables](docs/variables.md)) we
can use another pre-cooked evaluator.

### `VariableMapperExpressionEvaluator`

`VariableMapperExpressionEvaluator` works similar to `BaseExpressionEvaluator` but we provide
`VariableMapper` to its constructor. Because `VariableMapper` defines how to get variable values
from a provided object (evaluation context, specific to each eval execution) we dont't provide
`VariableResolver` into `eval` method - rather we provide the evaluation context object from which
the values will be extracted during `eval`:
```
result = expressionEvaluator.eval(expression, contextObject);
```

`VariableMapper` will do the rest for us (see [Variables](docs/variables.md) for details).
This evaluator also has `expressionMetadata()` method that returns `ExpressionMetadata`.
This allows client code to offer suggestions for variable or function names and types, and for
function it also describes their parameters (name, type).

We can also check the expression while it is created (e.g. on UI) without evaluating it (which we
cannot as we don't know how to get concrete values for the variables):
```
ExpressionType resultType = expressionEvaluator.check(expression);
```

If the expression is invalid it will throw an exception. For more about expression validations
and metadata see [this document](docs/validation.md).


## Open for discussion

* ExpressionValidatorVisitor should return not just ExpressionType, but our type + real Java type
(this allows to do better checking for comparison operator for instance)
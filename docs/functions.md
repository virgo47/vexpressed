# Functions

## `FunctionExecutor` abstraction

Functions are executed by a single very simple abstraction.
`FunctionExecutor` returns an `Object` (return value) taking function name and actual
arguments as input. Let's first implement something dummy - whatever the name of function we use
we always get the sum of the arguments (assuming they are integers).
```
eval("fun(1, 2, 3) + anyotherfun(4, 5)", NULL_VARIABLE_RESOLVER,
	(name, numbers) -> numbers.stream().mapToInt(o -> (Integer) o.value).sum());
```
This produces 15, as expected.

## `FunctionMapper` groups function definitions

We probably want something more useful and this is where `FunctionMapper` comes - which is similar
to how `VariableMapper` complements `VariableResolver`. First we implement our function using
a normal Java method spiced up with some annotations:
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
eval("sum(nums: [1, 2, 3, 4])", NULL_VARIABLE_RESOLVER, functionMapper.executor());
```
Obviously, we can construct function mapper only once and keep it in some field or so. When calling
the function we can also specify the name of the parameter and even change its order - unnamed
arguments will be used positionally against any unused parameter names. There is one difference
here to notice - we had to wrap the numbers into a list because `FunctionMapper` currently does not
support varargs. (Vararg support itself does not affect our expression grammar but it needs to
be added to the implementation of the function execution.)

Another thing to notice is that we scanned for functions on a class (`Scrapbook.class`)
but we can also scan on an instance. This allows us to run functions implemented even on managed
components, accessing database, etc.

## Using `VariableResolver` inside the function

Finally, sometimes we want to create a specific function that uses some variable value. You can
always pass it as an argument, but what if you don't want? What if you want to use it implicitly?
All you have to do is call the `executor` method on the `FunctionMapper` with additional
`VariableResolver` parameter like this: `functionMapper.executor(variableResolver)`.
Following example is rather contrive but this mechanism can be useful to make expressions in some
situations more fluent when the function name implies the usage of the variables.
```
// This time we map non-annotated function, first is name of the function (in expression)
// then the implementing class/object (delegate), name of the method and then method
// parameter types. VariableResolver parameter is technical and not counted.
functionMapper = new FunctionMapper().registerFunction(
	"isLargerThan", Scrapbook.class, "areaLargerThan", VariableResolver.class, int.class);

VariableResolver varResolver = rectangleMapper.resolverFor(new Rectangle(3, 4));
// function is used only with one argument, the area we compare the actual area with
eval("isLargerThan(10)", varResolver, functionMapper.executor(varResolver));
varResolver = rectangleMapper.resolverFor(new Rectangle(3, 2));
eval("isLargerThan(10)", varResolver, functionMapper.executor(varResolver));

...
public static boolean areaLargerThan(VariableResolver variableResolver, int threshold) {
	int a = (int) variableResolver.resolve("a");
	int b = (int) variableResolver.resolve("b");
	return a * b > threshold;
}
```

First eval returns true because the rectangle area is 12 (larger than 10), second false (actual
area is 6).

## Summary

"Injecting" functions into your expressions is very easy and flexible
enough. You can use provided mapper or roll in your own `FunctionExecutor` implementation
altogether.
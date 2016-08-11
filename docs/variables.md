# Variables

*Vexpressed* highest abstraction for variable resolution is called `VariableResolver` - it takes
variable name in and returns its value (`Object`) out. In this example, any variable will return
2 and the resolver is implemented as lambda:
```
eval("a * 3", var -> 2, null) // returns 6, because a (or any other variable) is 2
```


## `VariableBinding`

When we want to map concrete values to variable names we can do it easily with `VariableBinding`: 
```
eval("a * b", new VariableBinding().add("a", 3).add("b", 6), null);
```

If we have the values in a map already we can use `binding.addAll(myMap)` (input map type is
`Map<String, Object>`). Implementing `VariableResolver` for a map is a matter of a simple lambda
expression, but `VariableBinding` also allows to combine map with further values.

`VariableBinding` is simple to use but does not allow to specify mapping of variable extraction
from an object, it must be done explicitly in the code before you pass it for expression
evaluation. It also does not help us with expression introspection and related validation.
For that we use more sophisticated `VariableMapper`.


## `VariableMapper`

Sometimes we want to map variable resolution to an object of some type (or more objects with
specific structure). We may want to define how to get value for a variable without using any
particular instance. For example, let's say we have an object of type `Rectangle` with fields
`a` and `b` and we want to use these as variables. Firstly, we define the mapper for these
variables:
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

With `VariableMapper` we don't need to mine the information from the input object, the resolver
provided by the mapper will do it for us. The mapper object also contains additional metadata
about the variables which can be further used for [expression validation](validation.md).


## Other implementations

You can always roll in your own implementation of `VariableResolver` with the support around it,
some of it can be done easily with lambda. For instance, if you want to provide variables in
a `Map` you can simply implement it as `var -> map.get(var)`.
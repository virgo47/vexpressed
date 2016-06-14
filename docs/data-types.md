# Data types

Syntax is only part of the expression language - we also need to know what data types
are usable in what contexts. In examples of valid expressions after the list of syntax
rules we saw that we can work with strings, numbers and boolean (true/false) values,
but this is not all. In general expressions can work with any type supported by the
Java language.

Expressions support these types in a special way:

* `STRING` - like literal 'This is a string';
* `BOOLEAN` - literals true/false and also as a result of relational and logical operations;
* `INTEGER` - integer number (within certain limits ~±2 billion, or to be precise it is 32-bit
signed integer, which ranges from -231 to 231-1), e.g. literal 47;
* `DECIMAL` - any other number, these are all decimal numbers or any integers out of INTEGER
range, e.g. 3.14, but also 0.0 (because we used decimal point);
* `DATE`, `DATE_TIME`, `TIMESTAMP` - temporal types, date/date and time typically represent
local date(time), while timestamp is world-wide moment in time;
* `OBJECT` - any other object type - expression respects the type, and while it doesn't work
well in most operations, it can still be used in function calls or as a return value.

Let's see what operations are supported for each type. Any type can be used as function
call argument (both for classic and infix style), can be returned by a function call
or as a value of a variable, and any type can also be returned by the expression. For this
reason we don't mention these cases in the next sections and focus only on specific
support for built-in operations.

## STRING support

String supports binary `+` which returns concatenations of the two strings. In fact,
if the left side of the addition is a `STRING`, right side is converted to a string and
concatenation of these two strings is returned. This means that any type can be on
the right side. Null value on the right is considered an error and fails during
evaluation (at runtime).

Examples:

* `'a' + 'b'` returns `'ab'`
* `'a' + 47` returns `'a47'`

In both cases the returned type is `STRING`.

## BOOLEAN support

Logical values `true` and `false` are built-in literals (case-insensitive) and also the only
two possible values of the BOOLEAN type. Functions and variables producing boolean may
technically also return null value, but that would produce error in most operations.

Boolean is a result of any relational operation (`eq`, `ne`/`neq`, `gt`, `lt`, `ge`, `le`)
and also the only allowed type for operands and a return type of logical operations
(`not`, `and`, `or`).

## INTEGER and DECIMAL support

Numbers can be used on both sides of arithmetic and logical operations. Mixing of
`INTEGER` and `DECIMAL` is not a problem, integer is promoted to decimal if necessary.
If any side of the operation is decimal, the whole operation is treated as decimal.
There are, however, differences between integer and decimal operations:

* Addition, subtraction and multiplication does expected things in all contexts.
The only surprise may happen when the result of integer operation would overflow
integer range - in that case decimal result is returned.
* Division operation between two integer types produces integer result (rounded down,
known as "floor" operation), decimal division produces decimal results up to
configured precision.
* Power operation between two integers produces integer result, or decimal result
when out of integer range. Otherwise it produces decimal result. If right side is
integer the calculation is as expected, if right side is decimal than both sides
are converted to IEEE 754 double precision and power is calculated on these, result is
converted back to decimal. These details should not affect result of a single
exponentiation significantly and are provided here only for completeness.

> :warning: Note that automatic result promotion after integer operation overflow may
> bring correct, but surprising results. In integer range `(a+a)/(a+1)` returns `1` and
> the result of `%` (remainder) operation would be `a-1`. But if overflow switches first
> `a+a` to decimal the result for division is also decimal - something very close to `2`,
> depending on the chosen precision. Reminder result is still as expected. This can be
> mitigated by using `asInteger(...)` function around the problematic part of expression
> if we insist that on integer division and we are sure the result fits into integer range.

## Number precision

Number precision is important for decimal calculation and describes how many numbers after
decimal point are used. It is not possible to use unlimited precision because expressions
like `1/3` would produce infinitely long number. Internally 15 decimal places are preserved
between operations and the result is rounded to final 6 decimal numbers which should be
sufficient for most needs.

Both these precisions can be configured on `ExpressionCalculatorVisitor`:
```
visitor = new ExpressionCalculatorVisitor(variableResolver)
  .maxScale(20)
  .maxResultScale(10);
```

If using `BaseExpressionEvaluator` you have to subclass it and override `adjustCalculatorVisitor`
where you call `max(Result)Scale` methods on the provided calculator visitor instance.

## Temporal types

Temporal types are `DATE` (for local date without time zone), `DATE_TIME` (for local date
and time) and `TIMESTAMP` (worldwide valid moment in time). For all types `+` and `-` is
supported to add/subtract specific time interval (days or more, hours or smaller intervals
are not supported and mostly not that useful). There are two ways how to adjust value
of temporal type:

* When the right side is an integer, days are used. E.g. `someDate + 2` returns value two
days in the future from the original `someDate` value.
* When the right side is a string other units can be specified with a letter after number
like `date + '2m'`. D (days), W (weeks), M (months) and Y (years) are supported, casing is
ignored. It is not possible to combine multiple interval letters in a single string -
e.g. `date+'2m3w'` is NOT supported, use `date + '2m' + '3w'` instead.

In any case the type of the returned value is the same as the type on the left side.

Relational operations are also supported between the same type of temporal values,
mixing types causes runtime failure (expression will not be evaluated). There is no support
for data literals (yet) but you can 

## Other types

Values of other types are not supported by any operations, but are respected and not
converted automatically - except for various number types. These are narrowed to either
`INTEGER` or converted to `DECIMAL` if too big (or in fact decimal) whenever returned
by a function or when resolved as a variable value.

Any types that can be compared (implementing Java `Comparable`) can also be compared
using relational operations but types on both sides must be the same, otherwise runtime
failure during expression evaluation occurs.
# Using PMD

Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset (see the [pmd install instruction](./pmd-help.md)). Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false positive). Explain why you would not solve this issue.

## Answer

We choose the Commons-cli project, here is an example of what it founds :

In PatternOptionBuilder line 133 we have a ``	Switch statements or expressions should be exhaustive, add a default case (or missing enum branches)`` : 
```java
public static Class<?> getValueType(final char ch) {
        switch (ch) {
        case '@':
            return OBJECT_VALUE;
        case ':':
            return STRING_VALUE;
        case '%':
            return NUMBER_VALUE;
        case '+':
            return CLASS_VALUE;
        case '#':
            return DATE_VALUE;
        case '<':
            return EXISTING_FILE_VALUE;
        case '>':
            return FILE_VALUE;
        case '*':
            return FILES_VALUE;
        case '/':
            return URL_VALUE;
        }

        return null;
    }
```

We can solve it if we replace the return null with a default return statement : 
```java
public static Class<?> getValueType(final char ch) {
        switch (ch) {
        case '@':
            return OBJECT_VALUE;
        case ':':
            return STRING_VALUE;
        case '%':
            return NUMBER_VALUE;
        case '+':
            return CLASS_VALUE;
        case '#':
            return DATE_VALUE;
        case '<':
            return EXISTING_FILE_VALUE;
        case '>':
            return FILE_VALUE;
        case '*':
            return FILES_VALUE;
        case '/':
            return URL_VALUE; 
            
            default : return null;
        }
 }
```
---
We can found other problem that we can't solve (false positive), for example in the class HelpFormatter.java line 699 we 
have this problem : ``	Ensure that resources like this PrintWriter object are closed after use``, but if we look at the 
code we can see that the class is deprecated. 
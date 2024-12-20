# Extending PMD

Use XPath to define a new rule for PMD to prevent complex code. The rule should detect the use of three or more nested `if` statements in Java programs so it can detect patterns like the following:

```Java
if (...) {
        ...
        if (...) {
        ...
        if (...) {
        ....
        }
        }

        }
```
Notice that the nested `if`s may not be direct children of the outer `if`s. They may be written, for example, inside a `for` loop or any other statement.
Write below the XML definition of your rule.

You can find more information on extending PMD in the following link: https://pmd.github.io/latest/pmd_userdocs_extending_writing_rules_intro.html, as well as help for using `pmd-designer` [here](https://github.com/selabs-ur1/VV-ISTIC-TP2/blob/master/exercises/designer-help.md).

Use your rule with different projects and describe you findings below. See the [instructions](../sujet.md) for suggestions on the projects to use.

## Answer
- Xpath : `//IfStatement//IfStatement//IfStatement`
- Rule : [NestingRule.xml](../code/Exercise3/NestingRule.xml)
- Command : `pmd check -f html -R code/Exercise3/NestingRule.xml -d codebase/commons-cli/ -r report.html`
- Result : 81 nested if found in [commons-cli](https://github.com/apache/commons-cli) project ([report.html](../code/Exercise3/report.html))


#### AST problem
In this example the code is represented in AST as nested if statements.
```
public class A {

    public void a() {

        if (true) {
             // ...
        } else if (true) {
             // ...
        } else if (true) {
             // ...
        } else {
            // ...
        }
    }
}
```
```
<?xml version='1.0' encoding='UTF-8' ?>
<CompilationUnit Image='' PackageName=''>
    <ClassDeclaration Abstract='false' Annotation='false' Anonymous='false' BinaryName='A' CanonicalName='A' EffectiveVisibility='public' Enum='false' Final='false' Image='' Interface='false' Local='false' Nested='false' PackageName='' Record='false' RegularClass='true' RegularInterface='false' SimpleName='A' Static='false' TopLevel='true' UnnamedToplevelClass='false' Visibility='public'>
        <ModifierList EffectiveModifiers='[public]' ExplicitModifiers='[public]' Image='' />
        <ClassBody Empty='false' Image='' Size='1'>
            <MethodDeclaration Abstract='false' Arity='0' EffectiveVisibility='public' Final='false' Image='' MainMethod='false' Name='a' Overridden='false' Static='false' Varargs='false' Visibility='public' Void='true'>
                <ModifierList EffectiveModifiers='[public]' ExplicitModifiers='[public]' Image='' />
                <VoidType Image='' />
                <FormalParameters Empty='true' Image='' Size='0' />
                <Block Empty='false' Image='' Size='1' containsComment='false'>
                    <IfStatement Else='true' Image=''>
                        <BooleanLiteral CompileTimeConstant='true' Expression='true' Image='' LiteralText='true' ParenthesisDepth='0' Parenthesized='false' True='true' />
                        <Block Empty='true' Image='' Size='0' containsComment='true' />
                        <IfStatement Else='true' Image=''>
                            <BooleanLiteral CompileTimeConstant='true' Expression='true' Image='' LiteralText='true' ParenthesisDepth='0' Parenthesized='false' True='true' />
                            <Block Empty='true' Image='' Size='0' containsComment='true' />
                            <IfStatement Else='true' Image=''>
                                <BooleanLiteral CompileTimeConstant='true' Expression='true' Image='' LiteralText='true' ParenthesisDepth='0' Parenthesized='false' True='true' />
                                <Block Empty='true' Image='' Size='0' containsComment='true' />
                                <Block Empty='true' Image='' Size='0' containsComment='true' />
                            </IfStatement>
                        </IfStatement>
                    </IfStatement>
                </Block>
            </MethodDeclaration>
        </ClassBody>
    </ClassDeclaration>
</CompilationUnit>
```
Alternative try :
```
//IfStatement[ancestor::IfStatement[ancestor::IfStatement]]
```
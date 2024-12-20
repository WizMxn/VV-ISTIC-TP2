package fr.istic.vv;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.stmt.*;

import com.github.javaparser.ast.visitor.GenericVisitorWithDefaults;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


// This class visits a compilation unit and
// prints all public enum, classes or interfaces along with their public methods
public class CyclomaticComplexity extends GenericVisitorWithDefaults<Integer, Void> {


    @Override
    public Integer visit(CompilationUnit unit, Void arg) {
        for (TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }
        return 0;
    }

    @Override
    public Integer defaultAction(Node n, Void arg) {
        return 0;
    }


    @Override
    public Integer visit(ClassOrInterfaceDeclaration declaration, Void arg) {
        List<MethodDeclaration> methods = declaration.getMethods();

        String report = methods.stream()
                .map(methodDeclaration ->
                        "\n - " + methodDeclaration.getName().asString() + " : " + methodDeclaration.accept(this, arg))
                .collect(Collectors.joining());

        System.out.println("Class " + declaration.getName().asString() + " : " + report);

        return 0;
    }

    @Override
    public Integer visit(MethodDeclaration declaration, Void arg) {
        Optional<BlockStmt> body = declaration.getBody();
        return body.map(blockStmt -> blockStmt.accept(this, arg) + 1).orElse(1);
    }

    @Override
    public Integer visit(BlockStmt blockStmt, Void arg) {
        return blockStmt.getStatements()
                .stream()
                .map(stm -> stm.accept(this, arg))
                .reduce(Integer::sum)
                .orElse(0);
    }

    @Override
    public Integer visit(IfStmt ifStmt, Void arg) {

        int res = ifStmt.getThenStmt().accept(this, arg) + 1 ;

        Optional<Statement> elseStmt = ifStmt.getElseStmt();
        if (elseStmt.isPresent()) {
            res += elseStmt.get().accept(this, arg) ;
        }

        return res;
    }

    @Override
    public Integer visit(WhileStmt whileStmt, Void arg) {
        return whileStmt.getBody().accept(this, arg) + 1;
    }

    @Override
    public Integer visit(ForStmt forStmt, Void arg) {
        return forStmt.getBody().accept(this, arg) + 1;
    }

    @Override
    public Integer visit(ForEachStmt forEachStmt, Void arg) {
        return forEachStmt.getBody().accept(this, arg) + 1;
    }

    @Override
    public Integer visit(DoStmt doStmt, Void arg) {
        return doStmt.getBody().accept(this, arg) + 1;
    }

    @Override
    public Integer visit(SwitchStmt switchStmt, Void arg) {
        final Optional<Integer> sum = switchStmt.getEntries()
                .stream()
                .map(entry -> entry.accept(this, arg) + 1)
                .reduce(Integer::sum);
        return sum.orElse(0);
    }


}

package fr.istic.vv;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PublicElementsPrinter extends VoidVisitorWithDefaults<Void> {

    private final List<String[]> results = new ArrayList<>(); // To store field-class pairs for the Markdown table

    @Override
    public void visit(CompilationUnit unit, Void arg) {
        for (TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }
    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
        if (!declaration.isPublic()) return;

        // Process nested types
        for (BodyDeclaration<?> member : declaration.getMembers()) {
            if (member instanceof TypeDeclaration)
                member.accept(this, arg);
        }

        // Check private fields and their getters
        if (declaration instanceof ClassOrInterfaceDeclaration) {
            ClassOrInterfaceDeclaration clazz = declaration.asClassOrInterfaceDeclaration();
            List<FieldDeclaration> privateFields = clazz.getFields().stream()
                    .filter(FieldDeclaration::isPrivate)
                    .collect(Collectors.toList());

            List<MethodDeclaration> publicMethods = clazz.getMethods().stream()
                    .filter(MethodDeclaration::isPublic)
                    .collect(Collectors.toList());

            checkFieldsForGetters(clazz.getFullyQualifiedName().orElse("[Anonymous]"), privateFields, publicMethods);
        }
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration declaration, Void arg) {
        visitTypeDeclaration(declaration, arg);
    }

    @Override
    public void visit(EnumDeclaration declaration, Void arg) {
        visitTypeDeclaration(declaration, arg);
    }

    private void checkFieldsForGetters(String className, List<FieldDeclaration> privateFields, List<MethodDeclaration> publicMethods) {
        privateFields.forEach(privateField -> {
            if (!hasGetter(privateField, publicMethods)) {
                // Store field name and class name for the Markdown table
                results.add(new String[]{privateField.getVariable(0).getNameAsString(), className});
            }
        });
    }

    private boolean hasGetter(FieldDeclaration field, List<MethodDeclaration> publicMethods) {
        String fieldName = field.getVariable(0).getNameAsString();
        String getter = "get" + StringUtils.capitalize(fieldName);

        return publicMethods.stream().anyMatch(method ->
                method.getNameAsString().equals(getter));
    }

    public String generateMarkdownTable() {
        if (results.isEmpty()) {
            return "No private fields without getters found.";
        }

        StringBuilder markdown = new StringBuilder();
        markdown.append("| Private Field | Class Name | Package Name |\n");
        markdown.append("|---------------|------------|--------------|\n");

        for (String[] result : results) {
            markdown.append("| ").append(result[0]).append(" | ").append(result[1]).append(" |\n");
        }

        return markdown.toString();
    }
}

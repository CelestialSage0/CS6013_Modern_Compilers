package visitor;

import syntaxtree.*;
import visitor.*;
import java.util.*;

/**
 * First-pass visitor: populates a Map<String,ClassInfo>.
 *
 * R = String — type names / identifiers bubbled up
 * A = Map<String,ClassInfo> — the class table being built
 */
public class BuildTableVisitor extends GJDepthFirst<String, Map<String, ClassInfo>> {

    private ClassInfo currentClass = null;
    private MethodInfo currentMethod = null;
    private boolean inMethodLocals = false;

    // ------------------------------------------------------------------
    // Goal
    // ------------------------------------------------------------------
    @Override
    public String visit(Goal n, Map<String, ClassInfo> tbl) {
        n.f0.accept(this, tbl); // MainClass
        n.f1.accept(this, tbl); // ( TypeDeclaration() )*
        return null;
    }

    // ------------------------------------------------------------------
    // MainClass — register it; no methods to collect
    // ------------------------------------------------------------------
    @Override
    public String visit(MainClass n, Map<String, ClassInfo> tbl) {
        String name = n.f1.accept(this, tbl);
        currentClass = new ClassInfo(name, null);
        tbl.put(name, currentClass);
        return null;
    }

    // ------------------------------------------------------------------
    // ClassDeclaration
    // ------------------------------------------------------------------
    @Override
    public String visit(ClassDeclaration n, Map<String, ClassInfo> tbl) {
        String name = n.f1.accept(this, tbl);
        currentClass = new ClassInfo(name, null);
        tbl.put(name, currentClass);
        n.f3.accept(this, tbl); // ( VarDeclaration() )* — class fields
        n.f4.accept(this, tbl); // ( MethodDeclaration() )*
        return null;
    }

    // ------------------------------------------------------------------
    // ClassExtendsDeclaration
    // ------------------------------------------------------------------
    @Override
    public String visit(ClassExtendsDeclaration n, Map<String, ClassInfo> tbl) {
        String name = n.f1.accept(this, tbl);
        String parent = n.f3.accept(this, tbl);
        currentClass = new ClassInfo(name, parent);
        tbl.put(name, currentClass);
        n.f5.accept(this, tbl); // fields
        n.f6.accept(this, tbl); // methods
        return null;
    }

    // ------------------------------------------------------------------
    // VarDeclaration — class field OR method local depending on context
    // ------------------------------------------------------------------
    @Override
    public String visit(VarDeclaration n, Map<String, ClassInfo> tbl) {
        String type = n.f0.accept(this, tbl); // Type
        String name = n.f1.accept(this, tbl); // Identifier
        if (inMethodLocals && currentMethod != null)
            currentMethod.locals.add(new String[] { type, name });
        else if (currentClass != null)
            currentClass.fields.put(name, type);
        return null;
    }

    // ------------------------------------------------------------------
    // MethodDeclaration
    // ------------------------------------------------------------------
    @Override
    public String visit(MethodDeclaration n, Map<String, ClassInfo> tbl) {
        String retType = n.f1.accept(this, tbl); // Type
        String methName = n.f2.accept(this, tbl); // Identifier
        currentMethod = new MethodInfo(currentClass.name, methName, retType, n);
        n.f4.accept(this, tbl); // ( FormalParameterList() )?
        inMethodLocals = true;
        n.f7.accept(this, tbl); // ( VarDeclaration() )*
        inMethodLocals = false;
        currentClass.methods.put(methName, currentMethod);
        currentMethod = null;
        return null;
    }

    // ------------------------------------------------------------------
    // FormalParameterList / FormalParameter / FormalParameterRest
    // ------------------------------------------------------------------
    @Override
    public String visit(FormalParameterList n, Map<String, ClassInfo> tbl) {
        n.f0.accept(this, tbl);
        n.f1.accept(this, tbl);
        return null;
    }

    @Override
    public String visit(FormalParameter n, Map<String, ClassInfo> tbl) {
        String type = n.f0.accept(this, tbl);
        String name = n.f1.accept(this, tbl);
        if (currentMethod != null)
            currentMethod.params.add(new String[] { type, name });
        return null;
    }

    @Override
    public String visit(FormalParameterRest n, Map<String, ClassInfo> tbl) {
        n.f1.accept(this, tbl); // FormalParameter
        return null;
    }

    // ------------------------------------------------------------------
    // Type — dispatches to one of the four subtypes, returns type string
    // ------------------------------------------------------------------
    @Override
    public String visit(Type n, Map<String, ClassInfo> tbl) {
        return n.f0.accept(this, tbl);
    }

    @Override
    public String visit(ArrayType n, Map<String, ClassInfo> tbl) {
        return "int[]";
    }

    @Override
    public String visit(BooleanType n, Map<String, ClassInfo> tbl) {
        return "boolean";
    }

    @Override
    public String visit(IntegerType n, Map<String, ClassInfo> tbl) {
        return "int";
    }

    // ------------------------------------------------------------------
    // Identifier — returns token image
    // ------------------------------------------------------------------
    @Override
    public String visit(Identifier n, Map<String, ClassInfo> tbl) {
        return n.f0.tokenImage;
    }
}
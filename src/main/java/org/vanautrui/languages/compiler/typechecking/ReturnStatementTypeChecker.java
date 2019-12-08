package org.vanautrui.languages.compiler.typechecking;

import org.vanautrui.languages.compiler.parsing.astnodes.nonterminal.statements.controlflow.ReturnStatementNode;
import org.vanautrui.languages.compiler.parsing.astnodes.nonterminal.upperscopes.AST_Whole_Program;
import org.vanautrui.languages.compiler.parsing.astnodes.nonterminal.upperscopes.MethodNode;
import org.vanautrui.languages.compiler.parsing.astnodes.nonterminal.upperscopes.NamespaceNode;
import org.vanautrui.languages.compiler.symboltables.LocalVarSymbolTable;
import org.vanautrui.languages.compiler.symboltables.SubroutineSymbolTable;
import org.vanautrui.languages.compiler.symboltables.structs.StructsSymbolTable;
import org.vanautrui.languages.compiler.typeresolution.TypeResolver;

import static org.vanautrui.languages.compiler.typechecking.ExpressionNodeTypeChecker.typeCheckExpressionNode;

public final class ReturnStatementTypeChecker {

  static void typeCheckReturnStatementNode(
          final AST_Whole_Program asts,
          NamespaceNode namespaceNode,
          MethodNode methodNode,
          ReturnStatementNode returnStatementNode,
          SubroutineSymbolTable subTable,
          LocalVarSymbolTable varTable,
          StructsSymbolTable structsTable
  ) throws Exception {
    //the type of the value returned should be the same as the method return type
    var returnValueType = TypeResolver.getTypeExpressionNode(returnStatementNode.returnValue, subTable, varTable,structsTable);
    if (
            !(returnValueType.getTypeName().equals(methodNode.returnType.getTypeName()))

            && !contains_type(returnValueType.getTypeName(),methodNode.returnType.getTypeName())
    ) {
      throw new Exception(TypeChecker.class.getSimpleName()
              + ": return type of the method has to equal the return value type. return type '"
              + methodNode.returnType.getTypeName() + "' does not equal the returned type '" + returnValueType.getTypeName() + "'. found in method: "+methodNode.methodName);
    }
    typeCheckExpressionNode(asts, namespaceNode, methodNode, returnStatementNode.returnValue, subTable, varTable,structsTable);
  }

  private static boolean contains_type(String typename, String maybeContainerType) {
    //Integer contains PInt, NInt
    if(maybeContainerType.equals("Integer")){
      if(typename.equals("PInt") || typename.equals("NInt")){
        return true;
      }
    }
    return false;
  }
}

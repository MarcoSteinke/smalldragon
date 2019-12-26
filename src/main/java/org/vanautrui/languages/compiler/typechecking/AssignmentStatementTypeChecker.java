package org.vanautrui.languages.compiler.typechecking;

import org.vanautrui.languages.compiler.parsing.astnodes.nonterminal.statements.AssignmentStatementNode;
import org.vanautrui.languages.compiler.parsing.astnodes.nonterminal.upperscopes.AST_Whole_Program;
import org.vanautrui.languages.compiler.parsing.astnodes.nonterminal.upperscopes.MethodNode;
import org.vanautrui.languages.compiler.parsing.astnodes.nonterminal.upperscopes.NamespaceNode;
import org.vanautrui.languages.compiler.symboltables.util.SymbolTableContext;
import org.vanautrui.languages.compiler.typeresolution.TypeResolver;

import static org.vanautrui.languages.compiler.typechecking.ExpressionNodeTypeChecker.typeCheckExpressionNode;
import static org.vanautrui.languages.compiler.typechecking.VariableNodeTypeChecker.typeCheckVariableNode;

public final class AssignmentStatementTypeChecker {

  //prevents instance creation
  private AssignmentStatementTypeChecker(){}

  static void typeCheckAssignmentStatementNode(
          final AST_Whole_Program asts,
          final NamespaceNode namespaceNode,
          final MethodNode methodNode,
          final AssignmentStatementNode assignmentStatementNode,
          final SymbolTableContext ctx
          ) throws Exception{

    final var leftSideType = TypeResolver.getTypeVariableNode(assignmentStatementNode.variableNode,ctx);
    final var rightSideType = TypeResolver.getTypeExpressionNode(assignmentStatementNode.expressionNode,ctx);

    final boolean types_equal = leftSideType.getTypeName().equals(rightSideType.getTypeName());
    final boolean one_is_wildcard = leftSideType.getTypeName().equals("#") || rightSideType.getTypeName().equals("#");

    final boolean leftside_type_contains_rightside_type = leftSideType.getTypeName().equals("Integer") && TypeChecker.isIntegralType(rightSideType);

    if(
            !types_equal && !one_is_wildcard && !leftside_type_contains_rightside_type
    ){
      throw new Exception(
              "with an assignment, both sides have to have the same type. here, a value of type "+rightSideType.getTypeName()+" was assigned to a value of type "+leftSideType.getTypeName()
                      +" in source: '"+assignmentStatementNode.toSourceCode()+"'"
      );
    }

    typeCheckVariableNode(asts, namespaceNode,methodNode,assignmentStatementNode.variableNode,ctx);
    typeCheckExpressionNode(asts, namespaceNode,methodNode,assignmentStatementNode.expressionNode,ctx);
  }
}


//standard headers
#include <vector>
#include <set>

//project headers

@JsonTypeInfo(
		use = JsonTypeInfo.Id.NAME,
		include = JsonTypeInfo.As.PROPERTY,
		property = "type")
@JsonSubTypes({
		@JsonSubTypes.Type(value = ArrayConstantNode.class, name = "ArrayConstantNode"),
		@JsonSubTypes.Type(value = ExpressionNode.class, name = "ExpressionNode"),
		@JsonSubTypes.Type(value = VariableNode.class, name = "VariableNode"),
		@JsonSubTypes.Type(value = MethodCallNode.class, name = "MethodCallNode"),
		@JsonSubTypes.Type(value = BoolConstNode.class, name = "BoolConstNode"),
		@JsonSubTypes.Type(value = CharConstNode.class, name = "CharConstNode"),
		@JsonSubTypes.Type(value = FloatConstNode.class, name = "FloatConstNode"),
		@JsonSubTypes.Type(value = IntConstNode.class, name = "IntConstNode")
})
public interface ITermNode extends IASTNode {

	//pseudo interface,
	//only (AST Nodes classes) which are on the right side of a 'term' production should implement it
}

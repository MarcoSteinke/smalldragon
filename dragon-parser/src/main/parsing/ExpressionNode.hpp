#ifndef EXPRESSIONNODE
#define EXPRESSIONNODE

#include <optional>
#include <vector>

#include "OperatorNode.hpp"
#include "TermNode.hpp"
#include "../commandline/TokenList.hpp"

using namespace std;

class ExpressionNode {

public:
	//DragonExpressionNode should be similar to jack expression
	//an expression should be anything that returns a value or computes to a value

	ExpressionNode(TokenList tkl);
	ExpressionNode(TermNode myterm);
	ExpressionNode(TermNode leftTerm, OperatorNode op, TermNode rightTerm);


	void performTreeTransformation(
		vector<OperatorNode> ops,
		vector<TermNode> terms
	);

	TermNode term1;
	optional<OperatorNode> op;
	optional<TermNode> term2;
};

#endif
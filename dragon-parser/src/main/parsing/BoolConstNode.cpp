
//standard headers
#include <vector>
#include <set>

//project headers
#include "BoolConstNode.hpp"
#include "../lexing/TokenList.hpp"

using namespace std;

BoolConstNode::BoolConstNode(TokenList tokens) {

	TokenList copy = TokenList(tokens);

	if (copy.get(0) instanceof BoolConstantToken) {
		BoolConstantToken tk = (BoolConstantToken) copy.get(0);
		this->boolValue = tk.value;
		copy.consume(1);
	} else {
		throw ("could not read Bool Constant node");
	}

	tokens.set(copy);
}
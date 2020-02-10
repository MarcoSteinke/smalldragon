#include "MethodCallTest.hpp"

#include "../../main/commandline/TokenList.hpp"
#include "../../main/commandline/TokenKeys.hpp"
#include "../../main/commandline/Token.hpp"


#include "../../main/parsing/statements/MethodCall.hpp"

int methodcall_test1() {
	TokenList list = new TokenList();

	list.add(ID,"main");
	list.add(LPARENS);
	list.add(RPARENS);

	MethodCall* call = new MethodCall(list,false);

	bool assert1 = (0 == list.size());
	return (assert1)?1:0;
}

int methodcall_test2() {
	TokenList list = new TokenList();

	list.add(ID,"main");
	list.add(LPARENS);
	list.add(INTEGER,"4");
	list.add(RPARENS);

	MethodCall* call = new MethodCall(list,false);
	bool assert1 = (0 == list.size());

	return (assert1)?1:0;
}

int methodcall_test3() {
	TokenList list = new TokenList();

	list.add(ID,"main");
	list.add(LPARENS);
	list.add(ID,"x");
	list.add(RPARENS);

	MethodCall* call = new MethodCall(list,false);
	bool assert1 = (0 == list.size());
	return (assert1)?1:0;
}

int methodcall_test_can_parse_subroutine_call() {

	TokenList tl = new TokenList();
	tl.add(ID,"println");
	tl.add(LPARENS);
	tl.add(INTEGER,"1");
	tl.add(RPARENS);

	MethodCall* call = new MethodCall(tl,false);
	bool assert1 = (0 == tl.size());

	return (assert1)?1:0;
}

int methodcall_test_can_parse_subroutine_call2() {
	//println("x<5")

	TokenList tokens = new TokenList();
	tokens.add(ID,"println");
	tokens.add(LPARENS);
	tokens.add(STRINGCONST,"x<5");
	tokens.add(RPARENS);

	MethodCall* call = new MethodCall(tokens,false);
	bool assert1 = (0 == tokens.size());

	return (assert1)?1:0;
}


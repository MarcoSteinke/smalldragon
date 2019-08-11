package org.vanautrui.languages.parsing.astnodes.terminal;

import org.simpleframework.xml.Attribute;
import org.vanautrui.languages.lexing.tokens.DragonToken;
import org.vanautrui.languages.lexing.tokens.StringConstantToken;
import org.vanautrui.languages.lexing.collections.DragonTokenList;
import org.vanautrui.languages.parsing.IDragonASTNode;
import org.vanautrui.languages.parsing.astnodes.nonterminal.DragonAST;
import org.vanautrui.languages.parsing.astnodes.nonterminal.DragonClassNode;
import org.vanautrui.languages.parsing.astnodes.nonterminal.DragonMethodNode;

import java.util.Optional;
import java.util.Set;

public class DragonStringConstantNode implements IDragonASTNode {

    @Attribute
    public String str;

    public DragonStringConstantNode(DragonTokenList tokens) throws Exception {

        DragonToken token = tokens.get(0);

        if (token instanceof StringConstantToken) {
            this.str = ((StringConstantToken) token).getContents();
            tokens.consume(1);
        } else {
            throw new Exception("could not read stringConstant node");
        }

    }

    @Override
    public String toSourceCode() {
        return this.str;
    }

    @Override
    public void doTypeCheck(Set<DragonAST> asts, Optional<DragonClassNode> currentClass, Optional<DragonMethodNode> currentMethod) throws Exception {
        //a string is always typesafe, nothing to do here
        return;
    }
}

package org.vanautrui.languages.compiler.parsing.astnodes.nonterminal.upperscopes;

import org.apache.commons.lang3.tuple.Pair;
import org.vanautrui.languages.compiler.lexing.tokens.KeywordToken;
import org.vanautrui.languages.compiler.lexing.tokens.SymbolToken;
import org.vanautrui.languages.compiler.lexing.utils.TokenList;
import org.vanautrui.languages.compiler.parsing.IASTNode;
import org.vanautrui.languages.compiler.parsing.astnodes.terminal.AccessModifierNode;
import org.vanautrui.languages.compiler.parsing.astnodes.terminal.TypeIdentifierNode;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ClassNode implements IASTNode {

    public boolean isPublic;
    public Path srcPath= Paths.get("/dev/null");

    public TypeIdentifierNode name;

    public List<ClassFieldNode> fieldNodeList = new ArrayList<>();
    public List<MethodNode> methodNodeList = new ArrayList<>();

    public ClassNode(TokenList tokens,Path path,boolean debug) throws Exception {

        if(debug){
            System.out.println("try to parse "+this.getClass().getSimpleName()+" from "+tokens.toSourceCodeFragment());
        }

        this.srcPath=path;
        TokenList copy = tokens.copy();

        AccessModifierNode access = new AccessModifierNode(copy);

        copy.expectAndConsumeOtherWiseThrowException(new KeywordToken("class"));

        this.name = new TypeIdentifierNode(copy);

        copy.expectAndConsumeOtherWiseThrowException(new SymbolToken("{"));

        if(!copy.endsWith(new SymbolToken("}"))){
            //we assume the class only received a token stream with only the relavant info for it
            throw new Exception(" a class should end with '}' but it did not ");
        }

        //all fields must occur before the subroutines
        //this simplifies the parser

        boolean success_field = true;
        while (success_field) {
            try {
                this.fieldNodeList.add(new ClassFieldNode(copy,debug));
            } catch (Exception e22) {
                success_field = false;
            }
        }

        boolean success_method = true;
        Pair<TokenList, TokenList> pair = copy.split_into_tokens_and_next_block_and_later_tokens();
        while (success_method){
            //DEBUG
            System.out.println("pair:");
            System.out.println(pair.getLeft().toSourceCodeFragment());
            System.out.println(pair.getRight().toSourceCodeFragment());
            try {
                int chunk_size=pair.getLeft().size();
                this.methodNodeList.add(new MethodNode(pair.getLeft(),debug));
                copy.consume(chunk_size);
                pair = pair.getRight().split_into_tokens_and_next_block_and_later_tokens();
            } catch (Exception e11) {
                success_method = false;
            }
        }

        //DEBUG:
        System.out.println("tokens left after try to parse fields and methods:");
        System.out.println(copy.toSourceCodeFragment());

        copy.expectAndConsumeOtherWiseThrowException(new SymbolToken("}"));

        tokens.set(copy);
    }


    @Override
    public String toSourceCode() {
        String result = "";

        result+=((this.isPublic)?" public":" private")+" class "+this.name.toSourceCode()+" {";

        result += fieldNodeList
                .stream()
                .map(node -> node.toSourceCode())
                .collect(Collectors.joining("\n"));
        result+="\n";

        result += methodNodeList
                .stream()
                .map(node -> node.toSourceCode())
                .collect(Collectors.joining("\n"));

        result+="\n";
        result+="}";
        return result;
    }
}

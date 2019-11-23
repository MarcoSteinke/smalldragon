package org.vanautrui.languages.compiler.typechecking;

import org.junit.Test;
import org.vanautrui.languages.commandline.ParserPhases;
import org.vanautrui.languages.compiler.lexing.utils.TokenList;
import org.vanautrui.languages.compiler.parsing.Parser;
import org.vanautrui.languages.compiler.parsing.astnodes.nonterminal.upperscopes.AST;

import java.util.ArrayList;

import static org.junit.Assert.fail;

public final class StructDeclNodeTypeCheckerTest {


  @Test
  public void test_typechecking_struct_valid() throws Exception{

    Parser parser=new Parser();
    TokenList tokens = ParserPhases.makeTokenList(
            "public namespace ExampleClass{ struct MyStruct{} ()~>PInt main{ return 0;}" +
                    "}");

    AST ast = parser.parseTestMode(tokens,false);

    ArrayList<AST> asts = new ArrayList<>();
    asts.add(ast);
    TypeChecker.doTypeCheck(asts,false);
  }

  @Test
  public void test_typechecking_struct_invalid_primitive_typename() throws Exception{

    Parser parser=new Parser();
    TokenList tokens = ParserPhases.makeTokenList(
            "public namespace ExampleClass{ struct PInt{}" +
                    "}");

    AST ast = parser.parseTestMode(tokens,false);

    ArrayList<AST> asts = new ArrayList<>();
    asts.add(ast);
    try {
      TypeChecker.doTypeCheck(asts, false);
      fail();
    }catch (Exception e){
      //
    }
  }

  @Test
  public void test_typechecking_struct_invalid_multiple_declarations() throws Exception{

    final TokenList tokens = ParserPhases.makeTokenList(
            "public namespace ExampleClass{ struct MyStruct{} struct MyStruct{}" +
                    "}");

    AST ast = (new Parser()).parseTestMode(tokens,false);

    ArrayList<AST> asts = new ArrayList<>();
    asts.add(ast);
    try {
      TypeChecker.doTypeCheck(asts, false);
      fail();
    }catch (Exception e){
      //
    }
  }

}

package org.vanautrui.languages.compiler.vmcodegenerator;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.vanautrui.languages.compiler.lexing.Lexer;
import org.vanautrui.languages.compiler.lexing.utils.TokenList;
import org.vanautrui.languages.compiler.parsing.astnodes.nonterminal.statements.MethodCallNode;

import static org.junit.Assert.assertEquals;
import static org.vanautrui.languages.CodeGeneratorTestUtils.compile_and_run_program_for_testing;

public class AssignmentStatementCodeGeneratorTest {

    @Test
    public void test_can_assign_to_local_variable()throws Exception{
        //depends on loop statement,return statement already working

        String source="public namespace MainTest333{" +
                "\tpublic ()~>PInt main{" +
                "\t\tx=3;" +
                "\t\tloop x {putchar('x');}" +
                "\t return 0;}" +
                "}";
        Process pr = compile_and_run_program_for_testing(source,"MainTest333");

        assertEquals(0,pr.exitValue());
        assertEquals("xxx",IOUtils.toString(pr.getInputStream()));
    }

    @Test
    public void test_can_assign_to_local_variable_muliple_times()throws Exception{
        //depends on loop statement,return statement already working

        String source="public namespace MainTest333{" +
                "\tpublic ()~>PInt main{" +
                "\t\tx=3;" +
                "\t\tx=2;" +
                "\t\tloop x {putchar('x');}" +
                "\t return 0;}" +
                "}";
        Process pr = compile_and_run_program_for_testing(source,"MainTest333");

        assertEquals(0,pr.exitValue());
        assertEquals("xx",IOUtils.toString(pr.getInputStream()));
    }

    @Test
    public void test_can_read_from_array()throws Exception{
        String source="public namespace MainTest333{" +
                "\tpublic ()~>PInt main{" +
                "\t\tarr=['A','B'];" +
                "\t\tx=arr[0];" +
                "\t\tputchar(x);" +
                "\t return 0;}" +
                "}";
        Process pr = compile_and_run_program_for_testing(source,"MainTest333");

        assertEquals(0,pr.exitValue());
        assertEquals("A",IOUtils.toString(pr.getInputStream()));
    }


    @Test
    public void test_can_write_into_array()throws Exception{
        String source="public namespace MainTest454{" +
                "\tpublic ()~>PInt main{" +
                "\t\tarr=['a','b','c'];" +
                "\t\tarr[1]='d';" +
                "\t\tputchar(arr[1]);" +
                "\treturn 0;}" +
                "}";
        Process pr = compile_and_run_program_for_testing(source,"MainTest454");

        assertEquals(0,pr.exitValue());
        assertEquals("d",IOUtils.toString(pr.getInputStream()));
    }

    @Test
    public void test_can_use_structs()throws Exception{
        String source="public namespace MainTest954{" +
                "struct MyStruct{PInt b}"+
                "\tpublic ()~>PInt main{" +
                "\t\tMyStruct a=new(1);" +
                "\t\ta.b=1;" +
                "\t\tputdigit(a.b);" +
                "\treturn 0;}" +
                "}";
        Process pr = compile_and_run_program_for_testing(source,"MainTest954");

        assertEquals(0,pr.exitValue());
        assertEquals("1",IOUtils.toString(pr.getInputStream()));
    }

    @Test
    public void test_can_use_structs_nested()throws Exception{
        String source="public namespace MainTest9541{" +
                "struct MyStruct{PInt b,MyStruct s}"+
                "\tpublic ()~>PInt main{" +
                "\t\tMyStruct a = new(2);" +
                "a.s = new(2);"+
                "\t\ta.s.b=1;" +
                "\t\tputdigit(a.s.b);" +
                "\treturn 0;}" +
                "}";
        Process pr = compile_and_run_program_for_testing(source,"MainTest9541");

        assertEquals(0,pr.exitValue());
        assertEquals("1",IOUtils.toString(pr.getInputStream()));
    }
}

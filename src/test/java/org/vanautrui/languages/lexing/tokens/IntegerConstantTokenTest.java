package org.vanautrui.languages.lexing.tokens;

import org.junit.Assert;
import org.junit.Test;
import org.vanautrui.languages.lexing.CharacterList;
import org.vanautrui.languages.lexing.DragonLexer;
import org.vanautrui.languages.parsing.DragonTokenList;

public class IntegerConstantTokenTest {

    @Test
    public void test1() throws Exception {

        CharacterList list = new CharacterList("2038");

        IntegerConstantToken token = new IntegerConstantToken(list);

        Assert.assertEquals("2038", token.getContents());
    }

}

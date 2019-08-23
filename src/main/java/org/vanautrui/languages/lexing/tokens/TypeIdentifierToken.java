package org.vanautrui.languages.lexing.tokens;

import org.vanautrui.languages.lexing.collections.CharacterList;
import org.vanautrui.languages.lexing.tokens.utils.BasicToken;
import org.vanautrui.languages.lexing.tokens.utils.Token;
import com.fasterxml.jackson.annotation.*;
import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TypeIdentifierToken extends BasicToken implements Token {

    //a Type name should start with a Uppercase letter,
    public static final String regex_alphanumeric_type_identifier = "^[A-Z][a-zA-Z0-9_]*";

    public static final String regex_alphanumeric_array_type_identifier = "^\\[[A-Z][a-zA-Z0-9_]*\\]";

    public static final int MAX_IDENTIFIER_LENGTH = 100;

    private String content;

    public TypeIdentifierToken(CharacterList list) throws Exception {
        super();
        CharacterList copy = new CharacterList(list);
        try{
            new KeywordToken(copy);
            throw new Exception("type identifier token should not parse as keyword token");
        }catch (Exception e){
            //pass
        }

        Pattern pNormal = Pattern.compile(regex_alphanumeric_type_identifier);
        Pattern pArrayType = Pattern.compile(regex_alphanumeric_array_type_identifier);

        Matcher m = pNormal.matcher(list.getLimitedStringMaybeShorter(MAX_IDENTIFIER_LENGTH));
        Matcher m2 = pArrayType.matcher(list.getLimitedStringMaybeShorter(MAX_IDENTIFIER_LENGTH));

        if (m.find()) {
            this.content = m.group(0);
            list.consumeTokens(this.content.length());
        }else if(m2.find()){
            String tmp = m2.group();
            this.content=tmp;
            list.consumeTokens(tmp.length());
        } else {
            throw new Exception("could not recognize type identifier");
        }
    }

    public TypeIdentifierToken(String str) throws Exception{
        this(new CharacterList(str));
    }

    @Override
    public String getContents() {
        return this.content;
    }

    @Override
	@JsonIgnore
    public Color getDisplayColor() {
        return Color.YELLOW;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TypeIdentifierToken)) return false;
        TypeIdentifierToken that = (TypeIdentifierToken) o;
        return content.equals(that.content);
    }

}

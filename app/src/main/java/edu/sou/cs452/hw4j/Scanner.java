package edu.sou.cs452.hw4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scanner {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private static final Map<String, TokenType> keywords;

    static {

        // The wisdom of the class was correct, no create keyword in Pony.
        // Removed create to match https://tutorial.ponylang.io/appendices/keywords.html
        keywords = new HashMap<>();
        keywords.put("actor", TokenType.ACTOR);
        keywords.put("new", TokenType.NEW);
    }

    private int start = 0;
    private int current = 0;
    private int line = 1;

    public Scanner(String source) {
        this.source = source;
    }

    public List<Token> scanTokens() {
        while (!isAtEnd()) {
            start = current; // We are at the beginning of the next lexeme.
            scanToken();
        }

        tokens.add(new Token(TokenType.EOF, "", null, line));
        return tokens;
    }

    private boolean isAtEnd() {
        return current >= source.length();
    }

    private void scanToken() {
        char c = advance();
        switch (c) {
            case '(':
                addToken(TokenType.LEFT_PAREN);
                break;
            case ')':
                addToken(TokenType.RIGHT_PAREN);
                break;
            case ':':
                addToken(TokenType.COLON);
                break;
            case '.':
                addToken(TokenType.DOT);
                break;
            case '=':
                if (match('>')) {
                    addToken(TokenType.RIGHT_ARROW);
                }
                break;
            case '"':
                string();
                break;

            default:
                if (isAlpha(c)) {
                    identifier();
                } else if (Character.isWhitespace(c)) {
                    if (c == '\n') line++;
                    // Ignore whitespace.
                } else {
                    App.error(line, "Unexpected character: " + c);
                }
                break;
        }
    }

   private boolean match(char expected) {
       if (isAtEnd() || source.charAt(current) != expected) return false;

       current++;
       return true;
   }

   private void string() {
       while (peek() != '"' && !isAtEnd()) {
           if (peek() == '\n') line++;
           advance();
       }

       if (isAtEnd()) {
           App.error(line, "Unterminated string.");
           return;
       }

       // The closing ".
       advance();

       // Trim the surrounding quotes.
       String value = source.substring(start + 1, current - 1);
       addToken(TokenType.STRING, value);
   }

   private char peek() {
      return isAtEnd() ? '\0' : source.charAt(current);
   }

   private void identifier() {
      while (isAlphaNumeric(peek())) advance();

      String text = source.substring(start, current);

      TokenType type = keywords.getOrDefault(text, TokenType.IDENTIFIER);
      addToken(type);
   }
   
   private boolean isAlpha(char c) { 
      return (c >= 'a' && c <= 'z') || 
             (c >= 'A' && c <= 'Z') || 
              c == '_'; 
   }
   
   private boolean isAlphaNumeric(char c) { 
      return isAlpha(c); 
   } 

   private char advance() { 
      return source.charAt(current++); 
   } 

   private void addToken(TokenType type) { 
      addToken(type, null); 
   } 

   private void addToken(TokenType type, Object literal) { 
      String text = source.substring(start, current); 
      tokens.add(new Token(type, text, literal, line)); 
   } 

}


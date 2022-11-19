import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexeylex {
    
	//class to create different lexemes
	static Pattern declare = Pattern.compile("int\\[\\d\\];", Pattern.CASE_INSENSITIVE);
	
	public static enum Type {
        
        LPAREN, RPAREN, IDENTIFIER, OPERATOR, LOOP, STMT, SEPERATOR, DECLARE ;
    }
    public static class Token {
        public final Type t;
        public final String c; 
        
        public Token(Type t, String c) {
            this.t = t;
            this.c = c;
        }
        public String toString() {
        	if(t == Type.LOOP) {
                return "<loop>" + c;
            }
        	
        	if(t == Type.IDENTIFIER) {
                return "<variable>" + c;
            }
            if(t == Type.OPERATOR) {
                return "<operator>" + c;
            }
            return t.toString();
        }
    }

    /*
     * Given a String, and an index, get the word starting at that index
     */
    public static String getWord(String s, int i) {
        int j = i;
        for( ; j < s.length(); ) {
            if(Character.isLetter(s.charAt(j))) {
                j++;
            } else {
                return s.substring(i, j);
            }
        }
        return s.substring(i, j);
    }

    public static List<Token> lex(String input) {
        //takes in a string, concatted by whitespace and check to see what is contained in it, and creates a new token based off the contents
    	List<Token> result = new ArrayList<Token>();
        for(int i = 0; i < input.length(); ) {
            switch(input.charAt(i)) {
            case '(':
                result.add(new Token(Type.LPAREN, "("));
                i++;
                break;
            case ')':
                result.add(new Token(Type.RPAREN, ")"));
                i++;
                break;
            case '+':
                result.add(new Token(Type.OPERATOR, "+"));
                i++;
                break;
            case '-':
                result.add(new Token(Type.OPERATOR, "-"));
                i++;
                break;
            case '/':
                result.add(new Token(Type.OPERATOR, "/"));
                i++;
                break;
            case '*':
                result.add(new Token(Type.OPERATOR, "*"));
                i++;
                break;
            case ';':
                result.add(new Token(Type.STMT, ";"));
                i++;
                break;
            case '=':
                result.add(new Token(Type.OPERATOR, "="));
                i++;
                break;
            case '~':
                result.add(new Token(Type.SEPERATOR, "~"));
                i++;
                break;
               
                
                
            default:
                if(Character.isWhitespace(input.charAt(i))) {
                    i++;
                } else {
                    String word = getWord(input, i);
                    Matcher declare_match = declare.matcher(word);
                   //i can't get my regex for a declaration to work, :(
                    if(word.equals("watching")) {
                    	i+= word.length();
                    	result.add(new Token(Type.LOOP, word));
                    }
                    if(declare_match.matches()) {
                        	i+= word.length();
                        	result.add(new Token(Type.DECLARE, word));
                    	
                    } else {
                    i += word.length();
                    result.add(new Token(Type.IDENTIFIER, word));
                    }
                }
                break;
            
            }
        }
        return result;
    }

    public static void main(String[] args) throws IOException {
        //reads the test files and creates a list of tokens as it's read, then prints the list out in order of lexemes
    	Path yourcode = Path.of("put test file");
        
        String code = Files.readString(yourcode);
        List<Token> tokens = lex(code);
        for(Token t : tokens) {
            System.out.println(t);
        }
    }
}

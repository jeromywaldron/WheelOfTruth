/**
 * @author Nikita Kouevda, Jenny Shen
 * @date 2012/03/27
 */

package wof.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class WheelOfFortuneGame {
    // -----------------------------------------------------------------
    // Fields
    // -----------------------------------------------------------------
    
    private static final String PHRASES_DIR = "/wof/phrases/";
    
    private static final int INITIAL_TURNS = 7;
    
    private final ArrayList<String> CATEGORIES, PHRASES;
    
    private String myCategory;
    
    private String myPhrase;
    
    private ArrayList<Character> myGuessedLetters;
    
    private int myScore, myTurnsLeft;
    
    // -----------------------------------------------------------------
    // Constructors
    // -----------------------------------------------------------------
    
    public WheelOfFortuneGame() {
        // Construct and initialize the list of phrases to choose from
        CATEGORIES = new ArrayList<String>();
        PHRASES = new ArrayList<String>();
        
        try {
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(getClass()
                            .getResourceAsStream(
                                    PHRASES_DIR + "phrases.txt")));
            
            while (reader.ready()) {
                CATEGORIES.add(reader.readLine());
                
                String phrase = "";
                
                for (int i = 0; i < 4; i++)
                    phrase += reader.readLine();
                
                PHRASES.add(phrase);
            }
        } catch (IOException ex) {
            CATEGORIES.add("Error");
            PHRASES.add("             No Phrases     File                ");
        }
        
        myScore = 0;
        myTurnsLeft = INITIAL_TURNS;
        
        // Pick a phrase to be used initially
        newPhrase();
    }
    
    // -----------------------------------------------------------------
    // Methods
    // -----------------------------------------------------------------
    
    public String getCategory() {
        return myCategory;
    }
    
    public String getPhrase() {
        return myPhrase;
    }
    
    public ArrayList<Character> getGuessedLetters() {
        return myGuessedLetters;
    }
    
    public int getScore() {
        return myScore;
    }
    
    public int getTurnsLeft() {
        return myTurnsLeft;
    }
    
    public void addScore(int score) {
        myScore += score;
    }
    
    public void resetScore() {
        myScore = 0;
    }
    
    public void resetValues() {
        myTurnsLeft = INITIAL_TURNS;
        myScore = 0;
    }
    
    public void subtractTurn() {
        myTurnsLeft--;
    }
    
    public boolean isAllVowelsGuessed() {
        return !(myPhrase.contains("A")
                && !myGuessedLetters.contains("A")
                || myPhrase.contains("E")
                && !myGuessedLetters.contains("E")
                || myPhrase.contains("I")
                && !myGuessedLetters.contains("I")
                || myPhrase.contains("O")
                && !myGuessedLetters.contains("O") || myPhrase
                .contains("U") && !myGuessedLetters.contains("U"));
    }
    
    public boolean isSolved() {
        // Return false if any letter has not been guessed yet
        for (char c : myPhrase.toCharArray())
            if (!myGuessedLetters.contains(c))
                return false;
        
        return true;
    }
    
    public void newPhrase() {
        int index = (int)(PHRASES.size() * Math.random());
        
        myCategory = CATEGORIES.get(index);
        myPhrase = PHRASES.get(index).toUpperCase();
        
        // Clear revealed letters
        myGuessedLetters = new ArrayList<Character>();
    }
    
    public int revealLetter(char letter) {
        // Return 0 if the letter has already been revealed
        if (myGuessedLetters.contains(letter))
            return 0;
        
        // Add letter to guessed letters
        myGuessedLetters.add(letter);
        
        // Number of occurences of letter in the puzzle
        int occurences = 0;
        
        // Determine whether letter appears in the puzzle
        for (char c : myPhrase.toCharArray())
            if (letter == c)
                occurences++;
        
        return occurences;
    }
    
    public void revealPuzzle() {
        for (char c : myPhrase.toCharArray())
            myGuessedLetters.add(c);
    }
    
    public void disableVowels() {
        myGuessedLetters.add('a');
        myGuessedLetters.add('e');
        myGuessedLetters.add('i');
        myGuessedLetters.add('o');
        myGuessedLetters.add('u');
    }
}

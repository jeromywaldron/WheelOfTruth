/**
 * @author Nikita Kouevda, Jenny Shen
 * @date 2013/10/05
 */

package wof.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WheelOfFortuneGame {
    private static final String PHRASES_DIR = "/wof/phrases/";

    private final List<String> CATEGORIES, PHRASES;

    private String category, phrase;

    private Set<Character> guessedLetters;

    private int score1, score2;

    public WheelOfFortuneGame() {
        CATEGORIES = new ArrayList<String>();
        PHRASES = new ArrayList<String>();

        try {
            BufferedReader reader =
                new BufferedReader(new InputStreamReader(getClass()
                    .getResourceAsStream(PHRASES_DIR + "phrases.txt")));

            while (reader.ready()) {
                CATEGORIES.add(reader.readLine());

                StringBuilder phrase = new StringBuilder();

                for (int i = 0; i < 4; ++i) {
                    phrase.append(reader.readLine());
                }

                PHRASES.add(phrase.toString());
            }
        } catch (IOException ex) {
            CATEGORIES.add("Error");
            PHRASES.add("             No Phrases     File                ");
        }

        score1 = 0;
        score2 = 0;

        newPhrase();
    }

    public String getCategory() {
        return category;
    }

    public String getPhrase() {
        return phrase;
    }

    public Set<Character> getGuessedLetters() {
        return guessedLetters;
    }

    public int getScore1() {
        return score1;
    }
    public int getScore2() {
        return score2;
    }


    public void addScore1(int score1) {
        this.score1 += score1;
    }
    public void addScore2(int score2) {
        this.score2 += score2;
    }

    public void resetScore1() {
        score1 = 0;
    }
    public void resetScore2() {
        score2 = 0;
    }

    public void resetValues() {
        score1 = 0;
        score2 = 0;
    }

    public boolean isAllVowelsGuessed() {
        return !(phrase.contains("A") && !guessedLetters.contains("A")
            || phrase.contains("E") && !guessedLetters.contains("E")
            || phrase.contains("I") && !guessedLetters.contains("I")
            || phrase.contains("O") && !guessedLetters.contains("O") || phrase
            .contains("U") && !guessedLetters.contains("U"));
    }

    public boolean isSolved() {
        // Return false if any letter has not been guessed yet
        for (char c : phrase.toCharArray()) {
            if (!guessedLetters.contains(c)) {
                return false;
            }
        }

        return true;
    }

    public void newPhrase() {
        int index = (int)(PHRASES.size() * Math.random());

        category = CATEGORIES.get(index);
        phrase = PHRASES.get(index).toUpperCase();

        guessedLetters = new HashSet<Character>();
    }

    public int revealLetter(char letter) {
        if (guessedLetters.contains(letter)) {
            return 0;
        }

        guessedLetters.add(letter);

        int occurences = 0;

        for (char c : phrase.toCharArray()) {
            if (letter == c) {
                ++occurences;
            }
        }

        return occurences;
    }

    public void revealPuzzle() {
        for (char c : phrase.toCharArray()) {
            guessedLetters.add(c);
        }
    }

    public void disableVowels() {
        guessedLetters.add('a');
        guessedLetters.add('e');
        guessedLetters.add('i');
        guessedLetters.add('o');
        guessedLetters.add('u');
    }
}

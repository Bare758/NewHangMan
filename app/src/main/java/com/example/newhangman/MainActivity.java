package com.example.newhangman;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
        //declare variables

    TextView txtGuessTheWord;
    String GuessTheWord;
    String wordDisPlayedString;
    char[] wordDisPlayedCharArray;
    ArrayList<String> myList0fWords;
    EditText edtInput;
    TextView txtLettersUse;
    String lettersTried;
    final String MESSAGE_WITH_LETTERS_TRIED = "Letters Tried; ";
    TextView txtTriesLeft;
    String triesLeft;
    final String WINNING_MESSAGE = "You Won!";
    final String LOOSING_MESSAGE = "You Lost!";
    Animation rotateAnimation;
    Animation scaleAnimation;
    Animation ScaleAndRotateAnimation;
    TableRow trReset;
    TableRow trTriesLeft;
    private Button btnResult;


    void  revealLetterInWord (char letter) {
        int indexOfLetter = GuessTheWord.indexOf(letter);
            //loop if index is positive or 0
        while(indexOfLetter >= 0) {
            wordDisPlayedCharArray [indexOfLetter] = GuessTheWord.charAt(indexOfLetter);
            indexOfLetter = GuessTheWord.indexOf(letter, indexOfLetter + 1);

        }

        // update the string as well
        wordDisPlayedString = String.valueOf(wordDisPlayedCharArray);

    }

    void displayWordOnScreen () {
        String formattedString = "";
        for (char character : wordDisPlayedCharArray){
            formattedString += character + " ";

        }
        txtGuessTheWord.setText(formattedString);
    }

    void initializeGame() {
        //1.word
        //shuffle the array list and get the first element and then remove it
        Collections.shuffle(myList0fWords);
        GuessTheWord = myList0fWords.get(0);
        myList0fWords.remove(0);

        // initialize character array
        wordDisPlayedCharArray = GuessTheWord.toCharArray();

        // add underscores
        for(int i = 0; i < wordDisPlayedCharArray.length -1; i++) {
            wordDisPlayedCharArray[i] = '_';
        }
        //reveal all occurences of first character
        revealLetterInWord(wordDisPlayedCharArray[0]);

        //reveal all occurences of last character
        revealLetterInWord(wordDisPlayedCharArray[wordDisPlayedCharArray.length -1]);
        // initialize a string from this char array(for search purposes)
        String wordDisplayed = String.copyValueOf(wordDisPlayedCharArray);

        //display word
        displayWordOnScreen();


        //2.Input
        //clear input field
        edtInput.setText("");

        // 3.letters tried
        //initialize string for letters tried with a space
        lettersTried = " ";

        // display on a screen
        txtTriesLeft.setText(MESSAGE_WITH_LETTERS_TRIED);

        // 4.tries left
        // initialize the string for tries left
        triesLeft = " X X X X X";
        txtTriesLeft.setText(triesLeft);


        }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //initialize variables
        myList0fWords = new ArrayList<String>();
        txtGuessTheWord = (TextView) findViewById(R.id.txtGUESSTHEWORD);
        edtInput = (EditText) findViewById(R.id.edtInput);
        txtLettersUse = (TextView) findViewById(R.id.txtLettersUse);
        txtTriesLeft = (TextView) findViewById(R.id.txtTriesLeft);
        rotateAnimation = AnimationUtils.loadAnimation(this,R.anim.rotate );
        scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.scale);
        ScaleAndRotateAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_and_rotate);
        ScaleAndRotateAnimation.setFillAfter(true);
        trReset = (TableRow) findViewById(R.id.trReset);
        trTriesLeft = (TableRow) findViewById(R.id.trTriesLeft);



        //traverse database file and populate array
        InputStream myInputStream  = null;
        Scanner in = null;
        String aWord = "";
        try {
            myInputStream = getAssets().open("database_file.txt");
            in = new Scanner (myInputStream);
            while(in.hasNext()) {
        aWord = in.next();
        myList0fWords.add(aWord);

            }
        } catch (IOException e) {
            Toast.makeText(this,
                    e.getClass().getSimpleName() + "; " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
        finally {
            //close scanner
           if(in != null){
               in.close();
           }
           //inputStream
            try {
                if (myInputStream != null) {
                    myInputStream.close();
                }
                myInputStream.close();
            } catch (IOException e) {
                Toast.makeText(this,
                        e.getClass().getSimpleName() + "; " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        }


        initializeGame();

        // setup the text changed listener for the the edit text
        edtInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // if there is some letter on the input field
                if (s.length() !=0){
                    checkIfLetterIsInWord(s.charAt(0));

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    void checkIfLetterIsInWord(char letter) {
        //if the letter was found inside the word to be guessed
        if(GuessTheWord.indexOf(letter) >= 0){

            // if the letter was npt displayed yet
            if (wordDisPlayedString.indexOf(letter) < 0){

                //animate
                txtGuessTheWord.startAnimation(scaleAnimation);

                // replace undescore with that letter
                revealLetterInWord(letter);

                //uppdate changes in screen
                displayWordOnScreen();

                // check if the game is won
                if (!wordDisPlayedString.contains("_")){
                    trTriesLeft.startAnimation(ScaleAndRotateAnimation);
                    txtTriesLeft.setText(WINNING_MESSAGE);
                }
            }
         }
        //otherwise if the letter was not found inside the guess the word
        else{
            //decrease the number of tries left and show it on the screen
            decreaseAndDisplayTriesLeft();

            //check if the game is lost
            if (triesLeft.isEmpty()){
                trTriesLeft.startAnimation(ScaleAndRotateAnimation);
                txtTriesLeft.setText(LOOSING_MESSAGE);
                txtGuessTheWord.setText((GuessTheWord));

            }
        }
        //display the letter that was used
        if (lettersTried.indexOf(letter) < 0){
            lettersTried += letter + ", ";
            String messageToBeDisplayed = MESSAGE_WITH_LETTERS_TRIED + lettersTried;
            txtLettersUse.setText(messageToBeDisplayed);
        }

    }

    void decreaseAndDisplayTriesLeft() {
        // check if there is more tries left
        if (!triesLeft.isEmpty()) {
            //animate
            txtTriesLeft.startAnimation(scaleAnimation);
            // take out the last 2 characters
            triesLeft = triesLeft.substring(0,triesLeft.length() -2);
            txtTriesLeft.setText(triesLeft);

        }
    }

    public void resultGame(View v){
        Intent intent = new Intent(MainActivity.this,Main2Activity.class);

        intent.putExtra("tries" , triesLeft);

        startActivityForResult(intent, 0);

    }


    public void resetGame (View v) {
       //start animation
        trReset.startAnimation(rotateAnimation);
        //clear animation on table row
        trTriesLeft.clearAnimation();
        //set up new game
        initializeGame();
    }

}

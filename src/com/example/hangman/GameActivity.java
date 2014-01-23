package com.example.hangman;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends Activity {
	TextView hangmanText;
	TextView guessedText;
	EditText guessField;
	Button guessButton;
	
	String word;
	String obscuredWord;
	
	String[] words;
	
	int incorrectGuessCount;
	int score;
	int game;
	
	//Called when the game is over. Transitions back to EnterWord Screen
	protected void endGame() {
		Intent goToStartActivity = new Intent(GameActivity.this, EnterWordActivity.class);
		goToStartActivity.putExtra("score", score);
		startActivity(goToStartActivity);
	}
	
	protected void updateScore(boolean guessedWord) {
		if(guessedWord) {
			score += 10 + obscuredWord.length();
		}
		else {
			int count = 0;
			for(int i=0; i<obscuredWord.length(); i++) {
				if(obscuredWord.charAt(i) != '*') {
					count++;
				}
			}
			score+=count;
		}
		System.out.println(score);
	}
	
	protected void resetGame() {
		if(game == 3) {
			endGame();
		}else {
			word = words[game];
			obscuredWord = "";
			for(int i = 0; i<word.length(); i++)
			{
				obscuredWord += '*';
			}
			
			hangmanText.setText(obscuredWord);
			guessedText.setText("");
			incorrectGuessCount = 0;
		}
	}
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		score = 0;
		game=0;
		
		Intent intent = getIntent();
		words = intent.getStringArrayExtra("words");
		
		obscuredWord = new String();
		
		hangmanText = (TextView) this.findViewById(R.id.hangmantext);
		guessField = (EditText) this.findViewById(R.id.textBox);
		guessButton = (Button) this.findViewById(R.id.guessButton);
		guessedText = (TextView) this.findViewById(R.id.guessedLetters);
		
		resetGame();
		
		guessButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//Get the user's guess and the already guessed characters
				String guess = guessField.getText().toString();
				String alreadyGuessed = guessedText.getText().toString();
				
				if(guess.length() == 0) {
					return;
				}
				
				//clear the guess field
				guessField.setText("");
				
				Context context = getApplicationContext();
				
				if(alreadyGuessed.contains(guess)) {

					Toast toast = Toast.makeText(context, "You already guessed that", Toast.LENGTH_SHORT);
					toast.show();
				}
				else {
					if(word.contains(guess)) {
						for(int i=0; i< word.length(); i++) {
							if(word.charAt(i) == guess.charAt(0)) {
								char[] temp = obscuredWord.toCharArray();
								temp[i] = guess.charAt(0);
								obscuredWord = String.copyValueOf(temp);
								hangmanText.setText(obscuredWord);
							}
						}
						if(obscuredWord.equals(word)) {
							Toast toast = Toast.makeText(context, "You won", Toast.LENGTH_SHORT);
							toast.show();
							game++;
							updateScore(true);
							resetGame();
						}
					}
					else {
						alreadyGuessed += guess;
						incorrectGuessCount++;
						if(incorrectGuessCount == 7) { //Only 7 incorrect guesses are allowed
							Toast toast = Toast.makeText(context, "You lost", Toast.LENGTH_SHORT);
							toast.show();
							game++; 
							updateScore(false);
							resetGame();
						}else {
							guessedText.setText(alreadyGuessed);
						}
					}
						
				}
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

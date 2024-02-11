package LiorDavidi_LennyMedina;

import java.io.Serializable;
import java.util.*;

public abstract class Question implements Serializable {
	protected static int idCounter = 1000;

	public enum eDifficulty {
		Easy, Medium, Hard
	};

	protected String questionContent;
	protected int id;
	protected eDifficulty difficulty;

	public Question(String questionContent, eDifficulty difficulty) {
		this.questionContent = questionContent;
		this.difficulty = difficulty;
		this.id = idCounter++;
	}

	public static int getIdCounter() {
		return idCounter;
	}

	public String getQuestionContent() {
		return questionContent;
	}

	public void setQuestionContent(String questionContent) {
		this.questionContent = questionContent;
	}

	public eDifficulty getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(eDifficulty difficulty) {
		this.difficulty = difficulty;
	}

	public int getId() {
		return id;
	}

	protected boolean eraseAnswer(String answerContent) {
		return true;
	}

	public boolean addAnswer(Answer temp, boolean isCorrect) {
		return false;
	}

	public int getCurrentAns() {
		// TODO Auto-generated method stub
		return 0;
	}

	public ArrayList<Answer> getAllAnswers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString() {
		return this.questionContent + " (" + this.difficulty.toString() + ") - ID #" + this.id + "\n";
	}

	public String toStringExam() {
		return this.questionContent + " (" + this.difficulty.toString() + ") - ID #" + this.id + "\n";
	}

	public static void setID(int dbCurrentQuestion) {
		idCounter += dbCurrentQuestion;
	}
}

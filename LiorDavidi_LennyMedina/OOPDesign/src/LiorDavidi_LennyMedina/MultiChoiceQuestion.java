package LiorDavidi_LennyMedina;

import java.util.*;

public class MultiChoiceQuestion extends Question {
	public final int MAX_ANSWERS = 12;
	private ArrayList<Answer> allAnswers;
	private ArrayList<Boolean> isCorrectAns;// client side correct answer value display (true/false).
	private int currentAns;// shows current available index in the array.
	private int countCorrectAnswer;
	private ArrayList<Boolean> correctAnsDB;// programmer side of real answer value display (back end).

	public MultiChoiceQuestion(String questionContent, eDifficulty difficulty) {
		super(questionContent, difficulty);

		this.allAnswers = new ArrayList<>();
		this.isCorrectAns = new ArrayList<>();
		this.correctAnsDB = new ArrayList<>();
		for(int i= 0;i<MAX_ANSWERS;i++) {
			this.allAnswers.add(null);
			this.isCorrectAns.add(false);
			this.correctAnsDB.add(false);
		}
		this.isCorrectAns.set(0, true);
		this.allAnswers.set(0, new Answer("None of the below."));
		this.allAnswers.set(1, new Answer("More than one answer is correct.")) ;
		this.currentAns = 2;
		this.countCorrectAnswer = 0;
	}

	public String getQuestionContent() {
		return questionContent;

	}

	public void setQuestionContent(String questionContent) {
		this.questionContent = questionContent;
	}

	public ArrayList<Answer> getAllAnswers() {
		return allAnswers;
	}

	public int getCurrentAns() {
		return currentAns;
	}

	public void setCurrentAns(int currentAns) {
		this.currentAns = currentAns;
	}

	public ArrayList<Boolean> getCorrectAnsDB() {
		return this.correctAnsDB;
	}

	public ArrayList<Boolean> getIsCorrectAns() {
		return this.isCorrectAns;
	}

	public boolean addAnswer(Answer ans, boolean isCorrect) {
		if (this.currentAns == MAX_ANSWERS) {
			return false;
		} else {
			for (int i = 0; i < this.currentAns; i++) {
				if (ans.getAnswerContent().equals(this.allAnswers.get(i).getAnswerContent())) {
					return false;
				}
			}
			if (isCorrect) {
				this.correctAnsDB.set(this.currentAns,true);
				this.countCorrectAnswer++;
				if (!this.isCorrectAns.get(1)) {
					if (this.countCorrectAnswer == 1) {
						this.isCorrectAns.set(0, false);
						this.isCorrectAns.set(this.currentAns,true);
					} else
						multipleCorrectAnswers();
				}
			}
			this.allAnswers.set(this.currentAns, ans);
			this.currentAns++;
			return true;
		}
	}

	public void multipleCorrectAnswers() { // update "More than one answer is correct" to true and update everything
											// else as false.
		this.isCorrectAns.set(1, true);
		for (int i = 2; i < this.currentAns; i++) {
			this.isCorrectAns.set(i,false);
		}
	}

	public boolean eraseAnswer(String ansContent) {
		for (int i = 2; i < this.currentAns; i++) {
			if (this.allAnswers.get(i).getAnswerContent().equals(ansContent)) {
				eraseHelper(i);
				return true;
			}
		}
		return false;
	}

	public void eraseHelper(int i) {
		if (this.correctAnsDB.get(i)) {
			this.countCorrectAnswer--;
			switch (this.countCorrectAnswer) {
			case 0:
				this.isCorrectAns.set(0, true);
				this.isCorrectAns.set(1, false);
				break;
			case 1:
				this.isCorrectAns.set(1, false);
				for (int j = 2; j < this.currentAns; j++) {
					if (this.correctAnsDB.get(j) && i != j)
						this.isCorrectAns.set(j,true);
				}
				break;
			}
		}
		for (; i < this.currentAns - 1; i++) {
			this.allAnswers.set(i, this.allAnswers.get(i+1)) ;
			this.correctAnsDB.set(i, this.correctAnsDB.get(i+1));
			this.isCorrectAns.set(i, this.isCorrectAns.get(i+1));
		}
		this.allAnswers.set(--this.currentAns, null);
		this.correctAnsDB.set(this.currentAns, false);
		this.isCorrectAns.set(this.currentAns, false);
	}

	@Override
	public String toString() {// with indication if the answer is t/f
		StringBuilder str = new StringBuilder(super.toString());
		for (int i = 0; i < this.currentAns; i++) {
			str.append("\t" + (i + 1) + ") " + this.allAnswers.get(i).toString() + " "
					+ (this.isCorrectAns.get(i) ? "(T)" : "(F)") + "\n");
		}
		return str.toString() + "\n";
	}

	public String toStringExam() {// without indication
		StringBuilder str = new StringBuilder(super.toString());
		for (int i = 0; i < this.currentAns; i++) {
			str.append("\t" + (i + 1) + ") " + this.allAnswers.get(i).toString() + "\n");
		}
		return str.toString() + "\n";
	}
}

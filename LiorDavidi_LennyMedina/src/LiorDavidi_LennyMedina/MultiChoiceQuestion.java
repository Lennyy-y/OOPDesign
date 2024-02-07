package LiorDavidi_LennyMedina;

import java.util.*;

public class MultiChoiceQuestion extends Question {
	public final int MAX_ANSWERS = 12;
	private HashSet<Answer> allAnswers; //***Changed
	private int countCorrectAnswer;
	private boolean[] correctAnsDB;// programmer side of real answer value display (back end).

	public MultiChoiceQuestion(String questionContent, eDifficulty difficulty) {
		super(questionContent, difficulty);

		this.allAnswers = new HashSet<>();
		this.allAnswers.add(new Answer("None of the below.", true));
		this.allAnswers.add(new Answer("More than one answer is correct."));
		this.countCorrectAnswer = 0;
	}

	public String getQuestionContent() {
		return questionContent;

	}

	public void setQuestionContent(String questionContent) {
		this.questionContent = questionContent;
	}

	public HashSet<Answer> getAllAnswers() {
		return allAnswers;
	}


	public int getAnswerAmount() {
		return this.allAnswers.size();
	}


	public boolean addAnswer(Answer ans) {
		if (!this.allAnswers.add(ans)) {
			return false;	
		}
		return true;
	}

	public void multipleCorrectAnswers() { // update "More than one answer is correct" to true and update everything
											// else as false.
		for (Answer answer : allAnswers) {
			if (answer.getAnswerContent().equals("More than one answer is correct."))
				answer.setTruthValue(true);
			else
				answer.setTruthValue(false);
		}
	}

	public boolean eraseAnswer(String ansContent) {
		for (Answer answer : allAnswers) {
			if(answer.getAnswerContent().equals(ansContent)) {
				allAnswers.remove(answer);
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {// with indication if the answer is t/f
		StringBuilder str = new StringBuilder(super.toString());
		for (Answer answer : allAnswers) {
			int i = 1;
			str.append("\t" + i + ") " + answer.toString() + " "
					+ (answer.getTruthValue() ? "(T)" : "(F)") + "\n");
			i++;
		}
			
			

		return str.toString() + "\n";
	}

	public String toStringExam() {// without indication
		StringBuilder str = new StringBuilder(super.toString());
		for (Answer answer : allAnswers) {
			int i = 1;
			str.append("\t" + i + ") " + answer.toString() + "\n");
			i++;		
		}
		return str.toString() + "\n";
	}
}

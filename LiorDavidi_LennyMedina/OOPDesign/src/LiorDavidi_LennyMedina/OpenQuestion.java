package LiorDavidi_LennyMedina;

import java.util.*;


public class OpenQuestion extends Question {

	private Answer answerContent;

	public OpenQuestion(String questionContent, Answer answerContent, eDifficulty difficulty) {
		super(questionContent, difficulty);
		this.answerContent = answerContent;
	}

	public Answer getAnswerContent() {
		return answerContent;
	}

	public void setAnswerContent(Answer answerContent) {
		this.answerContent = answerContent;
	}

	@Override
	public String toString() {// with correct answer
		return super.toString() + "Answer: " + this.answerContent + "\n";
	}

	public String toStringExam() {// without answer
		return super.toString() + "Answer: \n";
	}
}

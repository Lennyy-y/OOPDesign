package LiorDavidi_LennyMedina;

import java.io.Serializable;

public class Answer implements Serializable {
	private String answerContent;
	private boolean isCorrect;

	public Answer(String answerContent) {
		this.answerContent = answerContent;
		this.isCorrect = false;
	}
	
	public Answer(String answerContent, boolean isCorrect) {
		this.answerContent = answerContent;
		this.isCorrect = isCorrect;
	}

	public Answer(Answer copy) {
		this.answerContent = copy.getAnswerContent();
		this.isCorrect = copy.isCorrect;
	}

	public String getAnswerContent() {
		return this.answerContent;
	}
	public boolean getTruthValue() {
		return this.isCorrect;
	}
	public void setAnswerContent(String answerContent) {
		this.answerContent = answerContent;
	}
	public void setTruthValue(boolean isCorrect) {
		this.isCorrect = isCorrect;
	}

	@Override
	public String toString() {
		return this.answerContent;
	}

}

package LiorDavidi_LennyMedina;

import java.io.Serializable;
import java.util.*;


public class Answer implements Serializable {
	private String answerContent;

	public Answer(String answerContent) {
		this.answerContent = answerContent;
	}

	public Answer(Answer copy) {
		this.answerContent = copy.getAnswerContent();
	}

	public String getAnswerContent() {
		return answerContent;
	}

	public void setAnswerContent(String answerContent) {
		this.answerContent = answerContent;
	}

	@Override
	public String toString() {
		return this.answerContent;
	}

}

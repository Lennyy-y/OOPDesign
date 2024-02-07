package LiorDavidi_LennyMedina;

import java.io.Serializable;
import java.util.*;

public class Database implements Serializable {
	private HashSet<Question> dbQuestions;
	private HashSet<Answer> dbAnswers;

	public Database() {
		this.dbQuestions = new HashSet<>();
		this.dbAnswers = new HashSet<>();
	}

	public HashSet<Question> getDbQuestions() {
		return dbQuestions;
	}

	public void setDbQuestions(HashSet<Question> dbQuestions) {
		this.dbQuestions = dbQuestions;
	}


	public HashSet<Answer> getDbAnswers() {
		return dbAnswers;
	}

	public void setDbAnswers(HashSet<Answer> dbAnswers) {
		this.dbAnswers = dbAnswers;
	}


	public boolean addAnswer(Answer ans) {
		if (!dbAnswers.add(ans))
			return false;
		return true;
	}

	public boolean addAnswerToQuestion(Answer ans, MultiChoiceQuestion ques, boolean isCorrect) {
		return ques.addAnswer(ans, isCorrect);
	}

	public void setOpenAnswer(OpenQuestion ques, String ans) {
		ques.setAnswerContent(ans);
	}

	public boolean addQuestion(Question ques) {
		if (!dbQuestions.add(ques))
			return false;
		return true;
	}

	public String qToString() {
		StringBuilder str = new StringBuilder();
		for (Question question : dbQuestions) {
			int i = 1;
			str.append("Q." + (i + 1) + ") " + question.toString() + "\n");
			i++;
		}
		return str.toString();
	}

	public String aToString() {
		StringBuilder str = new StringBuilder();
		for (Answer answer : dbAnswers) {
			int i = 1;
			str.append("Q." + (i + 1) + ") " + answer.toString() + "\n");
			i++;
		}
		return str.toString();
	}

	public boolean eraseQuestion(String eraseContent) {
		for (Question question : dbQuestions) {
			if (question.getQuestionContent().equals(eraseContent)) {
				dbQuestions.remove(question);
				return true;
			}
		}
		return false;
	}
}

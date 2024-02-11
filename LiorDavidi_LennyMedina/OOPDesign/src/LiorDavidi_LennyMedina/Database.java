package LiorDavidi_LennyMedina;

import java.io.Serializable;
import java.util.*;


public class Database implements Serializable {
	private ArrayList<Question> dbQuestions;
	private int currentQuestion;
	private ArrayList<Answer> dbAnswers;
	private int currentAnswer;

	public Database() {
		this.dbQuestions = new ArrayList<>();
		this.dbAnswers = new ArrayList<>();
		this.currentAnswer = 0;
		this.currentQuestion = 0;
	}

	public ArrayList<Question> getDbQuestions() {
		return dbQuestions;
	}

	public void setDbQuestions(ArrayList<Question> dbQuestions) {
		this.dbQuestions = dbQuestions;
	}

	public int getCurrentQuestion() {
		return currentQuestion;
	}

	public void setCurrentQuestion(int currentQuestion) {
		this.currentQuestion = currentQuestion;
	}

	public ArrayList<Answer> getDbAnswers() {
		return dbAnswers;
	}

	public void setDbAnswers(ArrayList<Answer> dbAnswers) {
		this.dbAnswers = dbAnswers;
	}

	public int getCurrentAnswer() {
		return currentAnswer;
	}

	public void setCurrentAnswer(int currentAnswer) {
		this.currentAnswer = currentAnswer;
	}

	public boolean addAnswer(Answer ans) {
		for (int i = 0; i < currentAnswer; i++) {
			if (ans.getAnswerContent().equals(dbAnswers.get(i).getAnswerContent())) {
				return false;
			}

		}
		dbAnswers.add(ans);
		this.currentAnswer++;
		return true;
	}

	public boolean addAnswerToQuestion(Answer ans, MultiChoiceQuestion ques, boolean isCorrect) {
		return ques.addAnswer(ans, isCorrect);
	}

	public void setOpenAnswer(OpenQuestion ques, Answer ans) {
		ques.setAnswerContent(ans);
	}

	public boolean addQuestion(Question ques) {
		for (int i = 0; i < currentQuestion; i++) {
			if (ques.getQuestionContent().equals(dbQuestions.get(i).getQuestionContent())) {
				return false;
			}
		}
		dbQuestions.add(ques);
		this.currentQuestion++;
		return true;
	}

	public String qToString() {
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < this.currentQuestion; i++) {
			str.append("Q." + (i + 1) + ") " + this.dbQuestions.get(i).toString() + "\n");
		}
		return str.toString();
	}

	public String aToString() {
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < this.currentAnswer; i++) {
			str.append("A." + (i + 1) + ") " + this.dbAnswers.get(i).toString() + "\n");
		}
		return str.toString();
	}

	public boolean eraseQuestion(String eraseContent) {
		for (int i = 0; i < this.currentAnswer; i++) {
			if (this.dbQuestions.get(i).getQuestionContent().equals(eraseContent)) {
				this.dbQuestions.remove(i);
				return true;
			}
		}
		return false;
	}

	public boolean eraseAnswer(String eraseContent) {
		for (int i = 0; i < this.currentAnswer; i++) {
			if (this.dbAnswers.get(i).getAnswerContent().equals(eraseContent)) {
				this.dbAnswers.remove(i);
				return true;
			}
		}
		return false;
	}

}

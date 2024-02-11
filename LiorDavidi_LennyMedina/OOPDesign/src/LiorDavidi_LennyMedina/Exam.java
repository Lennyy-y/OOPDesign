package LiorDavidi_LennyMedina;

import java.io.File;
import java.util.*;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.time.LocalDate;

public abstract class Exam implements Exammable, Serializable {
	protected ArrayList<Question> examQuestions;
	protected int currentQuestion;
	protected int maxQuestions = 10;

	public Exam() {
		this.examQuestions = new ArrayList<Question>();
		this.currentQuestion = 0;
	}

	public Exam(int max) {
		this.examQuestions = new ArrayList<Question>();
		this.currentQuestion = 0;
		maxQuestions = max;
	}

	public int getCurrentQuestion() {
		return this.currentQuestion;
	}

	public ArrayList<Question> getExamQuestions() {
		return this.examQuestions;
	}

	public boolean addQuestion(Question q) {
		if (this.currentQuestion != 0) {
			for (int i = 0; i < this.currentQuestion; i++) {
				if (this.examQuestions.get(i).getQuestionContent().equals(q.getQuestionContent()))
					return false;
			}
		}
		this.examQuestions.add(q);
		this.currentQuestion++;
		return true;
	}

	public boolean verifyQuestion(Question q, int currentQuestion)
			throws BelowThreeAnswersException, MaxQuestionExceededException {
		if (currentQuestion >= 10) {
			throw new MaxQuestionExceededException("Exam cannot exceed 10 questions.");
		} else if (q instanceof MultiChoiceQuestion && q.getCurrentAns() <= 3) {
			throw new BelowThreeAnswersException("Question has less than three answers.");
		}
		return addQuestion(q);
	}

	public boolean updateQuestion(String oldContent, String newContent) {
		for (int i = 0; i < this.currentQuestion; i++) {
			if (this.examQuestions.get(i).getQuestionContent().equals(oldContent)) {
				this.examQuestions.get(i).setQuestionContent(newContent);
				return true;
			}
		}
		return false;
	}

	public boolean updateAnswer(String oldContent, String newContent) {
		for (int i = 0; i < this.currentQuestion; i++) {
			if (this.examQuestions.get(i) instanceof OpenQuestion) {
				OpenQuestion temp = (OpenQuestion) this.examQuestions.get(i);
				if (temp.getAnswerContent().getAnswerContent().equals(oldContent)) {
					temp.setAnswerContent(new Answer(newContent));
					return true;
				}
			} else {
				MultiChoiceQuestion temp = (MultiChoiceQuestion) this.examQuestions.get(i);
				for (int j = 0; j < temp.getCurrentAns(); j++) {
					if (temp.getAllAnswers().get(j).getAnswerContent().equals(oldContent)) {
						temp.getAllAnswers().get(j).setAnswerContent(newContent);
						return true;
					}
				}
			}
		}
		return false;
	}

	public boolean eraseQuestion(String eraseContent) {
		for (int i = 0; i < this.currentQuestion; i++) {
			if (this.examQuestions.get(i).getQuestionContent().equals(eraseContent)) {
				eraseHelper(i);
				return true;
			}
		}
		return false;
	}

	public void eraseHelper(int i) {
		this.examQuestions.remove(i);
	}

	public void examToFile(String filePath) throws FileNotFoundException {
		File f = new File(filePath);
		PrintWriter pw = new PrintWriter(f);
		LocalDate date = LocalDate.now();
		pw.println(
				"Exam Page " + date.getDayOfMonth() + "/" + date.getMonthValue() + "/" + (date.getYear() % 100) + "\n");
		pw.println(toStringExam());
		pw.close();
	}

	public void solutionToFile(String filePath) throws FileNotFoundException {
		File f = new File(filePath);
		PrintWriter pw = new PrintWriter(f);
		LocalDate date = LocalDate.now();
		pw.println("Solution Page " + date.getDayOfMonth() + "/" + date.getMonthValue() + "/" + (date.getYear() % 100)
				+ "\n");
		pw.println(toString());
		pw.close();
	}

	@Override
	public String toString() {// with answer indication
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < this.currentQuestion; i++) {
			str.append("Q." + (i + 1) + ") " + this.examQuestions.get(i).toString() + "\n");
		}
		return str.toString();
	}

	public String toStringExam() {// without indication
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < this.currentQuestion; i++) {
			str.append("Q." + (i + 1) + ") " + this.examQuestions.get(i).toStringExam() + "\n");
		}
		return str.toString();
	}
}

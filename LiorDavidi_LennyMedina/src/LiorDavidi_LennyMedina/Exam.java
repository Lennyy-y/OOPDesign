package LiorDavidi_LennyMedina;

import java.io.File;
import java.util.*;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.time.LocalDate;

public abstract class Exam implements Exammable, Serializable {
	protected LinkedHashSet<Question> examQuestions;

	protected int maxQuestions;

	public Exam() {
		this.examQuestions = new LinkedHashSet<>();
		this.maxQuestions = 0;
	}
	public Exam(int maxQuestions) {
		this.examQuestions = new LinkedHashSet<>();
		this.maxQuestions = maxQuestions;
		
	}
	public int getMaxQuestions() {
		return this.maxQuestions;
	}

	public int getQuestionAmount() {
		return this.examQuestions.size();
	}

	public LinkedHashSet<Question> getExamQuestions() {
		return this.examQuestions;
	}

	public boolean addQuestion(Question q) {
		if (!this.examQuestions.add(q))
			return false;
		return true;
	}

	public boolean verifyQuestion(Question q)
			throws BelowThreeAnswersException, MaxQuestionExceededException {
		if (this.getQuestionAmount() >= this.getMaxQuestions()) {
			throw new MaxQuestionExceededException("Exam cannot exceed " + this.getMaxQuestions() + " questions.");
		} else if (q instanceof MultiChoiceQuestion && q.getCurrentAns() <= 3) {
			throw new BelowThreeAnswersException("Question has less than three answers.");
		}
		return addQuestion(q);
	}

	public boolean updateQuestion(String oldContent, String newContent) {
		for (Question question : examQuestions) {
			if (question.getQuestionContent().equals(oldContent)) {
				question.setQuestionContent(newContent);
				return true;
			}
		}
		return false;
	}

//	public boolean updateAnswer(String oldContent, String newContent) { *** Need to come up with new logic.
//		
//		for (Question question : examQuestions) {
//			for (Answer question2 : examQuestions) {
//				
//			}
//		}
//		return false;
//	}

	public boolean eraseQuestion(String eraseContent) {
		for (Question question : examQuestions) {
			if (question.getQuestionContent().equals(eraseContent)) {
				examQuestions.remove(question);
				return true;
			}
				
		}
		return false;
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
		for (Question question : examQuestions) {
			int i = 1;
			str.append("Q." + i + ") " + question.toString() + "\n");
			i++;
			
		}
		
		return str.toString();
	}

	public String toStringExam() {// without indication
		StringBuilder str = new StringBuilder();
		for (Question question : examQuestions) {
			int i = 1;
			str.append("Q." + i + ") " + question.toStringExam() + "\n");
			i++;
		}
		return str.toString();
	}
}

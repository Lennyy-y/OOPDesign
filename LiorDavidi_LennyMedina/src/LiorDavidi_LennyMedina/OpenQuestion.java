package LiorDavidi_LennyMedina;

public class OpenQuestion extends Question {

	private Answer answer;

	public OpenQuestion(String questionContent, Answer answer, eDifficulty difficulty) {
		super(questionContent, difficulty);
		this.answer = answer;
	}

	public String getAnswerContent() {
		return answer.getAnswerContent();
	}

	public void setAnswerContent(String answerContent) {
		this.answer.setAnswerContent(answerContent);
	}

	@Override
	public String toString() {// with correct answer
		return super.toString() + "Answer: " + this.answer.getAnswerContent() + "\n";
	}

	public String toStringExam() {// without answer
		return super.toString() + "Answer: \n";
	}
}

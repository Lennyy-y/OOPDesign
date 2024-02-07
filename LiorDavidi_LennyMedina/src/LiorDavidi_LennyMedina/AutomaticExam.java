package LiorDavidi_LennyMedina;

import java.util.Iterator;

public class AutomaticExam extends Exam {
	public AutomaticExam(int qAmount) {
		super(qAmount);
	}

	@Override
	public void createExam(Database db) {
		for (int i = 0; i < this.examQuestions.length;) {
			if (addQuestion(db.getDbQuestions()[Program.rand.nextInt(db.getCurrentQuestion())])) {
				if (this.getExamQuestions()[i] instanceof MultiChoiceQuestion) {
					for (int j = this.getExamQuestions()[i].getCurrentAns(); j < 6; ) {
						if (this.examQuestions[i].addAnswer(db.getDbAnswers()[Program.rand.nextInt(db.getCurrentAnswer())], false)) {
							j++;
						}
					}
				}
				i++;
			}
		}
	}

}

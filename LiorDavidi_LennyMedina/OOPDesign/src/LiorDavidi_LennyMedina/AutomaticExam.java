package LiorDavidi_LennyMedina;

import java.util.*;

public class AutomaticExam extends Exam {
	public AutomaticExam(int qAmount) {
		super(qAmount);
	}

	@Override
	public void createExam(Database db) {
		for (int i = 0; i < this.examQuestions.size();) {
			if (addQuestion(db.getDbQuestions().get(Program.rand.nextInt(db.getCurrentQuestion())))) {
				if (this.getExamQuestions().get(i) instanceof MultiChoiceQuestion) {
					for (int j = this.getExamQuestions().get(i).getCurrentAns(); j < 6; ) {
						if (this.examQuestions.get(i).addAnswer(db.getDbAnswers().get(Program.rand.nextInt(db.getCurrentAnswer())), false)) {
							j++;
						}
					}
				}
				i++;
			}
		}
	}

}

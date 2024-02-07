package LiorDavidi_LennyMedina;

public class ManualExam extends Exam {
	public ManualExam() {
		super();
	}
	
	public ManualExam(int maxQuestions) {
		super(maxQuestions);
	}

	@Override
	public void createExam(Database db) {
		int qAmount = this.getExamQuestions().length;
		System.out.println("Please choose " + qAmount + " questions from the following:");
		System.out.println(db.qToString());
		int[] qSelection = new int[qAmount];
		for (int i = 0; i < qAmount; i++) {
			System.out.println("Please choose a question:");
			qSelection[i] = Program.validateInput(1, db.getCurrentQuestion());
			System.out.println("Question " + qSelection[i] + " selected.");
			if (db.getDbQuestions()[qSelection[i] - 1] instanceof OpenQuestion) {
				OpenQuestion temp = (OpenQuestion) db.getDbQuestions()[qSelection[i] - 1];
				if (temp.getAnswerContent().getAnswerContent().equals("No answer has been added.")) {
					System.out.println("This question has no answer, please select an answer from the following:");
					System.out.println(db.aToString());
					int aSelection = Program.validateInput(1, db.getCurrentAnswer());
					System.out.println("Answer " + aSelection + " selected.");
					temp.setAnswerContent(db.getDbAnswers()[aSelection - 1]);
				}
				addQuestion(temp);
			} else {
				boolean isNumOfAns = false;
				while (!isNumOfAns) {
					System.out.println("Please choose up to 10 answers from the following, choose 0 to stop");
					System.out.println(db.aToString());
					int[] aSelection = new int[12 - db.getDbQuestions()[qSelection[i] - 1].getCurrentAns()];
					for (int j = 0; j < aSelection.length; j++) {
						System.out.println("Please choose an answer (type 0 to stop):");
						aSelection[j] = Program.validateInput(0, db.getCurrentAnswer());
						if (aSelection[j] == 0)
							break;
						System.out.println("Answer " + aSelection[j] + " selected.");
						System.out.println("Is the answer correct? (true/false)");
						if (!db.getDbQuestions()[qSelection[i] - 1].addAnswer(db.getDbAnswers()[aSelection[j] - 1],
								Program.input.nextBoolean())) {
							System.out.println("Answer already exists for this question.");
						}
					}
					try {
						verifyQuestion(db.getDbQuestions()[qSelection[i] - 1], getCurrentQuestion());
						isNumOfAns = true;
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}
				}
			}
		}
	}

}

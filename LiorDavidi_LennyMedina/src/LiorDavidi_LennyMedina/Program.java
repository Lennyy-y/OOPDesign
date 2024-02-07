package LiorDavidi_LennyMedina;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.Scanner;

public class Program {
	public static Scanner input = new Scanner(System.in);
	public static boolean clearBuffer = false;
	public static Random rand = new Random(69420);

	public static void main(String[] args) throws Exception {
		ObjectInputStream inFile = new ObjectInputStream(new FileInputStream("Database.dat"));
		MegaDatabase allDBs = (MegaDatabase) inFile.readObject();
		inFile.close();

//		in case of uid serial mismatch, run backup method below.
//		MegaDatabase allDBs = loadBackupDB();

		ObjectInputStream inFileID = new ObjectInputStream(new FileInputStream("id.dat"));
		Question.setID((int) inFileID.readObject());
		inFileID.close();

		boolean subjectFlag = true;
		while (subjectFlag) {
			int subjectCount = allDBs.getCurrentSubject();
			StringBuilder str = new StringBuilder();
			for (int i = 0; i < subjectCount; i++) {
				str.append((i + 1) + ") " + allDBs.getAllSubjectDNames()[i] + ".\n");
			}
			System.out.println("Subjects menu (unsaved changes will be lost):\n" + str.toString() + ""
					+ (subjectCount + 1) + ") Create a subject.\n" + (subjectCount + 2) + ") Exit program.");
			int subjectOption = validateInput(1, subjectCount + 2);
			if (subjectOption == subjectCount + 1) {
				// add subject
				System.out.println("Please enter a new subject.");
				if (clearBuffer) {
					input = new Scanner(System.in);
				}
				String subjectName = input.nextLine();
				allDBs.verifySubject(new Database(), subjectName);
				System.out.println("Subject added successfully.\n");
			} else if (subjectOption == subjectCount + 2) {
				// exit program
				System.out.println("Program aborted successfully.");
				subjectFlag = false;
			} else {
				Database db = allDBs.getAllSubjectDBs()[subjectOption - 1];
				ManualExam exam = new ManualExam();
				boolean mainMenuFlag = true;
				while (mainMenuFlag) {
					System.out.println(
							"Main menu:\n1) Add question.\n2) Modify existing questions.\n3) Modify answers of an existing question.\n4) Display database questions to the console.\n5) Display database answers to the console.\n6) Display exam to the console.\n7) Create & export an exam manually (WARNING: Once selected, cannot back out).\n8) Create & export an exam automatically (WARNING: Once selected, cannot back out).\n9) Finish & export.\n10) Return to subject menu.");
					int mainMenuOption = validateInput(1, 10);
					switch (mainMenuOption) {
					case 1:
						// add question
						menu1Func(exam, db);
						break;

					case 2:
						// modifying existing questions
						menu2Func(exam, db);
						break;

					case 3:
						// modify answers of an existing question
						menu3Func(exam, db);
						break;

					case 4:
						// display db questions and their corresponding answers to console
						System.out.println(db.qToString());
						break;

					case 5:
						// display db answers to console
						System.out.println(db.aToString());
						break;

					case 6:
						// display exam to console
						System.out.print(exam.toString());
						break;

					case 7:
						// create an exam manually
						menu7Func(db, allDBs);
						mainMenuFlag = false;
						subjectFlag = false;
						break;

					case 8:
						// create & export automatic exam
						menu8Func(db, allDBs);
						mainMenuFlag = false;
						subjectFlag = false;
						break;
					case 9:
						// finish and export
						menu9Func(exam, allDBs);
						mainMenuFlag = false;
						subjectFlag = false;
						break;
					case 10:
						// back out to subject menu
						mainMenuFlag = false;
						break;
					}
				}
			}
		}
	}

	public static void menu1Func(Exam exam, Database db)
			throws BelowThreeAnswersException, MaxQuestionExceededException {
		boolean option1Flag = true;
		while (option1Flag) {
			System.out.println(
					"Add question menu:\n1) Create new question.\n2) Choose an existing question from database.\n3) Return to main menu.");
			int option1 = validateInput(1, 3);
			switch (option1) {
			case 1:
				// create new question
				System.out.println(
						"Please enter the desired question type:\n1)Open-Ended Question\n2)Multiple Choice Question");
				int choice = validateInput(1, 2);
				System.out.println("Please enter the question's content.");
				if (clearBuffer) {
					input = new Scanner(System.in);
					clearBuffer = false;
				}
				String question = input.nextLine();
				clearBuffer = true;
				System.out.println("Set question difficulty:\n1) " + Question.eDifficulty.values()[0] + "\n2) "
						+ Question.eDifficulty.values()[1] + "\n3) " + Question.eDifficulty.values()[2]);
				int diffChoice = validateInput(1, 3);
				if (choice == 2) {
					MultiChoiceQuestion tempQ = new MultiChoiceQuestion(question,
							Question.eDifficulty.values()[diffChoice - 1]);
					if (db.addQuestion(tempQ)) {
						// change this to match 6 answers in a question either by choosing or by random
						// generated answers
						// change exam.addQuestion to exam.verifyQuestion
						System.out.println(
								"A multiple choice question must have a minimum of 4 answers.\n1) Generate randomly from the database.\n2) Choose from the database.\n");
						if (validateInput(1, 2) == 1) {
							for (int i = 0; i < 4; i++) {
								tempQ.addAnswer(db.getDbAnswers()[rand.nextInt(db.getCurrentAnswer())],
										rand.nextInt(4) == 1 ? true : false);
							}
						} else {
							System.out.println(db.aToString());
							System.out.println("Please choose an answer.");
							for (int i = 0; i < 4;) {
								int qChoice = validateInput(1, db.getDbAnswers().length);
								if (tempQ.addAnswer(db.getDbAnswers()[qChoice], rand.nextInt(4) == 1 ? true : false)) {
									if (i != 3) {
										System.out.println("Answer added successfully, please choose " + (3 - i)
												+ " more answer" + (i != 2 ? "s." : "."));
									}
									i++;
								}
							}
						}
						exam.verifyQuestion(tempQ, exam.getCurrentQuestion());
						System.out.println("Question created successfully.\n");
					} else {
						System.out.println("Question already exists.\n");
					}
				} else {
					System.out.println("WARNING: An open-ended question should not be left unanswered.");
					OpenQuestion tempQ = new OpenQuestion(question, new Answer("No answer has been added."),
							Question.eDifficulty.values()[diffChoice - 1]);
					if (db.addQuestion(tempQ)) {
						exam.addQuestion(tempQ);
						System.out.println("Question created successfully.\n");
					} else {
						System.out.println("Question already exists.\n");
					}
				}
				break;
			case 2:
				// choose an existing question
				System.out.println("Please choose from the following.");
				System.out.println(db.qToString());
				System.out.println("Type \"" + (db.getCurrentQuestion() + 1) + "\" to return to main menu.");
				choice = validateInput(1, db.getCurrentQuestion() + 1);
				if (choice == db.getCurrentQuestion() + 1) {
					option1Flag = false;
				} else {
					try {
						if (exam.verifyQuestion(db.getDbQuestions()[choice - 1], exam.getCurrentQuestion())) {
							System.out.println(
									"Question #" + (choice) + " from the Database has been successfully added.\n");
						} else {
							System.out.println("Question already exists.\n");
						}
					} catch (BelowThreeAnswersException e) {
						System.out.println(e.getMessage());
					} catch (MaxQuestionExceededException e) {
						System.out.println("Failed to add question, the exam already has 10 questions.");
					}
				}
				break;
			case 3:
				// return to main menu
				option1Flag = false;
				break;
			}
		}
	}

	public static void menu2Func(Exam exam, Database db) {
		boolean option2Flag = true;
		while (option2Flag) {
			System.out.println(
					"Modify questions menu:\n1) Update question content.\n2) Remove question from exam.\n3) Remove question from Database & exam.\n4) Return to main menu.");
			int option2 = validateInput(1, 4);
			switch (option2) {
			case 1: {
				// update question content
				int qCount = db.getCurrentQuestion();
				if (qCount == 0) {
					System.out.println("There are no existing questions, Returning to main menu...\n");
					option2Flag = false;
				} else {
					Question[] dbQuestions = db.getDbQuestions();
					System.out.println("Choose the question you want to modify.");
					for (int i = 0; i < qCount; i++) {
						System.out.println((i + 1) + " - " + dbQuestions[i].getQuestionContent());
					}
					System.out.println("Type \"" + (qCount + 1) + "\" to return to main menu.");
					int choice = validateInput(1, qCount + 1);
					if (choice == qCount + 1) {
						option2Flag = false;
					} else {
						System.out.println("Question selected: " + (choice) + ", please enter the updated text.");
						if (clearBuffer) {
							input = new Scanner(System.in);
							clearBuffer = false;
						}
						String newQues = input.nextLine();
						clearBuffer = true;
						exam.updateQuestion(dbQuestions[choice - 1].getQuestionContent(), newQues);
						System.out.println("Question updated successfully...\n");
					}
				}
				break;
			}
			case 2: {
				// remove question from exam
				int qCount = exam.getCurrentQuestion();
				if (qCount == 0) {
					System.out.println("There are no existing questions, Returning to main menu...\n");
					option2Flag = false;
				} else {
					Question[] examQuestions = exam.getExamQuestions();
					System.out.println("Choose the question you want to remove.");
					for (int i = 0; i < qCount; i++) {
						System.out.println((i + 1) + " - " + examQuestions[i].getQuestionContent());
					}
					System.out.println("Type \"" + (qCount + 1) + "\" to return to main menu.");
					int choice = validateInput(1, qCount + 1);
					if (choice == qCount + 1) {
						option2Flag = false;
					} else {
						System.out.println("Question selected: " + (choice));
						exam.eraseQuestion(examQuestions[choice - 1].getQuestionContent());
						System.out.println("Question removed successfully...\n");
					}
				}
				break;
			}
			case 3:
				// remove question from DB & exam
				int qCount = db.getCurrentQuestion();
				if (qCount == 0) {
					System.out.println("There are no existing questions, Returning to main menu...\n");
					option2Flag = false;
				} else {
					Question[] dbQuestions = db.getDbQuestions();
					System.out.println("Choose the question you want to remove.");
					for (int i = 0; i < qCount; i++) {
						System.out.println((i + 1) + " - " + dbQuestions[i].getQuestionContent());
					}
					System.out.println("Type \"" + (qCount + 1) + "\" to return to main menu.");
					int choice = validateInput(1, qCount + 1);
					if (choice == qCount + 1) {
						option2Flag = false;
					} else {
						System.out.println("Question selected: " + (choice));
						exam.eraseQuestion(dbQuestions[choice - 1].getQuestionContent());
						db.eraseQuestion(dbQuestions[choice - 1].getQuestionContent());
						System.out.println("Question removed successfully from the exam and the Database.\n");
					}
				}
				break;

			case 4:
				// return to main menu
				option2Flag = false;
				break;
			}
		}

	}

	public static void menu3Func(Exam exam, Database db) {
		int qCount = db.getCurrentQuestion();
		if (qCount == 0) {
			System.out.println("There are no existing questions, Returning to main menu...\n");
		} else {
			Question[] dbQuestions = db.getDbQuestions();
			System.out.println("Choose the question you want to modify.");
			for (int i = 0; i < qCount; i++) {
				System.out.println((i + 1) + " - " + dbQuestions[i].getQuestionContent());
			}
			System.out.println("Type \"" + (qCount + 1) + "\" to return to main menu.");
			int qChoice = validateInput(1, qCount + 1);
			if (qChoice != qCount + 1) {
				boolean option3Flag = true;
				while (option3Flag) {
					int option3;
					if (dbQuestions[qChoice - 1] instanceof OpenQuestion) {
						System.out.println("Modify answers menu:\n1) Edit answer's content.\n2) Return to main menu.");
						option3 = validateInput(1, 2);
						if (option3 == 1) // indices are different, 1 -> 2, 2 -> 5
							option3 = 2;
						else
							option3 = 5;
					} else {
						System.out.println(
								"Modify answers menu:\n1) Add a new answer.\n2) Edit answer's content.\n3) Remove an answer from the selected question.\n4) Remove an answer from Database and exam.\n5) Return to main menu.");
						option3 = validateInput(1, 5);
					}
					switch (option3) {
					case 1:
						// add new answer
						boolean option31Flag = true;
						while (option31Flag) {
							System.out.println(
									"Add answer menu:\n1) Create new answer.\n2) Choose an existing answer from database.\n3) Return to Modify answers menu.\n4) Return to main menu.");
							int option31 = validateInput(1, 4);
							switch (option31) {
							case 1:
								// create new answer
								System.out.println("Please enter an answer.");
								if (clearBuffer) {
									input = new Scanner(System.in);
									clearBuffer = false;
								}
								String answer = input.nextLine();
								clearBuffer = true;
								System.out.println("Is the answer correct? (true/false).");
								boolean isCorrect = input.nextBoolean();
								Answer temp = new Answer(answer);
								if (dbQuestions[qChoice - 1].addAnswer(temp, isCorrect)) {
									System.out.println("Answer added successfully.");
									db.addAnswer(temp);
								} else {
									System.out.println("Failed to add answer.");
								}
								break;
							case 2:
								// choose existing from db
								System.out.println("Choose from the following:");
								System.out.println(db.aToString());
								System.out.println(
										"Type \"" + (db.getCurrentAnswer() + 1) + "\" to return to main menu.");
								int aChoice = validateInput(1, db.getCurrentAnswer() + 1);
								if (aChoice == db.getCurrentAnswer() + 1) {
									option31Flag = false;
									option3Flag = false;
								} else {
									System.out.println("Is the answer true/false?");
									boolean isCorrect1 = input.nextBoolean();
									System.out.println("Question selected: " + (aChoice));
									if (db.getDbQuestions()[qChoice - 1].addAnswer(db.getDbAnswers()[aChoice - 1],
											isCorrect1)) {
										System.out.println("Answer added successfully to the question.\n");
									} else {
										System.out.println("Failed to add answer.");
									}
								}

								break;
							case 3:
								// return to modify answer menu
								option31Flag = false;

								break;
							case 4:
								// return to main menu
								option31Flag = false;
								option3Flag = false;
								break;
							}
						}
						break;
					case 2:
						// edit answer content
						int aCount = db.getCurrentAnswer();
						if (aCount == 0) {
							System.out.println("There are no answers, returning to main menu.");
						} else {
							if (dbQuestions[qChoice - 1] instanceof OpenQuestion) {
								OpenQuestion temp = (OpenQuestion) dbQuestions[qChoice - 1];
								System.out.println("Current answer: " + temp.getAnswerContent()
										+ "\nPlease enter the updated text:");
								if (clearBuffer) {
									input = new Scanner(System.in);
									clearBuffer = false;
								}
								String newAns = input.nextLine();
								clearBuffer = true;
								temp.setAnswerContent(new Answer(newAns));
								System.out.println("Answer updated successfully.\n");
							} else {
								System.out.println("Choose the answer you want to modify.");
								System.out.println(db.aToString());
								System.out.println("Type \"" + (aCount + 1) + "\" to return to main menu.");
								int aChoice = validateInput(1, aCount + 1);
								if (aChoice != aCount + 1) {
									System.out.println(
											"Answer selected: " + (aChoice) + ", please enter the updated text:");
									if (clearBuffer) {
										input = new Scanner(System.in);
										clearBuffer = false;
									}
									String newAns = input.nextLine();
									clearBuffer = true;
									db.getDbAnswers()[aChoice - 1].setAnswerContent(newAns);
									System.out.println("Answer updated successfully.\n");
								} else {
									option3Flag = false;
								}
							}
						}
						break;
					case 3:
						// remove answer from question
						System.out.println("Choose from the following:");
						for (int i = 2; i < db.getDbQuestions()[qChoice - 1].getCurrentAns(); i++) {
							MultiChoiceQuestion temp = (MultiChoiceQuestion) db.getDbQuestions()[qChoice - 1];
							System.out.println((i + 1) + " - " + temp.getAllAnswers()[i].toString()
									+ (temp.getIsCorrectAns()[i] ? " (T)" : " (F)"));
						}
						System.out.println("Type \"" + (db.getDbQuestions()[qChoice - 1].getCurrentAns() + 1)
								+ "\" to return to main menu.");
						int aChoice = validateInput(1, db.getDbQuestions()[qChoice - 1].getCurrentAns() + 1);
						if (aChoice == (db.getDbQuestions()[qChoice - 1].getCurrentAns() + 1)) {
							option3Flag = false;
						} else {
							MultiChoiceQuestion temp = (MultiChoiceQuestion) db.getDbQuestions()[qChoice - 1];
							if (temp.eraseAnswer(temp.getAllAnswers()[aChoice - 1].getAnswerContent())) {
								System.out.println("Answer has been removed successfully.");
							} else {
								System.out.println("Failed to add answer.");
							}
						}
						break;
					case 4:
						// remove answer from database & exam
						System.out.println("Choose from the following:");
						System.out.println(db.aToString());
						int aChoice1 = validateInput(1, (db.getCurrentAnswer() + 1));
						if (aChoice1 == db.getCurrentAnswer() + 1) {
							option3Flag = false;
						} else {
							if (db.eraseAnswer(db.getDbAnswers()[aChoice1 - 1].getAnswerContent())) {
								System.out.println("Answer has been removed successfully.");
							} else {
								System.out.println("Failed to add answer.");
							}
						}
						break;
					case 5:
						// return to main menu
						option3Flag = false;
						break;
					}
				}
			}
		}
	}

	public static void menu7Func(Database db, MegaDatabase allDBs) throws FileNotFoundException, IOException {
		System.out.println("Please enter the amount of questions for the exam (Maximum of 10).");
		int qAmount = Program.validateInput(1, 10);
		ManualExam manEx = new ManualExam(qAmount);
		manEx.createExam(db);
		System.out.print("Exam created. ");
		menu9Func(manEx, allDBs);

	}

	public static void menu8Func(Database db, MegaDatabase allDBs) throws FileNotFoundException, IOException {
		System.out.println("Enter question amount (Maximum of 10)");
		int qAmount = validateInput(1, 10);
		AutomaticExam AutoEx = new AutomaticExam(qAmount);
		AutoEx.createExam(db);
		System.out.println("Exam has been created successfully.");
		menu9Func(AutoEx, allDBs);
	}

	public static void menu9Func(Exam exam, MegaDatabase allDBs) throws FileNotFoundException, IOException {
		LocalDateTime time = LocalDateTime.now();
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm");
		System.out.println("Exporting files to folder...");
		exam.examToFile("exam_" + time.format(format) + ".txt");
		exam.solutionToFile("solution_" + time.format(format) + ".txt");
		dbToBinaryFile("Database.dat", allDBs);
		System.out.println("Files exported successfully! Thank you, 200$ were deducted from your bank account!");
	}

	public static void dbToBinaryFile(String filePath, MegaDatabase allDBs) throws FileNotFoundException, IOException {
		ObjectOutputStream outFile = new ObjectOutputStream(new FileOutputStream(filePath));
		outFile.writeObject(allDBs);
		ObjectOutputStream outFileID = new ObjectOutputStream(new FileOutputStream("id.dat"));
		outFileID.writeObject(calculateSubjectIDs(allDBs));
		outFile.close();
		outFileID.close();
	}

	public static int calculateSubjectIDs(MegaDatabase allDBs) {
		int sum = 0;
		for (int i = 0; i < allDBs.getCurrentSubject(); i++) {
			sum += allDBs.getAllSubjectDBs()[i].getCurrentQuestion();
		}
		return sum;
	}

	public static int validateInput(int firstOption, int lastOption) {
		Scanner input1 = new Scanner(System.in);
		try {
			int userInput = input1.nextInt();
			while (userInput < firstOption || userInput > lastOption) {
				System.out.println("Please enter a valid option between " + firstOption + "-" + lastOption);
				userInput = input1.nextInt();
			}
			return userInput;
		} catch (Exception e) {
			System.out.println("Please enter a valid option between " + firstOption + "-" + lastOption);
			return validateInput(firstOption, lastOption);
		}
	}

	public static MegaDatabase loadBackupDB() {
		MegaDatabase allDBs = new MegaDatabase();
		String[] tempQuestions = { "What is the capital of France?", "Who is the author of the Harry Potter series?",
				"What is the smallest planet in our solar system?", "Who was the first person to walk on the moon?",
				"What is the highest mountain in Africa?", "What is the largest organ in the human body?",
				"What is the currency of Japan?", "What is the national animal of Australia?",
				"What is the name of the dog in the Peanuts comic strip?",
				"What is the largest country in South America?",
				"What is the name of the river that runs through Egypt?",
				"Who painted the famous artwork 'The Starry Night'?",
				"What is the name of the first Harry Potter book?", "What is the name of the Greek god of the sea?",
				"What is the largest desert in the world?", "What is the chemical symbol for gold?",
				"Who is the current Prime Minister of Canada?",
				"What is the name of the actor who played Jack Sparrow in the Pirates of the Caribbean movies?",
				"What is the largest planet in our solar system?",
				"What is the name of the ship that Charles Darwin sailed on during his scientific expedition?" };

		String[] tempAnswers = { "Paris", "J.K. Rowling", "Mercury", "Neil Armstrong", "Mount Kilimanjaro", "Skin",
				"Yen", "Kangaroo", "Snoopy", "Brazil", "Nile", "Vincent van Gogh",
				"Harry Potter and the Philosopher's Stone", "Poseidon", "Sahara", "Au", "Justin Trudeau", "Johnny Depp",
				"Jupiter", "HMS Beagle" };

		String[] openQuestions = { "What is the scientific method?", "Who discovered the theory of relativity?",
				"What was the significance of the invention of the steam engine?",
				"What is the impact of climate change on ecosystems?", "What is the history of the internet?",
				"How did the discovery of the New World impact European history?", "What is the structure of DNA?",
				"What was the impact of the French Revolution on world politics?",
				"What was the role of Martin Luther in the Protestant Reformation?",
				"What is the significance of the Magna Carta in English history?" };

		String[] openAnswers = {
				"The scientific method is a systematic approach to discovering new knowledge through observation and experimentation.",
				"Albert Einstein is credited with discovering the theory of relativity, which fundamentally changed our understanding of space, time, and gravity.",
				"The invention of the steam engine had a profound impact on industry, transportation, and communication, and was a key driver of the Industrial Revolution.",
				"Climate change is causing widespread and significant impacts on ecosystems, including sea level rise, ocean acidification, and changes in temperature and precipitation patterns.",
				"The internet was developed in the 1960s as a means of connecting researchers and academics, and has since grown to become a ubiquitous and essential part of modern life.",
				"The discovery of the New World by European explorers had a profound impact on European history, leading to the expansion of trade, the spread of Christianity, and the colonization of the Americas.",
				"DNA is a molecule that carries genetic information and is composed of four chemical building blocks, or nucleotides.",
				"The French Revolution had a significant impact on world politics, leading to the overthrow of the monarchy, the rise of Napoleon Bonaparte, and the spread of revolutionary ideals across Europe.",
				"Martin Luther was a key figure in the Protestant Reformation, a movement that sought to reform the Catholic Church and led to the development of numerous Protestant denominations.",
				"The Magna Carta was a document signed in 1215 by King John of England, and is considered a foundation of modern constitutional law and individual rights." };
		Database db = new Database();
		for (int i = 0; i < tempQuestions.length; i++) {
			MultiChoiceQuestion tempQ = new MultiChoiceQuestion(tempQuestions[i],
					Question.eDifficulty.values()[rand.nextInt(3)]);
			Answer tempA = new Answer(tempAnswers[i]);
			db.addQuestion(tempQ);
			db.addAnswerToQuestion(tempA, tempQ, true);
			db.addAnswer(tempA);
		}
		for (int i = 0; i < openQuestions.length; i++) {
			Answer temp = new Answer(openAnswers[i]);
			db.addQuestion(new OpenQuestion(openQuestions[i], temp, Question.eDifficulty.values()[rand.nextInt(3)]));
			db.addAnswer(temp);
		}

		String[] tempQuestions2 = { "What is the capital of Australia?", "Which river flows through Paris?",
				"What is the largest country in South America?",
				"Which African country is known as the 'Land of a Thousand Hills'?",
				"What is the highest mountain in Africa?", "What is the longest river in the world?",
				"Which city is located at the confluence of the Danube and Sava rivers?",
				"What is the largest ocean on Earth?", "Which European country is known for its tulips and windmills?",
				"What is the largest island in the Caribbean?",
				"Which Asian country is the most populous in the world?", "What is the largest desert in the world?",
				"Which South American country is home to Machu Picchu?", "What is the capital of Canada?",
				"Which country is both an island and a continent?", "What is the smallest country in the world?",
				"Which European city is divided by the Bosphorus Strait?",
				"What is the largest city in the United States?", "Which country is located on the Iberian Peninsula?",
				"What is the official language of Brazil?" };

		String[] tempAnswers2 = { "Canberra", "Seine", "Brazil", "Rwanda", "Mount Kilimanjaro", "Nile", "Belgrade",
				"Pacific Ocean", "Netherlands", "Cuba", "China", "Sahara Desert", "Peru", "Ottawa", "Australia",
				"Vatican City", "Istanbul", "New York City", "Spain", "Portuguese" };

		String[] openQuestions2 = { "What is the highest mountain in North America?",
				"What is the largest waterfall in the world?", "Which city is called the 'Eternal City'?",
				"What is the capital of Egypt?", "Which country is home to the famous Angkor Wat temple complex?",
				"What is the currency of Japan?", "Which European country is famous for its fjords?",
				"Which continent is known as the 'Dark Continent'?", "What is the official language of Mexico?",
				"Which river forms part of the border between the United States and Mexico?" };

		String[] openAnswers2 = { "Mount McKinley (Denali)", "Angel Falls", "Rome", "Cairo", "Cambodia", "Japanese Yen",
				"Norway", "Africa", "Spanish", "Rio Grande" };

		Database db2 = new Database();
		for (int i = 0; i < tempQuestions2.length; i++) {
			MultiChoiceQuestion tempQ = new MultiChoiceQuestion(tempQuestions2[i],
					Question.eDifficulty.values()[rand.nextInt(3)]);
			Answer tempA = new Answer(tempAnswers2[i]);
			db2.addQuestion(tempQ);
			db2.addAnswerToQuestion(tempA, tempQ, true);
			db2.addAnswer(tempA);
		}
		for (int i = 0; i < openQuestions2.length; i++) {
			Answer temp = new Answer(openAnswers2[i]);
			db2.addQuestion(new OpenQuestion(openQuestions2[i], temp, Question.eDifficulty.values()[rand.nextInt(3)]));
			db2.addAnswer(temp);
		}

		String[] tempQuestions3 = { "Who was the first President of the United States?",
				"Which war was fought between the North and South regions of the United States?",
				"Who wrote the famous play 'Romeo and Juliet'?", "In what year did World War II end?",
				"Which city is famously known as the 'Eternal City'?", "Who painted the Mona Lisa?",
				"What was the name of the ship on which Charles Darwin sailed during his voyage?",
				"Who is known for discovering the theory of gravity?", "Which country was the first to reach the moon?",
				"In what year did the Berlin Wall fall?", "Who was the leader of the Soviet Union during World War II?",
				"Which document declared the American colonies' independence from Great Britain?",
				"Who was the founder of the Mongol Empire?", "Which civilization built the famous Machu Picchu?",
				"Who was the first female Prime Minister of the United Kingdom?", "Who invented the telephone?",
				"Which country is known for the Taj Mahal?",
				"Which U.S. President signed the Emancipation Proclamation?",
				"Which ancient city was destroyed by a volcanic eruption in 79 AD?",
				"Who was the leader of the Civil Rights Movement in the United States?" };

		String[] tempAnswers3 = { "George Washington", "American Civil War", "William Shakespeare", "1945", "Rome",
				"Leonardo da Vinci", "HMS Beagle", "Isaac Newton", "United States", "1989", "Joseph Stalin",
				"Declaration of Independence", "Genghis Khan", "Inca civilization", "Margaret Thatcher",
				"Alexander Graham Bell", "India", "Abraham Lincoln", "Pompeii", "Martin Luther King Jr." };

		String[] openQuestions3 = { "Who painted the ceiling of the Sistine Chapel?",
				"Which famous battle was fought in 1066 and changed the course of English history?",
				"Who was the first female pharaoh of ancient Egypt?",
				"In what year did the United States declare its independence from Great Britain?",
				"Which European city is famous for the Eiffel Tower?", "Who wrote the novel '1984'?",
				"Which city was the capital of the Byzantine Empire?",
				"Who was the leader of the Bolshevik Revolution in Russia?", "In what year did the Cold War end?",
				"Who was the first person to step on the moon?" };

		String[] openAnswers3 = { "Michelangelo", "Battle of Hastings", "Hatshepsut", "1776", "Paris", "George Orwell",
				"Constantinople (Istanbul)", "Vladimir Lenin", "1991", "Neil Armstrong" };

		Database db3 = new Database();
		for (int i = 0; i < tempQuestions3.length; i++) {
			MultiChoiceQuestion tempQ = new MultiChoiceQuestion(tempQuestions3[i],
					Question.eDifficulty.values()[rand.nextInt(3)]);
			Answer tempA = new Answer(tempAnswers3[i]);
			db3.addQuestion(tempQ);
			db3.addAnswerToQuestion(tempA, tempQ, true);
			db3.addAnswer(tempA);
		}
		for (int i = 0; i < openQuestions3.length; i++) {
			Answer temp = new Answer(openAnswers3[i]);
			db3.addQuestion(new OpenQuestion(openQuestions3[i], temp, Question.eDifficulty.values()[rand.nextInt(3)]));
			db3.addAnswer(temp);
		}

		allDBs.addSubject(db, "General Knowledge");
		allDBs.addSubject(db2, "Geography");
		allDBs.addSubject(db3, "History");
		return allDBs;
	}

}

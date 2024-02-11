package LiorDavidi_LennyMedina;

import java.io.Serializable;
import java.util.*;

import java.time.LocalDateTime;
import java.time.Month;


public class MegaDatabase implements Serializable {
	private ArrayList<Database> allSubjectDBs;
	private ArrayList<String> allSubjectNames;
	private int currentSubject;

	public MegaDatabase() {
		this.allSubjectDBs = new ArrayList<>();
		this.allSubjectNames = new ArrayList<>();
		this.currentSubject = 0;
	}

	public ArrayList<Database> getAllSubjectDBs() {
		return this.allSubjectDBs;
	}

	public ArrayList<String> getAllSubjectDNames() {
		return this.allSubjectNames;
	}
	
	public int getCurrentSubject() {
		return this.currentSubject;
	}


	public void verifySubject(Database subjectDB,String subjectName) throws Exception {
		
		for (int i = 0; i < this.currentSubject; i++) {
			if(this.allSubjectNames.get(i).equalsIgnoreCase(subjectName)) {
				throw new Exception("Failed to add subject, subject already exists.");
			}
		}
		this.allSubjectDBs.add(subjectDB);
		this.allSubjectNames.add(subjectName);
		this.currentSubject++;
	}
}

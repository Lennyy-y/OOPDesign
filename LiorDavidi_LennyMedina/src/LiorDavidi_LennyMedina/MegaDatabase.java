package LiorDavidi_LennyMedina;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

public class MegaDatabase implements Serializable {
	private HashSet<Database> allSubjectDBs;
	private HashSet<String> allSubjectNames;

	public MegaDatabase() {
		this.allSubjectDBs = new HashSet<>();
		this.allSubjectNames = new HashSet<>();
	}

	public HashSet<Database> getAllSubjectDBs() {
		return this.allSubjectDBs;
	}

	public HashSet<String> getAllSubjectDNames() {
		return this.allSubjectNames;
	}
	


	public void addSubject(Database subjectDB, String subjectName) {
		this.allSubjectDBs.add(subjectDB);
		this.allSubjectNames.add(subjectName);
	}

}

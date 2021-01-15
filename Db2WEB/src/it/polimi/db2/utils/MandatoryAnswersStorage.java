//Valutare se serve veramente(Early Design)

package it.polimi.db2.utils;

public class MandatoryAnswersStorage {
	private int[] questionsId;
	private String[] answers;
	private int size;
	
	//I assumed no questionnaire can contain more than a 100 questions;
	public MandatoryAnswersStorage() {
		answers = new String[100];
		questionsId = new int[100];
		size = 0;
	}
	
	
	
	public void addAnswer(int id, String answer) {
		answers[size] = answer;
		questionsId[size] = id;
		size ++;
	}
	
	public int getSize() {
		return size;
	}
	
	public int removeAnswer_1() {
		int x = questionsId[size - 1];
		return x;
	}
	
	public String removeAnswer_2() {
		String x = answers[size - 1];
		size --;
		return x;
	}
	

}

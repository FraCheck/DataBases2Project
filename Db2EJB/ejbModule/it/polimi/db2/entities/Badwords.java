package it.polimi.db2.entities;

	import java.io.Serializable;
	import javax.persistence.*;


	/**
	 * The persistent class for the Badwords database table.
	 * 
	 */

	@Entity
	@Table(name = "badwords", schema = "db_project")
	@NamedQuery(name="Badwords.findAll", query="SELECT b FROM Badwords b")
	public class Badwords implements Serializable {
		private static final long serialVersionUID = 1L;
		
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private int id;
		
		private String text;
		
		public Badwords() {}
		
		public Badwords(int id, String text) {
			super();
			this.id = id;
			this.text = text;
		}
		
		public int getId() {
			return this.id;
		}

		public void setId(int id) {
			this.id = id;
		}
		
		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}


}

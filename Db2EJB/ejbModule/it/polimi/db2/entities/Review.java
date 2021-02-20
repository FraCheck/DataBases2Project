package it.polimi.db2.entities;

import java.io.Serializable;

import javax.persistence.*;

@Entity
@Table(name = "reviews", schema = "db_project")
public class Review implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String review_Text;
	
	private String reviewer_Name;
	
	// Mapped for simplicity, we don't need to access a product from a review
	@ManyToOne
	@JoinColumn(name = "product")
	private Product product;
	
	

	public Review() {}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getReviewText() {
		return review_Text;
	}

	public void setReviewText(String reviewText) {
		this.review_Text = reviewText;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public String getReviewerName() {
		return reviewer_Name;
	}

	public void setReviewerName(String reviewer_Name) {
		this.reviewer_Name = reviewer_Name;
	}
}

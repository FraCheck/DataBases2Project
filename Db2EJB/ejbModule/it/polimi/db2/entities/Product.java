package it.polimi.db2.entities;

import java.io.Serializable;
import java.util.Base64;
import java.util.List;

import javax.persistence.*;


/**
 * The persistent class for the product database table.
 * 
 */

@Entity
@Table(name = "product", schema = "db_project")
@NamedQuery(name="Product.findAll", query="SELECT p FROM Product p")
public class Product implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String name;
	
	private byte[] photoimage;
	
	@OneToMany(mappedBy="product", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, orphanRemoval = true )
	private List<Review> reviews;
	
	public List<Review> getReviews() {
		if (reviews.size()==0)
			return null;
		return reviews;
	}

	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}

	public Product() {}
	
	public Product(int id, String name, byte[] photoimage) {
		super();
		this.id = id;
		this.name = name;
		this.photoimage = photoimage;
	}
	
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte[] getPhotoimage() {
		return photoimage;
	}
	
	public String getPhotoimageData() {
		if(photoimage!=null)
			return Base64.getMimeEncoder().encodeToString(photoimage);
		return "";
	}

	public void setPhotoimage(byte[] photoimage) {
		this.photoimage = photoimage;
	} 
}

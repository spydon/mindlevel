package net.mindlevel.shared;

import java.io.Serializable;

public class Mission implements Serializable{
	private static final long serialVersionUID = 1L;
	private String name, category, description, creator, timestamp;
	private int id;
	private boolean adult;
	
	public Mission(String name, String category, String description, String creator, boolean adult) {
		this.name = name;
		this.category = category;
		this.description = description;
		this.creator = creator;
		this.adult = adult;
	}
	
	public Mission() {
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return category;
	}
	
	public String getDescription() {
		return description;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean getAdult() {
		return adult;
	}
	
	public void setAdult(boolean adult) {
		this.adult = adult;
	}
	
	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}
	
	public String getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
}
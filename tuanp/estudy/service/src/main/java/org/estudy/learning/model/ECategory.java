package org.estudy.learning.model;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.exoplatform.services.jcr.util.IdGenerator;


public class ECategory {
	public static String PREF = "ecat";
	public static String NT_NAME = "exo:category";
	public static String P_NAME = "exo:categoryname";
	private String id;
	private String name;
	
	public ECategory(){
		 id = PREF + IdGenerator.generate();
	}
	public ECategory(Node node) throws RepositoryException {
		id = node.getName();
		name = node.getProperty(P_NAME).getString();
	}
	public ECategory(String name) {
		id = PREF + IdGenerator.generate();
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
}

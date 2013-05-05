package org.estudy.learning.model;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.exoplatform.services.jcr.util.IdGenerator;

public class ESession {
	public final static String PREF = "eset";
	public final static String NT_NAME = "exo:session";
	public final static String P_TITLE = "exo:title";
	public final static String P_DEC = "exo:description";
	public final static String P_RFLINK = "exo:rflink";
	public final static String P_VLINK = "exo:vlink";
	
	private String id;
	private String title;
	private String dec;
	private String rflink;
	private String vlink;

	public ESession(){
		setId(PREF + IdGenerator.generate());
	}
	public ESession(Node node) throws RepositoryException {
		setId(node.getName());
		setTitle(node.getProperty(P_TITLE).getString());
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDec() {
		return dec;
	}
	public void setDec(String dec) {
		this.dec = dec;
	}
	public String getRflink() {
		return rflink;
	}
	public void setRflink(String rflink) {
		this.rflink = rflink;
	}
	public String getVlink() {
		return vlink;
	}
	public void setVlink(String vlink) {
		this.vlink = vlink;
	}

}

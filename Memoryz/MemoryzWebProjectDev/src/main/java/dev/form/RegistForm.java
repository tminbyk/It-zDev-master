package dev.form;

import org.apache.struts.action.ActionForm;

public class RegistForm extends ActionForm{

	private String password = null;
	private String userID = null;

	public String getUserTel() {
		return password;
	}
	public void setUserTel(String password) {
		this.password = password;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}

}

package dev.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.springframework.beans.factory.annotation.Autowired;

import dev.service.DBConnectService;

public class RegistAction extends Action {

	@Autowired
	DBConnectService dbconnector = null;

	@Override
	public ActionForward execute(ActionMapping map, ActionForm form,
	        HttpServletRequest request, HttpServletResponse response) throws Exception {

        DynaActionForm dynaForm = (DynaActionForm)form;
        String userID = dynaForm.getString("userID");
        String password = dynaForm.getString("password");

		dbconnector.connectDB();
		if (dbconnector.registerData(userID, password)) {
			return map.findForward("success");
		} else {
			return map.findForward("failure");
		}
	}
}

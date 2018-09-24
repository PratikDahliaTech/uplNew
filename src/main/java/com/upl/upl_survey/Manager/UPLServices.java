package com.upl.upl_survey.Manager;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.upl.upl_survey.Dao.UserDao;
import com.upl.upl_survey.Model.FormMaster;
import com.upl.upl_survey.Model.UserDetails;

@RestController
@Produces(MediaType.APPLICATION_JSON)
@RequestMapping("/upl_survey")
public class UPLServices {

	private static final Logger logger = LoggerFactory.getLogger(UPLServices.class);

	@Autowired
	private UserDao userDao;

	@Autowired
	PasswordValidation passEncrp;

	@GET
	@Path("/loginUser")
	// @RequestMapping(value = "/loginUser", method = RequestMethod.POST)
	public UserDetails getLoginUser(@QueryParam("email") String email, @QueryParam("password") String password) {
		logger.info("Login User ..");
		logger.debug("User Id :{} and Password :{} ", email, password);
		String UserPassword = userDao.getUserPassword(email);
		UserPassword = passEncrp.decrypt(UserPassword);
		if (!password.equals(UserPassword)) {
			UserDetails details = userDao.getLoginUser(email);
			return details;
		} else {
			logger.info("User or Password not vaild ..");
			return null;
		}
	}

	@GET
	@Path("/addUser")
//	@RequestMapping(value = "/addUser", method = RequestMethod.POST)
	public void addUser(@QueryParam("password") String password, @QueryParam("created_by") Long created_by, @QueryParam("phone_no") Number phone_No,
			@QueryParam("email") String email, @QueryParam("user_master_id") Long user_master_id) {
		logger.info("Add new user");
		logger.debug("New User Details password :{} created_by:{} phone_No:{} email:{}", password,
				created_by, phone_No, email);
		if (password != null) {
			password = passEncrp.encrypt(password);
		}
		Date created_date = new Date();
		Date updated_date = new Date();
		userDao.addUser(password, created_by, created_by, created_date, updated_date, phone_No, email, user_master_id);
	}

	@GET
	@Path("/updateUserDetails")
//	@RequestMapping(value = "/updateRegisterUser", method = RequestMethod.POST)
	public void updateUserDetails(@QueryParam("id") Long id, @QueryParam("password") String password,
			@QueryParam("updated_by") Long updated_by, @QueryParam("phone_no") Number phone_no,
			@QueryParam("email") String email, @QueryParam("user_master_id") Long user_master_id) {
		logger.info("Update User :{}", id);
		if (password != null) {
			password = passEncrp.encrypt(password);
		}
		Date updated_date = new Date();
		userDao.updateUserDetails(password, updated_by, updated_date, phone_no, email, user_master_id);
	}

	@GET
	@Path("/deleteUserDetails")
	// @RequestMapping(value = "/getUserDetails", method = RequestMethod.GET)
	public void deleteUserDetails(@QueryParam("id") Long id) {
		logger.info("Delete User :{}", id);
		userDao.deleteUserDetails(id);
	}

	@GET
	@Path("/getAllUserDetails")
	// @RequestMapping(value = "/getUserDetails", method = RequestMethod.GET)
	public List<UserDetails> getAllUserDetails() {
		logger.info("Get All User");
		List<UserDetails> list = userDao.getAllUserDetails();
		logger.trace("Get All User :{}", list.toString());
		return list;
	}

	@GET
	@Path("/createForm")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
//	@RequestMapping(value = "/insertFormData", method = RequestMethod.POST)
	public void insertFormData(@QueryParam("form_detail") InputStream form_detail,
			@QueryParam("form_id") String form_id, @QueryParam("created_by") Long created_by,
			@QueryParam("last_updated_by") Long last_updated_by, @QueryParam("language_id") String language_id) {
		Date created_date = new Date();
		Date updated_date = new Date();
		userDao.createForm(form_id, created_by, last_updated_by, created_date, updated_date, language_id);
		Long form_master_id = userDao.getFormId(form_id);
		userDao.insertFormDetails(form_detail, created_date, form_master_id, last_updated_by);
	}

	@GET
	@Path("/deleteForm")
	// @RequestMapping(value = "/getUserDetails", method = RequestMethod.GET)
	public void deleteForm(@QueryParam("id") Long id) {
		logger.info("Delete Form :{}", id);
		userDao.deleteForm(id);
	}

	@GET
	@Path("/updateForm")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
//	@RequestMapping(value = "/insertFormData", method = RequestMethod.POST)
	public void updateFormData(@QueryParam("id") Long id, @QueryParam("form_detail") InputStream form_detail,
			@QueryParam("form_id") String form_id, @QueryParam("last_updated_by") Long last_updated_by) {
		Date updated_date = new Date();
		userDao.updateForm(id, form_id, last_updated_by, updated_date);
//		Long form_master_id = userDao.getFormId(form_id);
		userDao.insertFormDetails(form_detail, updated_date, id, last_updated_by);
	}

	@GET
	@Path("/getAllForms")
	// @RequestMapping(value = "/getAllForms", method = RequestMethod.GET)
	public List<FormMaster> getAllForms() {
		List<FormMaster> list = userDao.getAllForms();
		return list;
	}

}
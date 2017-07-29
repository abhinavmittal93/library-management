/*
 * SecurityDAO.java
 *
 * 
 */

package com.dts.dae.dao;

import com.dts.dae.model.Profile;
import com.dts.core.dao.AbstractDataAccessObject;
import com.dts.core.util.DateWrapper;
import com.dts.core.util.LoggerManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

/**
 *
 * @author
 */
public class SecurityDAO extends AbstractDataAccessObject {
	Connection con;
	private String desc;
	private boolean flag;

	/** Creates a new instance of SecurityDAO */
	public SecurityDAO() {

		// getting Database Connection
		con = getConnection();

	}

	// Compare Password
	public boolean checkPassword(Profile regbean) {
		String loginid = regbean.getLoginID();
		String oldpassword = regbean.getPassword();

		try {
			con.setAutoCommit(true);
			PreparedStatement pst = con.prepareStatement("select * from logindetails where password=? and loginname=?");
			pst.setString(1, oldpassword);
			pst.setString(2, loginid);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				flag = true;
				LoggerManager.writeLogInfo("Login Success");
			} else {
				flag = false;
			}
			con.close();
		} catch (SQLException ex) {
			LoggerManager.writeLogSevere(ex);
			LoggerManager.writeLogInfo("Database Connection problem");
			flag = false;
		}
		return flag;
	}

	// Compare Password
	public int checkFirstLogin(String loginname) {
		int fstatus = 2;
		try {

			con.setAutoCommit(true);
			PreparedStatement pst = con.prepareStatement("select firstlogin from logindetails where loginname=?");
			pst.setString(1, loginname);
			ResultSet rs = pst.executeQuery();

			if (rs.next()) {
				fstatus = rs.getInt(1);
			}
			con.close();
		} catch (SQLException ex) {
			LoggerManager.writeLogSevere(ex);
			LoggerManager.writeLogInfo("Database Connection problem");
			flag = false;
		}
		return fstatus;
	}

	// Login Check
	public String loginCheck(Profile regbean) {
		String loginid = regbean.getLoginID();
		String password = regbean.getPassword();
		String role = "";
		try {
			con.setAutoCommit(true);
			PreparedStatement pst = con
					.prepareStatement("select logintype from logindetails where password=? and loginname=?");
			pst.setString(2, loginid);
			pst.setString(1, password);

			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				/*
				 * Statement st=con.createStatement(); st.executeUpdate(
				 * "Update login_details set firstlogin=1 where loginname='"
				 * +loginid+"'");
				 */
				role = rs.getString(1);
				desc = "Login Success";
			} else {
				flag = false;
				desc = "Entered Invalid password";
			}

		} catch (SQLException ex) {
			LoggerManager.writeLogSevere(ex);
			desc = "Database Connection problem";
			flag = false;
		}
		loginaudit(loginid, desc);
		return role;
	}

	// Method for login audit
	public void loginaudit(String loginid, String desc) {
		try {
			String newdate = DateWrapper.parseDate(new Date());
			PreparedStatement pst = con.prepareStatement("insert into loginaudit values(?,?,?)");
			pst.setString(1, loginid);
			pst.setString(2, newdate);
			pst.setString(3, desc);
			int i = pst.executeUpdate();
			if (i == 1)
				con.commit();
			else
				con.rollback();
			con.close();
		} catch (Exception e) {
			try {
				con.rollback();
			} catch (SQLException ex) {
				LoggerManager.writeLogSevere(ex);
			}
			e.printStackTrace();
		}
	}

	// Change Password
	public boolean changePassword(Profile regbean) {
		String loginid = regbean.getLoginID();
		String oldpassword = regbean.getPassword();
		String newpassword = regbean.getNewPassword();
		try {
			con.setAutoCommit(false);
			String newdate = DateWrapper.parseDate(new Date());
			PreparedStatement pst = con.prepareStatement(
					"UPDATE logindetails SET password=?,passmodifieddate=? WHERE loginname=? and password=?");

			pst.setString(1, newpassword);
			pst.setString(2, newdate);
			pst.setString(3, loginid);
			pst.setString(4, oldpassword);

			int i = pst.executeUpdate();
			if (i == 1) {
				flag = true;
				con.commit();
			} else {
				flag = false;
				con.rollback();
			}
			con.close();
		} catch (SQLException ex) {
			LoggerManager.writeLogSevere(ex);
			flag = false;
			try {
				con.rollback();
			} catch (SQLException sex) {
				LoggerManager.writeLogSevere(sex);
			}

		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
			try {
				con.rollback();
			} catch (SQLException sex) {
				LoggerManager.writeLogSevere(sex);
			}
		}
		return flag;
	}

	// Change Secret Question
	public boolean changeQuestion(Profile regbean) {
		String loginid = regbean.getLoginID();
		String password = regbean.getPassword();
		int secretquestid = regbean.getSecretQuestionID();
		String ownsecretquest = regbean.getOwnSecretQuestion();
		String secretans = regbean.getSecretAnswer();

		PreparedStatement pst;
		int i = 0;
		try {
			con.setAutoCommit(false);
			if (checkPassword(regbean)) {
				if (secretquestid == 0) {
					Statement st = con.createStatement();
					ResultSet rs = st.executeQuery("select (max(questionid))+1 from questionbase");

					if (rs.next())
						secretquestid = rs.getInt(1);
					pst = con.prepareStatement("INSERT INTO question_base VALUES(?,?)");

					pst.setInt(1, secretquestid);
					pst.setString(2, ownsecretquest);

					pst.executeUpdate();
				}

				pst = con.prepareStatement(
						"UPDATE logindetails SET squestionid=?,sanswer=? WHERE loginname=? and password=?");

				pst.setInt(1, secretquestid);
				pst.setString(2, secretans);
				pst.setString(3, loginid);
				pst.setString(4, password);

				i = pst.executeUpdate();
				if (i == 1) {
					flag = true;
					con.commit();
				} else {
					flag = false;
					con.rollback();
				}
			}
			con.close();
		} catch (SQLException ex) {
			LoggerManager.writeLogSevere(ex);
			flag = false;
			try {
				con.rollback();
			} catch (SQLException sex) {
				LoggerManager.writeLogSevere(sex);
			}
		} catch (Exception e) {
			LoggerManager.writeLogSevere(e);
			flag = false;
			try {
				con.rollback();
			} catch (SQLException sex) {
				LoggerManager.writeLogSevere(sex);
			}
		}
		return flag;
	}

	// Recover Password using Existed Question
	public String recoverPasswordByExistQuestion(Profile regbean) {
		String password;
		String loginid = regbean.getLoginID();
		int secretquestid = regbean.getSecretQuestionID();
		String secretans = regbean.getSecretAnswer();
		try {
			con.setAutoCommit(true);
			PreparedStatement pst = con.prepareStatement(
					"SELECT password FROM logindetails  WHERE loginname=? and squestionid=? and sanswer=?");

			pst.setString(1, loginid);
			pst.setInt(2, secretquestid);
			pst.setString(3, secretans);

			ResultSet rs = pst.executeQuery();

			if (rs.next())
				password = rs.getString(1);
			else
				password = "";
			con.close();
		} catch (SQLException ex) {
			LoggerManager.writeLogSevere(ex);
			password = "";
		} catch (Exception e) {
			LoggerManager.writeLogSevere(e);
			password = "";
		}
		return password;
	}

	// Recover Password using OWN Question
	public String recoverPasswordByOWNQuestion(Profile regbean) {
		String password;
		String loginid = regbean.getLoginID();
		String ownsecretquest = regbean.getOwnSecretQuestion();
		String secretans = regbean.getSecretAnswer();
		try {
			con.setAutoCommit(true);
			PreparedStatement pst = con.prepareStatement(
					"SELECT password FROM logindetails  WHERE loginname=? and squestionid=(select questionid from questionbase where questiondetails=?) and sanswer=?");

			pst.setString(1, loginid);
			pst.setString(2, ownsecretquest);
			pst.setString(3, secretans);

			ResultSet rs = pst.executeQuery();

			if (rs.next())
				password = rs.getString(1);
			else
				password = "";
			con.close();
		} catch (SQLException ex) {
			LoggerManager.writeLogSevere(ex);
			password = "";
		} catch (Exception e) {
			LoggerManager.writeLogSevere(e);
			password = "";
		}
		return password;
	}
}

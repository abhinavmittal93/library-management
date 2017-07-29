/*
 * ProfileDAO.java
 *
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
public class ProfileDAO extends AbstractDataAccessObject {

	public Connection con;

	private boolean flag;

	/** Creates a new instance of ProfileDAO */
	public ProfileDAO() {
		// getting Database Connection

	}

	// User Registration
	public boolean registration(Profile regbean) {
		String loginid = regbean.getLoginID();
		String password = regbean.getPassword();
		String firstname = regbean.getFirstName();
		String lastname = regbean.getLastName();
		String logintype = regbean.getLoginType();// this is mobile no
		String Landno = regbean.getPhoneNo();
		String bdate = DateWrapper.parseDate(regbean.getBirthDate());
		String city = regbean.getCity();
		String state = regbean.getState();
		String country = regbean.getCountry();
		String pinno = regbean.getPincode();
		String status = regbean.getStatus();
		// String regdate=regbean.getRegDate();
		int secretquest = regbean.getSecretQuestionID();
		String ownsecretquest = regbean.getOwnSecretQuestion();
		String secretans = regbean.getSecretAnswer();
		int firstlogin = regbean.getFirstLogin();
		String locale = regbean.getLocale();
		String passmdate = regbean.getPasswordModifiedDate();
		String profilemdate = regbean.getProfileModifiedDate();

		// rb.setStatus(1);
		// rb.setPinno(request.getParameter("pinno"));

		try {
			con = getConnection();

			PreparedStatement pst = null;
			Statement st = con.createStatement();
			int i = 0;
			if (secretquest == 0) {
				ResultSet rs = st.executeQuery("select (max(questionid))+1 from questionbase");
				if (rs.next())
					secretquest = rs.getInt(1);
				pst = con.prepareStatement("INSERT INTO questionbase VALUES(?,?)");

				pst.setInt(1, secretquest);
				pst.setString(2, ownsecretquest);

				pst.executeUpdate();
			}
			String newdate = DateWrapper.parseDate(new Date());

			// st.executeUpdate("insert into LOGIN_DETAILS
			// values('"+loginid+"','"+password+"','"+firstname+"','"+lastname+"','"+logintype+"',"+status+",'"+newdate+"',"+secretquest+",'"+secretans+"',"+firstlogin+",'"+newdate+"')");
			pst = con.prepareStatement("insert into LOGINDETAILS values(?,?,?,?,?,?,?,?,?,?)");

			pst.setString(1, loginid);
			pst.setString(2, password);
			pst.setString(3, firstname);
			pst.setString(4, lastname);
			pst.setString(5, logintype);
			pst.setString(6, Landno);
			pst.setString(7, newdate);
			pst.setInt(8, secretquest);
			pst.setString(9, secretans);
			pst.setString(10, newdate);
			i = pst.executeUpdate();
			if (i == 1) {
				pst = con.prepareStatement("insert into LOGINPROFILE values(?,?,?,?,?,?,?,?)");
				pst.setString(1, loginid);
				pst.setString(2, bdate);
				pst.setString(3, city);
				pst.setString(4, state);
				pst.setString(5, country);
				pst.setString(6, pinno);
				pst.setString(7, locale);
				pst.setString(8, newdate);
				i = pst.executeUpdate();
			}
			if (i == 1) {
				flag = true;
				con.commit();
			} else {
				flag = false;
				con.rollback();
			}
			con.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
			flag = false;
			try {
				con.rollback();
			} catch (SQLException sex) {
				sex.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
			try {
				con.rollback();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return flag;
	}

	// Getting profile
	public Profile getProfile(String loginname) {
		Profile rb = null;
		try {
			con = getConnection();
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(
					"select ld.firstname,ld.lastname,lp.birthdate,lp.city,lp.state,lp.country,ld.LandLineNo from logindetails ld,loginprofile lp where ld.loginname=lp.loginid and ld.loginname='"
							+ loginname + "'");
			if (rs.next()) {
				rb = new Profile();
				rb.setLoginID(loginname);
				rb.setFirstName(rs.getString(1));
				rb.setLastName(rs.getString(2));
				rb.setBirthDate1(rs.getDate(3));
				rb.setCity(rs.getString(4));
				rb.setState(rs.getString(5));
				rb.setCountry(rs.getString(6));
				rb.setPhoneNo(rs.getString(7));

			}
			con.close();
		} catch (Exception e) {
			LoggerManager.writeLogSevere(e);
		}
		return rb;
	}

	// Modify Profile
	public boolean modifyProfile(Profile regbean) {
		String loginid = regbean.getLoginID();
		String bdate = DateWrapper.parseDate(regbean.getBirthDate());
		String city = regbean.getCity();
		String state = regbean.getState();
		String country = regbean.getCountry();
		System.out.println(bdate);
		String firstname = regbean.getFirstName();
		String lastname = regbean.getLastName();
		String phoneno = regbean.getPhoneNo();

		try {
			con = getConnection();
			con.setAutoCommit(false);
			PreparedStatement pst = con.prepareStatement(
					"UPDATE loginprofile SET birthdate=?,city=?,state=?,country=?,profilemodifieddate=? WHERE loginid=?");
			PreparedStatement pst1 = con
					.prepareStatement("UPDATE logindetails SET firstname=?,lastname=?,LandLineNo=? WHERE loginname=?");

			pst.setString(1, bdate);
			pst.setString(2, city);
			pst.setString(3, state);
			pst.setString(4, country);
			pst.setString(5, DateWrapper.parseDate(new Date()));
			pst.setString(6, loginid);

			pst1.setString(1, firstname);
			pst1.setString(2, lastname);
			pst1.setString(3, phoneno);

			pst1.setString(4, loginid);

			int i = pst.executeUpdate();

			if (i != 0) {
				i = pst1.executeUpdate();
				if (i != 0) {
					flag = true;
					con.commit();
				}
			}
			return flag;
		} catch (Exception e) {
			e.printStackTrace();

		}
		return flag;
	}

	public boolean changeAccountStatus(String loginid, int status) {
		try {
			con = getConnection();
			con.setAutoCommit(false);
			PreparedStatement pst = con.prepareStatement("UPDATE logindetails SET loginstatus=? WHERE loginid=?");

			pst.setInt(1, status);
			pst.setString(2, loginid);
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
			} catch (SQLException se) {
				LoggerManager.writeLogSevere(se);
			}
		} catch (Exception e) {
			LoggerManager.writeLogSevere(e);
			flag = false;
			try {
				con.rollback();
			} catch (SQLException se) {
				LoggerManager.writeLogSevere(se);
			}
		}
		return flag;
	}
}

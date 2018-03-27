package com.gameapi.rha.services;

import com.gameapi.rha.models.User;

import java.util.*;
//import com.gameapi.rha.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

/**
 * UserService is a class to operate with params from UserController.
 */
@Service
@Transactional
public class UserService {

  private static JdbcTemplate jdbc;
  private static RatingMapper RATING_MAPPER = new RatingMapper();
  private static UserMapper USER_MAPPER = new UserMapper();
  /**
  * UserService default constructor specialized.
  */

  @Autowired
  public UserService(JdbcTemplate jdbc) {
    this.jdbc = jdbc;
  }

  /**
  * rating table.
  */
  public static Map<String, Integer> RatingTable = new HashMap<>();

  /**
   * Insertion into DB with SQL.
   * @param user is user to create
   * @return user, if all right
   */

  public static void createUser(User user) {
    String SQL = "INSERT INTO \"users\" (username,rating,email,password) VALUES (? ,? ,? ,?);";
    jdbc.update(SQL,user.getUsername(),user.getRating(),user.getEmail(),user.getPassword());
  }

  //авторизация по мылу ииии по нику

  /**
  * function rating returns rating by it's page.
  * @param  page  Is used to tell the pagenum.
  * @return rating result
  */
  public static List<Map<String,Integer>> rating(Integer page, String user) {
    List<Map<String,Integer>> res = new LinkedList<>();
    String SQL = "(SELECT username,rating FROM \"users\""
            + "ORDER BY rating "
            + "OFFSET ? Rows LIMIT ?)"
            + "UNION (SELECT username,rating WHERE username=?);";

    res = jdbc.query(SQL,RATING_MAPPER,page * 2, 2, user);
    SQL="SELECT count(*) FROM users;";
    Map<String, Integer> map=new HashMap<>();
    map.put("pages", jdbc.queryForObject(SQL, Integer.class));
    res.add(map);
    return(res);
  }

  public static User check(String email, String password)  {
    String SQL = "SELECT * FROM \"users\" WHERE email=?;";
    User authed = jdbc.queryForObject(SQL,USER_MAPPER,email);
    if(authed.checkPassword(password)) {
      return authed;
    } else {
      return null;
    }
  }

  public static User userInfo(String email) {
    String SQL = "SELECT * FROM \"users\" WHERE email=?;";
    return jdbc.queryForObject(SQL,USER_MAPPER,email);
  }


  /**
   * Change user is function to change current user in session.
   * @param prevUser is previous user
   * @param newUser is new user
   */
  public static void changeUser(String prevUser, User newUser) {

    List<Object> lst = new LinkedList<>();
    String SQL = "UPDATE \"users\" SET";
    if (newUser.getEmail() != null) {
      SQL += " email = ?, ";
      lst.add(newUser.getEmail());
    }
    if (newUser.getPassword() != null) {
      SQL += "password = ?, ";
      lst.add(newUser.getPassword());
    }
    if(newUser.getRating() != null) {
      SQL += "rating = ?, ";
      lst.add(newUser.getPassword());
    }
    SQL += " WHERE email=?;";
    jdbc.update(SQL,lst,prevUser);
  }




  public static final class RatingMapper implements RowMapper<Map<String,Integer>> {
    public Map<String,Integer> mapRow(ResultSet rs, int rowNum) throws SQLException {
      final Map<String,Integer> th = new HashMap<>();
      th.put(rs.getString("username"),rs.getInt("rating"));
      return th;
    }
  }
  public static final class UserMapper implements RowMapper<User> {
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
      final User th=new User(rs.getString("username"),rs.getString("password"),rs.getString("email"),rs.getInt("rating"));
      return th;
    }
  }
}

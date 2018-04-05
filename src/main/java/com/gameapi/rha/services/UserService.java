package com.gameapi.rha.services;

import com.gameapi.rha.models.Rating;
import com.gameapi.rha.models.User;


import java.awt.*;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.*;
import java.util.List;

//import com.gameapi.rha.models.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNullApi;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;


/**
 * UserService is a class to operate with params from UserController.
 */

@Service
@Transactional
public class UserService {
  public static final String PATH_AVATARS_FOLDER = Paths.get("uploads")
          .toAbsolutePath().toString() + '/';
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
   */

  public static User createUser(User user) {
    String SQL = "INSERT INTO \"users\" (username,rating,email,password) VALUES (? ,? ,? ,?);";
    jdbc.update(SQL,user.getUsername(),user.getRating(),user.getEmail(),user.getPassword());
    return user;
  }

  //авторизация по мылу ииии по нику


  /**
  * function rating returns rating by it's page.
  * @param  page  Is used to tell the pagenum.
  * @return rating result
  */


  public static List<Map<String,Integer>> rating(Integer page, String user) {
    String SQL = "(SELECT username,rating FROM \"users\""
            + "ORDER BY rating DESC "
            + "OFFSET ? Rows LIMIT ?)"
            + "UNION (SELECT username,rating FROM \"users\" WHERE username=?::citext);";

    List<Map<String, Integer>> res = jdbc.query(SQL, RATING_MAPPER, page * 2, 2, user);
    SQL = "SELECT count(*) FROM users;";
    final Map<String, Integer> map = new HashMap<>();
    map.put("pages", jdbc.queryForObject(SQL, Integer.class));
    res.add(map);
    return (res);
  }

//  public static Map<String,Object> ratingM(Integer page, String user) {
//    String SQL = "(SELECT username,rating FROM \"users\""
//            + "ORDER BY rating "
//            + "OFFSET ? Rows LIMIT ?)"
//            + "UNION (SELECT username,rating FROM \"users\" WHERE username=?::citext);";
//
//    Map<String, Object> res = jdbc.queryForMap(SQL, page * 2, 2, user);
//    SQL = "SELECT count(*) FROM users;";
//    res.put("pages", jdbc.queryForObject(SQL, Integer.class)/2);
//    return (res);
//  }

  public static @Nullable User check(String email, String password)  {
    final String SQL = "SELECT * FROM \"users\" WHERE email=?;";
    final User authed = jdbc.queryForObject(SQL,USER_MAPPER,email);
    if (authed.checkPassword(password)) {
      return authed;
    } else {
      return null;
    }
  }

  public static User userInfo(String nick) {
    String SQL = "SELECT * FROM \"users\" WHERE username=?;";
    return jdbc.queryForObject(SQL,USER_MAPPER,nick);
  }


  /**
   * Change user is function to change current user in session.
   * @param user is new user to change
   */
  public static void changeUser( User user) {

    final List<Object> lst = new ArrayList<>();
    String SQL = "UPDATE \"users\" SET";

    if (user.getEmail() != null) {
      SQL += " email = ?, ";
      lst.add(user.getEmail());
    }
    if (user.getPassword() != null) {
      SQL += "password = ?, ";
      lst.add(user.getPassword());
    }
    if (user.getRating() != null) {
      SQL += "rating = ? ";
      lst.add(user.getRating());
    }
    SQL += " WHERE username=?;";
    lst.add(user.getUsername());
    jdbc.update(SQL,lst.toArray());
  }

  public static void store(MultipartFile file, String user) throws IOException {
   File tosave= new File(PATH_AVATARS_FOLDER+user+"a.jpg");
    file.transferTo(tosave);
    String SQL = "UPDATE \"users\" SET avatar=? WHERE username=(?)::citext;";
    jdbc.update(SQL,user+"a.jpg",user);
  }

  public static Resource loadAvatar(String user) {
    String image=jdbc.queryForObject(
            "SELECT avatar FROM \"users\" " +
                    "WHERE username = ? LIMIT 1;",
            String.class, user
    );
    Resource avatar=new FileSystemResource(PATH_AVATARS_FOLDER+image);
    return avatar;
  }


  public static final class RatingMapper implements RowMapper<Map<String,Integer>> {
    @Override
    public @NotNull Map<String,Integer> mapRow(ResultSet rs, int rowNum) throws SQLException {
      final Map<String,Integer> th = new HashMap<>();
      th.put(rs.getString("username"),rs.getInt("rating"));
      return th;

    }
  }


  public static final class UserMapper implements RowMapper<User> {
    @Override
    public @NotNull User mapRow(ResultSet rs, int rowNum) throws SQLException {
      return new User(rs.getString("username"),
              rs.getString("password"),rs.getString("email"),
              rs.getInt("rating"));
    }

  }
}

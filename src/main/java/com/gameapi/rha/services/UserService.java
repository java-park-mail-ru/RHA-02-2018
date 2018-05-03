package com.gameapi.rha.services;

import com.gameapi.rha.models.User;

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
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;



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

   * Insertion into DB with SQL.
   * @param user is user to create
   */

  public User createUser(User user) {
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


  public List<List<Map<String, Integer>>> rating(Integer page, String user) {
    String SQL = "SELECT username,rating FROM \"users\" WHERE username!=? "
            + "ORDER BY rating DESC "
            + "OFFSET ? Rows LIMIT ?;";

    List<List<Map<String, Integer>>> res = new LinkedList<>();
    res.add(jdbc.query(SQL, RATING_MAPPER, user,page * 5, 5));
    if (res.get(0).isEmpty()) {
      return null;
    }
    SQL = "SELECT username,rating FROM \"users\" WHERE username=?::citext LIMIT 1;";
    res.add(jdbc.query(SQL, RATING_MAPPER, user));
    SQL = "SELECT count(*) FROM users;";
    final Map<String, Integer> map = new HashMap<>();
    map.put("pages", (jdbc.queryForObject(SQL, Integer.class) - 1) / 5);
    List<Map<String,Integer>> lst = new LinkedList<>();
    lst.add(map);
    res.add(lst);
    return (res);
  }


  /**
   * Authorisation check.
   * @param email user mail
   * @param password password
   * @return user
   */
  public @Nullable User check(String email, String password)  {
    final String SQL = "SELECT * FROM \"users\" WHERE email=?;";
    final User authed = jdbc.queryForObject(SQL,USER_MAPPER,email);
    if (authed.checkPassword(password)) {
      return authed;
    } else {
      return null;
    }
  }


  public User userInfo(String nick) {
    String SQL = "SELECT * FROM \"users\" WHERE username=?;";
    return jdbc.queryForObject(SQL,USER_MAPPER,nick);
  }


  /**
   * Change user is function to change current user in session.
   * @param user is new user to change
   */
  public void changeUser (User user) {

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

  /**
   * Function to save image on PC and db
   * @param file image to save
   * @param user user to avatar
   * @throws IOException if there is error(Handled in controller)
   */
  public void store(MultipartFile file, String user) throws IOException {
   File tosave = new File(PATH_AVATARS_FOLDER + user + "a.jpg");
    file.transferTo(tosave);
    String SQL = "UPDATE \"users\" SET avatar=? WHERE username=(?)::citext;";
    jdbc.update(SQL,user+"a.jpg",user);
  }


  /**
   * load
   * @param user
   * @return
   */

  public File loadAvatar(String user) {
    String image = jdbc.queryForObject(
            "SELECT avatar FROM \"users\" "
                    + "WHERE username = ? LIMIT 1;",
            String.class, user
    );
    File avatar = new File(PATH_AVATARS_FOLDER+image);
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

package com.gameapi.rha.services;

import com.gameapi.rha.models.User;

import java.awt.image.BufferedImage;
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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
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
  private static final RatingMapper RATING_MAPPER = new RatingMapper();
  private static final UserMapper USER_MAPPER = new UserMapper();

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
    final String sQl = "INSERT INTO \"users\" (username,rating,email,password) VALUES (?, ?, ?, ?);";
    jdbc.update(sQl, user.getUsername(), user.getRating(), user.getEmail(), user.getPassword());
    return user;
  }

  //авторизация по мылу ииии по нику


  /**
  * function rating returns rating by it's page.
  * @param  page  Is used to tell the pagenum.
  * @return rating result
  */


  public List<List<Map<String, Integer>>> rating(Integer page, String user) {
    String sql = "SELECT username,rating FROM \"users\" WHERE username!=? "
            + "ORDER BY rating DESC "
            + "OFFSET ? Rows LIMIT ?;";

    List<List<Map<String, Integer>>> res = new LinkedList<>();
    res.add(jdbc.query(sql, RATING_MAPPER, user, page * 5, 5));
    if (res.get(0).isEmpty()) {
      return null;
    }
    sql = "SELECT username,rating FROM \"users\" WHERE username=?::citext LIMIT 1;";
    res.add(jdbc.query(sql, RATING_MAPPER, user));
    sql = "SELECT count(*) FROM users;";
    final Map<String, Integer> map = new HashMap<>();
    int pages = jdbc.queryForObject(sql, Integer.class) - 1;
    if(pages % 5 == 0) {
      map.put("pages",pages / 5);
    } else {
      map.put("pages",pages / 5 + 1);
    }
    List<Map<String, Integer>> lst = new LinkedList<>();
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
    final String sql = "SELECT * FROM \"users\" WHERE email=?;";
    final User authed = jdbc.queryForObject(sql, USER_MAPPER, email);
    if (authed.checkPassword(password)) {
      return authed;
    } else {
      return null;
    }
  }


  public User userInfo(String nick) {
    String sql = "SELECT * FROM \"users\" WHERE username=?;";
    return jdbc.queryForObject(sql, USER_MAPPER, nick);
  }


  /**
   * Change user is function to change current user in session.
   * @param user is new user to change
   */
  public void changeUser(User user) {

    final List<Object> lst = new ArrayList<>();
    String sql = "UPDATE \"users\" SET";

    if (user.getEmail() != null) {
      sql += " email = ?, ";
      lst.add(user.getEmail());
    }
    if (user.getPassword() != null) {
      sql += "password = ?, ";
      lst.add(user.getPassword());
    }
    if (user.getRating() != null) {
      sql += "rating = ? ";
      lst.add(user.getRating());
    }
    sql += " WHERE username=?;";
    lst.add(user.getUsername());
    jdbc.update(sql, lst.toArray());
  }

  /**
   * Function to save image on PC and db.
   * @param file image to save
   * @param user user to avatar
   * @throws IOException if there is error(Handled in controller)
   */
  public void store(MultipartFile file, String user) throws IOException {
   File tosave = new File(PATH_AVATARS_FOLDER + user + "a.jpg");
    file.transferTo(tosave);
    String sql = "UPDATE \"users\" SET avatar=? WHERE username=(?)::citext;";
    jdbc.update(sql, user + "a.jpg", user);
  }


  /**
   * load.
   * @param user is username
   * @return avatar
   */

  public BufferedImage loadAvatar(String user) throws IOException {
    String image = jdbc.queryForObject(
            "SELECT avatar FROM \"users\" "
                    + "WHERE username = ? LIMIT 1;",
            String.class, user
    );
    BufferedImage avatar = ImageIO.read(new File(PATH_AVATARS_FOLDER + image));
    return avatar;
  }


  public static final class RatingMapper implements RowMapper<Map<String, Integer>> {
    @Override
    public @NotNull Map<String, Integer> mapRow(ResultSet rs, int rowNum) throws SQLException {
      final Map<String, Integer> th = new HashMap<>();
      th.put(rs.getString("username"), rs.getInt("rating"));
      return th;

    }
  }


  public static final class UserMapper implements RowMapper<User> {
    @Override
    public @NotNull User mapRow(ResultSet rs, int rowNum) throws SQLException {
      return new User(rs.getString("username"),
              rs.getString("password"), rs.getString("email"),
              rs.getInt("rating"));
    }

  }
}

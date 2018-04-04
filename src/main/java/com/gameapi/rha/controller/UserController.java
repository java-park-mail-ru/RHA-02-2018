package com.gameapi.rha.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.gameapi.rha.models.Message;
import com.gameapi.rha.models.Rating;
import com.gameapi.rha.models.User;
import com.gameapi.rha.services.UserService;


import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@CrossOrigin(origins = {"https://rha-staging-deploy.herokuapp.com", "http://bf-balance.herokuapp.com", "http://localhost:3000"}, allowCredentials = "true")
@RequestMapping("/users")
@EnableJdbcHttpSession
public class UserController {

  static ObjectMapper mapper = new ObjectMapper();


  /**
   * Enum of status messages for response.
   * Elements from this enum are used for response.
   */
  @SuppressWarnings("CheckStyle")
  private enum UserStatus {
    SUCCESSFULLY_REGISTERED,
    SUCCESSFULLY_AUTHED,
    SUCCESSFULLY_LOGGED_OUT,
    SUCCESSFULLY_CHANGED,
    ACCESS_ERROR,
    WRONG_CREDENTIALS,
    NOT_UNIQUE_USERNAME,
    ALREADY_AUTHENTICATED,
    UNEXPECTED_ERROR,
    NOT_FOUND
  }

  /**
   * User creation function.
   * @param user user to create
   * @param session session to input user
   * @return returns response
   */

  @PostMapping(path = "/create", consumes = "application/json", produces = "application/json")
  public ResponseEntity<?> create(HttpSession session,
                               @RequestBody User user) {
    // Аутентифицированный пользователь не может зарегистрироваться

    if (session.getAttribute("user") != null) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
              new Message(UserStatus.ALREADY_AUTHENTICATED,user.getUsername()));
    }
    if(user.getPassword()==null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Message(UserStatus.WRONG_CREDENTIALS));
    }

    user.saltHash();
    try {
      UserService.createUser(user);
    } catch (DuplicateKeyException except) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body(new Message(UserStatus.NOT_UNIQUE_USERNAME));
    } catch (NullPointerException except) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Message(UserStatus.WRONG_CREDENTIALS));
    }
    sessionAuth(session, user);
    return ResponseEntity.status(HttpStatus.CREATED).body(
            new Message(UserStatus.SUCCESSFULLY_REGISTERED,user.getUsername()));

  }

  /**
   * Function to authorise user.
   * @param user its user to authorise
   * @param session his session to change
   * @return response
   */
  @PostMapping(path = "/auth", consumes = "application/json", produces = "application/json")
  public ResponseEntity auth(@RequestBody User user,
                             HttpSession session)  {
    ResponseEntity respond;
    if (session.getAttribute("user") != null) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
              new Message(UserStatus.ALREADY_AUTHENTICATED));
    }

    // Если неверные учетные данные
    user = UserService.check(user.getEmail(), user.getPassword());
    if (user == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Message(UserStatus.WRONG_CREDENTIALS));

    }
    sessionAuth(session, user);
    return ResponseEntity.status(HttpStatus.OK).body(new Message(UserStatus.SUCCESSFULLY_AUTHED));
  }

  /**
   * Function to logout user.
   * @param request actually request
   * @param session session to logout
   * @param response response, that logs out
   * @return logout
   */
  @PostMapping(path = "/logout")
  public ResponseEntity logout(HttpServletRequest request,
                               HttpSession session, HttpServletResponse response) {

    if (session.getAttribute("user") == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Message(UserStatus.ACCESS_ERROR));
    }
    session.setAttribute("user", null);
    session.invalidate();
    return ResponseEntity.status(HttpStatus.OK).body(
            new Message(UserStatus.SUCCESSFULLY_LOGGED_OUT));

  }

  /**
   * This function gets paginated rating.
   * @param page page of rating to show
   * @param request request to respone
   * @param session session to check authentification
   * @param response response of rating
   * @return rating
   */
  @GetMapping(path = "/rating/{page}")
  public ResponseEntity rating(@PathVariable("page") Integer page,
                               HttpServletRequest request, HttpSession session,
                               HttpServletResponse response) {
    if (page == null) {
      page = 1;
    }
    page--;


    List<Map<String,Integer>> resp;
    // Мы не можем получить статистику, не войдя

    if (session.getAttribute("user") == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Message(UserStatus.ACCESS_ERROR));
    }

    try {
      resp = UserService.rating(page,session.getAttribute("user").toString());
    } catch (IncorrectResultSizeDataAccessException exc) {

      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Message(UserStatus.NOT_FOUND));
    } catch (DataAccessException exc) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(
              new Message("Что-то на сервере."));
    }

    return ResponseEntity.status(HttpStatus.OK).body(resp);
  }



  /**
   * Function to get information about session and user.
   * @param session session to check
   * @return information
   */
  @GetMapping(path = "/info")
  public ResponseEntity info(HttpSession session) {
    if (session.getAttribute("user") == null) {
      session.invalidate();
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
              new Message(UserStatus.ACCESS_ERROR));
    }

    final User result;
    try {
      result = UserService.userInfo((String) session.getAttribute("user"));
    } catch (IncorrectResultSizeDataAccessException exc) {
      // Этого быть не может
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
              new Message(UserStatus.UNEXPECTED_ERROR));
    } catch (DataAccessException exc) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(
              new Message("Что-то на сервере."));
    }

    result.setPassword("You are not gonna steal data this way, you little piece of shit.");

    return ResponseEntity.status(HttpStatus.OK).body(new Message(result));
  }

  /**
   * Function to change user parameters.
   * @param user changed user
   * @param session user to change
   * @return changed user
   */
  @PostMapping(path = "/change")
  public ResponseEntity change(@RequestBody User user, HttpSession session) {

    if (session.getAttribute("user") == null) {
      session.invalidate();
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Message(UserStatus.ACCESS_ERROR));
    }
    user.setUsername(session.getAttribute("user").toString());
    UserService.changeUser(user);

    return ResponseEntity.status(HttpStatus.OK).body(new Message(UserStatus.SUCCESSFULLY_CHANGED));
  }

  private static void sessionAuth(HttpSession session, User user) {
    session.setAttribute("user", user.getUsername());
    session.setMaxInactiveInterval(30 * 60);
  }


  @PostMapping(path = "/chpwd", consumes = "application/json", produces = "application/json")
  public ResponseEntity changePass(@RequestBody Map<String, String> json,
                             HttpSession session)  {

    // Мы не можем дважды аутентицифироваться
    if (session.getAttribute("user") == null) {
      session.invalidate();
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
               new Message(UserStatus.NOT_FOUND));
    }
    String old = json.get("oldp");
    String newp = json.get("newp");
    // Если неверные учетные данные
    User user;
    try {
         user = UserService.userInfo(session.getAttribute("user").toString());
    } catch (DataAccessException Except) {
        session.invalidate();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new Message(UserStatus.WRONG_CREDENTIALS));
    }
    if (old == null || !user.checkPassword(old) || newp == null) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new Message(UserStatus.WRONG_CREDENTIALS));
    }
    user.setPassword(newp);

    UserService.changeUser(user);
    return ResponseEntity.status(HttpStatus.OK).body(new Message(UserStatus.SUCCESSFULLY_AUTHED));

  }


}

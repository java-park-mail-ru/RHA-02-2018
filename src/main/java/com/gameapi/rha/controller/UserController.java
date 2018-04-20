package com.gameapi.rha.controller;

import com.gameapi.rha.models.Message;
import com.gameapi.rha.models.User;
import com.gameapi.rha.services.UserService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;



@RestController
@CrossOrigin(origins = {"https://rha-staging-deploy.herokuapp.com", "https://bf-balance.herokuapp.com", "http://localhost:3000"}, allowCredentials = "true")
@RequestMapping("/users")
@EnableJdbcHttpSession

public class UserController {

  /**
   * Enum of status messages for response.

   * Elements from this enum are used for response.

   */
  @Autowired
  protected UserService userService;

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
    if (user.getPassword() == null || user.getEmail() == null || user.getUsername()==null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
              new Message(UserStatus.WRONG_CREDENTIALS));
    }

    user.saltHash();
    try {
      userService.createUser(user);
    } catch (DuplicateKeyException except) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body(
              new Message(UserStatus.NOT_UNIQUE_USERNAME));
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
      try {
          user = userService.check(user.getEmail(), user.getPassword());
      } catch (DataAccessException exc) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new Message(UserStatus.WRONG_CREDENTIALS));
      }

// catch (IncorrectResultSizeDataAccessException Exc){
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
//              new Message(UserStatus.WRONG_CREDENTIALS));
//
//    }
    catch (RuntimeException exc) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new Message(UserStatus.UNEXPECTED_ERROR));
    }
    if (user == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
              new Message(UserStatus.WRONG_CREDENTIALS));
    }
    sessionAuth(session, user);

    return ResponseEntity.status(HttpStatus.OK).body(new Message(UserStatus.SUCCESSFULLY_AUTHED));
  }

  /**
   * Function to logout user.
   * @param session session to logout

   * @return logout
   */
  @PostMapping(path = "/logout")
  public ResponseEntity logout(HttpSession session) {

    if (session.getAttribute("user") == null) {

      return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Message(UserStatus.ACCESS_ERROR));

    }
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


    List<List<Map<String, Integer>>> resp;
    // Мы не можем получить статистику, не войдя

    if (session.getAttribute("user") == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
              new Message(UserStatus.ACCESS_ERROR));
    }

    try {
      resp = userService.rating(page,session.getAttribute("user").toString());
    }    catch (DataAccessException exc) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(
              new Message(UserStatus.UNEXPECTED_ERROR,"Что-то на сервере."));

    }

// catch (DataIntegrityViolationException exc) {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
//                new Message("Перебрал ты страниц... Мдааа..."));
//
//    }
    if (resp == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
               new Message(UserStatus.NOT_FOUND,"Перебрал ты страниц... Мдааа..."));
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
      result = userService.userInfo((String) session.getAttribute("user"));
    } catch (IncorrectResultSizeDataAccessException exc) {
      // Этого быть не может
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
              new Message(UserStatus.UNEXPECTED_ERROR));
    } catch (DataAccessException exc) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(
              new Message(UserStatus.UNEXPECTED_ERROR,"Что-то на сервере."));
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
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Message(UserStatus.ACCESS_ERROR));
    }
    if( user.getEmail() == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Message(UserStatus.WRONG_CREDENTIALS));

    }
    user.setUsername(session.getAttribute("user").toString());
    userService.changeUser(user);


    return ResponseEntity.status(HttpStatus.OK).body(new Message(UserStatus.SUCCESSFULLY_CHANGED));
  }

  private static void sessionAuth(HttpSession session, User user) {

    session.setAttribute("user", user.getUsername());
    session.setMaxInactiveInterval(30 * 60);
  }

  /**
   * Function to change password.
   * @param json passwords(old and new)
   * @param session (session to check)
   * @return response
   */
  @PostMapping(path = "/chpwd", consumes = "application/json", produces = "application/json")
  public ResponseEntity changePass(@RequestBody Map<String, String> json,
                             HttpSession session)  {

    // Мы не можем дважды аутентицифироваться
    if (session.getAttribute("user") == null) {
      session.invalidate();
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
               new Message(UserStatus.NOT_FOUND));
    }
    final String old = json.get("oldp");
    final String newp = json.get("newp");
    // Если неверные учетные данные
    final User user;
    try {
         user = userService.userInfo(session.getAttribute("user").toString());
    } catch (DataAccessException except) {
        session.invalidate();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new Message(UserStatus.WRONG_CREDENTIALS));
    }
    if (old == null || !user.checkPassword(old) || newp == null) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new Message(UserStatus.WRONG_CREDENTIALS));
    }
    user.setPassword(newp);

    userService.changeUser(user);
    return ResponseEntity.status(HttpStatus.OK).body(new Message(UserStatus.SUCCESSFULLY_CHANGED));

  }


  /**
   * Function to change avatar.
   * @param file file of avatar to change
   * @param session session to use
   * @return response
   */
  @PostMapping("/chava")
  public ResponseEntity changeAva(@RequestParam("image") MultipartFile file,
                                   HttpSession session) {
      if (session.getAttribute("user") == null) {
          session.invalidate();
          return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                  new Message(UserStatus.NOT_FOUND));
      }
      try {
        userService.store(file,session.getAttribute("user").toString());
      } catch (IOException except) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new Message(UserStatus.UNEXPECTED_ERROR));
      }
      return ResponseEntity.status(HttpStatus.OK).body(
              new Message(UserStatus.SUCCESSFULLY_AUTHED));

  }

  /**
   * Function to get avatar.
   * @param session session to check
   * @return response(image)
   */
    @GetMapping("/gava")
    public ResponseEntity getAva(HttpSession session) {
        if (session.getAttribute("user") == null) {
            session.invalidate();
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                    new Message(UserStatus.NOT_FOUND));
        }
        final Resource file;
        try {
             file = userService.loadAvatar(session.getAttribute("user").toString());
        } catch (DataAccessException except) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(
                    new Message(UserStatus.UNEXPECTED_ERROR));
        }
        try {
            return ResponseEntity.status(HttpStatus.OK).body(file.getFile());
        } catch (IOException exc) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    UserStatus.UNEXPECTED_ERROR);
        }
    }

  }

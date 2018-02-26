package com.gameapi.rha.services;

import com.gameapi.rha.models.User;
//import sun.security.util.Password;

import javax.servlet.http.HttpSession;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class UserService {
    private static Map<String, User> map = new HashMap<String, User>();

    public static User create(User user) throws Exception {
        if (map.containsKey(user.getUsername()) )
            return null;
        user.SaltHash();
        map.put(user.getUsername(), user);
        return user;
    }

    public static Boolean exists(User user) {
        return map.containsKey(user.getUsername());
    }
//    public static Boolean exists(String user) {
//        return map.containsKey(user);
//    }

    public static Boolean check (String user,String pass) throws Exception {
        return (map.containsKey(user) && map.get(user).checkPassword(pass));
    }

    public static Boolean sessionAuth(HttpSession session, User user)
    {
        try {
            System.out.println(session.getId());
            session.setAttribute("user", user.getUsername());
            session.setMaxInactiveInterval(30*60);
            return true;
        }

        finally {
                return false;
        }


    }

    public static User userInfo(String username) {
        return null;
    }


    private static Integer getUserCount() {
        return map.size();
    }
}

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
        user.SaltHash();
        if (map.containsKey(user.getUsername()) )
            return null;
        map.put(user.getUsername(), user);
        return user;
    }

    public static User create(String username, String email, String password) throws Exception {
        if (map.containsKey(username) )
            return null;
        User user = new User(username, email, password);
        map.put(username, user);
        return user;
    }


//    public static User auth(String username, String password)throws Exception{
//        User ToAuth=map.get(username);
//        if(ToAuth == null)
//            return null;
//        if(ToAuth.getPassword() != password)
//            return null;
//        HttpSession us = new HttpSession() {
//        };
//        us.setAttribute("user",username);
////        response.setContentType("text/html");
////        PrintWriter out = response.getWriter();
////        request.setAttribute("name", "RoseIndia");
////        RequestDispatcher rd = getServletContext().getRequestDispatcher("/servletTwo");
////        rd.forward(request, response);
//    }

    public static Boolean exists(User user) {
        return map.containsKey(user.getUsername());
    }
    public static Boolean exists(String user) {
        return map.containsKey(user);
    }

    public static Boolean check(String user,String pass) throws Exception {
        return (map.containsKey(user)&& map.get(user).checkPassword(pass));
    }
    public static HttpServletResponse sessionAuth(HttpSession session, User user, HttpServletResponse response)
    {

        System.out.println(session.getId());
        session.setAttribute("user", "Pankaj");
        //setting session to expiry in 30 mins
        session.setMaxInactiveInterval(30*60);
        Cookie userName = new Cookie("Auth",user.getUsername());
        userName.setMaxAge(30*60);
        System.out.println("<font color=red>All right.</font>");
        response.addCookie(userName);
        return response;
    }

//    public static Boolean auth(User user) {
//        if(map.containsKey(user.getUsername())
////                &&(map.get(user.getUsername()).getPassword()==user.getPassword())
//        )
//        {
//            return true;
//        }
//        else{
//            return false;
//        }
//    }



    private static Integer getUserCount() {
        return map.size();
    }
}

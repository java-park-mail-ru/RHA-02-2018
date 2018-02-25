//package com.gameapi.rha.servlets;
//
//import com.gameapi.rha.services.UserService;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.io.IOException;
//import java.io.PrintWriter;
//
//import javax.servlet.RequestDispatcher;
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.Cookie;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//
//
//
//public class AuthServlet extends HttpServlet {
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String user = request.getParameter("user");
//        String password = request.getParameter("password");
//        if(UserService.auth(user,password)){
//            HttpSession session = request.getSession();
//            session.setAttribute("user", "Pankaj");
//            //setting session to expiry in 30 mins
//            session.setMaxInactiveInterval(30*60);
//            Cookie userName = new Cookie("user", user);
//            userName.setMaxAge(30*60);
//            System.out.println("<font color=red>All right.</font>");
//            response.addCookie(userName);
//            response.sendRedirect("/");
//        }else{
//            RequestDispatcher rd = getServletContext().getRequestDispatcher("/wrong");
//            System.out.println("<font color=red>Either user name or password is wrong.</font>");
//            rd.include(request, response);
//        }
//
//    }
//
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//
//    }
//}

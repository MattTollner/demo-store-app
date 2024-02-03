//package com.mattcom.demostoreapp.controller;
//
//
//
//import com.mattcom.demostoreapp.entity.User;
//import com.mattcom.demostoreapp.service.UserService;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//
//@RestController
//@RequestMapping("/users")
//public class UserController {
//
//    private UserService userService;
//
//    public UserController(UserService userService) {
//        this.userService = userService;
//    }
//
//    @GetMapping("/list")
//    public String listUsers(Model model){
//        List<User> users = userService.findAll();
//
//        for (User user: users) {
//            System.out.println(user.toString());
//        }
//        return "logs";
//    }
//
//
//}

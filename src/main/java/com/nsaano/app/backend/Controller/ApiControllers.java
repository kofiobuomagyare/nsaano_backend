package com.nsaano.app.backend.Controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nsaano.app.backend.Models.User;
import com.nsaano.app.backend.Repo.UserRepo;

@RestController
public class ApiControllers {
    
   @Autowired
    private UserRepo userRepo;
   @GetMapping("/")
    public String getPage(){
        return "Welcome";
    }
    
    @GetMapping("/users")
    public List<User> getUsers(){
        return userRepo.findAll();
    }

    @PostMapping("/save")
    public String SaveUser(@RequestBody User user){
        userRepo.save(user);
        return "User saved...";
    }
}

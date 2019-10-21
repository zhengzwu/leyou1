package com.leyou.web;


import com.leyou.pojo.User;
import com.leyou.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("check/{data}/{type}")
    public ResponseEntity<Boolean> check(@PathVariable("data") String data,
                                         @PathVariable("type") Integer type){
        Boolean check = userService.check(data, type);

        return ResponseEntity.ok(check);
    }
    @PostMapping("code")
    public ResponseEntity<Void> sendCode(@RequestParam("phone" )String phone){
        userService.sendcode(phone);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    @PostMapping("register")
    public ResponseEntity<Void> register(User user,@RequestParam("code" )String code){

        userService.register(user,code);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @GetMapping("query")
    public ResponseEntity<User> queryUsernameAndPassword(
            @RequestParam("username") String username,
            @RequestParam("password") String password){

        return ResponseEntity.ok(userService.queryUsernameAndPassword(username,password));

    }
}

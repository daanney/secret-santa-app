package com.danney.xmas.users;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@CrossOrigin
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UsersService usersService;

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("pong");
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User saved = usersService.createUser(user);
        if(null == saved) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(user);
        }

        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User user) {
        if(null != user && null != user.getName() && null != user.getPassword()) {
            User loggedIn = usersService.login(user.getName(), user.getPassword());
            if(null != loggedIn)
                return ResponseEntity.ok(loggedIn);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new User());
    }

    @PostMapping("/pwd")
    public ResponseEntity<User> updatePassword(@RequestBody User req) {
        if(null != req && !StringUtils.isEmpty(req.getToken()) && !StringUtils.isEmpty(req.getPassword())) {
            User user = usersService.updatePassword(req.getToken(), req.getPassword());
            if(null != user) {
                return ResponseEntity.ok(user);
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(req);
    }

    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = usersService.getUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") Integer id) {
        User user = usersService.getUser(id);
        if(null == user) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(user);
        }

        return ResponseEntity.ok(user);
    }

    @GetMapping("/assign")
    public ResponseEntity<String> assignSantas() {
        boolean result = usersService.assignSantas();
        return ResponseEntity.ok(String.format("%s", result));
    }
}

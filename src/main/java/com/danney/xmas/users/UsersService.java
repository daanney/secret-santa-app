package com.danney.xmas.users;

import com.sun.istack.NotNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service
public class UsersService {

    @Autowired
    private UsersRepository usersRepository;

    public User createUser(User user) {
        if(usersRepository.findByName(user.getName()).isPresent()
            || StringUtils.isEmpty(user.getName()) || StringUtils.isEmpty(user.getPassword()))
            return null;

        user.setId(null);
        user.setPassword(user.getPassword());
        User saved = usersRepository.save(user);
        user.setToken(generateToken(user));
        user.setUserKey(hashOf(String.format("%s", user.getName())));
        usersRepository.save(saved);
        return getUser(saved.getId());
    }

    public @NotNull List<User> getUsers() {
        List<User> users = new LinkedList<>();
        usersRepository.findAll().forEach(users::add);
        users.forEach(user -> {
            user.setPassword(null);
            user.setAssignedUser(null);
            user.setToken(null);
        });
        return users;
    }

    public User getUser(Integer id) {
        return getUser(id, true);
    }

    public User getUser(Integer id, boolean recursive) {
        User user = usersRepository.findById(id).orElse(null);
        if(null != user) {
            user.setPassword(null);
            if(recursive && null != user.getAssignedUserId()) {
                User assigned = getUser(user.getAssignedUserId(), false);
                user.setAssignedUser(assigned);
            }
        }
        return user;
    }

    public User login(String name, String password) {
        Optional<User> userOpt = usersRepository.findByName(name);
        if(userOpt.isPresent()) {
            User user = userOpt.get();
            String pwd = hashOf(password);
            if(pwd.equals(user.getPassword())) {
                return getUser(user.getId());
            }
        }

        return null;
    }

    public User updatePassword(String token, String password) {
        Optional<User> userOptional = usersRepository.findByToken(token);

        if(!userOptional.isPresent() || StringUtils.isEmpty(password))
            return null;

        User dbuser = userOptional.get();
        dbuser.setPassword(hashOf(password));
        dbuser.setToken(generateToken(dbuser));
        usersRepository.save(dbuser);
        return getUser(dbuser.getId());
    }

    public boolean assignSantas() {
        Iterable<User> users = usersRepository.findAll();
        List<User> ids = new LinkedList<>();

        for(User user : users) ids.add(user);
        Collections.shuffle(ids);
        User first = ids.remove(0);

        User prev  = first;
        for(User curr : ids) {
            prev.setAssignedUserId(curr.getId());
            usersRepository.save(prev);
            prev = curr;
        }

        prev.setAssignedUserId(first.getId());
        usersRepository.save(prev);

        return true;
    }

    private String hashOf(String source) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(source.getBytes());
            return DatatypeConverter.printHexBinary(md.digest());
        }catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException("hash failed");
        }
    }

    private String generateToken(User user) {
        return hashOf(String.format("%s%s%s", user.getId(), user.getPassword(), user.getName()));
    }
}

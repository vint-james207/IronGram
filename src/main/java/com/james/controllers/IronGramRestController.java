package com.james.controllers;

import com.james.entities.Photo;
import com.james.entities.User;
import com.james.services.PhotoRepository;
import com.james.services.UserRepository;
import com.james.utils.PasswordStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.time.LocalDateTime;


/**
 * Created by jamesyburr on 6/28/16.
 */
@RestController
public class IronGramRestController {

    @Autowired
    UserRepository users;

    @Autowired
    PhotoRepository photos;

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public User login(@RequestBody User user, HttpSession session) throws Exception {
        User userFromDB = users.findFirstByName(user.getName());
        if (userFromDB == null) {
            user.setPassword(PasswordStorage.createHash(user.getPassword()));
            users.save(user);
        }
        else if (!PasswordStorage.verifyPassword(user.getPassword(), userFromDB.getPassword())) {
            throw new Exception("Wrong password!");
        }
        session.setAttribute("username", user.getName());
        return user;
    }

    @RequestMapping(path = "/logout", method = RequestMethod.POST)
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @RequestMapping(path = "/photos", method = RequestMethod.GET)
    public Iterable<Photo> getPhotos(HttpSession session) {
        String username = (String) session.getAttribute("username");
        User user = users.findFirstByName(username);
        Iterable<Photo> recipPhoto = photos.findByRecipient(user);

        for (Photo photo : recipPhoto) {
            if (photo.getDt() == null) {
                photo.setDt(LocalDateTime.now());
                photos.save(photo);
            }
            else if(LocalDateTime.now().isAfter(photo.getDt().plusSeconds(photo.getDeleteTime()))) {
                File f = new File("public/photos/" + photo.getFilename());
                f.delete();
                photos.delete(photo);
            }
        }
         return photos.findByRecipient(user);
    }

    @RequestMapping(path = "/user", method = RequestMethod.GET)
    public User getUser(HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return null;
        }
        else {
            return users.findFirstByName(username);
        }
    }

    @RequestMapping(path = "/public-photos", method = RequestMethod.GET)
    public Iterable<Photo> publicPhotos(String username) {
        User user = users.findFirstByName(username);
        return photos.findByIsPublicAndSender(true, user);

    }
}

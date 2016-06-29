package com.james.controllers;

import com.james.entities.Photo;
import com.james.entities.User;
import com.james.services.PhotoRepository;
import com.james.services.UserRepository;
import org.h2.tools.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * Created by jamesyburr on 6/28/16.
 */
@Controller
public class IronGramController {

    @Autowired
    UserRepository users;

    @Autowired
    PhotoRepository photos;

    @PostConstruct
    public void init() throws SQLException {
        Server.createWebServer().start();
    }

    @RequestMapping(path = "upload", method = RequestMethod.POST)
    public String upload(MultipartFile file, String receiver, HttpSession session, int deleteTime, boolean isPublic) throws Exception {
        String username = (String) session.getAttribute("username");
        User sender = users.findFirstByName(username);
        User rec = users.findFirstByName(receiver);

        if (sender == null || rec == null) {
            throw new Exception("Can't find sender or receiver!");
        }

        if (!file.getContentType().contains("image")) {
            throw new Exception("Incorrect file type");
        }
        File dir = new File("public/photos");
        dir.mkdirs();
        File photofile = File.createTempFile("photo", file.getOriginalFilename(), dir);
        FileOutputStream fos = new FileOutputStream(photofile);

        fos.write(file.getBytes());

        Photo photo = new Photo(sender, rec, photofile.getName(), deleteTime, isPublic);
        photos.save(photo);


        return "redirect:/";
    }
}

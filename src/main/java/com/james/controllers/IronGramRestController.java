package com.james.controllers;

import com.james.services.PhotoRepository;
import com.james.services.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by jamesyburr on 6/28/16.
 */
@RestController
public class IronGramRestController {

    @Autowired
    UserRepository users;

    @Autowired
    PhotoRepository photos;
}

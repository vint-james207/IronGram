package com.james.services;

import com.james.entities.Photo;
import com.james.entities.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by jamesyburr on 6/28/16.
 */
public interface PhotoRepository extends CrudRepository<Photo, Integer> {

}

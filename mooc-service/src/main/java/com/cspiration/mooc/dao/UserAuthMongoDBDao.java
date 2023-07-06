package com.cspiration.mooc.dao;

import com.cspiration.mooc.entity.UserAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserAuthMongoDBDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    public void insert(UserAuth userAuth) {
        mongoTemplate.save(userAuth);
    }
}

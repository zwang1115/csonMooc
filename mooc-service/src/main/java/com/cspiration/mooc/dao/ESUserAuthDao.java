package com.cspiration.mooc.dao;

import com.cspiration.mooc.entity.UserAuth;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ESUserAuthDao extends ElasticsearchRepository<UserAuth, Long> {

    UserAuth findByUsernameLike(String keyword);
}

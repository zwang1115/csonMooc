package com.cspiration.mooc.dao;

import com.cspiration.mooc.entity.Comment;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ESCommentDao extends ElasticsearchRepository<Comment, Long> {

    Comment findByContentContaining(String keyword);
}

package com.cspiration.mooc.service;

import com.cspiration.mooc.entity.Comment;

import java.util.List;


public interface ICommentService {

    /**
     * 新增评论
     * @param comment
     * @return
     */
    Comment insert(Comment comment);

    /**
     * 查找评论
     * @param comment
     * @return
     */
    List<Comment> query(Comment comment);
}

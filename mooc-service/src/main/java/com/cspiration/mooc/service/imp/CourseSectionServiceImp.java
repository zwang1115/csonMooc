package com.cspiration.mooc.service.imp;

import com.cspiration.mooc.dao.CourseSectionDao;
import com.cspiration.mooc.entity.CourseSection;
import com.cspiration.mooc.service.ICourseSectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseSectionServiceImp implements ICourseSectionService {

    @Autowired
    private CourseSectionDao courseSectionDao;
    @Override
    public List<CourseSection> query(CourseSection courseSection) {
        return courseSectionDao.query(courseSection);
    }
}

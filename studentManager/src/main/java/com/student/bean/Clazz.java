package com.student.bean;

import org.springframework.stereotype.Component;

@Component
public class Clazz {
    private Integer id;
    private Integer gradeId;//年级Id
    private String name;
    private String remark;

    public Clazz() {
    }

    public Clazz(Integer id, Integer gradeId, String name, String remark) {
        this.id = id;
        this.gradeId = gradeId;
        this.name = name;
        this.remark = remark;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGradeId() {
        return gradeId;
    }

    public void setGradeId(Integer gradeId) {
        this.gradeId = gradeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

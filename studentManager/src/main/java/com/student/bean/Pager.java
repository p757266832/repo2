package com.student.bean;

import org.springframework.stereotype.Component;

@Component
public class Pager {
    private int page;//当前页数
    private int rows;//每页显示多少条记录
    private int offset;//起始索引

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getOffset() {
        this.offset = (page - 1) * rows;
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = (page - 1) * rows;
    }
}

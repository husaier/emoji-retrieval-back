package org.bupt.hse.retrieval.vo;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * created by Hu Saier <husserl@bupt.edu.cn>
 * 2024-10-01
 */

public class PageVO<T> {

    @ApiModelProperty("当前页码")
    long cur;

    @ApiModelProperty("页大小")
    long pageSize;

    @ApiModelProperty("总页数")
    long pageNum;

    @ApiModelProperty("总数据行数")
    long total;

    @ApiModelProperty("数据")
    List<T> records;

    public long getCur() {
        return cur;
    }

    public void setCur(long cur) {
        this.cur = cur;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    public long getPageNum() {
        return pageNum;
    }

    public void setPageNum(long pageNum) {
        this.pageNum = pageNum;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }
}

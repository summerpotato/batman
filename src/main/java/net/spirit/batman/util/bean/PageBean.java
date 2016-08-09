package net.spirit.batman.util.bean;

import java.io.Serializable;
import java.util.List;

public class PageBean<T> implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer pageNum;
	private Integer pageSize;
	private Integer totalPages;
	private Integer totalRows;
	
	private List<T> data;
	
	public PageBean() {
	}
		
	public PageBean(Integer pageNum, Integer pageSize, Integer totalPages, Integer totalRows, List<T> data) {
		this.pageNum = pageNum;
		this.pageSize = pageSize;
		this.totalRows = totalRows;
		this.totalPages = totalPages;
		this.data = data;
	}

	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}

	public Integer getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(Integer totalRows) {
		this.totalRows = totalRows;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}
	
}

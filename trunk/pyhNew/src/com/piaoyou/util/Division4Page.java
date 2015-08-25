package com.piaoyou.util;

import java.util.List;


public class Division4Page {

	private List list;
	private int previous;
	private int next;
	private int pageNum;
	private int pageCount;
	private int startNav;
	private int endNav;
	private int rowCount;
	private int first = 1;
	private int end;
	private int nvaSize = 11;
	private int size = 10;
	private int startRow;

	public Division4Page() {
	}

	public Division4Page(String pageNum, int rowCount, int size) {
		if (pageNum == null || pageNum.equals("")||Integer.parseInt(pageNum)==0) {
			this.pageNum = 1;
		} else {
			this.pageNum = Integer.parseInt(pageNum);
		}
		this.size = size;
		this.rowCount = rowCount;
		// this.list = list;

		this.startRow = (this.pageNum - 1) * this.size;

		this.pageCount = (int) Math.ceil(rowCount * 1.0 / this.size);

		this.end = (this.pageCount==0)?1:this.pageCount;

		this.startNav = Math.max(this.first, this.pageNum - (nvaSize - 1) / 2);

		this.endNav = Math.min(this.end, this.pageNum + (nvaSize - 1) / 2);

		this.previous = Math.max(this.first, this.pageNum - 1);

		this.next = Math.min(this.end, this.pageNum + 1);

		if ((this.pageCount - this.pageNum) < (nvaSize - 1) / 2) {
			this.startNav = Math
					.max(
							(this.startNav - (nvaSize - 1) / 2 + (this.pageCount - this.pageNum)),
							this.first);
		}

		if ((this.pageNum - this.first) < (nvaSize - 1) / 2) {
			this.endNav = Math
					.min(
							(this.endNav + (nvaSize - 1) / 2 - (this.pageNum - this.first)),
							this.end);
		}

	}

	public int getPrevious() {
		return previous;
	}

	public void setPrevious(int previous) {
		this.previous = previous;
	}

	public int getNext() {
		return next;
	}

	public void setNext(int next) {
		this.next = next;
	}

	public int getStartNav() {
		return startNav;
	}

	public void setStartNav(int startNav) {
		this.startNav = startNav;
	}

	public int getEndNav() {
		return endNav;
	}

	public void setEndNav(int endNav) {
		this.endNav = endNav;
	}

	public int getFirst() {
		return first;
	}

	public void setFirst(int first) {
		this.first = first;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}

	public int getStartRow() {
		return startRow;
	}

	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public int getRowCount() {
		return rowCount;
	}

	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}

	public int getNvaSize() {
		return nvaSize;
	}

	public void setNvaSize(int nvaSize) {
		this.nvaSize = nvaSize;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

}

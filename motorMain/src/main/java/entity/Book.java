package entity;

import java.util.List;

public class Book {
    private Integer bid;
    private String bname;
    private List<Category> categories;
    
	public Integer getBid() {
        return bid;
    }
    public void setBid(Integer bid) {
        this.bid = bid;
    }
    public String getBname() {
        return bname;
    }
    public void setBname(String bname) {
        this.bname = bname == null ? null : bname.trim();
    }
	public List<Category> getCategories() {
		return categories;
	}
	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}
}






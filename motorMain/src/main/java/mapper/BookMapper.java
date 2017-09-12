package mapper;

import entity.Book;

import java.util.List;


public interface BookMapper {
	
   List<Book> queryAll();
   
}
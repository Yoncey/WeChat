package yoncey.gdkm.weixin.library.service;

import org.springframework.data.domain.Page;

import yoncey.gdkm.weixin.library.domain.Book;
import yoncey.gdkm.weixin.library.domain.DebitList;

public interface LibraryService {
	public Page<Book> search(String keyword, int pageNumber);

	public void add(String id, DebitList debitList);

	public void remove(String id, DebitList debitList);
}

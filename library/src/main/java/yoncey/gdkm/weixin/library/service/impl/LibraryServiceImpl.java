package yoncey.gdkm.weixin.library.service.impl;

import java.util.LinkedList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import yoncey.gdkm.weixin.library.domain.Book;
import yoncey.gdkm.weixin.library.domain.DebitList;
import yoncey.gdkm.weixin.library.repository.BookRepository;
import yoncey.gdkm.weixin.library.service.LibraryService;

@Service
public class LibraryServiceImpl implements LibraryService {

	@Autowired
	private BookRepository bookRepository;

	@Override
	public Page<Book> search(String keyword, int pageNumber) {
		Pageable pageable = PageRequest.of(pageNumber, 10);
		Page<Book> page;
		if (StringUtils.isEmpty(keyword)) {
			page = this.bookRepository.findByDisabledFalse(pageable);
		} else {
			page = this.bookRepository.findByDisabledFalseAndNameContaining(keyword, pageable);
		}
		return page;
	}

	@Override
	public void add(String id, DebitList debitList) {
		if (debitList.getBooks() == null) {
			debitList.setBooks(new LinkedList<>());
		}
		boolean exists = false;
		for (Book b : debitList.getBooks()) {
			if (b.getId().equals(id)) {
				exists = true;
				break;
			}
		}
		if (!exists) {
			this.bookRepository.findById(id).ifPresent(debitList.getBooks()::add);
		}
	}

	@Override
	public void remove(String id, DebitList debitList) {
		debitList.getBooks().stream().filter(book -> book.getId().equals(id)).findFirst()
				.ifPresent(debitList.getBooks()::remove);
	}
}

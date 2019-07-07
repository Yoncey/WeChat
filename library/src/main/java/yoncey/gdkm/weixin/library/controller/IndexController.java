package yoncey.gdkm.weixin.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import yoncey.gdkm.weixin.library.domain.Book;
import yoncey.gdkm.weixin.library.service.LibraryService;

@Controller
@RequestMapping("/kemao/library")
public class IndexController {

	@Autowired
	private LibraryService libraryService;

	@RequestMapping
	public String index(@RequestParam(name = "keyword", required = false) String keyword,
			@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber, Model model) {
		Page<Book> page = this.libraryService.search(keyword, pageNumber);
		model.addAttribute("page", page);
		return "/WEB-INF/views/library/index.jsp";
	}
}

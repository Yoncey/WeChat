package yoncey.gdkm.weixin.menu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import yoncey.gdkm.weixin.menu.domain.SelfMenu;
import yoncey.gdkm.weixin.menu.service.SelfMenuService;

@Controller
@RequestMapping("/kemao/menu")
public class SelfMenuController {

	@Autowired
	private SelfMenuService menuService;

	@GetMapping
	public ModelAndView index() {
		return new ModelAndView("/WEB-INF/views/menu/index.jsp");

	}

	@GetMapping(produces = "application/json")
	@ResponseBody
	public SelfMenu data() {
		SelfMenu a = menuService.getMenu();
		return a;

	}

	@PostMapping
	@ResponseBody
	public String save(@RequestBody SelfMenu selfMenu) {
		this.menuService.save(selfMenu);
		return "保存成功";
	}
}

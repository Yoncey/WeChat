package yoncey.gdkm.weixin.menu.service;

import yoncey.gdkm.weixin.menu.domain.SelfMenu;

public interface SelfMenuService {

	SelfMenu getMenu();

	void save(SelfMenu selfMenu);

}

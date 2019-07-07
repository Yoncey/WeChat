package yoncey.gdkm.weixin.menu.service.impl;

import java.util.List;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import yoncey.gdkm.weixin.menu.domain.MenuButton;
import yoncey.gdkm.weixin.menu.domain.SelfMenu;
import yoncey.gdkm.weixin.menu.repository.SelfMenuRepository;
import yoncey.gdkm.weixin.menu.service.SelfMenuService;
import yoncey.gdkm.weixin.service.WeiXinProxy;

@Service
public class SelfMenuServiceImpl implements SelfMenuService {

	private static final Logger LOG = LoggerFactory.getLogger(SelfMenuServiceImpl.class);
	@Autowired
	private SelfMenuRepository menuRepository;
	@Autowired
	private WeiXinProxy weiXinProxy;

	@Override
	public SelfMenu getMenu() {
		List<SelfMenu> all = this.menuRepository.findAll();
		if (all.isEmpty()) {
			return new SelfMenu();
		}
		return all.get(0);
	}

	@Override
	public void save(SelfMenu selfMenu) {
		selfMenu.getSubMenus().forEach(b1 -> {
			if (!b1.getSubMenus().isEmpty()) {
				b1.setAppId(null);
				b1.setKey(null);
				b1.setMediaId(null);
				b1.setPagePath(null);
				b1.setType(null);
				b1.setUrl(null);
			}
		});
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode buttonNode = mapper.createObjectNode();
		ArrayNode buttonsNode = mapper.createArrayNode();

		buttonNode.set("button", buttonsNode);
		selfMenu.getSubMenus().forEach(b1 -> {
			ObjectNode menu1 = mapper.createObjectNode();
			buttonsNode.add(menu1);
			menu1.put("name", b1.getName());
			if (b1.getSubMenus().isEmpty()) {
				setValues(menu1, b1);
			} else {
				ArrayNode subButtons = mapper.createArrayNode();
				menu1.set("sub_button", subButtons);
				b1.getSubMenus().forEach(new Consumer<MenuButton>() {
					@Override
					public void accept(MenuButton b2) {
						ObjectNode menu2 = mapper.createObjectNode();
						subButtons.add(menu2);
						menu2.put("name", b2.getName());
						setValues(menu2, b2);
					}
				});
			}
		});
		try {
			String json = mapper.writeValueAsString(buttonNode);
			this.weiXinProxy.createMenu(json);
		} catch (JsonProcessingException e) {
			LOG.error("更新微信公众号菜单出现问题：" + e.getLocalizedMessage(), e);
		}
	}

	private void setValues(ObjectNode menu, MenuButton b) {
		menu.put("type", b.getType());
		if (b.getType().equals("miniprogram")) {
			menu.put("appid", b.getAppId());
			menu.put("url", b.getUrl());
			menu.put("pagepath", b.getPagePath());
		} else if ("click".equals(b.getType())//
				|| "scancode_push".equals(b.getType())//
				|| "scancode_waitmsg".equals(b.getType())//
				|| "pic_sysphoto".equals(b.getType())//
				|| "pic_photo_or_album".equals(b.getType())//
				|| "pic_weixin".equals(b.getType())//
				|| "location_select".equals(b.getType())) {
			menu.put("key", b.getKey());
		} else if ("media_id".equals(b.getType())//
				|| "view_limited".equals(b.getType())) {
			menu.put("media_id", b.getMediaId());
		} else if ("view".equals(b.getType())) {
			menu.put("url", b.getUrl());
		}
	}
}
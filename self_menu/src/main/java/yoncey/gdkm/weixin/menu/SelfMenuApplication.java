package yoncey.gdkm.weixin.menu;

import yoncey.gdkm.weixin.CommonsConfig;
import yoncey.gdkm.weixin.service.AccessTokenManager;
import yoncey.gdkm.weixin.serviceImpl.AccessTokenManagerSample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages="yoncey.gdkm")
@EntityScan("yoncey.gdkm")
@ComponentScan("yoncey.gdkm")
public class SelfMenuApplication implements CommonsConfig {

	public static void main(String[] args) {
		SpringApplication.run(SelfMenuApplication.class, args);
		AccessTokenManager access=new AccessTokenManagerSample();
	}
}

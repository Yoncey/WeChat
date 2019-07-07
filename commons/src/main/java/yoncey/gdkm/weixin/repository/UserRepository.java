package yoncey.gdkm.weixin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import yoncey.gdkm.weixin.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
	User findByOpenId(String openId);
}


package cn.e3mall.sso.service;

import cn.e3mall.common.pojo.E3Result;

public interface UserService {
	E3Result checkUserInfo(String val, int type) throws Exception;

	E3Result register(String username, String password, String phone) throws Exception;

	E3Result login(String username, String password);

	E3Result getUserInfo(String token);

	E3Result logout(String token);
}

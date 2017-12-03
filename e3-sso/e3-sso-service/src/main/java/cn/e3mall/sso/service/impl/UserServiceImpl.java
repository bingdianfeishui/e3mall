package cn.e3mall.sso.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.util.JsonUtils;
import cn.e3mall.mapper.TbUserMapper;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.pojo.TbUserExample;
import cn.e3mall.pojo.TbUserExample.Criteria;
import cn.e3mall.sso.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private TbUserMapper userMapper;

	@Autowired
	private JedisClient jedisClient;

	@Value("${USER_SESSION_PREFIX}")
	private String USER_SESSION_PREFIX;
	@Value("${USER_SESSION_EXPIRE}")
	private int USER_SESSION_EXPIRE;

	@Override
	public E3Result checkUserInfo(String val, int type) throws Exception {
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		// type: 1-用户名 2-手机号 3-邮箱
		if (type == 1)
			criteria.andUsernameEqualTo(val);
		else if (type == 2)
			criteria.andPhoneEqualTo(val);
		else if (type == 3)
			criteria.andEmailEqualTo(val);
		else
			throw new Exception("invalid type value:" + type);

		int count = userMapper.countByExample(example);
		return count == 0 ? E3Result.ok(true) : E3Result.ok(false);
	}

	@Override
	public E3Result register(String username, String password, String phone) throws Exception {
		// 数据有效性校验
		if (StringUtils.isBlank(username) || StringUtils.isBlank(password) || StringUtils.isBlank(phone))
			return E3Result.build(HttpStatus.SC_BAD_REQUEST, "注册信息不完整，注册失败！");
		E3Result result1 = this.checkUserInfo(username, 1);
		if (!(Boolean) result1.getData())
			return E3Result.build(HttpStatus.SC_BAD_REQUEST, "用户名已存在！");
		E3Result result2 = this.checkUserInfo(phone, 2);
		if (!(Boolean) result2.getData())
			return E3Result.build(HttpStatus.SC_BAD_REQUEST, "手机号已被使用！");

		// 都为真，说明数据有效
		TbUser user = new TbUser();
		user.setUsername(username);
		user.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));
		user.setPhone(phone);

		// 补全user属性
		user.setCreated(new Date());
		user.setUpdated(new Date());

		userMapper.insert(user);
		return E3Result.ok();
	}

	@Override
	public E3Result login(String username, String password) {
		// 登录判断
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username.trim());
		List<TbUser> list = userMapper.selectByExample(example);
		if (list == null || list.size() == 0)
			// 未查询到同用户名的用户
			return E3Result.build(400, "用户名或密码错误！");

		TbUser user = list.get(0);
		// 校验密码
		if (!user.getPassword().equals(DigestUtils.md5DigestAsHex(password.getBytes())))
			return E3Result.build(400, "用户名或密码错误！");

		// 登录成功，生成token,写入redis
		user.setPassword(null);
		String token = UUID.randomUUID().toString();
		jedisClient.set(USER_SESSION_PREFIX + token, JsonUtils.objectToJson(user));
		jedisClient.expire(USER_SESSION_PREFIX + token, USER_SESSION_EXPIRE);

		return E3Result.ok(token);
	}

	@Override
	public E3Result getUserInfo(String token) {
		String key = USER_SESSION_PREFIX + token;
		String json = jedisClient.get(key);
		if (StringUtils.isNoneBlank(json)) {
			// 重置过期时间
			jedisClient.expire(key, USER_SESSION_EXPIRE);
			return E3Result.ok(json);
		}
		return E3Result.build(201, "登录过期，请重新登录！");
	}

	@Override
	public E3Result logout(String token) {
		String key = USER_SESSION_PREFIX + token;
		jedisClient.del(key);
		return E3Result.ok();
	}

}

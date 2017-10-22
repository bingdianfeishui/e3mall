package cn.e3mall.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.e3mall.common.util.FastDFSClient;
import cn.e3mall.common.util.JsonUtils;

/**
 * 图片上传Controller
 * <p>
 * Title: PictureController
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2017
 * </p>
 * <p>
 * Company: NULL.Co
 * </p>
 * 
 * @author Lee
 * @date 2017年10月22日下午8:02:28
 * @version 1.0
 */
@Controller
public class PictureController {

	@Value("${IMAGE_SERVER_URL}")
	private String IMAGE_SERVER_URL;

	@RequestMapping(value="/pic/upload", produces=MediaType.TEXT_PLAIN_VALUE+";charset=utf-8")
	@ResponseBody
	public String uploadFile(MultipartFile uploadFile) {
		Map<String, Object> result = new HashMap<>();
		try {
			// 把图片上传到图片服务器
			FastDFSClient client = new FastDFSClient("classpath:conf/client.conf");
			// 得到一个图片的地址和文件名
			String url = client.uploadFile(uploadFile.getBytes(),
					FilenameUtils.getExtension(uploadFile.getOriginalFilename()));
			// 补充为完整的url
			url = IMAGE_SERVER_URL + url;

			// 封装到map中返回
			result.put("error", 0);
			result.put("url", url);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("error", 1);
			result.put("message", "图片上传失败");
		}
		return JsonUtils.objectToJson(result);
	}
}

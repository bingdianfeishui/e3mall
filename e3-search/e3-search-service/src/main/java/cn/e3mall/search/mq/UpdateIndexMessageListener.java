package cn.e3mall.search.mq;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import cn.e3mall.search.service.SearchItemService;

/**
 * 更新索引的消息监听器
 * 
 * @author younng
 *
 */
public class UpdateIndexMessageListener implements MessageListener {

	@Autowired
	private SearchItemService searchItemService;

	@Override
	public void onMessage(Message message) {
		try {
			TextMessage textMsg = (TextMessage) message;
			if (textMsg != null) {
				String text = textMsg.getText();
				System.out.println("Topic Msg Recived: "+text);
				long id;
				// 有id字符串，且id转换为long不为0
				if (StringUtils.isNotBlank(text) && (id = Long.valueOf(text)) != 0) {
					searchItemService.updateItemIndexById(id);
				}
			}
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}

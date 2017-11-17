package cn.e3mall.search.mq;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;

import cn.e3mall.search.service.SearchItemService;

/**
 * 更新索引的消息监听器
 * 
 * @author younng
 *
 */
public class UpdateIndexMessageListener implements MessageListener {

	protected static final String PREFIX_UPDATE = "U";
	protected static final String PREFIX_DELETE = "D";
	protected static final String SEPARATOR = ":";

	@Autowired
	private SearchItemService searchItemService;

	@Override
	public void onMessage(Message message) {
		try {
			TextMessage textMsg = (TextMessage) message;
			if (textMsg != null) {
				System.out.println("TOPIC RECIVED! " + textMsg.getText());
				String[] strs = textMsg.getText().trim().split(SEPARATOR);
				String[] ids = {};
				if (strs.length > 1)
					ids = strs[1].split(",");

				if (PREFIX_UPDATE.equals(strs[0])) {
					searchItemService.updateItemIndexes(ids);
				} else if (PREFIX_DELETE.equals(strs[0])) {
					searchItemService.deleteItemIndexes(ids);
				} else {
					System.out.println("消息参数格式有误：" + textMsg.getText());
				}
			}
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}

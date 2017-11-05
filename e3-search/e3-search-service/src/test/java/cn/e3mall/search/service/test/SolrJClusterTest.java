package cn.e3mall.search.service.test;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.search.service.SearchItemService;

/**
 * solr-cloud集群测试
 * @author Lee
 *
 */
public class SolrJClusterTest {

	//@Test
	public void testAddToCluster() {
		// 第二步：创建一个SolrServer对象，需要使用CloudSolrServer子类。构造方法的参数是zookeeper的地址列表。
		// 参数是zookeeper的地址列表，使用逗号分隔, 不能有空格
		String zkHost = "192.168.25.101:2181,192.168.25.102:2181,192.168.25.103:2181";
		CloudSolrClient client = new CloudSolrClient(zkHost);
		// 第三步：需要设置DefaultCollection属性
		client.setDefaultCollection("core1");
		// 第四步：创建一SolrInputDocument对象。
		SolrInputDocument doc = new SolrInputDocument();
		// 第五步：向文档对象中添加域
		doc.addField("item_title", "测试商品");
		doc.addField("item_price", "100");
		doc.addField("id", "test001");
		try {
			// 第六步：把文档对象写入索引库。
			client.add(doc);
			// 第七步：提交。
			client.commit();
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		} finally {
			if (client != null)
				try {
					client.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	//@Test
	public void importAllItems() {
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"classpath:spring/applicationContext-*.xml");

		SearchItemService searchItemService = applicationContext.getBean(SearchItemService.class);
		E3Result result = searchItemService.importAllItems();
		System.out.println(result.getMsg());

		applicationContext.close();
	}
}

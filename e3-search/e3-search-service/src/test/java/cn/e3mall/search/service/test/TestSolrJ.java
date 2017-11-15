package cn.e3mall.search.service.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestSolrJ {
	SolrClient client = null;

	//@Before
	public void getClient() {
		client = new HttpSolrClient("http://192.168.56.120:8983/solr/core1/");
	}

	//@After
	public void closeClient() {
		try {
			if (client != null)
				client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//@Test
	public void testAddDoc() throws SolrServerException, IOException {
		List<SolrInputDocument> list = new ArrayList<>();
		Random rand = new Random(System.nanoTime());
		for (int i = 0; i < 10; i++) {
			SolrInputDocument doc = new SolrInputDocument();
			doc.addField("id", "doc" + (i + 1));
			doc.addField("item_title", "测试商品" + (i + 1));
			doc.addField("item_price", rand.nextInt(1000));
			list.add(doc);
		}
		client.add(list);
		client.commit();
	}

	//@Test
	public void testDelete() throws SolrServerException, IOException {
		testAddDoc();
		client.deleteById("1");
		client.commit();
	}

	//@Test
	public void testDeleteByQuery() throws SolrServerException, IOException {
		testAddDoc();
		client.deleteByQuery("item_price:{200 TO *]");
		client.commit();
	}

	//@Test
	public void testDeleteAll() throws SolrServerException, IOException {
		client.deleteByQuery("*:*");
		client.commit();
	}
}

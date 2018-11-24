package com;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-solr.xml"})
public class TestSolr {

    @Resource
    private SolrTemplate solrTemplate;

    //	添加或者更新索引
    @Test
    public void testSaveOrUpdate(){
        Item item=new Item();
        item.setBrand("华为5555");
        item.setCategory("手机");
        item.setGoodsId(1L);
        item.setId(2L);
        item.setImage("http://ip:port/image/xxx.jpg");
        item.setPrice(new BigDecimal("99998"));
        item.setSeller("阿奴比的小店");
        item.setTitle("小米8SE 32G内存 4G全网通");
        item.setUpdateTime(new Date());

        // 设置自动提交：commitWithinMs：设置提交
        solrTemplate.saveBean(item,500);
        //solrTemplate.commit();
    }

    //	根据主键查询
    @Test
    public void testFindById(){
        Item item = solrTemplate.getById(1, Item.class);
        System.out.println(item);
    }

    //根据条件查询
    @Test
    public void testFindByQuery(){
        SimpleQuery query=new SimpleQuery();
        Criteria criteria=new Criteria();
        criteria=criteria.or("item_title").contains("米");
        query.addCriteria(criteria);
        ScoredPage<Item> items = solrTemplate.queryForPage(query, Item.class);
        System.out.println("总条数:"+items.getTotalElements());
        List<Item> itemList = items.getContent();
        for (Item item : itemList) {
            System.out.println(item);
        }

    }

    //	根据id删除
    @Test
    public void testDeleteById(){

        solrTemplate.deleteById("1");
        solrTemplate.commit();
    }

    //	先查后删
    @Test
    public void testDeleteByQuery(){
        //删除所有
        SimpleQuery query=new SimpleQuery("*:*");
        solrTemplate.delete(query);
        solrTemplate.commit();


    }


}

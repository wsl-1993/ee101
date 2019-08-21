package cn.itcast;

import com.google.gson.Gson;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * @创建人 wsl
 * @创建时间 2019/8/19 21:38
 * @描述
 */
public class RestManager {
    private RestHighLevelClient client;
    @Before
    public void init(){
        client=new RestHighLevelClient(RestClient.builder(
                new HttpHost("localhost",9201,"http"),
                new HttpHost("localhost",9202,"http"),
                new HttpHost("localhost",9203,"http")));
    }
    Gson gson = new Gson();
    @Test
    public void addDocument() throws Exception {
        //创建一个item对象
    Item item = new Item(1L,"小米6X手机","手机","小米",1199.0,"weehtgc");
    //创建用来创建文档的请求对象
        IndexRequest request = new IndexRequest("leyou", "item", item.getId().toString());
        //使用gson把对象转成json字符串
        String jsonString = gson.toJson(item);
        request.source(jsonString,XContentType.JSON);
        //使用客户端创建
        client.index(request,RequestOptions.DEFAULT);
    }
    @Test
    public void delDocument() throws Exception {
        //        创建用来删除文档的请求对象
        DeleteRequest request = new DeleteRequest("leyou", "item", "1");
        //        使用客户端创建
        client.delete(request,RequestOptions.DEFAULT);

    }
   // 批量新增文档
    @Test
    public void addDocumentBulk() throws Exception {
        // 准备文档数据：
        List<Item> list = new ArrayList<>();
        list.add(new Item(1L, "小米手机7", "手机", "小米", 3299.00,"http://image.leyou.com/13123.jpg"));
        list.add(new Item(2L, "坚果手机R1", "手机", "锤子", 3699.00,"http://image.leyou.com/13123.jpg"));
        list.add(new Item(3L, "华为META10", "手机", "华为", 4499.00,"http://image.leyou.com/13123.jpg"));
        list.add(new Item(4L, "小米Mix2S", "手机", "小米", 4299.00,"http://image.leyou.com/13123.jpg"));
        list.add(new Item(5L, "荣耀V10", "手机", "华为", 2799.00, "http://image.leyou.com/13123.jpg"));
//        创建用来批量创建文档的请求对象
        BulkRequest bulkRequest = new BulkRequest();
        for (Item item : list) {
            bulkRequest.add(new IndexRequest("leyou","item",item.getId().toString())
                    .source(gson.toJson(item),XContentType.JSON));
        }
        //        使用客户端创建
        client.bulk(bulkRequest, RequestOptions.DEFAULT);


    }
    @After
    public void end() throws Exception {
        client.close();
    }

}

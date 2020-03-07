package com.zqj.boot.es;

import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.health.ClusterHealthStatus;
import org.elasticsearch.cluster.health.ClusterIndexHealth;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.filter.Filter;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;
import org.elasticsearch.search.aggregations.metrics.cardinality.Cardinality;
import org.elasticsearch.search.aggregations.metrics.max.Max;
import org.elasticsearch.search.aggregations.metrics.min.Min;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * ElasticSearch基本的用法
 *
 * @author zqj
 * @create 2020-03-07 23:51
 */
public class ElasticSearchDemo {

    //从es中查询数据
    @Test
    public void test1() throws UnknownHostException {
        //1、指定es集群  cluster.name 是固定的key值，my-application是ES集群的名称
        Settings settings = Settings.builder().put("cluster.name", "my-application").build();
        //2.创建访问ES服务器的客户端
        TransportClient client = new PreBuiltTransportClient(settings)
                //获取es主机中节点的ip地址及端口号(以下是单个节点案例)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.1.94"), 9300));
        //实现数据查询(指定_id查询) 参数分别是 索引名，类型名  id
        GetResponse response = client.prepareGet("lib3", "user", "1").execute().actionGet();
        //得到查询出的数据
        System.out.println(response.getSourceAsString());//打印出json数据
        client.close();//关闭客户端

    }

    //插入数据
    @Test
    public void test2() throws IOException {
        //1、指定es集群  cluster.name 是固定的key值，my-application是ES集群的名称
        Settings settings = Settings.builder().put("cluster.name", "my-application").build();
        //2.创建访问ES服务器的客户端
        TransportClient client = new PreBuiltTransportClient(settings)
                //获取es主机中节点的ip地址及端口号(以下是单个节点案例)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.1.94"), 9300));
        //将数据转换成文档的格式（后期可以使用java对象，将数据转换成json对象就可以了）
        XContentBuilder doContentBuilder = XContentFactory.jsonBuilder()
                .startObject()
                .field("id", "1") //字段名 ： 值
                .field("title", "java设计模式之装饰模式")
                .field("content", "在不必改变原类文件和使用继承的情况下，动态地扩展一个对象的功能")
                .field("postdate", "2016-06-06")
                .field("url", "https://www.baidu.com")
                .endObject();
        //添加文档  index1:索引名 blog:类型 10:id
//.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE) 代表插入成功后立即刷新，因为ES中插入数据默认分片要1秒钟后再刷新


        IndexResponse response = client.prepareIndex("index1", "blog", "10")
                .setSource(doContentBuilder).setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE).get();
        System.out.println(response.status());
        //打印出CREATED 表示添加成功
    }

    //删除文档
    @Test
    public void test3() throws UnknownHostException {
        Settings settings = Settings.builder().put("cluster.name", "my-application").build();
        //2.创建访问ES服务器的客户端
        TransportClient client = new PreBuiltTransportClient(settings)
                //获取es主机中节点的ip地址及端口号(以下是单个节点案例)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.1.94"), 9300));
        DeleteResponse response = client.prepareDelete("index1", "blog", "10").get();
        System.out.println(response.status());
        //控制台打印出OK代表删除成功
    }

    //修改数据（指定字段进行修改)
    @Test
    public void test4() throws IOException, InterruptedException, ExecutionException {
        Settings settings = Settings.builder().put("cluster.name", "my-application").build();
        //2.创建访问ES服务器的客户端
        TransportClient client = new PreBuiltTransportClient(settings)
                //获取es主机中节点的ip地址及端口号(以下是单个节点案例)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.1.94"), 9300));
        UpdateRequest request = new UpdateRequest();
        request.index("index1") //索引名
                .type("blog") //类型
                .id("10")//id
                .doc(
                        XContentFactory.jsonBuilder()
                                .startObject()
                                .field("title", "单例设计模式")//要修改的字段 及字段值
                                .endObject()
                );
        UpdateResponse response = client.update(request).get();
        System.out.println(response.status());
        //控制台出现OK 代表更新成功
    }

    //upsert 修改用法：修改文章存在，执行修改，不存在则执行插入
    @Test
    public void test5() throws IOException, InterruptedException, ExecutionException {
        Settings settings = Settings.builder().put("cluster.name", "my-application").build();
        //2.创建访问ES服务器的客户端
        TransportClient client = new PreBuiltTransportClient(settings)
                //获取es主机中节点的ip地址及端口号(以下是单个节点案例)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.1.94"), 9300));
        IndexRequest request1 = new IndexRequest("index1", "blog", "8").source(
                XContentFactory.jsonBuilder()
                        .startObject()
                        .field("id", "2") //字段名 ： 值
                        .field("title", "工厂模式")
                        .field("content", "静态工厂，实例工厂")
                        .field("postdate", "2018-05-20")
                        .field("url", "https://www.baidu.com")
                        .endObject()
        );
        UpdateRequest request2 = new UpdateRequest("index1", "blog", "8").doc(
                XContentFactory.jsonBuilder().startObject()
                        .field("title", "设计模式")
                        .endObject()
        ).upsert(request1);
        UpdateResponse response = client.update(request2).get();
        System.out.println(response.status());
    }

    //bulk批量操作(批量添加)
    @Test
    public void test7() throws IOException {
//1、指定es集群  cluster.name 是固定的key值，my-application是ES集群的名称
        Settings settings = Settings.builder().put("cluster.name", "my-application").build();
        //2.创建访问ES服务器的客户端
        TransportClient client = new PreBuiltTransportClient(settings)
                //获取es主机中节点的ip地址及端口号(以下是单个节点案例)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.1.94"), 9300));

        BulkRequestBuilder builder = client.prepareBulk();
        IndexRequestBuilder request = client.prepareIndex("lib2", "books", "8").setSource(
                XContentFactory.jsonBuilder()
                        .startObject()
                        .field("title", "python")
                        .field("price", 99)
                        .endObject()
        );
        IndexRequestBuilder request2 = client.prepareIndex("lib2", "books", "9").setSource(
                XContentFactory.jsonBuilder()
                        .startObject()
                        .field("title", "VR")
                        .field("price", 29)
                        .endObject()
        );
        builder.add(request);
        builder.add(request2);
//该方法ES默认是分片1秒钟后刷新，即插入成功后马上查询，插入的数据不能马上被查出
        BulkResponse response = builder.get();
        System.out.println(response.status());
        if (response.hasFailures()) {
            System.out.println("操作失败");
        }
    }

    //查询删除：将查询到的数据进行删除
    @Test
    public void test8() throws UnknownHostException {
        //1、指定es集群  cluster.name 是固定的key值，my-application是ES集群的名称
        Settings settings = Settings.builder().put("cluster.name", "my-application").build();
        //2.创建访问ES服务器的客户端
        TransportClient client = new PreBuiltTransportClient(settings)
                //获取es主机中节点的ip地址及端口号(以下是单个节点案例)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.1.94"), 9300));
        BulkByScrollResponse response = DeleteByQueryAction.INSTANCE
                .newRequestBuilder(client)
                .filter(QueryBuilders.matchQuery("title", "工厂"))
                .source("index1")
                .get();
        //删除并返回删除的个数
        long counts = response.getDeleted();
        System.out.println(counts);
    }

    //match_all查询所有
    @Test
    public void test9() throws UnknownHostException {
        //1、指定es集群  cluster.name 是固定的key值，my-application是ES集群的名称
        Settings settings = Settings.builder().put("cluster.name", "my-application").build();
        //2.创建访问ES服务器的客户端
        TransportClient client = new PreBuiltTransportClient(settings)
                //获取es主机中节点的ip地址及端口号(以下是单个节点案例)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.1.94"), 9300));
        QueryBuilder qBuilder = QueryBuilders.matchAllQuery();
        SearchResponse sResponse = client.prepareSearch("lib3")
                .setQuery(qBuilder)
                .get();
        SearchHits hits = sResponse.getHits();
        for (SearchHit hit : hits) {
            System.out.println(hit.getSourceAsString());
            //将获取的值转换成map的形式
            Map<String, Object> map = hit.getSourceAsMap();
            for (String key : map.keySet()) {
                System.out.println(key + " key对应的值为：" + map.get(key));
            }
        }
    }

    //条件查询match query
    @Test
    public void test10() throws UnknownHostException {
        //1、指定es集群  cluster.name 是固定的key值，my-application是ES集群的名称
        Settings settings = Settings.builder().put("cluster.name", "my-application").build();
        //2.创建访问ES服务器的客户端
        TransportClient client = new PreBuiltTransportClient(settings)
                //获取es主机中节点的ip地址及端口号(以下是单个节点案例)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.1.94"), 9300));
        QueryBuilder builder = QueryBuilders.matchQuery("interests", "changge");
        SearchResponse response = client.prepareSearch("lib3").setQuery(builder).get();
        SearchHits hits = response.getHits();
        for (SearchHit hit : hits) {
            System.out.println(hit.getSourceAsString());
            //将获取的值转换成map的形式
            Map<String, Object> map = hit.getSourceAsMap();
            for (String key : map.keySet()) {
                System.out.println(key + " key对应的值为：" + map.get(key));
            }
        }
    }

    //multiMatchQuery 查询的值在多个字段中进行匹配
    @Test
    public void test11() throws UnknownHostException {
        //1、指定es集群  cluster.name 是固定的key值，my-application是ES集群的名称
        Settings settings = Settings.builder().put("cluster.name", "my-application").build();
        //2.创建访问ES服务器的客户端
        TransportClient client = new PreBuiltTransportClient(settings)
                //获取es主机中节点的ip地址及端口号(以下是单个节点案例)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.1.94"), 9300));
        //第一个参数是查询的值，后面的参数是字段名，可以跟多个字段，用逗号隔开
        QueryBuilder builder = QueryBuilders.multiMatchQuery("changge", "address", "interests");
        SearchResponse response = client.prepareSearch("lib3").setQuery(builder).get();
        SearchHits hits = response.getHits();
        for (SearchHit hit : hits) {
            System.out.println(hit.getSourceAsString());
            //将获取的值转换成map的形式
            Map<String, Object> map = hit.getSourceAsMap();
            for (String key : map.keySet()) {
                System.out.println(key + " key对应的值为：" + map.get(key));
            }
        }
    }

    //term查询
    @Test
    public void test12() throws UnknownHostException {
        //1、指定es集群  cluster.name 是固定的key值，my-application是ES集群的名称
        Settings settings = Settings.builder().put("cluster.name", "my-application").build();
        //2.创建访问ES服务器的客户端
        TransportClient client = new PreBuiltTransportClient(settings)
                //获取es主机中节点的ip地址及端口号(以下是单个节点案例)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.1.94"), 9300));
        //term查询是不进行分词的
        QueryBuilder builder = QueryBuilders.termQuery("interests", "changge");
        SearchResponse response = client.prepareSearch("lib3").setQuery(builder).get();
        SearchHits hits = response.getHits();
        for (SearchHit hit : hits) {
            System.out.println(hit.getSourceAsString());
            //将获取的值转换成map的形式
            Map<String, Object> map = hit.getSourceAsMap();
            for (String key : map.keySet()) {
                System.out.println(key + " key对应的值为：" + map.get(key));
            }
        }
    }

    //terms查询：与term区别在于可以在同个字段中同时匹配多个条件，但是不支持分词
    public void test13() throws UnknownHostException {
        //1、指定es集群  cluster.name 是固定的key值，my-application是ES集群的名称
        Settings settings = Settings.builder().put("cluster.name", "my-application").build();
        //2.创建访问ES服务器的客户端
        TransportClient client = new PreBuiltTransportClient(settings)
                //获取es主机中节点的ip地址及端口号(以下是单个节点案例)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.1.94"), 9300));
        //terms查询是不进行分词的 与term的区别在terms可以指定一个字段匹配多个查询内容
        QueryBuilder builder = QueryBuilders.termsQuery("interests", "changge", "旅游");
        SearchResponse response = client.prepareSearch("lib3").setQuery(builder).get();
        SearchHits hits = response.getHits();
        for (SearchHit hit : hits) {
            System.out.println(hit.getSourceAsString());
            //将获取的值转换成map的形式
            Map<String, Object> map = hit.getSourceAsMap();
            for (String key : map.keySet()) {
                System.out.println(key + " key对应的值为：" + map.get(key));
            }
        }
    }

    //reange 范围查询（日期在多少之间等)
    @Test
    public void test14() throws UnknownHostException {
        //1、指定es集群  cluster.name 是固定的key值，my-application是ES集群的名称
        Settings settings = Settings.builder().put("cluster.name", "my-application").build();
        //2.创建访问ES服务器的客户端
        TransportClient client = new PreBuiltTransportClient(settings)
                //获取es主机中节点的ip地址及端口号(以下是单个节点案例)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.1.94"), 9300));
        //rangeQurey 第一个参数为字段名，后面是范围 在设置日期格式
        QueryBuilder builder = QueryBuilders.rangeQuery("birthday").from("1990-01-01").to("2000-10-10").format("yyyy-MM-dd");
        SearchResponse response = client.prepareSearch("lib3").setQuery(builder).get();
        SearchHits hits = response.getHits();
        for (SearchHit hit : hits) {
            System.out.println(hit.getSourceAsString());
            //将获取的值转换成map的形式
            Map<String, Object> map = hit.getSourceAsMap();
            for (String key : map.keySet()) {
                System.out.println(key + " key对应的值为：" + map.get(key));
            }
        }
    }

    //prefix前缀查询
    @Test
    public void test15() throws UnknownHostException {
        //1、指定es集群  cluster.name 是固定的key值，my-application是ES集群的名称
        Settings settings = Settings.builder().put("cluster.name", "my-application").build();
        //2.创建访问ES服务器的客户端
        TransportClient client = new PreBuiltTransportClient(settings)
                //获取es主机中节点的ip地址及端口号(以下是单个节点案例)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.1.94"), 9300));
        //prefixQuery 第一个参数为字段名，后面是以zhao开头的条件进行查询
        QueryBuilder builder = QueryBuilders.prefixQuery("name", "zhao");
        SearchResponse response = client.prepareSearch("lib3").setQuery(builder).get();
        SearchHits hits = response.getHits();
        for (SearchHit hit : hits) {
            System.out.println(hit.getSourceAsString());
            //将获取的值转换成map的形式
            Map<String, Object> map = hit.getSourceAsMap();
            for (String key : map.keySet()) {
                System.out.println(key + " key对应的值为：" + map.get(key));
            }
        }
    }

    //wildcard模糊查询
    @Test
    public void test16() throws UnknownHostException {
        //1、指定es集群  cluster.name 是固定的key值，my-application是ES集群的名称
        Settings settings = Settings.builder().put("cluster.name", "my-application").build();
        //2.创建访问ES服务器的客户端
        TransportClient client = new PreBuiltTransportClient(settings)
                //获取es主机中节点的ip地址及端口号(以下是单个节点案例)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.1.94"), 9300));
        //wildcardQuery模糊查询
        QueryBuilder builder = QueryBuilders.wildcardQuery("name", "zhao*");
        SearchResponse response = client.prepareSearch("lib3").setQuery(builder).get();
        SearchHits hits = response.getHits();
        for (SearchHit hit : hits) {
            System.out.println(hit.getSourceAsString());
            //将获取的值转换成map的形式
            Map<String, Object> map = hit.getSourceAsMap();
            for (String key : map.keySet()) {
                System.out.println(key + " key对应的值为：" + map.get(key));
            }
        }
    }

    //fuzzy模糊查询(输入的值输入个大概也可以查询出来）
    @Test
    public void test17() throws UnknownHostException {
        //1、指定es集群  cluster.name 是固定的key值，my-application是ES集群的名称
        Settings settings = Settings.builder().put("cluster.name", "my-application").build();
        //2.创建访问ES服务器的客户端
        TransportClient client = new PreBuiltTransportClient(settings)
                //获取es主机中节点的ip地址及端口号(以下是单个节点案例)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.1.94"), 9300));
        //fuzzyQuery模糊查询
        QueryBuilder builder = QueryBuilders.fuzzyQuery("name", "chagge");
        SearchResponse response = client.prepareSearch("lib3").setQuery(builder).get();
        SearchHits hits = response.getHits();
        for (SearchHit hit : hits) {
            System.out.println(hit.getSourceAsString());
            //将获取的值转换成map的形式
            Map<String, Object> map = hit.getSourceAsMap();
            for (String key : map.keySet()) {
                System.out.println(key + " key对应的值为：" + map.get(key));
            }
        }
    }

    //typeQuery类型查询
    @Test
    public void test18() throws UnknownHostException {
        //1、指定es集群  cluster.name 是固定的key值，my-application是ES集群的名称
        Settings settings = Settings.builder().put("cluster.name", "my-application").build();
        //2.创建访问ES服务器的客户端
        TransportClient client = new PreBuiltTransportClient(settings)
                //获取es主机中节点的ip地址及端口号(以下是单个节点案例)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.1.94"), 9300));
        //typeQuery类型查询
        QueryBuilder builder = QueryBuilders.typeQuery("blog");
        SearchResponse response = client.prepareSearch("index1").setQuery(builder).get();
        SearchHits hits = response.getHits();
        for (SearchHit hit : hits) {
            System.out.println(hit.getSourceAsString());
            //将获取的值转换成map的形式
            Map<String, Object> map = hit.getSourceAsMap();
            for (String key : map.keySet()) {
                System.out.println(key + " key对应的值为：" + map.get(key));
            }
        }
    }

    //idsQuery id查询（可以同时根据多个id进行查询）
    @Test
    public void test19() throws UnknownHostException {
        //1、指定es集群  cluster.name 是固定的key值，my-application是ES集群的名称
        Settings settings = Settings.builder().put("cluster.name", "my-application").build();
        //2.创建访问ES服务器的客户端
        TransportClient client = new PreBuiltTransportClient(settings)
                //获取es主机中节点的ip地址及端口号(以下是单个节点案例)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.1.94"), 9300));
        //idsQuery id查询
        QueryBuilder builder = QueryBuilders.idsQuery("1", "2");
        SearchResponse response = client.prepareSearch("lib3").setQuery(builder).get();
        SearchHits hits = response.getHits();
        for (SearchHit hit : hits) {
            System.out.println(hit.getSourceAsString());
            //将获取的值转换成map的形式
            Map<String, Object> map = hit.getSourceAsMap();
            for (String key : map.keySet()) {
                System.out.println(key + " key对应的值为：" + map.get(key));
            }
        }
    }

    //max 求最大值
    @Test
    public void test30() throws UnknownHostException {
        //1、指定es集群  cluster.name 是固定的key值，my-application是ES集群的名称
        Settings settings = Settings.builder().put("cluster.name", "my-application").build();
        //2.创建访问ES服务器的客户端
        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.1.94"), 9300));
        //aggMax为最大值的别名 ，age是要求最大值的列
        AggregationBuilder builder = AggregationBuilders.max("aggMax").field("age");
        SearchResponse response = client.prepareSearch("lib3").addAggregation(builder).get();
        Max max = response.getAggregations().get("aggMax");
        //打印最大值
        System.out.println(max.getValue());
    }

    //min 求最小值
    @Test
    public void test31() throws UnknownHostException {
        //1、指定es集群  cluster.name 是固定的key值，my-application是ES集群的名称
        Settings settings = Settings.builder().put("cluster.name", "my-application").build();
        //2.创建访问ES服务器的客户端
        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.1.94"), 9300));
        //aggMin为最小值的别名 ，age是要求最小值的列
        AggregationBuilder builder = AggregationBuilders.min("aggMin").field("age");
        SearchResponse response = client.prepareSearch("lib3").addAggregation(builder).get();
        Min min = response.getAggregations().get("aggMin");
        //打印最小值
        System.out.println(min.getValue());
    }

    //avg 求平均值
    @Test
    public void test32() throws UnknownHostException {
        //1、指定es集群  cluster.name 是固定的key值，my-application是ES集群的名称
        Settings settings = Settings.builder().put("cluster.name", "my-application").build();
        //2.创建访问ES服务器的客户端
        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.1.94"), 9300));
        //aggavg为平均值的别名 ，age是要求最大值的列
        AggregationBuilder builder = AggregationBuilders.avg("aggavg").field("age");
        SearchResponse response = client.prepareSearch("lib3").addAggregation(builder).get();
        Avg avg = response.getAggregations().get("aggavg");
        //打印平均值
        System.out.println(avg.getValue());
    }

    //sum 求和
    @Test
    public void test33() throws UnknownHostException {
        //1、指定es集群  cluster.name 是固定的key值，my-application是ES集群的名称
        Settings settings = Settings.builder().put("cluster.name", "my-application").build();
        //2.创建访问ES服务器的客户端
        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.1.94"), 9300));
        //aggSum为总和的别名 ，age是要求总和的列
        AggregationBuilder builder = AggregationBuilders.max("aggSum").field("age");
        SearchResponse response = client.prepareSearch("lib3").addAggregation(builder).get();
        Sum sum = response.getAggregations().get("aggSum");
        //打印总和
        System.out.println(sum.getValue());
    }

    //cardinality基数（互不相同的个数）
    @Test
    public void test34() throws UnknownHostException {
        //1、指定es集群  cluster.name 是固定的key值，my-application是ES集群的名称
        Settings settings = Settings.builder().put("cluster.name", "my-application").build();
        //2.创建访问ES服务器的客户端
        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.1.94"), 9300));
        //Cardinality基数查询： 查询age字段中互不相同的个数
        AggregationBuilder builder = AggregationBuilders.cardinality("aggCardinality").field("age");
        SearchResponse response = client.prepareSearch("lib3").addAggregation(builder).get();
        Cardinality cardinality = response.getAggregations().get("aggCardinality");
        //打印基数
        System.out.println(cardinality.getValue());
    }

    //commonTermsQuery
    @Test
    public void test35() throws UnknownHostException {
        //1、指定es集群  cluster.name 是固定的key值，my-application是ES集群的名称
        Settings settings = Settings.builder().put("cluster.name", "my-application").build();
        //2.创建访问ES服务器的客户端
        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.1.94"), 9300));
        QueryBuilder builder = QueryBuilders.commonTermsQuery("name", "zhaoliu");
        SearchResponse response = client.prepareSearch("lib3").setQuery(builder).get();
        SearchHits hits = response.getHits();
        for (SearchHit hit : hits) {
            System.out.println(hit.getSourceAsString());
            //将获取的值转换成map的形式
            Map<String, Object> map = hit.getSourceAsMap();
            for (String key : map.keySet()) {
                System.out.println(key + " key对应的值为：" + map.get(key));
            }
        }
    }

    //queryStringQuery  根据值去每个字段进行模糊查询 +代表必须含有  -代表不能含有
    @Test
    public void test36() throws UnknownHostException {
        //1、指定es集群  cluster.name 是固定的key值，my-application是ES集群的名称
        Settings settings = Settings.builder().put("cluster.name", "my-application").build();
        //2.创建访问ES服务器的客户端
        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.1.94"), 9300));
        //查询文档中含必须含有changge，不含有hejiu的文档（会每个字段去查询）+代表必须含有  -代表不能含有
        QueryBuilder builder = QueryBuilders.queryStringQuery("+changge  -hejiu");
        SearchResponse response = client.prepareSearch("lib3").setQuery(builder).get();
        SearchHits hits = response.getHits();
        for (SearchHit hit : hits) {
            System.out.println(hit.getSourceAsString());
            //将获取的值转换成map的形式
            Map<String, Object> map = hit.getSourceAsMap();
            for (String key : map.keySet()) {
                System.out.println(key + " key对应的值为：" + map.get(key));
            }
        }
    }

    //simpleQueryStringQuery  根据值去每个字段进行模糊查询 只要有一个符合就会返回该文章
    @Test
    public void test37() throws UnknownHostException {
        //1、指定es集群  cluster.name 是固定的key值，my-application是ES集群的名称
        Settings settings = Settings.builder().put("cluster.name", "my-application").build();
        //2.创建访问ES服务器的客户端
        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.1.94"), 9300));
        //只要所有字段中，含有changge或hejiui的都返回
        QueryBuilder builder = QueryBuilders.simpleQueryStringQuery("changge  hejiu");
        SearchResponse response = client.prepareSearch("lib3").setQuery(builder).get();
        SearchHits hits = response.getHits();
        for (SearchHit hit : hits) {
            System.out.println(hit.getSourceAsString());
            //将获取的值转换成map的形式
            Map<String, Object> map = hit.getSourceAsMap();
            for (String key : map.keySet()) {
                System.out.println(key + " key对应的值为：" + map.get(key));
            }
        }
    }

    //分组聚合
    @Test
    public void test40() throws UnknownHostException {
        //1、指定es集群  cluster.name 是固定的key值，my-application是ES集群的名称
        Settings settings = Settings.builder().put("cluster.name", "my-application").build();
        //2.创建访问ES服务器的客户端
        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.1.94"), 9300));
        //以年龄分组，组名为：terms
        AggregationBuilder builder = AggregationBuilders.terms("terms").field("age");
        SearchResponse response = client.prepareSearch("lib3").addAggregation(builder).execute().actionGet();
        Terms terms = response.getAggregations().get("terms");
        for (Terms.Bucket term : terms.getBuckets()) {
            System.out.println(term.getKey() + "  " + term.getDocCount());
        }
    }

    //filter聚合
    @Test
    public void test41() throws UnknownHostException {
        //1、指定es集群  cluster.name 是固定的key值，my-application是ES集群的名称
        Settings settings = Settings.builder().put("cluster.name", "my-application").build();
        //2.创建访问ES服务器的客户端
        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.1.94"), 9300));
        //以年龄分组，并查询年龄为20的有多少人
        QueryBuilder queryBuilder = QueryBuilders.termQuery("age", 20);
        AggregationBuilder builder = AggregationBuilders.filter("filter", queryBuilder);
        SearchResponse response = client.prepareSearch("lib3").addAggregation(builder).execute().actionGet();
        Filter filter = response.getAggregations().get("filter");
        System.out.println(filter.getDocCount());
    }

    //filters聚合:指定多个过滤条件
    @Test
    public void test42() throws UnknownHostException {
        //1、指定es集群  cluster.name 是固定的key值，my-application是ES集群的名称
        Settings settings = Settings.builder().put("cluster.name", "my-application").build();
        //2.创建访问ES服务器的客户端
        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.1.94"), 9300));
        //查询heijiu changge中各有多少个文档
        QueryBuilder queryBuilder = QueryBuilders.termQuery("interests", "hejiu");
        QueryBuilder queryBuilder2 = QueryBuilders.termQuery("interests", "changge");

        AggregationBuilder builder = AggregationBuilders.filters("filters", queryBuilder, queryBuilder2);
        SearchResponse response = client.prepareSearch("lib3").addAggregation(builder).execute().actionGet();
        Aggregation filter = response.getAggregations().get("filters");
        System.out.println(filter.toString());
    }

    //range聚合:范围分组过滤条件
    @Test
    public void test43() throws UnknownHostException {
        //1、指定es集群  cluster.name 是固定的key值，my-application是ES集群的名称
        Settings settings = Settings.builder().put("cluster.name", "my-application").build();
        //2.创建访问ES服务器的客户端
        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.1.94"), 9300));
        //统计年龄在50以下的，年龄在25至50之间，年龄在25以上的人数
        AggregationBuilder builder = AggregationBuilders
                .range("range")
                .field("age")
                .addUnboundedTo(50)
                .addRange(25, 50)
                .addUnboundedFrom(25);
        SearchResponse response = client.prepareSearch("lib3").addAggregation(builder).execute().actionGet();
        Aggregation filter = response.getAggregations().get("range");
        System.out.println(filter.toString());
    }

    //missing聚合:为空聚合统计
    @Test
    public void test44() throws UnknownHostException {
        //1、指定es集群  cluster.name 是固定的key值，my-application是ES集群的名称
        Settings settings = Settings.builder().put("cluster.name", "my-application").build();
        //2.创建访问ES服务器的客户端
        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.1.94"), 9300));
        //统计价格为空的个数
        AggregationBuilder builder = AggregationBuilders.missing("missing").field("price");
        SearchResponse response = client.prepareSearch("lib4").addAggregation(builder).execute().actionGet();
        Aggregation filter = response.getAggregations().get("missing");
        System.out.println(filter.toString());
    }

    @Test
    public void test45() throws UnknownHostException {
        //1、指定es集群  cluster.name 是固定的key值，my-application是ES集群的名称
        Settings settings = Settings.builder().put("cluster.name", "my-application").build();
        //2.创建访问ES服务器的客户端
        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.1.94"), 9300));
        //获取集群信息
        ClusterHealthResponse healthResponse = client.admin().cluster().prepareHealth().get();
        //获取集群名称
        String clusterName = healthResponse.getClusterName();
        System.out.println(clusterName);
        //获取存放数据的那些节点
        int numberOfDataNodes = healthResponse.getNumberOfDataNodes();
        System.out.println(numberOfDataNodes);
        //获取节点的总数量
        int numberOfNodes = healthResponse.getNumberOfNodes();
        System.out.println(numberOfNodes);
        //获取集群中一共有多少索引
        for (ClusterIndexHealth health : healthResponse.getIndices().values()) {
            String index = health.getIndex();//当前索引名称
            int numberOfShards = health.getNumberOfShards();//主分片
            int numberOfReplicas = health.getNumberOfReplicas();//副本
            ClusterHealthStatus status = health.getStatus();//得到当前的健康状况
            System.out.println(status);//健康-绿色  一般-黄色  不健康-红色
        }

    }


}

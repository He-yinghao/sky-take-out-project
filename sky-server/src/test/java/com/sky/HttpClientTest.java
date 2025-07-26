package com.sky;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class HttpClientTest {
    @Test
    public void testGet() throws IOException {
//        1.创建httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();
//        2.创建请求对象，设置请求的url
        HttpGet httpGet = new HttpGet("http://localhost:8080/admin/shop/status");
        // 添加Header参数：token
        httpGet.setHeader("token", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJjbGFpbXMiOnsiZW1wSWQiOjF9LCJleHAiOjE3NDE2MTU3MjV9.jaREuGwfC8znR8vxAsh6WJopgTDr-hH_gGpkHL-UjyM"); // 将"your_token_value_here"替换为实际的token值
//        3.发送请求，得到响应对象
        CloseableHttpResponse response = httpclient.execute(httpGet);
        System.out.println("响应状态码：" + response.getStatusLine().getStatusCode());
        HttpEntity entity = response.getEntity();
        String body = EntityUtils.toString(entity); // 将实体内容读取为字节数组
        System.out.println("响应体：" + body);
//        4.关闭资源
        response.close();
        httpclient.close();
    }
    @Test
    public void testPost() throws IOException {
//        1.创建httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();
//        2.创建请求对象，设置请求的url
        HttpPost httpPost = new HttpPost("http://localhost:8080/admin/employee/login");
        httpPost.setEntity(new StringEntity("{\"username\":\"admin\",\"password\":\"123456\"}"));
//        指定请求头
        httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
//        3. 发送请求，得到响应对象
        CloseableHttpResponse response = httpclient.execute(httpPost);
//        获取响应码
        System.out.println("响应状态码：" + response.getStatusLine().getStatusCode());
        HttpEntity entity = response.getEntity();
        String body = EntityUtils.toString(entity);
        System.out.println("响应体：" + body);
//        4.关闭资源
        response.close();
        httpclient.close();
    }

    @Test
    public void test(){
        String str = "ad";
        str.chars().forEach(c -> System.out.println(c));
    }
}

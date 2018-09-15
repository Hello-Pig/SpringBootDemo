package net.hellopig.app.utils;

import net.hellopig.app.utils.component.HttpRequestConfig;
import net.hellopig.app.utils.component.HttpRequestResult;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Description：HttpClientUtils测试类
 *
 * @author CC
 * @date 2018/8/5 13:54    HttpClientUtilsTest.java
 */
class HttpClientUtilsTest {

    @Test
    void get() throws IOException {
        HttpRequestConfig config = HttpRequestConfig.create().url("http://localhost:8080/pigs/id/2");
        HttpRequestResult result = HttpClientUtils.get(config);
        System.out.println(result.getResponseText());
    }

    @Test
    void post() throws IOException {
        HttpRequestConfig config = HttpRequestConfig.create().url("http://localhost:8080/pig")
                .addParam("id",5)
                .addParam("name","小猫")
                .addParam("phone","67857476")
                .addParam("email","67857476@qq.com")
                .addParam("password","xiaomao")
                .addParam("sex","母")
                .addParam("age",19)
                .addParam("address","猫窝");
        HttpRequestResult result = HttpClientUtils.post(config);
        System.out.println(result.getResponseText());
    }

    @Test
    void put() throws IOException {
        HttpRequestConfig config = HttpRequestConfig.create().url("http://localhost:8080/pig/id/4")
                .addParam("id",5)
                .addParam("name","小猫")
                .addParam("phone","67857476")
                .addParam("email","67857476@qq.com")
                .addParam("password","xiaomao")
                .addParam("sex","母")
                .addParam("age",19)
                .addParam("address","猫窝");
        HttpRequestResult result = HttpClientUtils.put(config);
        System.out.println(result.getResponseText());
    }

    @Test
    void header() {
    }

    @Test
    void patch() {
    }

    @Test
    void delete() throws IOException {
        HttpRequestConfig config = HttpRequestConfig.create().url("http://localhost:8080/pig/id/67bd14c4-2c73-44a4-ac76-07bcba5e7b0f");
        HttpRequestResult result = HttpClientUtils.delete(config);
        System.out.println(result.getResponseText());
    }

    @Test
    void trace() {
    }

    @Test
    void options() {
    }

    @Test
    void download() throws IOException {
        //百度logo
        String imgUrl = "https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo/logo_white_fe6da1ec.png";
        File file = new File("E:/baidu.png");
        HttpClientUtils.download(HttpRequestConfig.create().url(imgUrl).out(new FileOutputStream(file)));
        if (file.exists()) {
            System.out.println("图片下载成功了！存放在：" + file.getPath());
        }
    }

}
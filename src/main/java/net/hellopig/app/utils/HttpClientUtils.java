package net.hellopig.app.utils;

import net.hellopig.app.utils.component.HttpRequestConfig;
import net.hellopig.app.utils.component.HttpRequestMethod;
import net.hellopig.app.utils.component.HttpRequestResult;
import net.hellopig.app.utils.component.MyHttpClientBuilder;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Description：httpclient 工具类
 *
 * @author CC
 * @date 2018/9/15 15:48    HttpClientUtils.java
 */
public class HttpClientUtils {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);

    /**
     * 默认采用的http协议的HttpClient对象
     */
    private static HttpClient httpClient;

    /**
     * 默认采用的https协议的HttpClient对象
     */
    private static HttpClient httpsClient;

    private final static Object SYNC_LOCK = new Object();

    private static final int CONNECT_TIMEOUT = 10000;
    private static final int SOCKET_TIMEOUT = 10000;
    private static final int CONNECT_REQUEST_TIMEOUT = 10000;
    private static final int MAX_TOTAL = 100;
    private static final int MAX_PER_ROUTE = 100;
    private static final int RETRY_TIMES = 3;


    /**
     * 初始化 httpCient
     * @param httpRequestConfig http client 请求配置类
     */
    private static void initHttpClinet(HttpRequestConfig httpRequestConfig) {

        if (httpRequestConfig.getHttpClient() == null) {
            if (httpRequestConfig.isUseSSL()) {
                if (httpsClient == null) {
                    synchronized (SYNC_LOCK) {
                        if (httpsClient == null) {
                            httpsClient =MyHttpClientBuilder.custom()
                                    .ssl()
                                    .timeout(CONNECT_TIMEOUT,SOCKET_TIMEOUT,CONNECT_REQUEST_TIMEOUT)
                                    .pool(MAX_TOTAL,MAX_PER_ROUTE)
                                    .retry(RETRY_TIMES,false).build();
                        }
                    }
                }
                httpRequestConfig.client(httpsClient);
            } else {
                if (httpClient == null) {
                    synchronized (SYNC_LOCK) {
                        if (httpClient == null) {
                            httpClient = MyHttpClientBuilder.custom()
                                    .timeout(CONNECT_TIMEOUT,SOCKET_TIMEOUT,CONNECT_REQUEST_TIMEOUT)
                                    .pool(MAX_TOTAL,MAX_PER_ROUTE)
                                    .retry(RETRY_TIMES,false).build();
                        }
                    }
                }
                httpRequestConfig.client(httpClient);
            }
        }
    }

    /**
     *  GET 请求
     * @param httpRequestConfig 请求配置类
     * @return 请求结果
     * @throws IOException 读写异常
     */
    static HttpRequestResult get(HttpRequestConfig httpRequestConfig) throws IOException {
        return transformHttpRequestResult(execute(httpRequestConfig.requestMethod(HttpRequestMethod.GET)),httpRequestConfig);
    }

    /**
     * POST 请求
     * @param httpRequestConfig 请求配置类
     * @return HttpRequestResult
     * @throws IOException 读写异常
     */
    static HttpRequestResult post(HttpRequestConfig httpRequestConfig) throws IOException {
        return transformHttpRequestResult(execute(httpRequestConfig.requestMethod(HttpRequestMethod.POST)),httpRequestConfig);
    }

    /**
     * PUT 请求
     * @param httpRequestConfig 请求配置类
     * @return HttpRequestResult
     * @throws IOException 读写异常
     */
    static HttpRequestResult put(HttpRequestConfig httpRequestConfig) throws IOException {
        return transformHttpRequestResult(execute(httpRequestConfig.requestMethod(HttpRequestMethod.PUT)),httpRequestConfig);
    }

    /**
     * HEADER 请求
     * @param httpRequestConfig 请求配置类
     * @return HttpRequestResult
     * @throws IOException 读写异常
     */
    public static HttpRequestResult header(HttpRequestConfig httpRequestConfig) throws IOException {
        return transformHttpRequestResult(execute(httpRequestConfig.requestMethod(HttpRequestMethod.HEAD)),httpRequestConfig);
    }

    /**
     * PATCH 请求
     * @param httpRequestConfig 请求配置类
     * @return HttpRequestResult
     * @throws IOException 读写异常
     */
    public static HttpRequestResult patch(HttpRequestConfig httpRequestConfig) throws IOException {
        return transformHttpRequestResult(execute(httpRequestConfig.requestMethod(HttpRequestMethod.PATCH)),httpRequestConfig);
    }

    /**
     * DELETE 请求
     * @param httpRequestConfig 请求配置类
     * @return HttpRequestResult
     * @throws IOException 读写异常
     */
    static HttpRequestResult delete(HttpRequestConfig httpRequestConfig) throws IOException {
        return transformHttpRequestResult(execute(httpRequestConfig.requestMethod(HttpRequestMethod.DELETE)),httpRequestConfig);
    }


    /**
     * TRACE 请求
     * @param httpRequestConfig 请求配置类
     * @return HttpRequestResult
     * @throws IOException 读写异常
     */
    public static HttpRequestResult trace(HttpRequestConfig httpRequestConfig) throws IOException {
        return transformHttpRequestResult(execute(httpRequestConfig.requestMethod(HttpRequestMethod.TRACE)),httpRequestConfig);
    }

    /**
     * OPTIONS 请求
     * @param httpRequestConfig 请求配置类
     * @return HttpRequestResult
     * @throws IOException 读写异常
     */
    public static HttpRequestResult options(HttpRequestConfig httpRequestConfig) throws IOException {
        return transformHttpRequestResult(execute(httpRequestConfig.requestMethod(HttpRequestMethod.OPTIONS)),httpRequestConfig);
    }

    /**
     * 下载
     * @param httpRequestConfig 请求配置类
     * @throws IOException 读写异常
     */
    static void download(HttpRequestConfig httpRequestConfig) throws IOException {
        HttpResponse response = execute(httpRequestConfig.requestMethod(HttpRequestMethod.GET));
        response.getEntity().writeTo(HttpRequestConfig.getOut());
        close(response);
    }

    /**
     * 下载
     * @param httpRequestConfig 请求配置类
     * @return OutputStream
     * @throws IOException 读写异常
     */
    public static OutputStream downloadStream(HttpRequestConfig httpRequestConfig) throws IOException {
        HttpResponse response = execute(httpRequestConfig.requestMethod(HttpRequestMethod.GET));
        response.getEntity().writeTo(HttpRequestConfig.getOut());
        close(response);
        return HttpRequestConfig.getOut();
    }


    /**
     * 请求资源或服务
     *
     * @param config		请求参数配置
     * @return				返回HttpResponse对象
     */
    private static HttpResponse execute(HttpRequestConfig config) throws IOException {

        if (config.getRequestMethod() == null) {
            throw new NullPointerException("request method is null!");
        }

        //设置httpClient
        initHttpClinet(config);

        //创建请求对象
        HttpRequestBase request = getRequest(HttpRequestConfig.getUrl(), config.getRequestMethod());

        //设置header信息
        request.setHeaders(config.getHeaders());

        //判断是否支持设置entity(仅HttpPost、HttpPut、HttpPatch支持)
        if (HttpEntityEnclosingRequestBase.class.isAssignableFrom(request.getClass())) {
            //装填参数
            HttpEntity entity = config.getEntity();
            //设置参数到请求对象中
            ((HttpEntityEnclosingRequestBase) request).setEntity(entity);

            if (config.getParamMap().size() > 0) {
                logger.debug("请求参数：{}",config.getParamMap());
            }

            if (StringUtils.isNotBlank(config.getJsonParam())) {
                logger.debug("请求参数：{}",config.getJsonParam());
            }

        } else {
            int index = HttpRequestConfig.getUrl().indexOf("?");
            if ( index > 0) {
                logger.debug("请求参数：{}",HttpRequestConfig.getUrl().substring(index+1));
            }
        }
        //执行请求操作，并拿到结果（同步阻塞）
        if (config.getContext() == null) {
            return config.getHttpClient().execute(request);
        } else {
            return config.getHttpClient().execute(request, config.getContext());
        }

    }

    /**
     *
     * @param response 响应体
     * @param config 请求参数配置
     * @return HttpRequestResult
     * @throws IOException 读写异常
     */
    private static HttpRequestResult transformHttpRequestResult(HttpResponse response, HttpRequestConfig config) throws IOException{
        //获取结果实体
        HttpRequestResult result = new HttpRequestResult(response.getStatusLine().getStatusCode(), response.getAllHeaders());
        if (response.getEntity() != null) {
            //content 编码
            String charset = ( response.getEntity().getContentEncoding() == null ) ? config.getResponseCharset() : response.getEntity().getContentEncoding().getValue();
            result.setContentEncoding(charset);
            //content 类型
            String contentType = ( response.getEntity().getContentType() == null) ? "" : response.getEntity().getContentType().getValue();
            result.setContentType(contentType);
            result.setResponseText(EntityUtils.toString(response.getEntity(), config.getResponseCharset()));
        } else {//有可能是head请求
            result.setResponseText(response.getStatusLine().toString());
        }

        close(response);
        return result;
    }

    /**
     * 尝试关闭
     * @param response 响应体
     */
    private static void close(HttpResponse response) {
        try {
            EntityUtils.consume(response.getEntity());
            //如果CloseableHttpResponse 是resp的父类，则支持关闭
            if(CloseableHttpResponse.class.isAssignableFrom(response.getClass())){
                ((CloseableHttpResponse)response).close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据请求方法名，获取request对象
     *
     * @param url			资源地址
     * @param method		请求方式
     * @return				返回Http处理request基类
     */
    private static HttpRequestBase getRequest(String url, HttpRequestMethod method) {
        HttpRequestBase request;
        switch (method.getCode()) {
            case 0:
                request = new HttpGet(url);
                break;
            case 1:
                request = new HttpPost(url);
                break;
            case 2:
                request = new HttpHead(url);
                break;
            case 3:
                request = new HttpPut(url);
                break;
            case 4:
                request = new HttpDelete(url);
                break;
            case 5:
                request = new HttpTrace(url);
                break;
            case 6:
                request = new HttpPatch(url);
                break;
            case 7:
                request = new HttpOptions(url);
                break;
            default:
                request = new HttpPost(url);
                break;
        }
        return request;
    }
}

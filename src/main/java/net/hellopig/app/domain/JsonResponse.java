package net.hellopig.app.domain;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * 
 * @Title: JsonResponse.java
 * @Package com.hellopig.utils
 * @Description: 自定义响应数据结构  抽象出的最外层响应体
 *                  具体的返回数据信息在data的中
 * 				200：表示成功
 * 			    401：传入的参数格式不匹配
 * 				500：表示错误，错误信息在msg字段中
 * 				501：bean验证错误，不管多少个错误都以map形式返回
 * 				502：拦截器拦截到用户token出错
 * 				555：异常抛出信息
 *
 * @author cc
 * @date 2018年7月27日 下午8:33:36
 * @version V1.0
 */
public class JsonResponse {

    /**
     * 定义jackson对象
     */
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 响应业务状态
     */
    private Integer status;

    /**
     * 响应消息
     */
    private String msg;

    /**
     * 响应中的数据
     */
    private Object data;

    /**
     * 构造方法
     *
     * @param status 状态码
     * @param msg 错误信息
     * @param data 具体响应数据
     */
    private JsonResponse(Integer status, String msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 成功状态 构造方法
     *
     * @param data 具体响应数据
     */
    private JsonResponse(Object data) {
        this.status = 200;
        this.msg = "OK";
        this.data = data;
    }

    /**
     * 生成响应体（通用）
     *
     * @param status 状态码
     * @param msg 错误（成功）信息
     * @param data 具体返回的数据
     * @return json格式的响应体
     */
    private static JsonResponse generate(Integer status, String msg, Object data) {
        return new JsonResponse(status, msg, data);
    }

    /**
     * 构造成功 且 值为空的请求体
     *
     * @return data为空的请求体
     */
    public static JsonResponse ok() {
        return new JsonResponse(null);
    }

    /**
     * 构造成功 且 值不为空的请求体
     *
     * @param data 具体响应数据
     * @return data不为空的请求体
     */
    public static JsonResponse ok(Object data) {
        return new JsonResponse(data);
    }

    /**
     * 构造错误响应体（通用）
     *   注：不建议使用此类，尽量使用以下枚举好的错误构造方法来定义
     *
     * @param status 状态码
     * @param msg 错误信息
     * @return 错误响应体
     */
    public static JsonResponse error(Integer status, String msg) {
        return new JsonResponse(status, msg, null);
    }

    /**
     *
     *
     * @param msg 错误信息
     * @return
     */
    public static JsonResponse errorMsg(String msg) {
        return new JsonResponse(500, msg, null);
    }

    public static JsonResponse errorMap(Object data) {
        return new JsonResponse(501, "error", data);
    }

    public static JsonResponse errorTokenMsg(String msg) {
        return new JsonResponse(502, msg, null);
    }

    public static JsonResponse errorException(String msg) {
        return new JsonResponse(555, msg, null);
    }

    public Boolean isOK() {
        return this.status == 200;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }


    /**
     * 
     * @Description: 将json结果集转化为 JsonResponse 对象
     * 				需要转换的对象是一个类
     * @param jsonData json格式数据
     * @param clazz 对象
     * @return JsonResponse
     * 
     * @author cc
     * @date 2018年7月27日 下午8:33:36
     */
    public static JsonResponse formatToPojo(String jsonData, Class<?> clazz) {
        try {
            if (clazz == null) {
                return MAPPER.readValue(jsonData, JsonResponse.class);
            }
            JsonNode jsonNode = MAPPER.readTree(jsonData);
            JsonNode data = jsonNode.get("data");
            Object obj = null;
            if (data.isObject()) {
                obj = MAPPER.readValue(data.traverse(), clazz);
            } else if (data.isTextual()) {
                obj = MAPPER.readValue(data.asText(), clazz);
            }
            return generate(jsonNode.get("status").intValue(), jsonNode.get("msg").asText(), obj);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 
     * @Description: 没有object对象的转化
     * @param json json
     * @return JsonResponse
     * 
     * @author cc
     * @date 2018年7月27日 下午8:33:36
     */
    public static JsonResponse format(String json) {
        try {
            return MAPPER.readValue(json, JsonResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 
     * @Description: Object是集合转化
     * 				需要转换的对象是一个list
     * @param jsonData json格式数据
     * @param clazz 对象
     * @return JsonResponse
     * 
     * @author cc
     * @date 2018年7月27日 下午8:33:36
     */
    public static JsonResponse formatToList(String jsonData, Class<?> clazz) {
        try {
            JsonNode jsonNode = MAPPER.readTree(jsonData);
            JsonNode data = jsonNode.get("data");
            Object obj = null;
            if (data.isArray() && data.size() > 0) {
                obj = MAPPER.readValue(data.traverse(),
                        MAPPER.getTypeFactory().constructCollectionType(List.class, clazz));
            }
            return generate(jsonNode.get("status").intValue(), jsonNode.get("msg").asText(), obj);
        } catch (Exception e) {
            return null;
        }
    }


}

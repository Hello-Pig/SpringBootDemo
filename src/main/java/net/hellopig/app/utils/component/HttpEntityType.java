package net.hellopig.app.utils.component;

/**
 * Description：HttpEntity 类型
 *
 * @author CC
 * @date 2018/9/15 15:15    HttpEntityType.java
 */
public enum HttpEntityType {

    /**
     * StringEntity 类型
     */
    ENTITY_STRING(0, "StringEntity"),

    /**
     * FileEntity 类型
     */
    ENTITY_FILE(1, "FileEntity"),

    /**
     * ByteArrayEntity 类型
     */
    ENTITY_BYTES(2, "ByteArrayEntity"),

    /**
     * ENTITY_INPUT_STREAM 类型
     */
    ENTITY_INPUT_STREAM(3, "ENTITY_INPUT_STREAM"),

    /**
     * SerializableEntity 类型
     */
    ENTITY_SERIALIZABLE(4, "SerializableEntity"),

    /**
     * MultipartEntity 类型
     */
    ENTITY_MULTIPART(5, "MultipartEntity"),

    /**
     * UrlEncodedFormEntity 类型
     */
    ENTITY_FORM(6, "UrlEncodedFormEntity");

    private int code;
    private String name;

    HttpEntityType(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getCode() {
        return code;
    }
}

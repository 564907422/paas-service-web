package com.paas.web.constants;

public class ServiceConstants {
    public static final Byte CONNFIG_INNER_TYPE = 1;

    //缓存
    public static final Byte CACHE_TYPE = 1;
    //MQ
    public static final Byte MQ_TYPE = 2;


    //缓存
    public static final String CACHE_STR = "cache";
    //MQ
    public static final String MQ_STR = "mq";
    //MONGO
    public static final String MONGO_STR = "mongo";

    public static final String SPLIT_STR = "-";

    public static final String OPEN_ERROR_STR = "";

    //有效状态
    public static final String STATUS_VA = "1";


    public static final String ZK_PATH_PRE = "/bbtree/ts";
    public static final String ZK_PATH_SPLIT = "/";

    public static final Integer ZK_TIMEOUT = 5000;
    public static final Byte INNER_ZK = 1;
    public static final String CACHE_CLIENT_INFO = "{\"maxActive\":1024,\"maxIdle\":100,\"maxWait\":3000,"
            + "\"testOnBorrow\":\"false\",\"testOnReturn\":\"true\"}";

    public static final String MONGO_CLIENT_INFO = "{\"threshold\":1000}";

    public static final class INFO {
        public static final int code_success = 10000;
        public static final int code_fail = 20000;
        public static final String msg_success = "操作成功！";
        public static final String msg_fail = "操作失败！";
        public static final String msg_null = "数据为空！";
    }

    public static final String SESSION_KEY = "session_key_user";
}

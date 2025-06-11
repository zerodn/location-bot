package com.resdii.vars.bot.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProxyApiDTO {
    public Data data;
    public String code;
    public String message;
    public long timestamp;
    public String status;
    public String error;
    public String success;

    public class Data {
        public String id;
        public String realIpAddress;
        public String http;
        public String socks5;
        public String nextRequestAt;
        public int httpPort;
        public String socks5Port;
        public String host;
        public String packageTtc;
        public String location;
        public String region;
        public String expirationAt;
        public String effectiveAt;
        public String ttl;
        public String ttc;
        public String useInfinity;
    }
}

package com.balofun.bot.feign.impl;

import com.balofun.bot.dto.ProxyApiDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

/**
 * @author ANSI.
 */

//@FeignClient(value = "ProxyApiFeign", url = "https://api.vars.vn", configuration = DefaultFeignConfig.class)
//public interface ProxyApiFeign {
//
//    @RequestMapping(method = RequestMethod.GET, value = "/vhb/system/proxy?scope=crawl")
//    ProxyApiDTO getProxy();
//}
public class ProxyApiFeign {

    public static ProxyApiDTO getProxy() {
        try {
            Connection.Response response = Jsoup.connect("https://api.vars.vn/vhb/system/proxy?scope=crawl").ignoreContentType(true).execute();
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(response.body(), ProxyApiDTO.class);
        } catch (Exception ex) {
            return null;
        }
    }
}
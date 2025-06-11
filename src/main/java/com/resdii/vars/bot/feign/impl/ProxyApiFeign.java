package com.resdii.vars.bot.feign.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.resdii.vars.bot.dto.ProxyApiDTO;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

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
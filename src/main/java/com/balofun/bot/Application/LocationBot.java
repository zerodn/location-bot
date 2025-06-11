package com.balofun.bot.Application;

import com.balofun.bot.dto.ProxyApiDTO;
import com.balofun.bot.feign.impl.ProxyApiFeign;
import com.balofun.bot.util.CrawlUtil;
import com.balofun.bot.util.KillOldGeckodriver;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LocationBot {
    public static ProxyApiFeign proxyApi;
    public static List<String> hrefList = new ArrayList<>();
    public static String[] listURL = {"", "mua-ban-nha-dat", "cho-thue-nha-dat", ""
            , "mua-ban-nha-dat", "cho-thue-nha-dat", "", "mua-ban-nha-dat", "cho-thue-nha-dat"
            //, "tra-cuu-quy-hoach"
            , "tin-tuc", "cam-nang", "varsconnect.vn"};
    public static Random rand = new Random();
    public static int[] numbers = {0, 0, 0, 0, 0, 1, 0, 1, 1, 0, 1, 0, 0, 0};

    private static void setupFFDriver() {
        ProxyApiDTO response = proxyApi.getProxy();
        if (response != null && "success".equals(response.status)) {
            ProxyApiDTO.Data data = response.data;
            System.out.println(String.format("******************* Proxy IP %s", data.realIpAddress + ":" + data.httpPort));
            CrawlUtil.loadFirefoxDriver(data.realIpAddress, data.httpPort);
        } else {
            CrawlUtil.loadFirefoxDriver();
        }
    }

    public static void fetchTopLand() {

        try {
            setupFFDriver();

            String url = "https://varsland.vn/" + listURL[CrawlUtil.random.nextInt(listURL.length)];
            if (url.contains("varsconnect.vn")) {
                url = url.replace("varsland.vn/", "");
            }
            CrawlUtil.getWebDrive().get(url);
            CrawlUtil.getWebDriverWait().until(ExpectedConditions.jsReturnsValue("return document.readyState=='complete';"));

            CrawlUtil.scrollWindow(CrawlUtil.getWebDrive());

            List<WebElement> linksWithHref = CrawlUtil.getWebDrive().findElements(By.cssSelector("a[href]"));
            for (int i = 0; i < linksWithHref.size(); i++) {
                try {
                    WebElement link = linksWithHref.get(i);
                    String href = link.getAttribute("href");
                    if (href.length() > 100 && !hrefList.contains(href) && href.contains("https://varsland.vn/")) {
                        if (href.contains("?")) {
                            href = href + "&v=" + numbers[rand.nextInt(numbers.length)];
                        } else {
                            href = href + "?v=" + numbers[rand.nextInt(numbers.length)];
                        }
                        hrefList.add(href);
                    }
                } catch (StaleElementReferenceException e) {
                    linksWithHref = CrawlUtil.getWebDrive().findElements(By.cssSelector("a[href]"));
                    i--;
                }
            }

            for (int i = 0; i < hrefList.size(); i++) {
                System.out.println(hrefList.get(i));
            }
            System.out.println("********* Fetch done: " + url);
        } catch (Exception ex) {
            System.out.println("fetchTopLand Exception: " + ex.getMessage());
        }
    }

    public static void openWeb() {
        if (hrefList.size() > 0) {
            for (int i = 0; i < hrefList.size(); i++) {
                try {
                    if (((RemoteWebDriver) CrawlUtil.getWebDrive()).getSessionId() == null) {
                        System.out.println("Session hết hạn. Cần khởi động lại trình duyệt.");
                        setupFFDriver();
                    }
                    CrawlUtil.getWebDrive().get(hrefList.get(i));
                    CrawlUtil.getWebDriverWait().until(ExpectedConditions.jsReturnsValue("return document.readyState=='complete';"));

                    CrawlUtil.scrollWindow(CrawlUtil.getWebDrive());

                    CrawlUtil.sleep("Thread.sleep0", 10000);
                    System.out.println("Fetch done: " + hrefList.get(i));
                } catch (Exception e) {
                    setupFFDriver();
                    System.out.println("Fetch fail: " + hrefList.get(i));
                    CrawlUtil.sleep("Thread.sleep1", 5000);
                }
            }
            hrefList.clear();

            CrawlUtil.sleep("Thread.sleep2", 10000);
        }

        fetchTopLand();
    }

    public static void main(String[] args) {

        while (true) {
            try {
                KillOldGeckodriver.kill();

                System.setProperty("webdriver.gecko.driver", "C:\\VarsBot\\firefox_driver\\geckodriver.exe");
                openWeb();
            } catch (Exception ex) {
                System.out.println("Main fail: " + ex.getMessage());
            }
        }
    }
}
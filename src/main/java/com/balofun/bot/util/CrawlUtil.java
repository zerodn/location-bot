package com.balofun.bot.util;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class CrawlUtil {

    private static FirefoxOptions firefoxOptions;
    private static Proxy firefoxProxy;
    private static FirefoxProfile firefoxProfile;
    private static WebDriver firefoxDriver;
    private static WebDriverWait webDriverWait;
    public static Random random = new Random();

    public static WebDriver loadFirefoxDriver() {
        return loadFirefoxDriver(null, 0);
    }

    public static void sleep(String from, int milisecond) {
        try {
            Thread.sleep(CrawlUtil.random.nextInt(milisecond) + milisecond);
        } catch (Exception ex) {
            System.out.println("OpenWeb " + from + " fail: " + ex.getMessage());
        }
    }

    public static WebDriver loadFirefoxDriver(String proxyHost, int proxyPort) {

        if (firefoxDriver != null) {
            firefoxDriver.quit();
        }

        if (firefoxOptions == null) {
            firefoxOptions = new FirefoxOptions();
            firefoxOptions.addArguments("--incognito");
            firefoxOptions.addArguments("--disable-blink-features=AutomationControlled");
            firefoxOptions.addArguments("-private");
            firefoxOptions.setBinary("C:\\Program Files\\Mozilla Firefox\\firefox.exe");

            firefoxProfile = new FirefoxProfile();
            firefoxProfile.setPreference("permissions.default.image", 2);
            firefoxProfile.setPreference("gfx.downloadable_fonts.enabled", false);
            firefoxOptions.setProfile(firefoxProfile);
        }

        if (proxyHost != null) {
            firefoxProxy = new Proxy();
            firefoxProxy.setHttpProxy("" + proxyHost + ":" + proxyPort);
            firefoxProxy.setSslProxy("" + proxyHost + ":" + proxyPort);
            firefoxOptions.setProxy(firefoxProxy);
            firefoxOptions.setCapability("proxy", firefoxProxy);
        }

        firefoxOptions.addPreference("general.useragent.override", CrawlUtil.userAgents.get(random.nextInt(CrawlUtil.userAgents.size())));
        System.setProperty("webdriver.gecko.driver", "C:\\VarsBot\\firefox_driver\\geckodriver_v0.36.0.exe");
        firefoxDriver = new FirefoxDriver(firefoxOptions);
        firefoxDriver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
        firefoxDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        return firefoxDriver;
    }

    public static WebDriver getWebDrive() {
        if (firefoxDriver == null) {
            return loadFirefoxDriver();
        }
        return firefoxDriver;
    }

    public static WebDriverWait getWebDriverWait() {
        if (firefoxDriver == null) {
            return null;
        }
        if (webDriverWait == null) {
            webDriverWait = new WebDriverWait(firefoxDriver, 30);
        }
        return webDriverWait;
    }

    public static void scrollWindow(WebDriver webDriver) {
        try {
            boolean atBottom = false;
            Thread.sleep(CrawlUtil.random.nextInt(2000) + 2000);
            JavascriptExecutor js = (JavascriptExecutor) webDriver;

            while (!atBottom) {
                js.executeScript("window.scrollBy(0, 500);");
                Thread.sleep(CrawlUtil.random.nextInt(500) + 500);
                Long currentHeight = (Long) js.executeScript("return window.scrollY + window.innerHeight;");
                Long totalHeight = (Long) js.executeScript("return document.body.scrollHeight;");
                if (currentHeight >= totalHeight) {
                    atBottom = true;
                }
            }

            Thread.sleep(CrawlUtil.random.nextInt(10000) + 10000);
        } catch (Exception ex) {
            System.out.println("scrollWindow fail: " + ex.getMessage());
        }
    }

    public static boolean isGeckoDriverRunning() {
        try {
            // Chạy lệnh để kiểm tra tiến trình geckodriver
            Process process = Runtime.getRuntime().exec("tasklist"); // Dành cho Windows
            // Nếu dùng Linux/MacOS, thay "tasklist" bằng "ps -e"

            // Đọc kết quả từ lệnh
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.toLowerCase().contains("geckodriver")) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<String> userAgents = Arrays.asList(
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36",
            "Mozilla/5.0 (Linux; Android 11; SM-G973F) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Mobile Safari/537.36",
            "Mozilla/5.0 (iPhone; CPU iPhone OS 15_5 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/15.0 Mobile/15E148 Safari/604.1",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Edge/18.18363 Safari/537.36",
            "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36",
            "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.85 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.5112.102 Safari/537.36",
            "Mozilla/5.0 (iPhone; CPU iPhone OS 14_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.0 Mobile/15E148 Safari/604.1",
            "Mozilla/5.0 (Linux; Android 9; Pixel 3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Mobile Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.5195.52 Safari/537.36",
            "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:98.0) Gecko/20100101 Firefox/98.0",
            "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Edge/107.0.1418.42 Safari/537.36",
            "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:92.0) Gecko/20100101 Firefox/92.0",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36",
            "Mozilla/5.0 (X11; Linux x86_64; rv:98.0) Gecko/20100101 Firefox/98.0",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.110 Safari/537.36",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.4638.69 Safari/537.36",
            "Mozilla/5.0 (Linux; Android 10; Pixel 3 XL) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.81 Mobile Safari/537.36",
            "Mozilla/5.0 (iPhone; CPU iPhone OS 14_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.0 Mobile/15E148 Safari/604.1",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:92.0) Gecko/20100101 Firefox/92.0",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7; rv:91.0) Gecko/20100101 Firefox/91.0",
            "Mozilla/5.0 (Linux; Android 11; Pixel 5) Gecko/20100101 Firefox/92.0",
            "Mozilla/5.0 (iPhone; CPU iPhone OS 14_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.0 Mobile/15E148 Safari/604.1",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/15.0 Safari/605.1.15",
            "Mozilla/5.0 (iPhone; CPU iPhone OS 14_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.0 Mobile/15E148 Safari/604.1",
            "Mozilla/5.0 (iPad; CPU OS 14_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.0 Mobile/15E148 Safari/604.1",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.1020.40 Safari/537.36 Edg/95.0.1020.40",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.1054.34 Safari/537.36 Edg/96.0.1054.34",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.4638.54 Safari/537.36 OPR/80.0.4170.72",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.110 Safari/537.36 OPR/82.0.4227.29",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64; Trident/7.0; AS; rv:11.0) like Gecko",
            "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; AS; rv:11.0) like Gecko",
            "Mozilla/5.0 (iPhone; CPU iPhone OS 15_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/15.0 Mobile/15E148 Safari/604.1",
            "Mozilla/5.0 (Android 11; SM-G991B; Samsung Galaxy S21) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.61 Mobile Safari/537.36"
    );
}

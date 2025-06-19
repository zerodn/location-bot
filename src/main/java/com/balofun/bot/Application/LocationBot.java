package com.balofun.bot.Application;

import com.balofun.bot.dto.ProxyApiDTO;
import com.balofun.bot.feign.impl.ProxyApiFeign;
import com.balofun.bot.repository.TbShopRepository;
import com.balofun.bot.util.CrawlUtil;
import com.balofun.bot.util.DataKhuVuc;
import com.balofun.bot.util.DbUtils;
import com.balofun.bot.util.KillOldGeckodriver;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;

@Component
public class LocationBot {

    private static ProxyApiFeign proxyApi;

    @Autowired
    private TbShopRepository tbShopRepository;

    public static long requestStartTime = System.currentTimeMillis();

    int startKhuVuc = 0;
    int startUrlSearch = 0;
    int startTextSearch = 0;

    @PostConstruct
    void init() {
        try {
            KillOldGeckodriver.kill();
            System.setProperty("webdriver.gecko.driver", "C:\\VarsBot\\firefox_driver\\geckodriver.exe");
            craw();
        } catch (Exception ex) {
            System.out.println("Main fail: " + ex.getMessage());
        }
    }

    private void craw() {
        try {
            for (int i = startKhuVuc; i < DataKhuVuc.KHUVUC.length; i++) {
                startKhuVuc = i;

                for (int j = startUrlSearch; j < DataKhuVuc.DANHSACHURL.length; j++) {
                    startUrlSearch = j;

                    for (int k = startTextSearch; k < DataKhuVuc.BANGCHUCAI.length; k++) {
                        String textSearch = DataKhuVuc.BANGCHUCAI[k];
                        startTextSearch = k;
                        String urlWithTextSearch = "https://shopeefood.vn/" + DataKhuVuc.KHUVUC[i][1] + DataKhuVuc.DANHSACHURL[j] + textSearch;
                        //urlWithTextSearch = "https://shopeefood.vn/da-nang/liquor/danh-sach-dia-diem-giao-tan-noi?q=M";
                        System.out.println("*************************** Fetch start: " + urlWithTextSearch);
                        driver = getSafeDriver(driver);

                        //Thực hien tính thoi gian request
                        Thread watchdog = startWatchDog(driver, System.currentTimeMillis());

                        driver.get(urlWithTextSearch);

                        watchdog.stop();

                        getWebDriverWait().until(ExpectedConditions.or(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a.item-content")), ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(),'ShopeeFood không tìm thấy kết quả cho từ khóa')]"))));
                        List<WebElement> linksWithHref = driver.findElements(By.cssSelector("a.item-content"));

                        if (linksWithHref.size() > 0) {

                            for (WebElement link : linksWithHref) {
                                DbUtils.saveLocation(tbShopRepository, link.getAttribute("href"), textSearch);
                            }

                            while (true) {
                                try {
                                    watchdog = startWatchDog(driver, System.currentTimeMillis());
                                    // 1. Lưu lại phần tử cuối cùng để kiểm tra khi reload xong
                                    WebElement lastElementBeforeClick = driver.findElements(By.cssSelector("a.item-content")).stream().reduce((first, second) -> second).orElse(null);

                                    // 2 lưu dữ liệu trên page
                                    linksWithHref = driver.findElements(By.cssSelector("a.item-content"));
                                    for (WebElement link : linksWithHref) {
                                        DbUtils.saveLocation(tbShopRepository, link.getAttribute("href"), textSearch);
                                    }

                                    // Click page tiếp theo
                                    List<WebElement> paginations = driver.findElements(By.cssSelector("ul.pagination"));
                                    if (paginations.isEmpty()) {
                                        break;
                                    }

                                    getWebDriverWait().until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("ul.pagination")));

                                    WebElement nextLink = driver.findElement(By.cssSelector("ul.pagination li.active + li a"));
                                    List<WebElement> spans = nextLink.findElements(By.cssSelector("span.icon-paging-next"));
                                    if (!spans.isEmpty()) {
                                        break;
                                    }

                                    nextLink.click();

                                    // 5. Chờ cho đến khi AJAX load xong (có thể kiểm tra phần tử mới xuất hiện)
                                    if (lastElementBeforeClick != null) {
                                        getWebDriverWait().until(ExpectedConditions.stalenessOf(lastElementBeforeClick));  // đợi phần tử cũ biến mất
                                    }

                                    watchdog.stop();
                                } catch (NoSuchElementException | TimeoutException e1) {
                                    System.out.println("Không còn phân trang — dừng lại: " + urlWithTextSearch);
                                    break;
                                }
                            }

                            if (watchdog != null) {
                                watchdog.stop();
                            }
                        }

                        System.out.println("*************************** Fetch end: " + urlWithTextSearch);
                    }
                    startTextSearch = 0;
                }
                startUrlSearch = 0;
            }
            startKhuVuc = 0;
        } catch (Exception ex) {
            System.out.println("craw Exception: " + ex.getMessage());
            System.out.println("startKhuVucIndex: " + startKhuVuc);
            System.out.println("startUrlSearchIndex: " + startUrlSearch);
            System.out.println("startTextSearchIndex: " + startTextSearch);
            craw();
        }
    }

    WebDriver driver = null;
    WebDriverWait webDriverWait = null;

    public WebDriver getSafeDriver(WebDriver driver) {
        try {
            if (driver != null && ((RemoteWebDriver) driver).getSessionId() != null) {
                return driver; // Driver còn sống
            }
        } catch (Exception e) {
            // Bất kỳ lỗi nào, tạo mới
        }

        try {
            if (driver != null) driver.quit(); // Tắt cũ
        } catch (Exception ignore) {
        }

        driver = getSafeProxy(driver);

        return driver;
    }

    private Thread startWatchDog(WebDriver driver, long inRequestStartTime) {
        Thread watchdog = new Thread(() -> {
            try {
                Thread.sleep(30 * 1000);
                long elapsedSeconds = (System.currentTimeMillis() - inRequestStartTime) / 1000;
                if (elapsedSeconds > 30) {
                    System.out.println("Watchdog: Đợi quá lâu. Reload lại trang.");
                    driver.navigate().refresh();

                    startWatchDog(driver, System.currentTimeMillis());
                }
            } catch (InterruptedException ignored) {
                System.out.println("Watchdog: " + ignored.getMessage());
            }
        });

        watchdog.start();
        return watchdog;
    }

    private WebDriver getSafeProxy(WebDriver driver) {
//        ProxyApiDTO response = proxyApi.getProxy();
//        if (response != null && "success".equals(response.status)) {
//            ProxyApiDTO.Data data = response.data;
//            System.out.println(String.format("******************* Proxy IP %s", data.realIpAddress + ":" + data.httpPort));
//            Proxy proxy = new Proxy();
//            proxy.setHttpProxy(data.realIpAddress + ":" + data.httpPort);
//            proxy.setSslProxy(data.realIpAddress + ":" + data.httpPort);
//            FirefoxOptions options = new FirefoxOptions();
//            options.setProxy(proxy);
//            options.setCapability("proxy", proxy);
//            driver = new FirefoxDriver(options);
//        } else {
        driver = new FirefoxDriver();
//        }

        return driver;
    }

    public WebDriverWait getWebDriverWait() {
        if (webDriverWait == null) {
            return webDriverWait = new WebDriverWait(driver, 15000);
        }
        return webDriverWait;
    }
}
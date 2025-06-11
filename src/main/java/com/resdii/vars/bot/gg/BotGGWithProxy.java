package com.resdii.vars.bot.gg;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.TimeUnit;

@RestController
public class BotGGWithProxy {

    // Cấu hình proxy
    private static String proxyHost = "103.95.197.104";
    private static int proxyPort = 7001;
    private static List<String> hrefList = new ArrayList<>();
    private static WebDriver driver;
    private static WebDriverWait webDriverWait;
    private static String[] listURL = {"mua-ban-nha-dat", "cho-thue-nha-dat", "mua-ban-nha-dat"};
    private static Random random = new Random();
    private static ChromeOptions options;
    private static FirefoxOptions firefoxOptions;
    private static FirefoxProfile profile;
    private static Proxy proxy;
    private static boolean isDynamicland = false;
    // Danh sách các user-agent khác nhau
    private static List<String> userAgents = Arrays.asList(
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
    private static String[] provinces = {
            "An Giang", "Bà Rịa - Vũng Tàu", "Bạc Liêu", "Bắc Kạn", "Bắc Giang", "Bắc Ninh", "Bến Tre",
            "Bình Dương", "Bình Định", "Bình Phước", "Bình Thuận", "Cà Mau", "Cao Bằng",
            "Cần Thơ", "Đà Nẵng", "Đắk Lắk", "Đắk Nông", "Điện Biên", "Đồng Nai", "Đồng Tháp",
            "Gia Lai", "Hà Giang", "Hà Nam", "Hà Nội", "Hà Tĩnh", "Hải Dương", "Hải Phòng",
            "Hậu Giang", "Hòa Bình", "Hưng Yên", "Khánh Hòa", "Kiên Giang", "Kon Tum",
            "Lai Châu", "Lâm Đồng", "Lạng Sơn", "Lào Cai", "Long An", "Nam Định",
            "Nghệ An", "Ninh Bình", "Ninh Thuận", "Phú Thọ", "Phú Yên", "Quảng Bình",
            "Quảng Nam", "Quảng Ngãi", "Quảng Ninh", "Quảng Trị", "Sóc Trăng",
            "Sơn La", "Tây Ninh", "Thái Bình", "Thái Nguyên", "Thanh Hóa",
            "Thừa Thiên Huế", "Tiền Giang", "TP. Hồ Chí Minh", "Trà Vinh",
            "Tuyên Quang", "Vĩnh Long", "Vĩnh Phúc", "Yên Bái"
    };
    private static String[] textGoogle = {
            "bất động sản {TT} T10 vars",
            "cần mua bất động sản {TT} T10 vars connect",
            "cần mua bất động sản tại {TT} T10 vars connect",
            "cần bán bất động sản {TT} T10 vars connect",
            "cần bán bất động sản tại {TT} T10 vars connect",
            "nhà đất giá rẻ tại {TT} vars connect",
            "Mua bán nhà đất tại {TT} vars connect"
    };


    private static void reloadWithFirefox(boolean isFirefox) {
        if (driver != null) {
            driver.quit();
        }
        if (isFirefox) {
            driver = createFirefoxOptionsWithProxy();
        } else {
            driver = createChromeOptionsWithProxy();
        }
    }

    // Hàm tạo cấu hình proxy
    private static WebDriver createFirefoxOptionsWithProxy() {
        if (firefoxOptions == null) {
            System.setProperty("webdriver.gecko.driver", "C:\\VarsBot\\firefox_driver\\geckodriver.exe");
            proxy = proxy = new Proxy();
            proxy.setHttpProxy("" + proxyHost + ":" + proxyPort);
            proxy.setSslProxy("" + proxyHost + ":" + proxyPort);
            profile = new FirefoxProfile();
            //profile.setPreference("permissions.default.image", 2);
            profile.setPreference("gfx.downloadable_fonts.enabled", false);
            firefoxOptions = new FirefoxOptions();
            firefoxOptions.setProxy(proxy);
            firefoxOptions.setCapability("proxy", proxy);
            firefoxOptions.setBinary("C:\\Program Files\\Mozilla Firefox\\firefox.exe");
            firefoxOptions.setProfile(profile);
            firefoxOptions.addPreference("general.useragent.override", userAgents.get(random.nextInt(userAgents.size())));
        }
        driver = new FirefoxDriver(firefoxOptions);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        webDriverWait = new WebDriverWait(driver, 30);
        return driver;
    }

    // Hàm tạo cấu hình proxy
    private static WebDriver createChromeOptionsWithProxy() {
        if (options == null) {
            System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\Google\\Chrome\\Application\\chromedriver.exe");
            proxy = proxy = new Proxy();
            proxy.setHttpProxy("http://" + proxyHost + ":" + proxyPort);
            proxy.setSslProxy("http://" + proxyHost + ":" + proxyPort);

            options = new ChromeOptions();
            options.setBinary("C:\\VarsBot\\chrome_driver\\chrome-win64\\chrome.exe");
            options.setProxy(proxy);
            options.setCapability("proxy", proxy);
            options.addArguments("user-agent=" + "MyUserAgent/" + UUID.randomUUID().toString());
        }

        driver = new ChromeDriver(options);
        webDriverWait = new WebDriverWait(driver, 30);
        return driver;
    }

    public static void fetchTopLand() {
        try {
            reloadWithFirefox(true);

            System.out.println("*******************************fetchTopLand starting!*****************************");

            String url = "https://varsland.vn/" + listURL[random.nextInt(listURL.length)];
            driver.get(url);
            webDriverWait.until(ExpectedConditions.jsReturnsValue("return document.readyState=='complete';"));
            scrollWindow();
            List<WebElement> linksWithMarkTag = driver.findElements(By.cssSelector("mark"));
            for (int i = 0; i < linksWithMarkTag.size(); i++) {
                try {
                    WebElement link = linksWithMarkTag.get(i);
                    String text = link.getText();
                    hrefList.add(text);
                } catch (StaleElementReferenceException e) {
                    linksWithMarkTag = driver.findElements(By.cssSelector("mark"));
                    i--;
                }
            }

            for (int i = 0; i < hrefList.size(); i++) {
                System.out.println(hrefList.get(i));
            }
            System.out.println("*******************************fetchTopLand done!*****************************");
        } catch (Exception ex) {
            reloadWithFirefox(true);
        }
    }

    public static void openWebWithDynamiclink() {
        if (hrefList.size() > 0) {
            for (int i = 0; i < hrefList.size(); i++) {
                try {
                    reloadWithFirefox(true);
                    driver.get("https://www.google.com");
                    WebElement searchBox = driver.findElement(By.name("q"));
                    searchBox.sendKeys(hrefList.get(i) + " vars");

                    try {
                        Thread.sleep(random.nextInt(10000) + 10000);
                    } catch (Exception ex) {
                    }

                    searchBox.submit();

                    // Tìm tất cả các liên kết trong kết quả tìm kiếm
                    List<WebElement> searchResults = driver.findElements(By.cssSelector("a"));

                    // Duyệt qua các kết quả tìm kiếm và tìm URL bắt đầu với "varsland.vn"
                    boolean found = false;
                    for (WebElement result : searchResults) {
                        String href = result.getAttribute("href");
                        if (href != null && href.contains("varsland.vn")) {
                            System.out.println("Tìm thấy URL: " + href);

                            // Thực hiện click vào kết quả
                            result.click();

                            String initialUrl = driver.getCurrentUrl();
                            webDriverWait.until(ExpectedConditions.not(ExpectedConditions.urlToBe(initialUrl)));

                            try {
                                Thread.sleep(random.nextInt(10000) + 10000);
                            } catch (Exception ex) {
                            }

                            scrollWindow();

                            found = true;
                            break; // Dừng sau khi tìm thấy và click vào URL đầu tiên
                        }
                    }

                    if (!found) {
                        System.out.println("Không tìm thấy kết quả nào có URL bắt đầu bằng 'varsland.vn'.");
                    }
                } catch (Exception e) {
                    System.out.println("Fetch fail: " + hrefList.get(i));
                    reloadWithFirefox(true);
                }
            }
            hrefList.clear();
        }

        try {
            Thread.sleep(random.nextInt(10000) + 10000);
        } catch (Exception ex) {
        }

        try {
            fetchTopLand();
        } catch (Exception ex) {
        }
    }

    public static void openWebWithKeyWord() {
        if (provinces.length > 0) {
            for (int i = 0; i < provinces.length; i++) {
                try {
                    reloadWithFirefox(true);
                    driver.get("https://www.google.com");

                    try {
                        Thread.sleep(random.nextInt(5000) + 5000);
                    } catch (Exception ex) {
                    }

                    WebElement searchBox = driver.findElement(By.name("q"));
                    searchBox.sendKeys(textGoogle[random.nextInt(textGoogle.length)].replace("{TT}", provinces[random.nextInt(provinces.length)]));

                    try {
                        Thread.sleep(random.nextInt(5000) + 5000);
                    } catch (Exception ex) {
                    }

                    searchBox.submit();

                    try {
                        Thread.sleep(random.nextInt(10000) + 10000);
                    } catch (Exception ex) {
                    }

                    // Tìm tất cả các liên kết trong kết quả tìm kiếm
                    List<WebElement> searchResults = driver.findElements(By.cssSelector("a"));

                    // Duyệt qua các kết quả tìm kiếm và tìm URL bắt đầu với "varsland.vn"
                    boolean found = false;
                    for (WebElement result : searchResults) {
                        String href = result.getAttribute("href");
                        if (href != null && href.contains("varsland.vn")) {
                            System.out.println("Tìm thấy URL: " + href);

                            // Thực hiện click vào kết quả
                            result.click();
                            String initialUrl = driver.getCurrentUrl();
                            webDriverWait.until(ExpectedConditions.not(ExpectedConditions.urlToBe(initialUrl)));

                            scrollWindow();

                            try {
                                Thread.sleep(random.nextInt(10000) + 10000);
                            } catch (Exception ex) {
                            }

                            found = true;
                            break; // Dừng sau khi tìm thấy và click vào URL đầu tiên
                        }
                    }

                    if (!found) {
                        System.out.println("Không tìm thấy kết quả nào có URL bắt đầu bằng 'varsland.vn'.");
                    }
                } catch (Exception e) {
                    System.out.println("Fetch fail: " + hrefList.get(i));
                    reloadWithFirefox(true);
                }
            }
            hrefList.clear();
        }

        try {
            Thread.sleep(random.nextInt(10000) + 10000);
        } catch (Exception ex) {
        }

        try {
            fetchTopLand();
        } catch (Exception ex) {
        }
    }

    public static void scrollWindow() {
        try {
            boolean atBottom = false;
            Thread.sleep(random.nextInt(5000) + 5000);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            while (!atBottom) {
                js.executeScript("window.scrollBy(0, 500);");
                Thread.sleep(random.nextInt(500) + 500);
                Long currentHeight = (Long) js.executeScript("return window.scrollY + window.innerHeight;");
                Long totalHeight = (Long) js.executeScript("return document.body.scrollHeight;");
                if (currentHeight >= totalHeight) {
                    atBottom = true;
                }
            }
            Thread.sleep(random.nextInt(5000) + 5000);
        } catch (Exception ex) {
        }
    }
//
//    public static void main(String[] args) {
//        while (true) {
//            try {
//                if (isDynamicland) {
//                    openWebWithDynamiclink();
//                    isDynamicland = false;
//                } else {
//                    openWebWithKeyWord();
//                    isDynamicland = true;
//                }
//            } catch (Exception ex) {
//                System.out.println(ex.getMessage());
//            }
//        }
//    }
}
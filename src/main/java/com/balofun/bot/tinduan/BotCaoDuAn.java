package com.balofun.bot.tinduan;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.web.bind.annotation.RestController;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

@RestController
public class BotCaoDuAn {

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
            options.addArguments("--start-maximized"); // Mở toàn màn hình (chỉ Windows)
        }

        driver = new ChromeDriver(options);
        webDriverWait = new WebDriverWait(driver, 30);
        return driver;
    }

    public static void fetchPage() {
        try {

            System.out.println("*******************************https://propinsight.vn/ starting!*****************************");

            String url = "https://propinsight.vn/du-an?page=";
            String filePath = "D:\\temp\\caoduan\\propinsight.csv";
            try (FileOutputStream fos = new FileOutputStream(filePath);
                 OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
                 BufferedWriter writer = new BufferedWriter(osw)) {
                for (int i = 0; i < 308; i++) {
                    System.out.println(url + (i + 1));
                    reloadWithFirefox(true);
                    driver.get(url + (i + 1));
                    webDriverWait.until(ExpectedConditions.jsReturnsValue("return document.readyState=='complete';"));
                    List<WebElement> projectCardTag = driver.findElements(By.className("project-card"));
                    for (int j = 0; j < projectCardTag.size(); j++) {

                        String trangThai = "";
                        String giaBan = "";
                        String tieuDe = "";
                        String diaChi = "";
                        String chuDauTu = "";
                        String ngayDang = "";

                        try {
                            //Get Trạng Thái và Giá Bán
                            WebElement item = projectCardTag.get(j);
                            List<WebElement> smallTags = item.findElements(By.className("is-elevated"));
                            if (smallTags.size() >= 2) {
                                trangThai = smallTags.get(0).getText();
                                giaBan = smallTags.get(1).getText().replace(",", ".");
                            }

                            //Get TieuDe
                            List<WebElement> projectCardTitle = item.findElements(By.className("project-card-title"));
                            if (projectCardTitle.size() > 0) {
                                tieuDe = projectCardTitle.get(0).getText().replace(",", "-");
                            }

                            //Get project-address
                            List<WebElement> projectAddress = item.findElements(By.className("project-address"));
                            if (projectAddress.size() > 0) {
                                diaChi = projectAddress.get(0).getText().replace(",", "-");
                            }

                            //Get project-investor
                            List<WebElement> projectInvestor = item.findElements(By.className("project-investor"));
                            if (projectInvestor.size() > 0) {
                                chuDauTu = projectInvestor.get(0).getText().replace(",", "-");
                            }

                            //Get project-publication-time
                            List<WebElement> projectPublicationTime = item.findElements(By.className("project-publication-time"));
                            if (projectPublicationTime.size() > 0) {
                                ngayDang = projectPublicationTime.get(0).getText().replace(",", "-");
                            }

                            System.out.println(tieuDe + " - " + ngayDang);
                            System.out.println(trangThai + " - " + giaBan);
                            System.out.println(chuDauTu);
                            System.out.println(diaChi);
                            System.out.println("-----------------------------------------------------");

                            writer.append(String.join(",", Arrays.asList(tieuDe, ngayDang, trangThai, giaBan, chuDauTu, diaChi)));
                            writer.newLine();
                        } catch (StaleElementReferenceException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                    //break;
                }

                System.out.println("*******************************propinsight done!*****************************");
            }
        } catch (IOException ex) {
            System.err.println("Error creating CSV file: " + ex.getMessage());
        }
    }

    public static void fetchPageBDS() {
        try {

            System.out.println("*******************************https://batdongsan.com.vn/ starting!*****************************");

            String url = "https://batdongsan.com.vn/du-an-bat-dong-san/p";
            String filePath = "D:\\temp\\caoduan\\batdongsan.csv";
            try (FileOutputStream fos = new FileOutputStream(filePath);
                 OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
                 BufferedWriter writer = new BufferedWriter(osw)) {
                for (int i = 567; i < 568; i++) {
                    try {
                        System.out.println(url + (i + 1));
                        reloadWithFirefox(true);
                        driver.get(url + (i + 1));
                        webDriverWait.until(ExpectedConditions.jsReturnsValue("return document.readyState=='complete';"));
                        List<WebElement> projectCardTag = driver.findElements(By.className("js__project-card"));
                        for (int j = 0; j < projectCardTag.size(); j++) {

                            String trangThai = "N/A";
                            String giaBan = "N/A";
                            String tieuDe = "N/A";
                            String diaChi = "N/A";
                            String chuDauTu = "N/A";
                            String ngayDang = "N/A";

                            //Get Trạng Thái và Giá Bán
                            WebElement item = projectCardTag.get(j);
                            List<WebElement> smallTags = item.findElements(By.className("re__prj-card-config-value"));
                            if (smallTags.size() > 0) {
                                giaBan = smallTags.get(0).getText().replace(",", ".").replace("\n", " ").replace("\r", " ").replaceAll("\\s+", " ").trim();
                            }

                            List<WebElement> tagInfo = item.findElements(By.className("re__prj-tag-info"));
                            if (tagInfo.size() > 0) {
                                trangThai = tagInfo.get(0).getText().replace("\n", " ").replace("\r", " ").replaceAll("\\s+", " ").trim();
                            }

                            //Get TieuDe
                            List<WebElement> projectCardTitle = item.findElements(By.className("re__prj-card-title"));
                            if (projectCardTitle.size() > 0) {
                                tieuDe = projectCardTitle.get(0).getText().replace(",", "-").replace("\n", " ").replace("\r", " ").replaceAll("\\s+", " ").trim();
                            }

                            //Get project-address
                            List<WebElement> projectAddress = item.findElements(By.className("re__prj-card-location"));
                            if (projectAddress.size() > 0) {
                                diaChi = projectAddress.get(0).getText().replace(",", "-").replace("\n", " ").replace("\r", " ").replaceAll("\\s+", " ").trim();
                            }

                            //Get project-investor
                            List<WebElement> projectInvestor = item.findElements(By.className("re__prj-card-contact-avatar"));
                            if (projectInvestor.size() > 0) {
                                chuDauTu = projectInvestor.get(0).getAttribute("aria-label").replace(",", "-").replace("\n", " ").replace("\r", " ").replaceAll("\\s+", " ").trim();
                            }

                            //Get project-publication-time
//                            List<WebElement> projectPublicationTime = item.findElements(By.className("project-publication-time"));
//                            if (projectPublicationTime.size() > 0) {
//                                ngayDang = projectPublicationTime.get(0).getText().replace(",","-");
//                            }

                            System.out.println(tieuDe + " - " + ngayDang);
                            System.out.println(trangThai + " - " + giaBan);
                            System.out.println(chuDauTu);
                            System.out.println(diaChi);
                            System.out.println("-----------------------------------------------------");

                            writer.append(String.join(",", Arrays.asList(tieuDe, ngayDang, trangThai, giaBan, chuDauTu, diaChi)));
                            writer.newLine();
                        }
                    } catch (StaleElementReferenceException | IOException e) {
                        System.out.println(e.getMessage());
                    }
                }
                System.out.println("*******************************propinsight done!*****************************");
            }
        } catch (
                IOException ex) {
            System.err.println("Error creating CSV file: " + ex.getMessage());
        }

    }

//    public static void main(String[] args) {
//        //fetchPage();
//        fetchPageBDS();
//    }
}
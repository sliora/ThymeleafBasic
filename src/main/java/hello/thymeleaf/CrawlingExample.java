package hello.thymeleaf;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CrawlingExample {

    private WebDriver driver;
    private static final String url = "https://loawa.com";

    @GetMapping("/test/api")
    @ResponseBody
    public String process() {
        System.setProperty("webdriver.chrome.driver", "d:\\chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-popup-blocking");       //팝업안띄움
        options.addArguments("--headless");                       //브라우저 안띄움
        options.addArguments("--disable-gpu");			//gpu 비활성화
        //options.addArguments("--blink-settings=imagesEnabled=false"); //이미지 다운 안받음
        driver = new ChromeDriver(options);

        try {
            return getDataList();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        driver.close();	//탭 닫기
        driver.quit();	//브라우저 닫기

        return "";
    }

    private String getDataList() throws InterruptedException {
        List<String> list = new ArrayList<>();

        WebDriverWait webDriverWait = new WebDriverWait(driver, 10);	//⭐⭐⭐
        driver.get(url);

        List<WebElement> landImg = webDriverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//*[@id=\"main\"]/div[2]/div[3]/div[2]/div[2]/ul/li/figure/img[contains(@class, 'rounded-top') and contains(@src,'https://')]")));
        List<WebElement> landPay = webDriverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//*[@id=\"main\"]/div[2]/div[3]/div[2]/div[2]/ul/li/h5[not(@data-encrypted)]")));
        List<WebElement> landName = webDriverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//*[@id=\"main\"]/div[2]/div[3]/div[2]/div[2]/ul/li/h4/span[not(@data-encrypted)]")));

        JsonObject result = new JsonObject();
        JsonArray jsonArr =new JsonArray();

        result.addProperty("code","200");
        result.addProperty("message","OK");

        for (int i = 0; i < landName.size(); i++) {
            JsonObject jsonObj = new JsonObject();

            jsonObj.addProperty("landName", landName.get(i).getText());
            jsonObj.addProperty("landPay", landPay.get(i).getText());
            jsonObj.addProperty("landImg", landImg.get(i).getAttribute("src"));
            jsonArr.add(jsonObj);
        }

        result.add("data",jsonArr);

        return result.toString();
    }


}

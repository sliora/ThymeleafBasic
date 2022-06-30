package hello.thymeleaf;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.thymeleaf.context.IContext;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class ThymeleafBasicApplicationTests {

	private WebDriver driver;
	private static final String url = "https://loawa.com";

	@Test
	public void process() {
		System.setProperty("webdriver.chrome.driver", "d:\\chromedriver.exe");

		ChromeOptions options = new ChromeOptions();
		options.addArguments("--disable-popup-blocking");       //팝업안띄움
		options.addArguments("--headless");                       //브라우저 안띄움
		options.addArguments("--disable-gpu");			//gpu 비활성화
		//options.addArguments("--blink-settings=imagesEnabled=false"); //이미지 다운 안받음
		driver = new ChromeDriver(options);

		try {
			getDataList();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		driver.close();	//탭 닫기
		driver.quit();	//브라우저 닫기

	}
	@Test
	private List<String> getDataList() throws InterruptedException {
		List<String> list = new ArrayList<>();

		WebDriverWait webDriverWait = new WebDriverWait(driver, 10);	//⭐⭐⭐
		driver.get(url);

		//List<WebElement> until = webDriverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//*[@id=\"main\"]/div[2]/div[3]/div[2]/div[2]/ul/li[1]/figure/img[contains(@src,'https://')]")));
		List<WebElement> landImg = webDriverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//*[@id=\"main\"]/div[2]/div[3]/div[2]/div[2]/ul/li/figure/img[contains(@class, 'rounded-top') and contains(@src,'https://')]")));
		List<WebElement> landPay = webDriverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//*[@id=\"main\"]/div[2]/div[3]/div[2]/div[2]/ul/li/h5[not(@data-encrypted)]")));
		List<WebElement> landName = webDriverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//*[@id=\"main\"]/div[2]/div[3]/div[2]/div[2]/ul/li/h4/span[not(@data-encrypted)]")));

		for (int i = 0; i < landName.size(); i++) {

			System.out.println(landName.get(i).getText());
			System.out.println(landPay.get(i).getText());
			System.out.println(landImg.get(i).getAttribute("src"));
		}

		return list;
	}
}

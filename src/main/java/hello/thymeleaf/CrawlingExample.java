package hello.thymeleaf;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class CrawlingExample {

    private WebDriver driver;
    private static final String url = "https://lostark.game.onstove.com/Profile/Character/엉왜";

    @GetMapping("test/api/v2")
    public String createApiV2() throws IOException {
        Document document = Jsoup.connect(url).ignoreContentType(true).get();
        //Jsoup.connect("https://lostark.game.onstove.com/Profile/Character/);

        //String itemLv = document.select(".level-info2__item").text().replace("Lv.","").replace("달성 아이템 레벨","").substring(0, 5);
        String itemLv = document.select(".level-info2__item").get(0).ownText();
        String charLv = document.select(".level-info__item").text().replace("Lv.","").replace("전투 레벨","");
        String totalLv = document.select(".level-info__expedition").text().replace("Lv.","").replace("원정대 레벨","");
        String server = document.select(".profile-character-info__server").text().replace("@","");
        String guild = document.select(".game-info__guild").text().replace("길드","");
        String skillPoint = document.select(".profile-skill__point").text().replace("사용 스킬 포인트","").replace("보유 스킬 포인트","");
        String carve = document.select(".profile-ability-engrave span").text().replace("환류","환").replace("예리한 둔기","예").replace("타격의 대가","타").replace("아드레날린","아").replace("원한","원").replace("저주받은 인형","저").replace("버스트","버").replace("기습의 대가","기").replace("속전속결","속").replace("점화","점").replace("돌격대장","돌").replace("멈출 수 없는 충동","충").replace("핸드거너","핸").replace("정밀 단도","정").replace("속전속결","속").replace("안정된 상태","안").replace("진화의 유산","유").replace("바리게이트","바").replace("최대 마나 증가","최").replace("절실한 구원","절").replace("중갑 착용","중").replace("각성","각").replace("위기 모면","위").replace("달인의 저력","달").replace("강화 무기","강").replace("정기 흡수","정").replace("일격필살","일").replace("피스메이커","피").replace("잔재된 기운","잔").replace("슈퍼 차지","슈").replace("상급 소환사","상").replace("만개","만").replace("전문의","전");
        String attDef = document.select(".profile-ability-basic span").next("span").text().replace(" "," / ").replace("\\B(?=(\\d{3})+(?!\\d))", ",");
        String charImage = document.select(".profile-equipment__character img").get(0).absUrl("src");
        String card = document.select(".card-effect__title").last().text();
        int qualityValue = 0;
        int equipValue = 0;

        JsonObject result = new JsonObject();
        JsonArray jsonArr =new JsonArray();
        JsonObject jsonObj = new JsonObject();

        result.addProperty("code","200");
        result.addProperty("message","OK");

        //jsonArr
        jsonObj.addProperty("charLv", charLv);
        jsonObj.addProperty("totalLv", totalLv);
        jsonObj.addProperty("server", server);
        jsonObj.addProperty("guild", guild);
        jsonObj.addProperty("skillPoint", skillPoint);
        jsonObj.addProperty("carve", card);
        jsonObj.addProperty("attDef", attDef);
        jsonObj.addProperty("charImage", charImage);
        jsonObj.addProperty("card", card);

        jsonArr.add(jsonObj);
        result.add("data",jsonArr);

        String[] charFeature = {};
        for (var i=0; i<document.select(".profile-ability-battle span").next("span").size(); i++) {

            //charFeature[i] = (document.select(".profile-ability-battle span").next("span").get(i).text());
            System.out.println(document.select(".profile-ability-battle span").next("span").get(i).text());
        }


        //악세 퀄리티 가져오기
        //var data = eval(org.jsoup.Jsoup.connect("https://lostark.game.onstove.com/Profile/Character/"+msg.split(" ")[1]).ignoreContentType(true).get().select("script").get(2).toString().split('<script type="text/javascript">')[1].replace("$.", "").split("</script>")[0]);
        //var data = org.jsoup.Jsoup.connect("https://lostark.game.onstove.com/Profile/Character/"+"엉웨").ignoreContentType(true).get().select("script").get(2).toString().split("<script type='text/javascript'>")[1].replace("$.", "").split("</script>")[0];
        var accTotal = 0;
        var accCount = 0;


        //replier.reply(Object.values(result)[10]);
        //replier.reply(JSON.stringify(result));


        return result.toString();
    }

        /**
         * 조회할 URL셋팅 및 Document 객체 로드하기
         */


    @GetMapping("/test/api")
    @ResponseBody
    public String process() {

        System.setProperty("webdriver.chrome.driver", "chromedriver");


        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-popup-blocking");       //팝업안띄움
        options.addArguments("--headless");                       //브라우저 안띄움
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-gpu");            //gpu 비활성화
        //options.addArguments("--blink-settings=imagesEnabled=false"); //이미지 다운 안받음
        driver = new ChromeDriver(options);

        try {
            return getDataList();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        driver.close();    //탭 닫기
        driver.quit();    //브라우저 닫기

        return "";
    }

    private String getDataList() throws InterruptedException {
        List<String> list = new ArrayList<>();

        //WebDriverWait webDriverWait = new WebDriverWait(driver, 10);	//⭐⭐⭐
        driver.get(url);
        Thread.sleep(1000);

        List<WebElement> landImg = driver.findElements(By.xpath("//*[@id=\"main\"]/div[2]/div[3]/div[2]/div[2]/ul/li/figure/img[contains(@class, 'rounded-top') and contains(@src,'https://')]"));
        List<WebElement> landPay = driver.findElements(By.xpath("//*[@id=\"main\"]/div[2]/div[3]/div[2]/div[2]/ul/li/h5[not(@data-encrypted)]"));
        List<WebElement> landName = driver.findElements(By.xpath("//*[@id=\"main\"]/div[2]/div[3]/div[2]/div[2]/ul/li/h4/span[not(@data-encrypted)]"));

        //List<WebElement> landImg = webDriverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//*[@id=\"main\"]/div[2]/div[3]/div[2]/div[2]/ul/li/figure/img[contains(@class, 'rounded-top') and contains(@src,'https://')]")));
        //List<WebElement> landPay = webDriverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//*[@id=\"main\"]/div[2]/div[3]/div[2]/div[2]/ul/li/h5[not(@data-encrypted)]")));
        //List<WebElement> landName = webDriverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//*[@id=\"main\"]/div[2]/div[3]/div[2]/div[2]/ul/li/h4/span[not(@data-encrypted)]")));

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

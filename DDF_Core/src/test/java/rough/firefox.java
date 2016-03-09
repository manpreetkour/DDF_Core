package rough;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class firefox {
	public static void main(String[] args) {
		WebDriver dr=new FirefoxDriver();
		dr.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		dr.get("https://www.yahoo.com/");
		
		System.setProperty("webdriver.chrome.driver", "C:\\001_Manpreet\\010_softwares\\chromedriver.exe");
		dr=new ChromeDriver();
		dr.get("https://www.yahoo.com/");
	}
}

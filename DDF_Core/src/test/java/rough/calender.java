package rough;

import org.openqa.selenium.By;
import org.testng.annotations.Test;

import com.qtp.selenium.framework.datadriven.TestBase;

public class calender extends TestBase {
	@Test
	public void setCalender() {
		init();
		doLogin("Mozilla", "manpreetkour@yahoo.com", "manpreet");
		driver.findElement(By.xpath("//*[@id='addStock']")).click();
		driver.findElement(By.xpath("//*[@id='addstockname']")).sendKeys("HDFC Bank Ltd.");
		//driver.findElement(By.xpath("//*[@id='stockPurchaseDate']")).click();
		driver.findElement(By.xpath("//*[@id='stockAddDate']")).sendKeys("19-02-2016");
	}
}

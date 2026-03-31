package framework.base;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.io.FileHandler;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

public abstract class BaseTest {
    // Dùng ThreadLocal để hỗ trợ chạy song song an toàn, KHÔNG dùng biến static
    private static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();

    protected WebDriver getDriver() {
        return tlDriver.get();
    }

    // Nhận 2 tham số browser và env từ testng.xml, dùng @Optional để có giá trị mặc định
    @Parameters({"browser", "env"})
    @BeforeMethod(alwaysRun = true)
    public void setUp(@Optional("chrome") String browser, @Optional("dev") String env) {
        System.setProperty("env", env);

        // Đã thay thế khởi tạo thủ công bằng DriverFactory
        // Nó sẽ tự động bật Headless nếu đang chạy trên GitHub Actions
        WebDriver driver = DriverFactory.createDriver(browser);

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        // Mở sẵn trang web thực hành
        driver.get("https://www.saucedemo.com");

        tlDriver.set(driver);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        // Chụp ảnh màn hình và lưu vào target/screenshots/ với tên = {testName}_{timestamp}.png
        if (result.getStatus() == ITestResult.FAILURE) {
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = result.getName() + "_" + timestamp + ".png";
            File srcFile = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
            File destFile = new File("target/screenshots/" + fileName);

            try {
                FileHandler.createDir(new File("target/screenshots/"));
                FileHandler.copy(srcFile, destFile);
                System.out.println("Đã lưu ảnh màn hình lỗi tại: " + destFile.getAbsolutePath());
            } catch (IOException e) {
                System.out.println("Lỗi lưu ảnh: " + e.getMessage());
            }
        }

        // Đóng trình duyệt và xóa ThreadLocal tránh memory leak
        if (getDriver() != null) {
            getDriver().quit();
            tlDriver.remove();
        }
    }
}
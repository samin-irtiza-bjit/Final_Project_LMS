package com.spark.lms;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.spark.lms.SparkLmsApplication;
import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * Test using a local web application based on spring-boot.
 *
 * @author Samin Irtiza
 * @since 1.0.0
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SparkLmsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LMSLoginTest {

    WebDriver driver;

    @LocalServerPort
    int serverPort;

    @BeforeAll
    static void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setupTest() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*","--headless");
        driver = new ChromeDriver(options);
    }

    @AfterEach
    void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void test() {
        // Open system under test
        driver.get("http://localhost:" + serverPort);

        // Verify first page title
        String firstPageTitle = driver.getTitle();
        String expectedFirstPageTitle = "BJIT LMS - Login";
        assertEquals(expectedFirstPageTitle, firstPageTitle);

        WebElement usernameInput = driver.findElement(By.name("username"));
        WebElement passwordInput = driver.findElement(By.name("password"));
        WebElement submitButton = driver.findElement(By.className("submit"));

        usernameInput.sendKeys("admin");
        passwordInput.sendKeys("admin");
        submitButton.click();
        WebElement loggedInUsername = driver.findElement(By.className("user-profile"));
        assertEquals("Mr. Admin", loggedInUsername.getText());
    }

}
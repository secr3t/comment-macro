package commentMacro;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CommentMController implements Initializable {
	@FXML
	private TextField adminId;				// adminIdField
	@FXML
	private PasswordField adminPw;		// pw
	@FXML
	private Slider numberOfUO;			// numberof UnderOver probability 
	@FXML
	private Slider numberOfVTD;			// numberof VictoryTieDefeat probability 
	@FXML
	private TextField	urlInput;
	@FXML
	private Button createBtn;
	@FXML
	private Button loginBtn;
	@FXML
	private Button commentUOBtn;
	@FXML
	private Button commentVTDBtn;
	@FXML
	private Button exit;
	
	private WebDriver adminDriver;
	private org.openqa.selenium.Alert alert;
	private Dimension windowSize;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.setProperty("webdriver.chrome.driver", "src/chromedriver.exe");
		loadAdmin();
		binding();
	}
	
	public void loadAdmin() {
		adminDriver = new ChromeDriver();
		adminDriver.manage().window().maximize();
		windowSize = adminDriver.manage().window().getSize();
		adminDriver.manage().window().setPosition(new Point(0, 0));   
		adminDriver.manage().window().setSize(new Dimension(windowSize.getWidth()/2, windowSize.getHeight()/2));
	}

	/*
	 * write comment methods 
	*/
	public void writeComment() throws InterruptedException {
		String send = "";
		WebElement comment = adminDriver.findElement(By.cssSelector("#comment"));
		scrollIntoView(adminDriver, comment);
		comment.clear();
		comment.sendKeys(send);
		Thread.sleep(200);
		String url = adminDriver.getCurrentUrl();
		String value = url.split("b_key=")[1];
		value = value.split("&")[0];
		((JavascriptExecutor) adminDriver).executeScript("Javascript:BoardReplyWrite(" + value + ")");
		try {
			Thread.sleep(200);
			alert = adminDriver.switchTo().alert();
			alert.accept();
		} catch (Exception e) {
			System.out.println("alert창이 없음");
		}
		adminDriver.switchTo().defaultContent();
	}

	public Object runScript(WebDriver driver, String script, WebElement target) {
		/*
		 * to run script shorter
		 */
		return ((JavascriptExecutor) driver).executeScript(script, target);
	}

	public void scrollIntoView(WebDriver driver, WebElement element) {
		runScript(driver, "arguments[0].scrollIntoView(true)", element);
	}
	
	/*
	 * adminLogin
	*/
	
	public void adminLogin() {
		adminDriver.findElement(By.cssSelector("#login_id")).sendKeys(adminId.getText());
		adminDriver.findElement(By.cssSelector("#login_pass")).sendKeys(adminPw.getText());
		WebElement loginBtn = 
				adminDriver.findElement(By.cssSelector("#login-wrap > form > button"));
		try {
			loginBtn.click();
		} catch (Exception e) {
			scrollIntoView(adminDriver, loginBtn);
			loginBtn.click();
		}
	}
	
	
	/*
	 * binding
	*/
	public void binding() {
		bindExit();
	}
 
	public void bindExit() {
		exit.setOnAction(e->{
				try {
					exit(e);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
		});
	}
	
	/*
	 * methods for bind
	*/
	public void exit(ActionEvent e) throws InterruptedException {
		adminDriver.quit();
		Stage stage = (Stage) exit.getScene().getWindow();
		stage.close();
	}
	
}
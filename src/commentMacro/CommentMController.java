package commentMacro;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import algorithm.ReduplicativePermutation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CommentMController implements Initializable {
	@FXML
	private TextField adminIdInput; // adminIdField
	@FXML
	private PasswordField adminPwInput; // pw
	@FXML
	private Slider uoSlider; // numberof UnderOver probability
	@FXML
	private Slider vtdSlider; // numberof VictoryTieDefeat probability
	@FXML
	private TextField urlInput; // 댓글 작성할 url
	@FXML
	private Button createBtn; // 가짓수 생성 버튼
	@FXML
	private Button loginBtn; // 관리자 로그인
	@FXML
	private Button exitBtn; // 종료 버튼
	@FXML
	private TextField uoInput; // 작성할 가짓수
	@FXML
	private TextField vtdInput; // 작성할 가짓수
	@FXML
	private Label uoProbLabel; // 생성된 언옵 가짓수
	@FXML
	private Label vtdProbLabel;

	private WebDriver adminDriver;
	private org.openqa.selenium.Alert alert;
	private Dimension windowSize;
	private String[] uoVal = { "언", "옵", "언", "옵", "언", "옵", "언", "옵", "언", "옵", "언", "옵" };
	private String[] vtdVal = { "승", "무", "패", "승", "무", "패", "승", "무", "패" };
	private int numUO = 0; // 고를 경기수
	private int numVTD = 0; // 고를 경기수
	private List<String> vtdOutput = new ArrayList<>();
	private List<String> uoOutput = new ArrayList<>();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.setProperty("webdriver.chrome.driver", "src/chromedriver.exe");
		loadAdmin();
	}

	public void loadAdmin() {
		adminDriver = new ChromeDriver();
		adminDriver.manage().window().maximize();
		windowSize = adminDriver.manage().window().getSize();
		adminDriver.manage().window().setPosition(new Point(0, 0));
		adminDriver.manage().window().setSize(new Dimension(windowSize.getWidth() / 2, windowSize.getHeight() / 2));
		adminDriver.get("http://www.815asiabet.com/admin/index.php");
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
		adminDriver.findElement(By.cssSelector("#login_id")).sendKeys(adminIdInput.getText());
		adminDriver.findElement(By.cssSelector("#login_pass")).sendKeys(adminPwInput.getText());
		WebElement loginBtn = adminDriver.findElement(By.cssSelector("#login-wrap > form > button"));
		try {
			loginBtn.click();
		} catch (Exception e) {
			scrollIntoView(adminDriver, loginBtn);
			loginBtn.click();
		}
	}

	/*
	 * methods for bind
	 */
	public void exit() {
		adminDriver.quit();
		Stage stage = (Stage) exitBtn.getScene().getWindow();
		stage.close();
	}

	public void makePerm() {
		PermutationThread t1 = new PermutationThread();
		t1.start();
		while (t1.isAlive()) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		uoProbLabel.setText(String.valueOf(uoOutput.size()));
		vtdProbLabel.setText(String.valueOf(vtdOutput.size()));
	}

	public void commentUo() {
		String uoInputText = uoInput.getText();
		int numOfUO = 0;
		try {
			numOfUO = Integer.parseInt(uoInputText);
			if(numOfUO > uoOutput.size()) {
				numOfUO = uoOutput.size();
				uoInput.setText(String.valueOf(numOfUO));
			}
			System.out.println(uoInputText);
			WritingThread thread = new WritingThread();
			thread.setContents(uoOutput);
			thread.setNums(numOfUO);
			thread.run();
		} catch (NumberFormatException e) {
			System.out.println(e.getMessage() + "\t 숫자가 아닙니다.");
			uoInput.setText("");
		}		

	}

	public void commentVtd() {
		String vtdInputText = vtdInput.getText();
		int numOfVTD = 0;
		try {
			numOfVTD = Integer.parseInt(vtdInputText);
			if(numOfVTD > vtdOutput.size()) {
				numOfVTD = vtdOutput.size();
				vtdInput.setText(String.valueOf(numOfVTD));
			}
			System.out.println(vtdInputText);
			WritingThread thread = new WritingThread();
			thread.setContents(vtdOutput);
			thread.setNums(numOfVTD);
			thread.run();
		} catch (NumberFormatException e) {
			System.out.println(e.getMessage() + "\t 숫자가 아닙니다.");
			vtdInput.setText("");
		}
	}
	
	class WritingThread extends Thread {
		private List<String>contents = new ArrayList<>();
		private int nums;
		public void setContents(List<String> contents) {
			this.contents = contents;
		}
		public void setNums(int nums) {
			this.nums = nums;
		}
		@Override
		public void run() {
			try {
				writeComment(contents, nums);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.interrupt();
		}
	}

	public void writeComment(List<String> contents, int nums) throws InterruptedException {
		adminDriver.get(urlInput.getText());
		for (int i = nums; i > 0; i--) {
			Random rand = new Random();
			int randomNum = rand.nextInt(i);
			WebElement comment = adminDriver.findElement(By.cssSelector("#comment"));
			scrollIntoView(adminDriver, comment);
			comment.clear();
			String selectedComment = contents.get(randomNum);
			contents.remove(randomNum);
			comment.sendKeys(selectedComment);
			Thread.sleep(200);
			String url = adminDriver.getCurrentUrl();
			String value = url.split("b_key=")[1];
			value = value.split("&")[0];
			((JavascriptExecutor) adminDriver).executeScript("Javascript:BoardReplyWrite(" + value + ")");
			try {
				alert = adminDriver.switchTo().alert();
				alert.accept();
			} catch (Exception e) {
				System.out.println("alert창이 없음");
			}
			adminDriver.switchTo().defaultContent();
		}

	}

	
	
	/*
	 * Main thread에서 가짓수를 생성하면 Process가 blocked 상태이기 때문에 뽑을 갯수가 7개 이상일 때 2초 이상 동작이
	 * 방해되는 현상이 발생한다. 따라서 MultiThread로 구현한다.
	 */

	class PermutationThread extends Thread {
		@Override
		public void run() {
			makePerm();
			this.interrupt();
		}

		public void makePerm() {
			numUO = (int) uoSlider.getValue();
			numVTD = (int) vtdSlider.getValue();
			ReduplicativePermutation UOpermutation = new ReduplicativePermutation(numUO < 2 ? 2 : numUO, numUO);
			ReduplicativePermutation VTDpermutation = new ReduplicativePermutation(numVTD < 3 ? 3 : numVTD, numVTD);
			UOpermutation.perm(uoVal, 0);
			VTDpermutation.perm(vtdVal, 0);
			// long b = System.currentTimeMillis();
			uoOutput = UOpermutation.getResults();
			for (String s : uoOutput)
				System.out.println(s);
			System.out.println("언옵갯수 : " + uoOutput.size());
			vtdOutput = VTDpermutation.getResults();
			for (String s : vtdOutput)
				System.out.println(s);
			System.out.println("승무패갯수 : " + vtdOutput.size());
			// long e = System.currentTimeMillis();
			// System.out.println("소요시간 : " + (e-b) + "(ms)");
		}
	}

}

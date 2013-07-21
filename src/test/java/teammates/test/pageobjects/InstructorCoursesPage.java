package teammates.test.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/** Represents the "Courses" page for Instructors. */
public class InstructorCoursesPage extends AppPage {
	/* Explanation: This class follows the 'Page Objects Pattern' and as 
	 * explained in https://code.google.com/p/selenium/wiki/PageObjects
	 * This class represents an abstraction for the the 'Courses' page as 
	 * shown in the Browser. The test class interact with this object when it
	 * wants to perform an action on the web page (e.g., click a button).
	 */
	
	/* Explanation: These are the elements in the page that we want to interact
	 * with. The @FindBy annotation tells the PageFactory class 
	 * (see https://code.google.com/p/selenium/wiki/PageFactory) how to find 
	 * the element. 
	 */
	@FindBy (id = "button_sortcoursename")
	private WebElement sortByCourseNameIcon;
	
	@FindBy (id = "button_sortcourseid")
	private WebElement sortByCourseIdIcon;
	
	@FindBy(id = "courseid")
	private WebElement courseIdTextBox;
	
	@FindBy(id = "coursename")
	private WebElement courseNameTextBox;
	
	@FindBy(id = "instructorlist")
	private WebElement instructorListTextBox;
	
	@FindBy(id = "btnAddCourse")
	private WebElement submitButton;

	public InstructorCoursesPage(Browser browser){
		super(browser);
	}

	/** Used to check if the loaded page is indeed the 'Courses' page */
	@Override
	protected boolean containsExpectedPageContents() {
		return getPageSource().contains("<h1>Add New Course</h1>");
	}

	/**
	 * If instructorsList is null, the current value in the page will be used instead.
	 */
	public InstructorCoursesPage addCourse(String courseId, String courseName, String instructorsList) {
		fillTextBox(courseIdTextBox, courseId);
		fillTextBox(courseNameTextBox, courseName);
		if(instructorsList!=null){
			fillTextBox(instructorListTextBox, instructorsList);
		}
		submitButton.click();
		waitForPageToLoad();
		return this;
	}
	
	public String fillCourseIdTextBox(String value){
		fillTextBox(courseIdTextBox, value);
		return getTextBoxValue(courseIdTextBox);
	}
	
	public String fillCourseNameTextBox(String value){
		fillTextBox(courseNameTextBox, value);
		return getTextBoxValue(courseNameTextBox);
	}

	public String getInstructorList() {
		return getTextBoxValue(instructorListTextBox);
	}

	public String fillInstructorListTextBox(String value) {
		fillTextBox(instructorListTextBox, value);
		return getTextBoxValue(instructorListTextBox);
	}

	public void submitAndConfirm() {
		clickAndConfirm(submitButton);
		waitForPageToLoad();
	}

	public void submitAndCancel() {
		clickAndCancel(submitButton);
		waitForPageToLoad();
	}

	public WebElement getDeleteLink(String courseId) {
		int courseRowNumber = getRowNumberOfCourse(courseId);
		return getDeleteLinkInRow(courseRowNumber);
	}

	public InstructorCoursesPage sortByCourseName() {
		sortByCourseNameIcon.click();
		return this;
	}
	
	public InstructorCoursesPage sortByCourseId() {
		sortByCourseIdIcon.click();
		return this;
	}

	public InstructorCourseEnrollPage loadEnrollLink(String courseId) {
		int courseRowNumber = getRowNumberOfCourse(courseId);
		return goToLinkInRow(
				By.className("t_course_enroll" + courseRowNumber), 
				InstructorCourseEnrollPage.class);
	}

	public InstructorCourseDetailsPage loadViewLink(String courseId) {
		int courseRowNumber = getRowNumberOfCourse(courseId);
		return goToLinkInRow(
				By.className("t_course_view" + courseRowNumber),
				InstructorCourseDetailsPage.class);
	}

	private int getCourseCount() {
		return browser.driver.findElements(By.className("courses_row")).size();
	}

	private int getRowNumberOfCourse(String courseId) {
		for (int i = 0; i < getCourseCount(); i++) {
			if (getCourseIdCell(i).getText().equals(courseId)) {
				return i;
			}
		}
		return -1;
	}

	private WebElement getCourseIdCell(int rowId) {
		return browser.driver.findElement(By.id("courseid" + rowId));
	}

	private WebElement getDeleteLinkInRow(int rowId) {
		By deleteLink =  By.className("t_course_delete" + rowId);
		return browser.driver.findElement(deleteLink);
	}

	private <T extends AppPage>T goToLinkInRow(By locator, Class<T> destinationPageType) {
		browser.driver.findElement(locator).click();
		waitForPageToLoad();
		return changePageType(destinationPageType);
	}

}

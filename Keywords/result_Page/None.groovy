package result_Page

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows

import internal.GlobalVariable

public class None {

	String searchedCompanyName

	@Keyword
	def operation_NONE() {

		searchedCompanyName = GlobalVariable.company

		println('\n' + searchedCompanyName)

		WebUI.click(findTestObject('Search_Result_Page/CheckBox_RX'))

		WebUI.click(findTestObject('Search_Result_Page/CheckBox_OTC'))

		WebUI.click(findTestObject('Search_Result_Page/CheckBox_DISCN'))

		WebUI.selectOptionByValue(findTestObject('Search_Result_Page/Dropdown_View_All'), '-1', false)
	}
}

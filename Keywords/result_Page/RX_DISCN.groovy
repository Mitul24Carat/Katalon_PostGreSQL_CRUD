package result_Page

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement

import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVRecord
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable

public class RX_DISCN {

	//
	String searchedCompanyName
	String tableName
	String no_Records

	//	Operations Of Downloaded File
	File downloadedCSV
	File oldSameCSV
	File newFile

	//	Operations Of CSV Reader
	File downloadedCSVFileTORead
	BufferedReader reader
	org.apache.commons.csv.CSVParser csvParser

	List<String> mktStatus1stColumn = new ArrayList<String>()
	List<String> activeIngredient2ndColumn = new ArrayList<String>()
	List<String> proprietaryName3rdColumn = new ArrayList<String>()
	List<String> applNo4thColumn = new ArrayList<String>()
	List<String> productNumber5thColumn = new ArrayList<String>()
	List<String> dosageform6thColumn = new ArrayList<String>()
	List<String> route7thColumn = new ArrayList<String>()
	List<String> strength8thColumn = new ArrayList<String>()
	List<String> teCode9thColumn = new ArrayList<String>()
	List<String> rld10thColumn = new ArrayList<String>()
	List<String> rs11thColumn = new ArrayList<String>()
	List<String> applicantHolder12thColumn = new ArrayList<String>()
	List<String> approvalDate13thColumn = new ArrayList<String>()

	//	Operations Of DATABASE

	Connection con
	Statement statement
	PreparedStatement preparedStatement
	ResultSet rs

	String createTableQuery
	String ifTableExists


	@Keyword
	def operation_RX_DISCN() {


		searchedCompanyName= GlobalVariable.company

		println('\n' + searchedCompanyName)

		tableName = searchedCompanyName + '_RX_DISCN_Records'

		println('\n'+tableName)

		WebUI.click(findTestObject('Search_Result_Page/CheckBox_OTC'))

		WebUI.selectOptionByValue(findTestObject('Search_Result_Page/Dropdown_View_All'), '-1', false)

		sleep(1000)

		if (existsElement(GlobalVariable.no_records_Xpath) == true) {

			no_Records = WebUI.getText(findTestObject('Search_Result_Page/No_records_available'))

			println('\n' + no_Records + " For Searched Company----------------------")

			WebUI.closeBrowser()
		}

		else {

			WebUI.click(findTestObject('Search_Result_Page/Button_Download_CSV'))

			sleep(2000)

			deleteAndRenameFile()

			readCSVByColumnAndStoreIntoList()

			dbConnectionAndOperations()
		}
	}

	def deleteAndRenameFile() {

		/*
		 * Downloaded File Fetching, Delete if already Exists And Renaming according to check-box Selection
		 */

		sleep(1500)

		downloadedCSV = new File(GlobalVariable.download_path ).listFiles()?.sort {
			-it.lastModified()
		}?.head()

		oldSameCSV = new File(GlobalVariable.download_path + tableName + '.csv')

		if (oldSameCSV.exists()) {

			oldSameCSV.delete()

			println('\n' + 'Same Exsited File Deteled')
		}
		else {
			println('\n' + 'File did not Delete')
		}

		newFile = new File(GlobalVariable.download_path + tableName + '.csv')
		downloadedCSV.renameTo(newFile)
	}

	def readCSVByColumnAndStoreIntoList() {

		/*
		 * Will Read Downloaded CSV File and Store Its data into ArrayList
		 */

		sleep(1500)

		downloadedCSVFileTORead = new File(GlobalVariable.download_path + tableName + '.csv')

		sleep(1000)

		reader = new BufferedReader(new FileReader(downloadedCSVFileTORead))
		csvParser = CSVFormat.DEFAULT.withDelimiter(',' as char).withHeader().parse(reader)

		for (CSVRecord record : csvParser) {

			mktStatus1stColumn.add(record.get("Mkt.Status"))
			activeIngredient2ndColumn.add(record.get("Active Ingredient"))
			proprietaryName3rdColumn.add(record.get("Proprietary Name"))
			applNo4thColumn.add(record.get("Appl. No."))
			productNumber5thColumn.add(record.get("Product Number"))
			dosageform6thColumn.add(record.get("Dosage Form"))
			route7thColumn.add(record.get("Route"))
			strength8thColumn.add(record.get("Strength"))
			teCode9thColumn.add(record.get("TE Code"))
			rld10thColumn.add(record.get("RLD"))
			rs11thColumn.add(record.get("RS"))
			applicantHolder12thColumn.add(record.get("Applicant Holder"))
			approvalDate13thColumn.add(record.get("Approval Date"))
		}

		println( '\n' + "Total number of the Rows of the Column are ---->> " + mktStatus1stColumn.size())
	}

	def dbConnectionAndOperations() {

		/*
		 * Connection TO the Database
		 * Delete the Table If already exists and Create New Table
		 * Add ArrayList Data into Table
		 * Read the Table Data after adding values into Console
		 */

		sleep(1500)

		deleteExistsTableAndCreateNew()

		addCSVDataIntoCreatedTable()

		readTableData()
	}

	def deleteExistsTableAndCreateNew() {

		Class.forName(GlobalVariable.postgre_sql_class)
		con = DriverManager.getConnection(GlobalVariable.db_url_us_orange_book, GlobalVariable.db_username, GlobalVariable.db_password)
		statement = con.createStatement()

		ifTableExists = "DROP TABLE IF EXISTS " + '"' + tableName + '"'

		System.out.println('\n' + ifTableExists)

		statement.executeUpdate(ifTableExists)

		boolean flagTableExist = true

		if (flagTableExist == true) {

			println('\n' + "Table with same name " + "(" + tableName + ") "	+ "was existed and Deleted----------")

			createTableAfterDeletion()
		}
		else {

			println('\n' + "Table with same name was not existed So Created the table directly without deletion----------")

			createTableAfterDeletion()
		}
	}

	def createTableAfterDeletion() {

		sleep(1000)

		createTableQuery = "CREATE TABLE " + '"' + tableName + '"' + "(" + "Mkt" + "_"+ "Status" + " " + GlobalVariable.varchar + ", " + "Active" + "_" + "Ingredient" + " " + GlobalVariable.varchar + ", " + "Proprietary" + "_" + "Name" + " " + GlobalVariable.varchar + ", " + "Appl" + "_" + "No" + " " + GlobalVariable.varchar + ", " + "Product" + "_" + "Number" + " " + GlobalVariable.varchar + ", " + "Dosage" + "_" + "Form" + " " + GlobalVariable.varchar + ", " + "Route" + " " + GlobalVariable.varchar + ", " + "Strength" + " " + GlobalVariable.varchar + ", " + "TE" + "_" + "Code" + " " + GlobalVariable.varchar + ", " + "RLD" + " " + GlobalVariable.varchar + ", " + "RS" + " " + GlobalVariable.varchar + " , " + "Applicant" + "_" + "Holder" + " " + GlobalVariable.varchar + ", " + "Approval" + "_" + "Date" + " " + GlobalVariable.varchar + ")"

		println('\n' + createTableQuery)

		statement.executeUpdate(createTableQuery)

		boolean flagTableCreation = true

		statement.close()

		if (flagTableCreation == true) {

			System.out.println('\n' + tableName	+ " Table is Created Successfully with Updated Records.----------")
		}
		else {

			System.out.println('\n' + "Somthing went wrong during table creation----------")
		}
	}

	def addCSVDataIntoCreatedTable() {

		sleep(1000)

		for (int i = 0; i < mktStatus1stColumn.size(); i++) {

			String addDataIntoSQLQuery = "INSERT INTO " + '"' + tableName + '"' + "" + "(Mkt_Status, Active_Ingredient, Proprietary_Name, Appl_No, Product_Number, Dosage_Form, Route, Strength, TE_Code, RLD, RS, Applicant_Holder, Approval_Date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"

			preparedStatement = con.prepareStatement(addDataIntoSQLQuery)

			preparedStatement.setString(1, mktStatus1stColumn.get(i))
			preparedStatement.setString(2, activeIngredient2ndColumn.get(i))
			preparedStatement.setString(3, proprietaryName3rdColumn.get(i))
			preparedStatement.setString(4, applNo4thColumn.get(i))
			preparedStatement.setString(5, productNumber5thColumn.get(i))
			preparedStatement.setString(6, dosageform6thColumn.get(i))
			preparedStatement.setString(7, route7thColumn.get(i))
			preparedStatement.setString(8, strength8thColumn.get(i))
			preparedStatement.setString(9, teCode9thColumn.get(i))
			preparedStatement.setString(10, rld10thColumn.get(i))
			preparedStatement.setString(11, rs11thColumn.get(i))
			preparedStatement.setString(12, applicantHolder12thColumn.get(i))
			preparedStatement.setString(13, approvalDate13thColumn.get(i))

			preparedStatement.executeUpdate()
		}

		println('\n' + 'All Data Added into Table ---->> '+ tableName + '\n')
	}

	def readTableData() {

		sleep(1000)

		// SQL command data stored in String datatype
		String sql = "select * from " + '"' + tableName + '"'

		preparedStatement = con.prepareStatement(sql)

		rs = preparedStatement.executeQuery()

		System.out.println("Mkt_Status\t\tActive_Ingredient\t\tProprietary_Name\t\tAppl_No\t\tProduct_Number\t\tDosage_Form\t\tRoute\t\tStrength\t\tTE_Code\t\tRLD\t\tRS\t\tApplicant_Holder\t\tApproval_Date")

		// Condition check
		while (rs.next()) {

			String column1 = rs.getString("Mkt_Status")
			String column2 = rs.getString("Active_Ingredient")
			String column3 = rs.getString("Proprietary_Name")
			String column4 = rs.getString("Appl_No")
			int number = rs.getInt("Product_Number")
			String column6 = rs.getString("Dosage_Form")
			String column7 = rs.getString("Route")
			String column8 = rs.getString("Strength")
			String column9 = rs.getString("TE_Code")
			String column10 = rs.getString("RLD")
			String column11 = rs.getString("RS")
			String column12 = rs.getString("Applicant_Holder")
			String column13 = rs.getString("Approval_Date")

			println(column1 + "\t\t" + column2 + "\t\t" + "\t\t" + column3 + "\t\t" + column4 + "\t\t" + number + "\t\t" + column6 + "\t\t" + column7 + "\t\t" + column8 + "\t\t" + column9 + "\t\t" + column10 + "\t\t" + column11 + "\t\t" + column12 + "\t\t" + column13)
		}

		println('\n' + 'Total Number of Rows Added and Fetched is ---->> ' + mktStatus1stColumn.size())
	}

	boolean existsElement(String xPath) {
		try {
			WebDriver driver = DriverFactory.getWebDriver()
			driver.findElement(By.xpath(xPath))
		} catch (Exception e) {
			return false
		}
		return true
	}
}

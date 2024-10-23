package crud_db_operations

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject

import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement

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

public class CRUD_Operations_PostGre_DB {

	String tableName = 'contestant_data'


	String addValue1 = "(1, 'John', 'Toronto', '24-01-2024', '1234567890'), "
	String addValue2 = "(2, 'Steve', 'Windsor', '12-01-2024', '2345678910'), "
	String addValue3 = "(3, 'Robert', 'Banff', '15-02-2024', '3456789120'), "
	String addValue4 = "(4, 'Mark', 'Victoria', '24-02-2024', '4567891230'), "
	String addValue5 = "(5, 'Logan', 'Qubec', '24-06-2024', '5678912345');"

	String updateValue3 = "(Alex, Ottawa, 28-02-2024, 2424242424)"
	String updateValue4 = "(Paul, Montreal, 01-03-2024, 3434343434)"


	//	Operations Of DATABASE

	Connection con
	Statement statement
	PreparedStatement preparedStatement
	ResultSet rs

	//	DB Queries
	String ifTableExists
	String createTable_Query
	String addDataIntoTable_Query
	String readTable_Query
	String updateTable_Query
	String deletaDataFromTable_Query;




	@Keyword
	def allCRUDOeration() {

		ifTableExists = "DROP TABLE IF EXISTS " + '"' + tableName + '"'

		createTable_Query = "CREATE TABLE " + '"' + tableName + '"' + "(contestant_id SERIAL PRIMARY KEY, " + "contestant_name VARCHAR(100) NOT NULL, " + "city VARCHAR(100) NOT NULL, " + "date_of_admission DATE NOT NULL DEFAULT CURRENT_DATE, " + "contact VARCHAR(100) NOT NULL);"

		addDataIntoTable_Query = "INSERT INTO "  + '"' + tableName + '"' + "(contestant_id, contestant_name, city, date_of_admission, contact) "+ " VALUES " + addValue1 + addValue2 + addValue3 + addValue4 + addValue5

		readTable_Query = "select * from " + '"' + tableName + '"'

		//		updateTable_Query = "UPDATE "+ tableName + " SET (contestant_name, city, date_of_admission, contact) = CASE contestant_id WHEN 3 THEN '"+updateValue3+"' WHEN 4 THEN '"+updateValue4+"' ELSE 'Table Not Uptated' END;"
		//		println('\n' + updateTable_Query)

		updateTable_Query = "UPDATE "+ tableName + " SET contestant_name = CASE contestant_id WHEN 3 THEN 'Alex' WHEN 4 THEN 'Paul' ELSE contestant_name END;"

		deletaDataFromTable_Query = "DELETE FROM " + tableName +" WHERE contestant_id = 5;"

		getConnectedToDB()

		createTable()

		addDataInTable()

		readTable()

		updateTable()

		readTable()

		deleteDataFromTable()

		readTable()
	}

	def getConnectedToDB() {

		Class.forName(GlobalVariable.postgre_sql_class)
		con = DriverManager.getConnection(GlobalVariable.db_url_katalon_sql_demo, GlobalVariable.db_username, GlobalVariable.db_password)

		if (con != null) {
			println('\n' + 'Connection to Database is Successful')
		}
		else {
			println('\n' + 'Connection is not Established')
		}
	}

	def createTable() {

		sleep(1000)

		statement = con.createStatement()

		System.out.println('\n' + ifTableExists)

		statement.executeUpdate(ifTableExists)

		boolean flagTableExist = true

		if (flagTableExist == true) {

			println('\n' + "Table with same name " + "(" + tableName + ") "	+ "was existed and Deleted----------")

			makeTable()
		}
		else {

			println('\n' + "Table with same name was not existed So Created the table directly without deletion----------")

			makeTable()
		}
	}


	def makeTable() {

		sleep(1000)

		println('\n' + createTable_Query)

		statement.executeUpdate(createTable_Query)

		boolean flagTableCreation = true

		statement.close()

		if (flagTableCreation == true) {

			System.out.println('\n' + tableName	+ " Table is Created Successfully with Updated Records.----------")
		}
		else {

			System.out.println('\n' + "Somthing went wrong during table creation----------")
		}
	}

	def addDataInTable() {

		sleep(1000)

		println('\n' + addDataIntoTable_Query)

		preparedStatement = con.prepareStatement(addDataIntoTable_Query)

		preparedStatement.executeUpdate()

		println('\n' + 'All Data Added into Table ---->> '+ tableName + '\n')
	}

	def readTable() {

		sleep(1000)

		preparedStatement = con.prepareStatement(readTable_Query)

		rs = preparedStatement.executeQuery()

		System.out.println("contestant_id\t\tcontestant_name\t\tcity\t\tdate_of_admission\t\tcontact")

		// Condition check
		while (rs.next()) {

			String column1 = rs.getString("contestant_id")
			String column2 = rs.getString("contestant_name")
			String column3 = rs.getString("city")
			String column4 = rs.getString("date_of_admission")
			String column5 = rs.getString("contact")


			println(column1 + "\t\t" + column2 + "\t\t" + "\t\t" + column3 + "\t\t" + column4 + "\t\t" + column5)
		}
	}

	def updateTable() {

		sleep(1000)

		println('\n' + updateTable_Query)

		preparedStatement = con.prepareStatement(updateTable_Query)

		preparedStatement.executeUpdate()

		println('\n' + 'Successfully Updated the ---->> '+ tableName + '\n')
	}

	def deleteDataFromTable() {

		sleep(1000)

		println('\n' + deletaDataFromTable_Query)

		preparedStatement = con.prepareStatement(deletaDataFromTable_Query)

		preparedStatement.executeUpdate()

		println('\n' + 'Successfully Deleted the Data From ---->> '+ tableName + '\n')
	}
}

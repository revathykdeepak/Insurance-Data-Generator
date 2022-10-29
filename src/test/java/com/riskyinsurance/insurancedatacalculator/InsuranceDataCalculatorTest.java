/**
 * 
 */
package com.riskyinsurance.insurancedatacalculator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.riskyinsurance.insurancedatacalculator.Utils.CalcMethod;

public class InsuranceDataCalculatorTest {

	static InsuranceDataCalculator calculator;
	static ObjectMapper jsonMapper;
	static String testDir = "insurance-data-calculator-test-data/";
	@Rule
	public final ExpectedException exception = ExpectedException.none();
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		calculator = new InsuranceDataCalculator(CalcMethod.U_ACC);
		jsonMapper =  JsonMapper.builder()
	    		   .addModule(new JavaTimeModule())
	    		   .build();
		Files.createDirectories(Paths.get(testDir));
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}


	private void testWriteDataToJsonPath(InsuranceData dataToWrite, Path path) {
		InsuranceData dataRead;
		boolean resFlag;
		try {
			resFlag = calculator.writeDataToJsonPath(dataToWrite, path);
			assertTrue(resFlag);
		
			dataRead = jsonMapper.readValue(path.toFile(), InsuranceData.class);
			assertTrue(dataToWrite.equals(dataRead));
		} catch (Exception e) {
			fail("Exception : "+ e.getMessage() );
		}
	}
	
	@Test
	public void testWriteDataToJsonPath_DefaultValues() {
		InsuranceData dataToWrite;
		Path path;
		String savePath = testDir + "jsonWrite_default.json";
	
		dataToWrite = new InsuranceData();
		path = Paths.get(savePath);
		testWriteDataToJsonPath(dataToWrite, path);
	}
	
	@Test
	public void testWriteDataToJsonPath_WithValues() {
		InsuranceData dataToWrite;
		String savePath = testDir + "jsonWrite_WithValues.json";
		Path path;
		dataToWrite = new InsuranceData(new BigDecimal(15.0), Duration.ofMillis(5002)
				, new BigDecimal(150.0), Duration.ofMillis(15002) );
		path = Paths.get(savePath);
		testWriteDataToJsonPath(dataToWrite, path);
	}

	private void testGetWaypointListFromPath(ArrayList<Waypoint> writtenList, String dataToWrite, Path path) {
		ArrayList<Waypoint> readList;
		try {
			Files.writeString(path, dataToWrite);
			readList = calculator.getWaypointListFromPath(path);
			assertEquals(readList, writtenList);
		} catch (IOException e) {
			fail("Exception : "+ e.getMessage() );
		}
	}
	
	@Test
	public void testGetWaypointListFromPath_WithNoValues() {
		ArrayList<Waypoint>  writtenList;
		String dataToWrite = "[]";
		writtenList = new ArrayList<Waypoint>();
		String savePath = testDir + "jsonRead_WithNoValues.json";
		Path path = Paths.get(savePath);
		
		testGetWaypointListFromPath(writtenList, dataToWrite, path);		
	}
	
	@Test
	public void testGetWaypointListFromPath_With1Value() {
		ArrayList<Waypoint>  writtenList = new ArrayList<Waypoint>();
		String dataToWrite = "[\r\n"
				+ "  {\r\n"
				+ "    \"timestamp\": \"2016-06-21T12:00:00.000Z\",\r\n"
				+ "    \"position\": {\r\n"
				+ "      \"latitude\": 59.334,\r\n"
				+ "      \"longitude\": 18.0667 \r\n"
				+ "    },\r\n"
				+ "    \"speed\": 6.3889,\r\n"
				+ "    \"speed_limit\": 8.33\r\n"
				+ "  }]";
		Waypoint wp = new Waypoint(LocalDateTime.parse("2016-06-21T12:00:00.000"),
				59.334, 18.0667, 6.3889, 8.33);
		writtenList.add(wp);
		String savePath = testDir + "jsonRead_With1Value.json";
		Path path = Paths.get(savePath);
		
		testGetWaypointListFromPath(writtenList, dataToWrite, path);		
	}
	@Test
	public void testGetWaypointListFromPath_WithValues() {
		ArrayList<Waypoint>  writtenList = new ArrayList<Waypoint>();
		String dataToWrite = "[\r\n"
				+ "  {\r\n"
				+ "    \"timestamp\": \"2016-06-21T12:00:00.000Z\",\r\n"
				+ "    \"position\": {\r\n"
				+ "      \"latitude\": 59.334,\r\n"
				+ "      \"longitude\": 18.0667 \r\n"
				+ "    },\r\n"
				+ "    \"speed\": 6.3889,\r\n"
				+ "    \"speed_limit\": 8.33\r\n"
				+ "  },\r\n"
				+ "  {\r\n"
				+ "    \"timestamp\": \"2016-06-21T12:00:05.000Z\",\r\n"
				+ "    \"position\": {\r\n"
				+ "      \"latitude\": 59.3337,\r\n"
				+ "      \"longitude\": 18.0662\r\n"
				+ "    },\r\n"
				+ "    \"speed\": 9.4,\r\n"
				+ "    \"speed_limit\": 8.33\r\n"
				+ "  },\r\n"
				+ "  {\r\n"
				+ "    \"timestamp\": \"2016-06-21T12:00:10.000Z\",\r\n"
				+ "    \"position\": {\r\n"
				+ "      \"latitude\": 59.3331,\r\n"
				+ "      \"longitude\": 18.0664\r\n"
				+ "    },\r\n"
				+ "    \"speed\": 11.1,\r\n"
				+ "    \"speed_limit\": 8.33\r\n"
				+ "  }]";
		Waypoint wp = new Waypoint(LocalDateTime.parse("2016-06-21T12:00:00.000"),
				59.334, 18.0667, 6.3889, 8.33);
		writtenList.add(wp);
		wp = new Waypoint(LocalDateTime.parse("2016-06-21T12:00:05.000"),
				59.3337, 18.0662, 9.4, 8.33);
		writtenList.add(wp);
		wp = new Waypoint(LocalDateTime.parse("2016-06-21T12:00:10.000"),
				59.3331, 18.0664, 11.1, 8.33);
		writtenList.add(wp);
		String savePath = testDir + "jsonRead_WithValues.json";
		Path path = Paths.get(savePath);
		
		testGetWaypointListFromPath(writtenList, dataToWrite, path);		
	}
	
	@Test
	public void testGetWaypointListFromPath_MissingField() {
		ArrayList<Waypoint>  writtenList = new ArrayList<Waypoint>();
		String dataToWrite = "[\r\n"
				+ "  {\r\n"
				+ "    \"timestamp\": \"2016-06-21T12:00:00.000Z\",\r\n"
				+ "    \"position\": {\r\n"
				+ "      \"latitude\": 59.334,\r\n"
				+ "      \"longitude\": 18.0667 \r\n"
				+ "    },\r\n"
				+ "    \"speed_limit\": 8.33\r\n"
				+ "  }]";
		Waypoint wp = new Waypoint(LocalDateTime.parse("2016-06-21T12:00:00.000"),
				59.334, 18.0667, 0.0, 8.33);
		writtenList.add(wp);
		//speed value is  missing in file
		String savePath = testDir + "jsonRead_MissingField.json";
		Path path = Paths.get(savePath);
		
		testGetWaypointListFromPath(writtenList, dataToWrite, path);		
	}
	
	@Test
	public void testGetWaypointListFromPath_AdditionalField() {
		/**
		 * Currently not supported !!!!!!!
		 * 
		 * Added an extra field "direction" in file. 
		 * Exception thrown and no data is retreived
		 */
		//
		String dataToWrite = "[\r\n"
				+ "  {\r\n"
				+ "    \"timestamp\": \"2016-06-21T12:00:00.000Z\",\r\n"
				+ "    \"position\": {\r\n"
				+ "      \"latitude\": 59.334,\r\n"
				+ "      \"longitude\": 18.0667 \r\n"
				+ "    },\r\n"
				+ "    \"speed\": 6.3889,\r\n"
				+ "    \"speed_limit\": 8.33,\r\n"
				+ "    \"direction\":\"N\"\r\n"
				+ "  }]";
	
		String savePath = testDir + "jsonRead_AdditionalField.json";
		Path path = Paths.get(savePath);

		testGetWaypointListFromPath(new ArrayList<Waypoint>(), dataToWrite, path);		
	}
	
	@Test
	public void testIsValidWaypoint() {
		ArrayList<Waypoint> wpList = new ArrayList<Waypoint>();
		wpList.add(new Waypoint(LocalDateTime.parse("2016-06-21T12:00:00.000"),
				59.334, -18.0667, 0.0, 8.33)); //valid
		wpList.add(new Waypoint(LocalDateTime.parse("2016-06-21T12:00:00.000"),
				59.334, -181.0667, 0.0, 8.33)); //long invalid
		wpList.add(new Waypoint(LocalDateTime.parse("2016-06-21T12:00:00.000"),
				59.334, -18.0667, 0.0, 128.33)); //speed limit invalid
		wpList.add(new Waypoint(LocalDateTime.parse("2016-06-21T12:00:00.000"),
				59.334, 18.0667, 155.0, 8.33)); //speed invalid
		wpList.add(new Waypoint(LocalDateTime.parse("2016-06-21T12:00:00.000"),
				-99.334, 18.0667, 155.0, 8.33)); //latitude invalid
		boolean isValid[]= {true,false,false,false,false};
		for(int i=0; i< isValid.length; i++) {
			assertEquals(isValid[i], calculator.isValidWaypoint(wpList.get(i)));
		}
	}
	
	@Test
	public void testGetnextValidWaypoint() {
		
		ArrayList<Waypoint> wpList;
		Iterator<Waypoint> iterator;
		Waypoint valWp1 = new Waypoint(LocalDateTime.parse("2016-06-21T12:00:00.000"),
				59.334, 18.0667, 6.3889, 8.33);
		Waypoint valWp2 = new Waypoint(LocalDateTime.parse("2016-06-21T12:00:05.000"),
				59.3337, 18.0662, 9.4, 8.33);
		Waypoint valWp3 = new Waypoint(LocalDateTime.parse("2016-06-21T12:00:10.000"),
				59.3331, 18.0664, 11.1, 8.33);
		
		Waypoint invalWp1 = new Waypoint(LocalDateTime.parse("2016-06-21T12:00:00.000"),
				59.334, -188.0667, 6.3889, 8.33);
		Waypoint invalWp2 = new Waypoint(LocalDateTime.parse("2016-06-21T12:00:05.000"),
				59.3337, 18.0662, 9.4, 128.33);
		Waypoint invalWp3 = new Waypoint(LocalDateTime.parse("2016-06-21T12:00:10.000"),
				-99.3331, 18.0664, 11.1, 8.33);
		
		Waypoint[] wpArray1 = {valWp1, invalWp1,valWp2};
		wpList = new ArrayList<>(Arrays.asList(wpArray1)) ;
		iterator = wpList.iterator();
		assertEquals(valWp1, calculator.getnextValidWaypoint(iterator));
		assertEquals(valWp2, calculator.getnextValidWaypoint(iterator));
		
		Waypoint[] wpArray2 = {invalWp1, valWp1,invalWp2};
		wpList = new ArrayList<>(Arrays.asList(wpArray2)) ;
		iterator = wpList.iterator();
		assertEquals(valWp1, calculator.getnextValidWaypoint(iterator));
		assertEquals(null, calculator.getnextValidWaypoint(iterator));
		
		Waypoint[] wpArray3 = {invalWp1, invalWp2};
		wpList = new ArrayList<>(Arrays.asList(wpArray3)) ;
		iterator = wpList.iterator();
		assertEquals(null, calculator.getnextValidWaypoint(iterator));
		assertEquals(null, calculator.getnextValidWaypoint(iterator));
		
		
		Waypoint[] wpArray4 = {valWp1, invalWp1,invalWp2,invalWp3, valWp2};
		wpList = new ArrayList<>(Arrays.asList(wpArray4)) ;
		iterator = wpList.iterator();
		assertEquals(valWp1, calculator.getnextValidWaypoint(iterator));
		assertEquals(valWp2, calculator.getnextValidWaypoint(iterator));
		
		Waypoint[] wpArray5 = {invalWp1,invalWp2,invalWp3, valWp2};
		wpList = new ArrayList<>(Arrays.asList(wpArray5)) ;
		iterator = wpList.iterator();
		assertEquals(valWp2, calculator.getnextValidWaypoint(iterator));
		assertEquals(null, calculator.getnextValidWaypoint(iterator));
		
		Waypoint[] wpArray6 = {valWp1, valWp2,valWp3};
		wpList = new ArrayList<>(Arrays.asList(wpArray6)) ;
		iterator = wpList.iterator();
		assertEquals(valWp1, calculator.getnextValidWaypoint(iterator));
		assertEquals(valWp2, calculator.getnextValidWaypoint(iterator));
		
		Waypoint[] wpArray7 = {valWp1};
		wpList = new ArrayList<>(Arrays.asList(wpArray7)) ;
		iterator = wpList.iterator();
		assertEquals(valWp1, calculator.getnextValidWaypoint(iterator));
		assertEquals(null, calculator.getnextValidWaypoint(iterator));
		
		wpList = new ArrayList<>() ;
		iterator = wpList.iterator();
		assertEquals(null, calculator.getnextValidWaypoint(iterator));
		assertEquals(null, calculator.getnextValidWaypoint(iterator));
	}
	
	@Test
	public void testGetInsuranceDataFromWaypoint_NoValue() {
		ArrayList<Waypoint> wpList = new ArrayList<Waypoint>();
		InsuranceData expData = new InsuranceData();
		InsuranceData actData = calculator.getInsuranceDataFromWaypoint(wpList);
		assertEquals(expData, actData);
	}
	
	@Test
	public void testGetInsuranceDataFromWaypoint_1Value() {
		ArrayList<Waypoint> wpList = new ArrayList<Waypoint>();
		InsuranceData expData, actData;
		
		Waypoint valWp1 = new Waypoint(LocalDateTime.parse("2016-06-21T12:00:00.000"),
				59.334, 18.0667, 6.3889, 8.33);
		Waypoint invalWp1 = new Waypoint(LocalDateTime.parse("2016-06-21T12:00:00.000"),
				59.334, -188.0667, 6.3889, 8.33);
		
		// [validWp] => no insurance data as only single valid point
		wpList = new ArrayList<Waypoint>();
		wpList.add(valWp1);
		expData = new InsuranceData();
		actData = calculator.getInsuranceDataFromWaypoint(wpList);
		assertEquals(expData, actData);
		
		// [validWp,invalidWp] => no insurance data as only single valid point
		wpList = new ArrayList<Waypoint>();
		wpList.add(valWp1);
		wpList.add(invalWp1);
		expData = new InsuranceData();
		actData = calculator.getInsuranceDataFromWaypoint(wpList);
		assertEquals(expData, actData);
		
		// [invalidWp,validWp] => no insurance data as only single valid point
		wpList = new ArrayList<Waypoint>();
		wpList.add(invalWp1);
		wpList.add(valWp1);
		expData = new InsuranceData();
		actData = calculator.getInsuranceDataFromWaypoint(wpList);
		assertEquals(expData, actData);
		
	}
	
	@Test
	public void testGetInsuranceDataFromWaypoint_2Value() {
		
		//To test insurance data points, speed value generated from 
		//uniformly accelerated motion used because data can be easily
		//calculated
		InsuranceData expData, actData;
		ArrayList<Object> res= get2ValueWpList();
		@SuppressWarnings("unchecked")
		ArrayList<Waypoint> wpList = (ArrayList<Waypoint>) res.get(0);
		expData = (InsuranceData) res.get(1);
		CalcMethod prevMethod = calculator.calcMethod;
		calculator.calcMethod = CalcMethod.U_ACC;
		actData = calculator.getInsuranceDataFromWaypoint(wpList);
		assertEquals(expData, actData);
		calculator.calcMethod = prevMethod;
		

	}

	private ArrayList<Object> get2ValueWpList() {
		ArrayList<Waypoint> wpList;
		InsuranceData expData;
		Waypoint valWp1, valWp2;
		
		valWp1 = new Waypoint(LocalDateTime.parse("2016-06-21T12:00:00.000"),
				59.334, 18.0667, 30, 20);
		valWp2 = new Waypoint(LocalDateTime.parse("2016-06-21T12:00:05.000"),
				59.3337, 18.0662, 10, 20);
		wpList = new ArrayList<Waypoint>();
		wpList.add(valWp1);
		wpList.add(valWp2);
		ArrayList<Object> res = new ArrayList<>();
		res.add(wpList);
		expData = new InsuranceData( 62.5, Duration.ofMillis(2500),
				100,Duration.ofSeconds(5)) ;
		res.add(expData);
		return res;
	}
	
	private ArrayList<Object> getMultiValueWpList() {
		ArrayList<Waypoint> wpList = new ArrayList<Waypoint>();
		InsuranceData expData;
		wpList.add(new Waypoint(LocalDateTime.parse("2016-06-21T12:00:00.000"),
				59.334, 18.0667, 5, 25.15));
		wpList.add(new Waypoint(LocalDateTime.parse("2016-06-21T12:00:05.000"),
				59.3337, 18.0662, 7.3, 25.15));
		wpList.add(new Waypoint(LocalDateTime.parse("2016-06-21T12:00:10.000"),
				59.3331, 18.0664, 35.75, 25.15));
		wpList.add(new Waypoint(LocalDateTime.parse("2016-06-21T12:00:15.000"),
				59.3327, 18.0665, 28.33, 25.15));
		wpList.add(new Waypoint(LocalDateTime.parse("2016-06-21T12:00:20.000"),
				59.3323, 18.0666, 10, 25.15));
		ArrayList<Object> res = new ArrayList<>();
		res.add(wpList);
		expData = new InsuranceData( 240.112, Duration.ofMillis(7730),
				394.4,Duration.ofSeconds(20)) ;
		res.add(expData);
		return res;
	}
	
	@Test
	public void testGetInsuranceDataFromWaypoint_WithValues() {
		
		//To test insurance data points, speed value generated from 
		//uniformly accelerated motion used because data can be easily
		//calculated
		InsuranceData expData, actData;
		ArrayList<Object> res= getMultiValueWpList();
		@SuppressWarnings("unchecked")
		ArrayList<Waypoint> wpList = (ArrayList<Waypoint>) res.get(0);
		expData = (InsuranceData) res.get(1);
		CalcMethod prevMethod = calculator.calcMethod;
		calculator.calcMethod = CalcMethod.U_ACC;
		actData = calculator.getInsuranceDataFromWaypoint(wpList);
		assertEquals(expData, actData);
		calculator.calcMethod = prevMethod;
		
		

	}
	
	@Test
	public void testGetInsuranceDataFromWaypointPath_NoValue() {
		InsuranceData expData = new InsuranceData();
		String dataToWrite = "[]";
		String savePath = testDir + "getIDfromWPpath_noValue.json";
		Path path = Paths.get(savePath);
		try {
			Files.writeString(path, dataToWrite);
		} catch (IOException e) {
			fail("Exception : "+ e.getMessage() );
		}
		calculator.calcMethod = CalcMethod.U_ACC;
		InsuranceData actData = calculator.getInsuranceDataFromWaypoint(path);
		assertEquals(expData, actData);
	}
	
	private ArrayList<Object> get1ValueWpString() {
		ArrayList<Object> res = new ArrayList<>();
		String dataToWrite = "[\r\n"
				+ "  {\r\n"
				+ "    \"timestamp\": \"2016-06-21T12:00:00.000Z\",\r\n"
				+ "    \"position\": {\r\n"
				+ "      \"latitude\": 59.334,\r\n"
				+ "      \"longitude\": 18.0667 \r\n"
				+ "    },\r\n"
				+ "    \"speed\": 5,\r\n"
				+ "    \"speed_limit\": 25.15\r\n"
				+ "  }]";
		InsuranceData expData = new InsuranceData() ;
		
		res.add(dataToWrite);
		res.add(expData);
		return res;
	}
	
	private ArrayList<Object> getMultiValueWpString() {
		ArrayList<Object> res = new ArrayList<>();
		String dataToWrite = "[\r\n"
				+ "  {\r\n"
				+ "    \"timestamp\": \"2016-06-21T12:00:00.000Z\",\r\n"
				+ "    \"position\": {\r\n"
				+ "      \"latitude\": 59.334,\r\n"
				+ "      \"longitude\": 18.0667 \r\n"
				+ "    },\r\n"
				+ "    \"speed\": 5,\r\n"
				+ "    \"speed_limit\": 25.15\r\n"
				+ "  },\r\n"
				+ "  {\r\n"
				+ "    \"timestamp\": \"2016-06-21T12:00:05.000Z\",\r\n"
				+ "    \"position\": {\r\n"
				+ "      \"latitude\": 59.3337,\r\n"
				+ "      \"longitude\": 18.0662\r\n"
				+ "    },\r\n"
				+ "    \"speed\": 7.3,\r\n"
				+ "    \"speed_limit\": 25.15\r\n"
				+ "  },\r\n"
				+ "  {\r\n"
				+ "    \"timestamp\": \"2016-06-21T12:00:10.000Z\",\r\n"
				+ "    \"position\": {\r\n"
				+ "      \"latitude\": 59.3331,\r\n"
				+ "      \"longitude\": 18.0664\r\n"
				+ "    },\r\n"
				+ "    \"speed\": 35.75,\r\n"
				+ "    \"speed_limit\": 25.15\r\n"
				+ "  },\r\n"
				+ "  {\r\n"
				+ "    \"timestamp\": \"2016-06-21T12:00:15.000Z\",\r\n"
				+ "    \"position\": {\r\n"
				+ "      \"latitude\": 59.3327,\r\n"
				+ "      \"longitude\": 18.0665\r\n"
				+ "    },\r\n"
				+ "    \"speed\": 28.33,\r\n"
				+ "    \"speed_limit\": 25.15\r\n"
				+ "  },\r\n"
				+ "  {\r\n"
				+ "    \"timestamp\": \"2016-06-21T12:00:20.000Z\",\r\n"
				+ "    \"position\": {\r\n"
				+ "      \"latitude\": 59.3323,\r\n"
				+ "      \"longitude\": 18.0666\r\n"
				+ "    },\r\n"
				+ "    \"speed\": 10,\r\n"
				+ "    \"speed_limit\": 25.15\r\n"
				+ "  }\r\n"
				+ "]\r\n";
		InsuranceData expData = new InsuranceData( 240.112, Duration.ofMillis(7730),
				394.4,Duration.ofSeconds(20));
		res.add(dataToWrite);
		res.add(expData);
		return res;
	}
	
	@Test
	public void testGetInsuranceDataFromWaypointPath_1Value() {
		ArrayList<Object> res= get1ValueWpString();
		String dataToWrite = (String) res.get(0);
		InsuranceData expData = (InsuranceData) res.get(1);
		String savePath = testDir + "getIDfromWPpath_1Value.json";
		Path path = Paths.get(savePath);
		try {
			Files.writeString(path, dataToWrite);
		} catch (IOException e) {
			fail("Exception : "+ e.getMessage() );
		}
		calculator.calcMethod = CalcMethod.U_ACC;
		InsuranceData actData = calculator.getInsuranceDataFromWaypoint(path);
		assertEquals(expData, actData);
	}
	
	@Test
	public void testGetInsuranceDataFromWaypointPath_MultiValue() {
		ArrayList<Object> res= getMultiValueWpString();
		String dataToWrite = (String) res.get(0);
		InsuranceData expData = (InsuranceData) res.get(1);
		String savePath = testDir + "getIDfromWPpath_MultiValue.json";
		Path path = Paths.get(savePath);
		try {
			Files.writeString(path, dataToWrite);
		} catch (IOException e) {
			fail("Exception : "+ e.getMessage() );
		}
		calculator.calcMethod = CalcMethod.U_ACC;
		InsuranceData actData = calculator.getInsuranceDataFromWaypoint(path);
		assertEquals(expData, actData);
	}

	private void testSaveIDfromWaypoint(InsuranceData expData, String dataToWrite, Path sourcePath, Path destPath) {
		InsuranceData actData;
		try {
			Files.writeString(sourcePath, dataToWrite);
		} catch (IOException e) {
			fail("Exception : "+ e.getMessage() );
		}
		calculator.calcMethod = CalcMethod.U_ACC;
		assertTrue(calculator.saveInsuranceDataFromWaypoint(sourcePath, destPath));
		try {
			actData = jsonMapper.readValue(destPath.toFile(), InsuranceData.class);
			assertEquals(expData, actData);
		} catch (IOException e) {
			fail("Exception : "+ e.getMessage() );
		}
	}
	
	@Test
	public void testSaveInsuranceDataFromWaypointPath_NoValue() {
		InsuranceData expData = new InsuranceData();
		String dataToWrite = "[]";
		String savePath = testDir + "saveIDfromWPpath_noValue.json";
		Path sourcePath = Paths.get(savePath);
		Path destPath = Paths.get(testDir + "saveIDfromWPpath_noValue_res.json");
		testSaveIDfromWaypoint(expData, dataToWrite, sourcePath, destPath);
		
	}
	
	@Test
	public void testSaveInsuranceDataFromWaypointPath_1Value() {
		ArrayList<Object> res= get1ValueWpString();
		String dataToWrite = (String) res.get(0);
		InsuranceData expData = (InsuranceData) res.get(1);
		String savePath = testDir + "saveIDfromWPpath_1Value.json";
		Path sourcePath = Paths.get(savePath);
		Path destPath = Paths.get(testDir + "saveIDfromWPpath_1Value_res.json");
		testSaveIDfromWaypoint(expData, dataToWrite, sourcePath, destPath);
		
	}
	
	@Test
	public void testSaveInsuranceDataFromWaypointPath_MultiValue() {
		ArrayList<Object> res= getMultiValueWpString();
		String dataToWrite = (String) res.get(0);
		InsuranceData expData = (InsuranceData) res.get(1);
		String savePath = testDir + "saveIDfromWPpath_MultiValue.json";
		Path sourcePath = Paths.get(savePath);
		Path destPath = Paths.get(testDir + "saveIDfromWPpath_MultiValue_res.json");
		testSaveIDfromWaypoint(expData, dataToWrite, sourcePath, destPath);
		
	}


	
//	@Test
//	public void testSaveInsuranceDataFromWaypointPath_1Value() {
//		ArrayList<Object> res= get1ValueWpString();
//		String dataToWrite = (String) res.get(0);
//		InsuranceData expData = (InsuranceData) res.get(1);
//		String savePath = testDir + "getIDfromWPpath_1Value.json";
//		Path path = Paths.get(savePath);
//		try {
//			Files.writeString(path, dataToWrite);
//		} catch (IOException e) {
//			fail("Exception : "+ e.getMessage() );
//		}
//		calculator.calcMethod = CalcMethod.U_ACC;
//		InsuranceData actData = calculator.getInsuranceDataFromWaypoint(path);
//		assertEquals(expData, actData);
//	}
}

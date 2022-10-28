/**
 * 
 */
package com.riskyinsurance.insurancedatacalculator;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class InsuranceDataCalculatorTest {

	static InsuranceDataCalculator calculator;
	static ObjectMapper jsonMapper;
	static String testDir = "insurance-data-calculator-test-data/";
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		calculator = new InsuranceDataCalculator();
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

	
	@Test
	public void testWriteDataToJsonPath_DefaultValues() {
		InsuranceData dataToWrite, dataRead;
		Path path;
		String savePath = testDir + "sample1_res.json";
		boolean resFlag;
		
		dataToWrite = new InsuranceData();
		path = Paths.get(savePath);
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
	public void testWriteDataToJsonPath_WithValues() {
		InsuranceData dataToWrite, dataRead;
		Path path;
		String savePath = testDir + "sample2_res.json";
		boolean resFlag;
		
		dataToWrite = new InsuranceData(new BigDecimal(15.0), Duration.ofMillis(5002)
				, new BigDecimal(150.0), Duration.ofMillis(15002) );
		path = Paths.get(savePath);
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
	public void testGetWaypointListFromPath_WithNoValues() {
		
	
	}
	
}

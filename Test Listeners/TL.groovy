import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import com.kazurayam.materials.MaterialRepository
import com.kazurayam.materials.MaterialRepositoryFactory
import com.kazurayam.visualtesting.GlobalVariableHelpers as GVH
import com.kms.katalon.core.annotation.BeforeTestCase
import com.kms.katalon.core.annotation.BeforeTestSuite
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.context.TestCaseContext
import com.kms.katalon.core.context.TestSuiteContext

import internal.GlobalVariable as GlobalVariable

class TL {

	private Path materialsDir = Paths.get(RunConfiguration.getProjectDir()).resolve('Materials')
	private Path reportFolder = Paths.get(RunConfiguration.getReportFolder())

	private static String CURRENT_TESTSUITE_ID = 'CURRENT_TESTSUITE_ID'
	private static String CURRENT_EXECUTION_PROFILE = 'CURRENT_EXECUTION_PROFILE'
	private static String CURRENT_TESTSUITE_TIMESTAMP = 'CURRENT_TESTSUITE_TIMESTAMP'
	private static String MATERIAL_REPOSITORY = 'MATERIAL_REPOSITORY'
	
	/**
	 *
	 */
	@BeforeTestCase
	def beforeTestCase(TestCaseContext testCaseContext) {
		if (!GVH.isGlobalVariablePresent(CURRENT_TESTSUITE_ID)) {
			GVH.addGlobalVariable(CURRENT_TESTSUITE_ID, '_')
		}
	}

	/**
	 *
	 */
	@BeforeTestSuite
	def beforeTestSuite(TestSuiteContext testSuiteContext) {
		String testSuiteId = testSuiteContext.getTestSuiteId()
		String executionProfile = RunConfiguration.getExecutionProfile()
		String testSuiteTimestamp = reportFolder.getFileName().toString()
		GVH.addGlobalVariable(CURRENT_TESTSUITE_ID, testSuiteId)
		GVH.addGlobalVariable(CURRENT_EXECUTION_PROFILE, executionProfile)
		GVH.addGlobalVariable(CURRENT_TESTSUITE_TIMESTAMP, testSuiteTimestamp)

		Files.createDirectories(materialsDir)
		MaterialRepository mr = MaterialRepositoryFactory.createInstance(materialsDir)
		mr.markAsCurrent(testSuiteId, executionProfile, testSuiteTimestamp)
		def tsr = mr.ensureTSuiteResultPresent(
			testSuiteId, executionProfile, testSuiteTimestamp)
		GVH.addGlobalVariable("MATERIAL_REPOSITORY", mr)
	}
}

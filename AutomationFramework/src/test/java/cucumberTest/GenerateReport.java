package cucumberTest;

import net.masterthought.cucumber.ReportBuilder;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import baseClass.AppTest;

public class GenerateReport {
	public static void GenerateMasterthoughtReport(){
        try{
          //  String RootDir = System.getProperty("user.dir");
            File reportOutputDirectory = new File("target/Masterthought");
          
            List<String> list = new ArrayList<String>();
		    for(String test:AppTest.tests){
            	System.out.println("Test Name" +test);
				list.add("target/"+test +".json");
            }
			System.out.println("List Size" + list.size());
            
       
            String pluginUrlPath = "";
            String buildNumber = "";
            String buildProject = "cucumber-jvm";
            boolean skippedFails = true;
            boolean pendingFails = true;
            boolean undefinedFails = true;
            boolean missingFails = true;
            boolean flashCharts = true;
            boolean runWithJenkins = false;
            boolean highCharts = false;
            boolean parallelTesting = true;
    
            ReportBuilder reportBuilder = new ReportBuilder(list, reportOutputDirectory, pluginUrlPath, buildNumber,
                    buildProject, skippedFails, pendingFails, undefinedFails, missingFails, flashCharts, runWithJenkins,
                    highCharts, parallelTesting);

            reportBuilder.generateReports();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

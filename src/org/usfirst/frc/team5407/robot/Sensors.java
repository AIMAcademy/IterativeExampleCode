package org.usfirst.frc.team5407.robot;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Preferences;


public class Sensors {
	// declare you variable up here
	AnalogInput ana_IRBucketSensor;
	AnalogInput ana_StringPotentiometerWedge;
	AnalogInput ana_SonicFrontSensor;

	int i_IRBucketSensor;
	int i_SonicFrontSensor;
	int i_StringPotentiometerWedge;
	int ip_TooCloseToWallDistance;
	
	
	// class Contructor initialize you variables here
    public Sensors(int ANAConnector_StringPot, 
    			   int ANAConnector_IRBucket,
		   		   int ANAConnector_SonicFrontSensor) {
    	ana_StringPotentiometerWedge = 	new AnalogInput(ANAConnector_StringPot);
    	ana_IRBucketSensor = 			new AnalogInput(ANAConnector_IRBucket);
    	ana_SonicFrontSensor = 			new AnalogInput(ANAConnector_SonicFrontSensor);

    	i_IRBucketSensor = 0;
    	ip_TooCloseToWallDistance = 0;
    	i_SonicFrontSensor = 0;
    }

    
    public void addTelemetryHeaders(LCTelemetry telem ){
		telem.addColumn("S IR Bucket Sensor");
    	telem.addColumn("S Too Close To Wall");
		telem.addColumn("S Sonic Front Sensor");
    }

    public void writeTelemetryValues(LCTelemetry telem ){
		telem.saveInteger("S IR Bucket Sensor",		this.i_IRBucketSensor);
		telem.saveInteger("S Too Close To Wall", 	this.ip_TooCloseToWallDistance);
		telem.saveInteger("S Sonic Front Sensor", 	this.i_SonicFrontSensor);
    }



    // This will read all the inputs, cook them and save them to the appropriate variables.
    public void readValues() {   
    	
    	i_IRBucketSensor = ana_IRBucketSensor.getAverageValue();
    	i_StringPotentiometerWedge = ana_StringPotentiometerWedge.getAverageValue();
    	i_SonicFrontSensor = this.ana_SonicFrontSensor.getAverageValue();
    }
    
	// Show what variables we want to the SmartDashboard
	public void outputToDashboard(boolean b_MinDisplay)  {
		SmartDashboard.putNumber("S_WedgePosition", i_StringPotentiometerWedge);
		SmartDashboard.putNumber("S_SonicFrontSensor", this.i_SonicFrontSensor);
	}

	// Pull preferences into our variables.
	public void updatePreferences()  {

		ip_TooCloseToWallDistance = Preferences.getInstance().getInt("S_TooCloseToWall", 300);
        //pd_TowerSlowSpeed = Preferences.getInstance().getDouble("TowerSlowSpeed", .2);

	}


	public void writeTelemetry() {

		
	}

	

}

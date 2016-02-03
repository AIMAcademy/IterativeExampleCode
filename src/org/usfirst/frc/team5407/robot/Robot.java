package org.usfirst.frc.team5407.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	// Declare your variables up here

	Auton auton;
	Inputs inputs; 
	Sensors sensors;
	RobotBase robotbase;
	Mecanum mecanum;
	Wedge wedge;
	Tower tower;
	LCTelemetry telem;

	boolean bp_MinDisplay;
	
	/**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {

    	telem = new LCTelemetry();		// get telemetry going
    	
    									// Physical connections
    	auton = new Auton();
    	inputs = new Inputs( 0,			// USB Joystick Driver Left
    						 1			// USB Joystick Driver Right
    						);			// too many buttons to put here and these are not physical connections
    	
    	sensors =  new Sensors( 1, 		//ANAConnector IRBucketSensor,
    							2, 		//ANAConnector_StringPotSensor, 
    							3 		//ANAConnector_SonicFrontSensor) {			
    									);
    	
    	mecanum = new Mecanum();
    	
    	robotbase = new RobotBase( 0,  //PWM Left Front Motor
    							   1,  //PWM Right Front Motor
    							   2,  //PWM Left Front Motor
    							   3   //PWM Right Rear Motor 
    					 				);
    	
    	tower = new Tower( 4 );		   //PWM Tower Winch Motor 
    	
    	wedge = new Wedge( 5 ); 	   //PWM Wedge Winch Motor

    	bp_MinDisplay = true;

    	
    	// add the telemetry fields
    	inputs.addTelemetryHeaders( telem );
    	robotbase.addTelemetryHeaders( telem );
    	wedge.addTelemetryHeaders( telem );
    	sensors.addTelemetryHeaders( telem );
    	auton.addTelemetryHeaders( telem );
    	
    }

    /**
     * This function is run once each time the robot enters disable mode
     */
    public void disabledInit() {

    	telem.restartTimer();
    	
    }

    /**
     * This function is called periodically during disable
     */
    public void disabledPeriodic() {
    	
    	checkMinDisplay();
    	
    	// call the preferences for all parts we do it here so we are not slowed down
    	inputs.updatePreferences();
    	auton.updatePreferences();
    	robotbase.updatePreferences();
    	wedge.updatePreferences();
    	sensors.updatePreferences();
    	telem.updatePreferences();

    	// we go through all of this to see things change
    	// but we are disabled so no outout will occur. 
    	inputs.readValues();				// Zero out the human inputs.
    	sensors.readValues();				// read the sensors

    	robotThink();						// make any decisions here about what to do if needed 

    	robotbase.update();
    	tower.update(inputs, sensors);
    	wedge.update(inputs, sensors);

    	
    	SmartDashboard.putBoolean("R_MinDisplay(bool)", bp_MinDisplay);
    	inputs.outputToDashboard(this.bp_MinDisplay);
    	sensors.outputToDashboard(this.bp_MinDisplay);
    	auton.outputToDashboard(this.bp_MinDisplay);
    	robotbase.outputToDashboard(this.bp_MinDisplay);
    	telem.outputToDashBoard(this.bp_MinDisplay);		// do this only if disabled. Do not waste cycles in teleop. 
    	
    	if( inputs.b_SaveTelemetry == true )
    		telem.saveSpreadSheet();						// once done you cannot save more data there. 
    	
    }
    

    /**
     * This function is run once each time the robot enters autonomous mode
     */
    public void autonomousInit() {

    	telem.restartTimer();
    	auton.updatePreferences();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {

    	inputs.zeroInputs();				// Zero out the human inputs.
    	sensors.readValues();				// read the sensors
    	
    	auton.dispatcher(inputs, sensors, wedge);  // send in inputs and sensors and others to decide what to do

    	robotThink();						// make any decisions here about what to do if needed 

    	robotbase.update();
    	tower.update(inputs, sensors);
    	wedge.update(inputs, sensors);
    	
    	
    	SmartDashboard.putBoolean("R_DebugMode(bool)", bp_MinDisplay);
    	inputs.outputToDashboard(this.bp_MinDisplay);
    	sensors.outputToDashboard(this.bp_MinDisplay);
    	auton.outputToDashboard(this.bp_MinDisplay);
    	robotbase.outputToDashboard(this.bp_MinDisplay);
    	tower.outputToDashboard(this.bp_MinDisplay);
    	wedge.outputToDashboard(this.bp_MinDisplay);

    	inputs.writeTelemetryValues(telem);
    	robotbase.writeTelemetryValues(telem);
    	wedge.writeTelemetryValues(telem);
    	telem.writeRow();					// write a record now

    }
    
    /**
     * This function is called once each time the robot enters tele-operated mode
     */
    public void teleopInit(){

    	telem.restartTimer();

    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
    	
    	inputs.readValues();
    	sensors.readValues();

    	robotThink();

    	robotbase.update();
    	tower.update(inputs, sensors);
    	wedge.update(inputs, sensors);

    	SmartDashboard.putBoolean("R_DebugMode(bool)", bp_MinDisplay);		// tell us if minDisplay is active
    	inputs.outputToDashboard(this.bp_MinDisplay);
    	sensors.outputToDashboard(this.bp_MinDisplay);
    	robotbase.outputToDashboard(this.bp_MinDisplay);
    	tower.outputToDashboard(this.bp_MinDisplay);
    	wedge.outputToDashboard(this.bp_MinDisplay);
    	auton.outputToDashboard(this.bp_MinDisplay);

    	
    	inputs.writeTelemetryValues(telem);
    	robotbase.writeTelemetryValues(telem);
    	wedge.writeTelemetryValues(telem);
    	telem.writeRow();					
    }
    
    public void robotThink() {
    	
    	// test to see if we are too close to the wall
    	/*if( (inputs.d_RightDrivePower > 0.0 ||				// are we moving forward right or left 
    			inputs.d_LeftDrivePower > 0.0) && 
    				sensors.i_IRBucketSensor > sensors.pi_TooCloseToWallDistance){		// are we too close to the wall
        	inputs.d_LeftDrivePower = 0.0;					// if we are then set power to stop
        	inputs.d_RightDrivePower = 0.0;
    	}*/

    	robotbase.d_LeftFrontDrivePower  = 0.0;				// always do this so robot will stop if no change or bad coding. 
    	robotbase.d_RightFrontDrivePower = 0.0;
    	robotbase.d_LeftRearDrivePower   = 0.0;
    	robotbase.d_RightRearDrivePower  = 0.0;

    		
    	if( inputs.i_SelectDriveType == inputs.kArcadeDrive) {			// Archade Drive

        	robotbase.d_LeftFrontDrivePower = inputs.d_DriverArchadePower + inputs.d_DriverArchadeTurn;
        	robotbase.d_RightFrontDrivePower = inputs.d_DriverArchadePower - inputs.d_DriverArchadeTurn;
        	robotbase.d_LeftRearDrivePower = inputs.d_DriverArchadePower + inputs.d_DriverArchadeTurn;
        	robotbase.d_RightRearDrivePower = inputs.d_DriverArchadePower - inputs.d_DriverArchadeTurn;
    		
    	} else if( inputs.i_SelectDriveType == inputs.kMecanumDive) {			// mecannun drive

        	// Example using the Mecanum class. You need to define the inputs DriverDirection, Power and Crab
        	robotbase.d_LeftFrontDrivePower  = mecanum.GetMecanumPower( mecanum.kMecanumLeftFront, inputs.d_DriverMecanumTurn, inputs.d_DriverMecanumPower, inputs.d_DriverMecanumCrab );
        	robotbase.d_RightFrontDrivePower = mecanum.GetMecanumPower( mecanum.kMecanumRightFront,  inputs.d_DriverMecanumTurn, inputs.d_DriverMecanumPower, inputs.d_DriverMecanumCrab );
        	robotbase.d_LeftRearDrivePower   = mecanum.GetMecanumPower( mecanum.kMecanumLeftRear, inputs.d_DriverMecanumTurn, inputs.d_DriverMecanumPower, inputs.d_DriverMecanumCrab );
        	robotbase.d_RightRearDrivePower  = mecanum.GetMecanumPower( mecanum.kMecanumRightRear,  inputs.d_DriverMecanumTurn, inputs.d_DriverMecanumPower, inputs.d_DriverMecanumCrab );

    	} else {			// Tank Drive
    		
        	// here we update robot base outputs from inputs.
        	robotbase.d_LeftFrontDrivePower  = inputs.d_LeftTankDrivePower;
        	robotbase.d_RightFrontDrivePower = inputs.d_RightTankDrivePower;
        	robotbase.d_LeftRearDrivePower   = inputs.d_LeftTankDrivePower;
        	robotbase.d_RightRearDrivePower  = inputs.d_RightTankDrivePower;
    	}

    }

    public void checkMinDisplay(){
    	
    	this.bp_MinDisplay = Preferences.getInstance().getBoolean("R_MinDisplay(bool)", true);
    
	}

}

package org.usfirst.frc.team5407.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class Auton {

	Timer tim_AutonTimer, tim_StepTimer;	// timers for auton
	boolean b_Started;						// tells us that we have started auton

	int ip_ProgramNumber;					// what auton program will we run
	String s_ProgramDescription; 			// describe the auton program

	int i_Step;								// indicates what step we are on
	boolean b_StepIsSetup;					// used to allow a step to set specific things when it starts
	String s_StepDescription;				// describe the step. Can be saved in telemetry

	boolean b_IsDone;						// flag to indicate our auton program is done
											// autonomous may not be over but we may be done. 
	

    /**
     * Constructor
     */
    public Auton() {
    	tim_AutonTimer = new Timer();		// timer used to show how long we are in auton. 
    	tim_StepTimer = new Timer();		// timer used by the steps for various things. 
    	
    	b_Started = false;
    	
    	i_Step = 0; 
    	b_StepIsSetup = false;
    	s_StepDescription = "Init";

    	ip_ProgramNumber = 1;		// set the default auton program number
    	s_ProgramDescription = "Init"; 

    	b_IsDone = false; 
    	
    }
    
    public void start(){
    	tim_AutonTimer.start();
    	tim_AutonTimer.reset();
    	tim_StepTimer.start();
    	b_Started = true;
    }

    public void dispatcher(Inputs inputs, Sensors sensors, Wedge wedge) {
    	
    	if( this.b_Started == false)
    		this.start();
    	
//    	if( b_IsDone == true)
//  		return;
    	
    	switch (ip_ProgramNumber) {
	    	case 1: 
	    		autoProgram1(inputs, sensors);
	    		break;

	    	case 2:
	    		autoProgram2(inputs, sensors, wedge);
	    		break;

	    	default:
	    		b_IsDone = true;
	    		s_StepDescription = "Invalid program.";
	    		this.tim_StepTimer.stop();
	    		this.tim_AutonTimer.stop();
	    		break;
    	}
    }			    
    
    
    
    private void autoProgram1(Inputs inputs, Sensors sensors) {

    	inputs.zeroInputs();	// this is done in autoperiodic but cannot hurt here.

    	inputs.i_SelectDriveType = inputs.kTankDrive;  // this is the default for this auton program
													   // only needs to be set here. 
    	
    	switch(i_Step){
    	case 0:
    		// 0 always "initializes" the auton program. 
    		// set or reset all the stuff that it will need for this auton program 
        	s_ProgramDescription = "Drive1";		// set the program name, can be used in telemetry
    		s_StepDescription = "Init";				// set the step name, can be used on creen or in telemetry
    		tim_StepTimer.start();					// start the step timer just to be usre it started
    		tim_StepTimer.reset();					// reset the step timer just to be sure it is 0.0
    		b_StepIsSetup = false;					// set this so next step will do its own setup stuff
    		i_Step++;								// increment to call next step
    		break;									// skip out so it passes through code and does nothing
    												// if doing telemetry will write out this step and show description 
    		
    	case 1:
    		if(b_StepIsSetup == false ){			// first time this will be false, setup stuff for this step only
        		s_StepDescription = "Forward, 2 sec";
        		tim_StepTimer.reset();				// reset time, whocares that it is already done
        		b_StepIsSetup = true;				// mark this setup as done, again for THIS step
    		}

    		// execute the settings for this step
    		inputs.d_LeftTankDrivePower 	= .25;	// set inputs to 1/4 power just like humans would. 
    		inputs.d_RightTankDrivePower 	= .25;	// set inputs to 1/4 power just like humans would.
    		
    		// test to see if the step is complete
    		if( tim_StepTimer.get() > 2.0) {		// chec the time, next step after 2 seconds
        		b_StepIsSetup = false;				// step complete, mark setup as false
        		i_Step++;							// increment so we move to the next step
    		}
    		break;

    	case 2:
    		if(b_StepIsSetup == false ){			// setup the step 
        		s_StepDescription = "Stop, wait 2sec";
        		tim_StepTimer.reset();
        		b_StepIsSetup = true;				// mark step as set up
    		}
    												// not really needed as zero_inputs does this, but we want clarity
    		inputs.d_LeftTankDrivePower 	= 0.0;	// stop
    		inputs.d_RightTankDrivePower 	= 0.0;	// stop
    		
    		if( tim_StepTimer.get() > 2.0) {		// next step after 2 seconds
        		b_StepIsSetup = false;
        		i_Step++;
    		}
    		break;

    	case 3:
    		if(b_StepIsSetup == false ){
        		s_StepDescription = "Spin Left";
        		tim_StepTimer.reset();
        		b_StepIsSetup = true;
    		}
    		
    		inputs.d_LeftTankDrivePower 	= -.25;
    		inputs.d_RightTankDrivePower 	=  .25;
    		
    		if( tim_StepTimer.get() > 2.0) {		// next step after 2 seconds
        		b_StepIsSetup = false;
        		i_Step++;
    		}
    		break;

    	case 4:
    		if(b_StepIsSetup == false ){
        		s_StepDescription = "Stop, wait 2sec";
        		tim_StepTimer.reset();
        		b_StepIsSetup = true;
    		}
    												// not needed as zero inputs does this
    		inputs.d_LeftTankDrivePower 	= 0.0;		// stop
    		inputs.d_RightTankDrivePower 	= 0.0;		// stop
    		
    		if( tim_StepTimer.get() > 2.0) {		// next step after 2 seconds
        		b_StepIsSetup = false;
        		i_Step++;
    		}
    		break;

    	case 5:
    		if(b_StepIsSetup == false ){
        		s_StepDescription = "Backup";
        		tim_StepTimer.reset();
        		b_StepIsSetup = true;
    		}
    											
    		inputs.d_LeftTankDrivePower 	= -.25;		// reverse 1/4 speed
    		inputs.d_RightTankDrivePower 	= -.25;		// reverse 1/4 speed
    		
    		if( tim_StepTimer.get() > 2.0) {		// next step after 2 seconds
        		b_StepIsSetup = false;
        		i_Step++;
    		}
    		break;

    	case 6:
    		if(b_StepIsSetup == false ){
        		s_StepDescription = "Done";
        		this.tim_StepTimer.stop();
	    		this.tim_AutonTimer.stop();
        		b_StepIsSetup = true;
    		}
    											
       		i_Step = 99;							// force us to drop into default
    		break;

    	default:		// if the step number is not called out above it falls here.
    					// WARNING: when doen, do not set step to 0 or program will run again. 
    		this.tim_StepTimer.stop();
    		this.tim_AutonTimer.stop();
    		b_IsDone = true;
    		
    		
    	}
    	
    }
    		
	// we need to see the wedge variables do we need wedge passed to us.
    private void autoProgram2(Inputs inputs, Sensors sensors, Wedge wedge) {
    	inputs.zeroInputs();	// this is done in autoperiodic but cannot hurt here.
		inputs.i_SelectDriveType = inputs.kTankDrive;
    	
    	switch(i_Step){
    	case 0:
        	s_ProgramDescription = "DriveWedge"; 
    		s_StepDescription = "Init";
    		tim_StepTimer.reset();
    		b_StepIsSetup = false;
    		i_Step++;
    		break;

    	case 1:
    		if(b_StepIsSetup == false ){
        		s_StepDescription = "Position Wedge";
        		b_StepIsSetup = true;
        		wedge.clear();							// clear the b_In_position flag in the wedge. 
    		}
    													// imitate what the humans would do
    		inputs.i_WedgeDesiredPosition = 1800;		// virtual operator set the position
    		inputs.b_UseWedgeAdjuster = true;			// virtual operator has "pressed" the button
    		
    		if( wedge.b_InPosition == true){			// this flag is set when the wedge is in position
        		b_StepIsSetup = false;
        		i_Step++;
    		}
    		break;
    		
    	case 2:
    		if(b_StepIsSetup == false ){
        		s_StepDescription = "Move Forward";
        		b_StepIsSetup = true;
    		}
    		
    		inputs.d_LeftTankDrivePower 	= .40;
    		inputs.d_RightTankDrivePower 	= .40;

    		if( sensors.i_SonicFrontSensor < 500){		// check the sensor, too close then next step
        		b_StepIsSetup = false;
        		i_Step++;
    		}
    		break;

    	case 3:
    		if(b_StepIsSetup == false ){
        		s_StepDescription = "Spin Left";
        		tim_StepTimer.reset();
        		b_StepIsSetup = true;
    		}
    		
    		inputs.d_LeftTankDrivePower 	= -.25;
    		inputs.d_RightTankDrivePower 	=  .25;
    		
    		if( tim_StepTimer.get() > 4.0) {
        		b_StepIsSetup = false;
        		i_Step++;
    		}
    		break;

    	case 4:
    		if(b_StepIsSetup == false ){
        		s_StepDescription = "Done";
        		this.tim_StepTimer.stop();
	    		this.tim_AutonTimer.stop();
        		b_StepIsSetup = true;
    		}
    											
       		i_Step = 99;							// force us to drop into default
    		break;									// no more steps will be run. 

    	default:
    										// Warning: Do not reset step to 0 or program runs again.
    		this.tim_StepTimer.stop();
    		this.tim_AutonTimer.stop();
    		b_IsDone = true;
    		
    	}

    }

    
	// Pull preferences into our variables.
	public void updatePreferences()  {

		ip_ProgramNumber = Preferences.getInstance().getInt("A_ProgramNumber", 2); // default is program 2
		
	}

    public void addTelemetryHeaders(LCTelemetry telem ){
		telem.addColumn("A Program Number");
		telem.addColumn("A Started");
		telem.addColumn("A Is Done"); 
		telem.addColumn("A Auton Timer"); 
		telem.addColumn("A Program Desc"); 
		telem.addColumn("A Step Desc");
		telem.addColumn("A Step"); 
		telem.addColumn("A Step Timer"); 
    }

    public void writeTelemetryValues(LCTelemetry telem ){
		telem.saveInteger("A Program Number",this.ip_ProgramNumber);
		telem.saveTrueBoolean("A Started",this.b_Started);
		telem.saveTrueBoolean("A Is Done",this.b_IsDone); 
		telem.saveDouble("A Auton Timer",this.tim_AutonTimer.get());
		telem.saveString("A Program Desc",this.s_ProgramDescription);
		telem.saveString("A Step Desc",this.s_StepDescription);
		telem.saveInteger("A Step",this.i_Step);
		telem.saveDouble("A Step Timer",this.tim_StepTimer.get());
    }		

    public void outputToDashboard(boolean b_MinDisplay){
    	
    	SmartDashboard.putNumber("A_ProgramNumber",this.ip_ProgramNumber );
    	SmartDashboard.putBoolean("A_IsDone", this.b_IsDone);
    	
    	if( b_MinDisplay == false ){
	    	SmartDashboard.putString("A_ProgramName",this.s_ProgramDescription );
	    	SmartDashboard.putString("A_StepName",this.s_StepDescription );
	    	SmartDashboard.putNumber("A_StepNumber",this.i_Step);
	    	SmartDashboard.putNumber("A_StepTime",this.tim_StepTimer.get());
	    	SmartDashboard.putNumber("A_AutonTime",this.tim_StepTimer.get());
    	}
    }
    

}

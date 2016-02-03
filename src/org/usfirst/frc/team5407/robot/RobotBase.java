package org.usfirst.frc.team5407.robot;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Preferences;

public class RobotBase {

	// create your variables
	Double d_LeftFrontDrivePower;
	Double d_RightFrontDrivePower;
	Double d_LeftRearDrivePower;
	Double d_RightRearDrivePower;

	Talon mot_LeftFrontDriveMotor;
	Talon mot_RightFrontDriveMotor;
	Talon mot_LeftRearDriveMotor;
	Talon mot_RightRearDriveMotor;

	Boolean bp_AllowedToDrive;
	
	
    /**
     * This function is run when this class is first created used for any initialization code.
     */
    public RobotBase(int PWMConnector_LeftFrontDriveMotor,
		    		 int PWMConnector_RightFrontDriveMotor,
		    		 int PWMConnector_LeftRearDriveMotor,
		    		 int PWMConnector_RightRearDriveMotor ) {
    	
    	mot_LeftFrontDriveMotor = new Talon(PWMConnector_LeftFrontDriveMotor);
    	mot_RightFrontDriveMotor = new Talon(PWMConnector_RightFrontDriveMotor);
    	mot_LeftRearDriveMotor = new Talon(PWMConnector_LeftRearDriveMotor);
    	mot_RightRearDriveMotor = new Talon(PWMConnector_RightRearDriveMotor);
    	
    	// Make sure motors are stopped
    	mot_LeftFrontDriveMotor.set(0.0);
    	mot_RightFrontDriveMotor.set(0.0);
    	mot_LeftRearDriveMotor.set(0.0);
    	mot_RightRearDriveMotor.set(0.0);
    	
    	this.bp_AllowedToDrive = true;					//this is a safety mode so robot will not move 
    }

    /**
     * This function is run to update the output objects with data. 
     */
    public void update(){

    	// do it here so nothing can override above
    	if( this.bp_AllowedToDrive == false){
    		
        	d_LeftFrontDrivePower = 
        	d_RightFrontDrivePower = 
        	d_LeftRearDrivePower = 
        	d_RightRearDrivePower = 0.0;	// this form is allowed in Java and C++
        	
    	}

    		
    	/* Motors on one side are flipped over (nverted) so that if we apply the + power the robot goes in what you consider forward.  
    	 * In the case below we flip the Right. If it turns out that your robot is going backwards then you
    	 * would remove * -1 from right and put it on left. Just negating  as in -d_LeftFrontDrivePower does the same thing.      
    	 */
    	mot_LeftFrontDriveMotor.set(d_LeftFrontDrivePower);
    	mot_RightFrontDriveMotor.set(d_RightFrontDrivePower*-1.0);
    	mot_LeftRearDriveMotor.set(d_LeftRearDrivePower);
    	mot_RightRearDriveMotor.set(d_RightRearDrivePower*-1.0);

    }

    public void addTelemetryHeaders(LCTelemetry telem ){
		telem.addColumn("RB Left Front Drive Motor"); 
		telem.addColumn("RB Right Front Drive Motor"); 
		telem.addColumn("RB Left Rear Drive Motor"); 
		telem.addColumn("RB Right Rear Drive Motor");
    }

    public void writeTelemetryValues(LCTelemetry telem ){
		telem.saveDouble("RB Left Front Drive Motor", this.mot_LeftFrontDriveMotor.get()); 
		telem.saveDouble("RB Right Front Drive Motor", this.mot_RightFrontDriveMotor.get()); 
		telem.saveDouble("RB Left Rear Drive Motor", this.mot_LeftRearDriveMotor.get()); 
		telem.saveDouble("RB Right Rear Drive Motor", this.mot_RightRearDriveMotor.get());
    }


    
    
    
	// Show what variables we want to the SmartDashboard
	public void outputToDashboard(boolean b_MinDisplay)  {

    	SmartDashboard.putBoolean("O_AllowedToDrive", bp_AllowedToDrive);

    	if( b_MinDisplay == false ){
			SmartDashboard.putNumber("O_LeftFrontMotor", mot_LeftFrontDriveMotor.getSpeed());
	    	SmartDashboard.putNumber("O_RightFrontMotor", mot_RightFrontDriveMotor.getSpeed());
	    	SmartDashboard.putNumber("O_LeftRearMotor", mot_LeftRearDriveMotor.getSpeed());
	    	SmartDashboard.putNumber("O_RightRearMotor", mot_RightRearDriveMotor.getSpeed());
    	}

	}

	// Pull preferences into our variables.
	public void updatePreferences()  {

        this.bp_AllowedToDrive = Preferences.getInstance().getBoolean("O_AllowedToDrive(bool)", true);
		
	}


	public void writeTelemetry() {
		// Todo
		
	}

    
    
}

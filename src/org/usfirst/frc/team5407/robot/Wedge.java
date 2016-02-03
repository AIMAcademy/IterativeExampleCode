package org.usfirst.frc.team5407.robot;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Preferences;

public class Wedge {

	// create your variables
	double 	d_WedgeWinchPower;
	Talon 	mot_WedgeWinchMotor;
	int 	i_PositionDelta;
	boolean b_InPosition;
	boolean b_IsClose;
	int 	ip_ClosePosition, ip_OnTargetPosition;
	double 	dp_FastUpPower, dp_FastDownPower, dp_SlowUpPower, dp_SlowDownPower;
	
	
    /**
     * This function is run when this class is first created used for any initialization code.
     */
    public Wedge(int PWMConnector_WedgeWinchMotorPWMConnector) {
    
    	d_WedgeWinchPower =0.0;
    	mot_WedgeWinchMotor = new Talon(PWMConnector_WedgeWinchMotorPWMConnector);
    	mot_WedgeWinchMotor.set(0.0);
    	i_PositionDelta = 0;
    	b_InPosition = false;
    	b_IsClose = false;
    	
    	// preferences
    	ip_ClosePosition = 50;
    	ip_OnTargetPosition = 15;
    	dp_FastUpPower = .5;
    	dp_FastDownPower = .5;
    	dp_SlowUpPower = .2;
    	dp_SlowDownPower = .2;
    }


	// Pull preferences into our variables.
	public void updatePreferences()  {
		
		ip_ClosePosition 	= Preferences.getInstance().getInt("W_ClosePosition", 50);
		ip_OnTargetPosition = Preferences.getInstance().getInt("W_OnTargetPosition", 15);
		dp_FastUpPower 		= Preferences.getInstance().getDouble("W_FastUpPower", .5);
		dp_FastDownPower 	= Preferences.getInstance().getDouble("W_FastDownPower", .5);
		dp_SlowUpPower 		= Preferences.getInstance().getDouble("W_SlowUpPower", .2);
		dp_SlowDownPower 	= Preferences.getInstance().getDouble("W_SlowDownPower", .2);
	}

	
    public void addTelemetryHeaders(LCTelemetry telem ){
		telem.addColumn("Wedge Winch Motor");
		telem.addColumn("Wedge Position Delta");
		telem.addColumn("Wedge In Position");
		telem.addColumn("Wedge Is Close");
    }

    public void writeTelemetryValues(LCTelemetry telem ){
		telem.saveDouble("Wedge Winch Motor", this.mot_WedgeWinchMotor.get());
		telem.saveInteger("Wedge Position Delta", this.i_PositionDelta);
		telem.saveTrueBoolean("Wedge In Position", this.b_InPosition);
		telem.saveTrueBoolean("Wedge Is Close", this.b_IsClose);
    }

	
	public void outputToDashboard(boolean b_MinDisplay)  {

		if(b_MinDisplay==false){
	    	SmartDashboard.putNumber("W_WinchPower", d_WedgeWinchPower);
	    	SmartDashboard.putNumber("W_PositionDelta",i_PositionDelta );
	    	SmartDashboard.putBoolean("W_InPosition",this.b_InPosition );
		}

	}

	public void writeTelemetry() {

		
	}

	public boolean isInPosition() {
		return this.b_InPosition; 
	}
	
	public void clear() {				// allow others to set this to a known value before they use it. 
		this.b_InPosition = false; 
	}
	
	
	public void update(Inputs inputs, Sensors sensors) {

		d_WedgeWinchPower = 0.0;						// force the motor power to stop, the code below may change it
    	this.b_InPosition = false;						    /* always do this no matter what 
														   if we reposition in this pass when we are done it will be set true
														   otherwise we want it false.
														*/
    	this.b_IsClose = false;

	   	if(inputs.b_WedgeWinchUp == true){
			d_WedgeWinchPower = 0.5;
			
		}else if(inputs.b_WedgeWinchDown == true){
				d_WedgeWinchPower = -0.5;
				
		}else{
			d_WedgeWinchPower = 0.0;
		}

		
		if (inputs.b_UseWedgeAdjuster == true) {
			
			// Compute delta between current position and desired position
			i_PositionDelta = inputs.i_WedgeDesiredPosition - sensors.i_StringPotentiometerWedge;
			d_WedgeWinchPower = 0.0;
			
			if (i_PositionDelta > 50) {
				d_WedgeWinchPower = 0.5;
			} else if (i_PositionDelta < -50) {
				d_WedgeWinchPower = -0.5;
			} else if (i_PositionDelta > 15) {
				this.b_IsClose = true;
				d_WedgeWinchPower = 0.2;
			} else if (i_PositionDelta < -15) {
				this.b_IsClose = true;
				d_WedgeWinchPower = -0.2;
			} else {
				d_WedgeWinchPower = 0.0;
				this.b_InPosition = true;
			}			
		}
		
		// Update the motor power
		mot_WedgeWinchMotor.set(d_WedgeWinchPower);				
	}


	
	public void update_new(Inputs inputs, Sensors sensors) {  // this version usees all preset values. 

		d_WedgeWinchPower = 0.0;						// force the motor power to stop, the code below may change it
    	this.b_InPosition = false;						    /* always do this no matter what 
														   if we reposition in this pass when we are done it will be set true
														   otherwise we want it false.
														*/

		// test manual operation
	   	if(inputs.b_WedgeWinchUp == true){
			d_WedgeWinchPower = this.dp_FastUpPower;
			
		}else if(inputs.b_WedgeWinchDown == true){
				d_WedgeWinchPower = this.dp_FastDownPower;
		}
				

	   	
		// test the position automation
    	if (inputs.b_UseWedgeAdjuster == true) {

			d_WedgeWinchPower = 0.0;					// override any setting from above if button pressed and this code is called 

			// Compute delta between current position and desired position
    		// + means we are low and have to go up, - mweans we are too high and have to go down. 
			i_PositionDelta = inputs.i_WedgeDesiredPosition - sensors.i_StringPotentiometerWedge;

			if (i_PositionDelta > this.ip_ClosePosition ) {	 // We are too low so go up fast
				d_WedgeWinchPower = this.dp_FastUpPower;
				
			} else if (i_PositionDelta < -this.ip_ClosePosition ) {				 // We are too high so go down fast
				d_WedgeWinchPower = -this.dp_FastDownPower;
				
			} else if (i_PositionDelta > this.ip_OnTargetPosition) {			// we close but still low, go up (+) slow
				d_WedgeWinchPower = this.dp_SlowUpPower;
				
			} else if (i_PositionDelta < -this.ip_OnTargetPosition) {			// we close but still high go down (-) slow
				d_WedgeWinchPower = -this.dp_SlowDownPower;
				
			} else {
				d_WedgeWinchPower = 0.0;				// we are in the sweet spot, stop
				b_InPosition = true;					// indicate we are in Position
			}
			
		}

    	
		// Update the motor power
		mot_WedgeWinchMotor.set(d_WedgeWinchPower);
		
	}

	
	
}

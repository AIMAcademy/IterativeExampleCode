package org.usfirst.frc.team5407.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Preferences;

public class Inputs {

	// declare you variable up here
	Joystick	joy_LeftDriverStick;
	Joystick	joy_RightDriverStick;
	
	Preferences prefs;
	
	double d_RightTankDrivePower;
	double d_LeftTankDrivePower;
	
	
	double d_DriverMecanumPower;
	double d_DriverMecanumTurn;
	double d_DriverMecanumCrab;

	double d_DriverArchadePower;
	double d_DriverArchadeTurn;
	
	boolean b_WedgeWinchUp;
	boolean b_WedgeWinchDown;
	boolean b_TowerWinchUp;
	boolean b_TowerWinchDown;
	boolean b_UseWedgeAdjuster;
	boolean b_SaveTelemetry;
	boolean bp_FastOperation;
	
	int     i_WedgeDesiredPosition;
	int     i_SelectDriveType;
	
	final int kTankDrive = 1;
	final int kArcadeDrive = 2;
	final int kMecanumDive = 3;


	
	// class Contructor initialize you variables here
    public Inputs( int USBConnector_LeftDriverJoystick,
    			   int USBConnector_RightDriverJoystick ) {
    	
    	joy_LeftDriverStick  = new Joystick(USBConnector_LeftDriverJoystick);
    	joy_RightDriverStick = new Joystick(USBConnector_RightDriverJoystick);
    	zeroInputs();      				// this will init many variables
    	
    	bp_FastOperation = true;		// normal competition mode. Otherwise child mode
    	b_SaveTelemetry = false;
    }
     
    // this is the order they will be in the spread sheet. 
    public void addTelemetryHeaders(LCTelemetry telem ){
    	telem.addColumn("I Left Tank Drive Power");
    	telem.addColumn("I Right Tank Drive Power");
		telem.addColumn("I Driver Mecan Crab");
		telem.addColumn("I Driver Mecan Power");
		telem.addColumn("I Driver Mecan Turn");
		telem.addColumn("I Driver Arch Turn");
		telem.addColumn("I Driver Arch Power");
		telem.addColumn("I Fast Oper");
		telem.addColumn("I Wedge Winch Up");
		telem.addColumn("I Wedge Winch Down");
		telem.addColumn("I Tower Winch Up");
		telem.addColumn("I Tower Winch Down");
		telem.addColumn("I Use Wedge Adjuster");
		telem.addColumn("I Wedge Desired Position");  
		telem.addColumn("I Select Drive Type");
    }

    // the order does not matter here 
    public void writeTelemetryValues(LCTelemetry telem ){
    	telem.saveDouble("I Left Tank Drive Power", this.d_LeftTankDrivePower);
    	telem.saveDouble("I Right Tank Drive Power", this.d_RightTankDrivePower);
		telem.saveDouble("I Driver Mecan Crab", this.d_DriverMecanumCrab );
		telem.saveDouble("I Driver Mecan Power", this.d_DriverMecanumPower );
		telem.saveDouble("I Driver Mecan Turn", this.d_DriverMecanumTurn );
		telem.saveDouble("I Driver Arch Turn", this.d_DriverArchadeTurn );
		telem.saveDouble("I Driver Arch Power", this.d_DriverArchadePower );
		telem.saveTrueBoolean("I Wedge Winch Up", this.b_WedgeWinchUp);
		telem.saveTrueBoolean("I Wedge Winch Down", this.b_WedgeWinchDown);
		telem.saveTrueBoolean("I Tower Winch Up", this.b_TowerWinchUp);
		telem.saveTrueBoolean("I Tower Winch Down", this.b_TowerWinchDown);
		telem.saveTrueBoolean("I Use Wedge Adjuster", this.b_UseWedgeAdjuster);
		telem.saveInteger("I Wedge Desired Position", this.i_WedgeDesiredPosition);  
		telem.saveInteger("I Select Drive Type", this.i_SelectDriveType );
    }

    
    // This will read all the inputs, cook them and save them to the appropriate variables.
    public void readValues() {   

    	// you can overload the inputs to test different ideas. 
		d_LeftTankDrivePower = joy_LeftDriverStick.getY() * -1;			// -1 invert to cook range to be 1.0 for forward and -1.0 to backward
		d_RightTankDrivePower = joy_RightDriverStick.getY() * -1;		// -1 to invert
		
		d_DriverMecanumCrab  = joy_RightDriverStick.getX() * -1;		// -1 to invert
		d_DriverMecanumPower = joy_RightDriverStick.getY() * -1;		// -1 to invert
		d_DriverMecanumTurn  = joy_RightDriverStick.getZ() * -1;		// -1 to invert
		
		d_DriverArchadeTurn  = joy_RightDriverStick.getX() * -1 * .50;	//  we cook this down as full is too fast
		d_DriverArchadePower = joy_RightDriverStick.getY() * -1;		// -1 to invert
		
		if( bp_FastOperation == false){			// Modify setting for a child operator. Adjust levels to taste

			d_LeftTankDrivePower  *= .30;		// this is allowed in Java and C++. Same as value = value * .30; 
			d_RightTankDrivePower *= .30;		// all math will work like this +=  -=  /=  
			
			d_DriverMecanumCrab  *= .50;		// need more power to crab
			d_DriverMecanumPower *= .30;		
			d_DriverMecanumTurn  *= .40;		// need a little more to turn
			
			d_DriverArchadePower *= .30;
			d_DriverArchadeTurn  *= .50;		// need a little more power
		}

		
		b_WedgeWinchUp		= joy_RightDriverStick.getRawButton(11);
		b_WedgeWinchDown	= joy_RightDriverStick.getRawButton(10);

		b_TowerWinchUp		= joy_RightDriverStick.getRawButton(5);
		b_TowerWinchDown	= joy_RightDriverStick.getRawButton(3);

		b_UseWedgeAdjuster	= joy_RightDriverStick.getRawButton(8);
		
		if( joy_RightDriverStick.getRawButton(7) == true && joy_LeftDriverStick.getRawButton(7) == true)
			b_SaveTelemetry = true;		// only works in disabled mode
		
		// Use Joystick turney thing to get a number range. Normally all the way down is 1.0., up is -1.0. We will need to invert. 
		i_WedgeDesiredPosition = convertJoystickAxisToValueRange( joy_RightDriverStick.getThrottle() * -1 , 2000 ); //here we invert to be change the direction 
		//i_WedgeDesiredPosition = convertJoystickAxisToValueRange( joy_RightDriverStick.getZ() , 1000 ); // Different joystick have is as GetZ.

		if( i_WedgeDesiredPosition > 1900)	// highest you can go 
			i_WedgeDesiredPosition = 1900;

		
		if( i_WedgeDesiredPosition < 100)	// lowest you can go 
			i_WedgeDesiredPosition = 100;

		
		// here we set to 4. We get 0,1,2,3,4 because we want 1,2,3.  If joystick is broken we may never get 0 and 4 but we will get 1,2,3
		i_SelectDriveType = convertJoystickAxisToValueRange( joy_LeftDriverStick.getThrottle() * -1 , 4 ); //here we invert to be change the direction bottom is 0, 4 is top
		
		// we can get cute here. This way you will always see 1,2,3 and never see 0 or 4. 
		if(i_SelectDriveType < 1)
			i_SelectDriveType = 1;		// this is allowed no curley braces {} needed for the 1 line following
		
		else if(i_SelectDriveType > 3) 
			i_SelectDriveType = 3;

    }

    
	public int convertJoystickAxisToValueRange( double d_InputValue, int i_MaxValue )  {

		// Author Matt Hoffman LC2010 Alum.
		// use this to take an axis and convert into a range. 
		// Axis today it is 1.0 to -1.0, they have to invert outside when they pass in. 
		double d_temp = d_InputValue;						// get the current value    				   range: 1.0 to -1.0
		d_temp = d_temp + 1.0; 								// change range to positive only    		   range: 0.0 to 2.0
		d_temp = d_temp / 2.0;								// divide by 2 to get an average multiplier    range: 0.0 to 1.0  
		d_temp = (int) (d_temp * (double)i_MaxValue);	  	// multiply by highest, Example: 1000		   range:  0  to 1000 (int)
		return (int) d_temp;								// convert to int and return
		
															/* Truth table
															 *    getThrottle  change range	   convert to   mult by 
															 *                 positive +1     average /2   highest
															 *Down    -1.0        0.0             0.0            0            
															 *        -0.5        0.5             0.25         250            
															 *         0.0        1.0              .5          500            
															 *         0.5        1.5              .75         750            
															 *up       1.0        2.0             1.0         1000            
															 */
	}
    
    
	// Show what variables we want to the SmartDashboard
	public void outputToDashboard(boolean b_MinDisplay)  {

		SmartDashboard.putNumber("I_SelectDriveType", i_SelectDriveType);
		SmartDashboard.putNumber("I_SelectWedgePosition", i_WedgeDesiredPosition);
		SmartDashboard.putBoolean("I_FastOperation", bp_FastOperation);

		
		if ( b_MinDisplay == false ){
			SmartDashboard.putNumber("I_LeftDrivePower", d_LeftTankDrivePower);
			SmartDashboard.putNumber("I_RightDrivePower", d_RightTankDrivePower);
			
			SmartDashboard.putBoolean("I_WedgeWinchUp", b_WedgeWinchUp);
			SmartDashboard.putBoolean("I_WedgeWinchDown", b_WedgeWinchDown);
			SmartDashboard.putBoolean("I_WedgeWinchDown", b_UseWedgeAdjuster);
			
			SmartDashboard.putBoolean("I_TowerWinchUp", b_TowerWinchUp);
			SmartDashboard.putBoolean("I_TowerWinchDown", b_TowerWinchDown);
		}
		
	}

	// Pull preferences into our variables.
	public void updatePreferences()  {

        bp_FastOperation = Preferences.getInstance().getBoolean("I_FastOperation(bool)", true);	// ****  we do not zero this **** 
		
	}

	public void writeTelemetry() {
		
	}

	

    public void zeroInputs() {					// reset all variables to stop or off state
    	this.d_LeftTankDrivePower  = 0.0;
    	this.d_RightTankDrivePower = 0.0;
    	this.d_DriverArchadePower = 0.0;
    	this.d_DriverArchadeTurn = 0.0;
    	this.d_DriverMecanumCrab = 0.0;
    	this.d_DriverMecanumPower = 0.0;
    	this.d_DriverMecanumTurn = 0.0;
    	
    	this.b_WedgeWinchUp=false;
    	this.b_WedgeWinchDown=false;
    	this.b_TowerWinchUp=false;
    	this.b_TowerWinchDown=false;
    	this.b_UseWedgeAdjuster=false;
    	
		this.i_SelectDriveType = 0;
		this.i_WedgeDesiredPosition = 500;   // set to middle to be safe. Don't set this to 0. 
											 // may force wedge to go too low. 
    }
    

    
}

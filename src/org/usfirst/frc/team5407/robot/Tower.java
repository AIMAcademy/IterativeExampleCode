package org.usfirst.frc.team5407.robot;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class Tower {
	// declare you variable up here
	double d_TowerWinchPower;
	Talon mot_TowerWinchMotor;
	
	
	// class Contructor initialize you variables here
    public Tower(int PWMCnnnector_TowerWinchMotor) {
    	mot_TowerWinchMotor = new Talon(PWMCnnnector_TowerWinchMotor);
    	d_TowerWinchPower = 0.0;
    }
     


	// Pull preferences into our variables.
	public void updatePreferences()  {
		//pi_TooCloseToWallDistance = Preferences.getInstance().getInt("TooCloseToWall", 300);
        //pd_TowerSlowSpeed = Preferences.getInstance().getDouble("TowerSlowSpeed", .2);

	}

    // This will read all the inputs, cook them and save them to the appropriate variables.
    
	public void update(Inputs inputs, Sensors sensors)  {
		
		d_TowerWinchPower = 0.0;				// do this no mater what 

		if(inputs.b_TowerWinchUp == true){
			d_TowerWinchPower = 0.5;
		}else if(inputs.b_TowerWinchDown == true){
				d_TowerWinchPower = -0.5;
		}	
		
	}

	public void outputToDashboard(boolean b_MinDisplay) {
		
		if( b_MinDisplay == false) {
			SmartDashboard.putNumber("TowerWinchPower", d_TowerWinchPower);
		}
	}

		
	public void writeTelemetry() {

		
	}

	

}

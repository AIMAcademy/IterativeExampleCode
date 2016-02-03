package org.usfirst.frc.team5407.robot;

public class Mecanum {


/*******************************************************************************
* FUNCTION NAME: GetAgileMecanumPower
* PURPOSE:       To get values for the four AgileMecanum Wheels with rotation
* CALLED FROM:   LC2014
* ARGUMENTS:     i_wheel = the  defined number representing the wheel
*                f_direction = the direction to drive (X-axis or throttle based on drivers easy to use)
*                f_power = power sent to motors (Y-axis)
* 				 
* RETURNS:       retValue = the pwm value of the motor
* Authors:       Sreekanth Uppala Lansdale Catholic Robotics - 1-16-14
*******************************************************************************/


	// definitions of the wheels to be worked on. 
	final int kMecanumLeftFront = 1;
	final int kMecanumRightFront = 2;
	final int kMecanumLeftRear = 3;
	final int kMecanumRightRear = 4;
	
    /**
     * Constructor
     */
    public Mecanum() {

    }

	
    public double GetMecanumPower( int i_wheel, double f_direction, double f_power, double f_crab) {

    	double retValue = 0.0;
	
    	
		switch( i_wheel ) {
		
			case kMecanumLeftFront:
				retValue =  f_power - f_direction - f_crab;
				break;
				
			case kMecanumRightFront:
				retValue =  f_power + f_direction + f_crab;
				break;
	
				
			case kMecanumLeftRear:
				retValue =  f_power - f_direction + f_crab;
				break;
			
			case kMecanumRightRear:
				retValue =  f_power + f_direction - f_crab;
				break;
			
			default:
				//printf("GetAgileMacannumMotorValue: Bad Wheel passed....\n");
				break;
			}

	if( retValue < -1.0) 								// condition before recasting to unsigned char
		retValue = -1.0;

	if( retValue > 1.0 ) 
		retValue = 1.0; 

	return retValue; 
	}

}


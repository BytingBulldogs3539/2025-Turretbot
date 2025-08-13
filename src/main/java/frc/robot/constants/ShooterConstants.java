package frc.robot.constants;

import org.frcteam3539.BulldogLibrary.INIConfiguration.BBConstants;

public class ShooterConstants extends BBConstants {
	public ShooterConstants() {
		super("/home/lvuser/ShooterConstants.ini", true);
		save();
	}

	public static double flywheelAcceleration = 0.0; // rot per sec^2
	public static double flywheelkD = 0.0;
	public static double flywheelkI = 0.0;
	public static double flywheelkP = 0.0;
	public static double flywheelkV = 0.0;
	public static double hoodAcceleration = 0.0; // rot per sec^2
	public static double hoodCruiseVelocity = 0.0; // rot per sec
	public static double hoodkD = 0.0;
	public static double hoodkI = 0.0;
	public static double hoodkP = 0.0;
	public static double hoodkV = 0.0;
	public static double turretAcceleration = 0.0; // rot per sec^2
	public static double turretCruiseVelocity = 0.0; // rot per sec
	public static double turretkD = 0.0;
	public static double turretkI = 0.0;
	public static double turretkP = 0.0;
	public static double turretkV = 0.0;
}
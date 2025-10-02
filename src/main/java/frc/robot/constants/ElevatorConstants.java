package frc.robot.constants;

import org.frcteam3539.BulldogLibrary.INIConfiguration.BBConstants;

public class ElevatorConstants extends BBConstants {
	public ElevatorConstants() {
		super("/home/lvuser/ElevatorConstants.ini", true);
		save();
	}

	public static double elevatorVoltage = 2.0;
	public static double elevatorInchesPerRotation = 1.417;
	public static double troughHeight = 7.0;
	public static double coralLowHeight = 22.25;
	public static double coralMidHeight = 37.75;
	public static double coralHighHeight = 74.75;
	public static double netHeight = 79.0;
	public static double processorHeight = 22.75;
	public static double algaeLowHeight = 29.0;
	public static double algaeHighHeight = 45.0;
	public static double elevatorkP = 20.0;
	public static double elevatorkI = 0.0;
	public static double elevatorkD = 0.0;
	public static double elevatorkG = 0.0;
	public static double elevatorkV = 0.12;
	public static double elevatorAcceleration = 95.0;
	public static double elevatorCruiseVelocity = 90.0; // max 100
	public static double elevatorSoftMin = 5.0;
	public static double elevatorSoftMax = 79.0;
	public static double elevatorHomePositionOffset = 22.5;
	public static double handOffHeight = 21.75;
	public static double groundHeight = 6.0;
}
package frc.robot.constants;

import org.frcteam3539.BulldogLibrary.INIConfiguration.BBConstants;

public class IDConstants extends BBConstants {
	public IDConstants() {
		super("/home/lvuser/IDConstants.ini", true);
		save();
	}

	public static int turretMotorID = 0;
	public static int flywheelMotorID = 0;
	public static int hoodMotorID = 0;
	public static int funnelDeployCanCoderID = 0;
	public static int funnelDeployMotorID = 0;
	public static int outerIntakeMotorID = 0;
	public static int innerIntakeMotorID = 0;
	public static int feedWheelMotorID = 0;
	public static int feedBeltMotorID = 0;
	public static int pigeonID = 0;
}
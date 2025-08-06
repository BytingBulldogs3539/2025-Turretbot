package frc.robot.constants;

import org.frcteam3539.BulldogLibrary.INIConfiguration.BBConstants;

public class IDConstants extends BBConstants {
	public IDConstants() {
		super("/home/lvuser/IDConstants.ini", true);
		save();
	}

	public static int turretMotorID = 0;
	public static int flywheelMotorID = 0;
	public static int hoodMotor = 0;
}
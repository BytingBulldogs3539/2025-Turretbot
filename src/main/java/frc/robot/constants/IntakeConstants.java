package frc.robot.constants;

import org.frcteam3539.BulldogLibrary.INIConfiguration.BBConstants;

public class IntakeConstants extends BBConstants {
	public IntakeConstants() {
		super("/home/lvuser/IntakeConstants.ini", true);
		save();
	}

	public static double funnelDeployOffset = 0.0;
	public static double funnelDeployDiscontPoint = 0.0;
	public static double funnelDeploySoftMax = 0.0;
	public static double funnelDeploySoftMin = 0.0;
	public static double funnelDeployMotorToMechanism = 0.0;
	public static double funnelDeploykP = 0.0;
	public static double funnelDeploykI = 0.0;
	public static double funnelDeploykD = 0.0;
	public static double funnelDeploykV = 0.0;
	public static double funnelDeploykG = 0.0;
	public static double funnelDeployAcceleration = 0.0;
	public static double funnelDeployCruiseVelocity = 0.0;
}
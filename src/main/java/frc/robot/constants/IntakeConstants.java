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
	public static double groundFunnelDeployAngle = 0;
	public static double outerIntakeVoltage = 0;
	public static double innerIntakeVoltage = 0;
	public static double homeFunnelDeployAngle = 0;
	public static double handOffFunnelDeployAngle = 0;
	public static double humanFunnelDeployAngle = 0;
	public static double outerIntakeDiameter = 0.0;
	public static double innerIntakeDiameter = 0.0;
	public static double feedWheelDiameter = 0.0;
	public static double feedBeltDiameter = 0.0;
	public static double outerIntakeSS = 100;
	public static double innerIntakeSS = 100;
	public static double feedWheelSS = 100;
	public static double feedBeltSS = 100;
}
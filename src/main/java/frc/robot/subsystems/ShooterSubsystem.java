// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import static edu.wpi.first.units.Units.Degrees;
import static edu.wpi.first.units.Units.RotationsPerSecond;

import com.ctre.phoenix6.configs.CommutationConfigs;
import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.SlotConfigs;
import com.ctre.phoenix6.configs.SoftwareLimitSwitchConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.configs.TalonFXSConfiguration;
import com.ctre.phoenix6.controls.MotionMagicVelocityVoltage;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.hardware.TalonFXS;
import com.ctre.phoenix6.signals.ForwardLimitSourceValue;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.MotorArrangementValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotContainer;
import frc.robot.constants.IDConstants;
import frc.robot.constants.ShooterConstants;

public class ShooterSubsystem extends SubsystemBase {
    private static TalonFX shootMotor;
    private static TalonFXS turretRotateMotor, hoodMotor;
    // private static MotionMagicVoltage turretRotateMM, hoodMM;
    private static double requestedTurretRotateAngle, requestedHoodAngle;

    private static Translation2d turretAimPoint = new Translation2d(4.5, 4.03);

    private static double velocityX = 0, velocityY = 0;

    public ShooterSubsystem() {

        requestedHoodAngle = 0;
        requestedTurretRotateAngle = 0;

        shootMotor = new TalonFX(IDConstants.shootMotorID, "rio");
        shootMotor.getConfigurator()
                .apply(new TalonFXConfiguration().MotorOutput
                        .withInverted(InvertedValue.Clockwise_Positive));

        turretRotateMotor = new TalonFXS(IDConstants.turretMotorID, "rio");
        turretRotateMotor.getConfigurator()
                .apply(new TalonFXSConfiguration().MotorOutput
                        .withInverted(InvertedValue.CounterClockwise_Positive));
        turretRotateMotor.getConfigurator().apply(new TalonFXSConfiguration()
                .withCommutation(new CommutationConfigs()
                        .withMotorArrangement(MotorArrangementValue.Minion_JST)));
        turretRotateMotor.setNeutralMode(NeutralModeValue.Brake);
        turretRotateMotor.getConfigurator()
                .apply(new SoftwareLimitSwitchConfigs().withForwardSoftLimitEnable(true)
                        .withForwardSoftLimitThreshold(degreesToRotations(135.0))
                        .withReverseSoftLimitEnable(true)
                        .withReverseSoftLimitThreshold(degreesToRotations(-135)));

        // turretRotateMM = new MotionMagicVoltage(Degrees.zero());

        hoodMotor = new TalonFXS(IDConstants.hoodMotorID, "rio");
        hoodMotor.getConfigurator()
                .apply(new TalonFXSConfiguration().MotorOutput
                        .withInverted(InvertedValue.Clockwise_Positive));
        hoodMotor.getConfigurator().apply(new TalonFXSConfiguration()
                .withCommutation(new CommutationConfigs()
                        .withMotorArrangement(MotorArrangementValue.Minion_JST)));

        // hoodMM = new MotionMagicVoltage(Degrees.zero());

        reloadConstants();

    }

    public static void setTurretRotateAngle(double angle) {
        requestedTurretRotateAngle = angle;
    }

    public static void setHoodAngle(double angle) {
        requestedHoodAngle = angle;
    }

    public static void setShootVelocity(double angularVelocity) {
        shootMotor.setControl(new MotionMagicVelocityVoltage(angularVelocity));
    }

    public static double getTurretAngle() {
        return rotationsToDegrees(turretRotateMotor.getPosition().getValueAsDouble());
    }

    public static double rotationsToDegrees(double rotations) {
        return rotations * 360 / 47.0;
    };

    public static double degreesToRotations(double degrees) {
        return degrees * 47 / 360.0;
    };

    public static void reloadConstants() {
        shootMotor.getConfigurator().apply(new SlotConfigs()
                .withKP(ShooterConstants.shootkP)
                .withKI(ShooterConstants.shootkI)
                .withKD(ShooterConstants.shootkD)
                .withKV(ShooterConstants.shootkV));
        shootMotor.getConfigurator().apply(new MotionMagicConfigs()
                .withMotionMagicAcceleration(ShooterConstants.shootAcceleration));
        turretRotateMotor.getConfigurator().apply(new SlotConfigs()
                .withKP(ShooterConstants.turretRotatekP)
                .withKI(ShooterConstants.turretRotatekI)
                .withKD(ShooterConstants.turretRotatekD)
                .withKV(ShooterConstants.turretRotatekV));
        turretRotateMotor.getConfigurator().apply(new MotionMagicConfigs()
                .withMotionMagicAcceleration(ShooterConstants.turretRotateAcceleration)
                .withMotionMagicCruiseVelocity(ShooterConstants.turretRotateCruiseVelocity));
        hoodMotor.getConfigurator().apply(new SlotConfigs()
                .withKP(ShooterConstants.hoodkP)
                .withKI(ShooterConstants.hoodkI)
                .withKD(ShooterConstants.hoodkD)
                .withKV(ShooterConstants.hoodkV));
        hoodMotor.getConfigurator().apply(new MotionMagicConfigs()
                .withMotionMagicAcceleration(ShooterConstants.hoodAcceleration)
                .withMotionMagicCruiseVelocity(ShooterConstants.hoodCruiseVelocity));
    }

    public static Translation2d getOffsetTarget() {
		Translation2d offsetTarget;
		double ballSpeed = 4;
		double distanceToTarget = 0;
			distanceToTarget = Math.sqrt(Math.pow(
					RobotContainer.driveSubsystem.getPose2d().getTranslation().getDistance(turretAimPoint), 2)
					+ Math.pow(2.05, 2));
			double timeToTarget = distanceToTarget / ballSpeed;
			offsetTarget = new Translation2d(turretAimPoint.getX() - velocityX * timeToTarget,
                turretAimPoint.getY() - velocityY * timeToTarget);

		return offsetTarget;
    }

    @Override
    public void periodic() {
        velocityX = ChassisSpeeds.fromRobotRelativeSpeeds(RobotContainer.driveSubsystem.getKinematics().toChassisSpeeds(RobotContainer.driveSubsystem.getState().ModuleStates),
			RobotContainer.driveSubsystem.getPose2d().getRotation()).vxMetersPerSecond;
		velocityY = ChassisSpeeds.fromRobotRelativeSpeeds(RobotContainer.driveSubsystem.getKinematics().toChassisSpeeds(RobotContainer.driveSubsystem.getState().ModuleStates),
            RobotContainer.driveSubsystem.getPose2d().getRotation()).vyMetersPerSecond;

        double angleToTarget = RobotContainer.driveSubsystem.getPose2d().getTranslation()
            .minus(getOffsetTarget()).getAngle().getDegrees() - 180;

        double turretTarget = (RobotContainer.driveSubsystem.getPose2d().getRotation().getDegrees()
            - angleToTarget) % 360;

        if (turretTarget > 180)
            turretTarget -= 360;
        else if (turretTarget < -180)
            turretTarget += 360;

        // System.out.println("robot angle " + RobotContainer.driveSubsystem.getPose2d().getRotation().getDegrees()
        //     + " angle to target " + angleToTarget + " turret request " + turretTarget);

        turretRotateMotor.setControl(new MotionMagicVoltage(degreesToRotations(turretTarget)));
    }
}

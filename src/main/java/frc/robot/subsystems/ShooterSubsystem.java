// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import static edu.wpi.first.units.Units.Degrees;
import static edu.wpi.first.units.Units.RotationsPerSecond;

import com.ctre.phoenix6.configs.CommutationConfigs;
import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.SlotConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.configs.TalonFXSConfiguration;
import com.ctre.phoenix6.controls.MotionMagicVelocityVoltage;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.hardware.TalonFXS;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.MotorArrangementValue;

import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.IDConstants;
import frc.robot.constants.ShooterConstants;

public class ShooterSubsystem extends SubsystemBase {
    private static TalonFX flywheelMotor;
    private static TalonFXS turretMotor, hoodMotor;
    private static MotionMagicVoltage turretMM, hoodMM;
    private static Angle requestedTurretAngle, requestedHoodAngle;

    public ShooterSubsystem() {

        flywheelMotor = new TalonFX(IDConstants.flywheelMotorID, "rio");
        flywheelMotor.getConfigurator()
                .apply(new TalonFXConfiguration().MotorOutput.withInverted(InvertedValue.CounterClockwise_Positive));

        turretMotor = new TalonFXS(IDConstants.turretMotorID, "rio");
        turretMotor.getConfigurator()
                .apply(new TalonFXSConfiguration().MotorOutput.withInverted(InvertedValue.CounterClockwise_Positive));
        turretMotor.getConfigurator().apply(new TalonFXSConfiguration()
                .withCommutation(new CommutationConfigs().withMotorArrangement(MotorArrangementValue.Minion_JST)));

        turretMM = new MotionMagicVoltage(Degrees.zero());

        hoodMotor = new TalonFXS(IDConstants.hoodMotorID, "rio");
        hoodMotor.getConfigurator()
                .apply(new TalonFXSConfiguration().MotorOutput.withInverted(InvertedValue.Clockwise_Positive));
        hoodMotor.getConfigurator().apply(new TalonFXSConfiguration()
                .withCommutation(new CommutationConfigs().withMotorArrangement(MotorArrangementValue.Minion_JST)));

        hoodMM = new MotionMagicVoltage(Degrees.zero());
        

        reloadConstants();
    }

    public static void setTurretAngle(Angle angle) {
        requestedTurretAngle = angle;
    }

    public static void setHoodAngle(Angle angle) {
       requestedHoodAngle = angle;
    }

    public static void setFlywheelVelocity(AngularVelocity angularVelocity)
    {
       flywheelMotor.setControl(new MotionMagicVelocityVoltage(angularVelocity));
    }

    public static void reloadConstants() {
        flywheelMotor.getConfigurator().apply(new SlotConfigs()
                .withKP(ShooterConstants.flywheelkP)
                .withKI(ShooterConstants.flywheelkI)
                .withKD(ShooterConstants.flywheelkD)
                .withKV(ShooterConstants.flywheelkV));
        flywheelMotor.getConfigurator().apply(new MotionMagicConfigs()
                .withMotionMagicAcceleration(ShooterConstants.flywheelAcceleration));
        turretMotor.getConfigurator().apply(new SlotConfigs()
                .withKP(ShooterConstants.turretkP)
                .withKI(ShooterConstants.turretkI)
                .withKD(ShooterConstants.turretkD)
                .withKV(ShooterConstants.turretkV));
        turretMotor.getConfigurator().apply(new MotionMagicConfigs()
                .withMotionMagicAcceleration(ShooterConstants.turretAcceleration)
                .withMotionMagicCruiseVelocity(ShooterConstants.turretCruiseVelocity));
        hoodMotor.getConfigurator().apply(new SlotConfigs()
                .withKP(ShooterConstants.hoodkP)
                .withKI(ShooterConstants.hoodkI)
                .withKD(ShooterConstants.hoodkD)
                .withKV(ShooterConstants.hoodkV));
        hoodMotor.getConfigurator().apply(new MotionMagicConfigs()
                .withMotionMagicAcceleration(ShooterConstants.hoodAcceleration)
                .withMotionMagicCruiseVelocity(ShooterConstants.hoodCruiseVelocity));
    }

    @Override
    public void periodic() {
        turretMM.withPosition(requestedTurretAngle);
        turretMotor.setControl(turretMM);
        hoodMM.withPosition(requestedHoodAngle);
        hoodMotor.setControl(hoodMM);
        
    }
}

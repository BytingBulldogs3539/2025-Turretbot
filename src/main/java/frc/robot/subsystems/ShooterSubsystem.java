// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.configs.CommutationConfigs;
import com.ctre.phoenix6.configs.SlotConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.configs.TalonFXSConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.hardware.TalonFXS;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.MotorArrangementValue;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.IDConstants;
import frc.robot.constants.ShooterConstants;

public class ShooterSubsystem extends SubsystemBase {
  private static TalonFX flywheelMotor;
  private static TalonFXS turretMotor, hoodMotor;

  public ShooterSubsystem() {

    flywheelMotor = new TalonFX(IDConstants.flywheelMotorID, "rio");
    flywheelMotor.getConfigurator()
        .apply(new TalonFXConfiguration().MotorOutput.withInverted(InvertedValue.CounterClockwise_Positive));
    

    turretMotor = new TalonFXS(IDConstants.turretMotorID, "rio");
    turretMotor.getConfigurator()
        .apply(new TalonFXSConfiguration().MotorOutput.withInverted(InvertedValue.CounterClockwise_Positive));
    turretMotor.getConfigurator().apply(new TalonFXSConfiguration()
        .withCommutation(new CommutationConfigs().withMotorArrangement(MotorArrangementValue.Minion_JST)));


    hoodMotor = new TalonFXS(IDConstants.hoodMotor, "rio");
    hoodMotor.getConfigurator()
        .apply(new TalonFXSConfiguration().MotorOutput.withInverted(InvertedValue.Clockwise_Positive));
    hoodMotor.getConfigurator().apply(new TalonFXSConfiguration()
        .withCommutation(new CommutationConfigs().withMotorArrangement(MotorArrangementValue.Minion_JST)));
    
    reloadConstants();
  }

  public static void reloadConstants() {
    flywheelMotor.getConfigurator().apply(new SlotConfigs().withKP(ShooterConstants.flywheelkP)
        .withKI(ShooterConstants.flywheelkI).withKD(ShooterConstants.flywheelkD).withKV(ShooterConstants.flywheelkV));
    turretMotor.getConfigurator().apply(new SlotConfigs().withKP(ShooterConstants.turretkP)
        .withKI(ShooterConstants.turretkI).withKD(ShooterConstants.turretkD).withKV(ShooterConstants.turretkV));
    hoodMotor.getConfigurator().apply(new SlotConfigs().withKP(ShooterConstants.hoodkP).withKI(ShooterConstants.hoodkI)
        .withKD(ShooterConstants.hoodkD).withKV(ShooterConstants.hoodkV));

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}

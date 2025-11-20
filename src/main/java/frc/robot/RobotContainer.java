// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.lang.ModuleLayer.Controller;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.DriveCommand;
import frc.robot.commands.GiveItTheBeansCommand;
import frc.robot.commands.PrepareTheBeansCommand;
import frc.robot.commands.RotateTurretCommand;
import frc.robot.constants.IntakeConstants;
import frc.robot.constants.ShooterConstants;
import frc.robot.Generated.TunerConstants;
import frc.robot.subsystems.*;

public class RobotContainer {
  public static ShooterConstants shooterConstants = new ShooterConstants();
  public static IntakeConstants intakeConstants = new IntakeConstants();

  public static DriveSubsystem driveSubsystem = TunerConstants.createDrivetrain();
  public static IntakeSubsystem intakeSubsystem = new IntakeSubsystem();
  public static ShooterSubsystem ShooterSubsystem = new ShooterSubsystem();

  public static CommandXboxController operatorController = new CommandXboxController(0);
  public static CommandXboxController driverController = new CommandXboxController(1);
  public static Trigger rightDriverTrigger = driverController.rightTrigger();
  public static Trigger rightDriverBumper = driverController.rightBumper();


  public RobotContainer() {
    configureBindings();
    driveSubsystem.setDefaultCommand(new DriveCommand());
  }

  private void configureBindings() {
    operatorController.leftTrigger(0.1).whileTrue(new PrepareTheBeansCommand());
    operatorController.rightTrigger(0.1).whileTrue(new GiveItTheBeansCommand());
    operatorController.b().onTrue(new RotateTurretCommand(90));
    operatorController.y().onTrue(new RotateTurretCommand(0));
    operatorController.x().onTrue(new RotateTurretCommand(-90));

  }

  public Command getAutonomousCommand() {
    return Commands.print("No autonomous command configured");
  }
}

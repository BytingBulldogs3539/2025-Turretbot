// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import static edu.wpi.first.units.Units.RotationsPerSecond;

import edu.wpi.first.units.AngularVelocityUnit;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.IntakeConstants;
import frc.robot.constants.ShooterConstants;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.utils.Utils;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class GiveItTheBeansCommand extends Command {
  /** Creates a new ShootCommand. */
  public GiveItTheBeansCommand() {
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    System.out.println("init shoot vel " + Utils.ssToAngularVelocity(IntakeConstants.feedWheelDiameter, IntakeConstants.feedWheelSS).baseUnitMagnitude());
    IntakeSubsystem.setFeedBeltVelocity(Utils.ssToAngularVelocity(IntakeConstants.feedBeltDiameter, IntakeConstants.feedBeltSS).baseUnitMagnitude());
    IntakeSubsystem.setFeedWheelVelocity(Utils.ssToAngularVelocity(IntakeConstants.feedWheelDiameter, IntakeConstants.feedWheelSS).baseUnitMagnitude());
    // IntakeSubsystem.setInnerIntakeMotor(5);;

    


   }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {}

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    IntakeSubsystem.setFeedBeltVelocity(0);
    IntakeSubsystem.setFeedWheelVelocity(0);
    // IntakeSubsystem.setInnerIntakeMotor(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}

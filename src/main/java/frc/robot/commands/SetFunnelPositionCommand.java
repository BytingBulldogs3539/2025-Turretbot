// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.IntakeConstants;
import frc.robot.constants.EnumConstants.IntakeMode;
import frc.robot.subsystems.IntakeSubsystem;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class SetFunnelPositionCommand extends Command {
  /** Creates a new SetFunnelPositionCommand. */
  IntakeMode mode;
  double targetPosition;
  boolean useIsFinished;

  public SetFunnelPositionCommand(IntakeMode mode, boolean useIsFinished) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.mode = mode;
    this.useIsFinished = useIsFinished;

    switch (mode) {
      case GROUND:
        targetPosition = IntakeConstants.groundFunnelDeployAngle;
        break;
      case HANDOFF:
        targetPosition = IntakeConstants.handOffFunnelDeployAngle;
        break;
    }

  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    IntakeSubsystem.setFunnelDeployAngle(targetPosition);

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return !useIsFinished || MathUtil.isNear(targetPosition, IntakeSubsystem.getFunnelDeployAngle(), 1.5);
  }
}

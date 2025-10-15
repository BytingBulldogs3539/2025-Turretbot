// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.math.filter.LinearFilter;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.Alert;
import edu.wpi.first.wpilibj.Alert.AlertType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.IDConstants;
import frc.robot.constants.IntakeConstants;

import java.text.DecimalFormat;
import java.util.HashMap;

import com.ctre.phoenix6.configs.CommutationConfigs;
import com.ctre.phoenix6.configs.FeedbackConfigs;
import com.ctre.phoenix6.configs.MagnetSensorConfigs;
import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.SlotConfigs;
import com.ctre.phoenix6.configs.SoftwareLimitSwitchConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.configs.TalonFXSConfiguration;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.ParentDevice;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.hardware.TalonFXS;
import com.ctre.phoenix6.signals.FeedbackSensorSourceValue;
import com.ctre.phoenix6.signals.GravityTypeValue;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.MotorArrangementValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.SensorDirectionValue;

public class IntakeSubsystem extends SubsystemBase {

  private static TalonFX funnelDeployMotor, feedWheelMotor, feedBeltMotor; // , algaeDeployMotor,algaeIntakeMotor;
  private static TalonFXS innerIntakeMotor, outerIntakeMotor; 

  private static CANcoder funnelDeployCanCoder;
  private static double requestedFunnelDeployPos = 0;
  DecimalFormat df = new DecimalFormat("#.00000");
  public static LinearFilter filter = LinearFilter.movingAverage(5);
  public static LinearFilter funnelFilter = LinearFilter.movingAverage(4);

  public static HashMap<ParentDevice, Alert> connectedIntakeAlerts = new HashMap<>();
  public static HashMap<ParentDevice, Alert> wasDisconnectedIntakeAlerts = new HashMap<>();

  private void createAlert(ParentDevice device, String deviceName) {
    Alert isAlert = new Alert("Intake Subsystem", deviceName + ": is disconnected", AlertType.kError);
    connectedIntakeAlerts.put(device, isAlert);
    Alert wasAlert = new Alert("Intake Subsystem", deviceName + ": was disconnected", AlertType.kError);
    wasDisconnectedIntakeAlerts.put(device, wasAlert);
  }

  public IntakeSubsystem() {

    funnelDeployCanCoder = new CANcoder(IDConstants.funnelDeployCanCoderID, "rio");

    funnelDeployCanCoder.getConfigurator()
        .apply(new MagnetSensorConfigs().withAbsoluteSensorDiscontinuityPoint(IntakeConstants.funnelDeployDiscontPoint)
            .withSensorDirection(SensorDirectionValue.Clockwise_Positive)
            .withMagnetOffset(IntakeConstants.funnelDeployOffset));

    funnelDeployMotor = new TalonFX(IDConstants.funnelDeployMotorID, "rio");
    funnelDeployMotor.getConfigurator().apply(
          new TalonFXConfiguration().MotorOutput
              .withInverted(InvertedValue.CounterClockwise_Positive));
  

    funnelDeployMotor.setNeutralMode(NeutralModeValue.Brake);

    outerIntakeMotor = new TalonFXS(IDConstants.outerIntakeMotorID, "rio");
    outerIntakeMotor.getConfigurator().apply(new TalonFXSConfiguration()
        .withCommutation(new CommutationConfigs().withMotorArrangement(MotorArrangementValue.Minion_JST)));
    outerIntakeMotor.getConfigurator().apply(
        new TalonFXSConfiguration().MotorOutput
            .withInverted(InvertedValue.Clockwise_Positive));

    innerIntakeMotor = new TalonFXS(IDConstants.innerIntakeMotorID, "rio");
    innerIntakeMotor.getConfigurator().apply(new TalonFXSConfiguration()
        .withCommutation(new CommutationConfigs()
            .withMotorArrangement(MotorArrangementValue.Minion_JST)));
    innerIntakeMotor.getConfigurator().apply(
        new TalonFXSConfiguration().MotorOutput
            .withInverted(InvertedValue.Clockwise_Positive));

    funnelDeployMotor.getConfigurator().apply(new SoftwareLimitSwitchConfigs().withForwardSoftLimitEnable(true)
        .withForwardSoftLimitThreshold(degreesToFunnelDeployRotations(IntakeConstants.funnelDeploySoftMax))
        .withReverseSoftLimitEnable(true)
        .withReverseSoftLimitThreshold(degreesToFunnelDeployRotations(IntakeConstants.funnelDeploySoftMin)));

    funnelDeployMotor.getConfigurator()
        .apply(new FeedbackConfigs().withFeedbackRemoteSensorID(IDConstants.funnelDeployCanCoderID)
            .withRotorToSensorRatio(IntakeConstants.funnelDeployMotorToMechanism).withSensorToMechanismRatio(1)
            .withFeedbackSensorSource(FeedbackSensorSourceValue.FusedCANcoder));
    
    feedWheelMotor = new TalonFX(IDConstants.feedWheelMotorID, "rio");
    feedWheelMotor.getConfigurator()
                  .apply(new TalonFXConfiguration().MotorOutput.withInverted(InvertedValue.CounterClockwise_Positive));

    feedBeltMotor = new TalonFX(IDConstants.feedBeltMotorID, "rio");
    feedBeltMotor.getConfigurator().apply(new TalonFXConfiguration().MotorOutput.withInverted(InvertedValue.Clockwise_Positive));
    
    reloadConstants();

    createAlert(funnelDeployCanCoder, "funnelDeployCanConder");
    createAlert(funnelDeployMotor, "funnelDeployMotor");
    createAlert(outerIntakeMotor, "fuelIntakeMotor");
    createAlert(innerIntakeMotor, "funnelIntakeMotor");
   
  }

  public static void reloadConstants()
  {
    funnelDeployMotor.getConfigurator()
        .apply(new SlotConfigs().withKP(IntakeConstants.funnelDeploykP).withKI(IntakeConstants.funnelDeploykI)
            .withKD(IntakeConstants.funnelDeploykD).withKV(IntakeConstants.funnelDeploykV)
            .withKG(IntakeConstants.funnelDeploykG).withGravityType(GravityTypeValue.Arm_Cosine));
      funnelDeployMotor.getConfigurator()
            .apply(new MotionMagicConfigs()
                .withMotionMagicAcceleration(IntakeConstants.funnelDeployAcceleration)
                .withMotionMagicCruiseVelocity(IntakeConstants.funnelDeployCruiseVelocity));
  }

  public static double getFunnelDeployAngle() {
    return Units.rotationsToDegrees(
        funnelDeployCanCoder.getAbsolutePosition().getValueAsDouble());
  }

  public static void setFunnelDeployAngle(double angle) {
    requestedFunnelDeployPos = angle;
  }

  public static void initializFunnelDeployAngle() {
    requestedFunnelDeployPos = getFunnelDeployAngle();
  }


  public static void setfuelIntakeMotor(double voltage) {
    outerIntakeMotor.setControl(new VoltageOut(voltage).withEnableFOC(true));
  }

  public static void setInnerIntakeMotor(double voltage) {
    innerIntakeMotor.setControl(new VoltageOut(voltage).withEnableFOC(true));
  }

  public static double degreesToFunnelDeployRotations(double degrees) {
    return Units.degreesToRotations(degrees);
  }

  public static void setFunnelBreakMode(boolean enabled) {
    if (enabled) {
      funnelDeployMotor.setNeutralMode(NeutralModeValue.Brake);
    } else {
      funnelDeployMotor.setNeutralMode(NeutralModeValue.Coast);
    }

  }


  @Override
  public void periodic() {
    for (ParentDevice device : connectedIntakeAlerts.keySet()) {
      Alert isAlert = connectedIntakeAlerts.get(device);
      Alert wasAlert = wasDisconnectedIntakeAlerts.get(device);

      if (!device.isConnected()) {
        isAlert.set(true);
        wasAlert.set(false);

      } else if (isAlert.get()) {
        isAlert.set(false);
        wasAlert.set(true);

      }
    }
    
    // This method will be called once per scheduler run
    funnelDeployMotor.setControl(new MotionMagicVoltage(degreesToFunnelDeployRotations(requestedFunnelDeployPos)));
  }
}

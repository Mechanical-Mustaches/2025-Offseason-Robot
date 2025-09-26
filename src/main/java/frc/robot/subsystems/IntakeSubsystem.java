package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkLimitSwitch;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.ClosedLoopConfig;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.ClosedLoopConfig.FeedbackSensor;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class IntakeSubsystem extends SubsystemBase {

    SwerveDriveSubsystem swerve;

    private SparkMax centeringMotor = new SparkMax(13, MotorType.kBrushless);
    private SparkMax intakingMotor = new SparkMax(15, MotorType.kBrushless);
    private SparkMax pivotMotor = new SparkMax(14, MotorType.kBrushless);
    private SparkLimitSwitch lineBreakSensor = centeringMotor.getForwardLimitSwitch();

    public IntakeSubsystem() {
        SparkMaxConfig pivotConfig = new SparkMaxConfig();
        ClosedLoopConfig pidConfig = new ClosedLoopConfig();

        pidConfig
                .pid(0.1, 0, 0)
                .feedbackSensor(FeedbackSensor.kPrimaryEncoder);

        pivotConfig
                .smartCurrentLimit(30)
                .idleMode(IdleMode.kBrake)
                .apply(pidConfig);

        pivotMotor.configure(pivotConfig, null, null);

    }

    public double getEncoderValue() {
        return pivotMotor.getEncoder().getPosition();
    }

    public void pivotPickUp() {
        pivotMotor.getClosedLoopController().setReference(-1.8, ControlType.kPosition);
    }

    public void pivotDropOff() {
        pivotMotor.getClosedLoopController().setReference(58, ControlType.kPosition);
    }

    public void intakeSpin() {
        intakingMotor.set(0.5);
        centeringMotor.set(0.5);
    }

    public void dumbIntakePivotIn(){
        pivotMotor.set(0.5);
    }
    public void dumbIntakePivotOut(){
        pivotMotor.set(-0.5);
    }
    public void dumbIntakePivotStop(){
        pivotMotor.set(0);
    }
    

    public void intakeStop() {
        intakingMotor.set(0);
        centeringMotor.set(0);
    }

    public void centerCoral() {
        centeringMotor.set(0.2);
    }

    public boolean isCoralDetected() {
        return lineBreakSensor.isPressed();
    }

    public void stopCenteringCoral() {
        centeringMotor.set(0);
    }

    // public boolean isCoralDetected() {
    // if ((swerve.leftDistanceSensor.getRange() +
    // swerve.rightDistanceSensor.getRange()) / 2 < 5) {
    // return true;
    // } else {
    // return false;
    // }
    // }

    @Override
    public void periodic() {
        SmartDashboard.putBoolean("coralCentered", isCoralDetected());
        SmartDashboard.putNumber("IntakePivotEncoderValue", pivotMotor.getEncoder().getPosition());
    }
}

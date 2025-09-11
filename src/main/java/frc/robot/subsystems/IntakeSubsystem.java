package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.ClosedLoopConfig;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.ClosedLoopConfig.FeedbackSensor;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class IntakeSubsystem extends SubsystemBase {

    SwerveDriveSubsystem swerve;

    private SparkMax intakeMotor = new SparkMax(99, MotorType.kBrushless);
    private SparkMax intakePivotLeft = new SparkMax(98, MotorType.kBrushless);
    private SparkMax intakePivotRight = new SparkMax(97, MotorType.kBrushless);

    public IntakeSubsystem() {
        SparkMaxConfig leftConfig = new SparkMaxConfig();
        SparkMaxConfig rightConfig = new SparkMaxConfig();
        ClosedLoopConfig pidConfig = new ClosedLoopConfig();

        pidConfig
                .pid(0.1, 0, 0)
                .feedbackSensor(FeedbackSensor.kPrimaryEncoder);

        leftConfig
                .smartCurrentLimit(30)
                .idleMode(IdleMode.kBrake)
                .apply(pidConfig);

        rightConfig
                .apply(leftConfig)
                .follow(98, true);

        intakePivotLeft.configure(leftConfig, null, null);
        intakePivotRight.configure(rightConfig, null, null);

    }

    public double getEncoderValue() {
        return intakePivotLeft.getEncoder().getPosition();
    }

    public void pivotPickUp() {
        intakePivotLeft.getClosedLoopController().setReference(0, ControlType.kPosition);
    }

    public void pivotDropOff() {
        intakePivotLeft.getClosedLoopController().setReference(1, ControlType.kPosition);
    }

    public void intakeSpin() {
        intakeMotor.set(0.5);
    }

    public void intakeStop() {
        intakeMotor.set(0);
    }

    public boolean isCoralDetected() {
        if ((swerve.leftDistanceSensor.getRange() + swerve.rightDistanceSensor.getRange()) / 2 < 5) {
            return true;
        } else {
            return false;
        }
    }
}

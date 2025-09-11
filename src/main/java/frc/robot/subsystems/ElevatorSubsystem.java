package frc.robot.subsystems;

import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.ClosedLoopConfig;
import com.revrobotics.spark.config.ClosedLoopConfig.FeedbackSensor;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ElevatorSubsystem extends SubsystemBase {

    public enum Level {
        LDefault(0, 0),
        LIntake(0.5, 0.5),
        LSource(0.75, 0.5),
        L1(1, 0.1),
        L2(2, 0.2),
        L3(3, 0.3),
        L4(4, 0.4);

        public final double elevatorEncoderValue;
        public final double pivotEncoderValue;

        private Level(double elevatorEncoder, double pivotEncoder) {
            this.elevatorEncoderValue = elevatorEncoder;
            this.pivotEncoderValue = pivotEncoder;

        }
    }

    private SparkMax leftEleMotor = new SparkMax(11, MotorType.kBrushless);
    private SparkMax rightEleMotor = new SparkMax(12, MotorType.kBrushless);
    private SparkMax pivotMotor = new SparkMax(9, MotorType.kBrushless);

    public ElevatorSubsystem() {
        SparkMaxConfig leftConfig = new SparkMaxConfig();
        SparkMaxConfig rightConfig = new SparkMaxConfig();
        SparkMaxConfig pivotConfig = new SparkMaxConfig();
        ClosedLoopConfig pidEleConfig = new ClosedLoopConfig();
        ClosedLoopConfig pidArmConfig = new ClosedLoopConfig();

        pidEleConfig
                .pidf(0.1, 0, 0, 0.001)
                .feedbackSensor(FeedbackSensor.kPrimaryEncoder);

        pidArmConfig
                .pid(0.1, 0, 0)
                .feedbackSensor(FeedbackSensor.kPrimaryEncoder);

        leftConfig
                .smartCurrentLimit(40)
                .idleMode(IdleMode.kBrake)
                .apply(pidEleConfig);

        rightConfig
                .apply(leftConfig)
                .follow(96, true);

        pivotConfig
                .smartCurrentLimit(40)
                .idleMode(IdleMode.kBrake)
                .apply(pidArmConfig);

        leftEleMotor.configure(leftConfig, null, null);
        rightEleMotor.configure(rightConfig, null, null);
        pivotMotor.configure(pivotConfig, null, null);
    }

    public void setElevatorPosition(Level targetLevel) {
        leftEleMotor.getClosedLoopController().setReference(targetLevel.elevatorEncoderValue, ControlType.kPosition);
        // Sketchy
    }

    public double getEleEncoderValue() {
        return leftEleMotor.getEncoder().getPosition();
    }

    public double getArmEncoderValue() {
        return pivotMotor.getEncoder().getPosition();
    }

    public void setPivotPosition(Level targetLevel, Boolean flipScoringSide) {
        if (flipScoringSide) {
            pivotMotor.getClosedLoopController().setReference((1 - targetLevel.pivotEncoderValue),
                    ControlType.kPosition);
        } else {
            pivotMotor.getClosedLoopController().setReference(targetLevel.pivotEncoderValue, ControlType.kPosition);
        }
    }

}

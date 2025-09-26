package frc.robot.subsystems;

import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.ClosedLoopConfig;
import com.revrobotics.spark.config.ClosedLoopConfig.FeedbackSensor;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ElevatorSubsystem extends SubsystemBase {

    public enum Level {
        LDefault(0, 0),
        LIntake(-51, 0.38956),
        LSource(0.75, 0),
        L1(0, 0.07575),
        L2(-7, 0.07575),
        L3(-24, 0.07575),
        L4(-60, 0.07575);

        public final double elevatorEncoderValue;
        public final double pivotEncoderValue;

        private Level(double elevatorEncoder, double pivotEncoder) {
            this.elevatorEncoderValue = elevatorEncoder;
            this.pivotEncoderValue = pivotEncoder;

        }
    }

    private SparkMax leftEleMotor = new SparkMax(20, MotorType.kBrushless);
    private SparkMax rightEleMotor = new SparkMax(12, MotorType.kBrushless);
    private SparkMax pivotMotor = new SparkMax(9, MotorType.kBrushless);
    //private SparkMax o

    public ElevatorSubsystem() {
        SparkMaxConfig leftConfig = new SparkMaxConfig();
        SparkMaxConfig rightConfig = new SparkMaxConfig();
        SparkMaxConfig pivotConfig = new SparkMaxConfig();
        ClosedLoopConfig pidEleConfig = new ClosedLoopConfig();
        ClosedLoopConfig pidArmConfig = new ClosedLoopConfig();

        pidEleConfig
                .pid(0.15, 0.00001, 0.1)
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
                .follow(leftEleMotor.getDeviceId(), true);

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
    }

    public void setPivotPosition(Level targetLevel, Boolean flipScoringSide) {
        if (flipScoringSide) {
            pivotMotor.getClosedLoopController().setReference((1 - targetLevel.pivotEncoderValue),
                    ControlType.kPosition);
        } else {
            pivotMotor.getClosedLoopController().setReference(targetLevel.pivotEncoderValue, ControlType.kPosition);
        }
    }

    public double getEleEncoderValue() {
        return leftEleMotor.getEncoder().getPosition();
    }

    public double getPivotEncoderValue() {
        return pivotMotor.getEncoder().getPosition();
    }

    public void dumbEleUp() {
        leftEleMotor.set(0.1);
    }

    public void dumbEleDown() {
        leftEleMotor.set(-0.1);
    }

    public void dumbEleStop(){
        leftEleMotor.set(0);
    }

    @Override
    public void periodic() {

        SmartDashboard.putNumber("ElevatorEncoder: ", getEleEncoderValue());
        SmartDashboard.putNumber("ElevatorAmperage: ", leftEleMotor.getOutputCurrent());
        SmartDashboard.putNumber("ElevatorPivotEncoder: ", getPivotEncoderValue());
        SmartDashboard.putNumber("ElevatorPivotAmperage: ", pivotMotor.getOutputCurrent());
        SmartDashboard.putNumber("ElevatorSpeed", leftEleMotor.get());
    }

}

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
    
    public enum Level{
        LIntake(0),
        L1(1),
        L2(2),
        L3(3),
        L4(4);

        public final double encoderValue;

        private Level(double encoder) {
            this.encoderValue = encoder;
        }
    }

    private SparkMax leftEleMotor = new SparkMax(96, MotorType.kBrushless);
    private SparkMax rightEleMotor = new SparkMax(95, MotorType.kBrushless);
    private SparkMax armPivot = new SparkMax(94, MotorType.kBrushless);

    public ElevatorSubsystem(){
        SparkMaxConfig leftConfig = new SparkMaxConfig();
        SparkMaxConfig rightConfig = new SparkMaxConfig();
        SparkMaxConfig armPivotConfig = new SparkMaxConfig();
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

        armPivotConfig
            .smartCurrentLimit(40)
            .idleMode(IdleMode.kBrake)
            .apply(pidArmConfig);

        leftEleMotor.configure(leftConfig, null, null);
        rightEleMotor.configure(rightConfig, null, null);
        armPivot.configure(armPivotConfig, null, null);
    }

    public void setPosition(Level targetLevel) {
        leftEleMotor.getClosedLoopController().setReference(targetLevel.encoderValue, ControlType.kPosition);
        //Sketchy
    }

    public double getEleEncoderValue(){
        return leftEleMotor.getEncoder().getPosition();
    }
    public double getArmEncoderValue(){
        return armPivot.getEncoder().getPosition();
    }

    public void armUp(){
        armPivot.getClosedLoopController().setReference(1, ControlType.kPosition);
    }
    public void armDown(){
        armPivot.getClosedLoopController().setReference(0, ControlType.kPosition);
    }
}

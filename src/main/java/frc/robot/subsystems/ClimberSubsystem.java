package frc.robot.subsystems;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.ClosedLoopConfig;
import com.revrobotics.spark.config.ClosedLoopConfig.FeedbackSensor;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ClimberSubsystem extends SubsystemBase{

    private SparkMax climberMotor = new SparkMax(93, MotorType.kBrushless);

    public ClimberSubsystem() {
        SparkMaxConfig climberConfig = new SparkMaxConfig();
        ClosedLoopConfig pidConfig = new ClosedLoopConfig();

        pidConfig
            .pid(0.1, 0, 0)
            .feedbackSensor(FeedbackSensor.kPrimaryEncoder);
        
        climberConfig
            .smartCurrentLimit(50)
            .idleMode(IdleMode.kBrake)
            .apply(pidConfig);

            climberMotor.configure(climberConfig, null, null);
    }
    public double getEncoderValue(){
        return climberMotor.getEncoder().getPosition();
    }
}

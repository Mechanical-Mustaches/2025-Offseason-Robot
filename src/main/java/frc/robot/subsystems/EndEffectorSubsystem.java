package frc.robot.subsystems;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class EndEffectorSubsystem extends SubsystemBase {

    private SparkMax endEffectorMotor = new SparkMax(10, MotorType.kBrushless);

    public EndEffectorSubsystem() {
        SparkMaxConfig endEffectorConfig = new SparkMaxConfig();

        endEffectorConfig
                .smartCurrentLimit(30)
                .idleMode(IdleMode.kBrake);

    }

    public void grab() {
        endEffectorMotor.set(0.5);
    }

    public void stop() {
        endEffectorMotor.set(0);
    }

    public void release() {
        endEffectorMotor.set(-0.5);
    }
}

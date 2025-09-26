package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.ElevatorSubsystem;

public class ElevatorSequentialCommand extends SequentialCommandGroup{
    public ElevatorSequentialCommand(ElevatorSubsystem elevatorSubsystem, ElevatorSubsystem.Level level, boolean ArmFlip) {
        super(
                new ElevatorCommand(elevatorSubsystem, level),
                new PivotArmCommand(elevatorSubsystem, level, ArmFlip));
    }
}

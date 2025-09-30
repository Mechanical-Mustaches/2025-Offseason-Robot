package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.ElevatorSubsystem;
import frc.robot.subsystems.EndEffectorSubsystem;
import frc.robot.subsystems.ElevatorSubsystem.Level;

public class ElevatorSequentialCommand extends SequentialCommandGroup{
    EndEffectorSubsystem endEffectorSubsystem;
    public ElevatorSequentialCommand(ElevatorSubsystem elevatorSubsystem, ElevatorSubsystem.Level level, boolean ArmFlip) {
        super(
                new ElevatorCommand(elevatorSubsystem, level),
                new PivotArmCommand(elevatorSubsystem, level, ArmFlip));
                if (level == Level.LIntake)
                new InstantCommand(() -> endEffectorSubsystem.grab());
    }
}

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.IntakeSubsystem;

public class IntakeCommandGroup extends SequentialCommandGroup {

    public IntakeCommandGroup(IntakeSubsystem intake) {
        super(
                new IntakeCommand(intake),
                new WaitCommand(1),
                new InstantCommand(() -> intake.stopCenteringCoral()));
    }
}

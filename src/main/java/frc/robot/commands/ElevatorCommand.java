package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ElevatorSubsystem;

public class ElevatorCommand extends Command {
    private final ElevatorSubsystem elevator;
    private final ElevatorSubsystem.Level targetLevel;

    public ElevatorCommand(ElevatorSubsystem subsystem, ElevatorSubsystem.Level level) {
        elevator = subsystem;
        targetLevel = level;
    }

    @Override
    public void initialize() {
        elevator.setElevatorPosition(targetLevel);
    }

    @Override
    public boolean isFinished() {
        if (elevator.getEleEncoderValue() >= (targetLevel.elevatorEncoderValue - 2) || elevator.getEleEncoderValue() <= (targetLevel.elevatorEncoderValue +2)){
            return true;
        } else{
            return false;
        }
    }
}

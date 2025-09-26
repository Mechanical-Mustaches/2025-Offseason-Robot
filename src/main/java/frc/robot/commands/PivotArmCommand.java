package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ElevatorSubsystem;
import frc.robot.subsystems.ElevatorSubsystem.Level;

public class PivotArmCommand extends Command {
    private final ElevatorSubsystem elevator;
    private final ElevatorSubsystem.Level targetLevel;
    boolean ArmFlipState;
    
    public PivotArmCommand(ElevatorSubsystem subsystem, ElevatorSubsystem.Level level, boolean ArmFlip) {
        elevator = subsystem;
        targetLevel = level;
        ArmFlipState = ArmFlip;
    }

    @Override
    public void initialize() {
        if (ArmFlipState == true){
            elevator.setPivotPosition(targetLevel, true);
        } else {
            elevator.setPivotPosition(targetLevel, false);
        }
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public void end(boolean interupt) {
        elevator.setPivotPosition(Level.LDefault, null);
    }
}

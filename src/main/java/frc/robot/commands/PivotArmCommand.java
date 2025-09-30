package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ElevatorSubsystem;
import frc.robot.subsystems.ElevatorSubsystem.Level;

public class PivotArmCommand extends Command {
    private final ElevatorSubsystem elevator;
    private final ElevatorSubsystem.Level targetLevel;
    static public boolean ArmFlipState;
    
    public PivotArmCommand(ElevatorSubsystem subsystem, ElevatorSubsystem.Level level, boolean ArmFlip) {
        elevator = subsystem;
        targetLevel = level;
        ArmFlipState = ArmFlip;
    }

    @Override
    public void initialize() {
        elevator.dumbPivotPID(targetLevel);
    }

    @Override
    public void execute() {
        SmartDashboard.putBoolean("armFlipState", ArmFlipState);
        SmartDashboard.putNumber("armDesire", targetLevel.pivotEncoderValue);
    }

    @Override
    public void end(boolean interupt) {
        elevator.setPivotPosition(Level.LDefault, false);
    }


}

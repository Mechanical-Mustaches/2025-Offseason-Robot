package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.IntakeSubsystem;

public class IntakeL1Command extends Command{
  IntakeSubsystem intake;

    public IntakeL1Command(IntakeSubsystem intake) {
        this.intake = intake;
    }

    @Override
    public void initialize() {
        intake.goToL1();
    }

    @Override
    public void execute(){
        if(intake.atPosition(intake.kIntakeL1Position)){
            intake.stopPivot();
        }
        intake.goToL1();
    }

    @Override
    public void end(boolean interupt) {
    intake.intakeStop();
    intake.pivotDropOff();
    }   

    // @Override
    // public boolean isFinished(){
    //     if(intake.atPosition(intake.kIntakeL1Position)){
    //         return true;
    //     }

    //     return false;
    // }
}

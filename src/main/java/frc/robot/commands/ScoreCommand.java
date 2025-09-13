package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.EndEffectorSubsystem;

public class ScoreCommand extends Command{
    EndEffectorSubsystem score;

    public ScoreCommand(EndEffectorSubsystem score) {
        this.score = score;
    }

    @Override
    public void initialize() {
        score.release();
    }

    @Override
    public void end(boolean interupt) {
        score.stop();
    }
}

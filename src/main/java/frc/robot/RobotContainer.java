// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.pathplanner.lib.auto.AutoBuilder;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.ElevatorFeedforward;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.CommandGenericHID;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.ElevatorCommand;
import frc.robot.commands.ElevatorSequentialCommand;
import frc.robot.commands.IntakeCommand;
import frc.robot.commands.PivotArmCommand;
import frc.robot.commands.ScoreCommand;
import frc.robot.subsystems.ElevatorSubsystem;
import frc.robot.subsystems.EndEffectorSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.SwerveDriveSubsystem;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in
 * the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of
 * the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private final SwerveDriveSubsystem swerveDriveSubsystem;
  private final IntakeSubsystem intakeSubsystem = new IntakeSubsystem();
  private final ElevatorSubsystem elevatorSubsystem = new ElevatorSubsystem();
  private final EndEffectorSubsystem endEffectorSubsystem = new EndEffectorSubsystem();

  private final SendableChooser<Command> autoChooser;

  private final CommandXboxController m_driverController = new CommandXboxController(
      OperatorConstants.kDriverControllerPort);

  private final XboxController driverController_HID = m_driverController.getHID();

  private final CommandGenericHID m_gunnerController = new CommandGenericHID(OperatorConstants.kGunnerControllerPort);

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    swerveDriveSubsystem = new SwerveDriveSubsystem(null);

    // Configure the trigger bindings
    configureBindings();

    autoChooser = AutoBuilder.buildAutoChooser();
    SmartDashboard.putData("Auto Chooser", autoChooser);
    SmartDashboard.putNumber("Test", 1);

    swerveDriveSubsystem.setDefaultCommand(swerveDriveSubsystem.driveCommand(
        () -> -MathUtil.applyDeadband(driverController_HID.getRawAxis(1), 0.1),
        () -> -MathUtil.applyDeadband(driverController_HID.getRawAxis(0), 0.1),
        () -> -MathUtil.applyDeadband(driverController_HID.getRawAxis(4), 0.1)));
  }

  private void configureBindings() {
    // Schedule `ExampleCommand` when `exampleCondition` changes to `true`

    // Schedule `exampleMethodCommand` when the Xbox controller's B button is
    // pressed,
    // cancelling on release.

    // Elevator

    m_gunnerController.button(1)
        .whileTrue(new PivotArmCommand(elevatorSubsystem, ElevatorSubsystem.Level.L2, false));


    m_gunnerController.button(3)
        .whileTrue(new ElevatorSequentialCommand(elevatorSubsystem, ElevatorSubsystem.Level.L4, 
        m_gunnerController.button(7).getAsBoolean()));
    m_gunnerController.button(6)
        .whileTrue(new ElevatorSequentialCommand(elevatorSubsystem, ElevatorSubsystem.Level.L3, 
        m_gunnerController.button(7).getAsBoolean()));
    m_gunnerController.button(9)
        .whileTrue(new ElevatorSequentialCommand(elevatorSubsystem, ElevatorSubsystem.Level.L2, 
        m_gunnerController.button(7).getAsBoolean()));
    m_gunnerController.button(12)
        .whileTrue(new ElevatorSequentialCommand(elevatorSubsystem, ElevatorSubsystem.Level.L1, 
        m_gunnerController.button(7).getAsBoolean()));
    m_gunnerController.button(5)
        .onTrue(new ElevatorCommand(elevatorSubsystem, ElevatorSubsystem.Level.LSource));
    m_gunnerController.button(8)
        .onTrue(new ElevatorCommand(elevatorSubsystem, ElevatorSubsystem.Level.LIntake));
    m_gunnerController.button(11)
        .onTrue(new ElevatorCommand(elevatorSubsystem, ElevatorSubsystem.Level.LDefault));


    m_driverController.povUp().onTrue(
        new InstantCommand(() -> elevatorSubsystem.dumbEleUp()));
     m_driverController.povUp().onFalse(
         new InstantCommand(() -> elevatorSubsystem.dumbEleStop()));
    m_driverController.povDown().onTrue(
        new InstantCommand(() -> elevatorSubsystem.dumbEleDown()));
     m_driverController.povDown().onFalse(
         new InstantCommand(() -> elevatorSubsystem.dumbEleStop()));

    m_driverController.povLeft().onTrue(
        new InstantCommand(() -> intakeSubsystem.dumbIntakePivotIn()));
     m_driverController.povLeft().onFalse(
         new InstantCommand(() -> intakeSubsystem.dumbIntakePivotStop()));
    m_driverController.povRight().onTrue(
        new InstantCommand(() -> intakeSubsystem.dumbIntakePivotOut()));
     m_driverController.povRight().onFalse(
         new InstantCommand(() -> intakeSubsystem.dumbIntakePivotStop()));
    

    // Game Pieces
    m_gunnerController.button(10)
    .whileTrue(new IntakeCommand(intakeSubsystem));
    m_gunnerController.button(4).whileTrue(
        new InstantCommand(() -> intakeSubsystem.intakeSpinOut()));
     m_gunnerController.button(4).onFalse(
         new InstantCommand(() -> intakeSubsystem.intakeStop()));
    m_gunnerController.button(2)
        .whileTrue(new ScoreCommand(endEffectorSubsystem));

  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return autoChooser.getSelected();
  }
}

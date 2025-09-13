// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.Autos;
import frc.robot.commands.ElevatorCommand;
import frc.robot.commands.ExampleCommand;
import frc.robot.commands.IntakeCommand;
import frc.robot.subsystems.ElevatorSubsystem;
import frc.robot.subsystems.ExampleSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.SwerveDriveSubsystem;

import com.pathplanner.lib.auto.AutoBuilder;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandGenericHID;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

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
  private final ExampleSubsystem m_exampleSubsystem = new ExampleSubsystem();
  private final IntakeSubsystem intakeSubsystem = new IntakeSubsystem();
  private final ElevatorSubsystem elevatorSubsystem = new ElevatorSubsystem();

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
    m_gunnerController.button(3)
        .onTrue(new ElevatorCommand(elevatorSubsystem, ElevatorSubsystem.Level.L4));
    m_gunnerController.button(6)
        .onTrue(new ElevatorCommand(elevatorSubsystem, ElevatorSubsystem.Level.L3));
    m_gunnerController.button(9)
        .onTrue(new ElevatorCommand(elevatorSubsystem, ElevatorSubsystem.Level.L2));
    m_gunnerController.button(12)
        .onTrue(new ElevatorCommand(elevatorSubsystem, ElevatorSubsystem.Level.L1));

    // Game Pieces
    m_gunnerController.button(5);
    m_gunnerController.button(8);
    m_gunnerController.button(11)
        .whileTrue(new IntakeCommand(intakeSubsystem));

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

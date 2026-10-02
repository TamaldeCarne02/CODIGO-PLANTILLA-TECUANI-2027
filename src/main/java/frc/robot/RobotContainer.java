// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PS4Controller.Button;
import frc.robot.Constants.AutoConstants;
import frc.robot.Constants.ClaimberConstants;
import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.IntakeConstants;
import frc.robot.Constants.OIConstants;
import frc.robot.auto.AutoBuilderConfig;
import frc.robot.auto.AutoChooser;
import frc.robot.auto.NamedCommandsReg;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Launcher;
import frc.robot.subsystems.Climber;
//import frc.robot.subsystems.ServoSubsystem;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SwerveControllerCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import java.util.List;
import frc.robot.Constants.LauncherConstants;
import edu.wpi.first.wpilibj2.command.Commands;

// PATHPLANNER IMPORTS
// IMPORTS DE PATHPLANNER - VERSIÓN UNIVERSAL
// IMPORTS PARA PATHPLANNER 2026
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.commands.PathPlannerAuto;
import com.pathplanner.lib.path.PathPlannerPath;
import com.pathplanner.lib.config.PIDConstants;
import com.pathplanner.lib.config.RobotConfig;
import com.pathplanner.lib.controllers.PPHolonomicDriveController;



@SuppressWarnings("unused")
public class RobotContainer {
    private final SendableChooser<Command> autoChooser;

    // Los subsistemas del robot
    private final DriveSubsystem m_robotDrive;
    private final Intake m_Intake;
    private final Launcher m_Launcher;
    private final Climber m_climber;

   // private final ServoSubsystem m_Servo = new ServoSubsystem();

    // The driver's controller
    XboxController m_driverController = new XboxController(OIConstants.kDriverControllerPort);
    CommandXboxController m_driverController2 = new CommandXboxController(OIConstants.kDriverControllerPort2);
    CommandXboxController m_operatorController = new CommandXboxController(OIConstants.kOperatorControllerPort);

    public RobotContainer() {
        // Inicializar subsistemas
        m_robotDrive = new DriveSubsystem();
        m_Intake = new Intake();
        m_Launcher = new Launcher();
        m_climber = new Climber();

        
    NamedCommandsReg.register(m_Launcher, m_Intake);   
    AutoBuilderConfig.configure(m_robotDrive);       
    autoChooser = AutoChooser.build(m_robotDrive, m_Launcher, m_Intake, m_climber); 

    configureDefaultCommands();
    configureButtonBindings();
    }

    private void configureDefaultCommands() {
        // Configure default commands
        m_robotDrive.setDefaultCommand(
            new RunCommand(
                () -> m_robotDrive.drive(
                    -MathUtil.applyDeadband(m_driverController.getLeftY(), OIConstants.kDriveDeadband),
                    -MathUtil.applyDeadband(m_driverController.getLeftX(), OIConstants.kDriveDeadband),
                    -MathUtil.applyDeadband(m_driverController.getRightX(), OIConstants.kDriveDeadband),
                    true),
                m_robotDrive));
    }

    private void configureButtonBindings() {
        // LAUNCHER - Usando los nuevos métodos
        m_operatorController.rightBumper()
            .onTrue(new InstantCommand(() -> m_Launcher.runLauncher(LauncherConstants.MOTOR_LAUNCHER_SPEED)))
            .onFalse(new InstantCommand(() -> m_Launcher.stopLauncher()));

                // LAUNCHER - Usando los nuevos métodos
        m_operatorController.a()
            .onTrue(new InstantCommand(() -> m_Launcher.runLauncher(LauncherConstants.MOTOR_LAUNCHER_SPEEDR)))
            .onFalse(new InstantCommand(() -> m_Launcher.stopLauncher()));
    
        //LAUNCHER LENTO
        m_operatorController.start()
         .onTrue(new InstantCommand(() -> m_Launcher.toggleLauncher()));

        // INTAKE 
        m_operatorController.leftTrigger()
            .onTrue(new InstantCommand(() -> m_Intake.runIntake1(IntakeConstants.MOTOR_INTAKE_SPEED)))
            .onFalse(new InstantCommand(() -> m_Intake.stopIntake()));

        m_operatorController.rightTrigger()
            .onTrue(new InstantCommand(() -> m_Intake.runIntake1(IntakeConstants.MOTOR_INTAKE_SPEEDR)))
            .onFalse(new InstantCommand(() -> m_Intake.stopIntake()));
    

        // INTAKE2
        m_operatorController.y()
            .onTrue(new InstantCommand(() -> m_Intake.m_Intake2.set(IntakeConstants.MOTOR_INTAKE2_SPEED)))
            .onFalse(new InstantCommand(() -> m_Intake.m_Intake2.set(0.0)));

        // INTAKE2 Back
        m_operatorController.b()
            .onTrue(new InstantCommand(() -> m_Intake.m_Intake2.set(IntakeConstants.MOTOR_INTAKE2_SPEEDB)))
            .onFalse(new InstantCommand(() -> m_Intake.m_Intake2.set(0.0)));

        // X configuration button (para modo X)
        new JoystickButton(m_driverController, XboxController.Button.kRightBumper.value)
            .whileTrue(new RunCommand(
                () -> m_robotDrive.setX(),
                m_robotDrive));

        // Zero heading button
        new JoystickButton(m_driverController, XboxController.Button.kStart.value)
            .onTrue(new InstantCommand(
                () -> m_robotDrive.zeroHeading(),
                m_robotDrive));
                 
                Translation2d targetPose = new Translation2d(4.62, 4.03);

        PIDController pid = new PIDController(0.3, 0, 0);
        pid.enableContinuousInput(-Math.PI, Math.PI);
        // Autoalineacion PRO
        boolean vealgo = LimelightHelpers.getTV("limelight");

        {
        
        m_driverController2.leftBumper().whileTrue(new RunCommand(
                () -> m_robotDrive.drive(
                        -MathUtil.applyDeadband(m_driverController.getLeftY(), OIConstants.kDriveDeadband),
                        -MathUtil.applyDeadband(m_driverController.getLeftX(), OIConstants.kDriveDeadband),
                        pid.calculate(m_robotDrive.getPose().getRotation().getRadians(),
                                getTargetRotation(m_robotDrive.getPose(), targetPose).getRadians()),
                        true),
    
                m_robotDrive));
    }
    // SUBIR: Mientras presionas Y, el climber sube. Al soltar, se detiene.
    m_driverController2.x()
    .whileTrue(new RunCommand(() -> m_climber.runClimber(ClaimberConstants.MOTOR_CLAIMBER_SPEED), m_climber))
    .onFalse(new InstantCommand(() -> m_climber.stopClimber(), m_climber));

    // BAJAR: Mientras presionas A, el climber baja (velocidad negativa). Al soltar, se detiene.
    m_driverController2.b()
      .whileTrue(new RunCommand(() -> m_climber.runClimber(ClaimberConstants.MOTOR_CLAIMBER_SPEED2), m_climber))
        .onFalse(new InstantCommand(() -> m_climber.stopClimber(), m_climber));

    }
    

    public Command getAutonomousCommand() {
        // Retorna el auto seleccionado en SmartDashboard
        return autoChooser.getSelected();
    }


    public Rotation2d getTargetRotation(Pose2d currentPose, Translation2d targetPose) {
        Translation2d difference = targetPose.minus(currentPose.getTranslation());
        return difference.getAngle();
    }
}
    





package frc.robot.auto;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathPlannerPath;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;

import frc.robot.subsystems.DriveSubsystem;

import frc.robot.subsystems.Intake;
import frc.robot.Constants.IntakeConstants;

import frc.robot.subsystems.Launcher;
import frc.robot.Constants.LauncherConstants;;

public class AutoCommands {
    /** Prende el launcher, espera spin-up, dispara durante N segundos, apaga. */
    public static Command scoreNote(Launcher launcher, double seconds) {
        return Commands.sequence(
            new InstantCommand(() -> launcher.runLauncher(LauncherConstants.MOTOR_LAUNCHER_SPEEDL)),
            Commands.waitSeconds(1.0),
            new InstantCommand(() -> launcher.runLauncher(LauncherConstants.MOTOR_LAUNCHER_SPEED)),
            Commands.waitSeconds(seconds),
            new InstantCommand(launcher::stopLauncher)
        );
    }

    /** Recoge mientras sigue una ruta. */
    public static Command intakeWhileFollowing(Intake intake, PathPlannerPath path) {
        return AutoBuilder.followPath(path)
            .deadlineWith(
                Commands.run(() -> intake.runIntake1(IntakeConstants.MOTOR_INTAKE_SPEED), intake)
            )
            .andThen(new InstantCommand(intake::stopIntake));
    }

    /** Resetea heading y odometría al inicio de un auto. */
    public static Command resetFromPath(DriveSubsystem drive, PathPlannerPath path) {
        return Commands.sequence(
            new InstantCommand(drive::zeroHeading),
            new InstantCommand(() -> drive.resetOdometry(
                path.getStartingHolonomicPose().orElse(new Pose2d())
            ))
        );
    }
}

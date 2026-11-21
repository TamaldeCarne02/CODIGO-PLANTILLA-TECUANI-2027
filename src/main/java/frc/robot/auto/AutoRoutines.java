package frc.robot.auto;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathPlannerPath;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Launcher;

public class AutoRoutines {

    public static Command getAutoDispara(DriveSubsystem drive, Launcher launcher) {
    try {
        PathPlannerPath path1 = PathPlannerPath.fromPathFile("DISPARAA");
        return Commands.sequence(
            AutoCommands.resetFromPath(drive, path1),
            AutoBuilder.followPath(path1),
            AutoCommands.scoreNote(launcher, 8)
        );
        
    } catch (Exception e) {
        DriverStation.reportError("Auto blueLeftComplete falló", e.getStackTrace());
            return Commands.none();
        }
    }


      public static Command getAutoBlueLeftComplete(DriveSubsystem drive, Launcher launcher, Intake intake) {
        try {
            PathPlannerPath path1 = PathPlannerPath.fromPathFile("LANZA8B");
            PathPlannerPath path2 = PathPlannerPath.fromPathFile("RECOGER");
            PathPlannerPath path3 = PathPlannerPath.fromPathFile("DESORDENAR");
            PathPlannerPath path4 = PathPlannerPath.fromPathFile("HUMANB");

            return Commands.sequence(
                AutoCommands.resetFromPath(drive, path1),
                AutoBuilder.followPath(path1),
                AutoCommands.scoreNote(launcher, 5.0),
                AutoBuilder.followPath(path2),
                AutoCommands.intakeWhileFollowing(intake, path3),
                AutoBuilder.followPath(path4)
            );
        } catch (Exception e) {
            DriverStation.reportError("Auto blueLeftComplete falló", e.getStackTrace());
            return Commands.none();
        }
    }
}

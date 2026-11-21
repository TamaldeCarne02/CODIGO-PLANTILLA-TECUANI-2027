package frc.robot.auto;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.config.PIDConstants;
import com.pathplanner.lib.config.RobotConfig;
import com.pathplanner.lib.controllers.PPHolonomicDriveController;

import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.subsystems.DriveSubsystem;

public class AutoBuilderConfig {

    public static void configure(DriveSubsystem drive) {
        try {
            RobotConfig config = RobotConfig.fromGUISettings();
            AutoBuilder.configure(
                drive::getPose,
                drive::resetOdometry,
                drive::getRobotRelativeSpeeds,
                (speeds, ff) -> drive.driveRobotRelative(speeds),
                new PPHolonomicDriveController(
                    new PIDConstants(5.0, 0, 0),
                    new PIDConstants(5.0, 0, 0)
                ),
                config,
                () -> DriverStation.getAlliance()
                        .map(a -> a == DriverStation.Alliance.Red)
                        .orElse(false),
                drive
            );
        } catch (Exception e) {
            DriverStation.reportError("AutoBuilder falló", e.getStackTrace());
        }
    }
    
}

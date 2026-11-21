package frc.robot.auto;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Launcher;

public class AutoChooser {
      public static SendableChooser<Command> build(
        DriveSubsystem drive, Launcher launcher,
        Intake intake, Climber climber) {

        SendableChooser<Command> chooser = new SendableChooser<>();

        chooser.setDefaultOption("Ninguno", Commands.none());
        chooser.addOption("Blue Izquierda", AutoRoutines.getAutoBlueLeftComplete(drive, launcher, intake));
        chooser.addOption("Dispara", AutoRoutines.getAutoDispara(drive,launcher));

        SmartDashboard.putData("Auto Selector", chooser);
        return chooser;
    }
}

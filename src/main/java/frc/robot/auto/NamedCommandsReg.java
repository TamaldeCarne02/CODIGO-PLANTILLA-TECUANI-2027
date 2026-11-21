package frc.robot.auto;

import com.pathplanner.lib.auto.NamedCommands;

import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Launcher;
import frc.robot.Constants.LauncherConstants;;

public class NamedCommandsReg {
     public static void register(Launcher launcher, Intake intake) {
        NamedCommands.registerCommand("Shoot",
            AutoCommands.scoreNote(launcher, 1.5));
        NamedCommands.registerCommand("Intake",
            Commands.run(() -> intake.runIntake1(LauncherConstants.MOTOR_LAUNCHER_SPEED), intake)
                .withTimeout(1.5)
                .andThen(new InstantCommand(intake::stopIntake)));
        NamedCommands.registerCommand("StopIntake",
            new InstantCommand(intake::stopIntake));
    }
    
}

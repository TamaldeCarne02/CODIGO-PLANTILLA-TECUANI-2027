package frc.robot.command;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.subsystems.DriveSubsystem;

public class PrepareShotCommand extends Command {
    private final DriveSubsystem m_drive = new DriveSubsystem();
    

    private static final Pose2d HUB_POSE = new Pose2d(4.60, 4.0, Rotation2d.fromDegrees(0.0));


    // Dentro de tu RobotContainer o un comando específico

    // Crear el PID de rotación (Calibrar valores según tu robot)
    public PIDController headingPID = new PIDController(2.0, 0.0, 0.6);


    // Crear un comando que se ejecute mientras se mantiene presionado un botón
public Command alignToHeadingCommand() {
    return new RunCommand(() -> {
        // Obtener el ángulo actual del robot desde tu odometría/pose estimator
        Rotation2d currentHeading = m_drive.getPose().getRotation();
        Pose2d currentRobot = m_drive.getPose();

        double target = Math.tanh(HUB_POSE.getY()-currentRobot.getY()) / (HUB_POSE.getX()-currentRobot.getX());

        Rotation2d targetHeading = Rotation2d.fromDegrees(target);

        // Calcular la salida del PID (esto reemplaza al withHeadingPID)
        double rotationOutput = headingPID.calculate(currentHeading.getRadians(), targetHeading.getRadians());

        // Usar el método drive de tu DriveSubsystem (que ya tienes)
        // Pasas las velocidades X e Y que desees (pueden ser 0) y la rotación calculada
        m_drive.drive(
            0, // Velocidad X (adelante)
            0, // Velocidad Y (lateral)
            rotationOutput, // Rotación calculada por el PID
            true // Field-relative
        );
    }, m_drive); // El comando requiere el subsistema del drivetrain
}


}

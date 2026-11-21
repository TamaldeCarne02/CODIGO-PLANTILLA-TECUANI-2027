package frc.robot.command;

import static edu.wpi.first.units.Units.Degrees;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.function.DoubleSupplier;

import com.studica.frc.AHRS;
import com.studica.frc.AHRS.NavXComType;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.Constants.Driving;
import frc.robot.Landmarks;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.MAXSwerveModule;
import frc.util.DriveInputSmoother;
import frc.util.GeometryUtil;
import frc.util.ManualDriveInput;

public class PrepareShotCommand extends Command {
    private final AHRS m_gyro = new AHRS(NavXComType.kMXP_SPI);
    

    private static final Pose2d HUB_POSE = new Pose2d(4.60, 4.0, Rotation2d.fromDegrees(0.0));


// Dentro de tu RobotContainer o un comando específico

// 1. Crear el PID de rotación (equivalente al withHeadingPID(5, 0, 0))
    public PIDController headingPID = new PIDController(5.0, 0.0, 0.0);
// Opcional: hacer que el PID funcione con ángulos continuos para evitar saltos
// headingPID.enableContinuousInput(-Math.PI, Math.PI);

// 2. Ángulo deseado (por ejemplo, apuntar a 180 grados)
public Pose2d pose2drobot = new Pose2d();
public double target = Math.tanh(HUB_POSE.getY()-pose2drobot.getY()) / (HUB_POSE.getX()-pose2drobot.getX());




Rotation2d targetHeading = Rotation2d.fromDegrees(80);

// 3. Crear un comando que se ejecute mientras se mantiene presionado un botón
public Command alignToHeadingCommand() {
    return new RunCommand(() -> {
        // Obtener el ángulo actual del robot desde tu odometría/pose estimator
        Rotation2d currentHeading = m_robotDrive.getPose().getRotation();

        // Calcular la salida del PID (esto reemplaza al withHeadingPID)
        double rotationOutput = headingPID.calculate(currentHeading.getRadians(), targetHeading.getRadians());

        // Usar el método drive de tu DriveSubsystem (que ya tienes)
        // Pasas las velocidades X e Y que desees (pueden ser 0) y la rotación calculada
        m_robotDrive.drive(
            0, // Velocidad X (adelante)
            0, // Velocidad Y (lateral)
            rotationOutput, // Rotación calculada por el PID
            true // Field-relative
        );
    }, m_robotDrive); // El comando requiere el subsistema del drivetrain
}


}

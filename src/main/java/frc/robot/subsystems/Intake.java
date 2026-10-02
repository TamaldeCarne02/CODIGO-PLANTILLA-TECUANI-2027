package frc.robot.subsystems;

import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.Constants.IntakeConstants;

public class Intake extends SubsystemBase {

    // Declarando nuestros SparkMax
    public final SparkMax m_Intake = new SparkMax(IntakeConstants.MOTOR_INTAKE_PORT, MotorType.kBrushless);
    public final SparkMax m_Intake2 = new SparkMax(IntakeConstants.MOTOR_INTAKE2_PORT, MotorType.kBrushless);

    // Declarando las configuraciones
    private SparkMaxConfig m_motorIntakeConfig = new SparkMaxConfig();
    private SparkMaxConfig m_motorIntake2Config = new SparkMaxConfig();
    
    // Variable para trackear el estado
    private boolean isRunning = false;
    private double currentSpeed = 0.0;

    @SuppressWarnings("removal")
    public Intake() {
        // Configuración del motor 1 (intake principal)
        m_motorIntakeConfig
            .inverted(false)
            .idleMode(IdleMode.kBrake)
            .smartCurrentLimit(40);  // Límite de corriente

        // Configuración del motor 2 (intake secundario)
        m_motorIntake2Config
            .inverted(true)
            .idleMode(IdleMode.kBrake)
            .smartCurrentLimit(40)   // Límite de corriente
            .encoder.positionConversionFactor(360.0);  // Conversión a grados

        // Límites de movimiento para el motor 2 (si tiene un mecanismo limitado)
        m_motorIntake2Config.softLimit
            .forwardSoftLimitEnabled(true)
            .forwardSoftLimit(100.0)
            .reverseSoftLimitEnabled(true)
            .reverseSoftLimit(0.0);

        // Aplicar configuraciones
        m_Intake.configure(m_motorIntakeConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        m_Intake2.configure(m_motorIntake2Config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        
        System.out.println("Intake subsystem initialized");
    }

    @Override
    public void periodic() {
        // Útil para debugging - muestra el estado en SmartDashboard
        // SmartDashboard.putBoolean("Intake Running", isRunning);
        // SmartDashboard.putNumber("Intake Speed", currentSpeed);
    }

    /**
     * Activa ambos motores del intake a la velocidad especificada
     */
    public void runIntake(double speed) {
        m_Intake.set(speed);
        m_Intake2.set(speed);
        currentSpeed = speed;
        isRunning = true;
        System.out.println("Intake ON - Velocidad: " + speed);
    }

    /**
     * Activa SOLO el motor principal del intake
     */
    public void runIntake1(double speed) {
        m_Intake.set(speed);
        m_Intake2.set(0.0);
        currentSpeed = speed;
        isRunning = true;
        System.out.println("Intake 1 ON - Velocidad: " + speed);
    }

    /**
     * Activa SOLO el motor secundario del intake (para expulsar)
     */
    public void runIntake2(double speed) {
        m_Intake.set(0.0);
        m_Intake2.set(speed);
        currentSpeed = speed;
        isRunning = true;
        System.out.println("Intake 2 ON - Velocidad: " + speed);
    }

    /**
     * Activa el motor secundario en reversa (para expulsar)
     */
    public void runIntake2Reverse(double speed) {
        m_Intake.set(0.0);
        m_Intake2.set(-speed);  // Velocidad negativa para reversa
        currentSpeed = -speed;
        isRunning = true;
        System.out.println("Intake 2 REVERSE - Velocidad: " + speed);
    }

    /**
     * Detiene TODOS los motores del intake
     */
    public void stopIntake() {
        m_Intake.set(0.0);
        m_Intake2.set(0.0);
        currentSpeed = 0.0;
        isRunning = false;
        System.out.println("Intake OFF");
    }

    /**
     * Alterna el estado del intake (encender/apagar)
     */
    public void toggleIntake() {
        if (isRunning) {
            stopIntake();
        } else {
            runIntake(IntakeConstants.MOTOR_INTAKE_SPEED);
        }
    }

    /**
     * Alterna el intake2 en reversa (para expulsar)
     */
    public void toggleIntake2Reverse() {
        if (isRunning && currentSpeed < 0) {
            stopIntake();
        } else {
            runIntake2Reverse(IntakeConstants.MOTOR_INTAKE2_SPEEDB);
        }
    }

    /**
     * Verifica si el intake está funcionando
     */
    public boolean isRunning() {
        return isRunning;
    }

    /**
     * Obtiene la velocidad actual
     */
    public double getCurrentSpeed() {
        return currentSpeed;
    }

    // ============ MÉTODOS PARA COMANDOS ============

    /**
     * Comando para mover el intake por un tiempo determinado
     */
    public Command runIntakeCommand(double speed) {
        return Commands.startEnd(
            () -> runIntake(speed),
            () -> stopIntake(),
            this
        ).withName("RunIntake");
    }

    /**
     * Comando para mover SOLO el intake principal
     */
    public Command runIntake1Command(double speed) {
        return Commands.startEnd(
            () -> runIntake1(speed),
            () -> stopIntake(),
            this
        ).withName("RunIntake1");
    }

    /**
     * Comando para mover SOLO el intake secundario
     */
    public Command runIntake2Command(double speed) {
        return Commands.startEnd(
            () -> runIntake2(speed),
            () -> stopIntake(),
            this
        ).withName("RunIntake2");
    }

    /**
     * Comando para expulsar (intake2 en reversa)
     */
    public Command ejectCommand() {
        return Commands.startEnd(
            () -> runIntake2Reverse(IntakeConstants.MOTOR_INTAKE2_SPEEDB),
            () -> stopIntake(),
            this
        ).withTimeout(1.0)  // Se ejecuta por 1 segundo
        .withName("Eject");
    }

    /**
     * Comando para alternar el intake (on/off)
     */
    public Command toggleIntakeCommand() {
        return Commands.runOnce(
            () -> toggleIntake(),
            this
        ).withName("ToggleIntake");
    }

    /**
     * Comando para alternar el intake2 en reversa
     */
    public Command toggleIntake2ReverseCommand() {
        return Commands.runOnce(
            () -> toggleIntake2Reverse(),
            this
        ).withName("ToggleIntake2Reverse");
    }
}
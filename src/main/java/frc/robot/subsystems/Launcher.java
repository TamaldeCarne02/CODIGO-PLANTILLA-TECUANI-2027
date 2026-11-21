package frc.robot.subsystems;

import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.LauncherConstants;

public class Launcher extends SubsystemBase {

    // Declarando nuestros SparkMax
    public final SparkMax m_Launcher = new SparkMax(LauncherConstants.MOTOR_LAUNCHER_PORT, MotorType.kBrushless);
    public final SparkMax m_Launcher2 = new SparkMax(LauncherConstants.MOTOR_LAUNCHER_PORT2, MotorType.kBrushless);

    // Configuraciones
    private SparkMaxConfig m_motorLauncherConfig = new SparkMaxConfig();
    private SparkMaxConfig m_motorLauncherConfig2 = new SparkMaxConfig();

    @SuppressWarnings("removal")
    public Launcher() {
        // Configurar motor 1
        m_motorLauncherConfig
            .inverted(true)
            .idleMode(IdleMode.kBrake);
        
        // Configurar motor 2
        m_motorLauncherConfig2
            .inverted(false)
            .idleMode(IdleMode.kBrake);

        // Aplicar configuraciones
        m_Launcher.configure(m_motorLauncherConfig, ResetMode.kNoResetSafeParameters, PersistMode.kPersistParameters);
        m_Launcher2.configure(m_motorLauncherConfig2, ResetMode.kNoResetSafeParameters, PersistMode.kPersistParameters);
    }

    @Override
    public void periodic() {
        // Útil para debugging
    }

    // Método para activar ambos motores del launcher
    public void runLauncher(double speed) {
        m_Launcher.set(speed);
        m_Launcher2.set(speed);
        System.out.println("Launcher running at: " + speed);
    }

        public void runLauncherlento(double speed) {
        m_Launcher.set(LauncherConstants.MOTOR_LAUNCHER_SPEEDL);
        m_Launcher2.set(LauncherConstants.MOTOR_LAUNCHER_SPEEDL);
        System.out.println("Launcher /running at: " + speed);
    }

    // Método para detener el launcher
    public void stopLauncher() {
        m_Launcher.set(0.0);
        m_Launcher2.set(0.0);
        System.out.println("Launcher stopped");
    }

    // Método para verificar si el launcher está funcionando
    public boolean isRunning() {
        return Math.abs(m_Launcher.get()) > 0.1 || Math.abs(m_Launcher2.get()) > 0.1;
    }

      public void toggleLauncher() {
    if (m_Launcher.get() != 0) { // Si está encendido
        m_Launcher.set(0);
        m_Launcher2.set(0);
    } else { // Si está apagado
        m_Launcher.set(LauncherConstants.MOTOR_LAUNCHER_SPEEDL);
        m_Launcher2.set(LauncherConstants.MOTOR_LAUNCHER_SPEEDL);
    }

}

}
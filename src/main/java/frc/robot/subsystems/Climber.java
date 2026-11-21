package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ClaimberConstants;

public class Climber extends SubsystemBase {
  // Definimos el motor (asumiendo que es un SparkMax, cámbialo si es TalonSRX)
  private final SparkMax m_climberMotor = new SparkMax(ClaimberConstants.MOTOR_CLAIMBER_PORT, MotorType.kBrushless);


  /** Metodo para subir el climber */
  public void runClimber(double speed) {
    m_climberMotor.set(speed);
  }

  /** Metodo para detener el motor */
  public void stopClimber() {
    m_climberMotor.set(0);
  }

  @Override
  public void periodic() {
    // Este metodo se ejecuta cada 20ms
  }


}
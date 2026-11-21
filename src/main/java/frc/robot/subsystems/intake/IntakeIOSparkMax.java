package frc.robot.subsystems.intake;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import frc.robot.Constants.IntakeConstants;



public class IntakeIOSparkMax implements IntakeIO {
    private final SparkMax m_intake;
    private SparkMaxConfig m_motorIntakeConfig = new SparkMaxConfig();


    public IntakeIOSparkMax() {
        m_intake = new SparkMax(IntakeConstants.MOTOR_INTAKE_PORT, MotorType.kBrushless);


        m_motorIntakeConfig = (SparkMaxConfig) new SparkMaxConfig()
            .inverted(false)
            .idleMode(IdleMode.kBrake)
            .smartCurrentLimit(40);

        m_intake.configure(m_motorIntakeConfig,ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    }

    @Override
    public void updateInputs(IntakeIOInputs inputs) {
        inputs.velocityRPM = m_intake.getEncoder().getVelocity();
        inputs.currentAmps = m_intake.getOutputCurrent();
    }
    @Override
    public void setVoltage(double volts) {
        m_intake.setVoltage(volts);
    }
    @Override
    public void stop() {
        m_intake.setVoltage(0);
    }   

}




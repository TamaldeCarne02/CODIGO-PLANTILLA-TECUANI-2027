package frc.robot.subsystems.intake;

import org.littletonrobotics.junction.AutoLog;

public interface IntakeIO {
    @AutoLog
    public static class IntakeIOInputs {
        public double velocityRPM = 0.0;
        public double currentAmps = 0.0;
        public boolean atLimit = false;
    
        
    }
    
    public default void updateInputs(IntakeIOInputs inputs) {}
    public default void setVoltage(double volts) {}
    public default void stop() {}
}

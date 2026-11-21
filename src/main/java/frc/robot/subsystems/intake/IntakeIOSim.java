package frc.robot.subsystems.intake;

import edu.wpi.first.math.system.LinearSystem;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.wpilibj.simulation.DCMotorSim;
import edu.wpi.first.wpilibj.simulation.LinearSystemSim;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.wpilibj.RobotBase;

public class IntakeIOSim implements IntakeIO {
    private final DCMotorSim sim;
    private double simVoltage = 0.0;


    public IntakeIOSim() {
        sim = new DCMotorSim(LinearSystemId.createDCMotorSystem(DCMotor.getNEO(1),
        0.001,
        1),DCMotor.getNEO(1));
    }

    @Override
    public void updateInputs(IntakeIOInputs inputs) {
        sim.update(0.02); // Avanza la simulación 20 ms
        inputs.velocityRPM = sim.getAngularVelocityRPM();
        inputs.currentAmps = sim.getCurrentDrawAmps();
        inputs.atLimit = false; // Lógica de simulación si es necesario
    }

    @Override
    public void setVoltage(double volts) {
        simVoltage = volts;
        sim.setInputVoltage(volts);
    }

    @Override
    public void stop() {
        simVoltage = 0.0;
        sim.setInputVoltage(0.0);
    }
}
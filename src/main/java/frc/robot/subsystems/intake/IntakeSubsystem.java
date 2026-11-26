package frc.robot.subsystems.intake;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.inputs.LoggableInputs;
import frc.robot.subsystems.intake.IntakeIOInputsAutoLogged;

public class IntakeSubsystem extends SubsystemBase {

    private final IntakeIO io;
    private final IntakeIOInputsAutoLogged inputs = new IntakeIOInputsAutoLogged();

    
    public IntakeSubsystem(IntakeIO io) {
        this.io = io;
    }

    @Override
    public void periodic() {
        // 1. Actualiza los datos desde la capa IO (real o sim)
        io.updateInputs(inputs);
        // 2. Envía los datos al logger de AdvantageKit
        Logger.processInputs("Intake", inputs);
    }

    // Métodos de alto nivel que usan los comandos
    public void runIntake() {
        io.setVoltage(12.0); // Voltaje nominal para recoger
    }

    public void stop() {
        io.stop();
    }

    public boolean hasNote() {
        return inputs.atLimit;
    }

    // Fábricas de comandos
    public Command runIntakeCommand() {
        return Commands.startEnd(this::runIntake, this::stop, this);
    }
}
package frc.robot.commands;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.IndexerSubsystem;
import frc.robot.subsystems.IntakeSubsystem;

public class IntakeCommand extends Command {
    private final IntakeSubsystem intakeSubsystem;

    private final IndexerSubsystem indexerSubsystem;

    private final LEDSubsystem ledSubsystem;

    private final Targets target;

    private final boolean source;



    private final XboxController primaryController;
    private final XboxController secondaryController;

    //private LEDSubsystem ledSubsystem;

    /**
     * Command to run the intake
     * @param intakeSubsystem The instance of {@link IntakeSubsystem}
     * @param indexerSubsystem The instance of {@link IndexerSubsystem} (needed for limebreak detection to stop intake motor)
     */
    public IntakeCommand(IntakeSubsystem intakeSubsystem, IndexerSubsystem indexerSubsystem, LEDSubsystem ledSubsystem, Targets target, boolean source) {
        this.intakeSubsystem = intakeSubsystem;
        this.indexerSubsystem = indexerSubsystem;
        this.ledSubsystem = ledSubsystem;
        this.target = target;
        this.source = source;
        this.primaryController = primaryController;
        this.secondaryController = secondaryController;

        addRequirements(intakeSubsystem, indexerSubsystem);
    }


    @Override
    public void execute() {
        if (!indexerSubsystem.isCenter())
            ledSubsystem.setLED(0.5);

        if (source) {
            indexerSubsystem.moveIndexerToPos(Math.toRadians(140));
        } else {
//        if (!indexerSubsystem.isCenter()) {
//

        switch (target) {
            case AMP -> {
//                if (!indexerSubsystem.isTop()) {
                    indexerSubsystem.rotateMotorPercent(IndexerSubsystem.IndexerMotors.TOP_WHEEL, 0.22);
                    indexerSubsystem.rotateMotorPercent(IndexerSubsystem.IndexerMotors.BOTTOM_WHEELS, -0.22);
                    intakeSubsystem.setTopSpeed(-0.4);
                    intakeSubsystem.setBottomSpeed(-0.7);
//                } else if (indexerSubsystem.isTop()) {
                    indexerSubsystem.rotateAllWheelsPercent(0.0);
                    intakeSubsystem.setSpeed(0.0);
//                }
            }
            case SPEAKER -> {
                if (!indexerSubsystem.isCenter()) {
                    intakeSubsystem.setTopSpeed(0.4);
                    intakeSubsystem.setBottomSpeed(0.4);
                    indexerSubsystem.rotateAllWheelsPercent(0.3);
                } else if (indexerSubsystem.isCenter()) {
                   // intakeSubsystem.setSpeed(0);
                    try {
                        Thread.sleep(105);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (indexerSubsystem.isCenter()) {
                        indexerSubsystem.rotateAllWheelsPercent(0);
                        intakeSubsystem.setSpeed(0.0);
                        primaryController.setRumble(GenericHID.RumbleType.kBothRumble, 1.0);
                        secondaryController.setRumble(GenericHID.RumbleType.kBothRumble, 1.0);
                    }

                }
            }
        }
    }

    @Override
    public void end(boolean interrupted) {
        intakeSubsystem.setSpeed(0);
        indexerSubsystem.rotateAllWheelsPercent(0);
    }

    public enum Targets {
        AMP,
        SPEAKER
    }
}

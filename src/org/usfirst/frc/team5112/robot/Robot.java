package org.usfirst.frc.team5112.robot;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Joystick.RumbleType;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends SampleRobot {
	RobotDrive robotDrive;
	Joystick stick, xbox;
	Solenoid gripper;
	Jaguar pulleyControl;
	Compressor compressor;
	DriverStation ds;
	boolean compressorOff = false;
	Integer auto;
	SendableChooser autoChooser;

	public Robot() {
		autoChooser = new SendableChooser();
		autoChooser
				.addDefault("Bin/Tote Auto Location Based", new Integer(100));
		autoChooser.addObject("Bin/Tote Auto Step", new Integer(8));
		autoChooser.addObject("Bin/Tote Auto No Step", new Integer(4));
		autoChooser.addObject("Zone Auto", new Integer(0));
		autoChooser.addObject("Landfill Auto", new Integer(5));
		autoChooser.addObject("Do Nothing Auto", new Integer(2));
		autoChooser.addObject("Three Tote", new Integer(10));
		SmartDashboard.putData("Autonomous mode chooser", autoChooser);
		ds = DriverStation.getInstance();
		// Set up RobotDrive to control all of our motors
		robotDrive = new RobotDrive(RobotMap.frontLeftMotor,
				RobotMap.rearLeftMotor, RobotMap.frontRightMotor,
				RobotMap.rearRightMotor);
		// Motors are inverted
		robotDrive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, false);
		robotDrive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, false);
		robotDrive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
		robotDrive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);

		// Here are the controllers
		stick = new Joystick(RobotMap.joystickPort);
		xbox = new Joystick(RobotMap.xboxPort);
		// compressor
		compressor = new Compressor();
		compressor.setClosedLoopControl(true);

		// Pulley motor
		pulleyControl = new Jaguar(RobotMap.pulley);

		// Solenoid
		gripper = new Solenoid(RobotMap.solenoid);

	}

	/*
	 * Autonomous
	 */
	public void autonomous() {
		auto = (Integer) autoChooser.getSelected();
		robotDrive.setSafetyEnabled(false);
		switch (auto) {
		case AutonomousValues.TOTE:
			toteAuto();
			break;
		case AutonomousValues.ZONE:
			zoneAuto();
			break;
		case AutonomousValues.DO_NOTHING:
			doNothing();
			break;
		case AutonomousValues.SIDE_TOTE:
			sideToteAuto();
			break;
		case AutonomousValues.BIN:
			binAuto();
			break;
		case AutonomousValues.DRAG_TOTE:
			dragToteAuto();
			break;
		case AutonomousValues.ROTATE_NO_STEP:
			rotateAuto(1.8);
			break;
		case AutonomousValues.ROTATE_STEP:
			rotateAuto(2.2);
			break;
		case AutonomousValues.BIN_STEP:
			binAutoStep();
			break;
		case AutonomousValues.THREE_TOTE:
			threeTote();
			break;
		case AutonomousValues.DEFAULT:
			if (ds.getLocation() == 1) {
				binAuto();
			} else {
				binAutoStep();
			}
			break;
		default:
			doNothing();
			break;
		}
	}

	/*
	 * Tele-op
	 */
	public void operatorControl() {
		robotDrive.setSafetyEnabled(true);
		while (isOperatorControl() && isEnabled()) {

			operateDrivetrain();

			operateGripper();

			operatePulley();

			controlCompressor();

			humanPlayerStation();

			Timer.delay(0.005); // wait for a motor update time
		}
	}

	/*
	 * Auto Programs
	 */

	private void threeTote() {
		closeGripper();
		Timer.delay(0.3);
		up(0.5);
		Timer.delay(0.8);
		stopPulley();
		Timer.delay(0.3);
		driveLeft(0.5);
		Timer.delay(1.0);
		stop();
		driveForward(0.6);
		Timer.delay(0.8);
		stop();
		driveRight(0.5);
		Timer.delay(0.55);
		stop();
		down(0.5);
		Timer.delay(0.5);
		openGripper();
		Timer.delay(0.1);
		stopPulley();
		closeGripper();
		up(0.8);
		driveLeft(0.5);
		Timer.delay(1.5);
		stopPulley();
		stop();
		driveForward(0.6);
		Timer.delay(0.8);
		stop();
		driveRight(0.5);
		Timer.delay(0.55);
		stop();
		down(0.5);
		Timer.delay(0.5);
		openGripper();
		Timer.delay(0.1);
		closeGripper();
		up(0.8);
		Timer.delay(0.2);
		stopPulley();
		driveRight(0.8);
		Timer.delay(1.8);
		stop();
		down(0.4);
		Timer.delay(0.5);
		stopPulley();
		openGripper();
		driveBackward(0.5);
		Timer.delay(0.3);
		stop();

	}

	private void zoneAuto() {
		driveBackward(0.5);
		Timer.delay(0.8);
		stop();
	}

	private void doNothing() {
		stop();
	}

	private void dragToteAuto() {
		closeGripper();
		Timer.delay(0.3);
		driveForward(0.2);
		Timer.delay(1.1);
		stop();
		Timer.delay(0.2);
		down(0.4);
		Timer.delay(0.5);
		stopPulley();
		Timer.delay(0.2);
		driveBackward(0.4);
		Timer.delay(1.74);
		stop();
		Timer.delay(0.4);
		up(0.4);
		Timer.delay(0.5);
		stopPulley();
		Timer.delay(0.3);
		openGripper();
	}

	private void rotateAuto(double time) {
		closeGripper();
		Timer.delay(0.3);
		rotateCW(0.4);
		Timer.delay(1.0);
		stop();
		Timer.delay(0.3);
		driveForward(0.5);
		Timer.delay(time);
		stop();
		Timer.delay(0.3);
		openGripper();

	}

	private void toteAuto() {
		closeGripper();
		Timer.delay(0.4);
		driveBackward(0.5);
		Timer.delay(2.6);
		stop();
		Timer.delay(0.4);
		openGripper();
		Timer.delay(0.4);
		driveBackward(0.2);
		Timer.delay(0.2);
		stop();
	}

	private void sideToteAuto() {
		closeGripper();
		Timer.delay(0.4);
		driveRight(0.8);
		Timer.delay(4.3);
		stop();
		Timer.delay(0.4);
		openGripper();
	}

	private void binAutoStep() {
		/*
		 * driveForward(0.2); Timer.delay(0.3); stop(); Timer.delay(0.4);
		 */
		closeGripper();
		Timer.delay(0.4);
		up(0.4);
		Timer.delay(0.9);
		stopPulley();
		Timer.delay(0.4);
		driveBackward(0.8);
		Timer.delay(1.66);
		stop();
		Timer.delay(0.4);
		down(0.4);
		Timer.delay(0.9);
		stopPulley();
		Timer.delay(0.7);
		openGripper();
		driveBackward(0.2);
		Timer.delay(0.2);
		stop();
	}

	private void binAuto() {
		/*
		 * driveForward(0.2); Timer.delay(0.3); stop(); Timer.delay(0.4);
		 */
		closeGripper();
		Timer.delay(0.4);
		up(0.4);
		Timer.delay(0.8);
		stopPulley();
		Timer.delay(0.4);
		driveBackward(0.5);
		Timer.delay(1.5);
		stop();
		Timer.delay(0.4);
		down(0.4);
		Timer.delay(0.5);
		stopPulley();
		Timer.delay(0.7);
		openGripper();
		driveBackward(0.2);
		Timer.delay(0.2);
		stop();
	}

	/*
	 * Driving
	 */
	private void operateDrivetrain() {
		if (stick.getRawButton(3)) {
			driveLeft(0.8);
		} else if (stick.getRawButton(4)) {
			driveRight(0.8);
		}else {
		
			double throttle = stick.getThrottle();
			if (throttle <= 0) {
				throttle = 0.6;
			}
			double rotation = stick.getZ();
			if (Math.abs(rotation) < 0.35) {
				rotation = 0;
			} else {
				// rotation /= 2.0;
				if (rotation < 0) {
					rotation = -Utils.normalize(Math.abs(rotation), 0.35, 1) / 2.0;
				} else {
					rotation = Utils.normalize(rotation, 0.35, 1) / 2.0;
				}
			}

			double y = stick.getY();
			if (Math.abs(y) < 0.1) {
				y = 0;
			}
			double x = stick.getX();
			if (Math.abs(x) < 0.1) {
				x = 0;
			}
			drive(x * throttle, y * throttle, rotation);

		}
	}

	private void driveLeft(double speed) {
		robotDrive.mecanumDrive_Cartesian(-speed, 0.05, 0, 0);
	}

	private void driveRight(double speed) {
		robotDrive.mecanumDrive_Cartesian(speed, -0.05, 0, 0);
	}

	private void driveForward(double speed) {
		robotDrive.mecanumDrive_Cartesian(0, -speed, 0, 0);
	}

	private void driveBackward(double speed) {
		robotDrive.mecanumDrive_Cartesian(0, speed, 0, 0);
	}

	private void drive(double x, double y, double rotation) {
		robotDrive.mecanumDrive_Cartesian(x, y, rotation, 0);
	}

	private void rotateCW(double speed) {
		robotDrive.mecanumDrive_Cartesian(0, 0, speed, 0);
	}

	private void rotateCCW(double speed) {
		robotDrive.mecanumDrive_Cartesian(0, 0, -speed, 0);
	}

	private void stop() {
		robotDrive.mecanumDrive_Cartesian(0, 0, 0, 0);
	}

	private void humanPlayerStation() {
		if (stick.getPOV() == 270) {
			driveBackward(0.2);
			Timer.delay(0.5);
			stop();
		}
	}

	/*
	 * Gripper
	 */

	private void operateGripper() {
		// Grab and release
		// LB close
		if (xbox.getRawButton(Utils.LB)) {
			xbox.setRumble(RumbleType.kLeftRumble, 1.0f);
			closeGripper();
			Timer.delay(0.3);
			xbox.setRumble(RumbleType.kLeftRumble, 0.0f);
		}
		// RB open
		if (xbox.getRawButton(Utils.RB)) {
			xbox.setRumble(RumbleType.kRightRumble, 1.0f);
			openGripper();
			Timer.delay(0.3);
			xbox.setRumble(RumbleType.kRightRumble, 0.0f);
		}
	}

	private void closeGripper() {
		if (!gripper.get())
			gripper.set(true);
	}

	private void openGripper() {
		if (gripper.get())
			gripper.set(false);
	}

	private void controlCompressor() {
		if (xbox.getRawButton(Utils.START_BUTTON)) {
			compressorOff = true;
			compressor.stop();
		} else if (xbox.getRawButton(Utils.BACK_BUTTON)) {
			compressorOff = false;
			compressor.start();
		}
	}

	/*
	 * Pulley
	 */

	private void operatePulley() {
		double upSpeed = xbox.getRawAxis(Utils.RT);
		double downSpeed = xbox.getRawAxis(Utils.LT);
		if (upSpeed > Utils.TRIGGER_THRESHOLD) {
			// Stop compressor, set pulley motor to up value
			up(7 * upSpeed / 8.0);
		} else if (downSpeed > Utils.TRIGGER_THRESHOLD) {
			// Stop compressor, set pulley motor to down value
			down(5 * downSpeed / 7.0);
		} else {
			// Start compressor, shut down pulley motor
			stopPulley();
		}
	}

	private void up(double speed) {
		compressor.stop();
		pulleyControl.set(-speed);
	}

	private void down(double speed) {
		compressor.stop();
		pulleyControl.set(speed);
	}

	private void stopPulley() {
		pulleyControl.set(0.0);
		if (!compressorOff)
			compressor.start();
	}

	/**
	 * Runs during test mode
	 */
	public void test() {
		operatorControl();
	}
}

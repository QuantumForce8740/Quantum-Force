
package frc.robot;
import edu.wpi.first.wpilibj.TimedRobot;
//import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
// import edu.wpi.first.networktables.NetworkTable;
// import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
// import edu.wpi.first.wpilibj.Timer;
//import org.opencv.core.Mat;
//import org.opencv.imgproc.Imgproc;
//import org.opencv.video.CvSink;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
//import frc.robot.Apriltags;

public class Robot extends TimedRobot {

  private final PWMSparkMax left_motor_back = new PWMSparkMax(4);
  private final PWMSparkMax right_motor_back = new PWMSparkMax(1);
  private final PWMSparkMax left_motor_front = new PWMSparkMax(3);
  private final PWMSparkMax right_motor_front = new PWMSparkMax(2);
  private final MotorControllerGroup left_motor = new MotorControllerGroup(left_motor_back, left_motor_front);
  private final MotorControllerGroup right_motor = new MotorControllerGroup(right_motor_back, right_motor_front);
  private final DifferentialDrive drive = new DifferentialDrive(left_motor, right_motor);
  private final GenericHID control = new GenericHID(0);
  private final CANSparkMax upshooter = new CANSparkMax(1, MotorType.kBrushed);
  private final CANSparkMax middle_shooter = new CANSparkMax(2, MotorType.kBrushed);
  private final CANSparkMax downshooter = new CANSparkMax(3,MotorType.kBrushed);
  private final CANSparkMax intakedown = new CANSparkMax(4, MotorType.kBrushed);
  private final CANSparkMax intakeup = new CANSparkMax(5, MotorType.kBrushed);
  private final int buttonA = 1;
  private final int buttonB = 2;
  private final int buttonX = 3;
  private final int buttonY = 4;
  private final Apriltags AprilTgs = new Apriltags();
  DoublePublisher apriltag;
  double apriltag_ID = 0.0;

  @Override
  public void robotInit() {
    left_motor.setInverted(true);
    //CameraServer.startAutomaticCapture();
    intakeup.setInverted(true);
    AprilTgs.april_init();
    AprilTgs.apriltagVision();

    //Another option with apriltags (NetworkTables)
    NetworkTableInstance inst = NetworkTableInstance.getDefault();
    NetworkTable table = inst.getTable("datatable");
    apriltag = table.getDoubleTopic("apriltag").publish();

  }


  @Override
  public void robotPeriodic() {

  }


  @Override
  public void autonomousInit() {
    int tagID = AprilTgs.getCurrentID();
    System.out.println("ID: " + tagID);
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    int TagId = AprilTgs.getCurrentID();
    //Another option with NetworkTables
    //apriltag.set(TagId);

    if (TagId == 1){
      drive.arcadeDrive(0.5, 0);
    }
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {}
/** */
  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    drive.arcadeDrive(control.getRawAxis(1), control.getRawAxis(0));
    
    //SHOOTER.

    if (control.getRawButton(buttonA)){
      upshooter.set(-0.3);
      downshooter.set(-0.3);
    }

    if (control.getRawButton(buttonB)){

      upshooter.set(1);
      downshooter.set(1);
    }

    if (control.getRawButton(buttonX)){
      middle_shooter.set(0.5);
    }

    if (control.getRawButton(buttonY)){
      intakedown.set(1);
      intakeup.set(1);
    }

    
      }

  


  /** This function is called once when the robot is disabled. */
  // @Override
  // public void disabledInit() {}

  // /** This function is called periodically when disabled. */
  // @Override
  // public void disabledPeriodic() {}

  // /** This function is called once when test mode is enabled. */
  // @Override
  // public void testInit() {}

  // /** This function is called periodically during test mode. */
  // @Override
  // public void testPeriodic() {}

  // /** This function is called once when the robot is first started up. */
  // @Override
  // public void simulationInit() {}

  // /** This function is called periodically whilst in simulation. */
  // @Override
  // public void simulationPeriodic() {}
}


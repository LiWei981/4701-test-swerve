package swervelib.parser.json;

import com.revrobotics.SparkMaxRelativeEncoder.Type;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.SerialPort.Port;
import swervelib.encoders.AnalogAbsoluteEncoderSwerve;
import swervelib.encoders.SparkMaxEncoderSwerve;
import swervelib.encoders.SwerveAbsoluteEncoder;
import swervelib.imu.NavXSwerve;
import swervelib.imu.SwerveIMU;
import swervelib.motors.SparkMaxSwerve;
import swervelib.motors.SwerveMotor;

/**
 * Device JSON parsed class. Used to access the JSON data.
 */
public class DeviceJson
{

  /**
   * The device type, e.g. pigeon/pigeon2/sparkmax/talonfx/navx
   */
  public String type;
  /**
   * The CAN ID or pin ID of the device.
   */
  public int    id;
  /**
   * The CAN bus name which the device resides on if using CAN.
   */
  public String canbus = "";

  /**
   * Create a {@link SwerveAbsoluteEncoder} from the current configuration.
   *
   * @param motor {@link SwerveMotor} of which attached encoders will be created from, only used when the type is
   *              "attached" or "canandencoder".
   * @return {@link SwerveAbsoluteEncoder} given.
   */
  public SwerveAbsoluteEncoder createEncoder(SwerveMotor motor)
  {
    if (id > 40)
    {
      DriverStation.reportWarning("CAN IDs greater than 40 can cause undefined behaviour, please use a CAN ID below 40!",
                                  false);
    }
    switch (type)
    {
      case "none":
      case "integrated":
      case "attached":
        return null;
      case "thrifty":
      case "analog":
        return new AnalogAbsoluteEncoderSwerve(id);
      default:
        throw new RuntimeException(type + " is not a recognized absolute encoder type.");
    }
  }

  /**
   * Create a {@link SwerveIMU} from the given configuration.
   *
   * @return {@link SwerveIMU} given.
   */
  public SwerveIMU createIMU()
  {
    if (id > 40)
    {
      DriverStation.reportWarning("CAN IDs greater than 40 can cause undefined behaviour, please use a CAN ID below 40!",
                                  false);
    }
    switch (type)
    {

      case "navx_spi":
        return new NavXSwerve(SPI.Port.kMXP);
      case "navx_i2c":
        DriverStation.reportWarning(
            "WARNING: There exists an I2C lockup issue on the roboRIO that could occur, more information here: " +
            "\nhttps://docs.wpilib.org/en/stable/docs/yearly-overview/known-issues" +
            ".html#onboard-i2c-causing-system-lockups",
            false);
        return new NavXSwerve(I2C.Port.kMXP);
      case "navx_onborard":
        return new NavXSwerve(Port.kOnboard);
      case "navx_usb":
        return new NavXSwerve(Port.kUSB);
      case "navx_mxp":
      case "navx":
        return new NavXSwerve(Port.kMXP);
      case "pigeon":

      default:
        throw new RuntimeException(type + " is not a recognized absolute encoder type.");
    }
  }

  /**
   * Create a {@link SwerveMotor} from the given configuration.
   *
   * @param isDriveMotor If the motor being generated is a drive motor.
   * @return {@link SwerveMotor} given.
   */
  public SwerveMotor createMotor(boolean isDriveMotor)
  {
    if (id > 40)
    {
      DriverStation.reportWarning("CAN IDs greater than 40 can cause undefined behaviour, please use a CAN ID below 40!",
                                  false);
    }
    switch (type)
    {
     
      case "neo":
      case "sparkmax":
        return new SparkMaxSwerve(id, isDriveMotor);
      default:
        throw new RuntimeException(type + " is not a recognized absolute encoder type.");
    }
  }

  /**
   * Create a {@link SwerveAbsoluteEncoder} from the data port on the motor controller.
   *
   * @param motor The motor to create the absolute encoder from.
   * @return {@link SwerveAbsoluteEncoder} from the motor controller.
   */
  public SwerveAbsoluteEncoder createIntegratedEncoder(SwerveMotor motor)
  {
    switch (type)
    {
      case "sparkmax":
        return new SparkMaxEncoderSwerve(motor, 1);
      case "falcon":
      case "talonfx":
        return null;
    }
    throw new RuntimeException(
        "Could not create absolute encoder from data port of " + type + " id " + id);
  }
}

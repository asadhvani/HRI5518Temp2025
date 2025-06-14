package org.firstinspires.ftc.teamcode.Libs.AR.Archive;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;

import org.firstinspires.ftc.teamcode.Libs.AR.AR_Joint;

/**
 * This class create an AR_Arm object that is used to encapsulate all the code used to control and use
 * the 2024-2025 Aerospace Robotics Robot Arm.
 *
 * Instantiate that class for each AR_Arm (Currently just one in our design).
 *
 * Creation Date: 11/3/2024
 */


public class AR_Arm
{
    // Currently, the arm's rest is at approx. 43 degree up pointing straight down. That mean gravity is
    // working the most against the arm (horizontal) at -47 from the rest. So to make the angle align
    // with more realistic angles. the rest should be at 43 degree instead of 0.


    public static int FIRST_JOINT_REST_ANGLE = -40; //degrees

    // These variables are used to customize joint angles for the AR_Arm. All of these
    // variables are available to be adjusted, in real-time, using FTC Dashboard.
    public static int FIRST_JOINT_START = -40,      SECOND_JOINT_START = 0;
    public static int FIRST_JOINT_ACTIVE = -70,      SECOND_JOINT_ACTIVE = -65;
    public static int FIRST_JOINT_DEPLOY = -170, SECOND_JOINT_DEPLOY = -200;
    public static int FIRST_JOINT_GRAB = -70,    SECOND_JOINT_GRAB = -145;

    public static int start_X = 100,      start_Y = 15;
    public static int active_X = -70,      active_Y = -65;
    public static int grab_X = 32,    grab_Y = -15;

    public static double P1 = 0.003, I1 = 0.05, D1 = 0.0001;
    public static double F1 = 0.05;

    public static double P2 = 0.001, I2 = 0.05, D2 = 0.0001;
    public static double F2 = 0.05;

    public static int START = 0;
    public static int ACTIVE = 1;
    public static int GRAB = 2;
    public static int DEPLOY = 3;

    private final double L1 = 22.0; // Length of first arm segment
    private final double L2 = 16.0; // Length of second arm segment

    // Create a "AR_Joint" instance for each joint of the "AR_Arm".
    private AR_Joint jointFirst;
    private AR_Joint jointSecond;

    private CRServo leftGripper;
    private CRServo rightGripper;

    // Variables to save the desired angle of the two AR_JOINTs.
    private int targetFirst;
    private int targetSecond;

    private LinearOpMode bot;

    private int lastState = START;
    private int currentState = START;

    /**
     * Constructor. Gets the arm ready for moving.
     *
     * @param iBot Handle to the LinearOpMode.
     */
    public AR_Arm( LinearOpMode iBot )
    {
        // Take the passed in value and assign to class variables.
        this.bot = iBot;

        // Declare instances of the two joints.
        this.jointFirst = new AR_Joint(this.bot, "first_joint", P1, I1, D1, F1, false);
        this.jointSecond = new AR_Joint(this.bot, "second_joint", P2, I2, D2, F2,false);
        this.leftGripper = bot.hardwareMap.crservo.get("left_gripper");
        this.rightGripper = bot.hardwareMap.crservo.get("right_gripper");
    }

    /**
     * Return immediately and moves the joints to the desired location.
     */
    public void updateArmPos( )
    {
        // ToDo: I wonder if we need to come up with code to move the joints at different times. For example, maybe we have to move joint 1 20 degrees before moving joint 2 at all.
        // Arm should be tested before adding that code.
        this.jointFirst.moveJoint(this.targetFirst, currentState, lastState);
        this.jointSecond.moveJoint(this.targetSecond, currentState, lastState);
    }

    /**
     * Return immediately and sets the arm joint angles to a custom value.
     *
     * @param firstJoint The position of the first joint in degrees.
     * @param secondJoint The position of the second joint in degrees.
     */
    public void setArmCustomPos(double firstJoint, double secondJoint )
    {
        this.targetFirst = (int)firstJoint; //degrees
        this.targetSecond = (int)secondJoint; //degrees
    }

    /**
     * Return immediately and sets the arm joint angles to the preset location for deploying a specimen into the upper hopper.
     */
    public void setArmDeployPos( ) {
        // Todo: This needs to be carefully tested before we run the code to make sure the motor direction is correct, etc.
        
        this.targetFirst = FIRST_JOINT_DEPLOY;
        this.targetSecond = SECOND_JOINT_DEPLOY;

        if (currentState != AR_Arm.DEPLOY) {
            lastState = currentState;
            currentState = AR_Arm.DEPLOY;
        }
    }

    /**
     * Return immediately and ets the arm joint angles to the preset location for picking up a specimen.
     */
    public void setArmGrabPos( )
    {
        // Todo: This needs to be carefully tested before we run the code to make sure the motor direction is correct, etc.
        double[] angles = calculateJointAngles(grab_X, grab_Y);

        this.targetFirst = (int) angles[0];
        this.targetSecond = (int) angles[1];

        if( currentState != AR_Arm.GRAB ) {
            lastState = currentState;
            currentState = AR_Arm.GRAB;
        }
    }

    /**
     * Return immediately and sets the arm joint angles to the preset ready to travel position.
     */
    public void setArmActivePos( )
    {
        // Todo: This needs to be carefully tested before we run the code to make sure the motor direction is correct, etc.
        double[] angles = calculateJointAngles(active_X, active_Y);

        this.targetFirst = (int) angles[0];
        this.targetSecond = (int) angles[1];

        if( currentState != AR_Arm.ACTIVE ) {
            lastState = currentState;
            currentState = AR_Arm.ACTIVE;
        }


        // Todo: Somehow the power should be set to zero after movement because we don't want to waste battery power holding
        // the arm in the lowered position. This will only work if the arm has a rest which it doesn't right now.
    }

    /**
     * Return immediately and sets the arm joint angles to the start position.
     */
    public void setArmStartPos( )
    {
        // Todo: This needs to be carefully tested before we run the code to make sure the motor direction is correct, etc.
        double[] angles = calculateJointAngles(start_X, start_Y);

        this.targetFirst = (int) angles[0];
        this.targetSecond = (int) angles[1];

        if( currentState != AR_Arm.START ) {
            lastState = currentState;
            currentState = AR_Arm.START;
        }
    }

    public void grab( )
    {
        // Todo: This needs to be carefully tested before we run the code to make sure the motor direction is correct, etc.
        leftGripper.setPower(1);
        rightGripper.setPower(1);
        }
    public void drop( )
    {
        // Todo: This needs to be carefully tested before we run the code to make sure the motor direction is correct, etc.
        leftGripper.setPower(-1);
        rightGripper.setPower(-1);
    }
    public void rest( )
    {
        // Todo: This needs to be carefully tested before we run the code to make sure the motor direction is correct, etc.
        leftGripper.setPower(0);
        rightGripper.setPower(0);
    }

    public double[] calculateJointAngles(double x, double y) {

        double distance = Math.sqrt(x * x + y * y);
        // Check if the target is reachable
        if (distance > (L1 + L2) || distance < Math.abs(L1 - L2)){
            return null; // Target is unreachable
        }

        // Law of cosines for elbow angle
        double cosTheta2 = (x * x + y * y - L1 * L1 - L2 * L2) / (2 * L1 * L2);
        if (cosTheta2 < -1 || cosTheta2 > 1) {
            return null;
        }

        double theta2 = 0;
        try {
            theta2 = Math.acos(cosTheta2); // Elbow angle in radians
        } catch(Exception e){
            theta2 = 35.51;
        }

        // Law of cosines and trigonometry for shoulder angle

        double theta1 = Math.atan2(y, x) - Math.atan2(L2 * Math.sin(theta2), L1 + L2 * Math.cos(theta2));



        // Convert radians to degrees

        double shoulderAngle = Math.toDegrees(theta1);

        double elbowAngle = Math.toDegrees(theta2);

        return new double[] { shoulderAngle, elbowAngle };

    }
}
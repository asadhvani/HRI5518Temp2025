package org.firstinspires.ftc.teamcode.TeleOp.Test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.Libs.AR.AR_Arm_Fisher;

@TeleOp(name = "TeleOp_Arm_Test", group = "TeleOp")
public class TeleOp_Arm_Test extends LinearOpMode
{
    private AR_Arm_Fisher arm;

    //@Override
    public void runOpMode()
    {
        // Initialize the arm
        arm = new AR_Arm_Fisher(this);

        waitForStart();
        if (isStopRequested()) return;

        while (opModeIsActive())
        {
            //**************************************************************************************
            // -------------------- Gamepad 1 Controls ---------------------------------------------

            //**************************************************************************************
            // -------------------- Gamepad 2 Controls ---------------------------------------------
            if (gamepad2.triangle) {
                arm.setArmDeployPos();
                telemetry.addData("Arm Status", "Set Arm Deploy");
            }
            else if (gamepad2.square) {
                arm.setArmDeploy1Pos();
                telemetry.addData("Arm Status", "Set Arm Deploy");
            }
            else if (gamepad2.dpad_right) {
                arm.setArmActivePos();
                telemetry.addData("Arm Status", "Set Arm Active");
            }
            else if (gamepad2.dpad_left) {
                arm.setArmStartPos();
                telemetry.addData("Arm Status", "Set Arm Start");
            }
            else if (gamepad2.dpad_down) {
                arm.setArmGrabPos();
                telemetry.addData("Arm Status", "Set Arm Grab");
            }

            //**************************************************************************************
            // -------------------- Robot Systems Updates ------------------------------------------
            arm.updateArmPos();

            //**************************************************************************************
            //--------------------- TELEMETRY Code -------------------------------------------------
            // Useful telemetry data in case needed for testing and to find heading of robot
            arm.getTelemetry();
            telemetry.update();
        }
    }
}
/*
Copyright (c) 2016 Robert Atkinson

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Robert Atkinson nor the names of his contributors may be used to
endorse or promote products derived from this software without specific prior
written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESSFOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.LegacyModule;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a PushBot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="Test Linear Op", group="Linear Opmode")  // @Autonomous(...) is the other common choice

public class TeleOpLinear extends LinearOpMode {

    /* Declare OpMode members. */
    private ElapsedTime runtime = new ElapsedTime();

    private DcMotor leftMotor = null;
    private DcMotor rightMotor = null;
    private DcMotor zipMotor = null;
    private DcMotor kickMotor = null;
    private DcMotorController motorCtrl = null;
    private DcMotorController motorCtrl2 = null;
    private ServoController servoCtrl = null;
    private LegacyModule legacyMod = null;

    static final double LAUNCH_SPEED = 100;
    static final double RETURN_SPEED = .1;
    private boolean a = false;

    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        /* eg: Initialize the hardware variables. Note that the strings used here as parameters
         * to 'get' must correspond to the names assigned during the robot configuration
         * step (using the FTC Robot Controller app on the phone).
         */

        motorCtrl = hardwareMap.dcMotorController.get("motorcycle");
        motorCtrl2 = hardwareMap.dcMotorController.get("motorcycle2");
        zipMotor = hardwareMap.dcMotor.get("zip motor");
        kickMotor = hardwareMap.dcMotor.get("kick motor");
        leftMotor = hardwareMap.dcMotor.get("left motor");
        rightMotor = hardwareMap.dcMotor.get("right motor");
        servoCtrl = hardwareMap.servoController.get("servo");
        legacyMod = hardwareMap.legacyModule.get("legacy module");

        kickMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // eg: Set the drive motor directions:
        // "Reverse" the motor that runs backwards when connected directly to the battery
        // leftMotor.setDirection(DcMotor.Direction.FORWARD); // Set to REVERSE if using AndyMark motors
        // rightMotor.setDirection(DcMotor.Direction.REVERSE);// Set to FORWARD if using AndyMark motors

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();
        kickMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        kickMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            // eg: Run wheels in tank mode (note: The joystick goes negative when pushed forwards)
            leftMotor.setPower(-gamepad1.left_stick_y);
            rightMotor.setPower(gamepad1.right_stick_y);


            //Make the ziptie motor usable
            if (gamepad1.right_trigger > 0) zipMotor.setPower(gamepad1.right_trigger);
            else if (gamepad1.left_trigger > 0) zipMotor.setPower(-gamepad1.left_trigger);
            else zipMotor.setPower(0);

            //kickMotor
            if (gamepad1.right_bumper && !a) {
                this.a = true;
                kickMotor.setTargetPosition(kickMotor.getCurrentPosition() - 200);
                while(Math.abs(kickMotor.getCurrentPosition()-kickMotor.getTargetPosition())>10) {
                    kickMotor.setPower(LAUNCH_SPEED);
                }
                kickMotor.setPower(0);
                this.a = false;
            }
            /*
            else if (gamepad1.left_bumper) {
                while(Math.abs(kickMotor.getCurrentPosition()-kickMotor.getTargetPosition())>10) {
                    kickMotor.setTargetPosition(kickMotor.getCurrentPosition() + 220);
                    kickMotor.setPower(RETURN_SPEED);
                }
                kickMotor.setPower(0);
            }
            */

            telemetry.addData("Target Position", ":" + kickMotor.getTargetPosition());
            telemetry.addData("Current Position", ":" + kickMotor.getCurrentPosition());
            telemetry.addData("Status", "Run Time: " + runtime.toString(), "\n");
            telemetry.update();
            idle(); // Let hardware catch up
        }
    }
}

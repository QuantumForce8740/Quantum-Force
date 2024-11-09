package frc.robot;

import edu.wpi.first.apriltag.AprilTagDetection;
import edu.wpi.first.apriltag.AprilTagDetector;
import edu.wpi.first.apriltag.AprilTagPoseEstimator;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.CvSink;
import edu.wpi.first.cscore.CvSource;
import edu.wpi.first.cscore.UsbCamera;
// import edu.wpi.first.math.geometry.Rotation3d;
// import edu.wpi.first.math.geometry.Transform3d;
// import edu.wpi.first.networktables.IntegerArrayPublisher;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
// import java.util.ArrayList;
import org.opencv.core.Mat;
// import org.opencv.core.Point;
// import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class Apriltags{
    private int currentID = 0;
    public final void april_init(){
        var vision = new Thread(this::apriltagVision);
        vision.setDaemon(true);
        vision.start();
    }

    public int getCurrentID(){
        return currentID;
    }

    public void apriltagVision(){
        var detector = new AprilTagDetector();
        detector.addFamily("tag36h11", 1);
        var poseEstimator = new AprilTagPoseEstimator.Config(
            0.1651, 699.3778103158814, 677.7161226393544, 345.6059345433618, 207.12741326228522
        );
        var estimator = new AprilTagPoseEstimator(poseEstimator);
        System.out.println("Estimator: " + estimator);
        UsbCamera camera = CameraServer.startAutomaticCapture();
        camera.setResolution(640, 480);
        CvSink cvSink = CameraServer.getVideo();
        CvSource cvsource = CameraServer.putVideo("Detector", 640, 480);

        var mat = new Mat();
        var gray_mat = new Mat();

        // ArrayList<Long> tags = new ArrayList<>();
        // var Outlinecolor = new Scalar(0,255,0);
        // var crossColor = new Scalar(0,0,255);

        NetworkTable tagsTable = NetworkTableInstance.getDefault().getTable("apriltags");
        // IntegerArrayPublisher pubTags = tagsTable.getIntegerArrayTopic("tags").publish();

        while (!Thread.interrupted()){
            if (cvSink.grabFrame(mat) == 0){
                cvsource.notifyError(cvSink.getError());
                continue;
            }
            Imgproc.cvtColor(mat, gray_mat, Imgproc.COLOR_RGB2GRAY);
            AprilTagDetection[] detections=detector.detect(gray_mat);
            if (detections.length > 0){
                AprilTagDetection detection = detections[0];
                int tagID = detection.getId();
                tagsTable.getEntry("tag_id").setNumber(tagID);
                currentID = tagID;
            }
            cvsource.putFrame(mat);
            // for (AprilTagDetection detection : detections) {
            //     tags.add((long) detection.getId());
        
            //     for (var i = 0; i <= 3; i++) {
            //       var j = (i + 1) % 4;
            //       var pt1 = new Point(detection.getCornerX(i), detection.getCornerY(i));
            //       var pt2 = new Point(detection.getCornerX(j), detection.getCornerY(j));
            //       Imgproc.line(mat, pt1, pt2, Outlinecolor, 2);
            //     }
        
            //     var cx = detection.getCenterX();
            //     var cy = detection.getCenterY();
            //     var ll = 10;
            //     Imgproc.line(mat, new Point(cx - ll, cy), new Point(cx + ll, cy), crossColor, 2);
            //     Imgproc.line(mat, new Point(cx, cy - ll), new Point(cx, cy + ll), crossColor, 2);

            //     Imgproc.putText(mat, Integer.toString(detection.getId()), new Point(cx+ll, cy), Imgproc.FONT_HERSHEY_COMPLEX, 1, crossColor, 3);
            //     Transform3d pose = estimator.estimate(detection);
            //     Rotation3d rot = pose.getRotation();
            //     tagsTable.getEntry("pose_" + detection.getId()).setDoubleArray(new double[]{
            //         pose.getX(), pose.getY(), pose.getZ(), rot.getX(), rot.getY(), rot.getZ()
            //     });
            // }
            // pubTags.set(tags.stream().mapToLong(Long::longValue).toArray());
            // cvsource.putFrame(mat);

        }
        detector.close();
    }
}

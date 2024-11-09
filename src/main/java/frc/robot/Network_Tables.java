package frc.robot;

import edu.wpi.first.networktables.DoubleTopic;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.Topic;

public class Network_Tables {
    NetworkTableInstance inst = NetworkTableInstance.getDefault();
    NetworkTable table = inst.getTable("datatable");

    
// get a topic from a NetworkTableInstance
// the topic name in this case is the full name
    DoubleTopic dlb_topic = inst.getDoubleTopic("/datatable/X");

    // get a topic from a NetworkTable
// the topic name in this case is the name within the table;
// this line and the one above reference the same topic
    DoubleTopic dbl_topic = table.getDoubleTopic("X");

    Topic generTopic =inst.getTopic("/datatable/X");
    DoubleTopic bld_topic = new DoubleTopic(generTopic);

}

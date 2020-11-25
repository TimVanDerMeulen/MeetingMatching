package data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class AttendeeList extends ArrayList<Attendee> {

    public AttendeeList() {
        super();
    }

    public AttendeeList(Collection<? extends Attendee> c) {
        super(c);
    }

    public void add(String name, String group) {
        this.add(new Attendee(name, group));
    }

    public Attendee find(String name) {
        return this.stream().filter(attendee -> name.equals(attendee.getName())).findFirst().orElseGet(() -> null);
    }

    public List<Attendee> getAllFromGroup(String group) {
        return this.stream().filter(attendee -> group.equals(attendee.getGroup())).collect(Collectors.toList());
    }

    public List<Attendee> getAllFromOtherGroups(String group) {
        return this.stream().filter(attendee -> !group.equals(attendee.getGroup())).collect(Collectors.toList());
    }

}

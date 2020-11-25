import data.AttendeeList;
import org.junit.Test;
import service.MatchService;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class ServiceTest {

    private final MatchService matchService = new MatchService();

    private String[] names = getStrings(50, "Name ");
    private String[] groups = getStrings(50, "Group ");

    @Test
    public void noWorking() {
        Map<String, List<String>> data = freshData(2);

        AttendeeList attendees = new AttendeeList();
        attendees.add(names[0], groups[0]);
        attendees.add(names[1], groups[0]);

        assertFalse(matchService.calcAndApplyNextRunRecurse(data, attendees));
    }

    @Test
    public void minimalWorking() {
        Map<String, List<String>> data = freshData(2);

        AttendeeList attendees = new AttendeeList();
        attendees.add(names[0], groups[0]);
        attendees.add(names[1], groups[1]);

        assertTrue(matchService.calcAndApplyNextRunRecurse(data, attendees));

        validateSize(data, 1);

        validateMatches(data, attendees, false);
    }

    @Test
    public void minimalExtraRun() {
        Map<String, List<String>> data = freshData(3);

        AttendeeList attendees = new AttendeeList();
        attendees.add(names[0], groups[0]);
        attendees.add(names[1], groups[0]);
        attendees.add(names[2], groups[1]);

        assertTrue(matchService.calcAndApplyNextRunRecurse(data, attendees));

        validateSize(data, 2);

        validateMatches(data, attendees, true);
    }

    private Map<String, List<String>> freshData(int amount) {
        Map<String, List<String>> data = new HashMap<>();
        for (int i = 0; i < amount; i++)
            data.put(names[i], null);

        return data;
    }

    //private AttendeeList randomAttendeeGroups(Collection<String> attendeeNames, int amountGroups) {
    //    AttendeeList attendees = new AttendeeList();
    //    for (String name : attendeeNames)
    //        attendees.add(name, null);
//
//
//
    //    return attendees;
    //}

    private void validateSize(Map<String, List<String>> data, int expectedSize) {
        for (Map.Entry<String, List<String>> entry : data.entrySet())
            assertEquals(expectedSize, entry.getValue().size());
    }

    private void validateMatches(Map<String, List<String>> data, AttendeeList attendees, boolean hasExtraRun) {
        int extraRunCount = 0;
        for (String target : data.keySet()) {
            List<String> matches = data.get(target);
            for (String match : matches) {
                assert data.get(match).contains(target);
                if (attendees.find(target).getGroup().equals(attendees.find(match).getGroup())) {
                    extraRunCount++;
                    assertTrue(hasExtraRun);
                }
            }
        }
        if (!hasExtraRun)
            assertEquals(0, extraRunCount);
        assertEquals(hasExtraRun ? 2 : 1, data.entrySet().stream().findFirst().get().getValue().size());
    }

    //private String[] getRandomStrings(int amount) {
    //    String[] res = new String[amount];
    //    for (int i = 0; i < amount; i++)
    //        res[i] = UUID.randomUUID().toString();
    //    return res;
    //}

    private String[] getStrings(int amount, String prefix) {
        String[] res = new String[amount];
        for (int i = 0; i < amount; i++)
            res[i] = prefix + i;
        return res;
    }

}

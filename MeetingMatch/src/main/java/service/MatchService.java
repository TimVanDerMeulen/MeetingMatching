package service;

import data.Attendee;
import data.AttendeeList;

import java.util.*;
import java.util.stream.Collectors;

public class MatchService {

    public void addNextRun(Map<String, List<String>> data, AttendeeList attendees) {
        if (!calcAndApplyNextRunRecurse(data, attendees))
            ErrorService.error(new RuntimeException("Not possilbe!"), "Es war nicht möglich einen weiteren Durchlauf zu berechnen: ");
    }

    public boolean calcAndApplyNextRunRecurse(Map<String, List<String>> data, AttendeeList attendees) {
        return calcAndApplyNextRunRecurse(new HashMap<>(), data, new AttendeeList(attendees), attendees);
    }

    private boolean calcAndApplyNextRunRecurse(Map<String, String> currentState, Map<String, List<String>> data, AttendeeList remaining, AttendeeList attendees) {
        if (remaining.isEmpty()) {
            apply(currentState, data);
            return true;
        }
        Attendee target = remaining.get(0);
        if (remaining.size() == 1) {
            List<String> possibleMatches = getPossibleMatches(target, attendees, data, new ArrayList<>());
            String match = possibleMatches.get((int) (Math.random() * possibleMatches.size()));
            String matchMatch = currentState.get(match);

            currentState.put(target.getName(), match);

            Map<String, String> extraRun = new HashMap<>();
            extraRun.put(target.getName(), matchMatch);
            extraRun.put(match, target.getName());
            extraRun.put(matchMatch, target.getName());

            calcAndApplyNextRunRecurse(currentState, data, new AttendeeList(), attendees);
            calcAndApplyNextRunRecurse(extraRun, data, new AttendeeList(), attendees);
            return true;
        }

        List<String> possibleMatches = getPossibleMatches(target, attendees, data, currentState.keySet());

        Map<String, String> run;
        AttendeeList runRemainingAttendees;
        do {
            System.out.println(target.getName() + ": " + possibleMatches);
            //System.out.println(new Gson().toJson(currentState));
            if (possibleMatches.isEmpty())
                return false;

            String match = possibleMatches.get(0);
            Attendee attendee = attendees.find(match);

            run = deepCopy(currentState);
            runRemainingAttendees = new AttendeeList(remaining);

            run.put(target.getName(), match);
            run.put(match, target.getName());

            runRemainingAttendees.remove(target);
            runRemainingAttendees.remove(attendee);
            possibleMatches.remove(match);
        } while (!calcAndApplyNextRunRecurse(run, data, runRemainingAttendees, attendees));

        return true;
    }

    public boolean validateMatches(Map<String, List<String>> data, AttendeeList attendees) {
        for (String target : data.keySet()) {
            List<String> matches = data.get(target);
            for (String match : matches) {
                if (!data.get(match).contains(target))
                    return false;
            }
        }
        return true;
    }

    private <T> Map<String, T> deepCopy(Map<String, T> toCopy) {
        Map<String, T> res = new HashMap<>();
        for (Map.Entry<String, T> entry : toCopy.entrySet())
            res.put(entry.getKey(), entry.getValue() instanceof List ? (T) new ArrayList((Collection) entry.getValue()) : entry.getValue());
        return res;
    }

    private void apply(Map<String, String> matching, Map<String, List<String>> data) {
        for (String currentKey : matching.keySet())
            if (data.get(currentKey) == null)
                data.put(currentKey, new ArrayList<>());

        for (String key : data.keySet()) {
            String val = matching.get(key);
            data.get(key).add(val != null ? val : "-");
        }
    }

    private List<String> getPossibleMatches(Attendee target, AttendeeList attendees, Map<String, List<String>> data, Collection<String> alreadyMatched) {
        List<String> exclude = new ArrayList<>(alreadyMatched);
        List<String> old = data.get(target.getName());
        exclude.addAll(old != null && !old.isEmpty() ? old : new ArrayList<>());
        return attendees.getAllFromOtherGroups(target.getGroup()).stream() // filter same group
                .map(attendee -> attendee.getName()).filter(s -> !s.equals(target.getName()) && !exclude.contains(s)) // filter already matched
                .collect(Collectors.toList());
    }

}

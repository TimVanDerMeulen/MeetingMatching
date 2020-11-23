package service;

import data.Attendee;
import data.AttendeeList;

import java.util.*;
import java.util.stream.Collectors;

public class MatchService {

    public void addNextRun(Map<String, List<String>> data, AttendeeList attendees) {
        Map<String, String> alreadyMatched = new HashMap();
        Map<String, String> unevenRun = new HashMap();
        List<String> todo = new ArrayList<>(data.keySet());

        int retries = attendees.size();
        while (retries >= 0 && !todo.isEmpty()) {
            retries--;
            for (Map.Entry<String, List<String>> entry : data.entrySet()) {
                if (alreadyMatched.get(entry.getKey()) != null)
                    continue;

                Attendee attendee = attendees.find(entry.getKey());
                List<String> possibleMatches = getPossibleMatches(attendee, attendees, data, alreadyMatched.keySet());

                boolean switchMatch = possibleMatches.isEmpty();

                if (switchMatch)
                    possibleMatches = getPossibleMatches(attendee, attendees, data, new ArrayList<>());

                String match = possibleMatches.get((int) (Math.random() * possibleMatches.size()));

                String matchOfMatch = alreadyMatched.get(match);
                if (todo.size() == 1) {
                    // last one of uneven amount of attendees
                    alreadyMatched.put(entry.getKey(), match);

                    unevenRun.put(entry.getKey(), matchOfMatch);
                    unevenRun.put(match, entry.getKey());
                    unevenRun.put(matchOfMatch, entry.getKey());

                    todo.clear();
                    break;
                }
                if (switchMatch) {
                    String kicked = matchOfMatch;
                    alreadyMatched.remove(kicked);
                    todo.add(kicked);
                }

                alreadyMatched.put(entry.getKey(), match);
                alreadyMatched.put(match, entry.getKey());

                todo.remove(entry.getKey());
                todo.remove(match);
            }
        }

        if (retries < 0) {
            ErrorService.error(new RuntimeException("No retries left! Could not calculate next run"), "Fehler während der Berechnung: ");
            return;
        }

        // apply next run
        apply(alreadyMatched, data);

        // apply uneven run if needed
        if (!unevenRun.isEmpty())
            apply(unevenRun, data);
    }

    private void apply(Map<String, String> matching, Map<String, List<String>> data) {
        for (String key : data.keySet()) {
            String val = matching.get(key);
            data.get(key).add(val != null ? val : "-");
        }
    }

    private List<String> getPossibleMatches(Attendee target, AttendeeList attendees, Map<String, List<String>> data, Collection<String> alreadyMatched) {
        List<String> exclude = new ArrayList<>(alreadyMatched);
        exclude.addAll(data.get(target.getName()));
        return attendees.getAllFromOtherGroups(target.getGroup()).stream() // filter same group
                .map(attendee -> attendee.getName()).filter(s -> !s.equals(target) && !exclude.contains(s)) // filter already matched
                .collect(Collectors.toList());
    }

}

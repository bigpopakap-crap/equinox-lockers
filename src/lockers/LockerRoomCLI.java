package lockers;

import java.util.*;
import java.util.stream.Collectors;

public class LockerRoomCLI {

  private static final String HELP_TEXT = "(Should be list, groups, request, reserve, release, broken or unbroken)";

  private final LockerRoom room;

  public LockerRoomCLI(LockerRoom room) {
    this.room = room;
  }

  public String handleInput(String input) {
    String[] split = input.split(":");
    if (split.length < 1) {
      return "Enter a command. " + HELP_TEXT;
    }
    String command = split[0];

    if ("list".equals(command)) {
      return list();
    } else if ("groups".equals(command)) {
      return groups();
    } else if ("request".equals(command)) {
      if (split.length < 2) {
        return "Enter a locker number to request";
      }
      String arg = split[1];

      return request(arg);
    } else if ("reserve".equals(command)) {
      if (split.length < 2) {
        return "Enter a locker number to reserve";
      }
      String arg = split[1];

      return reserve(arg);
    } else if ("release".equals(command)) {
      if (split.length < 2) {
        return "Enter a locker number to release";
      }
      String arg = split[1];

      return release(arg);
    } else if ("broken".equals(command)) {
      if (split.length < 2) {
        return "Enter a locker number to take out of service";
      }
      String arg = split[1];

      return takeOutOfService(arg);
    } else if ("unbroken".equals(command)) {
      if (split.length < 2) {
        return "Enter a locker number to put back in service";
      }
      String arg = split[1];

      return putInService(arg);
    } else {
      return "Invalid command. " + HELP_TEXT;
    }
  }

  private String list() {
    List<Locker> lockers = new ArrayList<>(room.getAllLockers().getLockers());
    Collections.sort(lockers, (o1, o2) -> o1.getNumber() - o2.getNumber());

    StringBuilder response = new StringBuilder();
    lockers.forEach(locker -> response.append(locker).append('\n'));
    return response.toString();
  }

  public String groups() {
    List<LockerGroup> groups = room.getLockerRequestGroups().stream()
                        .map(requestGroup -> requestGroup.getPrimaryGroup())
                        .distinct()
                        .collect(Collectors.<LockerGroup>toList());
    Collections.sort(groups, (o1, o2) -> o1.getName().compareTo(o2.getName()));

    StringBuilder response = new StringBuilder();
    groups.forEach(lockerGroup -> {
      List<Locker> lockers = new ArrayList<>(lockerGroup.getLockers());
      Collections.sort(lockers, (o1, o2) -> o1.getNumber() - o2.getNumber());

      response.append(lockerGroup.getName())
                    .append(" (").append(lockers.size()).append(')')
                    .append('\n');
      lockers.forEach(locker -> response.append(locker).append('\n'));

      response.append('\n');
    });
    return response.toString();
  }

  private String request(String requestGroup) {
    Optional<LockerRequestGroup> optRequestedGroup = room.getLockerRequestGroups().stream()
                                                    .filter(lockerGroupProximityList -> lockerGroupProximityList.getPrimaryGroup().getName().equals(requestGroup))
                                                    .findFirst();
    if (optRequestedGroup.isPresent()) {
      LockerRequest request = new LockerRequest(optRequestedGroup.get());

      List<LockerSuggestion> suggestions = room.suggestLockers(request);
      if (suggestions.isEmpty()) {
        return "No suggestions";
      }

      StringBuilder response = new StringBuilder();
      suggestions.forEach(lockerSuggestion -> response.append(lockerSuggestion).append('\n'));
      return response.toString();
    } else {
      return "Bad input. No such group found";
    }
  }

  private String reserve(String lockerNumberInput) {
    int lockerNumber;
    try {
      lockerNumber = Integer.parseInt(lockerNumberInput);
    } catch (NumberFormatException ex) {
      return lockerNumberInput + " is not a number";
    }

    try {
      this.room.reserve(lockerNumber);
      return "Reserved locker " + lockerNumber;
    } catch (LockerUnavailableException e) {
      return "Locker unavailable, or number out of range";
    }
  }

  private String release(String lockerNumberInput) {
    int lockerNumber;
    try {
      lockerNumber = Integer.parseInt(lockerNumberInput);
    } catch (NumberFormatException ex) {
      return lockerNumberInput + " is not a number";
    }

    try {
      this.room.release(lockerNumber);
      return "Released locker " + lockerNumber;
    } catch (LockerUnavailableException e) {
      return "Locker unavailable, or number out of range";
    }
  }

  private String takeOutOfService(String lockerNumberInput) {
    int lockerNumber;
    try {
      lockerNumber = Integer.parseInt(lockerNumberInput);
    } catch (NumberFormatException ex) {
      return lockerNumberInput + " is not a number";
    }

    try {
      this.room.takeOutOfService(lockerNumber);
      return "Took locker " + lockerNumber + " out of service";
    } catch (LockerUnavailableException e) {
      return "Locker unavailable, or number out of range";
    }
  }

  private String putInService(String lockerNumberInput) {
    int lockerNumber;
    try {
      lockerNumber = Integer.parseInt(lockerNumberInput);
    } catch (NumberFormatException ex) {
      return lockerNumberInput + " is not a number";
    }

    try {
      this.room.putInService(lockerNumber);
      return "Put locker " + lockerNumber + " back in service";
    } catch (LockerUnavailableException e) {
      return "Locker unavailable, or number out of range";
    }
  }

}
